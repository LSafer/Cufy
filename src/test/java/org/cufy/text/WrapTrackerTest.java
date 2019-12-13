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

package org.cufy.text;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("JavaDoc")
public class WrapTrackerTest {
	@Test
	public void append_length() {
		WrapTracker tracker = new WrapTracker();
		tracker.append("{");
		tracker.append("e");
		tracker.append("[");
		tracker.append("m");
		tracker.append("<");
		tracker.append("w");
		tracker.append("\"");
		tracker.append("\\");
		tracker.append("'");

		Assert.assertEquals(4, tracker.length());

		tracker.append("\"");
		tracker.append(">");
		tracker.append("]");
		tracker.append("}");

		Assert.assertEquals(0, tracker.length());
	}
}
