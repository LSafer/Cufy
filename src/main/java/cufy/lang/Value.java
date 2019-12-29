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

import org.cufy.lang.BaseConverter;
import cufy.util.ObjectUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * A value with a type the annotations don't support.
 *
 * @author LSaferSE
 * @version 2 release (25-Nov-2019)
 * @since 21-Nov-2019
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Value {
	/**
	 * The caster to be used to cast the value.
	 *
	 * @return the caster to be used to cast this value
	 */
	Class<? extends Converter> converter() default BaseConverter.class;

	/**
	 * Whether this value equals to null or not.
	 *
	 * @return true if this value equals to null
	 */
	boolean isnull() default false;

	/**
	 * The type of this value.
	 *
	 * @return the type of this value
	 */
	Class<?> type() default String.class;

	/**
	 * The source string for this value.
	 *
	 * @return the source string
	 */
	String value() default "";

	/**
	 * Tools for this annotation. (aka static methods).
	 */
	final class util {
		/**
		 * Build the given typed value.
		 *
		 * @param value source
		 * @return a value from the given typedValue annotation instance
		 * @throws NullPointerException       if the given value is null
		 * @throws IllegalAnnotationException if ANY throwable get thrown while constructing the object
		 */
		public static Object construct(Value value) {
			try {
				ObjectUtil.requireNonNull(value, "value");
				return value.isnull() ? null : Global.get(value.converter()).convert(value.value(), (Class<? super Object>) value.type());
			} catch (Throwable t) {
				throw new IllegalAnnotationException("Can't construct " + value.getClass() + ": " + t.getMessage(), t);
			}
		}

		/**
		 * Build the given typed value. And make sure that it matches the given class.
		 *
		 * @param value    source
		 * @param expected expected type
		 * @param <T>      var-arg of the expected type
		 * @return a value from the given typedValue annotation instance
		 * @throws NullPointerException       if the given value is null
		 * @throws IllegalAnnotationException if ANY throwable get thrown while constructing the object
		 */
		public static <T> T construct(Value value, Class<T> expected) {
			ObjectUtil.requireNonNull(value, "value");
			ObjectUtil.requireNonNull(expected, "expected");

			try {
				return expected.cast(construct(value));
			} catch (ClassCastException e) {
				throw new IllegalAnnotationException("Can't construct " + value.getClass() + ": " + e.getMessage(), e);
			}
		}
	}
}
