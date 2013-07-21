/**
 * Copyright (C) 2013 
 * Nicholas J. Little <arealityfarbetween@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.expressions.predicates;


public interface IPredicateFactory<T> {

    /**
     * Return a new predicate to represent the result of ANDing
     * the two conditions 
     * 
     * @param lhs
     * @param rhs
     * @return
     */
    IPredicate<T> and(IPredicate<T> lhs, IPredicate<T> rhs);
    
    /**
     * Gets a new predicate to represent the result of ORing
     * the two conditions
     * 
     * @param lhs
     * @param rhs
     * @return
     */
    IPredicate<T> or(IPredicate<T> lhs, IPredicate<T> rhs);
    
    /**
     * Gets a new predicate to represent the result of XORing
     * the two conditions
     * 
     * @param lhs
     * @param rhs
     * @return
     */
    IPredicate<T> xor(IPredicate<T> lhs, IPredicate<T> rhs);
    
    /**
     * Gets a new predicate representing a NOT operation on the current
     * predicate
     * 
     * @param predicate
     * @return
     */
    IPredicate<T> not(IPredicate<T> predicate);
    
}
