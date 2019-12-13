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
package cufy.lang.alter;

import cufy.lang.Caster;
import cufy.lang.Global;
import cufy.lang.Range;
import cufy.lang.Recurse;
import org.cufy.text.JSON;
import org.cufy.util.CollectionUtil;
import org.cufy.util.ObjectUtil;
import org.cufy.util.StringUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Default casting engine.
 *
 * <ul>
 *     <font color="orange" size="4"><b>Dynamic Methods:</b></font>
 *     <li>
 *         <font color="yellow">{@link Collection}</font>
 *         <ul>
 *             <li>{@link #collection2array}</li>
 *             <li>{@link #collection2collection}</li>
 *             <li>{@link #collection2map}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link File}</font>
 *         <ul>
 *             <li>{@link #file2file}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Map}</font>
 *         <ul>
 *             <li>{@link #map2array}</li>
 *             <li>{@link #map2collection}</li>
 *             <li>{@link #map2list}</li>
 *             <li>{@link #map2map}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Number}</font>
 *         <ul>
 *             <li>{@link #number2byte}</li>
 *             <li>{@link #number2double}</li>
 *             <li>{@link #number2float}</li>
 *             <li>{@link #number2integer}</li>
 *             <li>{@link #number2long}</li>
 *             <li>{@link #number2short}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Object}</font>
 *         <ul>
 *             <li>{@link #object2string}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Recurse}</font>
 *         <ul>
 *             <li>{@link #recurse2object}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link CharSequence}</font>
 *         <ul>
 *             <li>{@link #sequence2object}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author LSaferSE
 * @version 3 release (26-Nov-2019)
 * @since 31-Aug-19
 */
public class Cast extends Caster implements Global {
	/**
	 * The global instance to avoid unnecessary instancing.
	 */
	final public static Cast global = new Cast();

	@StaticMethod
	@Override
	protected ClayCastPosition newCastPosition() {
		return new ClayCastPosition();
	}

	/**
	 * Get an array with the type of the given class. With the value of the given {@link Collection}.
	 *
	 * @param collection to get the value from
	 * @param out        the class of the targeted array
	 * @param position   the position to depend on
	 * @param <T>        the targeted type
	 * @return the value of the given collection on an array of the given class
	 */
	@CastMethod(in = @Range(subin = {
			Collection.class,
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			long[].class,
			short[].class
	}), out = @Range(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			long[].class,
			short[].class
	}))
	protected <T> T collection2array(Object collection, Class<? super T> out, ClayCastPosition position) {
		Collection<?> elements = CollectionUtil.from(collection);

		Class<?> type = out.getComponentType();
		T array = (T) Array.newInstance(type, elements.size());

		int i = 0;
		for (Object element : elements)
			Array.set(array, i++, position.cast(element, (Class<? super Object>) type, collection, array));

		return array;
	}

	/**
	 * Get a {@link Collection} with the type of the given class. With the value of the given {@link Collection}.
	 *
	 * @param collection to get the value from
	 * @param out        the class of the targeted collection
	 * @param position   the position to depend on
	 * @param <C>        the targeted type
	 * @return the value of the given collection on a collection of the given class
	 */
	@CastMethod(out = @Range(subin = Collection.class), in = @Range(subin = {
			Collection.class,
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			long[].class,
			short[].class
	}))
	protected <C extends Collection<?>> C collection2collection(Object collection, Class<? super C> out, ClayCastPosition position) {
		try {
			Collection<?> elements = CollectionUtil.from(collection);
			C collection1 = (C) out.getConstructor().newInstance();

			int i;
			for (Object element : elements)
				collection1.add(position.cast(element, Object.class, collection, collection1));

			return collection1;
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a {@link Map} with the type of the given class. With the value of the given {@link Collection}.
	 *
	 * @param collection to get the value from
	 * @param out        the class of the targeted map
	 * @param position   the position to depend on
	 * @param <M>        the targeted type
	 * @return the value of the given collection on a map of the given class
	 */
	@CastMethod(out = @Range(subin = Map.class), in = @Range(subin = {
			Collection.class,
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			long[].class,
			short[].class
	}))
	protected <M extends Map<?, ?>> M collection2map(Object collection, Class<? super M> out, ClayCastPosition position) {
		try {
			Collection<?> elements = CollectionUtil.from(collection);
			M map = (M) out.getConstructor().newInstance();

			int i = 0;
			for (Object element : elements)
				((Map<Object, Object>) map).put(i++, position.cast(element, Object.class, collection, map));

			return map;
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a {@link File} with the type of the given class. With the value of the given {@link File}.
	 *
	 * @param file     to get the value from
	 * @param out      the class of the targeted file
	 * @param position the position to depend on
	 * @param <F>      the targeted type
	 * @return the value of the given file on a file of the given class
	 */
	@CastMethod(in = @Range(subin = File.class), out = @Range(subout = File.class))
	protected <F extends File> F file2file(File file, Class<? super F> out, ClayCastPosition position) {
		try {
			return (F) out.getConstructor(String.class).newInstance(file.toString());
		} catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a array with the type of the given class. With the value of the given {@link Map}.
	 *
	 * @param map      to get the value from
	 * @param out      the class of the targeted array
	 * @param position the position to depend on
	 * @param <T>      the targeted type
	 * @return the value of the given map on a array of the given class
	 */
	@CastMethod(in = @Range(subin = Map.class), out = @Range(subin = {
			Object[].class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			long[].class,
			short[].class
	}))
	protected <T> T map2array(Map<?, ?> map, Class<? super T> out, ClayCastPosition position) {
		return this.collection2array(this.map2list(map, ArrayList.class, position), out, position);
	}

	/**
	 * Get a {@link Collection} with the type of the given class. With the value of the given {@link Map}.
	 *
	 * @param map      to get the value from
	 * @param out      the class of the targeted collection
	 * @param position the position to depend on
	 * @param <C>      the targeted type
	 * @return the value of the given map on a collection of the given class
	 */
	@CastMethod(in = @Range(subin = Map.class), out = @Range(subin = Collection.class, out = List.class))
	protected <C extends Collection<?>> C map2collection(Map<?, ?> map, Class<? super C> out, ClayCastPosition position) {
		try {
			C collection = (C) out.getConstructor().newInstance();
			((Collection<Object>) collection).addAll(map.values());
			return collection;
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a {@link List} with the type of the given class. With the value of the given {@link Map}.
	 *
	 * @param map      to get the value from
	 * @param out      the class of the targeted list
	 * @param position the position to depend on
	 * @param <L>      the targeted type
	 * @return the value of the given map on a list of the given class
	 */
	@CastMethod(in = @Range(subin = Map.class), out = @Range(subin = List.class))
	protected <L extends List<?>> L map2list(Map<?, ?> map, Class<? super L> out, ClayCastPosition position) {
		try {
			L list = (L) out.getConstructor().newInstance();

			//noinspection Java8MapForEach value may not be used
			map.entrySet().forEach(entry -> {
				Object key = entry.getKey();

				if (key instanceof Integer && (Integer) key > -1) {
					if ((Integer) key >= list.size())
						CollectionUtil.fill((List<Object>) list, (Integer) key + 1, i -> null);

					((List<Object>) list).set((Integer) key, entry.getValue());
				}
			});

			return list;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a {@link Map} with the type of the given class. With the value of the given {@link Map}.
	 *
	 * @param map      to get the value from
	 * @param out      the class of the targeted map
	 * @param position the position to depend on
	 * @param <M>      the targeted type
	 * @return the value of the given map on a map of the given class
	 */
	@CastMethod(in = @Range(subin = Map.class), out = @Range(subin = Map.class))
	protected <M extends Map<?, ?>> M map2map(Map<?, ?> map, Class<? super M> out, ClayCastPosition position) {
		try {
			M instance = (M) out.getConstructor().newInstance();
			((Map<Object, Object>) instance).putAll(map);
			return instance;
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Get a {@link Byte} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param number   to get the value from
	 * @param out      byte class
	 * @param position the position to depend on
	 * @return the value of the given number as byte
	 */
	@CastMethod(in = @Range(subin = Number.class), out = @Range(in = {Byte.class, byte.class}))
	protected Byte number2byte(Number number, Class<Byte> out, ClayCastPosition position) {
		return number.byteValue();
	}

	/**
	 * Get a {@link Double} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param number   to get the value from
	 * @param out      double class
	 * @param position the position to depend on
	 * @return the value of the given number as double
	 */
	@CastMethod(in = @Range(subin = Number.class), out = @Range(in = {Double.class, double.class}))
	protected Double number2double(Number number, Class<Double> out, ClayCastPosition position) {
		return number.doubleValue();
	}

	/**
	 * Get a {@link Float} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param number   to get the value from
	 * @param out      float class
	 * @param position the position to depend on
	 * @return the value of the given number as float
	 */
	@CastMethod(in = @Range(subin = Number.class), out = @Range(in = {Float.class, float.class}))
	protected Float number2float(Number number, Class<Float> out, ClayCastPosition position) {
		return number.floatValue();
	}

	/**
	 * Get a {@link Integer} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param number   to get the value from
	 * @param out      integer class
	 * @param position the position to depend on
	 * @return the value of the given number as integer
	 */
	@CastMethod(in = @Range(subin = Number.class), out = @Range(in = {Integer.class, int.class}))
	protected Integer number2integer(Number number, Class<Integer> out, ClayCastPosition position) {
		return number.intValue();
	}

	/**
	 * Get a {@link Long} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param number   to get the value from
	 * @param out      long class
	 * @param position the position to depend on
	 * @return the value of the given number as long
	 */
	@CastMethod(in = @Range(subin = Number.class), out = @Range(in = {Long.class, long.class}))
	protected Long number2long(Number number, Class<Long> out, ClayCastPosition position) {
		return number.longValue();
	}

	/**
	 * Get a {@link Short} with the type of the given class. With the value of the given {@link Number}.
	 *
	 * @param number   to get the value from
	 * @param out      short class
	 * @param position the position to depend on
	 * @return the value of the given number as short
	 */
	@CastMethod(in = @Range(subin = Number.class), out = @Range(in = {Short.class, short.class}))
	protected Short number2short(Number number, Class<Short> out, ClayCastPosition position) {
		return number.shortValue();
	}

	/**
	 * Returns the string representation of the Object argument.
	 *
	 * @param object   an Object.
	 * @param out      string class
	 * @param position the position to depend on
	 * @return the string representation of the Object argument
	 */
	@CastMethod(in = @Range(subin = Object.class), out = @Range(in = String.class))
	protected String object2string(Object object, Class<String> out, ClayCastPosition position) {
//		return JSON.global.format(object); We are in Java not JSON :)
		return String.valueOf(object);
	}

	/**
	 * The source object of the given recurse object depending on the given position.
	 *
	 * @param recurse  the object
	 * @param out      the targeted return class
	 * @param position to depend on
	 * @return the source object of the given recurse object
	 */
	@CastMethod(in = @Range(subin = Recurse.class), out = @Range(subin = Object.class))
	protected Object recurse2object(Object recurse, Class<?> out, ClayCastPosition position) {
		Object object = position.parents.get(recurse);
		if (object == null)
			throw new IllegalArgumentException("Not recurse!:" + StringUtil.logging(recurse));
		if (!out.isInstance(object))
			throw new ClassCastException("The recurse not instance of the targeted class");
		return object;
	}

	/**
	 * Parse the given {@link CharSequence} to the given 'out' class.
	 *
	 * @param sequence to be parsed
	 * @param out      to parse the sequence to
	 * @param position to depend on
	 * @return the given sequence parsed to the given class
	 */
	@CastMethod(in = @Range(subin = CharSequence.class), out = @Range(subin = {
			Object.class,
			boolean.class,
			byte.class,
			char.class,
			double.class,
			int.class,
			float.class,
			long.class,
			short.class,
	}))
	protected Object sequence2object(CharSequence sequence, Class<?> out, ClayCastPosition position) {
		return this.cast(JSON.global.parse(sequence), (Class<? super Object>) out);
	}

	/**
	 * Helps to effect the casting behavior depending on the casting position.
	 */
	public class ClayCastPosition implements CastPosition {
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
		public ClayCastPosition() {
		}

		/**
		 * Construct with the given parameters.
		 *
		 * @param parents        the parents mappings.
		 * @param sourceParent   a source mapping
		 * @param instanceParent a recurse mapping for the source
		 * @throws NullPointerException if 'parents' equals null
		 */
		public ClayCastPosition(Map<Object, Object> parents, Object sourceParent, Object instanceParent) {
			ObjectUtil.requireNonNull(parents, "parents");
			this.parents.putAll(parents);
			this.parents.put(sourceParent, instanceParent);
		}

		/**
		 * Cast the given object to the given 'out' class. Using the first method annotated with Cast.CastMethod. And that annotation allows the given
		 * 'in' and 'out' classes. (methods are ordered randomly).
		 *
		 * @param object         the object to be casted
		 * @param out            the targeted class to cast the object to
		 * @param <T>            the targeted type
		 * @param sourceParent   a source mapping
		 * @param instanceParent a recurse mapping for the source
		 * @return the given object casted to the given 'out' class
		 * @throws ClassCastException       on casting failure
		 * @throws IllegalArgumentException optional. on casting failure
		 */
		public <T> T cast(Object object, Class<? super T> out, Object sourceParent, Object instanceParent) {
			return this.cast(object, out, null, null, false, sourceParent, instanceParent);
		}

		/**
		 * Cast the given object to the given 'out' class. Using the first method annotated with Cast.CastMethod. And that annotation allows the given
		 * 'in' and 'out' classes. (methods are ordered randomly).
		 *
		 * @param object         the object to be casted
		 * @param out            the targeted class to cast the object to
		 * @param position       to format depending on (null to create a new one)
		 * @param in             the targeted method parameter type (null for the class of the given object)
		 * @param clone          true to create a new instance even when the object is instance of the targeted class
		 * @param sourceParent   a source mapping
		 * @param instanceParent a recurse mapping for the source
		 * @param <T>            the targeted type
		 * @return the given object casted to the given 'out' class
		 * @throws ClassCastException       on casting failure
		 * @throws IllegalArgumentException optional. on casting failure
		 * @throws NullPointerException     if the 'out' param equals to null
		 */
		public <T> T cast(Object object, Class<? super T> out, ClayCastPosition position, Class<?> in, boolean clone, Object sourceParent, Object instanceParent) {
			ObjectUtil.requireNonNull(out, "out");

			if (position == null)
				position = new ClayCastPosition(this.parents, sourceParent, instanceParent);
			if (in == null)
				in = this.parents.containsKey(object) ? Recurse.class : null;

			return Cast.this.cast(object, out, position, in, clone);
		}
	}
}
