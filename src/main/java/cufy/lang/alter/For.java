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

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A {@link Loop} version of the typical 'for' statement.
 *
 * @param <I> The type of the first initialized variable
 * @author LSaferSE
 * @version 1 release (07-Dec-2019)
 * @since 07-Dec-2019
 */
public class For<I> extends Loop<BiConsumer<For<I>, I>, I> {
	/**
	 * The function to be applied before each round on the loop to check whether the loop shall continue or break.
	 */
	final protected Function<I, Boolean> condition;
	/**
	 * A function to be applied after each round on the loop. The function focus on what to do on {@link #variable}.
	 */
	final protected Function<I, I> reducer;
	/**
	 * The variable first initialized.
	 */
	protected I variable;

	/**
	 * Construct a new for loop with the given arguments.
	 *
	 * @param variable  initial variable
	 * @param condition looping condition
	 * @param reducer   action to be applied to the variable on each round
	 * @throws NullPointerException if ether the given 'condition' or 'reducer' is null
	 */
	public For(I variable, Function<I, Boolean> condition, Function<I, I> reducer) {
		ObjectUtil.requireNonNull(condition, "condition");
		ObjectUtil.requireNonNull(reducer, "reducer");
		this.variable = variable;
		this.condition = condition;
		this.reducer = reducer;
	}

	/**
	 * Construct a new for loop with the given arguments.
	 *
	 * @param variable  initial variable
	 * @param condition looping condition
	 * @param reducer   action to be applied to the variable on each round
	 * @param code      first looping code
	 * @throws NullPointerException if one of the given 'condition' or 'reducer' or 'code' is null
	 */
	public For(I variable, Function<I, Boolean> condition, Function<I, I> reducer, BiConsumer<For<I>, I> code) {
		ObjectUtil.requireNonNull(condition, "condition");
		ObjectUtil.requireNonNull(reducer, "reducer");
		ObjectUtil.requireNonNull(code, "code");
		this.append(code);
		this.variable = variable;
		this.condition = condition;
		this.reducer = reducer;
	}

	@Override
	public For<I> append(BiConsumer<For<I>, I> code) {
		ObjectUtil.requireNonNull(code, "code");
		return (For<I>) this.append0(param -> code.accept(this, param));
	}

	@Override
	public void loop() {
		while (this.condition.apply(this.variable) && this.next(this.variable))
			this.variable = this.reducer.apply(this.variable);
	}
}
