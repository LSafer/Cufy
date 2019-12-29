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
import cufy.lang.Type;
import cufy.util.ObjectUtil;

import java.io.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An abstract for text formatting classes. Used to simplify the formatter and make it more inheritable. Also make inheriting just for add some
 * futures more easier. This uses the {@link Invoke} class and methods in this will be invoked using the dynamic method grouping algorithm. In order
 * to add a method on a dynamic method group. the method should be annotated with the group annotation. (see {@link Invoke}). Also the method should
 * match the conditions of that group to avoid parameters/output mismatches. This abstract have three methods group {@link ClassifyMethod}, {@link
 * ParseMethod} and {@link FormatMethod}. each of them have their own conditions.
 *
 * <ul>
 *     <font color="orange" size="4" face="verdana"><b>Abstract Methods:</b></font>
 *     <li>
 *         <font color="yellow">Support</font>
 *         <ul>
 *             <li>{@link #newFormatPosition}</li>
 *             <li>{@link #newParsePosition}</li>
 *         </ul>
 *     </li>
 * </ul>
 * <ul>
 *     <font color="orange" size="4" face="verdana"><b>Static-Dynamic Methods:</b></font>
 *     <li>
 *         <font color="yellow">{@link ClassifyMethod Classify}</font>
 *         <ul>
 *             <li>{@link #getClassifyMethods}</li>
 *             <li>{@link #classify0}</li>
 *             <li>{@link #classify}</li>
 *             <li>{@link #classifyElse}</li>
 *             <li>{@link #isNull}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link FormatMethod Format}</font>
 *         <ul>
 *             <li>{@link #getFormatMethod}</li>
 *             <li>{@link #format0}</li>
 *             <li>{@link #format}</li>
 *             <li>{@link #formatElse}</li>
 *             <li>{@link #formatNull}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link ParseMethod Parse}</font>
 *         <ul>
 *             <li>{@link #getParseMethod}</li>
 *             <li>{@link #parse0}</li>
 *             <li>{@link #parse}</li>
 *             <li>{@link #parseElse}</li>
 *             <li>{@link #parseNull}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author LSaferSE
 * @version 6 release (12-Dec-2019)
 * @implNote you have to navigate this class to where your {@link FormatMethod}, {@link ClassifyMethod} and {@link ParseMethod} methods is. By using
 * the mentioned annotations.
 * @since 28-Sep-19
 */
public abstract class Format extends Invoke {
	/**
	 * Query what's the suitable class for the given sequence.
	 *
	 * @param sequence to query a suitable class for
	 * @return the suitable class for the given sequence. Or null if unknown.
	 * @throws FormatException when any formatting error occurs
	 */
	@StaticMethod
	public Class<?> classify(CharSequence sequence) {
		try {
			return this.classify(new StringReader(String.valueOf(sequence)), null);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	/**
	 * Query what's the suitable class from the text given by the given 'reader'. Using all methods with {@link ClassifyMethod} on this formatter. Or
	 * null if no classifier can classify it.
	 *
	 * @param reader   to read the sequence from
	 * @param position to classify depending on (null to create a new position)
	 * @return the suitable class for the given string. Or null if unknown.
	 * @throws FormatException      when any formatting error occurs
	 * @throws IOException          if any I/O exception occurs
	 * @throws NullPointerException if the given reader is null
	 */
	@StaticMethod
	public Class<?> classify(Reader reader, ParsePosition position) throws IOException {
		ObjectUtil.requireNonNull(reader, "reader");

		position = ObjectUtil.requireNonNullElseGet(position, this::newParsePosition);

		reader.mark(50);

		if (this.isNull(reader, position)) {
			reader.reset();
			return null;
		}

		for (Method method : this.getClassifyMethods()) {
			boolean w = this.classify0(method, reader, position);
			reader.reset();
			if (w) return method.getAnnotation(ClassifyMethod.class).value();
		}

		Class<?> klass = this.classifyElse(reader, position);
		reader.reset();
		return klass;
	}

	/**
	 * Format the given object into a text on a string.
	 *
	 * @param object the object to be formatted
	 * @return the formatted string
	 * @throws FormatException when any formatting error occurs
	 */
	@StaticMethod
	public String format(Object object) {
		StringWriter writer = new StringWriter();
		try {
			this.format(object, writer, null, null);
		} catch (IOException e) {
			throw new Error(e);
		}
		return writer.toString();
	}

	/**
	 * Format the given object. Writing it to the given writer. Using the first method annotated with {@link FormatMethod}. And that annotation allows
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
	public void format(Object object, Writer writer, FormatPosition position, Class<?> klass) throws IOException {
		ObjectUtil.requireNonNull(writer, "writer");

		position = ObjectUtil.requireNonNullElseGet(position, this::newFormatPosition);

		if (object == null) {
			this.formatNull(writer, position);
			return;
		}

		klass = ObjectUtil.requireNonNullElseGet(klass, object::getClass);

		Method method = this.getFormatMethod(klass);

		if (method == null) {
			this.formatElse(object, writer, position);
			return;
		}

		this.format0(method, object, writer, position);
	}

	/**
	 * Parse an object from the given sequence.
	 *
	 * @param sequence to parse the object from
	 * @return parsed object from the given sequence
	 * @throws ParseException when any parsing error occurs
	 */
	@StaticMethod
	public Object parse(CharSequence sequence) {
		try {
			Reader reader = new StringReader(String.valueOf(sequence));
			AtomicReference<Object> buffer = new AtomicReference<>();
			this.parse(reader, buffer, null, null);
			return buffer.get();
		} catch (IOException e) {
			throw new Error(e);
		}
	}

	/**
	 * Parse the given sequence to an object. Storing that object to the given buffer. Using the first method annotated with {@link ParseMethod}. And
	 * that annotation allows the given class. (methods are ordered randomly).
	 *
	 * @param reader   to read the sequence form
	 * @param buffer   to store the parsed object to while parsing
	 * @param position to parse the given sequence depending on (null to create a new one)
	 * @param klass    the targeted method output type (null for classifying the given sequence dynamically)
	 * @throws ParseException       when any parsing error occurs
	 * @throws NullPointerException if the given 'reader' or 'buffer' is null
	 * @throws IOException          if any I/O exception occurs
	 */
	@StaticMethod
	public void parse(Reader reader, AtomicReference<?> buffer, ParsePosition position, Class<?> klass) throws IOException {
		ObjectUtil.requireNonNull(reader, "reader");
		ObjectUtil.requireNonNull(buffer, "buffer");

		position = ObjectUtil.requireNonNullElseGet(position, this::newParsePosition);

		if (klass == null)
			klass = this.classify(reader, position);
		if (klass == null) {
			this.parseNull(reader, buffer, position);
			return;
		}

		Method method = this.getParseMethod(klass);

		if (method == null) {
			this.parseElse(reader, buffer, position);
			return;
		}

		this.parse0(method, reader, buffer, position);
	}

	/**
	 * Invoke the given classify method with the given parameters.
	 *
	 * @param method   to be invoked
	 * @param reader   to read the sequence from
	 * @param position to classify depending on
	 * @return if the string is instance of the class
	 * @throws ParseException       when any classification error occurs
	 * @throws NullPointerException if null passed as any param
	 * @throws IOException          if any I/O exception occurs
	 */
	@StaticMethod
	protected boolean classify0(Method method, Reader reader, ParsePosition position) throws IOException {
		ObjectUtil.requireNonNull(method, "method");
		ObjectUtil.requireNonNull(reader, "reader");
		ObjectUtil.requireNonNull(position, "position");
		try {
			method.setAccessible(true);
			return (boolean) method.invoke(this, reader, position);
		} catch (IllegalAccessException e) {
			throw new Error(e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException)
				throw (IOException) cause;
			if (cause instanceof RuntimeException)
				throw (RuntimeException) cause;
			if (cause instanceof Error)
				throw (Error) cause;
			throw new Error(cause);
		}
	}

	/**
	 * Classify the string given by the given reader when no classifying method returned true to it.
	 *
	 * @param reader   to read the string from
	 * @param position to depend on
	 * @return the class from the string given by the given reader
	 * @throws ParseException                if any parsing exception occurs
	 * @throws NullPointerException          if any param given is null
	 * @throws IOException                   if any I/O exception occurred
	 * @throws UnsupportedOperationException if the method isn't supported by the format
	 */
	@StaticMethod
	protected Class<?> classifyElse(Reader reader, ParsePosition position) throws IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Invoke the given format method with the given parameters.
	 *
	 * @param method   to be invoked
	 * @param object   the object ot be formatted
	 * @param writer   to write the text to
	 * @param position to format depending on
	 * @throws FormatException      when any formatting errors occurs
	 * @throws NullPointerException if any argument given is null
	 * @throws IOException          if any I/O exception occurs
	 */
	@SuppressWarnings("DuplicatedCode")
	@StaticMethod
	protected void format0(Method method, Object object, Writer writer, FormatPosition position) throws IOException {
		ObjectUtil.requireNonNull(method, "method");
		ObjectUtil.requireNonNull(object, "object");
		ObjectUtil.requireNonNull(writer, "writer");
		ObjectUtil.requireNonNull(position, "position");
		try {
			method.setAccessible(true);
			method.invoke(this, object, writer, position);
		} catch (IllegalAccessException e) {
			throw new Error(e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException)
				throw (IOException) cause;
			if (cause instanceof RuntimeException)
				throw (RuntimeException) cause;
			if (cause instanceof Error)
				throw (Error) cause;
			throw new Error(cause);
		}
	}

	/**
	 * Format the given unsupported type object. If no dynamic method can handle the object. This method will be invoked. This method shouldn't be
	 * directly called.
	 *
	 * @param object   the unsupported object ot be formatted
	 * @param writer   to write the text to
	 * @param position to format depending ond
	 * @throws FormatException               when any formatting errors occurs
	 * @throws NullPointerException          if any param given is null
	 * @throws IOException                   if any I/O exception occurs
	 * @throws UnsupportedOperationException if the method isn't supported by the format
	 */
	@StaticMethod
	protected void formatElse(Object object, Writer writer, FormatPosition position) throws IOException {
		throw new UnsupportedEncodingException();
	}

	/**
	 * Format the value 'null'. This method shouldn't be directly called.
	 *
	 * @param writer   to write the text to
	 * @param position to format depending on
	 * @throws FormatException               when any formatting errors occurs (Also if this method isn't supported by this formatter)
	 * @throws NullPointerException          if any argument given is null
	 * @throws UnsupportedOperationException if the method isn't supported by the format
	 * @throws IOException                   if any I/O exception occurs
	 */
	@StaticMethod
	protected void formatNull(Writer writer, FormatPosition position) throws IOException {
		throw new UnsupportedEncodingException();
	}

	/**
	 * Get the classify methods group.
	 *
	 * @return the classify methods group
	 */
	@StaticMethod
	protected synchronized MethodGroup getClassifyMethods() {
		return this.getMethodGroup(ClassifyMethod.class);
	}

	/**
	 * Get the first formatter method that supports the given class as a parameter. (methods ordered randomly).
	 *
	 * @param klass to query a method for
	 * @return the first format method supports given class. Or null if this class don't have one
	 * @throws NullPointerException if the given class is null
	 */
	@StaticMethod
	protected synchronized Method getFormatMethod(Class<?> klass) {
		ObjectUtil.requireNonNull(klass, "klass");
		return this.getMethodGroup(FormatMethod.class).get(klass, method ->
				Type.util.test(method.getAnnotation(FormatMethod.class).in(), klass));
	}

	/**
	 * Get the first parser method that supports the given class as a parameter. (methods ordered randomly).
	 *
	 * @param klass to query a method for
	 * @return the first parse method supports given class. Or null if this class don't have one
	 * @throws NullPointerException if the given class is null
	 */
	@StaticMethod
	protected synchronized Method getParseMethod(Class<?> klass) {
		ObjectUtil.requireNonNull(klass, "klass");
		return this.getMethodGroup(ParseMethod.class).get(klass, method ->
				Type.util.test(method.getAnnotation(ParseMethod.class).out(), klass));
	}

	/**
	 * Check if the given string should be parsed as null or not. This method shouldn't be directly called.
	 *
	 * @param reader   to read the string from
	 * @param position to check depending on
	 * @return whether the given string is null or not.
	 * @throws ParseException       if any parsing exception occurs
	 * @throws NullPointerException if the position given is null
	 * @throws IOException          if any I/O exception occurs
	 */
	@StaticMethod
	protected boolean isNull(Reader reader, ParsePosition position) throws IOException {
		ObjectUtil.requireNonNull(reader, "reader");
		ObjectUtil.requireNonNull(position, "position");
		return false;
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
	 * @throws ParseException       when any parsing error occurs
	 * @throws NullPointerException if any of the given parameters is null
	 * @throws IOException          if any I/O exception occurs
	 */
	@SuppressWarnings("DuplicatedCode")
	@StaticMethod
	protected void parse0(Method method, Reader reader, AtomicReference<?> buffer, ParsePosition position) throws IOException {
		ObjectUtil.requireNonNull(method, "method");
		ObjectUtil.requireNonNull(reader, "reader");
		ObjectUtil.requireNonNull(buffer, "buffer");
		ObjectUtil.requireNonNull(position, "position");
		try {
			method.setAccessible(true);
			method.invoke(this, reader, buffer, position);
		} catch (IllegalAccessException e) {
			throw new Error(e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof IOException)
				throw (IOException) cause;
			if (cause instanceof RuntimeException)
				throw (RuntimeException) cause;
			if (cause instanceof Error)
				throw (Error) cause;
			throw new Error(cause);
		}
	}

	/**
	 * Parse the given unclassifiable string. This method shouldn't be directly called.
	 *
	 * @param reader   to read the text from
	 * @param buffer   to store the parsed object to while parsing
	 * @param position to parse depending on
	 * @throws ParseException                when the parser can't handle the given string
	 * @throws NullPointerException          if any of the given parameters is null
	 * @throws UnsupportedOperationException if this method not supported by this format
	 * @throws IOException                   if any I/O exception occurs
	 */
	@StaticMethod
	protected void parseElse(Reader reader, AtomicReference<?> buffer, ParsePosition position) throws IOException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parse the given null text. Set the parsed object to the given buffer.
	 *
	 * @param reader   to read the text from
	 * @param buffer   to set the object to
	 * @param position to depend on
	 * @throws ParseException                when the parser can't handle the given string
	 * @throws NullPointerException          if any of the given parameters is null
	 * @throws UnsupportedOperationException if this method not supported by this format
	 * @throws IOException                   if any I/O exception occurs
	 */
	@StaticMethod
	protected void parseNull(Reader reader, AtomicReference<?> buffer, ParsePosition position) throws IOException {
		ObjectUtil.requireNonNull(reader, "reader");
		ObjectUtil.requireNonNull(buffer, "buffer");
		ObjectUtil.requireNonNull(position, "position");
		buffer.set(null);
	}

	/**
	 * A position used by {@link Format} to manage nested formatting.
	 *
	 * @author LSaferSE
	 * @version 1 release (26-Nov-2019)
	 * @implSpec a point should have final values!
	 * @since 19-Nov-2019
	 */
	public interface FormatPosition {
	}

	/**
	 * A position used by {@link Format} to manage nested parsing.
	 *
	 * @author LSaferSE
	 * @version 1 release (26-Nov-2019)
	 * @implSpec a point should have final values!
	 * @since 19-Nov-2019
	 */
	public interface ParsePosition {
	}

	/**
	 * Navigate the {@link Format} class that the annotated method is a string-type-detecting method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link #classify0} rules
	 * @see #classify(Reader, ParsePosition) parameterization
	 * @see #getClassifyMethods grouping
	 * @see #classify0 invokation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface ClassifyMethod {
		/**
		 * Tells what class the annotated method is looking for. This will direct to the method with the same {@link ParseMethod#out()} as this
		 * value.
		 *
		 * @return the class the annotated method is looking for
		 */
		Class<?> value();
	}

	/**
	 * Navigate the {@link Format} class that the annotated method is a stringing method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link #format0} rules
	 * @see #format(Object, Writer, FormatPosition, Class) parameterization
	 * @see #getFormatMethod grouping
	 * @see #format0 invokation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface FormatMethod {
		/**
		 * Classes that the annotated method dose or dose not support as a parameter.
		 *
		 * @return the supported/unsupported classes
		 */
		Type in() default @Type;
	}

	/**
	 * Navigate the {@link Format} class that the annotated method is a parsing method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link #parse0} rules
	 * @see #parse(Reader, AtomicReference, ParsePosition, Class) parameterization
	 * @see #getParseMethod grouping
	 * @see #parse0 invokation
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface ParseMethod {
		/**
		 * This is the magnet value for the {@link ClassifyMethod}. The linked classify method should have the same {@link ClassifyMethod#value()} as
		 * this value
		 *
		 * @return the magnet values for the classify method
		 */
		Type out() default @Type;
	}
}
