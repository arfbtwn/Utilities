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

public abstract class BinaryPredicate<T> extends AbstractPredicate<T> {

    protected final IPredicate<T> lhs;
    protected final IPredicate<T> rhs;
    
    public BinaryPredicate(IPredicateFactory<T> factory, IPredicate<T> lhs, IPredicate<T> rhs) {
        super(factory);
        this.lhs = lhs;
        this.rhs = rhs;
    }
    
    public BinaryPredicate(IPredicate<T> lhs, IPredicate<T> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}