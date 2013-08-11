package little.nj.expressions.predicates;


public interface IPredicate<T> {
    
    boolean evaluate(T obj);
    
}