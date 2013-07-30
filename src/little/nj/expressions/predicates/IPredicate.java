package little.nj.expressions.predicates;

import little.nj.expressions.IExpression;

public interface IPredicate<T> extends IExpression<T, Boolean> {

    /**
     * Returns a new predicate expressing the binary AND operation
     * 
     * @param rhs
     * @return
     */
    IPredicate<T> and(IPredicate<T> rhs);
    
    /**
     * Returns a new predicate expressing the binary OR operation
     * 
     * @param rhs
     * @return
     */
    IPredicate<T> or(IPredicate<T> rhs);
    
    /**
     * Returns a new predicate expressing the binary XOR operation
     * 
     * @param rhs
     * @return
     */
    IPredicate<T> xor(IPredicate<T> rhs);
    
    /**
     * Returns a new predicate expressing the NOT operation
     * 
     * @return
     */
    IPredicate<T> not();
    
}