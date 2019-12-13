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

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("JavaDoc")
public class LockTest {
	@Test(timeout = 50)
	public void lock_release_close() throws InterruptedException {
		AtomicInteger integer = new AtomicInteger(0);
		Lock<Object> lock = new Lock<>(integer);

		Parallel parallel = new Parallel(loop -> {
			synchronized (integer) {
				integer.addAndGet(1);
			}
		});

		parallel.thread();
		parallel.pair();

		lock.lock();

		int current = integer.get();

		Thread.sleep(5);

		Assert.assertEquals("Lock not locked", current, integer.get());

		lock.release();

		Thread.sleep(5);

		synchronized (integer) {
			Assert.assertNotEquals("Lock not released", current, integer.get());
		}

		parallel.notify(Loop.BREAK);
		parallel.join();
		lock.close();

		Assert.assertFalse("Lock not closed", lock.isAlive());

		try {
			lock.lock();
			Assert.fail("Already closed");
		} catch (Exception ignored) {
		}
	}

	@Test(timeout = 50)
	public void wrong_caller() throws InterruptedException {
		Lock[] lock = new Lock[1];

		Thread thread = new Thread(() -> lock[0] = new Lock<>());
		thread.start();
		thread.join();

		try {
			//noinspection CallToThreadRun
			lock[0].run();
			Assert.fail("Wrong caller test fail");
		} catch (Exception ignored) {
		}
		try {
			lock[0].lock();
			Assert.fail("Wrong caller test fail");
		} catch (Exception ignored) {
		}
		try {
			lock[0].release();
			Assert.fail("Wrong caller test fail");
		} catch (Exception ignored) {
		}
	}
}
