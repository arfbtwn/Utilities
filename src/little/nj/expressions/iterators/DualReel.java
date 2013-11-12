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
package little.nj.expressions.iterators;

import java.util.Iterator;

import little.nj.exceptions.NotImplementedException;


public abstract class DualReel<A, B>
    implements ExpressionIterator<A, B> {

    protected final Iterator<A> rhs, lhs;

    public DualReel(Iterator<A> lhs, Iterator<A> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    protected Iterator<A> getCurrentIterator() {
        return lhs.hasNext() ? lhs
                             : rhs.hasNext() ? rhs
                                             : null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return getCurrentIterator() != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#removeListener()
     */
    @Override
    public final void remove() {
        throw new NotImplementedException();
    }
}
