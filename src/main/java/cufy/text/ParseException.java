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
package cufy.text;

import cufy.util.StringUtil;

/**
 * Thrown to indicate that the application has attempted to convert a string to an object, but that string does not have the appropriate type.
 *
 * @author LSaferSE
 * @version 2 release (26-Nov-2019)
 * @since 16-Nov-2019
 */
public class ParseException extends RuntimeException {
	/**
	 * The string that have causes the exception while parsing it.
	 */
	protected CharSequence sequence;

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg the message
	 */
	public ParseException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param cause the cause
	 */
	public ParseException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg   the message
	 * @param cause the cause
	 */
	public ParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg      the message9
	 * @param sequence the string caused the exception while parsing it
	 */
	public ParseException(String msg, CharSequence sequence) {
		super(msg + ": " + StringUtil.logging(sequence));
		this.sequence = sequence;
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param cause    the cause
	 * @param sequence the string caused the exception while parsing it
	 */
	public ParseException(Throwable cause, CharSequence sequence) {
		super(cause);
		this.sequence = sequence;
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg      the message
	 * @param cause    the cause
	 * @param sequence the string caused the exception while parsing it
	 */
	public ParseException(String msg, Exception cause, CharSequence sequence) {
		super(msg + ": " + StringUtil.logging(sequence), cause);
		this.sequence = sequence;
	}

	/**
	 * Get the string that have causes the exception while parsing it.
	 *
	 * @return the string that have causes the exception while parsing it.
	 */
	public CharSequence getSequence() {
		return this.sequence;
	}
}
