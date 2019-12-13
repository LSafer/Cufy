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

import cufy.lang.Castable;
import cufy.lang.Caster;
import cufy.util.ObjectUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

/**
 * A {@link Map maps} plugin interface. An interface that adds useful methods to the implement map.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @author LSaferSE
 * @version 3 release (07-Dec-2019)
 * @since 18-Sep-19
 */
public interface JetMap<K, V> extends Map<K, V>, Castable {
	/**
	 * Cast all keys and values in this depending on the classes given.
	 *
	 * @param keyClass   the class that keys should have
	 * @param valueClass the class that values should have
	 * @throws NullPointerException if ether the given 'keyClass' or 'valueClass' is null
	 */
	default void castAll(Class<?> keyClass, Class<?> valueClass) {
		ObjectUtil.requireNonNull(keyClass, "keyClass");
		ObjectUtil.requireNonNull(valueClass, "valueClass");

		Caster caster = this.caster();
		Set<Map.Entry<K, V>> entrySet = this.entrySet();
		Map<K, V> cast = new HashMap<>(entrySet.size());

		entrySet.forEach(entry -> {
			K key = entry.getKey();
			V value = entry.getValue();

			if (keyClass.isInstance(key)) {
				entry.setValue(caster.cast(value, (Class<? super V>) valueClass));
			} else {
				cast.put(key, value);
			}
		});
		cast.forEach((key, value) -> {
			this.remove(key);
			this.put(caster.cast(key, (Class<? super K>) keyClass), caster.cast(value, (Class<? super V>) valueClass));
		});
	}

	/**
	 * Do foreach but every key casted to the given key-class and every value casted to the given value-class.
	 *
	 * @param keyClass   the class that every key will be casted to
	 * @param valueClass the class that every value will be casted to
	 * @param action     to be applied foreach casted key and value
	 * @param <KK>       the type of keys
	 * @param <VV>       the type of values
	 * @throws NullPointerException if the given 'keyClass' or 'valueClass' or 'action' is null
	 */
	default <KK, VV> void castedForEach(Class<KK> keyClass, Class<VV> valueClass, BiConsumer<KK, VV> action) {
		ObjectUtil.requireNonNull(keyClass, "keyClass");
		ObjectUtil.requireNonNull(valueClass, "valueClass");
		ObjectUtil.requireNonNull(action, "action");
		Caster caster = this.caster();
		this.forEach((key, value) -> action.accept(caster.cast(key, keyClass), caster.cast(value, valueClass)));
	}

	/**
	 * Apply the passed function. ONLY if the stored value Not equals to null, and is instance of the given klass. Then return the value.
	 *
	 * @param klass    to make sure the mapped value is instance of the needed class
	 * @param key      to get it's mapped value
	 * @param function to be applied
	 * @param <T>      type of value
	 * @return the mapped value to the given key
	 * @throws NullPointerException if ether the given 'klass' or 'function' is null
	 */
	default <T> T doIfCastedPresent(Class<T> klass, Object key, Consumer<T> function) {
		ObjectUtil.requireNonNull(klass, "klass");
		ObjectUtil.requireNonNull(function, "function");
		T value = this.getCasted(klass, key);

		if (value != null)
			function.accept(value);

		return value;
	}

	/**
	 * Do foreach but only for keys and values matches the given conditions.
	 *
	 * @param keyClass   the class that the filtered key should have
	 * @param valueClass the class that the filtered value should have
	 * @param action     to be applied foreach filtered key and value
	 * @param <KK>       the type of keys
	 * @param <VV>       the type of values
	 * @throws NullPointerException if the given 'keyClass' or 'valueClass' or 'action' is null
	 */
	default <KK, VV> void filteredForEach(Class<KK> keyClass, Class<VV> valueClass, BiConsumer<KK, VV> action) {
		ObjectUtil.requireNonNull(keyClass, "keyClass");
		ObjectUtil.requireNonNull(valueClass, "valueClass");
		ObjectUtil.requireNonNull(action, "action");
		this.forEach((key, value) -> {
			if (keyClass.isInstance(key) && valueClass.isInstance(value))
				action.accept((KK) key, (VV) value);
		});
	}

	/**
	 * Get the value mapped to the given key. Or returns the given default value case the key didn't exist or it's mapped to null.
	 *
	 * <ul>
	 * <li>
	 * note: this will cast the mapped value. And return it. But it'll not map the casted instance.
	 * </li>
	 * </ul>
	 *
	 * @param klass to make sure the mapped value is instance of the needed class
	 * @param key   to get it's mapped value
	 * @param <T>   type of value
	 * @return the mapped value to the given key
	 * @throws NullPointerException if the given class is null
	 */
	default <T> T getCasted(Class<? extends T> klass, Object key) {
		ObjectUtil.requireNonNull(klass, "klass");
		return this.caster().cast(this.get(key), (Class<? super T>) klass);
	}

	/**
	 * Accept the 'thisContains' consumer foreach key this map contains but the given map dont. Then accept the 'mapContains' consumer foreach key the
	 * given map contains but this map dont.
	 *
	 * @param map          to match with this
	 * @param mapContains  action to do with the keys the given map contains but this map don't
	 * @param thisContains action to do with the keys this map contains but the given map don't
	 * @throws NullPointerException if the given 'map' or 'thisContains' or 'mapContains' is null
	 */
	default void match(Map<K, V> map, BiConsumer<K, V> thisContains, BiConsumer<K, V> mapContains) {
		ObjectUtil.requireNonNull(map, "map");
		ObjectUtil.requireNonNull(thisContains, "thisContains");
		ObjectUtil.requireNonNull(mapContains, "mapContains");

		Set<K> ours = new HashSet<>(this.keySet());
		Set<K> theirs = new HashSet<>(ours.size());

		map.keySet().forEach(key -> {
			if (!ours.remove(key))
				theirs.add(key);
		});

		ours.forEach(key -> thisContains.accept(key, this.get(key)));
		theirs.forEach(key -> mapContains.accept(key, map.get(key)));
	}

	/**
	 * Put all of the mappings on the given map to this map. Then accept the 'thisContains' consumer foreach key this map contains but the given map
	 * dont. Then accept the 'mapContains' consumer foreach key the given map contains but this map dont.
	 *
	 * @param map          to be put
	 * @param mapContains  action to do with the keys the given map contains but this map don't
	 * @param thisContains action to do with the keys this map contains but the given map don't
	 * @throws NullPointerException if any of the given parameters is null
	 */
	default void putAll(Map<K, V> map, BiConsumer<K, V> mapContains, BiConsumer<K, V> thisContains) {
		ObjectUtil.requireNonNull(map, "map");
		ObjectUtil.requireNonNull(mapContains, "mapContains");
		ObjectUtil.requireNonNull(thisContains, "thisContains");

		Set<K> ours = new HashSet<>(this.keySet());
		Set<K> theirs = new HashSet<>(ours.size());

		map.forEach((key, value) -> {
			if (!ours.remove(key))
				theirs.add(key);
		});

		ours.forEach(key -> thisContains.accept(key, this.get(key)));
		theirs.forEach(key -> mapContains.accept(key, map.get(key)));
	}

	/**
	 * Removes all of the entries of this that satisfy the given predicate. Errors or runtime exceptions thrown during iteration or by the predicate
	 * are relayed to the caller.
	 *
	 * @param filter a predicate which returns true for entries to be removed
	 * @return true if any entries were removed
	 * @throws NullPointerException if the given 'filter' is null
	 */
	default boolean removeIf(BiPredicate<K, V> filter) {
		ObjectUtil.requireNonNull(filter, "filter");
		Set<K> keySet = new HashSet<>(this.size());

		this.forEach((key, value) -> {
			if (filter.test(key, value))
				keySet.add(key);
		});

		if (keySet.isEmpty()) {
			return false;
		} else {
			keySet.forEach(this::remove);
			return true;
		}
	}

	/**
	 * Remove a value {@link ObjectUtil#equals(Object, Object) equals} to the given value. This method removes the first value equals to the given
	 * value. Then return the key it associated to. Or null if ether the key is null. Or there is no such value equals to the given value in this
	 * map.
	 *
	 * @param value to be removed
	 * @return the key the value is associated to
	 */
	default K removeValue(Object value) {
		for (Map.Entry<K, V> entry : this.entrySet())
			if (ObjectUtil.equals(entry.getValue(), value)) {
				K key = entry.getKey();
				this.remove(key);
				return key;
			}
		return null;
	}
}
