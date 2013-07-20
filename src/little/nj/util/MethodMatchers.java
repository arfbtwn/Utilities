package little.nj.util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodMatchers {

    static interface IMethodMatcher extends IMatcher<Method> { }
    
    static abstract class AbstractMatcher implements IMethodMatcher {
        public abstract boolean matches(Method m);
        
        public Method find(Method[] m) {
            for(Method i : m)
                if (matches(i))
                    return i;
            
            return null;
        }
    }
    
    static class NameMatcher extends AbstractMatcher {
        
        final String pattern;
        
        NameMatcher(String pattern) {
            this.pattern = pattern;
        }
        
        public boolean matches(Method m) {
            return m.getName().matches(pattern);
        }
    }
    
    static class ArgumentMatcher extends AbstractMatcher {
        final Class<?>[] args;
        
        ArgumentMatcher(Class<?>...args) {
            this.args = args;
        }
        
        public boolean matches(Method m) {
            return Arrays.equals(m.getParameterTypes(), args);
        }
    }
    
    static class ExactMatcher extends AbstractMatcher {
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