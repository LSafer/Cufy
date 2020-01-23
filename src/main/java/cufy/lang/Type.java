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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * Class ranging annotation. A way to specify a range of classes.
 *
 * @author LSaferSE
 * @version 3 release (23-Jan-2020)
 * @since 21-Nov-2019
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Type {
	/**
	 * Classes in range (subclasses NOT included).
	 *
	 * @return absolute classes in range
	 * @apiNote this overrides {@link #subout()}
	 */
	Class<?>[] in() default {};

	/**
	 * Classes out range (subclasses NOT included).
	 *
	 * @return absolute classes not in range
	 * @apiNote this will override {@link #in()}, {@link #subin()}
	 */
	Class<?>[] out() default {};

	/**
	 * Classes in range (subclasses included).
	 *
	 * @return super classes in range
	 */
	Class<?>[] subin() default {};

	/**
	 * Classes not in range (subclasses included).
	 *
	 * @return super classes not in range
	 * @apiNote this will override {@link #subin()}
	 */
	Class<?>[] subout() default {};

	/**
	 * Works just like {@link #in()}.
	 *
	 * @return absolute classes in range
	 */
	Class<?>[] value() default {};

	/**
	 * Tools for this annotation. (aka static methods).
	 */
	final class util {
		/**
		 * Check whether the given class is in the given range or not.
		 *
		 * @param type  to check if the class is in
		 * @param klass to be checked
		 * @return whether the given class is in the given range or not
		 * @throws NullPointerException if ether the given range or class is null
		 */
		public static boolean test(Type type, Class<?> klass) {
			Objects.requireNonNull(type, "type");
			Objects.requireNonNull(klass, "klass");

			for (Class<?> exclude : type.out())
				if (exclude == klass)
					return false;

			for (Class<?> include : type.in())
				if (include == klass)
					return true;

			for (Class<?> include : type.value())
				if (include == klass)
					return true;

			for (Class<?> exclude : type.subout())
				if (exclude.isAssignableFrom(klass))
					return false;

			for (Class<?> include : type.subin())
				if (include.isAssignableFrom(klass))
					return true;

			return false;
		}
	}
}
