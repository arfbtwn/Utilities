/**
 * Copyright (C) 2013 Nicholas J. Little <arealityfarbetween@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.expressions;

import java.util.List;

import little.nj.expressions.predicates.IPredicate;

public interface IExpressionEngine<T> extends Iterable<T> {

    /**
     * Does this collection contain elements satisfying the predicate
     * 
     * @param predicate
     * @return
     */
    boolean contains(IPredicate<T> predicate);

    /**
     * Count the elements in this iterable
     * 
     * @return
     */
    int count();

    /**
     * Count the elements satisfying the predicate
     * 
     * @param predicate
     * @return
     */
    int count(IPredicate<T> predicate);

    /**
     * Get the first element
     * 
     * @return
     */
    T first();

    /**
     * Get the first element satisfying the predicate
     * 
     * @param predicate
     * @return element, or null if none present
     */
    T first(IPredicate<T> predicate);

    /**
     * Get the last element
     * 
     * @return
     */
    T last();

    /**
     * Get the last element satisfying the predicate
     * 
     * @param predicate
     * @return element, or null if none present
     */
    T last(IPredicate<T> predicate);

    /**
     * Get a deferred iterator, filtering on the predicate
     * 
     * @param predicate
     * @return
     */
    IExpressionEngine<T> where(IPredicate<T> predicate);

    /**
     * Get an iterator on the union
     * 
     * @param union
     * @return
     */
    IExpressionEngine<T> union(Iterable<T> union);

    /**
     * Perform the specified transformation expression on each element in the
     * sequence
     * 
     * @param expression
     * @return
     */
    <E> IExpressionEngine<E> select(IExpression<T, E> expression);

    /**
     * Convert the current query result to a list
     * format
     * 
     * @return
     */
    List<T> toList();
    
}
