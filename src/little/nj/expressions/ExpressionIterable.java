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
package little.nj.expressions;

import java.util.Collection;
import java.util.Iterator;

import little.nj.expressions.adapters.PredicateIterator;
import little.nj.expressions.adapters.SelectIterator;
import little.nj.expressions.adapters.UnionIterator;
import little.nj.expressions.predicates.IPredicate;

/**
 * @author Nicholas Little
 *
 */
public class ExpressionIterable<T> 
    implements IExpressionIterable<T> {
    
    private final Iterator<T> iterator;
    
    public ExpressionIterable(Iterator<T> iterator) {
        this.iterator = iterator;
    }
    
    public ExpressionIterable(Collection<T> backing) {
        this(backing.iterator());
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionCollection#first(little.nj.expressions.IPredicate)
     */
    @Override
    public T first(IPredicate<T> predicate) {
        Iterator<T> it = new PredicateIterator<>(iterator, predicate);
        
        if (it.hasNext())
            return it.next();
        
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionExtension#last(little.nj.expressions.IExpression)
     */
    @Override
    public T last(IPredicate<T> predicate) {
        T rv = null;
        
        Iterator<T> it = new PredicateIterator<>(iterator, predicate);
        
        while(it.hasNext())
            rv = it.next();
        
        return rv;
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionCollection#where(little.nj.expressions.IPredicate)
     */
    @Override
    public IExpressionIterable<T> where(IPredicate<T> predicate) {
        return new ExpressionIterable<>(new PredicateIterator<>(iterator, predicate));
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionCollection#select(little.nj.expressions.IExpression)
     */
    @Override
    public <E> IExpressionIterable<E> select(IExpression<E, T> expression) {
        return new ExpressionIterable<>(new SelectIterator<>(iterator, expression));
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionExtension#count(little.nj.expressions.IPredicate)
     */
    @Override
    public int count(IPredicate<T> predicate) {
        int rv = 0;
        for(Iterator<T> it = new PredicateIterator<>(iterator, predicate);
                it.hasNext(); ++rv, it.next());
        return rv;
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionExtension#contains(little.nj.expressions.IPredicate)
     */
    @Override
    public boolean contains(IPredicate<T> predicate) {
        return first(predicate) != null;
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionProcess#union(java.lang.Iterable)
     */
    @Override
    public IExpressionIterable<T> union(Iterable<T> union) {
        return new ExpressionIterable<>(
                new UnionIterator<>(iterator, union.iterator()));
    }
}