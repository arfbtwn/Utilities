package little.nj.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;

import little.nj.expressions.predicates.Predicate;
import little.nj.expressions.predicates.IPredicate;

public class MethodMatcherFactory 
    implements IMethodMatcherFactory {
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getNameMatcher(java.lang.String)
     */
    @Override
    public IPredicate<Method> getNameMatcher(String pattern) {
        return new NameMatcher(pattern);
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getArgumentMatcher(java.lang.Class)
     */
    @Override
    public IPredicate<Method> getArgumentMatcher(Class<?>...clz) {
        return new ArgumentMatcher(clz);
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getSignatureMatcher(java.lang.String, java.lang.Class)
     */
    @Override
    public IPredicate<Method> getSignatureMatcher(String pattern, Class<?>...clz) {
        return new NameMatcher(pattern).and(new ArgumentMatcher(clz));
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getReturnMatcher(java.lang.Class)
     */
    @Override
    public IPredicate<Method> getReturnMatcher(Class<?> clz) {
        return new ReturnMatcher(clz);
    }
    
    public abstract class MethodMatcher
        extends Predicate<Method> { }
    
    /**
     * Matches method names against a regular expression
     */
    class NameMatcher extends MethodMatcher {
        
        final String pattern;
        
        NameMatcher(String pattern) {
            this.pattern = pattern;
        }
        
        /* (non-Javadoc)
         * @see little.nj.util.IMatcher#matches(java.lang.Object)
         */
        @Override
        public Boolean evaluateImpl(Method m) {
            return m.getName().matches(pattern);
        }
    }
    
    /**
     * Matches on parameter types
     */
    class ArgumentMatcher extends MethodMatcher {
        final Class<?>[] args;
        
        ArgumentMatcher(Class<?>...args) {
            this.args = args;
        }
        
        /* (non-Javadoc)
         * @see little.nj.util.IMatcher#matches(java.lang.Object)
         */
        @Override
        public Boolean evaluateImpl(Method m) {
            return Arrays.equals(m.getParameterTypes(), args);
        }
    }
    
    /**
     * Matches based on return type
     */
    class ReturnMatcher extends MethodMatcher {

        final Class<?> rv;
        
        ReturnMatcher(Class<?> rv) {
            this.rv = rv;
        }
        
        /* (non-Javadoc)
         * @see little.nj.util.IMatcher#matches(java.lang.Object)
         */
        @Override
        public Boolean evaluateImpl(Method obj) {
            return rv.equals(obj.getReturnType());
        }   
    }
}