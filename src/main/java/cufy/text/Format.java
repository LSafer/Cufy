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

import cufy.lang.Clazz;

import java.io.IOException;
import java.io.Reader;

/**
 * A class that is a {@link Formatter} and {@link Parser} and {@link Classifier} at the same time.
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @since 31-Mar-2020
 */
public interface Format extends Formatter, Parser, Classifier {
	/**
	 * Classify then parse the text read from the 'reader' to an object with the type of the 'outputClazz' and then return it.
	 *
	 * @param input       the input to read from
	 * @param output      the initial output instance
	 * @param outputClazz the clazz to be for the output
	 * @param <O>         the type of the parsed object
	 * @return the parsed object
	 * @throws NullPointerException if the given 'outputClass' or 'input' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws ParseException       if any parsing exception occurs
	 */
	default <O> O cparse(Reader input, O output, Clazz outputClazz) throws IOException {
		return this.parse(input, output, this.classify(input), outputClazz);
	}

	/**
	 * Classify then parse the text read from the 'reader' to an object with the type of the 'outputClazz' and then return it.
	 *
	 * @param input  the input to read from
	 * @param output the initial output instance
	 * @param <O>    the type of the parsed object
	 * @return the parsed object
	 * @throws NullPointerException if the given 'outputClass' or 'input' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws ParseException       if any parsing exception occurs
	 */
	default <O> O cparse(Reader input, O output) throws IOException {
		return this.parse(input, output, this.classify(input));
	}

	/**
	 * Classify then parse the text read from the 'reader' to an object with the type of the 'outputClazz' and then return it.
	 *
	 * @param input       the input to read from
	 * @param outputClazz the clazz to be for the output
	 * @param <O>         the type of the parsed object
	 * @return the parsed object
	 * @throws NullPointerException if the given 'outputClass' or 'input' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws ParseException       if any parsing exception occurs
	 */
	default <O> O cparse(Reader input, Clazz outputClazz) throws IOException {
		return this.parse(input, this.classify(input), outputClazz);
	}

	/**
	 * Classify then parse the text read from the 'reader' to an object with the type of the 'outputClazz' and then return it.
	 *
	 * @param input the input to read from
	 * @param <O>   the type of the parsed object
	 * @return the parsed object
	 * @throws NullPointerException if the given 'outputClass' or 'input' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws ParseException       if any parsing exception occurs
	 */
	default <O> O cparse(Reader input) throws IOException {
		return this.parse(input, this.classify(input));
	}
}
