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

/**
 * A class that can parse a text from a reader to an object. With just a simple gate method (for the caller).
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @since 31-Mar-2020
 */
public interface Parser {
	/**
	 * Parse the text read from the {@link ParseArguments#input} to an object with the type of {@link ParseArguments#outputClazz} and store it at
	 * {@link ParseArguments#output}.
	 *
	 * @param arguments the parsing instance that holds the variables of this parsing
	 * @param <O>       the type of the parsed object
	 * @return the parsed object
	 * @throws IOException          if any I/O exception occurs
	 * @throws NullPointerException if the given 'arguments' is null
	 * @throws ParseException       if any parsing exception occurs
	 */
	<O> O parse(ParseArguments<?, O> arguments) throws IOException;
}
