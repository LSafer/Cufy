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
package cufy.text;

import cufy.util.StringUtil;

/**
 * Thrown to indicate that the application has attempted to convert an object to a string (or string to object), but that object does not have the
 * appropriate type.
 *
 * @author LSaferSE
 * @version 2 release (26-Nov-2019)
 * @since 18-Nov-2019
 */
public class FormatException extends RuntimeException {
	/**
	 * The object that have causes the exception while formatting it.
	 */
	protected Object object;

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg the message
	 */
	public FormatException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param cause the cause
	 */
	public FormatException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg   the message
	 * @param cause the cause
	 */
	public FormatException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg    the message
	 * @param object the object caused the exception while formatting it
	 */
	public FormatException(String msg, Object object) {
		super(msg + ": " + StringUtil.logging(object));
		this.object = object;
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param cause  the cause
	 * @param object the object caused the exception while formatting it
	 */
	public FormatException(Throwable cause, Object object) {
		super(cause);
		this.object = object;
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg    the message
	 * @param cause  the cause
	 * @param object the object caused the exception while formatting it
	 */
	public FormatException(String msg, Throwable cause, Object object) {
		super(msg + ": " + StringUtil.logging(object), cause);
		this.object = object;
	}

	/**
	 * Get the object that have causes the exception while formatting it.
	 *
	 * @return the cause object.
	 */
	public Object getObject() {
		return this.object;
	}
}
