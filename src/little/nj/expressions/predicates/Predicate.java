package little.nj.expressions.predicates;


public interface Predicate<T> {
    
    boolean evaluate(T obj);
    
}