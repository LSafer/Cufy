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

import cufy.lang.alter.Cast;
import org.cufy.util.ObjectUtil;

/**
 * Defines that the implement class is a {@link Caster} user. Added more methods to direct use the {@link Caster}.
 *
 * @author LSaferSE
 * @version 3 release (07-Dec-2019)
 * @since 31-Aug-19
 */
public interface Castable {
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
		ObjectUtil.requireNonNull(klass, "klass");
		return this.caster().cast(this, klass, null, null, false);
	}

	/**
	 * Override the casting process to cast this object to the given class using this method.
	 *
	 * @param klass    to cast this object to
	 * @param position to start with
	 * @param <T>      the targeted type
	 * @return this object casted to the given class. Or null if this fails to cast
	 * @throws ClassCastException   on casting failure
	 * @throws NullPointerException if the given class or the given position are null
	 * @apiNote this is a class based casting. And it don't use the caster of this object. so PLEASE DON'T USE IT DIRECTLY
	 * @implNote this method will override caster methods. Here is a trick. Return an object contained on this. Represents this. And extends the given
	 * class.
	 * @implSpec Return null to skip to the caster casting methods.
	 */
	default <T> T castTo(Class<? super T> klass, Caster.CastPosition position) {
		ObjectUtil.requireNonNull(klass, "klass");
		ObjectUtil.requireNonNull(position, "position");
		return null;
	}

	/**
	 * Get the caster used by this.
	 *
	 * @return the caster used by this
	 */
	default Caster caster() {
		return Cast.global;
	}

	/**
	 * Clone this class into a new instance of the given class.
	 *
	 * @param klass to get a new instance of (or null for the class of this instance)
	 * @param <T>   type of the new clone
	 * @return a new clone casted from this to the given class
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 */
	default <T> T clone(Class<? super T> klass) {
		return this.caster().cast(this, klass == null ? (Class<? super T>) this.getClass() : klass, null, null, true);
	}
}
