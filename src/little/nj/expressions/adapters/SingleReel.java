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
package little.nj.expressions.adapters;

import java.util.Iterator;

import little.nj.exceptions.NotImplementedException;


public abstract class SingleReel<A, B> implements IExpressionIterator<A, B> {

    protected final Iterable<A> iterable;
    private Iterator<A> iterator;
    
    public SingleReel(Iterable<A> iterable) {
        this.iterable = iterable;
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.adapters.ExpressionIterator#getIterator()
     */
    protected Iterator<A> getIterator() {
        return iterator == null ? iterator = iterable.iterator() : iterator;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return getIterator().hasNext();
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new NotImplementedException();
    }
}
