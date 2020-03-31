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

import java.io.IOException;
import java.io.Writer;

/**
 * A class that can format objects and write it. With just a simple gate method (for the caller).
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @since 31-Mar-2020
 */
public interface Formatter {
	/**
	 * Write the text outputted from formatting {@link FormatArguments#input} to the {@link FormatArguments#output}.
	 *
	 * @param arguments the formatting instance that holds the variables of this formatting
	 * @return the writer in presented in the arguments
	 * @throws IOException          if any I/O exception occurs
	 * @throws NullPointerException if the given 'arguments' is null
	 * @throws FormatException      if any formatting exception occurs
	 */
	Writer format(FormatArguments arguments) throws IOException;
}
