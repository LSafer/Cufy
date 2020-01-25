/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.lang;

import cufy.beans.Invoke;
import cufy.beans.StaticMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * An abstract class for converter classes. Used to simplify the conversion processes and make it more inheritable. Also making the inheriting for
 * adding some futures more easier. This uses the {@link Invoke} class. And the methods in this will be invoked using the dynamic method grouping
 * algorithm. In order to add a method on a dynamic method group. The method should be annotated with that group annotation. (see {@link Invoke}).
 * Also the method should match the conditions of that group to avoid parameters/output mismatches. This abstract have one method group {@link
 * ConvertMethod}. and that group have it's own conditions.
 *
 * @author LSaferSE
 * @version 8 release (23-Jan-2020)
 * @implNote you have to navigate this class to where your dynamic methods is. By using annotations.
 * @since 31-Aug-19
 */
public abstract class Converter extends Invoke {
	/**
	 * Convert the given object to the given class.
	 *
	 * @param source       the object to be converted
	 * @param productClass the targeted class to convert the object to
	 * @param <T>          the targeted type
	 * @return the given object converted to the given 'productClass'
	 * @throws ClassConversionException if any converting error occurred
	 * @throws NullPointerException     if the 'productClass' is null
	 */
	@StaticMethod
	public <T> T convert(Object source, Class<T> productClass) {
		return this.convert(source, productClass, null, null, false);
	}
	/**
	 * Convert the given object to the given 'productClass'. Using the first method annotated with {@link ConvertMethod}. And that annotation allows
	 * the given 'sourceClass' and 'productClass' classes. (methods are ordered randomly)
	 *
	 * @param source       the object to be converted
	 * @param productClass the targeted class to convert the object to
	 * @param position     to convert depending on (null to create a new one)
	 * @param sourceClass  the targeted method parameter type (null for the class of the given object)
	 * @param clone        true to create a new instance even when the object is instance of the targeted class
	 * @param <T>          the targeted type
	 * @return the given object converted to the given 'productClass'
	 * @throws ClassConversionException if any converting error occurred
	 * @throws NullPointerException     if 'productClass' is null
	 */
	@StaticMethod
	public <T> T convert(Object source, Class<T> productClass, ConvertPosition position, Class<?> sourceClass, boolean clone) {
		Objects.requireNonNull(productClass, "productClass");

		if (position == null)
			position = this.newConvertPosition();
		if (!clone && productClass.isInstance(source))
			return (T) source;
		if (sourceClass == null)
			sourceClass = source == null ? Void.class : source.getClass();

		Method method = this.getConvertMethod(sourceClass, productClass);

		if (method == null) {
			return (T) this.convertElse(source, productClass, position);
		} else {
			return (T) this.convert0(method, source, productClass, position);
		}
	}

	/**
	 * Invoke the given {@link ConvertMethod} with the given parameters.
	 *
	 * @param method       to be invoked
	 * @param source       the object to be converted
	 * @param productClass the targeted class to convert the object to
	 * @param position     to convert depending on
	 * @return the given object converted to the given 'productClass'
	 * @throws ClassConversionException if any converting error occurred
	 * @throws NullPointerException     if any of the given parameters is null (except 'source')
	 * @throws IllegalArgumentException if the given method have limited access. Or if the given method have illegal parameters count
	 */
	@StaticMethod
	protected Object convert0(Method method, Object source, Class<?> productClass, ConvertPosition position) {
		if (DEBUGGING) {
			Objects.requireNonNull(method, "method");
			Objects.requireNonNull(productClass, "productClass");
			Objects.requireNonNull(position, "position");
		}

		try {
			method.setAccessible(true);
			switch (method.getParameterCount()) {
				case 0:
					return method.invoke(this);
				case 1:
					return method.invoke(this, source);
				case 2:
					return method.invoke(this, source, productClass);
				case 3:
					return method.invoke(this, source, productClass, position);
				default:
					throw new IllegalArgumentException(methods + " have illegal parameters count");
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(method + " have limited access", e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof ClassConversionException)
				throw (ClassConversionException) cause;
			else throw new ClassConversionException(cause);
		}
	}
	/**
	 * Convert the given object to the given class. If no dynamic method can handle the object. This method shouldn't be directly called.
	 *
	 * @param source       the object to be converted
	 * @param productClass the targeted class to convert the object to
	 * @param position     to convert depending on
	 * @return the given object converted to the given 'productClass'
	 * @throws ClassConversionException if any converting error occurred (Also if this method isn't supported by this caster)
	 * @throws NullPointerException     if any of the given parameters is null (except 'source')
	 */
	@StaticMethod
	protected Object convertElse(Object source, Class<?> productClass, ConvertPosition position) {
		if (DEBUGGING) {
			Objects.requireNonNull(productClass, "productClass");
			Objects.requireNonNull(position, "position");
		}

		throw new ClassConversionException("Cannot convert " + source.getClass() + " to " + productClass);
	}

	/**
	 * Find a method that converts the given 'sourceClass' to the given 'productClass'.
	 *
	 * @param sourceClass  type that the targeted method can except as a parameter
	 * @param productClass type that the targeted method can return
	 * @return a method that can convert the given sourceClass to the given productClass class
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@StaticMethod
	protected Method getConvertMethod(Class<?> sourceClass, Class<?> productClass) {
		if (DEBUGGING) {
			Objects.requireNonNull(sourceClass, "sourceClass");
			Objects.requireNonNull(productClass, "productClass");
		}

		return this.getMethods().subGroup(ConvertMethod.class).subGroup(Arrays.asList(sourceClass, productClass), method -> {
			ConvertMethod annotation = method.getAnnotation(ConvertMethod.class);
			return Type.util.test(annotation.in(), sourceClass) && Type.util.test(annotation.out(), productClass);
		}).getFirst();
	}

	/**
	 * Construct a new casting position.
	 *
	 * @return new Casting position
	 * @implSpec don't return null
	 */
	@StaticMethod
	protected ConvertPosition newConvertPosition() {
		return new ConvertPosition() {
		};
	}

	/**
	 * Navigate the {@link Converter} class that the annotated method is a converting method.
	 *
	 * @apiNote the annotated method SHOULD match the {@link Converter#convert0} rules
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ConvertMethod {
		/**
		 * The classes that the annotated method can accept as a parameter.
		 *
		 * @return the input type range
		 */
		Type in();

		/**
		 * The classes that the annotated method can return.
		 *
		 * @return the output type range
		 */
		Type out();
	}

	/**
	 * A position used by {@link Converter} to manage nested conversions.
	 *
	 * @implSpec every position should have final values!
	 */
	public interface ConvertPosition {
	}
}
