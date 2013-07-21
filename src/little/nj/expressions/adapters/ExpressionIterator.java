package little.nj.expressions.adapters;

import java.util.Iterator;

/**
 * @param <A> The Input type
 * @param <B> The Output type
 */
public abstract class ExpressionIterator<A, B> implements Iterator<B> {

    protected Iterator<? extends A> wrapped;
    
    protected Iterator<? extends A> getIterator() {
        return wrapped;
    }
    
    protected ExpressionIterator(Iterator<? extends A> wrapped) {
        this.wrapped = wrapped;
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return getIterator().hasNext();
    }
    
    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    @Override
    public void remove() {
        getIterator().remove();
    }
}
