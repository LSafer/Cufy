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

import java.util.function.BiFunction;

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <E> the exception
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingBiFunction<T, U, R, E extends Throwable> extends BiFunction<T, U, R> {
	@Override
	default R apply(T t, U u) {
		try {
			return this.apply0(t, u);
		} catch (Throwable e) {
			throw Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t the first function argument
	 * @param u the second function argument
	 * @return the function result
	 * @throws E the exception
	 */
	R apply0(T t, U u) throws E;
}
