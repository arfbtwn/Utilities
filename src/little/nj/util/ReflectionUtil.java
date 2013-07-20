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
package little.nj.util;

import java.lang.reflect.Method;

import little.nj.util.MethodMatchers.IMethodMatcher;
import little.nj.util.MethodMatchers.NameMatcher;
import little.nj.util.MethodMatchers.ArgumentMatcher;
import little.nj.util.MethodMatchers.ExactMatcher;

/**
 * @author Nicholas Little
 *
 */
public abstract class ReflectionUtil {
    
    /**
     * Searches the ancestral tree by recursion
     *
     */
    public static class RecursiveUtil extends ReflectionUtil {
        @Override 
        protected Method getMethodImpl(Class<?> claz, IMethodMatcher matcher) {
            if (claz == null)
                return null;
            
            try {
                Method[] poss = claz.getDeclaredMethods();
                
                Method rv;
                if ((rv = matcher.find(poss)) != null) {
                    return rv;
                }
                
            } catch (SecurityException e) { }
            
            return getMethodImpl(claz.getSuperclass(), matcher);
        }
    }
    
    /**
     * Searches the tree by iteration
     *
     */
    public static class IterativeUtil extends ReflectionUtil {
        @Override 
        protected Method getMethodImpl(Class<?> claz, IMethodMatcher matcher) {
            do {
                try {
                    if (claz == null)
                        break;
                    
                    Method[] poss = claz.getDeclaredMethods();
                    
                    Method rv;
                    if ((rv = matcher.find(poss)) != null) {
                        return rv;
                    }
                    
                    claz = claz.getSuperclass();
                    
                } catch (SecurityException e) { break; }
            } while(true);
            
            return null;
        }
    }
    
    /**
     * @see ReflectionUtil#getMethodImpl(Class, IMethodMatcher)
     * 
     * @param obj
     * @param matcher
     * @return 
     */
    protected Method getMethodImpl(Object obj, IMethodMatcher matcher) {
        if (obj == null)
            return null;
        
        return getMethodImpl(obj.getClass(), matcher);
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
    protected abstract Method getMethodImpl(Class<?> clz, IMethodMatcher matcher);
    
    /**
     * Uses an {@link ExactMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Object, IMethodMatcher)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethod(Object obj, String method, Class<?>...clz) {
        return getMethodImpl(obj, new ExactMatcher(method, clz));
    }
    
    /**
     * Uses an {@link ExactMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Class, IMethodMatcher)
     * 
     * @param claz
     * @param method
     * @param clz
     * @return
     */
    public Method getMethod(Class<?> claz, String method, Class<?>...clz)
    {
        return getMethodImpl(claz, new ExactMatcher(method, clz));
    }
    
    /**
     * Uses a {@link NameMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Object, IMethodMatcher)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethodByName(Object obj, String method) {
        return getMethodImpl(obj, new NameMatcher(method));
    }
    
    /**
     * Uses a {@link NameMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Class, IMethodMatcher)
     * 
     * @param claz
     * @param method
     * @param clz
     * @return
     */
    public Method getMethodByName(Class<?> claz, String method) {
        return getMethodImpl(claz, new NameMatcher(method));
    }
    
    /**
     * Uses an {@link ArgumentMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Object, IMethodMatcher)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethodByArgs(Object obj, Class<?>...clz) {
        return getMethodImpl(obj, new ArgumentMatcher(clz));
    }
    
    /**
     * Uses an {@link ArgumentMatcher} to return the appropriate method
     * 
     * @see ReflectionUtil#getMethodImpl(Class, IMethodMatcher)
     * 
     * @param claz
     * @param method
     * @param clz
     * @return
     */
    public Method getMethodByArgs(Class<?> claz, Class<?>...clz) {
        return getMethodImpl(claz, new ArgumentMatcher(clz));
    }
    
    /**
     * Singleton instance
     * @return ReflectionUtil with default construction
     */
    public final synchronized static ReflectionUtil getInstance() {
        if (_instance == null)
            _instance = new RecursiveUtil();
        
        return _instance;
    }
    
    private static ReflectionUtil _instance;
}
