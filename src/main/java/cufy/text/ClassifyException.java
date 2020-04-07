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
package cufy.text;

/**
 * Thrown to indicate that the application has attempted to classify a string, but that string does not have the appropriate type.
 *
 * @author LSaferSE
 * @version 3 release (31-Mar-2020)
 * @since 31-Mar-2020
 */
public class ClassifyException extends RuntimeException {
	/**
	 * Constructs a new exception with null as its detail message. The cause is not initialized, and may subsequently be initialized by a call to
	 * Throwable.initCause(java.lang.Throwable).
	 */
	public ClassifyException() {
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg the message
	 */
	public ClassifyException(String msg) {
		super(msg);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param cause the cause
	 */
	public ClassifyException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs a new exception with the specified arguments.
	 *
	 * @param msg   the message
	 * @param cause the cause
	 */
	public ClassifyException(String msg, Throwable cause) {
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
	protected ClassifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
