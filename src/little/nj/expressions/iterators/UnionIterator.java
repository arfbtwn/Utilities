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

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


public class UnionIterator<T> extends DualReel<T, T>{

    private final Set<T> set;
    private T next;    
    
    public UnionIterator(Iterator<T> lhs, Iterator<T> rhs) {
        super(lhs, rhs);
        
        set = new HashSet<>();
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.iterators.DualReel#hasNext()
     */
    @Override
    public boolean hasNext() {
        
        if (next == null && super.hasNext()) {
            next = getCurrentIterator().next();
        
            if (!set.add(next)) {
                next = null;
                return hasNext();
            }
        }
        
        return next != null;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        
        T that = next;
        
        next = null;
        
        return that;
    }
}
