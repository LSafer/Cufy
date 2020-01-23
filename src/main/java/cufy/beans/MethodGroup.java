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
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * A group of methods. Containing list of methods. And a map for those methods.
 *
 * @author LSaferSE
 * @version 2 alpha (18-Jan-2020)
 * @since 05-Dec-2019
 */
public class MethodGroup extends AbstractSet<Method> {
	/**
	 * Method Mappings.
	 */
	final protected Map<Object, MethodGroup> groups = new HashMap<>(10);
	/**
	 * Method collection.
	 *
	 * @implSpec final elements
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
		methods.forEach(Objects::requireNonNull);
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
	 * Get the method stored at the specified index on this group.
	 *
	 * @param index to get the method of
	 * @return the method stored at the specified index
	 */
	public Method get(int index) {
		return index >= this.methods.length ? null : this.methods[index];
	}

	/**
	 * Get a methods group by key. Or group a new one if not exist. Using the given predicate.
	 *
	 * @param key    group key
	 * @param tester methods test to group methods if no group stored at the given key
	 * @return group for the params passed
	 * @throws NullPointerException if the given 'test' is null
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
	 * Get methods annotated with all of the given annotations.
	 *
	 * @param annotations that the methods group should have
	 * @return group with every method annotated with all of the given annotations
	 * @throws NullPointerException if the given annotation array is null
	 */
	public MethodGroup getMethodGroup(Class<? extends Annotation>... annotations) {
		Objects.requireNonNull(annotations, "annotations");
		return this.getMethodGroup(Arrays.asList(annotations), method -> {
			for (Class<? extends Annotation> annotation : annotations) {
				Objects.requireNonNull(annotation, "annotation");

				if (!method.isAnnotationPresent(annotation))
					return false;
			}

			return true;
		});
	}
}
