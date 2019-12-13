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
package org.cufy.util;

import cufy.util.ObjectUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link Map maps} plugin interface. An Interface that adds all {@link List lists} base methods such as {@link List#add(Object)} or {@link
 * List#add(int, Object)}, etc... Using the Integer keyed entries as the bound for specific index. Not like lists. Hybrid map indexes isn't limited by
 * any range.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author LSaferSE
 * @version 5 release (07-Dec-2019)
 * @since 15-Sep-19
 */
public interface HybridMap<K, V> extends Map<K, V> {
	/**
	 * Depending on current indexing position on this map. Add an element at the index after the last index in this.
	 * <br><br><b>example:</b>
	 * <pre>
	 *     toString -> {-1:"-one", 0:"zero", 1:"one"}
	 *     add <- "two"
	 *     toString -> {-1:"-one", 0:"zero", 1:"one", 2:"two"}
	 * </pre>
	 *
	 * @param element to be added
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 */
	default V add(V element) {
		return this.put((K) (Object) (this.maxIndex() + 1), element);
	}

	/**
	 * Inserts the specified element at the specified position in this map indexing system. Shifts the element currently at that position (if any) and
	 * any subsequent elements to the right (adds one to their indices).
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 */
	default void add(int index, V element) {
		this.shiftIndexes(index, null, 1);
		this.put((K) (Object) index, element);
	}

	/**
	 * Appends all of the elements in the specified collection to the end of this map indexing system, in the order that they are returned by the
	 * specified collection's Iterator. The behavior of this operation is undefined if the specified collection is modified while the operation is in
	 * progress.
	 *
	 * @param collection containing elements to be added to this map
	 * @return true if this map changed as a result of the call
	 * @throws NullPointerException if the given collection is null
	 */
	default boolean addAll(Collection<V> collection) {
		ObjectUtil.requireNonNull(collection, "collection");
		int i = this.maxIndex();

		for (V element : collection)
			this.put((K) Integer.valueOf(++i), element);

		return collection.size() != 0;
	}

	/**
	 * Inserts all of the elements in the specified collection into this list, starting at the specified position. Shifts the element currently at
	 * that position (if any) and any subsequent elements to the right (increases their indices). The new elements will appear in the list in the
	 * order that they are returned by the specified collection's iterator.
	 *
	 * @param index      at which to insert the first element from the specified collection
	 * @param collection containing elements to be added to this list
	 * @return true if this map changed as a result of the call
	 * @throws NullPointerException if the given collection is null
	 */
	default boolean addAll(int index, Collection<V> collection) {
		ObjectUtil.requireNonNull(collection, "collection");
		this.shiftIndexes(index, null, collection.size());

		int i = 0;
		for (V object : collection)
			this.put((K) Integer.valueOf(index + i++), object);

		return collection.size() != 0;
	}

	/**
	 * The maximum integer. That have been stored as a key in this.
	 *
	 * @return the maximum index of this
	 */
	default int maxIndex() {
		int i = -1;

		for (Map.Entry<?, ?> entry : this.entrySet()) {
			Object key = entry.getKey();
			if (key instanceof Integer && (Integer) key > i)
				i = (Integer) key;
		}

		return i;
	}

	/**
	 * The minimum integer. That have been stored as a key in this.
	 *
	 * @return the minimum index of this
	 */
	default int minIndex() {
		int i = 1;

		for (Map.Entry<?, ?> entry : this.entrySet()) {
			Object key = entry.getKey();
			if (key instanceof Integer && (Integer) key < i)
				i = (Integer) key;
		}

		return i;
	}

	/**
	 * Depending on current indexing position on this map. Push an element at the index before the least index in this.
	 * <br><br><b>example:</b>
	 * <pre>
	 *     toString -> {-1:"-one", 0:"zero", 1:"one"}
	 *     push <- "-two"
	 *     toString -> {-2:"-two", -1:"-one", 0:"zero", 1:"one"}
	 * </pre>
	 *
	 * @param element to be pushed
	 * @return the previous value associated with key, or null if there was no mapping for key.
	 */
	default V push(V element) {
		return this.put((K) (Object) (this.minIndex() - 1), element);
	}

	/**
	 * Inserts the specified element at the specified position in this map indexing system. Shifts the element currently at that position (if any) and
	 * any subsequent elements to the left (subtract one to their indices).
	 *
	 * @param index   index at which the specified element is to be inserted
	 * @param element element to be inserted
	 */
	default void push(int index, V element) {
		this.shiftIndexes(null, index, -1);
		this.put((K) (Object) index, element);
	}

	/**
	 * Appends all of the elements in the specified collection to the start of this map indexing system, in the order that they are returned by the
	 * specified collection's Iterator. The behavior of this operation is undefined if the specified collection is modified while the operation is in
	 * progress.
	 *
	 * @param collection containing elements to be added to this map
	 * @return true if this map changed as a result of the call
	 * @throws NullPointerException if the given collection is null
	 */
	default boolean pushAll(Collection<V> collection) {
		ObjectUtil.requireNonNull(collection, "collection");
		int size = collection.size();
		int i = this.minIndex() - size;

		for (V element : collection)
			this.put((K) Integer.valueOf(++i), element);

		return size != 0;
	}

	/**
	 * Inserts all of the elements in the specified collection into this list, starting at the specified position. Shifts the element currently at
	 * that position (if any) and any subsequent elements to the left (subtract their indices). The new elements will appear in the list in the order
	 * that they are returned by the specified collection's iterator.
	 *
	 * @param index      at which to insert the first element from the specified collection
	 * @param collection containing elements to be added to this list
	 * @return true if this map changed as a result of the call
	 * @throws NullPointerException if the given collection is null
	 */
	default boolean pushAll(int index, Collection<V> collection) {
		ObjectUtil.requireNonNull(collection, "collection");
		this.shiftIndexes(null, index, -collection.size());

		int i = 0;
		for (V object : collection)
			this.put((K) Integer.valueOf(index + i++), object);

		return collection.size() != 0;
	}

	/**
	 * Copies all of the elements from the specified collection to this map. The effect of this call is equivalent to that of calling put(k, v) on
	 * this map once for each element from index i to element e in the specified collection. The behavior of this operation is undefined if the
	 * specified map is modified while the operation is in progress.
	 *
	 * @param collection elements to be stored in this map
	 */
	default void putAll(Collection<? extends V> collection) {
		int i = 0;
		for (V o : collection)
			this.put((K) Integer.valueOf(i++), o);
	}

	/**
	 * Remove a value in a specific index in this map. Then shift indexes to fill it's empty index.
	 *
	 * @param index to remove it's value
	 * @return the value that was in that index (before shifting indexes)
	 */
	default V removeIndex(int index) {
		V value = this.remove(index);
		this.shiftIndexes(index, null, -1);
		return value;
	}

	/**
	 * Remove a value {@link ObjectUtil#equals(Object, Object) equals} to the given value. This method removes the first value equals to the given
	 * value (If it's index is instanceOf {@link Integer}). Then return the index it associated to. Or -1 if there is no such value equals to the
	 * given value in this map. Then shift indexes to fill it's empty index.
	 *
	 * @param value to be removed
	 * @return the index that was associated to the given value
	 */
	default int removeIndexOf(Object value) {
		for (Map.Entry<K, V> entry : this.entrySet()) {
			K key = entry.getKey();
			if (key instanceof Integer && ObjectUtil.equals(entry.getValue(), value)) {
				int index = (Integer) key;
				this.remove(index);
				this.shiftIndexes(index, null, -1);
				return index;
			}
		}
		return -1;
	}

	/**
	 * Shift indexes within the given range by the given value.
	 * <p>
	 * ex.
	 * <pre>
	 *     map = {0:zero, 1:one, 2:two, 3:three}
	 *     map.shiftIndexes(<font color="gray">start</font> 1, <font color="gray">end</font> 2, <font color="gray">by</font> -1)
	 *     map => {0:zero, 1:two, 3:three}
	 *
	 *     map = {0:A, 1:B, 2:C, 3:D, 4:E}
	 *     map.shiftIndexes(<font color="gray">start</font> 2, <font color="gray">end</font> null, <font color="gray">by</font> 2)
	 *     map => {0:A, 1:B, 4:C, 5:D, 6:E}
	 * </pre>
	 *
	 * @param start the start of the shifting range (aka min)(null for no start)
	 * @param end   the end of the shifting range (aka max)(null for no end)
	 * @param by    the length to shift the values of this by
	 */
	default void shiftIndexes(Integer start, Integer end, int by) {
		HashMap<K, V> modified = new HashMap<>(this.size());

		boolean noStart = start == null, noEnd = end == null;

		//noinspection Java8MapForEach value may not be used
		this.entrySet().forEach(entry -> {
			K key = entry.getKey();
			//noinspection RedundantCast can't compile without it
			if (key instanceof Integer && (noStart || (Integer) key >= start) && (noEnd || (Integer) key <= end))
				modified.put(key, entry.getValue());
		});
		modified.forEach((key, value) -> {
			this.remove(key, value);
			int index = (Integer) key + by;

			if ((noStart || index >= start) && (noEnd || index <= end))
				this.put((K) (Integer) index, value);
		});
	}
}
