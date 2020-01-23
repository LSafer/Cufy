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
import cufy.lang.Type;
import cufy.text.Format;
import cufy.text.FormatException;
import cufy.util.Reader$;

import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Format} for {@link Serializable}s using {@link Base64}.
 *
 * @author LSaferSE
 * @version 6 release (23-Jan-2020)
 * @see Base64
 * @see Serializable
 * @since 02-Nov-2019
 */
public class SERB64 extends Format implements Global {
	/**
	 * The global instance to avoid unnecessary instancing.
	 */
	final public static SERB64 global = new SERB64();

	/**
	 * The expected length of the values.
	 */
	protected int DEFAULT_VALUE_LENGTH;

	{
		DEBUGGING = false;
		DEFAULT_VALUE_LENGTH = 100;
	}

	/**
	 * Serialize the given {@link Serializable} using base64. Then {@link Writer#append} it to the given {@link Writer}.
	 *
	 * @param serializable to be serialized
	 * @param writer       to append the serial to
	 * @throws IllegalArgumentException when unable to serialize the given object
	 * @throws FormatException          when any formatting errors occurs
	 * @throws IOException              if any I/O exception occurs
	 * @throws NullPointerException     if any of the given parameters is null
	 */
	@FormatMethod(value = @Type(subin = {
			Serializable.class,
			Void.class,
			boolean.class,
			byte.class,
			char.class,
			double.class,
			float.class,
			int.class,
			long.class,
			short.class,
	}))
	protected void formatSerializable(Writer writer, Serializable serializable) throws IOException {
		if (DEBUGGING) {
			Objects.requireNonNull(serializable, "serializable");
			Objects.requireNonNull(writer, "writer");
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(serializable);
		String string = Base64.getEncoder().encodeToString(baos.toByteArray());
		writer.write(string);

		baos.close();
		oos.close();
	}

	/**
	 * returns true.
	 *
	 * @return true
	 */
	@ClassifyMethod(Serializable.class)
	protected boolean isSerializable() {
		return true;
	}

	/**
	 * Parse the string from the given reader (written on base64) to a serializable object. Then set it to the given {@link AtomicReference buffer}.
	 *
	 * @param reader to read from
	 * @param buffer to set the parsed object to
	 * @throws FormatException        when any parsing error occurs
	 * @throws IOException            if any I/O exception occurs
	 * @throws ClassNotFoundException class of a serialized object cannot be found.
	 */
	@ParseMethod(@Type(Serializable.class))
	protected void parseSerializable(AtomicReference<Serializable> buffer, Reader reader) throws IOException, ClassNotFoundException {
		if (DEBUGGING) {
			Objects.requireNonNull(buffer, "buffer");
			Objects.requireNonNull(reader, "reader");
		}

		String string = Reader$.getRemaining(reader, DEFAULT_VALUE_LENGTH, DEFAULT_VALUE_LENGTH);
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(string));
		ObjectInputStream ois = new ObjectInputStream(bais);

		buffer.set((Serializable) ois.readObject());

		bais.close();
		ois.close();
	}
}
