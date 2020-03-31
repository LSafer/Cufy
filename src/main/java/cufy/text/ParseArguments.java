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
 * A parsing instance that holds the variables of a parsing.
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 * @author LSaferSE
 * @version 1 release (31-Mar-20)
 * @since 30-Mar-20
 */
public class ParseArguments<I, O> {
	/**
	 * The reader to read the input from.
	 */
	final public Reader input;
	/**
	 * The class that the input do have.
	 */
	final public Clazz<I> inputClazz;
	/**
	 * The class that the output should have.
	 */
	final public Clazz<O> outputClazz;
	/**
	 * The parsing-arguments for the parsing that required initializing this arguments.
	 */
	final public ParseArguments parent;
	/**
	 * The output of the parsing. (could be changed several times!)
	 */
	public O output;

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param parent      the parent arguments
	 * @param input       the input to read from
	 * @param output      the initial output instance
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' or 'input' is null
	 */
	public ParseArguments(ParseArguments parent, Reader input, O output, Clazz inputClazz, Clazz outputClazz) {
		Objects.requireNonNull(input, "input");
		Objects.requireNonNull(inputClazz, "inputClazz");
		Objects.requireNonNull(outputClazz, "outputClazz");

		//NO RECURSE DETECTION

		this.parent = parent;
		this.input = input;
		this.output = output;
		this.inputClazz = inputClazz;
		this.outputClazz = outputClazz;
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param parent the parent arguments
	 * @param input  the input to read from
	 * @param output the initial output instance (source for inputClazz and outputClazz)
	 * @throws NullPointerException if the given 'input' is null
	 */
	public ParseArguments(ParseArguments parent, Reader input, O output) {
		this(parent, input, output, Clazz.of(output), Clazz.of(output));
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param input       the input to read from
	 * @param output      the initial output instance
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' or 'input' is null
	 */
	public ParseArguments(Reader input, O output, Clazz inputClazz, Clazz outputClazz) {
		this(null, input, output, inputClazz, outputClazz);
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param input       the input to read from
	 * @param output      the initial output instance
	 * @param outputClazz the clazz to be for the output (also for inputClazz)
	 * @throws NullPointerException if the given 'outputClass' or 'input' is null
	 */
	public ParseArguments(Reader input, O output, Clazz outputClazz) {
		this(null, input, output, outputClazz, outputClazz);
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param input  the input to read from
	 * @param output the initial output instance (source for inputClazz and outputClazz)
	 * @throws NullPointerException if the given 'input' is null
	 */
	public ParseArguments(Reader input, O output) {
		this(null, input, output, Clazz.of(output), Clazz.of(output));
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param input       the input to read from
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' or 'input' is null
	 */
	public ParseArguments(Reader input, Clazz inputClazz, Clazz outputClazz) {
		this(null, input, null, inputClazz, outputClazz);
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param input       the input to read from
	 * @param outputClazz the clazz to be for the output (also for inputClass)
	 * @throws NullPointerException if the given 'outputClass' or 'input' is null
	 */
	public ParseArguments(Reader input, Clazz outputClazz) {
		this(null, input, null, outputClazz, outputClazz);
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param parent         the parent arguments
	 * @param input          the input to read from
	 * @param output         the initial output instance
	 * @param inputAltClazz  the inputClazz if the inputComponentType clazz does not exist
	 * @param outputAltClazz the outputClazz if the outputComponentType clazz does not exist
	 * @param component      the component index to get from the parent's clazzes to the clazzes of this
	 * @throws NullPointerException     if the given 'inputAltClazz' or 'outputAltClazz' or 'input' or 'parent' is null
	 * @throws IllegalArgumentException if the given component index is less than 0
	 */
	public ParseArguments(ParseArguments parent, Reader input, O output, Clazz inputAltClazz, Clazz outputAltClazz, int component) {
		Objects.requireNonNull(parent, "parent");
		Objects.requireNonNull(input, "input");
		if (component < 0)
			throw new IllegalArgumentException("component < 0");

		//clazzes declarations
		Clazz inputClazz = parent.inputClazz.getComponentType(component);
		Clazz outputClazz = parent.outputClazz.getComponentType(component);

		//replace to alt if needed
		if (inputClazz == null)
			inputClazz = inputAltClazz;
		if (outputClazz == null)
			outputClazz = outputAltClazz;

		//NO RECURSE DETECTION

		this.parent = parent;
		this.input = input;
		this.output = output;
		this.inputClazz = inputClazz;
		this.outputClazz = outputClazz;
	}

	/**
	 * Construct a new parsing arguments instance.
	 *
	 * @param parent    the parent arguments
	 * @param input     the input to read from
	 * @param output    the initial output instance (alt source for inputClazz and outputClazz)
	 * @param component the component index to get from the parent's clazzes to the clazzes of this
	 * @throws NullPointerException     if the given 'input' or 'parent' is null
	 * @throws IllegalArgumentException if the given component index is less than 0
	 */
	public ParseArguments(ParseArguments parent, Reader input, O output, int component) {
		this(parent, input, output, Clazz.of(output), Clazz.of(output), component);
	}
}
