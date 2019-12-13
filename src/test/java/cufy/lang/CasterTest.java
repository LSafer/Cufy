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

import java.util.List;
import java.util.Objects;

@SuppressWarnings("JavaDoc")
public class CasterTest {
	private TestCaster caster = new TestCaster();

	@Test(timeout = 50)
	public void cast() {
		Assert.assertEquals("Wrong cast", "abc", caster.cast(new StringBuilder("abc"), String.class));
	}

	@Test(expected = ClassCastException.class)
	public void castElse() {
		caster.cast(new Object(), List.class);
	}

	@Test(timeout = 50, expected = NullPointerException.class)
	public void castNull() {
		caster.cast(null, String.class);
	}

	public static class TestCaster extends Caster {
		@Override
		protected <T> T castNull(Class<?> out, CastPosition position) {
			throw new NullPointerException();
		}

		@CastMethod(in = @Range(subin = CharSequence.class), out = @Range(in = String.class))
		public String cast(CharSequence object, Class<String> klass, CastPosition position) {
			Objects.requireNonNull(position, "position");
			Objects.requireNonNull(object, "object");
			return String.valueOf(object);
		}
	}
}
