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


public abstract class FluentPredicate<T> implements Predicate<T> {

    /**
     * Returns a new predicate expressing the binary AND operation
     *
     * @param rhs
     * @return
     */
    public final FluentPredicate<T> and(Predicate<? super T> rhs) {
        return new AndPredicate<T>(this, rhs);
    }

    /**
     * Returns a new predicate expressing the binary OR operation
     *
     * @param rhs
     * @return
     */
    public final FluentPredicate<T> or(Predicate<? super T> rhs) {
        return new OrPredicate<T>(this, rhs);
    }

    /**
     * Returns a new predicate expressing the binary XOR operation
     *
     * @param rhs
     * @return
     */
    public final FluentPredicate<T> xor(Predicate<? super T> rhs) {
        return new XorPredicate<T>(this, rhs);
    }

    /**
     * Returns a new predicate expressing the NOT operation
     *
     * @return
     */
    public final FluentPredicate<T> not() {
        return new NotPredicate<T>(this);
    }
}