/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.cnv;

import cufy.meta.MetaFamily;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Navigate the {@link AbstractConverter} class that the annotated method is a converting method.
 *
 * @author LSaferSE
 * @version 1 release (30-Mar-2020)
 * @apiNote the annotated method SHOULD match the {@link AbstractConverter#convert0} rules
 * @since 30-Mar-2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ConvertMethod {
	/**
	 * The classes that the annotated method can accept as a parameter.
	 *
	 * @return the input type range
	 */
	MetaFamily input();
	/**
	 * The classes that the annotated method can return.
	 *
	 * @return the output type range
	 */
	MetaFamily output();
}
