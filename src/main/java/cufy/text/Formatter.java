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
	 * Write the text outputted from formatting the given 'input' to the 'output'.
	 *
	 * @param input       the input instance
	 * @param output      the output to write to
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @return the writer in presented in the arguments
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' or 'output' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws FormatException      if any formatting exception occurs
	 */
	default Writer format(Object input, Writer output, Clazz inputClazz, Clazz outputClazz) throws IOException {
		return this.format(new FormatArguments<>(input, output, inputClazz, outputClazz));
	}

	/**
	 * Write the text outputted from formatting the given 'input' to the 'output'.
	 *
	 * @param input       the input instance (source of inputClazz)
	 * @param output      the output to write to
	 * @param outputClazz the clazz to be for the output
	 * @return the writer in presented in the arguments
	 * @throws NullPointerException if the given 'outputClass' or 'output' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws FormatException      if any formatting exception occurs
	 */
	default Writer format(Object input, Writer output, Clazz outputClazz) throws IOException {
		return this.format(new FormatArguments<>(input, output, outputClazz));
	}

	/**
	 * Write the text outputted from formatting the given 'input' to the 'output'.
	 *
	 * @param input  the input instance (source of inputClazz and outputClazz)
	 * @param output the output to write to
	 * @return the writer in presented in the arguments
	 * @throws NullPointerException if the given 'output' is null
	 * @throws IOException          if any I/O exception occurs
	 * @throws FormatException      if any formatting exception occurs
	 */
	default Writer format(Object input, Writer output) throws IOException {
		return this.format(new FormatArguments<>(input, output));
	}

	/**
	 * Write the text outputted from formatting {@link FormatArguments#input} to the {@link FormatArguments#output}.
	 *
	 * @param arguments the formatting instance that holds the variables of this formatting
	 * @return the writer in presented in the arguments
	 * @throws IOException          if any I/O exception occurs
	 * @throws FormatException      if any formatting exception occurs
	 * @throws NullPointerException if the given 'arguments' is null
	 */
	Writer format(FormatArguments arguments) throws IOException;

}
