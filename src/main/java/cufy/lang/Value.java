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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * A value with a type the annotations don't support.
 *
 * @author LSaferSE
 * @version 3 release (23-Jan-2020)
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
		 * @throws NullPointerException   if the given value is null
		 * @throws BadAnnotationException if ANY throwable get thrown while constructing the object
		 */
		public static Object construct(Value value) {
			try {
				Objects.requireNonNull(value, "value");

				if (value.isnull()) {
					return null;
				} else {
					Converter converter = Global.get(value.converter());
					String str = value.value();
					Class<?> type = value.type();

					return converter.convert(str, type);
				}
			} catch (Throwable t) {
				throw new BadAnnotationException("Can't construct " + value.getClass() + ": " + t.getMessage(), t);
			}
		}
		/**
		 * Build the given typed value. And make sure that it matches the given class.
		 *
		 * @param value    source
		 * @param expected expected type
		 * @param <T>      var-arg of the expected type
		 * @return a value from the given typedValue annotation instance
		 * @throws NullPointerException   if any param given is null
		 * @throws BadAnnotationException if ANY throwable get thrown while constructing the object
		 */
		public static <T> T construct(Value value, Class<T> expected) {
			Objects.requireNonNull(value, "value");
			Objects.requireNonNull(expected, "expected");

			try {
				return expected.cast(construct(value));
			} catch (BadAnnotationException e) {
				throw e;
			} catch (Throwable e) {
				throw new BadAnnotationException("Can't construct " + value.getClass() + ": " + e.getMessage(), e);
			}
		}
	}
}
