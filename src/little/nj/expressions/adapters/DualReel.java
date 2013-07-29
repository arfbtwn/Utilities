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


public abstract class DualReel<A, B> implements IExpressionIterator<A, B> {

    private final Iterable<A> rhs, lhs;
    private Iterator<A> it_rhs, it_lhs;
    
    public DualReel(Iterable<A> rhs, Iterable<A> lhs) {
        this.rhs = rhs; this.lhs = lhs;
    }
    
    protected Iterable<A> getLhs() { return rhs; }
    protected Iterable<A> getRhs() { return lhs; }
    
    protected Iterator<A> getLhsIterator() { 
        return it_lhs == null ? it_lhs = lhs.iterator() : it_lhs;
    }
    
    protected Iterator<A> getRhsIterator() { 
        return it_rhs == null ? it_rhs = rhs.iterator() : it_rhs;
    }
    
    protected Iterator<A> getCurrentIterator() {
        return getLhsIterator().hasNext() ? getLhsIterator()
                : getRhsIterator().hasNext() ? getRhsIterator()
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
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        throw new NotImplementedException();
    }
}
