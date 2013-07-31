package little.nj.expressions.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

import little.nj.expressions.predicates.IPredicate;


public class PredicateIterator<T> extends SingleReel<T, T> {
    
    private final IPredicate<T> predicate;
    
    private transient T next;
    
    public PredicateIterator(Iterator<T> iterator, IPredicate<T> predicate) {
        super(iterator);
        
        this.predicate = predicate;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (next == null) { 
            T poss;
            Iterator<T> it = getIterator();
        
            while(it.hasNext()) {
                poss = it.next();
            
                if (protector(predicate.evaluate(poss))) {
                    next = poss;
                    break;
                }
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
    
    /**
     * Protects against null return values from
     * the predicate evaluate() function
     * 
     * @param in
     * @return
     */
    protected boolean protector(Boolean in) {
        return in == null ? false : in;
    }
}