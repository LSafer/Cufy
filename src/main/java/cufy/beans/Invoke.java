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

import org.cufy.util.JetHashMap;
import org.cufy.util.ReflectUtil;
import org.cufy.util.ObjectUtil;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;

/**
 * Base class for those classes that are just a collection of methods. This class will manage method grouping. And query methods for the class
 * implementing this.
 *
 * @author LSaferSE
 * @version 3 release (07-Dec-2019)
 * @apiNote this class not designed to be the direct super for the collection container
 * @since 20-Nov-2019
 */
@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
public abstract class Invoke {
	/**
	 * Method groups stored at this.
	 */
	final protected Map<Object, MethodGroup> groups = new HashMap<>(10);

	/**
	 * Get a methods group by key. Or group a new one if not exist. Using the given predicate.
	 *
	 * @param key    group key
	 * @param tester methods test to group methods if no group stored at the given key
	 * @return group for the params passed
	 * @throws NullPointerException if the given 'test' is null
	 */
	@StaticMethod
	protected MethodGroup getMethodGroup(Object key, Predicate<Method> tester) {
		ObjectUtil.requireNonNull(tester, "tester");

		MethodGroup methods = this.getMethods();
		return this.groups.computeIfAbsent(key, ignored -> {
			MethodGroup group = new MethodGroup();

			for (Method method : methods)
				if (tester.test(method))
					group.add(method);

			return group;
		});
	}

	/**
	 * Get methods annotated with all of the given annotations.
	 *
	 * @param annotations that the methods group should have
	 * @return group with every method annotated with all of the given annotations
	 * @throws NullPointerException if the given annotation array is null
	 */
	@SuppressWarnings("OverloadedVarargsMethod")
	@StaticMethod
	protected MethodGroup getMethodGroup(Class<?>... annotations) {
		ObjectUtil.requireNonNull(annotations, "annotations");
		return this.getMethodGroup(new HashSet<>(Arrays.asList(annotations)), method -> {
			for (Class<? extends Annotation> annotation : (Class<? extends Annotation>[]) annotations)
				if (!method.isAnnotationPresent(annotation))
					return false;
			return true;
		});
	}

	/**
	 * Get the main group of this. (the group with null key)
	 *
	 * @return all methods group
	 * @see ReflectUtil#allMethods(Class)
	 */
	@StaticMethod
	protected MethodGroup getMethods() {
		return this.groups.computeIfAbsent(null, key -> {
			List<Method> methods = ReflectUtil.allMethods(this.getClass());
			methods.removeIf(method -> method.isAnnotationPresent(StaticMethod.class));
			return new MethodGroup(methods);
		});
	}

	/**
	 * Defines that the annotated method shouldn't be grouped by {@link Invoke}'s default method grouping method.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	protected @interface StaticMethod {
	}

	/**
	 * A group of methods. Containing list of methods. And a map for those methods.
	 *
	 * @author LSaferSE
	 * @version 1 release (05-Dec-2019)
	 * @since 05-Dec-2019
	 */
	public class MethodGroup extends HashSet<Method> {
		/**
		 * Stores what method for what key on this group.
		 */
		final protected JetHashMap<Object, Method> map = new JetHashMap<>();

		/**
		 * Initialize a new empty group.
		 */
		public MethodGroup() {
		}

		/**
		 * Initialize a new group with the given methods.
		 *
		 * @param methods to be in the group
		 * @throws NullPointerException if the given 'methods' (or one of them) are null
		 */
		public MethodGroup(Collection<Method> methods) {
			this.addAll(ObjectUtil.requireNonNull(methods, "methods"));
		}

		/**
		 * Add the given method to this group.
		 *
		 * @return if this group did not already contain the given method
		 * @throws NullPointerException if the given method is null
		 */
		@Override
		public boolean add(Method method) {
			return super.add(ObjectUtil.requireNonNull(method, "method"));
		}

		@Override
		public boolean remove(Object method) {
			return super.remove(method) | this.map.removeIf((key, value) -> value == method);
		}

		/**
		 * Add all the given methods to this group.
		 *
		 * @param methods to be added
		 * @return if any modification applied to this set as a result of calling this method
		 * @throws NullPointerException if the given 'methods' (or one of them) are null
		 */
		@Override
		public boolean addAll(Collection<? extends Method> methods) {
			ObjectUtil.requireNonNull(methods, "methods");
			boolean modified = false;

			for (Method method : methods)
				modified |= this.add(ObjectUtil.requireNonNull(method, "methods[?]"));

			return modified;
		}

		/**
		 * Get a method on this group by the key given. Or query the method using the given predicate. Then map it to the given key.
		 *
		 * @param key    of the method
		 * @param tester condition of the method if the key not found
		 * @return the method mapped to the given key
		 * @throws NullPointerException if the given 'tester' is null
		 */
		public Method get(Object key, Predicate<Method> tester) {
			ObjectUtil.requireNonNull(tester, "tester");

			if (this.map.containsKey(key))
				return this.map.get(key);

			for (Method method : this)
				if (tester.test(method)) {
					this.map.put(key, method);
					return method;
				}

			return this.map.put(key, null);
		}
	}
}
