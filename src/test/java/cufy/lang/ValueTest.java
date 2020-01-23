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

import org.junit.Test;

import java.lang.annotation.Annotation;

@SuppressWarnings("JavaDoc")
public class ValueTest {
	@Test(timeout = 50)
	public void get() {
		//TODO
//		Value typedValue = newTypedValue(null, Integer[].class, "[3, 5, 2]", false);
//		Integer[] value = (Integer[]) Value.util.construct(typedValue);
//
//		Assert.assertNotNull("Null not expected", value);
//		Assert.assertEquals("Wrong length", 3, value.length);
//		Assert.assertEquals("Wrong 1st element", (Integer) 3, value[0]);
//		Assert.assertEquals("Wrong 2nd element", (Integer) 5, value[1]);
//		Assert.assertEquals("Wrong 3rd element", (Integer) 2, value[2]);
	}

	public Value newTypedValue(Class<? extends Converter> converter, Class<?> type, String value, boolean isnull) {
		return new Value() {
			@Override
			public Class<? extends Annotation> annotationType() {
				return Value.class;
			}

			@Override
			public Class<? extends Converter> converter() {
				return converter;
			}

			/**
			 * Whether this value equals to null or not.
			 *
			 * @return true if this value equals to null
			 */
			@Override
			public boolean isnull() {
				return isnull;
			}

			@Override
			public Class<?> type() {
				return type == null ? String.class : type;
			}

			@Override
			public String value() {
				return value;
			}
		};
	}
}
