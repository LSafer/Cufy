/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.beans;

import cufy.util.Array$;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * A group of methods. Containing list of methods. And a map for the resolved sub-method-groups.
 *
 * @author LSaferSE
 * @version 3 release (23-Jan-2020)
 * @since 05-Dec-2019
 */
public class MethodGroup extends AbstractSet<Method> {
	/**
	 * Sub-method-groups Mappings.
	 */
	final protected Map<Object, MethodGroup> groups = new HashMap<>(5);
	/**
	 * The methods stored at this group.
	 *
	 * @implSpec don't change any element
	 */
	final protected Method[] methods;

	/**
	 * Initialize a new group with the given methods.
	 *
	 * @param methods to be in the group
	 * @throws NullPointerException if the given 'methods' (or one of them) are null
	 */
	public MethodGroup(Collection<Method> methods) {
		Objects.requireNonNull(methods, "methods");
		for (Method method : methods)
			Objects.requireNonNull(method, "methods[?]");

		this.methods = methods.toArray(new Method[0]);
	}

	@Override
	public Iterator<Method> iterator() {
		return Array$.iterator(this.methods);
	}

	@Override
	public int size() {
		return this.methods.length;
	}

	/**
	 * Get the first method in this group. Or get null if this group is empty
	 *
	 * @return the first method in this group. Or null if this group is empty
	 */
	public Method getMethod() {
		return this.methods.length == 0 ? null : this.methods[0];
	}

	/**
	 * Get a methods group by key. Or group a new one if not exist. Using the given predicate.
	 *
	 * @param key    group key
	 * @param tester methods tester. Used to group the methods. If no group stored at the given key
	 * @return group for the params passed
	 * @throws NullPointerException if the given 'test' is null
	 * @apiNote once the targeted group declared. You can not change it
	 */
	public synchronized MethodGroup getMethodGroup(Object key, Predicate<Method> tester) {
		Objects.requireNonNull(tester, "tester");
		return this.groups.computeIfAbsent(key, k -> {
			HashSet<Method> methods = new HashSet<>(10);

			for (Method method : this.methods)
				if (tester.test(method))
					methods.add(method);

			return new MethodGroup(methods);
		});
	}
	/**
	 * Get all the methods on this group. The methods that have been annotated with all of the given annotations.
	 *
	 * @param annotations that the methods on the returned group should have
	 * @return a method group from this. With every method on it annotated with all of the given annotations
	 * @throws NullPointerException     if the given 'annotation' (or any element on it) is null.
	 * @throws IllegalArgumentException if any of the given annotations is not a runtime annotation
	 */
	public MethodGroup getMethodGroup(Class<? extends Annotation>... annotations) {
		Objects.requireNonNull(annotations, "annotations");
		return this.getMethodGroup(Arrays.asList(annotations), method -> {
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
