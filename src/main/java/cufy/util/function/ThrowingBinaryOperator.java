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

import java.util.function.BinaryOperator;

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <T> the type of the operands and result of the operator
 * @param <E> the exception
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingBinaryOperator<T, E extends Throwable> extends BinaryOperator<T> {
	@Override
	default T apply(T t, T t2) {
		try {
			return this.apply0(t, t2);
		} catch (Throwable e) {
			throw Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * Applies this function to the given arguments.
	 *
	 * @param t  the first function argument
	 * @param t2 the second function argument
	 * @return the function result
	 * @throws E the exception
	 */
	T apply0(T t, T t2) throws E;
}
