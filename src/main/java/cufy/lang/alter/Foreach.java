/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */

package cufy.lang.alter;

import cufy.lang.Loop;
import cufy.util.ObjectUtil;

import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * Loop for each item of a list.
 *
 * @param <I> items Type
 * @author LSaferSE
 * @version 1 release (07-Dec-2019)
 * @since 07-Dec-2019
 */
public class Foreach<I> extends Loop<BiConsumer<Foreach<I>, I>, I> {
	/**
	 * List of items to loop.
	 */
	final protected Iterable<I> iterable;

	/**
	 * Initialize this.
	 *
	 * @param array of items to be looped foreach
	 * @throws NullPointerException if the given array is null
	 */
	public Foreach(I[] array) {
		ObjectUtil.requireNonNull(array, "array");
		this.iterable = Arrays.asList(array);
	}

	/**
	 * Initialize this.
	 *
	 * @param iterable of items to be looped foreach
	 * @throws NullPointerException if the given iterable is null
	 */
	public Foreach(Iterable<I> iterable) {
		ObjectUtil.requireNonNull(iterable, "iterable");
		this.iterable = iterable;
	}

	/**
	 * Initialize this.
	 *
	 * @param array of items to be looped foreach
	 * @param code  first looping code
	 * @throws NullPointerException if the given 'array' or 'code' is null
	 */
	public Foreach(I[] array, BiConsumer<Foreach<I>, I> code) {
		ObjectUtil.requireNonNull(array, "array");
		ObjectUtil.requireNonNull(code, "code");
		this.append(code);
		this.iterable = Arrays.asList(array);
	}

	/**
	 * Initialize this.
	 *
	 * @param iterable of items to be looped foreach
	 * @param code     first looping code
	 * @throws NullPointerException if the given 'iterable' or 'code' is null
	 */
	public Foreach(Iterable<I> iterable, BiConsumer<Foreach<I>, I> code) {
		ObjectUtil.requireNonNull(iterable, "iterable");
		ObjectUtil.requireNonNull(code, "code");
		this.append(code);
		this.iterable = iterable;
	}

	@Override
	public Foreach<I> append(BiConsumer<Foreach<I>, I> code) {
		ObjectUtil.requireNonNull(code, "code");
		return (Foreach<I>) this.append0(param -> code.accept(this, param));
	}

	@Override
	public void loop() {
		for (I t : this.iterable)
			if (!this.next(t))
				break;
	}
}
