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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaDoc")
public class JSONTest {
	@Test
	public void format_map_array_nested() {
		Map<Object, Object> base = new HashMap<>(3);
		Map<Object, Object> map = new HashMap<>(3);
		base.put("map", map);
		map.put("number", Arrays.asList(9, 3, 5));

		String expected = "{\n" +
						  "\t\"map\":{\n" +
						  "\t\t\"number\":[\n" +
						  "\t\t\t9,\n" +
						  "\t\t\t3,\n" +
						  "\t\t\t5\n" +
						  "\t\t]\n" +
						  "\t}\n" +
						  "}";
		String actual = JSON.global.format(base);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void parse_map_array_nested() {
		Map<String, Map<String, List<Number>>> val = (Map<String, Map<String, List<Number>>>) JSON.global.parse("{\"map\":{\"number\":[9, 3, 5]}}");
		Map<String, List<Number>> map = val.get("map");
		List<Number> number = map.get("number");
		Assert.assertEquals(9L, number.get(0).longValue());
		Assert.assertEquals(3L, number.get(1).longValue());
		Assert.assertEquals(5L, number.get(2).longValue());
	}
}
