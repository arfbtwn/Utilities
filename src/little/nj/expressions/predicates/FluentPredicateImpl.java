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

import little.nj.expressions.predicates.finals.AndPredicate;
import little.nj.expressions.predicates.finals.NotPredicate;
import little.nj.expressions.predicates.finals.OrPredicate;
import little.nj.expressions.predicates.finals.XorPredicate;


public abstract class FluentPredicateImpl<T> implements FluentPredicate<T> {
    
    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.FluentPredicate#and(little.nj.expressions.predicates.Predicate)
     */
    @Override
    public final FluentPredicate<T> and(Predicate<? super T> rhs) {
        return new AndPredicate<T>(this, rhs);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.FluentPredicate#or(little.nj.expressions.predicates.Predicate)
     */
    @Override
    public final FluentPredicate<T> or(Predicate<? super T> rhs) {
        return new OrPredicate<T>(this, rhs);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.FluentPredicate#xor(little.nj.expressions.predicates.Predicate)
     */
    @Override
    public final FluentPredicate<T> xor(Predicate<? super T> rhs) {
        return new XorPredicate<T>(this, rhs);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.FluentPredicate#not()
     */
    @Override
    public final FluentPredicate<T> not() {
        return new NotPredicate<T>(this);
    }
}