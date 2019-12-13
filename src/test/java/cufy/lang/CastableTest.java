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
public class CastableTest {
	@Test(timeout = 50)
	public void castTo() {
		TestCastableA castableA = new TestCastableA();
		TestCastableB castableB = castableA.as(TestCastableB.class);

		Assert.assertEquals("Wrong instance", castableB, castableA.as(TestCastableB.class));
		Assert.assertEquals("Wrong instance", castableA, castableB.as(TestCastableA.class));

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

	static class TestCastableA implements Castable {
		final TestCastableB self;

		TestCastableA() {
			this.self = new TestCastableB(this);
		}

		TestCastableA(TestCastableB self) {
			if (self.self != null)
				throw new IllegalArgumentException();
			this.self = self;
		}

		@Override
		public <T> T castTo(Class<? super T> klass, Caster.CastPosition position) {
			return klass == TestCastableB.class ? (T) this.self : null;
		}
	}

	static class TestCastableB implements Castable {
		final TestCastableA self;

		TestCastableB() {
			this.self = new TestCastableA(this);
		}

		TestCastableB(TestCastableA self) {
			if (self.self != null)
				throw new IllegalArgumentException();
			this.self = self;
		}

		@Override
		public <T> T castTo(Class<? super T> klass, Caster.CastPosition position) {
			return klass == TestCastableA.class ? (T) this.self : null;
		}
	}
}
