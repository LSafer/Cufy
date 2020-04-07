/*
 *	Copyright 2020 Cufyorg
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package cufy.meta;

import cufy.lang.Clazz;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

/**
 * A recipe to pass information about a clazz in the annotations environment.
 *
 * @author LSaferSE
 * @version 1 release (31-Mar-2020)
 * @see Clazz
 * @see util#get(MetaClazz)
 * @since 31-Mar-2020
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaClazz {
	/**
	 * The component types of this clazz.
	 *
	 * @return the component types of this clazz
	 * @apiNote unfortunately we can't make it more customizable. Classes will be represented by clazzes with no component types (except arrays)
	 * @see Clazz#componentTypes
	 */
	Class[] componentTypes() default {};
	/**
	 * The family of this clazz. How this clazz should be treated.
	 *
	 * @return the family of this clazz. How this clazz should be treated.
	 * @see Clazz#family
	 */
	Class family() default util.class;
	/**
	 * The class represented by this clazz.
	 *
	 * @return the class represented by this clazz.
	 * @see Clazz#klass
	 */
	Class value();

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
		 * Get a clazz from the given meta-clazz.
		 *
		 * @param klazz the meta-clazz to get a clazz from
		 * @param <T>   the component-type of the returned clazz
		 * @return a clazz represents the same class that the given meta-clazz is representing
		 * @throws NullPointerException if the given 'klazz' is null
		 */
		public static <T> Clazz<T> get(MetaClazz klazz) {
			Objects.requireNonNull(klazz, "klazz");

			Class[] componentTypes = klazz.componentTypes();
			Class family = klazz.family();
			Class<T> klass = klazz.value();

			if (family == util.class)
				family = klass;

			Clazz[] componentTypez = new Clazz[componentTypes.length];
			for (int i = 0; i < componentTypes.length; i++)
				componentTypez[i] = Clazz.of(componentTypes[i]);

			return Clazz.of(family, klass, componentTypez);
		}
	}
}
