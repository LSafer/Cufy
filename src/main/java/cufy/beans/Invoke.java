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

import cufy.util.ConstantGroup;
import cufy.util.Reflect$;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Base class for those classes that are just a collection of methods. This class will manage method grouping. And query methods for the class
 * implementing this. Then save those queries for later use.
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

	/**
	 * A group of methods. Containing list of methods. And a map for the resolved sub-method-groups.
	 */
	public static class MethodGroup extends ConstantGroup<Method> {
		/**
		 * Initialize a new group with the given methods.
		 *
		 * @param methods to be in the group
		 * @throws NullPointerException if the given 'methods' (or one of them) is null.
		 */
		public MethodGroup(Method... methods) {
			super(methods);
			for (Method method : methods)
				Objects.requireNonNull(method, "methods[?]");
		}

		/**
		 * Initialize a new group with the given methods.
		 *
		 * @param methods to be in the group
		 * @throws NullPointerException if the given 'methods' (or one of them) is null.
		 */
		public MethodGroup(Collection<Method> methods) {
			super(methods);
			for (Method method : methods)
				Objects.requireNonNull(method, "methods[?]");
		}

		@Override
		public synchronized MethodGroup subGroup(Object groupKey, Predicate<Method> predicate) {
			Objects.requireNonNull(groupKey, "groupKey");
			Objects.requireNonNull(predicate, "predicate");

			return (MethodGroup) super.subgroups.computeIfAbsent(groupKey, k -> {
				Set<Method> set = new HashSet<>(super.elements.length);

				for (Object object : super.elements) {
					Method method = (Method) object;
					if (predicate.test(method))
						set.add(method);
				}

				return new MethodGroup(set);
			});
		}

		/**
		 * Get the first method in this group. Or get null if this group is empty
		 *
		 * @return the first method in this group. Or null if this group is empty
		 */
		public Method getFirst() {
			return super.elements.length == 0 ? null : (Method) super.elements[0];
		}

		/**
		 * Get all the methods on this group. The methods that have been annotated with all of the given annotations.
		 *
		 * @param annotations that the methods on the returned group should have
		 * @return a method group from this. With every method on it annotated with all of the given annotations
		 * @throws NullPointerException     if the given 'annotation' (or any element on it) is null.
		 * @throws IllegalArgumentException if any of the given annotations is not a runtime annotation
		 */
		public synchronized MethodGroup subGroup(Class<? extends Annotation>... annotations) {
			Objects.requireNonNull(annotations, "annotations");
			return this.subGroup(Arrays.asList(annotations), method -> {
				for (Class<? extends Annotation> annotation : annotations) {
					Objects.requireNonNull(annotation, "annotations[?]");
					Retention retention = annotation.getAnnotation(Retention.class);
					if (retention == null || retention.value() != RetentionPolicy.RUNTIME)
						throw new IllegalArgumentException(annotation + " is not a runtime annotation");

					if (!method.isAnnotationPresent(annotation))
						return false;
				}

				return true;
			});
		}
	}
}
