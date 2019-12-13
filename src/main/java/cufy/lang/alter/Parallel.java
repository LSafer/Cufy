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
import org.cufy.util.ObjectUtil;

import java.util.function.Consumer;

/**
 * Looping until get broken manually.
 *
 * @author LSaferSE
 * @version 1 release (07-Dec-2019)
 * @since 07-Dec-2019
 */
public class Parallel extends Loop<Consumer<Parallel>, Object> {
	/**
	 * Construct a new forever loop.
	 */
	public Parallel() {
	}

	/**
	 * Construct a new forever loop with the given parameters.
	 *
	 * @param code the first looping code
	 * @throws NullPointerException if the given code is null
	 */
	public Parallel(Consumer<Parallel> code) {
		ObjectUtil.requireNonNull(code, "code");
		this.append(code);
	}

	@Override
	public Parallel append(Consumer<Parallel> code) {
		ObjectUtil.requireNonNull(code, "code");
		return (Parallel) this.append0(param -> code.accept(this));
	}

	@Override
	public void loop() {
		//noinspection StatementWithEmptyBody
		while (this.next(null)) ;
	}
}
