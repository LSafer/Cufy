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
public class GlobalTest {
	@Test(timeout = 50)
	public void get() {
		Assert.assertSame("Wrong instance", Global.get(TestGlobal.class), TestGlobal.global);
	}

	public static class TestGlobal implements Global {
		final private static TestGlobal global = new TestGlobal();
	}
}
