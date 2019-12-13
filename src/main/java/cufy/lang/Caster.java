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
package cufy.lang;

import cufy.beans.Invoke;
import cufy.util.ObjectUtil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An abstract for casting classes. Used to simplify the castings and make it more inheritable. Also make inheriting just for add some futures more
 * easier. This uses the {@link Invoke} class and methods in this will be invoked using the dynamic method grouping algorithm. In order to add a
 * method on a dynamic method group. the method should be annotated with the group annotation. (see {@link Invoke}). Also the method should match the
 * conditions of that group to avoid parameters/output mismatches. This abstract have the method group {@link CastMethod}. and that group have it's
 * own conditions.
 *
 * <ul>
 *     <font color="orange" size="4" face="verdana"><b>Abstract Methods:</b></font>
 *     <li>
 *         <font color="yellow">Support</font>
 *         <ul>
 *             <li>{@link #newCastPosition()}</li>
 *         </ul>
 *     </li>
 * </ul>
 * <ul>
 *     <font color="orange" size="4" face="verdana"><b>Static-Dynamic Methods:</b></font>
 *     <li>
 *         <font color="yellow">{@link CastMethod Cast}</font>
 *         <ul>
 *             <li>{@link #getCastMethod}</li>
 *             <li>{@link #cast0}</li>
 *             <li>{@link #cast}</li>
 *             <li>{@link #castNull}</li>
 *             <li>{@link #castElse}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author LSaferSE
 * @version 7 release (07-Dec-2019)
 * @implNote you have to navigate this class to where your {@link CastMethod} methods is. By using the mentioned annotation.
 * @since 31-Aug-19
 */
public abstract class Caster extends Invoke {
	/**
	 * Cast the given object to the given class.
	 *
	 * @param object the object to be casted
	 * @param out    the targeted class to cast the object to
	 * @param <T>    the targeted type
	 * @return the given object casted to the given 'out' class
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if the 'out' param equals to null
	 */
	@StaticMethod
	public <T> T cast(Object object, Class<? super T> out) {
		return this.cast(object, out, null, null, false);
	}

	/**
	 * Cast the given object to the given 'out' class. Using the first method annotated with {@link CastMethod}. And that annotation allows the given
	 * 'in' and 'out' classes. (methods are ordered randomly)
	 *
	 * @param object   the object to be casted
	 * @param out      the targeted class to cast the object to
	 * @param position to format depending on (null to create a new one)
	 * @param in       the targeted method parameter type (null for the class of the given object)
	 * @param clone    true to create a new instance even when the object is instance of the targeted class
	 * @param <T>      the targeted type
	 * @return the given object casted to the given 'out' class
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if the 'out' param equals to null
	 */
	@StaticMethod
	public <T> T cast(Object object, Class<? super T> out, CastPosition position, Class<?> in, boolean clone) {
		ObjectUtil.requireNonNull(out, "out");

		position = ObjectUtil.requireNonNullElseGet(position, this::newCastPosition);

		if (object == null)
			return this.castNull(out, position);
		if (!clone && out.isInstance(object))
			return (T) object;
		if (object instanceof Castable) {
			T cast = ((Castable) object).castTo(out, position);
			if (cast != null)
				return cast;
		}

		in = ObjectUtil.requireNonNullElseGet(in, object::getClass);

		Method method = this.getCastMethod(in, out);

		if (method == null)
			return this.castElse(object, out, position);

		return this.cast0(method, object, out, position);
	}

	/**
	 * Invoke the given cast method with the given parameters.
	 *
	 * @param method   to be invoked
	 * @param object   the object to be casted
	 * @param out      the targeted class to cast the object to
	 * @param position to format depending on
	 * @param <T>      the targeted type
	 * @return the given object casted to the given 'out' class
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if any of the given parameters is null
	 */
	@StaticMethod
	protected <T> T cast0(Method method, Object object, Class<? super T> out, CastPosition position) {
		ObjectUtil.requireNonNull(method, "method");
		ObjectUtil.requireNonNull(object, "object");
		ObjectUtil.requireNonNull(out, "out");
		ObjectUtil.requireNonNull(position, "position");

		try {
			method.setAccessible(true);
			return (T) method.invoke(this, object, out, position);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			throw cause instanceof RuntimeException ? (RuntimeException) cause : new RuntimeException(cause);
		}
	}

	/**
	 * Cast the given unsupported type object to the given class. If no dynamic method can handle the object. This method will be invoked. This method
	 * shouldn't be directly called.
	 *
	 * @param object   the object to be casted
	 * @param out      the targeted class to cast the object to
	 * @param position to format depending on
	 * @param <T>      the targeted type
	 * @return the given object casted to the given 'out' class
	 * @throws ClassCastException       on casting failure (Also if this method isn't supported by this caster)
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if any of the given parameters equals to null
	 */
	@StaticMethod
	protected <T> T castElse(Object object, Class<? super T> out, CastPosition position) {
		ObjectUtil.requireNonNull(object, "object");
		ObjectUtil.requireNonNull(out, "out");
		ObjectUtil.requireNonNull(position, "position");
		throw new ClassCastException("Cannot cast " + object.getClass() + " to " + out);
	}

	/**
	 * Cast the value null to the given 'out' class.
	 *
	 * @param out      the targeted class to cast the object to
	 * @param position to format depending on
	 * @param <T>      the targeted type
	 * @return the given object casted to the given 'out' class
	 * @throws ClassCastException       on casting failure
	 * @throws IllegalArgumentException optional. on casting failure
	 * @throws NullPointerException     if the given position equals to null
	 */
	@StaticMethod
	protected <T> T castNull(Class<?> out, CastPosition position) {
		ObjectUtil.requireNonNull(out, "out");
		ObjectUtil.requireNonNull(position, "position");
		return null;
	}

	/**
	 * Find a method that casts any of the given 'input' class. to the given 'output' class.
	 *
	 * @param input  type that the targeted method can cast
	 * @param output type that the targeted method can return
	 * @return a method that casts the given input class to the given output class
	 * @throws NullPointerException if any of the given parameters equals to null
	 */
	@StaticMethod
	protected synchronized Method getCastMethod(Class<?> input, Class<?> output) {
		ObjectUtil.requireNonNull(input, "input");
		ObjectUtil.requireNonNull(output, "output");

		return this.getMethodGroup(CastMethod.class).get(input.getName() + output.getName(), method -> {
			CastMethod caster = method.getAnnotation(CastMethod.class);
			return Range.util.test(caster.in(), input) && Range.util.test(caster.out(), output);
		});
	}

	/**
	 * Construct a new casting position.
	 *
	 * @return new Casting position
	 * @implSpec don't return null
	 */
	@StaticMethod
	protected CastPosition newCastPosition() {
		return new CastPosition() {
		};
	}

	/**
	 * Navigate the {@link Caster} class that the annotated method is a stringing method.
	 *
	 * @author LSaferSE
	 * @version 1 release (07-Dec-2019)
	 * @apiNote the annotated method SHOULD match the {@link Caster#cast0} rules
	 * @see Caster#cast parameterization
	 * @see Caster#getCastMethod grouping
	 * @see Caster#cast0 invokation
	 * @since 07-Dec-2019
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface CastMethod {
		/**
		 * The input classes range the annotated method targeting.
		 *
		 * @return the input range
		 */
		Range in() default @Range;

		/**
		 * The output classes range the annotated method targeting.
		 *
		 * @return the output range
		 */
		Range out() default @Range;
	}

	/**
	 * A position used by {@link Caster} to manage nested castings.
	 *
	 * @author LSaferSE
	 * @version 1 release (25-Nov-2019)
	 * @implSpec a point should have final values!
	 * @since 25-Nov-2019
	 */
	public interface CastPosition {
	}
}
