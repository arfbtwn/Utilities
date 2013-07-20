package little.nj.util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodMatchers {
    
    public static interface IMethodMatcher extends IMatcher<Method> { }
    
    public static abstract class MethodMatcher 
        extends AbstractMatcher<Method>
        implements IMethodMatcher
    { }
    
    /**
     * Matches method names against a regular expression
     */
    static class NameMatcher extends MethodMatcher {
        
        final String pattern;
        
        NameMatcher(String pattern) {
            this.pattern = pattern;
        }
        
        public boolean matches(Method m) {
            return m.getName().matches(pattern);
        }
    }
    
    /**
     * Matches on parameter types
     */
    static class ArgumentMatcher extends MethodMatcher {
        final Class<?>[] args;
        
        ArgumentMatcher(Class<?>...args) {
            this.args = args;
        }
        
        public boolean matches(Method m) {
            return Arrays.equals(m.getParameterTypes(), args);
        }
    }
    
    /**
     * Matches on name and parameters
     */
    static class ExactMatcher extends MethodMatcher {
        NameMatcher name;
        ArgumentMatcher args;
        
        ExactMatcher(NameMatcher name, ArgumentMatcher args) {
            this.name = name;
            this.args = args;
        }
        
        ExactMatcher(String pattern, Class<?>...args) {
            this(new NameMatcher(pattern), new ArgumentMatcher(args));
        }
        
        public boolean matches(Method m) {
            return name.matches(m) && args.matches(m);
        }
    }
}