/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.meta;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaDoc")
public class MetaFamilyTest {
	@Test(timeout = 50)
	@MetaFamily(
			in = Map.class,
			out = HashMap.class,
			subin = List.class,
			subout = CharSequence.class
	)
	public void in_out_subin_subout() throws NoSuchMethodException {
		MetaFamily type = this.getClass().getMethod("in_out_subin_subout").getAnnotation(MetaFamily.class);

		//in
		Assert.assertTrue("Map is absolute included", MetaFamily.util.test(type, Map.class));
		Assert.assertFalse("Number is not included anywhere", MetaFamily.util.test(type, Number.class));
		//out
		Assert.assertFalse("HashMap is absolute excluded", MetaFamily.util.test(type, HashMap.class));
		//subin
		Assert.assertTrue("List is absolute included", MetaFamily.util.test(type, List.class));
		Assert.assertTrue("ArrayList is a sub included", MetaFamily.util.test(type, ArrayList.class));
		//subout
		Assert.assertFalse("CharSequence is absolute excluded", MetaFamily.util.test(type, CharSequence.class));
		Assert.assertFalse("String is sub excluded", MetaFamily.util.test(type, String.class));
	}

	@Test
	public void test() {
		//TODO
	}
}
