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

package cufy.beans;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("JavaDoc")
public class InvokeTest {
	protected TestInvoke dynamic = new TestInvoke();

	@Test(timeout = 50)
	public void getMethodGroup() throws InvocationTargetException, IllegalAccessException {
		for (Method method : dynamic.getMethods().getMethodGroup(TestMethod0.class))
			Assert.assertTrue("Not targeted", ((String) method.invoke(dynamic)).contains("work0"));

		for (Method method : dynamic.getMethods().getMethodGroup(TestMethod1.class))
			Assert.assertTrue("Not targeted", ((String) method.invoke(dynamic)).contains("work1"));

		for (Method method : dynamic.getMethods().getMethodGroup(TestMethod2.class))
			Assert.assertTrue("Not targeted", ((String) method.invoke(dynamic)).contains("work2"));
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface TestMethod0 {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface TestMethod1 {
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface TestMethod2 {
	}

	public static class TestInvoke extends Invoke {
		@TestMethod0
		@StaticMethod
		protected String banned() {
			throw new IllegalAccessError();
		}

		@TestMethod1
		@TestMethod2
		protected String hybrid12() {
			return "work1work2";
		}

		@StaticMethod
		protected String ignored() {
			throw new IllegalAccessError();
		}

		@TestMethod0
		protected String method0() {
			return "work0";
		}

		@TestMethod1
		protected String method1() {
			return "work1";
		}

		@TestMethod2
		protected String method2() {
			return "work2";
		}
	}
}
