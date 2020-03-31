/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.cnv;

import cufy.lang.Clazz;
import cufy.lang.Recurse;

import java.util.Objects;

/**
 * A conversion instance that holds the variables of a conversion.
 *
 * @param <I> the type of the input
 * @param <O> the type of the output
 * @author LSaferSE
 * @version 1 release (30-Mar-2020)
 * @since 30-Mar-2020
 */
public class ConvertArguments<I, O> {
	/**
	 * The input object.
	 */
	final public I input;
	/**
	 * The class that the input do have.
	 */
	final public Clazz<I> inputClazz;
	/**
	 * The class that the output should have.
	 */
	final public Clazz<O> outputClazz;
	/**
	 * The convert-arguments for the conversion that required initializing this arguments.
	 */
	final public ConvertArguments parent;
	/**
	 * The output of the conversion. (could be changed several times!)
	 */
	public O output;

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param parent      the parent converting-arguments
	 * @param input       the input instance
	 * @param output      the initial output instance
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' is null
	 */
	public ConvertArguments(ConvertArguments parent, I input, O output, Clazz inputClazz, Clazz outputClazz) {
		Objects.requireNonNull(inputClazz, "inputClazz");
		Objects.requireNonNull(outputClazz, "outputClazz");

		//recurse detection
		for (ConvertArguments grand = parent; grand != null; grand = grand.parent)
			if (grand.input == input) {
				inputClazz = Clazz.of(Recurse.class, inputClazz.getKlass(), inputClazz.getComponentTypes());
				break;
			}

		this.input = input;
		this.output = output;
		this.inputClazz = inputClazz;
		this.outputClazz = outputClazz;
		this.parent = parent;
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param parent the parent converting-arguments
	 * @param input  the input instance (source for inputClazz)
	 * @param output the initial output instance (source for outputClazz)
	 */
	public ConvertArguments(ConvertArguments parent, I input, O output) {
		this(parent, input, output, Clazz.of(input), Clazz.of(output));
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param parent the parent converting-arguments
	 * @param input  the input instance (source for inputClazz and outputClazz)
	 */
	public ConvertArguments(ConvertArguments parent, I input) {
		this(parent, input, null, Clazz.of(input), Clazz.of(input));
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param input       the input instance
	 * @param output      the initial output instance
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' is null
	 */
	public ConvertArguments(I input, O output, Clazz inputClazz, Clazz outputClazz) {
		this(null, input, output, inputClazz, outputClazz);
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param input       the input instance (source of inputClazz)
	 * @param output      the initial output instance
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'outputClass' is null
	 */
	public ConvertArguments(I input, O output, Clazz outputClazz) {
		this(null, input, output, Clazz.of(input), outputClazz);
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param input  the input instance (source of inputClazz)
	 * @param output the initial output instance (source of outputClazz)
	 * @apiNote null output may produce confusion issues
	 */
	public ConvertArguments(I input, O output) {
		this(null, input, output, Clazz.of(input), Clazz.of(output));
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param input       the input instance
	 * @param inputClazz  the clazz of the input
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'inputClass' or 'outputClass' is null
	 */
	public ConvertArguments(I input, Clazz inputClazz, Clazz outputClazz) {
		this(null, input, null, inputClazz, outputClazz);
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param input       the input instance (source of inputClazz)
	 * @param outputClazz the clazz to be for the output
	 * @throws NullPointerException if the given 'outputClass' is null
	 */
	public ConvertArguments(I input, Clazz outputClazz) {
		this(null, input, null, Clazz.of(input), outputClazz);
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param input the input instance (source of inputClazz and outputClazz)
	 */
	public ConvertArguments(I input) {
		this(null, input, null, Clazz.of(input), Clazz.of(input));
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param parent         the parent arguments
	 * @param input          the input instance
	 * @param output         the initial output instance
	 * @param inputAltClazz  the inputClazz if the inputComponentType clazz does not exist
	 * @param outputAltClazz the outputClazz if the outputComponentType clazz does not exist
	 * @param component      the component index to get from the parent's clazzes to the clazzes of this
	 * @throws NullPointerException     if the given 'parent' or 'inputAltClazz' or 'outputAltClazz' is null
	 * @throws IllegalArgumentException if the given component index is less than 0
	 */
	public ConvertArguments(ConvertArguments parent, I input, O output, Clazz inputAltClazz, Clazz outputAltClazz, int component) {
		Objects.requireNonNull(parent, "parent");
		Objects.requireNonNull(inputAltClazz, "inputAltClazz");
		Objects.requireNonNull(outputAltClazz, "outputAltClazz");
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

		//recurse detection
		for (ConvertArguments grand = parent; grand != null; grand = grand.parent)
			if (grand.input == input) {
				inputClazz = Clazz.of(Recurse.class, inputClazz.getKlass(), inputClazz.getComponentTypes());
				break;
			}

		this.input = input;
		this.output = output;
		this.inputClazz = inputClazz;
		this.outputClazz = outputClazz;
		this.parent = parent;
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param parent    the parent arguments
	 * @param input     the input instance (alt source of inputClazz)
	 * @param output    the output instance (alt source of outputClazz)
	 * @param component the component index to get from the parent's clazzes to the clazzes of this
	 * @throws NullPointerException     if the given 'parent' is null
	 * @throws IllegalArgumentException if the given component index is less than 0
	 * @apiNote null output may produce confusion issues
	 */
	public ConvertArguments(ConvertArguments parent, I input, O output, int component) {
		this(parent, input, output, Clazz.of(input), Clazz.of(output), component);
	}

	/**
	 * Construct a new conversion arguments instance.
	 *
	 * @param parent    the parent arguments
	 * @param input     the input instance (alt source of inputClazz and outputClazz)
	 * @param component the component index to get from the parent's clazzes to the clazzes of this
	 * @throws NullPointerException     if the given 'parent' is null
	 * @throws IllegalArgumentException if the given component index is less than 0
	 */
	public ConvertArguments(ConvertArguments parent, I input, int component) {
		this(parent, input, null, Clazz.of(input), Clazz.of(input), component);
	}
}
