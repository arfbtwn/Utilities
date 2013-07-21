package little.nj.expressions.adapters;

import java.util.Iterator;

import little.nj.expressions.IExpression;


/**
 * A transform iterator
 *
 * @param <X>
 * @param <Y>
 */
public class SelectIterator<X, Y> extends ExpressionIterator<X, Y> {

    protected final IExpression<Y, X> expression;
    
    public SelectIterator(Iterator<? extends X> wrapped, IExpression<Y, X> expression) {
        super(wrapped);
        
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