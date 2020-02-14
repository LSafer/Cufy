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
public class ConverterTest {
	private TestConverter caster = new TestConverter();

	@Test(timeout = 50)
	public void cast() {
		Assert.assertEquals("Wrong cast", "abc", caster.convert(new StringBuilder("abc"), String.class));
	}

	@Test(expected = ClassConversionException.class)
	public void castElse() {
		caster.convert(new Object(), List.class);
	}

	@Test(timeout = 50)
	public void castNull() {
		try {
			caster.convert(null, String.class);
		} catch (ClassConversionException e) {
			Assert.assertSame("Not the targeted exception", NullPointerException.class, e.getCause().getClass());
		}
	}

	public static class TestConverter extends Converter {
		@ConvertMethod(in = @Type(subin = CharSequence.class), out = @Type(in = String.class))
		public String c0(CharSequence object, Class<String> klass, ConvertPosition position) {
			Objects.requireNonNull(position, "position");
			Objects.requireNonNull(object, "object");
			return String.valueOf(object);
		}

		@ConvertMethod(in = @Type(in = Void.class), out = @Type(subin = Object.class))
		protected <T> T c1(Object object, Class<?> out, ConvertPosition position) {
			throw new NullPointerException();
		}
	}
}
