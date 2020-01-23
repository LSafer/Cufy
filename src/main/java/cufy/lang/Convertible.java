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

import java.util.Objects;

/**
 * Defines that the implement class is a {@link Converter} user. Added more methods to direct use the {@link Converter}.
 *
 * @author LSaferSE
 * @version 4 beta (18-Jan-2020)
 * @since 31-Aug-19
 */
public interface Convertible {
	/**
	 * Cast this instance to the given class. And if this isn't instance of the given class then make a new instance of the given class with data of
	 * this. By using the caster of this
	 *
	 * @param klass to be casted to
	 * @param <T>   the type of the returned instance
	 * @return this casted to the given class
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if the given class is null
	 */
	default <T> T as(Class<? super T> klass) {
		Objects.requireNonNull(klass, "klass");
		return this.converter().convert(this, klass, null, null, false);
	}

	/**
	 * Clone this class into a new instance of the given class.
	 *
	 * @param klass to get a new instance of
	 * @param <T>   type of the new clone
	 * @return a new clone casted from this to the given class
	 * @throws NullPointerException     if the given class is null
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 */
	default <T> T clone(Class<? super T> klass) {
		Objects.requireNonNull(klass, "klass");
		return this.converter().convert(this, klass, null, null, true);
	}

	/**
	 * Get the caster used by this.
	 *
	 * @return the caster used by this
	 */
	Converter converter();
}
