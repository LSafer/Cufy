/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.text;

import cufy.meta.MetaFamily;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Navigate the {@link AbstractFormat} class that the annotated method is a parsing method.
 *
 * @author LSaferSE
 * @version 1 release (30-Mar-2020)
 * @apiNote the annotated method SHOULD match the {@link AbstractFormat#parse0} rules
 * @since 30-Mar-2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParseMethod {
	/**
	 * Classes that the annotated method returns.
	 *
	 * @return the classes that the annotated method returns
	 */
	MetaFamily value() default @MetaFamily;
}
