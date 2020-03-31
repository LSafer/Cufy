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
 * @param <E> the type of the elements this group holds
 * @author LSaferSE
 * @version 3 release (31-Mar-2020)
 * @since 25-Jan-2020
 */
public class UnmodifiableGroup<E> extends AbstractSet<E> implements Group<E> {
	/**
	 * The elements of this group.
	 *
	 * @implSpec shouldn't be edited after the constructor
	 */
	final protected Object[] elements;
	/**
	 * The mappings of the subgroups of this group.
	 */
	final protected HashMap<Object, UnmodifiableGroup<E>> subgroups = new HashMap<>();

	/**
	 * Construct a new constant group holding the given elements as it's elements.
	 *
	 * @param elements the elements to be hold
	 * @throws NullPointerException if the given elements is null
	 */
	public UnmodifiableGroup(E... elements) {
		Objects.requireNonNull(elements, "elements");
		this.elements = Array$.copyOf(elements, elements.length, Object[].class);
	}

	/**
	 * Construct a new constant group holding the given elements as it's elements.
	 *
	 * @param elements the elements to be hold
	 * @throws NullPointerException if the given elements is null
	 */
	public UnmodifiableGroup(Collection<E> elements) {
		Objects.requireNonNull(elements, "elements");
		this.elements = elements.toArray();
	}

	@Override
	public Iterator<E> iterator() {
		return (Iterator<E>) Array$.iterator(this.elements);
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
	public boolean add(E e) {
		throw new UnsupportedOperationException("add");
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("remove");
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
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
	public boolean removeIf(Predicate<? super E> filter) {
		throw new UnsupportedOperationException("removeIf");
	}

	@Override
	public UnmodifiableGroup<E> subGroup(Object groupKey, Predicate<E> predicate) {
		Objects.requireNonNull(groupKey, "groupKey");
		Objects.requireNonNull(predicate, "predicate");

		return this.subgroups.computeIfAbsent(groupKey, k -> {
			Set<E> set = new HashSet<>(this.elements.length);

			for (Object element : this.elements)
				if (predicate.test((E) element))
					set.add((E) element);

			return new UnmodifiableGroup<>(set);
		});
	}
}
