package little.nj.reflection;

import java.lang.reflect.Method;
import little.nj.expressions.predicates.FluentPredicateImpl;
import little.nj.expressions.predicates.Predicate;

public class MethodMatcherFactoryImpl 
    implements MethodMatcherFactory {
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getNameMatcher(java.lang.String)
     */
    @Override
    public Predicate<Method> getNameMatcher(String pattern) {
        return new NameMatcher(pattern);
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getArgumentMatcher(java.lang.Class)
     */
    @Override
    public Predicate<Method> getArgumentMatcher(Class<?>...clz) {
        return new ArgumentMatcher(clz);
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getSignatureMatcher(java.lang.String, java.lang.Class)
     */
    @Override
    public Predicate<Method> getSignatureMatcher(String pattern, Class<?>...clz) {
        return new NameMatcher(pattern).and(new ArgumentMatcher(clz));
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.IMethodMatcherFactory#getReturnMatcher(java.lang.Class)
     */
    @Override
    public Predicate<Method> getReturnMatcher(Class<?> clz) {
        return new ReturnMatcher(clz);
    }
    
    public static abstract class MethodMatcher
        extends FluentPredicateImpl<Method> { }
    
    /**
     * Matches method names against a regular expression
     */
    public static class NameMatcher extends MethodMatcher {
        
        final String pattern;
        
        public NameMatcher(String pattern) {
            this.pattern = pattern;
        }
        
        /* (non-Javadoc)
         * @see little.nj.util.IMatcher#matches(java.lang.Object)
         */
        @Override
        public boolean evaluate(Method m) {
            return m.getName().matches(pattern);
        }
    }
    
    /**
     * Matches on parameter types
     */
    public static class ArgumentMatcher extends MethodMatcher {
        final Class<?>[] args;
        
        public ArgumentMatcher(Class<?>...args) {
            this.args = args;
        }
        
        /* (non-Javadoc)
         * @see little.nj.util.IMatcher#matches(java.lang.Object)
         */
        @Override
        public boolean evaluate(Method m) {
            Class<?>[] margs = m.getParameterTypes();
            
            if (margs.length != args.length)
                return false;
            
            for(int i = 0, end = args.length; i < end; ++i) {
                if (!margs[i].isAssignableFrom(args[i])) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    /**
     * Matches based on return type
     */
    public static class ReturnMatcher extends MethodMatcher {

        final Class<?> rv;
        
        public ReturnMatcher(Class<?> rv) {
            this.rv = rv;
        }
        
        /* (non-Javadoc)
         * @see little.nj.util.IMatcher#matches(java.lang.Object)
         */
        @Override
        public boolean evaluate(Method obj) {
            return rv.isAssignableFrom(obj.getReturnType());
        }   
    }
}