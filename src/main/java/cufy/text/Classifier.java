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
 * A class that can classify sequences from a reader. With just a simple gate method (for the caller).
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @since 31-Mar-2020
 */
public interface Classifier {
	/**
	 * Return the proper clazz for the text in the given 'input'.
	 *
	 * @param input  the input to read from
	 * @param output the initial output instance
	 * @param <O>    the component-type of the clazz returned
	 * @return the proper clazz for the text in the given reader
	 * @throws NullPointerException if the given 'input' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws ClassifyException    if any classifying exception occurs
	 */
	default <O> Clazz<O> classify(Reader input, Clazz output) throws IOException {
		return this.classify(new ClassifyArguments<>(input, output));
	}

	/**
	 * Return the proper clazz for the text in the given 'input'.
	 *
	 * @param input the input to read from
	 * @param <O>   the component-type of the clazz returned
	 * @return the proper clazz for the text in the given reader
	 * @throws NullPointerException if the given 'input' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws ClassifyException    if any classifying exception occurs
	 */
	default <O> Clazz<O> classify(Reader input) throws IOException {
		return this.classify(new ClassifyArguments<>(input));
	}

	/**
	 * Set the {@link ClassifyArguments#output} on the given arguments to the proper clazz for the text in the {@link ClassifyArguments#input}.
	 *
	 * @param arguments the classifying instance that holds the variables of this classification
	 * @param <O>       the component-type of the clazz returned
	 * @return the proper clazz for the text in the given reader
	 * @throws IOException          if any I/O exception occurs
	 * @throws NullPointerException if the given 'arguments' is null
	 * @throws ClassifyException    if any classifying exception occurs
	 */
	<O> Clazz<O> classify(ClassifyArguments<?, O> arguments) throws IOException;
}
