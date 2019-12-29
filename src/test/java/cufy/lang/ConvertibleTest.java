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

@SuppressWarnings("JavaDoc")
public class ConvertibleTest {
	@Test(timeout = 50)
	public void castTo() {
		TestConvertibleA castableA = new TestConvertibleA();
		TestConvertibleB castableB = castableA.as(TestConvertibleB.class);

		Assert.assertEquals("Wrong instance", castableB, castableA.as(TestConvertibleB.class));
		Assert.assertEquals("Wrong instance", castableA, castableB.as(TestConvertibleA.class));

		try {
			castableA.as(Integer.class);
			Assert.fail("Can't cast");
		} catch (ClassCastException ignored) {
		}
		try {
			castableB.as(Integer.class);
			Assert.fail("Can't cast");
		} catch (ClassCastException ignored) {
		}
	}

	static class TestConvertibleA implements Convertible {
		final TestConvertibleB self;

		TestConvertibleA() {
			this.self = new TestConvertibleB(this);
		}

		TestConvertibleA(TestConvertibleB self) {
			if (self.self != null)
				throw new IllegalArgumentException();
			this.self = self;
		}

		@Override
		public <T> T convertTo(Class<? super T> klass, Converter.ConvertPosition position) {
			return klass == TestConvertibleB.class ? (T) this.self : null;
		}
	}

	static class TestConvertibleB implements Convertible {
		final TestConvertibleA self;

		TestConvertibleB() {
			this.self = new TestConvertibleA(this);
		}

		TestConvertibleB(TestConvertibleA self) {
			if (self.self != null)
				throw new IllegalArgumentException();
			this.self = self;
		}

		@Override
		public <T> T convertTo(Class<? super T> klass, Converter.ConvertPosition position) {
			return klass == TestConvertibleA.class ? (T) this.self : null;
		}
	}
}
