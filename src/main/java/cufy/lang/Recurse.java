/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *  shall mention that this file has been edited.
 *  By adding a new header (at the bottom of this header)
 *  with the word "Editor" on top of it.
 */
package cufy.lang;

/**
 * A representation for a recursion occurred.
 *
 * @author LSaferSE
 * @version 2 release (30-Mar-2020)
 * @since 25-Nov-2019
 */
final public class Recurse {
	/**
	 * This is a representation class and should not be instanced.
	 *
	 * @throws AssertionError when called
	 */
	private Recurse() {
		throw new AssertionError("No instance for you!");
	}
}
