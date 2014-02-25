/**
 * Copyright (C) 2013 
 * Nicholas J. Little <arealityfarbetween@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.reflection;

import java.lang.reflect.Method;
import little.nj.expressions.predicates.Predicate;


public abstract class ReflectionUtil {
    
    private final MethodMatcherFactory factory;
    
    public ReflectionUtil() {
        this(new MethodMatcherFactoryImpl());
    }
    
    public ReflectionUtil(MethodMatcherFactory factory) {
        this.factory = factory;
    }
    
    /**
     * @see ReflectionUtil#getMethodImpl(Class, IPredicate<Method>)
     * 
     * @param obj
     * @param matcher
     * @return 
     */
    public final Method getMethod(Object obj, Predicate<Method> matcher) {
        if (obj == null)
            return null;
        
        return getMethodImpl(obj.getClass(), matcher);
    }
    
    /**
     * @see ReflectionUtil#getMethodImpl(Class, IPredicate<Method>)
     * 
     * @param clz
     * @param matcher
     * @return
     */
    public final Method getMethod(Class<?> clz, Predicate<Method> matcher) {
        return getMethodImpl(clz, matcher);
    }
    
    /**
     * Searches the ancestral tree by recursion or iteration until
     * the appropriate method signature is found, or the tree has
     * been fully traversed
     * 
     * @param clz
     * @param matcher
     * @return null if clz is null or the method was not found
     */
    protected abstract Method getMethodImpl(Class<?> clz, Predicate<Method> matcher);
    
    /**
     * Performs the match against a list of possible methods
     * 
     * @param poss
     * @param matcher
     * @return
     */
    protected Method matchImpl(Method[] poss, Predicate<Method> matcher) {

        for(Method i : poss) {
            if (matcher.evaluate(i))
                return i;
        }
    
        return null;
    }
    
    /**
     * Uses an {@link SignatureMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Object, IPredicate<Method>)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethod(Object obj, String method, Class<?>...clz) {
        return getMethod(obj, factory.getSignatureMatcher(method, clz));
    }
    
    /**
     * Uses an {@link SignatureMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Class, IPredicate<Method>)
     * 
     * @param claz
     * @param method
     * @param clz
     * @return
     */
    public Method getMethod(Class<?> claz, String method, Class<?>...clz)
    {
        return getMethod(claz, factory.getSignatureMatcher(method, clz));
    }
    
    /**
     * Uses a {@link NameMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Object, IPredicate<Method>)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethodByName(Object obj, String method) {
        return getMethod(obj, factory.getNameMatcher(method));
    }
    
    /**
     * Uses a {@link NameMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Class, IPredicate<Method>)
     * 
     * @param claz
     * @param method
     * @param clz
     * @return
     */
    public Method getMethodByName(Class<?> claz, String method) {
        return getMethod(claz, factory.getNameMatcher(method));
    }
    
    /**
     * Uses an {@link ArgumentMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Object, IPredicate<Method>)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethodByArgs(Object obj, Class<?>...clz) {
        return getMethod(obj, factory.getArgumentMatcher(clz));
    }
    
    /**
     * Uses an {@link ArgumentMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Class, IPredicate<Method>)
     * 
     * @param claz
     * @param method
     * @param clz
     * @return
     */
    public Method getMethodByArgs(Class<?> claz, Class<?>...clz) {
        return getMethod(claz, factory.getArgumentMatcher(clz));
    }
    
    /**
     * Singleton instance
     * 
     * @return RecursiveUtil with default construction
     */
    public final synchronized static ReflectionUtil getInstance() {
        if (_instance == null)
            _instance = new RecursiveUtil();
        
        return _instance;
    }
    
    private static ReflectionUtil _instance;
}
