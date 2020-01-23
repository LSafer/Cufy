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
public class SERB64Test {
	@Test
	public void array_format_parse() {
		String[] original = {"A", "B", "C"};
		String formatted = SERB64.global.format(original);
		String[] parsed = (String[]) SERB64.global.parse(formatted);
		Assert.assertArrayEquals("original don't match the formatted then parsed object", original, parsed);
	}
}
