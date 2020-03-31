/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.meta;

import cufy.util.Reflect$;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * A reference to a static field with a specific type. That field should have {@link MetaReference} annotated to it.
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @see util#get(MetaReference)
 * @since 31-Mar-2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MetaReference.Array.class)
public @interface MetaReference {
	/**
	 * The id of that field.
	 *
	 * @return the id of that field
	 */
	String id() default "";
	/**
	 * The class that the targeted field is at.
	 *
	 * @return the class that the targeted field is at
	 */
	Class klass() default util.class;
	/**
	 * The type of that field.
	 *
	 * @return the type of that field
	 */
	Class type() default Object.class;

	/**
	 * Array of meta-reference.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@interface Array {
		/**
		 * The array of the meta-references.
		 *
		 * @return the array of meta-references
		 */
		MetaReference[] value();
	}

	/**
	 * Utilities for this annotation. Since static methods are illegal in annotations.
	 */
	final class util {
		/**
		 * This is a util class. And shall not be instanced as an object.
		 *
		 * @throws AssertionError when called
		 */
		private util() {
			throw new AssertionError("No instance for you!");
		}

		/**
		 * Get the object in the field that the given reference is referring to.
		 *
		 * @param reference that refers to the targeted field
		 * @param <T>       the type of the returned object
		 * @return the object in the targeted field
		 * @throws NullPointerException if the given 'reference' is null
		 * @throws IllegalMetaException if the given reference refereed to a field that does not exist. Or have a limited access. Or have a different
		 *                              type of object stored in it
		 */
		public static <T> T get(MetaReference reference) {
			Objects.requireNonNull(reference, "reference");

			String id = reference.id();
			Class klass = reference.klass();
			Class<T> type = reference.type();

			if (klass == util.class)
				klass = type;

			for (Field field : Reflect$.getAllFields(klass)) {
				if (field.isAnnotationPresent(MetaReference.Array.class))
					for (MetaReference r : field.getAnnotation(MetaReference.Array.class).value()) {
						if (id.equals(r.id()))
							try {
								return type.cast(field.get(null));
							} catch (IllegalAccessException | ClassCastException e) {
								throw new IllegalMetaException(e);
							}
					}
				else if (field.isAnnotationPresent(MetaReference.class))
					if (id.equals(field.getAnnotation(MetaReference.class).id()))
						try {
							return type.cast(field.get(null));
						} catch (IllegalAccessException | ClassCastException e) {
							throw new IllegalMetaException(e);
						}
			}

			throw new IllegalMetaException(reference + " is pointing to an instance that does not exist");
		}
	}
}
