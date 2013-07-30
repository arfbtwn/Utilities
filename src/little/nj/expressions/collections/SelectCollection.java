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
package little.nj.expressions.collections;

import java.util.Iterator;

import little.nj.expressions.IExpression;
import little.nj.expressions.iterators.SelectIterator;


/**
 * @author Nicholas Little
 * @param <X>
 *
 */
public class SelectCollection<A, B> implements Iterable<B> {

    private final Iterable<A> backing;
    private final IExpression<B, A> expression;
    
    public SelectCollection(Iterable<A> backing, IExpression<B, A> expression) {
        this.backing = backing;
        this.expression = expression;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<B> iterator() {
        return new SelectIterator<>(backing.iterator(), expression);
    }

}
