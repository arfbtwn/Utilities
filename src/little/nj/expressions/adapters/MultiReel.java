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

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


/**
 * @author Nicholas Little
 *
 */
public abstract class MultiReel<A, B> extends ExpressionIterator<A, B> {

    private final Queue<Iterator<A>> iterators;
    
    @SafeVarargs
    public MultiReel(Iterator<A>...iterators) {
        this.iterators = new LinkedList<>(Arrays.asList(iterators));
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.adapters.ExpressionIterator#getIterator()
     */
    @Override
    protected Iterator<A> getIterator() {
        Iterator<A> current = iterators.peek();
        
        if (current == null)
            return null;
        else if (current.hasNext())
            return current;
        else {
            iterators.poll();
            return getIterator();
        }            
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return getIterator() != null;
    }
}
