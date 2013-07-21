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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author Nicholas Little
 *
 */
public class UnionIterator<T> extends ExpressionIterator<T, T>{

    private final Iterator<Iterator<T>> iterator;
    
    @SafeVarargs
    public UnionIterator(Iterator<T> begin, Iterator<T>...iterators) {
        super(begin);
        iterator = new ArrayList<>(Arrays.asList(iterators)).iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (getIterator().hasNext())
            return true;
        
        if (iterator.hasNext()) {
            wrapped = iterator.next();
            return hasNext();
        }
        
        return false;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public T next() {
        if (!hasNext())
            throw new NoSuchElementException();
        
        return getIterator().next();
    }
}
