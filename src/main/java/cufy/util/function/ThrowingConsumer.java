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

import java.util.function.Consumer;

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <E> the exception
 * @param <T> the type of the input to the operation
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> extends Consumer<T> {
	@Override
	default void accept(T t) {
		try {
			this.accept0(t);
		} catch (Throwable e) {
			Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * Performs this operation on the given argument.
	 *
	 * @param t the input argument
	 * @throws E the exception that this runnable throws
	 */
	void accept0(T t) throws E;
}
