/*
 *	Copyright 2020 Cufyorg
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package cufy.lang;

/**
 * An exception thrown for concurrent errors.
 *
 * @author LSaferSE
 * @version 2 release (07-Dec-2019)
 * @since 07-Dec-2019
 */
public class ConcurrentException extends RuntimeException {
	/**
	 * Constructs a new concurrent exception with null as its detail message. The cause is not initialized, and may subsequently be initialized by a
	 * call to Throwable.initCause(java.lang.Throwable).
	 */
	public ConcurrentException() {
	}

	/**
	 * Constructs a new concurrent exception with the specified detail message. The cause is not initialized, and may subsequently be initialized by a
	 * call to Throwable.initCause(java.lang.Throwable).
	 *
	 * @param message the detail message. The detail message is saved for later retrieval by the Throwable.getMessage() method.
	 */
	public ConcurrentException(String message) {
		super(message);
	}

	/**
	 * Constructs a new concurrent exception with the specified detail message and cause. Note that the detail message associated with cause is not
	 * automatically incorporated in this concurrent exception's detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the Throwable.getMessage() method).
	 * @param cause   the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates
	 *                that the cause is nonexistent or unknown.)
	 */
	public ConcurrentException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new concurrent exception with the specified cause and a detail message of (cause==null ? null : cause.toString()) (which typically
	 * contains the class and detail message of cause). This constructor is useful for concurrent exceptions that are little more than wrappers for
	 * other throwables.
	 *
	 * @param cause the cause (which is saved for later retrieval by the Throwable.getCause() method). (A null value is permitted, and indicates that
	 *              the cause is nonexistent or unknown.)
	 */
	public ConcurrentException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new concurrent exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace
	 * enabled or disabled.
	 *
	 * @param message            the detail message.
	 * @param cause              the cause. (A null value is permitted, and indicates that the cause is nonexistent or unknown.)
	 * @param enableSuppression  whether or not suppression is enabled or disabled
	 * @param writableStackTrace whether or not the stack trace should be writable
	 */
	protected ConcurrentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
