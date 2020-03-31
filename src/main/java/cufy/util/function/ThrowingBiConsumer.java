/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.util.function;

import cufy.util.Throwable$;

import java.util.function.BiConsumer;

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <E> the exception
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Throwable> extends BiConsumer<T, U> {
	@Override
	default void accept(T t, U u) {
		try {
			this.accept0(t, u);
		} catch (Throwable e) {
			Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * Performs this operation on the given arguments. Parameters:
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @throws E the exception
	 */
	void accept0(T t, U u) throws E;
}
