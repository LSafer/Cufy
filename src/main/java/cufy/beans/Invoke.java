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
package cufy.beans;

import cufy.util.Reflect$;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Base class for those classes that are just a collection of methods. This class will manage method grouping. And query methods for the class
 * implementing this.
 *
 * @author LSaferSE
 * @version 5 release (23-Jan-2020)
 * @apiNote this class is not designed to be the direct super for the method collection container
 * @since 20-Nov-2019
 */
public abstract class Invoke {
	/**
	 * Whether this class is in debugging mode or not. For better performance. Null checks and type compat tests should not be done on dynamic methods
	 * except while debugging.
	 *
	 * @implSpec enables null checks and compat tests.
	 */
	protected boolean DEBUGGING;

	/**
	 * The root method collection in this class.
	 *
	 * @implSpec all the methods. except static-methods
	 */
	protected MethodGroup methods;

	/**
	 * Get all the dynamic methods in this class.
	 *
	 * @return all the dynamic methods in this class
	 */
	@StaticMethod
	protected synchronized MethodGroup getMethods() {
		if (this.methods == null) {
			List<Method> methods = Reflect$.getAllMethods(this.getClass());
			methods.removeIf(method -> method.isAnnotationPresent(StaticMethod.class));
			this.methods = new MethodGroup(methods);
		}

		return this.methods;
	}
}
