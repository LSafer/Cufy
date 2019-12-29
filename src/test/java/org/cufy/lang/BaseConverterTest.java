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

package org.cufy.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

@SuppressWarnings("JavaDoc")
public class BaseConverterTest {
	@Test(timeout = 50)
	public void array2map() {
		Object[] array = {0, "a", 1, "b"};
		HashMap<Integer, ?> map = BaseConverter.global.convert(array, HashMap.class);

		Assert.assertEquals("First element not included", 0, map.get(0));
		Assert.assertEquals("Second element not included", "a", map.get(1));
		Assert.assertEquals("Third element not included", 1, map.get(2));
		Assert.assertEquals("Fourth element not included", "b", map.get(3));
	}

	@Test(timeout = 50)
	public void map2array() {
		HashMap<Object, Object> map = new HashMap<>(0);
		map.put("a", 1);
		map.put(-1, "n-one");
		map.put(1, "one");
		map.put(7, "seven");

		String[] strings = BaseConverter.global.convert(map, String[].class);

		Assert.assertEquals("wrong size", 8, strings.length);
		Assert.assertNull("No spacing added", strings[0]);
		Assert.assertEquals("First element not included", "one", strings[1]);
		Assert.assertNull("No spacing added", strings[2]);
		Assert.assertEquals("Seventh element not included", "seven", strings[7]);
	}
}
