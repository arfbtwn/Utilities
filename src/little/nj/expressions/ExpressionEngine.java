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

import little.nj.expressions.predicates.Predicate;

public interface ExpressionEngine<T> extends Iterable<T> {

    /**
     * Does this collection contain elements satisfying the predicate
     * 
     * @param predicate
     * @return
     */
    boolean contains(Predicate<? super T> predicate);

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
    int count(Predicate<? super T> predicate);

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
    T first(Predicate<? super T> predicate);

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
    T last(Predicate<? super T> predicate);

    /**
     * Get a deferred iterator, filtering on the predicate
     * 
     * @param predicate
     * @return
     */
    ExpressionEngine<T> where(Predicate<? super T> predicate);

    /**
     * Perform the specified transformation expression on each element in the
     * sequence
     * 
     * @param expression
     * @return
     */
    <E> ExpressionEngine<E> select(Expression<T, E> expression);
    
    /**
     * Get an iterator on the union
     * 
     * @param union
     * @return
     */
    ExpressionEngine<T> union(Iterable<T> union);
    
    /**
     * Get an iterator on the union
     * 
     * @param union
     * @return
     */
    ExpressionEngine<T> minus(Iterable<T> minus);
    
    /**
     * Get an iterator on the union
     * 
     * @param union
     * @return
     */
    ExpressionEngine<T> intersect(Iterable<T> intersect);

    /**
     * Convert the current query result to a list
     * format
     * 
     * @return
     */
    List<T> toList();
    
}
