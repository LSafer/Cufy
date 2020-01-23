/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */

package cufy.lang;

/**
 * Thrown to indicate that the code has attempted to convert an object to a class of which it can't be converted to. For example, the following code
 * generates a ClassConvertException:
 *
 * <pre>
 *     Boolean bool = true;
 *     Integer integer = SomeConverter.convert(bool, Integer.class);
 * </pre>
 *
 * @author LSaferSE
 * @version 1 beta (22-Jan-2020)
 * @see ClassCastException
 * @since 22-Jan-2020
 */
public class ClassConversionException extends RuntimeException {
	/**
	 * Constructs a new class conversion exception with null as its detail message. The cause is not initialized, and may subsequently be initialized
	 * by a call to Throwable.initCause(java.lang.Throwable).
	 */
	public ClassConversionException() {
	}

	/**
	 * Constructs a new class conversion exception with the specified detail message. The cause is not initialized, and may subsequently be
	 * initialized by a call to Throwable.initCause(java.lang.Throwable).
	 *
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ClassConversionException(String message) {
		super(message);
	}

	/**
	 * Constructs a new class conversion exception with the specified detail message and cause. Note that the detail message associated with cause is
	 * not automatically incorporated in this class conversion exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause   the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates
	 *                that the cause is nonexistent or unknown.)
	 */
	public ClassConversionException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new class conversion exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which
	 * typically contains the class and detail message of cause). This constructor is useful for class conversion exceptions that are little more than
	 * wrappers for other throwables.
	 *
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that
	 *              the cause is nonexistent or unknown.)
	 */
	public ClassConversionException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new class conversion exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace
	 * enabled or disabled.
	 *
	 * @param message            the detail message.
	 * @param cause              the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param enableSuppression  whether or not suppression is enabled or disabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 */
	protected ClassConversionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
