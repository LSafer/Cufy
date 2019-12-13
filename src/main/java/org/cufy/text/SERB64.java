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
import cufy.text.Format;
import cufy.text.FormatException;
import cufy.text.ParseException;
import cufy.util.ReaderUtil;

import java.io.*;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Format} for {@link Serializable}s using {@link Base64}.
 *
 * <ul>
 *     <font color="orange" size="4"><b>Dynamic Methods:</b></font>
 *     <li>
 *         <font color="yellow">{@link Serializable}</font>
 *         <ul>
 *             <li>{@link #formatSerializable}</li>
 *             <li>{@link #isSerializable}</li>
 *             <li>{@link #parseSerializable}</li>
 *         </ul>
 *     </li>
 *     <li>
 *         <font color="yellow">{@link #formatNull Null}</font>
 *         <ul>
 *             <li>{@link #formatNull}</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * @author LSaferSE
 * @version 5 release (13-Dec-2019)
 * @see Base64
 * @see Serializable
 * @since 02-Nov-2019
 */
public class SERB64 extends Format implements Global {
	/**
	 * The global instance to avoid unnecessary instancing.
	 */
	final public static SERB64 global = new SERB64();

	@Override
	@StaticMethod
	protected void formatNull(Writer writer, FormatPosition position) throws IOException {
		this.formatSerializable(null, writer, position);
	}

	/**
	 * Serialize the given {@link Serializable} using base64. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param serializable to be serialized
	 * @param writer       to append the serial to
	 * @param position     to serialize depending on
	 * @throws IllegalArgumentException when unable to serialize the given object
	 * @throws FormatException          when any formatting errors occurs
	 * @throws IOException              if any I/O exception occurs
	 */
	@FormatMethod(in = @Range(subin = {
			Serializable.class,
			Serializable[].class,
			boolean.class,
			byte.class,
			char.class,
			double.class,
			float.class,
			long.class,
			short.class,
			boolean[].class,
			byte[].class,
			char[].class,
			double[].class,
			float[].class,
			long[].class,
			short[].class
	}))
	protected void formatSerializable(Serializable serializable, Writer writer, FormatPosition position) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(serializable);
		byte[] bytes = baos.toByteArray();
		String serial = Base64.getEncoder().encodeToString(bytes);
		writer.append(serial);
		baos.close();
		oos.close();
	}

	/**
	 * Check if the string from the given reader is written on {@link Base64} or not.
	 *
	 * @param reader   to read the string from
	 * @param position to classify depending on
	 * @return true
	 * @throws ParseException when any classification error occurs
	 * @apiNote default returns true (just to trigger the super class)
	 */
	@ClassifyMethod(Serializable.class)
	protected boolean isSerializable(Reader reader, ParsePosition position) {
		return true;
	}

	/**
	 * Parse the string from the given reader (written on base64) to a serializable object. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader   to read from
	 * @param buffer   to set the parsed object to
	 * @param position to parse the given sequence depending on
	 * @throws FormatException        when any parsing error occurs
	 * @throws IOException            if any I/O exception occurs
	 * @throws ClassNotFoundException class of a serialized object cannot be found.
	 */
	@ParseMethod(out = @Range(subin = Serializable.class))
	protected void parseSerializable(Reader reader, AtomicReference<Serializable> buffer, ParsePosition position) throws IOException, ClassNotFoundException {
		String string = ReaderUtil.read(reader);
		byte[] bytes = Base64.getDecoder().decode(string);
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ObjectInputStream ois = new ObjectInputStream(bais);
		Serializable value = (Serializable) ois.readObject();
		buffer.set(value);
		bais.close();
		ois.close();
	}
}
