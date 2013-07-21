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

public class NotPredicate<T> extends UnaryPredicate<T> {

    public NotPredicate(IPredicateFactory<T> factory, IPredicate<T> predicate) {
        super(factory, predicate);
    }
    
    public NotPredicate(IPredicate<T> predicate) {
        super(predicate);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpression#evaluate(java.lang.Object)
     */
    @Override
    public Boolean evaluate(T obj) {
        return !unbox(predicate.evaluate(obj));
    }
}