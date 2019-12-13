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
package org.cufy.text;

import cufy.lang.Global;
import cufy.lang.Range;
import cufy.lang.Recurse;
import cufy.text.Format;
import cufy.text.FormatException;
import cufy.text.ParseException;
import cufy.util.CollectionUtil;
import cufy.util.ObjectUtil;
import cufy.util.ReaderUtil;
import cufy.util.StringUtil;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Format} for JSON.
 *
 * <ul>
 *     <font color="orange" size="4"><b>Dynamic Methods:</b></font>
 *     <li>
 *         <font color="yellow">{@link Collection Array}</font>
 *         <ul>
 *             	<li>{@link #formatArray}</li>
 *         		<li>{@link #isArray}</li>
 *         		<li>{@link #parseArray}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Boolean}</font>
 *     		<ul>
 *     		 	<li>{@link #formatBoolean}</li>
 *     			<li>{@link #isBoolean}</li>
 *     			<li>{@link #parseBoolean}</li>
 *     		</ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Number}</font>
 *         <ul>
 *             	<li>{@link #formatNumber}</li>
 *             	<li>{@link #isNumber}</li>
 *             	<li>{@link #parseNumber}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Map Object}</font>
 *         <ul>
 *            	<li>{@link #formatObject}</li>
 *            	<li>{@link #isObject}</li>
 *            	<li>{@link #parseObject}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Recurse}</font>
 *         <ul>
 *             <li>{@link #formatRecurse}</li>
 *             <li>{@link #isRecursive}</li>
 *             <li>{@link #parseRecurse}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link CharSequence String}</font>
 *         <ul>
 *             	<li>{@link #formatString}</li>
 *             	<li>{@link #isString}</li>
 *             	<li>{@link #parseString}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link Object Else}</font>
 *         <ul>
 *             <li>{@link #formatElse}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link #parseNull Null}</font>
 *         <ul>
 *         		<li>{@link #formatNull}</li>
 *         		<li>{@link #isNull}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author LSaferSE
 * @version 9 release (13-Dec-2019)
 * @apiNote for more about JSON please visit
 * @see <a href="http://www.json.org/">json.org</a>
 * @since 09-Jul-19
 */
public class JSON extends Format implements Global {
	/**
	 * The global instance to avoid unnecessary instancing.
	 */
	final public static JSON global = new JSON();

	@Override
	@StaticMethod
	protected void formatElse(Object object, Writer writer, Format.FormatPosition position) throws IOException {
		this.formatString(object.toString(), writer, (JSONFormatPosition) position);
	}

	@Override
	@StaticMethod
	protected void formatNull(Writer writer, Format.FormatPosition position) throws IOException {
		writer.append(val.NULL);
	}

	@Override
	@StaticMethod
	protected boolean isNull(Reader reader, Format.ParsePosition position) throws IOException {
		return ReaderUtil.equals(reader, true, true, false, val.NULL);
	}

	@Override
	@StaticMethod
	protected JSONFormatPosition newFormatPosition() {
		return new JSONFormatPosition();
	}

	@Override
	@StaticMethod
	protected JSONParsePosition newParsePosition() {
		return new JSONParsePosition();
	}

	/**
	 * Format the given {@link Collection Array}. To a {@link JSON} text. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param array    to be formatted
	 * @param writer   to append to
	 * @param position to format depending on
	 * @throws FormatException when any formatting errors occurs
	 * @throws IOException     when any I/O exception occurs
	 */
	@FormatMethod(in = @Range(subin = {
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
	protected void formatArray(Object array, Writer writer, JSONFormatPosition position) throws IOException {
		Iterator<?> iterator = CollectionUtil.from(array).iterator();

		if (!iterator.hasNext()) {
			writer.append(symbol.ARRAY_START)
					.append("\n")
					.append(position.tab)
					.append(symbol.ARRAY_END);
		} else {
			writer.append(symbol.ARRAY_START)
					.append("\n")
					.append(position.shift);

			while (true) {
				position.format(iterator.next(), writer, null, null, array, null, null);

				if (!iterator.hasNext()) {
					writer.append("\n")
							.append(position.tab)
							.append(symbol.ARRAY_END);
					return;
				}

				writer.append(symbol.MEMBER_END)
						.append("\n")
						.append(position.shift);
			}
		}
	}

	/**
	 * Format the given {@link Boolean}. To a {@link JSON} text. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param aBoolean to be formatted
	 * @param writer   to append to
	 * @param position to format depending on
	 * @throws FormatException when any formatting errors occurs
	 * @throws IOException     when any I/O exception occurs
	 */
	@FormatMethod(in = @Range(in = Boolean.class))
	protected void formatBoolean(Boolean aBoolean, Writer writer, JSONFormatPosition position) throws IOException {
		writer.append(aBoolean ? val.TRUE : val.FALSE);
	}

	/**
	 * Format the given {@link Number}. To a {@link JSON} text. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param number   to be formatted
	 * @param writer   to append to
	 * @param position to format depending on
	 * @throws FormatException when any formatting errors occurs
	 * @throws IOException     when any I/O exception occurs
	 */
	@FormatMethod(in = @Range(subin = {
			Number.class,
			double.class,
			float.class,
			long.class,
			short.class
	}))
	protected void formatNumber(Number number, Writer writer, JSONFormatPosition position) throws IOException {
		writer.append(NumberFormat.getInstance(Locale.ENGLISH).format(number));
	}

	/**
	 * Format the given {@link Map Object}. To a {@link JSON} text. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param object   to be formatted
	 * @param writer   to append to
	 * @param position to format depending on
	 * @throws FormatException when any formatting errors occurs
	 * @throws IOException     when any I/O exception occurs
	 */
	@FormatMethod(in = @Range(subin = Map.class))
	protected void formatObject(Map<?, ?> object, Writer writer, JSONFormatPosition position) throws IOException {
		Iterator<? extends Map.Entry<?, ?>> iterator = object.entrySet().iterator();

		if (!iterator.hasNext()) {
			writer.append(symbol.OBJECT_START)
					.append("\n")
					.append(position.tab)
					.append(symbol.OBJECT_END);
		} else {
			writer.append(symbol.OBJECT_START)
					.append("\n")
					.append(position.shift);

			while (true) {
				Map.Entry<?, ?> entry = iterator.next();

				position.format(entry.getKey(), writer, null, null, object, null, null);
				writer.append(symbol.DECLARATION);
				position.format(entry.getValue(), writer, null, null, object, null, null);

				if (!iterator.hasNext()) {
					writer.append("\n")
							.append(position.tab)
							.append(symbol.OBJECT_END);
					return;
				}

				writer.append(symbol.MEMBER_END)
						.append("\n")
						.append(position.shift);
			}
		}
	}

	/**
	 * Format the given {@link Recurse}. To a {@link JSON} text. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param recurse  to be formatted
	 * @param writer   to append to
	 * @param position to format depending on
	 * @throws FormatException when any formatting errors occurs
	 * @throws IOException     when any I/O exception occurs
	 */
	@FormatMethod(in = @Range(in = Recurse.class))
	protected void formatRecurse(Object recurse, Writer writer, JSONFormatPosition position) throws IOException {
		int index = position.parents.indexOf(recurse);
		if (index == -1)
			throw new IllegalArgumentException("Not recurse!: " + StringUtil.logging(recurse));
		writer.append(val.RECURSE)
				.append(String.valueOf(position.parents.size() - 1 - index));
	}

	/**
	 * Format the given {@link CharSequence String}. To a {@link JSON} text. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param string   to be formatted
	 * @param writer   to append to
	 * @param position to format depending on
	 * @throws FormatException when any formatting errors occurs
	 * @throws IOException     when any I/O exception occurs
	 */
	@FormatMethod(in = @Range(subin = CharSequence.class))
	protected void formatString(CharSequence string, Writer writer, JSONFormatPosition position) throws IOException {
		writer.append(symbol.STRING_START)
				.append(string.toString()
						.replace("\"", "\\\"")
						.replace("\\", "\\\\")
						.replace("/", "\\/")
						.replace("\b", "\\\b")
						.replace("\f", "\\f")
						.replace("\n", "\\n")
						.replace("\r", "\\r")
						.replace("\t", "\\t"))
				.append(symbol.STRING_END);
	}

	/**
	 * Check if the given string should be parsed as {@link Collection Array} or not.
	 *
	 * @param reader   to read from
	 * @param position to check depending on
	 * @return whether the given string should be parsed as {@code array} or not.
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ClassifyMethod(Collection.class)
	protected boolean isArray(Reader reader, JSONParsePosition position) throws IOException {
		return ReaderUtil.equals(reader, true, false, false, String.valueOf(symbol.ARRAY_START));
	}

	/**
	 * Check if the given string should be parsed as {@link Boolean} or not.
	 *
	 * @param reader   to read from
	 * @param position to check depending on
	 * @return whether the given string should be parsed as {@code boolean} or not.
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ClassifyMethod(Boolean.class)
	protected boolean isBoolean(Reader reader, JSONParsePosition position) throws IOException {
		return ReaderUtil.equals(reader, true, false, false, val.TRUE, val.FALSE);
	}

	/**
	 * Check if the given string should be parsed as {@link Number} or not.
	 *
	 * @param reader   to read from
	 * @param position to check depending on
	 * @return whether the given string should be parsed as {@code number} or not.
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ClassifyMethod(Number.class)
	protected boolean isNumber(Reader reader, JSONParsePosition position) throws IOException {
		ReaderUtil.skip(reader, Character::isWhitespace);
		return Character.isDigit(reader.read());
	}

	/**
	 * Check if the given string should be parsed as {@link Map Object} or not.
	 *
	 * @param reader   to read from
	 * @param position to check depending on
	 * @return whether the given string should be parsed as {@code object} or not.
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ClassifyMethod(Map.class)
	protected boolean isObject(Reader reader, JSONParsePosition position) throws IOException {
		return ReaderUtil.equals(reader, true, false, false, String.valueOf(symbol.OBJECT_START));
	}

	/**
	 * Check if the given string should be parsed as {@link Recurse} or not.
	 *
	 * @param reader   to read from
	 * @param position to check depending on
	 * @return whether the given string should be parsed as {@code recurse} or not.
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ClassifyMethod(Recurse.class)
	protected boolean isRecursive(Reader reader, JSONParsePosition position) throws IOException {
		return ReaderUtil.equals(reader, true, false, false, val.RECURSE);
	}

	/**
	 * Check if the given string should be parsed as {@link CharSequence String} or not.
	 *
	 * @param reader   to read from
	 * @param position to check depending on
	 * @return whether the given string should be parsed as {@code string} or not.
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ClassifyMethod(CharSequence.class)
	protected boolean isString(Reader reader, JSONParsePosition position) throws IOException {
		return ReaderUtil.equals(reader, true, false, false, String.valueOf(symbol.STRING_START));
	}

	/**
	 * Parse the string from the given reader to an {@link Collection Array}. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the string depending on
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ParseMethod(out = @Range(subin = Collection.class))
	protected void parseArray(Reader reader, AtomicReference<Collection<Object>> buffer, JSONParsePosition position) throws IOException {
		buffer.set(new ArrayList<>(10));
		WrapTracker tracker = new WrapTracker();
		StringBuilder builder = new StringBuilder(50);

		//skip '['
		reader.skip(1);

		int i;
		while ((i = reader.read()) > -1) {
			char point = (char) i;
			if (tracker == null)
				throw new ParseException("Collection closed before text end");
			if (tracker.length() == 0)
				switch (point) {
					case symbol.ARRAY_END:
						tracker = null;
					case symbol.MEMBER_END:
						buffer.get().add(position.parse(builder, buffer));
						builder = new StringBuilder(50);
						continue;
				}

			builder.append(point);
			tracker.append(point);
		}

		if (tracker != null)
			throw new ParseException("Collection not closed");
	}

	/**
	 * Parse the string from the given reader to an {@link Boolean}. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the string depending on
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ParseMethod(out = @Range(in = Boolean.class))
	protected void parseBoolean(Reader reader, AtomicReference<Boolean> buffer, JSONParsePosition position) throws IOException {
		String string = ReaderUtil.read(reader).trim();
		Boolean value = string.equals(val.TRUE) ? (Boolean) true : string.equals(val.FALSE) ? false : null;
		buffer.set(value);
	}

	/**
	 * Parse the string from the given reader to an {@link Number}. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the string depending on
	 * @throws ParseException           when any parsing exception occurs
	 * @throws IOException              when any I/O exception occurs
	 * @throws java.text.ParseException if the number on the string can't be parsed
	 */
	@ParseMethod(out = @Range(subin = Number.class))
	protected void parseNumber(Reader reader, AtomicReference<Number> buffer, JSONParsePosition position) throws IOException, java.text.ParseException {
		String string = ReaderUtil.read(reader).trim();
		Number value = NumberFormat.getInstance(Locale.ENGLISH).parse(string);
		buffer.set(value);
	}

	/**
	 * Parse the string from the given reader to an {@link Map Object}. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the string depending on
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ParseMethod(out = @Range(subin = Map.class))
	protected void parseObject(Reader reader, AtomicReference<Map<Object, Object>> buffer, JSONParsePosition position) throws IOException {
		buffer.set(new HashMap<>(10));
		WrapTracker tracker = new WrapTracker();
		StringBuilder builder = new StringBuilder(50), key = null;

		//skip '{'
		reader.skip(1);

		int i;
		while ((i = reader.read()) > -1) {
			char point = (char) i;
			if (tracker == null)
				throw new ParseException("Map closed before text end");
			if (tracker.length() == 0)
				switch (point) {
					case symbol.DECLARATION:
					case symbol.EQUATION:
						if (key != null)
							throw new ParseException("Two equation symbol");
						key = builder;
						builder = new StringBuilder(50);
						continue;
					case symbol.OBJECT_END:
						tracker = null;
					case symbol.MEMBER_END:
						if (key == null)
							throw new ParseException("No equation symbol");
						Object ok = position.parse(key, buffer);
						Object ov = position.parse(builder, buffer);
						buffer.get().put(ok, ov);
						key = null;
						builder = new StringBuilder(50);
						continue;
				}

			tracker.append(point);
			builder.append(point);
		}

		if (tracker != null)
			throw new ParseException("Map not closed");
	}

	/**
	 * Parse the string from the given reader to an {@link Recurse}. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the string depending on
	 * @throws ParseException           when any parsing exception occurs
	 * @throws IOException              when any I/O exception occurs
	 * @throws java.text.ParseException when any parsing exception occurs
	 */
	@ParseMethod(out = @Range(in = Recurse.class))
	protected void parseRecurse(Reader reader, AtomicReference<Object> buffer, JSONParsePosition position) throws IOException, java.text.ParseException {
		String string = ReaderUtil.read(reader).trim();
		String indexString = string.replaceFirst(val.RECURSE, "");
		Number index = NumberFormat.getInstance(Locale.ENGLISH).parse(indexString);
		Object value = position.parents.get(position.parents.size() - 1 - index.intValue());
		buffer.set(value);
	}

	/**
	 * Parse the string from the given reader to an {@link String}. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the string depending on
	 * @throws ParseException when any parsing exception occurs
	 * @throws IOException    when any I/O exception occurs
	 */
	@ParseMethod(out = @Range(subin = CharSequence.class))
	protected void parseString(Reader reader, AtomicReference<String> buffer, JSONParsePosition position) throws IOException {
		String string = ReaderUtil.read(reader).trim();
		String value = string.substring(1, string.length() - 1)
				.replace("\\\\", "\\")
				.replace("\\\"", "\"")
				.replace("\\/", "/")
				.replace("\\b", "\b")
				.replace("\\f", "\f")
				.replace("\\n", "\n")
				.replace("\\r", "\r")
				.replace("\\t", "\t");
		buffer.set(value);
	}

	/**
	 * JSON symbols.
	 */
	final public static class symbol {
		/**
		 * Array end char on JSON.
		 */
		final public static char ARRAY_END = ']';
		/**
		 * Array start char on JSON.
		 */
		final public static char ARRAY_START = '[';
		/**
		 * Pair mapping char on JSON.
		 */
		final public static char DECLARATION = ':';
		/**
		 * Pair equation char on other JSON like formats.
		 */
		final public static char EQUATION = '=';
		/**
		 * Member end char on JSON.
		 */
		final public static char MEMBER_END = ',';
		/**
		 * Object end char on JSON.
		 */
		final public static char OBJECT_END = '}';
		/**
		 * Object start char on JSON.
		 */
		final public static char OBJECT_START = '{';
		/**
		 * String end char on JSON.
		 */
		final public static char STRING_END = '"';
		/**
		 * String start char on JSON.
		 */
		final public static char STRING_START = '"';
	}

	/**
	 * JSON constant values.
	 */
	final public static class val {
		/**
		 * The value false of the type boolean on JSON.
		 */
		final public static String FALSE = "false";
		/**
		 * No value.
		 */
		final public static String NON = "";
		/**
		 * The value null on JSON.
		 */
		final public static String NULL = "null";
		/**
		 * Recurse reference on JSON.
		 */
		final public static String RECURSE = "this";
		/**
		 * The value true of the type boolean on JSON.
		 */
		final public static String TRUE = "true";
	}

	/**
	 * Helps to effect the formatting behavior depending on the formatting position.
	 */
	public class JSONFormatPosition implements Format.FormatPosition {
		/**
		 * Current parents on this position.
		 *
		 * @implSpec don't modify it after the constructor!
		 */
		final public ArrayList<Object> parents = new ArrayList<>(10);
		/**
		 * Current spacing from the edge of the text to the start of the shifted value.
		 */
		final public String shift;
		/**
		 * Current spacing from the edge of the text to the start of the value.
		 */
		final public String tab;

		/**
		 * New format position.
		 */
		public JSONFormatPosition() {
			this.shift = "\t";
			this.tab = "";
		}

		/**
		 * New format position with the given params.
		 *
		 * @param parents all parents currently formatting on
		 * @param parent  the direct parent formatting on
		 * @param tab     spacing from the edge of the text to the start of the value
		 * @param shift   spacing from the edge of the text to the start of the shifted value
		 */
		public JSONFormatPosition(ArrayList<Object> parents, Object parent, String tab, String shift) {
			this.parents.addAll(parents);
			this.parents.add(parent);
			this.tab = tab;
			this.shift = shift;
		}

		/**
		 * Format the given object depending on a sub-position of this position using the formatter of this.
		 *
		 * @param object to format
		 * @param parent to trance dejaVus
		 * @return a string from formatting the given object
		 */
		public String format(Object object, Object parent) {
			try {
				Writer writer = new StringWriter();
				this.format(object, writer, null, null, parent, null, null);
				return writer.toString();
			} catch (IOException e) {
				throw new Error(e);
			}
		}

		/**
		 * Format the given object depending on a sub-position of this position using the formatter of this. Then {@link Writer#append} it to the
		 * given {@link Writer}.
		 *
		 * @param object   to format
		 * @param writer   to write the formatted string to
		 * @param position the position to be used (null for a delegate of this)
		 * @param klass    the targeted method parameter type (null for the class of the given object)
		 * @param parent   the direct parent
		 * @param tab      spacing from the edge of the text to the start of the value (null for a plus tab of the tab of this)
		 * @param shift    spacing from the edge of the text to the start of the shifted value (null for a plus shift of the shift of this)
		 * @throws FormatException when any formatting errors occurs
		 * @throws IOException     if any I/O exception occurs
		 */
		public void format(Object object, Writer writer, JSONFormatPosition position, Class<?> klass, Object parent, String tab, String shift) throws IOException {
			ObjectUtil.requireNonNull(writer, "writer");

			String tab1 = ObjectUtil.requireNonNullElse(tab, this.shift);
			String shift1 = ObjectUtil.requireNonNullElseGet(shift, () -> this.shift + "\t");
			position = ObjectUtil.requireNonNullElseGet(position, () -> new JSONFormatPosition(this.parents, parent, tab1, shift1));

			if (klass == null)
				klass = this.parents.contains(object) ? Recurse.class : null;

			JSON.this.format(object, writer, position, klass);
		}
	}

	/**
	 * Helps to effect the parsing behavior depending on the formatting position.
	 */
	public class JSONParsePosition implements Format.ParsePosition {
		/**
		 * Current parents on this position.
		 */
		final public ArrayList<AtomicReference<?>> parents = new ArrayList<>(10);

		/**
		 * New position.
		 */
		public JSONParsePosition() {
		}

		/**
		 * New position with the given params.
		 *
		 * @param parents all parents currently formatting on
		 * @param parent  direct parent
		 */
		public JSONParsePosition(ArrayList<AtomicReference<?>> parents, AtomicReference<?> parent) {
			this.parents.addAll(parents);
			this.parents.add(parent);
		}

		/**
		 * Parse the sequence to an object depending on a sub-position of this position using the formatter of this.
		 *
		 * @param sequence to parse from
		 * @param parent   direct parent
		 * @return the parsed object
		 * @throws ParseException when any parsing error occurs
		 */
		public Object parse(CharSequence sequence, AtomicReference<?> parent) {
			try {
				Reader reader = new StringReader(String.valueOf(sequence));
				AtomicReference<?> buffer = new AtomicReference<>();
				this.parse(reader, buffer, null, null, parent);
				return buffer.get();
			} catch (IOException e) {
				throw new Error(e);
			}
		}

		/**
		 * Parse the string from the given reader depending on a sub-position of this. Then set the parsed object to the given {@link AtomicReference
		 * buffer}.
		 *
		 * @param reader   to read the string from
		 * @param buffer   to store the parsed object to while parsing
		 * @param position to parse the given sequence depending on (null for a delegate of this)
		 * @param klass    the targeted method output type (null for classifying the given sequence dynamically)
		 * @param parent   direct parent
		 * @throws ParseException when any parsing error occurs
		 * @throws IOException    if any I/O exception occurs
		 */
		public void parse(Reader reader, AtomicReference<?> buffer, JSONParsePosition position, Class<?> klass, AtomicReference<?> parent) throws IOException {
			if (position == null)
				position = new JSONParsePosition(this.parents, parent);

			JSON.this.parse(reader, buffer, position, klass);
		}
	}
}
