/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package org.cufy.lang;

import cufy.beans.StaticMethod;
import cufy.lang.Converter;
import cufy.lang.Global;
import cufy.lang.Recurse;
import cufy.lang.Type;
import cufy.util.Array$;
import cufy.util.Reflect$;

import java.io.File;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Base {@link Converter}.
 *
 * @author LSaferSE
 * @version 4 release (23-Jan-2020)
 * @since 31-Aug-19
 */
public class BaseConverter extends Converter implements Global {
	/**
	 * The global instance to avoid unnecessary instancing.
	 */
	final public static BaseConverter global = new BaseConverter();

	{
		DEBUGGING = false;
	}

	@StaticMethod
	@Override
	protected BaseConvertPosition newConvertPosition() {
		return new BaseConvertPosition();
	}

	/**
	 * Construct a new array with a type of the given type. Then copy all the elements from the given 'source' to the constructed array. And convert
	 * all elements after reading it from the 'source' to match the 'productClass' using this converter.
	 *
	 * @param source       to copy data from
	 * @param productClass the class of the product array
	 * @param position     to depend on while copping (hunting recursion for example)
	 * @return a new array with the type given. Holds all the elements from the given array. Converted to match the type of the new array
	 * @throws NullPointerException     if any of the given parameters is null
	 * @throws IllegalArgumentException if the given 'source' isn't an array. Or if the given 'productClass' is not a class for arrays.
	 */
	@ConvertMethod(in = @Type(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			int[].class,
			long[].class,
			short[].class
	}), out = @Type(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			int[].class,
			long[].class,
			short[].class
	}))
	protected Object arrayToArray(Object source, Class<?> productClass, BaseConvertPosition position) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			Objects.requireNonNull(position, "position");
			if (!source.getClass().isArray())
				throw new IllegalArgumentException(source + " is not an array");
			if (!productClass.isArray())
				throw new IllegalArgumentException(productClass + " is not a class for arrays");
		}

		Class<?> productComponentType = productClass.getComponentType();
		int length = Array.getLength(source);
		Object product = Array.newInstance(productComponentType, length);

		//Casting foreach element
		for (int i = 0; i < length; i++) {
			Object element = Array.get(source, i);
			element = position.convert(element, productComponentType, null, null, false, source, product);
			Array.set(product, i, element);
		}

		return product;
	}

	/**
	 * Construct a new collection with the type of the given type. Then copy all elements from the given 'source' to the constructed collection.
	 *
	 * @param source       to get the value from
	 * @param productClass the class of the targeted collection
	 * @return the value of the given source on a collection of the given class
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws IllegalArgumentException     if the given 'source' is not an array. Or if the given 'productClass' is not a class for collections
	 * @throws ReflectiveOperationException if any exception occurred while trying to construct the product collection
	 */
	@ConvertMethod(in = @Type(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			int[].class,
			float[].class,
			long[].class,
			short[].class,
	}), out = @Type(subin = Collection.class))
	protected Collection arrayToCollection(Object source, Class<? extends Collection> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!source.getClass().isArray())
				throw new IllegalArgumentException(source + " is not an array");
			if (!Collection.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for collections");
		}

		List<?> list = Array$.asList0(source);

		try {
			return productClass.getConstructor(Collection.class).newInstance(list);
		} catch (ReflectiveOperationException ignored) {
		}

		Collection<Object> product = productClass.getConstructor().newInstance();
		product.addAll(list);
		return product;
	}

	/**
	 * Construct a new map of the given type. Then put every element from the given 'source' to the constructed map. Every element will be put to an
	 * {@link Integer} key representing it's index on the 'source'.
	 *
	 * @param source       to get the value from
	 * @param productClass the class of the map to construct
	 * @return the value of the given array on a map of the given class
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws IllegalArgumentException     if the given 'source' is not an array. Or if the given 'productClass' is not a class for maps.
	 * @throws ReflectiveOperationException if any exception occurs while trying to construct the new map
	 */
	@ConvertMethod(in = @Type(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			int[].class,
			float[].class,
			long[].class,
			short[].class,
	}), out = @Type(subin = Map.class))
	protected Map arrayToMap(Object source, Class<? extends Map> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!source.getClass().isArray())
				throw new IllegalArgumentException(source + " is not an array");
			if (!Map.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for maps");
		}

		int length = Array.getLength(source);
		Map<Object, Object> product = productClass.getConstructor().newInstance();

		for (int i = 0; i < length; i++)
			product.put(i, Array.get(source, i));

		return product;
	}

	/**
	 * Construct a new array with a type of the given class. Then copy all elements from the given 'source' to the constructed array. And convert *
	 * all elements after reading it from the 'source' to match the 'productClass' using this converter.
	 *
	 * @param source       to copy data from
	 * @param productClass the class of the product array
	 * @param position     to depend on while copping (hunting recursion for example)
	 * @return a new array with the type given. Holds all the elements from the given array. Converted to math the type of the new array
	 * @throws NullPointerException     if any of the given parameters is null
	 * @throws IllegalArgumentException if the given 'productClass' is not a class for arrays
	 */
	@SuppressWarnings("DuplicatedCode")
	@ConvertMethod(in = @Type(subin = Collection.class), out = @Type(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			int[].class,
			long[].class,
			short[].class
	}))
	protected Object collectionToArray(Collection<?> source, Class<?> productClass, BaseConvertPosition position) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			Objects.requireNonNull(position, "position");
			if (!productClass.isArray())
				throw new IllegalArgumentException(productClass + " is not a class for arrays");
		}

		Class<?> productComponentType = productClass.getComponentType();
		Object product = Array.newInstance(productClass, source.size());

		int i = 0;
		for (Object element : source) {
			element = position.convert(element, productComponentType, null, null, false, source, product);
			Array.set(product, i++, element);
		}

		return product;
	}

	/**
	 * Construct a new collection with the type of the given type. Then copy all elements from the given 'source' to the constructed collection.
	 *
	 * @param source       to get the value from
	 * @param productClass the class of the targeted collection
	 * @return the value of the given source on a collection of the given class
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws IllegalArgumentException     if the given 'productClass' is not a class for collections
	 * @throws ReflectiveOperationException if any exception occurred while trying to construct the product collection
	 */
	@ConvertMethod(in = @Type(subin = Collection.class), out = @Type(subin = Collection.class))
	protected Collection collectionToCollection(Collection<?> source, Class<? extends Collection> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!Collection.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for collections");
		}

		try {
			return productClass.getConstructor(Collection.class).newInstance(source);
		} catch (ReflectiveOperationException ignored) {
		}

		Collection<Object> product = productClass.getConstructor().newInstance();
		product.addAll(source);
		return product;
	}

	/**
	 * Construct a new map of the given type. Then put every element from the given 'source' to the constructed map. Every element will be put to an
	 * {@link Integer} key representing it's index on the 'source'.
	 *
	 * @param source       to get the value from
	 * @param productClass the class of the map to construct
	 * @return the value of the given collection on a map of the given class
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws IllegalArgumentException     or if the given 'productClass' is not a class for maps.
	 * @throws ReflectiveOperationException if any exception occurs while trying to construct the new map
	 */
	@ConvertMethod(in = @Type(subin = Collection.class), out = @Type(subin = Map.class))
	protected Map collectionToMap(Collection<?> source, Class<? extends Map> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!Map.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for maps");
		}

		Map<Object, Object> map = productClass.getConstructor().newInstance();

		int i = 0;
		for (Object element : source)
			map.put(i++, element);

		return map;
	}

	/**
	 * Construct a file object of the given type. That represents the file that have been represented from the given 'source'.
	 *
	 * @param source       to get the value from
	 * @param productClass the class of the file to construct
	 * @return the value of the given file on a file of the given class
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws ReflectiveOperationException if any exception occurred while trying to construct the new file
	 * @throws IllegalArgumentException     if the given 'productClass' is not a class for files
	 */
	@ConvertMethod(in = @Type(subin = File.class), out = @Type(subout = File.class))
	protected File fileToFile(File source, Class<? extends File> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!File.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for files");
		}

		return productClass.getConstructor(String.class).newInstance(source.getAbsolutePath());
	}

	/**
	 * Construct a new array with type of the given type. And copy elements from the given 'source' to that array. This method will copy element that
	 * have been mapped to a non-negative {@link Integer} key only. Those keys will be used as Indexes for the elements they're pointing at. And the
	 * indexes between will be filled with nulls.
	 *
	 * @param source       to get the value from
	 * @param productClass the class of the targeted array
	 * @param position     the position to depend on
	 * @return the value of the given map on a array of the given class
	 * @throws NullPointerException     if any of the given parameters is null
	 * @throws IllegalArgumentException if the given 'productClass' is not a class for arrays
	 */
	@SuppressWarnings("DuplicatedCode")
	@ConvertMethod(in = @Type(subin = Map.class), out = @Type(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			int[].class,
			float[].class,
			long[].class,
			short[].class,
	}))
	protected Object mapToArray(Map<?, ?> source, Class<?> productClass, BaseConvertPosition position) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			Objects.requireNonNull(position, "position");
			if (!productClass.isArray())
				throw new IllegalArgumentException(productClass + " is not a class for arrays");
		}

		List<?> list;
		try {
			list = this.mapToList(source, ArrayList.class);
		} catch (ReflectiveOperationException e) {
			throw new Error("Should be able to construct a new ArrayList", e);
		}
		Class<?> productComponentType = productClass.getComponentType();
		Object product = Array.newInstance(productComponentType, list.size());

		int i = 0;
		for (Object element : list) {
			element = position.convert(element, productComponentType, null, null, false, source, product);
			Array.set(product, i++, element);
		}

		return product;
	}

	/**
	 * Construct a new collection with type of the given class. And fill it with values from the given 'source'.
	 *
	 * @param source       to get data from
	 * @param productClass the type of the collection to construct
	 * @return a new collection with type of the given class. Filled with the data from the given source
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws IllegalArgumentException     if the given 'productClass' is not a class for maps
	 * @throws ReflectiveOperationException if any exception occurred while trying to construct the product collection
	 */
	@ConvertMethod(in = @Type(subin = Map.class), out = @Type(subin = Collection.class, subout = List.class))
	protected Collection mapToCollection(Map<?, ?> source, Class<? extends Collection> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!Collection.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for collections");
		}

		try {
			return productClass.getConstructor(Collection.class).newInstance(source.values());
		} catch (ReflectiveOperationException ignored) {
		}

		Collection<Object> collection = productClass.getConstructor().newInstance();
		collection.addAll(source.values());
		return collection;
	}

	/**
	 * Construct a new list with type of the given type. This method will copy element that have been mapped to a non-negative {@link Integer} key
	 * only. Those keys will be used as Indexes for the elements they're pointing at. And the indexes between will be filled with nulls.
	 *
	 * @param source       to get data from
	 * @param productClass the class of the targeted list
	 * @return the value of the given map on a list of the given class
	 * @throws ReflectiveOperationException if any exception occurred while trying to construct the product list
	 * @throws NullPointerException         if any of the given parameters is null
	 * @throws IllegalArgumentException     if the given 'productClass' is not a class for lists
	 */
	@ConvertMethod(in = @Type(subin = Map.class), out = @Type(subin = List.class))
	protected List mapToList(Map<?, ?> source, Class<? extends List> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!List.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for lists");
		}

		List<Object> product = productClass.getConstructor().newInstance();

		//noinspection Java8MapForEach
		source.entrySet().forEach(entry -> {
			Object key = entry.getKey();

			if (key instanceof Integer && (Integer) key > -1) {
				int i = (Integer) key;

				while (i >= product.size()) {
					product.add(null);
				}

				product.set(i, entry.getValue());
			}
		});

		return product;
	}

	/**
	 * Construct a new map copping the value of the given 'source'.
	 *
	 * @param source       to copy value from
	 * @param productClass the class of the targeted map to be constructed
	 * @return a map of the given type that holds the values of the given source
	 * @throws ReflectiveOperationException if any exception occurred while trying to construct the product map
	 * @throws IllegalArgumentException     if the given 'productClass' is not a class for maps
	 * @throws NullPointerException         if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Map.class), out = @Type(subin = Map.class))
	protected Map mapToMap(Map<?, ?> source, Class<? extends Map> productClass) throws ReflectiveOperationException {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			if (!Map.class.isAssignableFrom(productClass))
				throw new IllegalArgumentException(productClass + " is not a class for maps");
		}

		try {
			return productClass.getConstructor(Map.class).newInstance(source);
		} catch (ReflectiveOperationException ignored) {
		}

		Map<Object, Object> product = productClass.getConstructor().newInstance();
		product.putAll(source);
		return product;
	}

	/**
	 * Returns null.
	 *
	 * @return null
	 */
	@ConvertMethod(in = @Type(subin = Void.class), out = @Type(subin = Object.class))
	protected Object nullToObject() {
		return null;
	}

	/**
	 * Get a {@link Byte} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param source to get the value from
	 * @return the value of the given number as byte
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Number.class), out = @Type(in = {Byte.class, byte.class}))
	protected Byte numberToByte(Number source) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
		}

		return source.byteValue();
	}

	/**
	 * Get a {@link Double} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param source to get the value from
	 * @return the value of the given number as double
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Number.class), out = @Type(in = {Double.class, double.class}))
	protected Double numberToDouble(Number source) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
		}

		return source.doubleValue();
	}

	/**
	 * Get a {@link Float} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param source to get the value from
	 * @return the value of the given number as float
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Number.class), out = @Type(in = {Float.class, float.class}))
	protected Float numberToFloat(Number source) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
		}

		return source.floatValue();
	}

	/**
	 * Get a {@link Integer} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param source to get the value from
	 * @return the value of the given number as integer
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Number.class), out = @Type(in = {Integer.class, int.class}))
	protected Integer numberToInteger(Number source) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
		}

		return source.intValue();
	}

	/**
	 * Get a {@link Long} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param source to get the value from
	 * @return the value of the given number as long
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Number.class), out = @Type(in = {Long.class, long.class}))
	protected Long numberToLong(Number source) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
		}

		return source.longValue();
	}

	/**
	 * Get a {@link Short} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param source to get the value from
	 * @return the value of the given number as short
	 * @throws NullPointerException if any of the given parameters is null
	 */
	@ConvertMethod(in = @Type(subin = Number.class), out = @Type(in = {Short.class, short.class}))
	protected Short numberToShort(Number source) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
		}

		return source.shortValue();
	}

	/**
	 * Returns the string representation of the Object argument.
	 *
	 * @param source an Object.
	 * @return the string representation of the Object argument
	 */
	@ConvertMethod(in = @Type(subin = Object.class), out = @Type(in = String.class))
	protected String objectToString(Object source) {
		return String.valueOf(source);
	}

	/**
	 * Cast the given 'source' to the given 'productClass'. Using the java primitive types casting method.
	 *
	 * @param source       the value to be casted
	 * @param productClass the type to cast the value to
	 * @return the given value casted to the given productClass
	 * @throws NullPointerException if the given 'productClass' is null. Or if the 'productClass' is a primitive type. And the given 'source' is null
	 * @throws ClassCastException   if the source can't be casted to the productClass using the java primitive types casting method
	 */
	@ConvertMethod(in = @Type(in = {
			boolean.class,
			byte.class,
			char.class,
			double.class,
			float.class,
			int.class,
			long.class,
			short.class,
			Boolean.class,
			Byte.class,
			Character.class,
			Double.class,
			Float.class,
			Integer.class,
			Long.class,
			Short.class,
	}), out = @Type(in = {
			boolean.class,
			byte.class,
			char.class,
			double.class,
			float.class,
			int.class,
			long.class,
			short.class,
			Boolean.class,
			Byte.class,
			Character.class,
			Double.class,
			Float.class,
			Integer.class,
			Long.class,
			Short.class,
	}))
	protected Object primitiveToPrimitive(Object source, Class<?> productClass) {
		if (DEBUGGING) {
			Objects.requireNonNull(productClass, "productClass");
		}

		return Reflect$.primitiveCast(productClass, source);
	}

	/**
	 * The source object of the given recurse object depending on the given position.
	 *
	 * @param source       the object
	 * @param productClass the targeted return class
	 * @param position     to depend on
	 * @return the source object of the given recurse object
	 * @throws NullPointerException if any of the given parameters is null
	 * @throws ClassCastException   if the product instance of the given recurse is not instance of the given 'productClass'
	 */
	@ConvertMethod(in = @Type(subin = Recurse.class), out = @Type(subin = Object.class))
	protected Object recurseToObject(Object source, Class<?> productClass, BaseConvertPosition position) {
		if (DEBUGGING) {
			Objects.requireNonNull(source, "source");
			Objects.requireNonNull(productClass, "productClass");
			Objects.requireNonNull(position, "position");
		}

		if (position.parents.containsKey(source)) {
			return productClass.cast(position.parents.get(source));
		} else {
			throw new IllegalArgumentException(source + " did not recurse");
		}
	}

	/**
	 * Helps to effect the casting behavior depending on the casting position.
	 */
	public class BaseConvertPosition implements ConvertPosition {
		/**
		 * The casting parents mappings. Helps match the recursive storing.
		 *
		 * @apiNote source => instance
		 * @implSpec no modification it after the constructing
		 */
		final public Map<Object, Object> parents = new HashMap<>(10);

		/**
		 * Default constructor.
		 */
		public BaseConvertPosition() {
		}

		/**
		 * Construct with the given parameters.
		 *
		 * @param parents       the parents mappings.
		 * @param sourceParent  a source mapping
		 * @param productParent a recurse mapping for the source
		 * @throws NullPointerException if 'parents' equals null
		 */
		public BaseConvertPosition(Map<Object, Object> parents, Object sourceParent, Object productParent) {
			if (DEBUGGING) {
				Objects.requireNonNull(parents, "parents");
				Objects.requireNonNull(sourceParent, "sourceParent");
				Objects.requireNonNull(productParent, "productParent");
			}

			this.parents.putAll(parents);
			this.parents.put(sourceParent, productParent);
		}

		/**
		 * Cast the given object to the given product class. Using the first method annotated with {@link ConvertMethod}. And that annotation allows
		 * the given 'productClass' and 'sourceClass'. (methods are ordered randomly).
		 *
		 * @param source        the object to be casted
		 * @param productClass  the targeted class to cast the object to
		 * @param position      to format depending on (null to create a new one)
		 * @param sourceClass   the targeted method parameter type (null for the class of the given object)
		 * @param clone         true to create a new instance even when the object is instance of the targeted class
		 * @param sourceParent  a source mapping
		 * @param productParent a recurse mapping for the source
		 * @return the given object casted to the given 'out' class
		 * @throws ClassCastException       on casting failure
		 * @throws IllegalArgumentException optional. on casting failure
		 * @throws NullPointerException     if the 'out' param equals to null
		 */
		public Object convert(Object source, Class<?> productClass, BaseConvertPosition position, Class<?> sourceClass, boolean clone, Object sourceParent, Object productParent) {
			if (DEBUGGING) {
				Objects.requireNonNull(productClass, "productClass");
				Objects.requireNonNull(sourceParent, "sourceParent");
				Objects.requireNonNull(productParent, "productParent");
			}

			if (position == null)
				position = new BaseConvertPosition(this.parents, sourceParent, productParent);
			if (sourceClass == null)
				sourceClass = this.parents.containsKey(source) ? Recurse.class : null;

			return BaseConverter.this.convert(source, productClass, position, sourceClass, clone);
		}
	}
}
