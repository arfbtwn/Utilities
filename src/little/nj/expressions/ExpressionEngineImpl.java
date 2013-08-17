/**
 * Copyright (C) 2013 Nicholas J. Little <arealityfarbetween@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import little.nj.expressions.collections.MinusCollection;
import little.nj.expressions.collections.PredicateCollection;
import little.nj.expressions.collections.SelectCollection;
import little.nj.expressions.collections.UnionCollection;
import little.nj.expressions.predicates.Predicate;

public class ExpressionEngineImpl<T> implements ExpressionEngine<T> {

    protected final Iterable<T> backing;

    public ExpressionEngineImpl(Iterable<T> backing) {
        this.backing = backing;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return backing.iterator();
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#contains(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public boolean contains(Predicate<? super T> predicate) {
        return first(predicate) != null;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#count()
     */
    @Override
    public int count() {
        int rv = 0;
        for (Iterator<T> it = iterator(); it.hasNext(); ++rv, it.next())
            ;
        return rv;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#count(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public int count(Predicate<? super T> predicate) {
        int rv = 0;
        for (Iterator<T> it = new PredicateCollection<>(backing, predicate)
                .iterator(); it.hasNext(); ++rv, it.next())
            ;
        return rv;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#first()
     */
    @Override
    public T first() {
        Iterator<T> it = iterator();
        return it.hasNext() ? it.next() : null;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#first(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public T first(Predicate<? super T> predicate) {
        Iterator<T> it = new PredicateCollection<>(backing, predicate)
                .iterator();

        if (it.hasNext())
            return it.next();

        return null;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#last()
     */
    @Override
    public T last() {
        Iterator<T> it = iterator();

        T rv = null;

        while (it.hasNext())
            rv = it.next();

        return rv;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#last(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public T last(Predicate<? super T> predicate) {
        T rv = null;

        Iterator<T> it = new PredicateCollection<>(backing, predicate)
                .iterator();

        while (it.hasNext())
            rv = it.next();

        return rv;
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#where(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public ExpressionEngine<T> where(Predicate<? super T> predicate) {
        return new ExpressionEngineImpl<>(new PredicateCollection<>(backing,
                predicate));
    }

    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#select(little.nj.expressions.IExpression)
     */
    @Override
    public <E> ExpressionEngine<E> select(Expression<T, E> expression) {
        return new ExpressionEngineImpl<>(new SelectCollection<>(backing,
                expression));
    }
    
    /*
     * (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#union(java.lang.Iterable)
     */
    @Override
    public ExpressionEngine<T> union(Iterable<T> union) {
        return new ExpressionEngineImpl<>(new UnionCollection<>(backing, union));
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.ExpressionEngine#minus(java.lang.Iterable)
     */
    @Override
    public ExpressionEngine<T> minus(Iterable<T> minus) {
        return new ExpressionEngineImpl<>(new MinusCollection<>(backing, minus));
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.ExpressionEngine#intersect(java.lang.Iterable)
     */
    @Override
    public ExpressionEngine<T> intersect(Iterable<T> intersect) {
        
        UnionCollection<T> union = new UnionCollection<>(backing, intersect);
        
        MinusCollection<T> minusAB = new MinusCollection<>(backing, intersect);
        
        MinusCollection<T> minusBA = new MinusCollection<>(intersect, backing);
        
        UnionCollection<T> unionMinus = new UnionCollection<>(minusAB, minusBA);
        
        return new ExpressionEngineImpl<>(new MinusCollection<>(union, unionMinus));
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.IExpressionEngine#toList()
     */
    @Override
    public List<T> toList() {
        List<T> rv = new ArrayList<>();
        Iterator<T> it = iterator();
        
        while(it.hasNext())
            rv.add(it.next());
        
        return rv;
    }
}
