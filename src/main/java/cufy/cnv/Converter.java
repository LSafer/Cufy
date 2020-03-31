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

/**
 * A class that can convert instances to different classes. With just a simple gate method (for the caller).
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @since 31-Mar-2020
 */
public interface Converter {
	/**
	 * Set the {@link ConvertArguments#output} on the given arguments to a value of the {@link ConvertArguments#input}, but as the class specified
	 * as in the {@link ConvertArguments#outputClazz}.
	 *
	 * @param arguments the conversion instance that holds the variables of this conversion
	 * @param <O>       the {@link ConvertArguments#output} of the given arguments after the converting process
	 * @return the type of the output targeted
	 * @throws ConvertException     if any converting error occurred
	 * @throws NullPointerException if the given 'arguments' is null.
	 */
	<O> O convert(ConvertArguments<?, O> arguments);
}
