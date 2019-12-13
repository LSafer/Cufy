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

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("JavaDoc")
public class RangeTest {
	@Test(timeout = 50)
	public void in_out_subin_subout() {
		Range range = this.newRange(
				new Class[]{Map.class}, //in
				new Class[]{HashMap.class}, //out
				new Class[]{List.class}, //subin
				new Class[]{CharSequence.class} //subout
		);

		//in
		Assert.assertTrue("Map is absolute included", Range.util.test(range, Map.class));
		Assert.assertFalse("Number is not included anywhere", Range.util.test(range, Number.class));
		//out
		Assert.assertFalse("HashMap is absolute excluded", Range.util.test(range, HashMap.class));
		//subin
		Assert.assertTrue("List is absolute included", Range.util.test(range, List.class));
		Assert.assertTrue("ArrayList is a sub included", Range.util.test(range, ArrayList.class));
		//subout
		Assert.assertFalse("CharSequence is absolute excluded", Range.util.test(range, CharSequence.class));
		Assert.assertFalse("String is sub excluded", Range.util.test(range, String.class));
	}

	private Range newRange(Class<?>[] in, Class<?>[] out, Class<?>[] subin, Class<?>[] subout) {
		return new Range() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return null;
			}

			@Override
			public Class<?>[] in() {
				return in == null ? new Class[]{} : in;
			}

			@Override
			public Class<?>[] out() {
				return out == null ? new Class[]{} : out;
			}

			@Override
			public Class<?>[] subin() {
				return subin == null ? new Class[]{} : subin;
			}

			@Override
			public Class<?>[] subout() {
				return subout == null ? new Class[]{} : subout;
			}
		};
	}
}
