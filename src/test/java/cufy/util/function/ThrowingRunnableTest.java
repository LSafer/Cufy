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

import org.junit.Test;

import java.io.IOException;

@SuppressWarnings("JavaDoc")
public class ThrowingRunnableTest {
	@Test(expected = IOException.class)
	public void run() {
		//noinspection RedundantCast
		((Runnable) (ThrowingRunnable<IOException>) () -> {
			throw new IOException();
		}).run();
	}
}
