/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *  shall mention that this file has been edited.
 *  By adding a new header (at the bottom of this header)
 *  with the word "Editor" on top of it.
 */
package cufy.text;

import cufy.beans.Invoke;
import cufy.beans.StaticMethod;
import cufy.lang.Type;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An abstract class for text formatting classes. Used to simplify the formatter and make it more inheritable. lso making the inheriting for adding
 * some futures more easier. This uses the {@link Invoke} class. And the methods in this will be invoked using the dynamic method grouping algorithm.
 * In order to add a method on a dynamic method group. The method should be annotated with that group annotation. (see {@link Invoke}). Also the
 * method should match the conditions of that group to avoid parameters/output mismatches. This abstract have three methods group {@link
 * ClassifyMethod}, {@link ParseMethod} and {@link FormatMethod}. And each of them have it's own conditions.
 *
 * @author LSaferSE
 * @version 7 release (23-Jan-2020)
 * @implNote you have to navigate this class to where your dynamic methods is. By using annotations.
 * @since 28-Sep-19
 */
public abstract class Format extends Invoke {
	/**
	 * Query what's the suitable class for the given sequence.
	 *
	 * @param sequence to query a suitable class for
	 * @return the suitable class for the given sequence. Or null if unknown.
	 * @throws ParseException       when any classifying error occurs
	 * @throws NullPointerException if the given 'sequence' is null
	 * @throws IOError              when any IOException occurred
	 */
	@StaticMethod
	public Class<?> classify(CharSequence sequence) {
		Objects.requireNonNull(sequence, "sequence");

		try {
			return this.classify(new StringReader(sequence.toString()), null);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	/**
	 * Query what's the suitable class from the text given by the given 'reader'. Using all methods with {@link ClassifyMethod} on this formatter.
	 *
	 * @param reader   to read the sequence from
	 * @param position to classify depending on (null to create a new position)
	 * @return the suitable class for the given string.
	 * @throws ParseException       when any classifying error occurs
	 * @throws IOException          if any I/O exception occurs
	 * @throws NullPointerException if the given reader is null
	 * @apiNote this may invoke {@link Reader#mark} on the given reader
	 */
	@StaticMethod
	public Class<?> classify(Reader reader, ParsePosition position) throws IOException {
		Objects.requireNonNull(reader, "reader");

		if (position == null)
			position = this.newParsePosition();

		for (Method method : this.getClassifyMethods())
			if (this.classify0(method, reader, position))
				return method.getAnnotation(ClassifyMethod.class).value();
		return this.classifyElse(reader, position);
	}

	/**
	 * Format the given object into a text on a string.
	 *
	 * @param object the object to be formatted
	 * @return the formatted string
	 * @throws FormatException when any formatting error occurs
	 * @throws IOError         when any IOException occurred
	 */
	@StaticMethod
	public String format(Object object) {
		StringWriter writer = new StringWriter();

		try {
			this.format(writer, object, null, null);
		} catch (IOException e) {
			throw new IOError(e);
		}

		return writer.toString();
	}
	/**
	 * Format the given object. Writing it to the given writer. Using the first method that have been annotated with {@link FormatMethod}. And allows
	 * the given class. (methods are ordered randomly).
	 *
	 * @param object   the object ot be formatted
	 * @param writer   to write the formatter string to
	 * @param position to format depending on (null to create a new one)
	 * @param klass    the targeted method parameter type (null for the class of the given object)
	 * @throws FormatException      when any formatting errors occurs
	 * @throws NullPointerException if the given 'writer' is null
	 * @throws IOException          if any I/O exception occurs
	 */
	@StaticMethod
	public void format(Writer writer, Object object, FormatPosition position, Class<?> klass) throws IOException {
		Objects.requireNonNull(writer, "writer");

		if (position == null)
			position = this.newFormatPosition();
		if (klass == null)
			klass = object == null ? Void.class : object.getClass();

		Method method = this.getFormatMethod(klass);

		if (method == null)
			this.formatElse(writer, object, position);
		else this.format0(method, writer, object, position);
	}

	/**
	 * Parse an object from the given sequence.
	 *
	 * @param sequence to parse the object from
	 * @return parsed object from the given sequence
	 * @throws ParseException       when any parsing error occurs
	 * @throws NullPointerException if the given sequence is null
	 * @throws IOError              when any IOException occurred
	 */
	@StaticMethod
	public Object parse(CharSequence sequence) {
		Objects.requireNonNull(sequence, "sequence");

		AtomicReference<Object> buffer = new AtomicReference<>();
		Reader reader = new StringReader(sequence.toString());

		try {
			this.parse(buffer, reader, null, null);
		} catch (IOException e) {
			throw new IOError(e);
		}

		return buffer.get();
	}
	/**
	 * Parse the given sequence to an object. Then store that object to the given buffer. Using the first method that have been annotated with {@link
	 * ParseMethod}. And allows the given class. (methods are ordered randomly).
	 *
	 * @param reader   to read the sequence form
	 * @param buffer   to store the parsed object to. While parsing
	 * @param position to parse the given sequence depending on (null to create a new one)
	 * @param klass    the targeted method output type (null for classifying the given sequence dynamically)
	 * @throws ParseException       when any parsing error occurs
	 * @throws NullPointerException if the given 'reader' or 'buffer' is null
	 * @throws IOException          if any I/O exception occurs
	 * @apiNote this may invoke {@link Reader#mark} on the given reader
	 * @implSpec if the given buffer is not empty. Do not replace it.
	 */
	@StaticMethod
	public void parse(AtomicReference<?> buffer, Reader reader, ParsePosition position, Class<?> klass) throws IOException {
		Objects.requireNonNull(reader, "reader");
		Objects.requireNonNull(buffer, "buffer");

		if (position == null)
			position = newParsePosition();
		if (klass == null)
			klass = this.classify(reader, position);

		Method method = this.getParseMethod(klass);

		if (method == null)
			this.parseElse(buffer, reader, position);
		else this.parse0(method, buffer, reader, position);
	}

	/**
	 * Invoke the given {@link ClassifyMethod} with the given parameters.
	 *
	 * @param method   to be invoked
	 * @param reader   to read the sequence from
	 * @param position to classify depending on
	 * @return if the string is instance of the class
	 * @throws ParseException           when any classification error occurs
	 * @throws NullPointerException     if null passed as any param
	 * @throws IOException              if any I/O exception occurs
	 * @throws IllegalArgumentException if the given method has illegal parameters count. Or if it has limited access
	 * @apiNote this may invoke {@link Reader#mark} on the given reader
	 */
	@StaticMethod
	protected boolean classify0(Method method, Reader reader, ParsePosition position) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(method, "method");
			Objects.requireNonNull(reader, "reader");
			Objects.requireNonNull(position, "position");
		}

		try {
			method.setAccessible(true);
			switch (method.getParameterCount()) {
				case 0:
					return (boolean) method.invoke(this);
				case 1:
					return (boolean) method.invoke(this, reader);
				case 2:
					return (boolean) method.invoke(this, reader, position);
				default:
					throw new IllegalArgumentException(method + " have illegal parameters count");
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(method + " have limited access", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException) {
				throw (IOException) cause;
			} else if (cause instanceof ParseException) {
				throw (ParseException) cause;
			} else {
				throw new ParseException(cause);
			}
		}
	}
	/**
	 * Classify the string given by the given reader when no classifying method returned true to it.
	 *
	 * @param reader   to read the string from
	 * @param position to depend on
	 * @return the class from the string given by the given reader
	 * @throws ParseException       if any parsing exception occurs
	 * @throws NullPointerException if any param given is null
	 * @throws IOException          if any I/O exception occurred
	 * @apiNote this may invoke {@link Reader#mark} on the given reader
	 */
	@StaticMethod
	protected Class<?> classifyElse(Reader reader, ParsePosition position) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(reader, "reader");
			Objects.requireNonNull(position, "position");
		}

		throw new ParseException("Can't classify");
	}

	/**
	 * Invoke the given {@link FormatMethod} with the given parameters.
	 *
	 * @param method   to be invoked
	 * @param object   the object ot be formatted
	 * @param writer   to write the text to
	 * @param position to format depending on
	 * @throws FormatException          when any formatting errors occurs
	 * @throws NullPointerException     if any argument given is null (except 'object')
	 * @throws IOException              if any I/O exception occurs
	 * @throws IllegalArgumentException if the given method has illegal parameters count. Or if it has limited access
	 */
	@StaticMethod
	protected void format0(Method method, Writer writer, Object object, FormatPosition position) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(method, "method");
			Objects.requireNonNull(writer, "writer");
			Objects.requireNonNull(position, "position");
		}

		try {
			method.setAccessible(true);
			switch (method.getParameterCount()) {
				case 0:
					method.invoke(this);
				case 1:
					method.invoke(this, writer);
					return;
				case 2:
					method.invoke(this, writer, object);
					return;
				case 3:
					method.invoke(this, writer, object, position);
					return;
				default:
					throw new IllegalArgumentException(method + " have illegal parameters count");
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(method + " have limited access", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException) {
				throw (IOException) cause;
			} else if (cause instanceof FormatException) {
				throw (FormatException) cause;
			} else {
				throw new FormatException(cause);
			}
		}
	}
	/**
	 * Format the given object. If no dynamic method can handle the object. This method shouldn't be directly called.
	 *
	 * @param object   the unsupported object ot be formatted
	 * @param writer   to write the text to
	 * @param position to format depending ond
	 * @throws FormatException      when any formatting errors occurs
	 * @throws NullPointerException if any param given is null (except 'object')
	 * @throws IOException          if any I/O exception occurs
	 */
	@StaticMethod
	protected void formatElse(Writer writer, Object object, FormatPosition position) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(writer, "writer");
			Objects.requireNonNull(position, "position");
		}

		throw new FormatException("Can't format");
	}

	/**
	 * Get the {@link ClassifyMethod} group.
	 *
	 * @return the {@link ClassifyMethod} group
	 */
	@StaticMethod
	protected MethodGroup getClassifyMethods() {
		return this.getMethods().subGroup(ClassifyMethod.class);
	}
	/**
	 * Get the first formatter method that supports the given class as a parameter. (methods ordered randomly).
	 *
	 * @param klass to query a method for
	 * @return the first format method supports given class. Or null if this class don't have one
	 * @throws NullPointerException if the given class is null
	 */
	@StaticMethod
	protected Method getFormatMethod(Class<?> klass) {
		Objects.requireNonNull(klass, "klass");
		return this.getMethods().subGroup(FormatMethod.class).subGroup(klass, method ->
				Type.util.test(method.getAnnotation(FormatMethod.class).value(), klass)).getFirst();
	}
	/**
	 * Get the first parser method that supports the given class as a parameter. (methods ordered randomly).
	 *
	 * @param klass to query a method for
	 * @return the first parse method supports given class. Or null if this class don't have one
	 * @throws NullPointerException if the given class is null
	 */
	@StaticMethod
	protected Method getParseMethod(Class<?> klass) {
		Objects.requireNonNull(klass, "klass");
		return this.getMethods().subGroup(ParseMethod.class).subGroup(klass, method ->
				Type.util.test(method.getAnnotation(ParseMethod.class).value(), klass)).getFirst();
	}

	/**
	 * Construct a new formatting position.
	 *
	 * @return new formatting position
	 * @implSpec don't return null
	 */
	@StaticMethod
	protected FormatPosition newFormatPosition() {
		return new FormatPosition() {
		};
	}
	/**
	 * Construct a new parsing position.
	 *
	 * @return new Parsing position
	 * @implSpec don't return null
	 */
	@StaticMethod
	protected ParsePosition newParsePosition() {
		return new ParsePosition() {
		};
	}

	/**
	 * Invoke the given parse method with the given parameters.
	 *
	 * @param method   to be invoked
	 * @param reader   to read the text from
	 * @param buffer   to store the parsed object to while parsing
	 * @param position to parse the given sequence depending on
	 * @throws ParseException           when any parsing error occurs
	 * @throws NullPointerException     if any of the given parameters is null
	 * @throws IOException              if any I/O exception occurs
	 * @throws IllegalArgumentException if the given method has illegal parameters count. Or if it has limited access
	 * @apiNote this may invoke {@link Reader#mark} on the given reader
	 * @implSpec if the given buffer is not empty. Do not replace it.
	 */
	@StaticMethod
	protected void parse0(Method method, AtomicReference<?> buffer, Reader reader, ParsePosition position) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(method, "method");
			Objects.requireNonNull(reader, "reader");
			Objects.requireNonNull(buffer, "buffer");
			Objects.requireNonNull(position, "position");
		}

		try {
			method.setAccessible(true);
			switch (method.getParameterCount()) {
				case 0:
					method.invoke(this);
				case 1:
					method.invoke(this, buffer);
				case 2:
					method.invoke(this, buffer, reader);
					return;
				case 3:
					method.invoke(this, buffer, reader, position);
					return;
				default:
					throw new IllegalArgumentException(method + " have illegal parameters count");
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(method + " have a limited access", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException) {
				throw (IOException) cause;
			} else if (cause instanceof ParseException) {
				throw (ParseException) cause;
			} else {
				throw new ParseException(cause);
			}
		}
	}
	/**
	 * Parse the given string on the given reader. If no method on this class can handle it. This method shouldn't be directly called.
	 *
	 * @param reader   to read the text from
	 * @param buffer   to store the parsed object to while parsing
	 * @param position to parse depending on
	 * @throws ParseException       when the parser can't handle the given string
	 * @throws NullPointerException if any of the given parameters is null
	 * @throws IOException          if any I/O exception occurs
	 * @apiNote this may invoke {@link Reader#mark} on the given reader
	 * @implSpec if the given buffer is not empty. Do not replace it.
	 */
	@StaticMethod
	protected void parseElse(AtomicReference<?> buffer, Reader reader, ParsePosition position) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(reader, "reader");
			Objects.requireNonNull(buffer, "buffer");
			Objects.requireNonNull(position, "position");
		}

		throw new ParseException("Can't parse");
	}

	/**
	 * A position used by {@link Format} to manage nested formatting.
	 *
	 * @implSpec the position should have final values!
	 */
	public interface FormatPosition {
	}

	/**
	 * A position used by {@link Format} to manage nested parsing.
	 *
	 * @implSpec the position should have final values!
	 */
	public interface ParsePosition {
	}

	/**
	 * Navigate the {@link Format} class that the annotated method is a string-type-detecting method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link #classify0} rules
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface ClassifyMethod {
		/**
		 * Tells what class is the annotated method is checking the input for.
		 *
		 * @return the class that the annotated method is checking the input for
		 */
		Class<?> value();
	}

	/**
	 * Navigate the {@link Format} class that the annotated method is a stringing method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link #format0} rules
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface FormatMethod {
		/**
		 * Classes that the annotated method dose support as a parameter.
		 *
		 * @return the supported classes
		 */
		Type value() default @Type;
	}

	/**
	 * Navigate the {@link Format} class that the annotated method is a parsing method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link #parse0} rules
	 * @implSpec if the given buffer is not empty. Do not replace it.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface ParseMethod {
		/**
		 * Classes that the annotated method returns.
		 *
		 * @return the classes that the annotated method returns
		 */
		Type value() default @Type;
	}
}
