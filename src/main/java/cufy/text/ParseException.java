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

/**
 * Thrown to indicate that the application has attempted to convert a string to an object, but that string does not have the appropriate type.
 *
 * @author LSaferSE
 * @version 3 release (23-Jan-2020)
 * @since 16-Nov-2019
 */
public class ParseException extends RuntimeException {
	/**
	 * Constructs a new exception with null as its detail message. The cause is not initialized, and may subsequently be initialized by a call to
	 * Throwable.initCause(java.lang.Throwable).
	 */
	public ParseException() {
	}

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
	 * Constructs a new exception exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace
	 * enabled or disabled.
	 *
	 * @param message            the detail message.
	 * @param cause              the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param enableSuppression  whether or not suppression is enabled or disabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 */
	protected ParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
