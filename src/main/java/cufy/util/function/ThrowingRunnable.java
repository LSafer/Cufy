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

/**
 * Functional Interface that can be specified to throw an exception.
 *
 * @param <E> the exception
 * @author LSaferSE
 * @version 1 release (13-Feb-2020)
 * @since 13-Feb-2020
 */
@FunctionalInterface
public interface ThrowingRunnable<E extends Throwable> extends Runnable {
	@Override
	default void run() {
		try {
			this.run0();
		} catch (Throwable e) {
			Throwable$.<Error>ignite(e);
		}
	}

	/**
	 * When an object implementing interface Runnable is used to create a thread, starting the thread causes the object's run method to be called in
	 * that separately executing thread. The general contract of the method run is that it may take any action whatsoever.
	 *
	 * @throws E the exception that this runnable throws
	 */
	void run0() throws E;
}
