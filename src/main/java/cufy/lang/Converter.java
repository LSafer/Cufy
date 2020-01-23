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
 * An abstract for casting classes. Used to simplify the castings and make it more inheritable. Also make inheriting just for add some futures more
 * easier. This uses the {@link Invoke} class and methods in this will be invoked using the dynamic method grouping algorithm. In order to add a
 * method on a dynamic method group. the method should be annotated with the group annotation. (see {@link Invoke}). Also the method should match the
 * conditions of that group to avoid parameters/output mismatches. This abstract have the method group {@link ConvertMethod}. and that group have it's
 * own conditions.
 *
 * @author LSaferSE
 * @version 7 release (07-Dec-2019)
 * @implNote you have to navigate this class to where your {@link ConvertMethod} methods is. By using the mentioned annotation.
 * @since 31-Aug-19
 */
public abstract class Converter extends Invoke {
	/**
	 * Cast the given object to the given class.
	 *
	 * @param source       the object to be casted
	 * @param productClass the targeted class to cast the object to
	 * @param <T>          the targeted type
	 * @return the given object casted to the given 'productClass'
	 * @throws ClassConversionException on converting failure occurred
	 * @throws NullPointerException     if the 'productClass' is null
	 */
	@StaticMethod
	public <T> T convert(Object source, Class<? super T> productClass) {
		return this.convert(source, productClass, null, null, false);
	}

	/**
	 * Cast the given object to the given 'productClass' class. Using the first method annotated with {@link ConvertMethod}. And that annotation
	 * allows the given 'sourceClass' and 'productClass' classes. (methods are ordered randomly)
	 *
	 * @param source       the object to be casted
	 * @param productClass the targeted class to cast the object to
	 * @param position     to format depending on (null to create a new one)
	 * @param sourceClass  the targeted method parameter type (null for the class of the given object)
	 * @param clone        true to create a new instance even when the object is instance of the targeted class
	 * @param <T>          the targeted type
	 * @return the given object casted to the given 'productClass' class
	 * @throws ClassConversionException on converting failure occurred
	 * @throws NullPointerException     if the 'productClass' param equals to null
	 */
	@StaticMethod
	public <T> T convert(Object source, Class<? super T> productClass, ConvertPosition position, Class<?> sourceClass, boolean clone) {
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
	 * Invoke the given cast method with the given parameters.
	 *
	 * @param method       to be invoked
	 * @param source       the object to be casted
	 * @param productClass the targeted class to cast the object to
	 * @param position     to format depending on
	 * @return the given object casted to the given 'out' class
	 * @throws ClassConversionException on converting failure occurred
	 * @throws NullPointerException     if any of the given parameters is null
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
	 * Cast the given unsupported type object to the given class. If no dynamic method can handle the object. This method will be invoked. This method
	 * shouldn't be directly called.
	 *
	 * @param source       the object to be casted
	 * @param productClass the targeted class to cast the object to
	 * @param position     to format depending on
	 * @return the given object casted to the given 'productClass' class
	 * @throws ClassCastException       on casting failure (Also if this method isn't supported by this caster)
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if any of the given parameters equals to null
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
	 * Find a method that casts any of the given 'sourceClass'. to the given 'productClass'.
	 *
	 * @param sourceClass  type that the targeted method can cast
	 * @param productClass type that the targeted method can return
	 * @return a method that casts the given sourceClass to the given productClass class
	 * @throws NullPointerException if any of the given parameters equals to null
	 */
	@StaticMethod
	protected Method getConvertMethod(Class<?> sourceClass, Class<?> productClass) {
		if (DEBUGGING) {
			Objects.requireNonNull(sourceClass, "sourceClass");
			Objects.requireNonNull(productClass, "productClass");
		}

		return this.getMethods().getMethodGroup(ConvertMethod.class).getMethodGroup(Arrays.asList(sourceClass, productClass), method -> {
			ConvertMethod annotation = method.getAnnotation(ConvertMethod.class);
			return Type.util.test(annotation.in(), sourceClass) && Type.util.test(annotation.out(), productClass);
		}).get(0);
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
	 * Navigate the {@link Converter} class that the annotated method is a stringing method.
	 *
	 * @author LSaferSE
	 * @version 1 release (07-Dec-2019)
	 * @apiNote the annotated method SHOULD match the {@link Converter#convert0} rules
	 * @see Converter#convert parameterization
	 * @see Converter#getConvertMethod grouping
	 * @see Converter#convert0 invokation
	 * @since 07-Dec-2019
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ConvertMethod {
		/**
		 * The input classes range the annotated method targeting.
		 *
		 * @return the input range
		 */
		Type in();

		/**
		 * The output classes range the annotated method targeting.
		 *
		 * @return the output range
		 */
		Type out();
	}

	/**
	 * A position used by {@link Converter} to manage nested castings.
	 *
	 * @author LSaferSE
	 * @version 1 release (25-Nov-2019)
	 * @implSpec a point should have final values!
	 * @since 25-Nov-2019
	 */
	public interface ConvertPosition {
		//Customize it whatever you like :)
	}
}
