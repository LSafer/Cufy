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

import cufy.util.ObjectUtil;

import java.lang.reflect.Field;

/**
 * Global instance getter. For those classes that have a one global instance.
 *
 * @author LSaferSE
 * @version 2 release (26-Nov-2019)
 * @since 07-Nov-2019
 */
public interface Global {
	/**
	 * Get the global instance for the given class.
	 *
	 * @param klass to get the global instance of
	 * @param <G>   the type of the class
	 * @return the global instance of the given class
	 * @throws RuntimeException     if the global instance is private. Or if no global instance found.
	 * @throws NullPointerException if the given class is null
	 */
	static <G> G get(Class<G> klass) {
		ObjectUtil.requireNonNull(klass, "klass");

		if (Global.class.isAssignableFrom(klass))
			try {
				return (G) klass.getField("global").get(null);
			} catch (IllegalAccessException | NoSuchFieldException e) {
				try {
					Field field = klass.getDeclaredField("global");
					field.setAccessible(true);
					return (G) field.get(null);
				} catch (NoSuchFieldException | IllegalAccessException ex) {
					throw new RuntimeException(ex);
				}
			}
		else throw new IllegalArgumentException(klass + " don't implement " + Global.class);
	}

	/**
	 * Get the global instance of the class of this.
	 *
	 * @param <G> the type of the class
	 * @return the global instance of the class of this
	 */
	default <G extends Global> G global() {
		return (G) Global.get(this.getClass());
	}
}
