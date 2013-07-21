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

public abstract class AbstractPredicate<T> 
    implements IPredicate<T> {

    private final IPredicateFactory<T> factory;
    
    public AbstractPredicate() {
        this(new PredicateFactory<T>());
    }
    
    public AbstractPredicate(IPredicateFactory<T> factory) {
        this.factory = factory;
    }
    
    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.IComposingPredicate#and(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public IPredicate<T> and(IPredicate<T> rhs) {
        return factory.and(this, rhs);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.IComposingPredicate#or(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public IPredicate<T> or(IPredicate<T> rhs) {
        return factory.or(this, rhs);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.IComposingPredicate#xor(little.nj.expressions.predicates.IPredicate)
     */
    @Override
    public IPredicate<T> xor(IPredicate<T> rhs) {
        return factory.xor(this, rhs);
    }

    /* (non-Javadoc)
     * @see little.nj.expressions.predicates.IComposingPredicate#not()
     */
    @Override
    public IPredicate<T> not() {
        return factory.not(this);
    }
    
    /**
     * For safe unboxing of boolean variables
     * 
     * @param b
     * @return primitive boolean
     */
    protected boolean unbox(Boolean b) {
        return b == null ? false : b;
    }
}