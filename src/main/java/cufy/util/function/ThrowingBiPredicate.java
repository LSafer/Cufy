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

import java.util.function.BiPredicate;

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <T> the type of the first argument to the predicate
 * @param <U> the type of the second argument the predicate
 * @param <E> the exception
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingBiPredicate<T, U, E extends Throwable> extends BiPredicate<T, U> {
	@Override
	default boolean test(T t, U u) {
		try {
			return this.test0(t, u);
		} catch (Throwable e) {
			throw Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * Evaluates this predicate on the given arguments.
	 *
	 * @param t the first input argument
	 * @param u the second input argument
	 * @return true if the input arguments match the predicate, otherwise false
	 * @throws E the exception
	 */
	boolean test0(T t, U u) throws E;
}
