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

package cufy.lang.alter;

import cufy.lang.Loop;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("JavaDoc")
public class ParallelTest {
	@Test(timeout = 110)
	public void start() throws InterruptedException {
		Thread main = Thread.currentThread();
		Parallel parallel = new Parallel();
		AtomicBoolean state = new AtomicBoolean(true);
		Thread thread = new Thread(() -> {
			parallel.start();
			state.set(false);
		});
		thread.start();
		Thread.sleep(50);
		parallel.notify(Loop.BREAK);
		parallel.join();
		Thread.sleep(10);
		Assert.assertFalse("Thread death took too long after the death of the loop", state.get());
	}
}
