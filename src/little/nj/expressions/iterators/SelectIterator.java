package little.nj.expressions.iterators;

import java.util.Iterator;

import little.nj.expressions.IExpression;


public class SelectIterator<X, Y> extends SingleReel<X, Y> {

    protected final IExpression<X, Y> expression;
    
    public SelectIterator(Iterator<X> iterator, IExpression<X, Y> expression) {
        super(iterator);
        
        this.expression = expression;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    @Override
    public Y next() {
        X next = getIterator().next();
        
        return expression.evaluate(next);
    }
}