package little.nj.expressions.predicates;

import little.nj.expressions.IExpression;

public interface IPredicate<T> extends IExpression<Boolean, T> {

    IPredicate<T> and(IPredicate<T> rhs);
    
    IPredicate<T> or(IPredicate<T> rhs);
    
    IPredicate<T> xor(IPredicate<T> rhs);
    
    IPredicate<T> not();
    
}