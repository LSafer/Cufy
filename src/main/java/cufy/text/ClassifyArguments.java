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

import java.io.Reader;
import java.util.Objects;

/**
 * A classification instance that holds the variables of a classification.
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 * @author LSaferSE
 * @version 1 release (31-Mar-20)
 * @since 30-Mar-20
 */
public class ClassifyArguments<I, O> {
	/**
	 * The reader to read input from.
	 *
	 * @apiNote return it to it's original position after using it
	 */
	final public Reader input;
	/**
	 * The output of the classification. (could be changed several times!)
	 */
	public Clazz<O> output;

	/**
	 * Construct a new classifying arguments instance.
	 *
	 * @param input  the input to read from
	 * @param output the initial output instance
	 * @throws NullPointerException if the given 'input' is null
	 */
	public ClassifyArguments(Reader input, Clazz output) {
		Objects.requireNonNull(input, "input");
		this.input = input;
		this.output = output;
	}

	/**
	 * Construct a new classifying arguments instance.
	 *
	 * @param input the input to read from
	 * @throws NullPointerException if the given 'input' is null
	 */
	public ClassifyArguments(Reader input) {
		this(input, null);
	}
}
