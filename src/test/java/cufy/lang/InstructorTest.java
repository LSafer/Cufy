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

package cufy.lang;

import cufy.lang.alter.Parallel;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("JavaDoc")
public class InstructorTest {
	@Test(timeout = 50)
	public void join() {
		Instructor group = new Instructor();
		Parallel parallel1 = new Parallel(group::tick);
		Parallel parallel2 = new Parallel(group::tick);

		group.thread(parallel1);
		group.thread(parallel2);
		group.pair();

		Assert.assertEquals("Group should have 2 loops", 2, group.getLoops().size());
		Assert.assertTrue("Loops should be alive", parallel1.isAlive() & parallel2.isAlive());

		group.notify(Loop.BREAK);

		group.join();

		Assert.assertEquals("Group still have loops", 0, group.getLoops().size());
		Assert.assertFalse("Loops should be dead", parallel1.isAlive() | parallel2.isAlive());
	}

	@Test(timeout = 20)
	public void synchronously() {
		Instructor group = new Instructor();
		Parallel parallel = new Parallel(group::tick);
		boolean[] w = {false};

		group.thread(parallel);
		group.pair();

		Assert.assertEquals("Group should have 1 loop", 1, group.getLoops().size());
		Assert.assertTrue("loop should be alive", parallel.isAlive());

		group.synchronously((g, loop) -> w[0] = true);

		Assert.assertTrue("Value not changed synchronously", w[0]);

		group.notify(Loop.BREAK);
	}
}
