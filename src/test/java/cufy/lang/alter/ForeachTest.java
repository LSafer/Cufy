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
import java.util.List;

@SuppressWarnings("JavaDoc")
public class ForeachTest {
	@Test(timeout = 50)
	public void start() {
		StringBuilder builder = new StringBuilder(3);
		List<String> list = Arrays.asList("A", "B", "C");
		List<String> list1 = new ArrayList<>(3);
		new Foreach<>(list, (loop, s) -> builder.append(s)).append((loop, s) -> list1.add(s)).start();

		Assert.assertEquals("Foreach not looped correctly", "ABC", builder.toString());
		Assert.assertEquals("Foreach not looped correctly", list, list1);
	}
}
