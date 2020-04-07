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
package cufy.text;

import cufy.meta.MetaFamily;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Navigate the {@link AbstractFormat} class that the annotated method is a parsing method.
 *
 * @author LSaferSE
 * @version 1 release (30-Mar-2020)
 * @apiNote the annotated method SHOULD match the {@link AbstractFormat#parse0} rules
 * @since 30-Mar-2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ParseMethod {
	/**
	 * Classes that the annotated method returns.
	 *
	 * @return the classes that the annotated method returns
	 */
	MetaFamily value() default @MetaFamily;
}
