/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.util;

import java.util.*;
import java.util.function.Predicate;

/**
 * A way to store final elements and get subgroups of them. This class holds the elements of it as a final elements. And give the user the ability to
 * iterate the elements back. Or get a subgroup of the elements it has got. The subgroup will be saved by it for the next requests for that group.
 *
 * @param <T> the type of the elements this group holds
 * @author LSaferSE
 * @version 1 release (25-Jan-2020)
 * @since 25-Jan-2020
 */
public class ConstantGroup<T> extends AbstractSet<T> {
	/**
	 * The elements of this group.
	 *
	 * @implSpec shouldn't be edited after the constructor
	 */
	final protected Object[] elements;
	/**
	 * The mappings of the subgroups of this group.
	 */
	final protected HashMap<Object, ConstantGroup<T>> subgroups = new HashMap<>();

	/**
	 * Construct a new constant group holding the given elements as it's elements.
	 *
	 * @param elements the elements to be hold
	 * @throws NullPointerException if the given elements is null
	 */
	public ConstantGroup(T... elements) {
		Objects.requireNonNull(elements, "elements");
		this.elements = Array$.copyOf(elements, elements.length, Object[].class);
	}

	/**
	 * Construct a new constant group holding the given elements as it's elements.
	 *
	 * @param elements the elements to be hold
	 * @throws NullPointerException if the given elements is null
	 */
	public ConstantGroup(Collection<T> elements) {
		Objects.requireNonNull(elements, "elements");
		this.elements = elements.toArray();
	}

	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) Array$.iterator(this.elements);
	}

	@Override
	public int size() {
		return this.elements.length;
	}

	@Override
	public Object[] toArray() {
		return Array$.copyOf(this.elements, this.elements.length, Object[].class);
	}

	@Override
	public <U> U[] toArray(U[] a) {
		Objects.requireNonNull(a, "a");
		int length = a.length;

		if (length < this.elements.length) {
			return (U[]) Array$.copyOf(this.elements, this.elements.length, (Class<Object[]>) a.getClass());
		} else {
			if (a.getClass().isAssignableFrom(this.elements.getClass()))
				System.arraycopy(this.elements, 0, a, 0, this.elements.length);
			else Array$.hardcopy0(this.elements, 0, a, 0, this.elements.length);

			if (length > this.elements.length) {
				a[this.elements.length] = null;
			}

			return a;
		}
	}

	@Override
	public boolean add(T t) {
		throw new UnsupportedOperationException("add");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("remove");
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		throw new UnsupportedOperationException("addAll");
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		throw new UnsupportedOperationException("retainAll");
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException("clear");
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		throw new UnsupportedOperationException("removeAll");
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		throw new UnsupportedOperationException("removeIf");
	}

	/**
	 * Returns a subgroup of this group. The subgroup returned has the elements of this that satisfies the given predicate. The given predicate will
	 * be invoked only if the given key has not been resolved previously. Otherwise the results of that previous call will be returned.
	 *
	 * @param groupKey  the key of that group (saves the group for later)
	 * @param predicate the tester to be satisfied
	 * @return a subgroup of this group that has only the elements satisfied the given predicate
	 * @throws NullPointerException if the given 'predicate' is null
	 */
	public ConstantGroup<T> subGroup(Object groupKey, Predicate<T> predicate) {
		Objects.requireNonNull(groupKey, "groupKey");
		Objects.requireNonNull(predicate, "predicate");

		return this.subgroups.computeIfAbsent(groupKey, k -> {
			Set<T> set = new HashSet<>(this.elements.length);

			for (Object element : this.elements)
				if (predicate.test((T) element))
					set.add((T) element);

			return new ConstantGroup<>(set);
		});
	}
}
