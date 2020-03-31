/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.lang;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines that the annotated element isn't a dynamic element. (shouldn't be touched by reflection).
 *
 * @author LSaferSE
 * @version 2 release (20-Mar-2020)
 * @since 14-Jan-2020
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Static {
}
