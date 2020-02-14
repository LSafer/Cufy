/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package org.cufy.util.function;

import cufy.util.Throwable$;

import java.util.function.Supplier;

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <T> the type of results supplied by this supplier
 * @param <E> the exception
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingSupplier<T, E extends Throwable> extends Supplier<T> {
	@Override
	default T get() {
		try {
			return this.get0();
		} catch (Throwable e) {
			throw Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * Gets a result.
	 *
	 * @return a result
	 * @throws E the exception
	 */
	T get0() throws E;
}