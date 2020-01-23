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
package cufy.lang;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Global instance getter. For those classes that have a one global instance.
 *
 * @author LSaferSE
 * @version 3 release (18-Jan-2020)
 * @since 07-Nov-2019
 */
public interface Global {
	/**
	 * Get the global instance for the given class.
	 *
	 * @param klass to get the global instance of
	 * @param <G>   the type of the class
	 * @return the global instance of the given class
	 * @throws NullPointerException     if the given class is null
	 * @throws IllegalArgumentException if {@link Global} class isn't assignable from the given class.
	 * @throws NoSuchFieldError         if the given class don't have a static field with the name "global"
	 * @throws IllegalAccessError       if the global instance's field of the given class can't be accessed.
	 */
	static <G> G get(Class<G> klass) {
		Objects.requireNonNull(klass, "klass");

		if (!Global.class.isAssignableFrom(klass))
			throw new IllegalArgumentException(Global.class + " can't be assignable from " + klass);

		try {
			Field field = klass.getField("global");
			field.setAccessible(true);
			return (G) field.get(null);
		} catch (NoSuchFieldException e) {
			try {
				Field field = klass.getDeclaredField("global");

				field.setAccessible(true);
				return (G) field.get(null);
			} catch (NoSuchFieldException ex) {
				throw (NoSuchFieldError) new NoSuchFieldError().initCause(ex);
			} catch (IllegalAccessException ex) {
				throw (IllegalAccessError) new IllegalAccessError().initCause(ex);
			}
		} catch (IllegalAccessException e) {
			throw (IllegalAccessError) new IllegalAccessError().initCause(e);
		}
	}

	/**
	 * Get the global instance of the class of this.
	 *
	 * @param <G> the type of the class
	 * @return the global instance of the class of this
	 * @throws NoSuchFieldError   if the given class don't have a static field with the name "global"
	 * @throws IllegalAccessError if the global instance's field of the given class can't be accessed.
	 */
	default <G extends Global> G global() {
		return (G) Global.get(this.getClass());
	}
}
