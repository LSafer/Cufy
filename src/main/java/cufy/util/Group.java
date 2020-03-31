/*
 * Copyright (c) 2019, LSafer, All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 * -You can edit this file (except the header).
 * -If you have change anything in this file. You
 *   shall mention that this file has been edited.
 *   By adding a new header (at the bottom of this header)
 *   with the word "Editor" on top of it.
 */
package cufy.util;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * A way to elements and get subgroups of them. This class holds the elements of it as a final elements. And give the user the ability to
 * iterate the elements back. Or get a subgroup of the elements it has got. The subgroup will be saved by it for the next requests for that group.
 *
 * @param <E> the type of the elements this group holds
 * @author LSaferSE
 * @version 3 release (31-Mar-2020)
 * @since 25-Jan-2020
 */
public interface Group<E> extends Collection<E> {
	/**
	 * Returns a subgroup of this group. The subgroup returned has the elements of this that satisfies the given predicate. The given predicate will
	 * be invoked only if the given key has not been resolved previously. Otherwise the results of that previous call will be returned.
	 *
	 * @param key       the key of that group (saves the group for later)
	 * @param predicate the tester to be satisfied
	 * @return a subgroup of this group that has only the elements satisfied the given predicate
	 * @throws NullPointerException if the given 'predicate' is null
	 */
	Group<E> subGroup(Object key, Predicate<E> predicate);
}
