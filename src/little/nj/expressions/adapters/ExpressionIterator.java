package little.nj.expressions.adapters;

import java.util.Iterator;

/**
 * @param <A> The Input type
 * @param <B> The Output type
 */
public abstract class ExpressionIterator<A, B> implements Iterator<B>, Iterable<B> {
    
    protected abstract Iterator<A> getIterator();
    
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
    
    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<B> iterator() {
        return this;
    }
}
