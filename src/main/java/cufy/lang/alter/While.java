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
import java.util.function.Supplier;

/**
 * A {@link Loop} version of the typical 'while' statement.
 *
 * @author LSaferSE
 * @version 1 release (07-Dec-2019)
 * @since 07-Dec-2019
 */
public class While extends Loop<Consumer<While>, Object> {
	/**
	 * The function to be applied before each round on the loop to check whether the loop shall continue or break.
	 */
	final protected Supplier<Boolean> condition;

	/**
	 * Construct a new while loop with the given arguments.
	 *
	 * @param condition looping condition
	 * @throws NullPointerException if the given 'condition' is null
	 */
	public While(Supplier<Boolean> condition) {
		ObjectUtil.requireNonNull(condition, "condition");
		this.condition = condition;
	}

	/**
	 * Construct a new while loop with the given arguments.
	 *
	 * @param condition looping condition
	 * @param code      first looping code
	 * @throws NullPointerException if ether the given 'condition' or 'code' is null
	 */
	public While(Supplier<Boolean> condition, Consumer<While> code) {
		ObjectUtil.requireNonNull(condition, "null");
		ObjectUtil.requireNonNull(code, "code");
		this.append(code);
		this.condition = condition;
	}

	@Override
	public While append(Consumer<While> code) {
		ObjectUtil.requireNonNull(code, "code");
		return (While) this.append0(param -> code.accept(this));
	}

	@Override
	public void loop() {
		//noinspection StatementWithEmptyBody
		while (this.condition.get() && this.next(null)) ;
	}
}
