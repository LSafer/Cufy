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

package org.cufy.misc;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

@SuppressWarnings("JavaDoc")
public class IdentifierTest {
	@Test
	public void no_match() {
		Identifier<Integer, Integer> identifier = new Identifier<>(0, (i, fl)-> i+fl[0]);

		HashSet<Integer> used = new HashSet<>(10);

		for (int i=0; i<100; i++)
			Assert.assertTrue(used.add(identifier.next(3)));

		Assert.assertEquals((Integer) 300, identifier.next(0));
	}
}
