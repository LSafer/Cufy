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

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class WhileTest {
	@Test(timeout = 50)
	public void start() {
		StringBuilder builder = new StringBuilder(3);
		List<String> list = new ArrayList<>(Arrays.asList("A", "B", "C"));
		Iterator<String> iterator = list.iterator();
		new While(iterator::hasNext, loop -> builder.append(iterator.next())).append(loop -> iterator.remove()).start();
		Assert.assertEquals("While not worked correctly", "ABC", builder.toString());
		Assert.assertTrue("While not worked correctly", list.isEmpty());
	}
}
