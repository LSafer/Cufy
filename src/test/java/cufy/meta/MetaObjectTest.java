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

import cufy.convert.BaseConverter;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("JavaDoc")
public class MetaObjectTest {
	@Test
	@MetaObject(
			value = "3",
			type = @MetaClazz(Integer.class),
			converter = @MetaReference(type = BaseConverter.class)
	)
	public void get() throws NoSuchMethodException {
		MetaObject object = this.getClass().getMethod("get").getAnnotation(MetaObject.class);
		int i = MetaObject.util.get(object);

		Assert.assertEquals("Wrong value", 3, i, 0);

		//TODO
	}
}
