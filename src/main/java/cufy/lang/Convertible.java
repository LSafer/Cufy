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
 * @param <T> the type of this
 * @author LSaferSE
 * @version 5 release (23-Jan-2020)
 * @since 31-Aug-19
 */
public interface Convertible<T extends Convertible<T>> {
	/**
	 * Cast this instance to the given class. And if this isn't instance of the given class then make a new instance of the given class with data of
	 * this. By using the {@link Converter} {@link #converter() of this}.
	 *
	 * @param klass to be casted to
	 * @param <U>   the type of the returned instance
	 * @return this. Casted/Converted to the given class
	 * @throws ClassConversionException on casting/converting failure
	 * @throws NullPointerException     if the given class is null
	 */
	default <U> U as(Class<? super U> klass) {
		Objects.requireNonNull(klass, "klass");
		return this.converter().convert(this, klass, null, null, false);
	}

	/**
	 * Clone this class into a new instance of the given class.
	 *
	 * @param klass to get a new instance of
	 * @param <U>   type of the new clone
	 * @return a new clone casted from this to the given class
	 * @throws NullPointerException     if the given class is null
	 * @throws ClassConversionException on converting failure
	 */
	default <U> U duplicate(Class<? super U> klass) {
		Objects.requireNonNull(klass, "klass");
		return this.converter().convert(this, klass, null, null, true);
	}
	/**
	 * Clone this class into a new instance of the class of this.
	 *
	 * @return a new clone of this=
	 * @throws ClassConversionException on cloning failure
	 */
	default T duplicate() {
		return this.converter().convert(this, this.getClass(), null, null, true);
	}

	/**
	 * Get the caster used by this.
	 *
	 * @return the caster used by this
	 */
	Converter converter();
}
