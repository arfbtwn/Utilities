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


/**
 * @author Nicholas Little
 *
 */
public class ReflectionUtil {

    public enum Strategy { RECURSIVE, ITERATIVE } 
    
    private Strategy strategy;
    
    public ReflectionUtil() {
        this(Strategy.RECURSIVE);
    }
    
    public ReflectionUtil(Strategy strategy) { 
        this.strategy = strategy;
    }
    
    /**
     * @see ReflectionUtil#getMethod(Class, String, Class...)
     * 
     * @param obj
     * @param method
     * @param clzprivate 
     * @return Method or null
     */
    public Method getMethod(Object obj, String method, Class<?>...clz) {
        if (obj == null)
            return null;
        
        return getMethod(obj.getClass(), method, clz);
    }
    
    /**
     * Searches the ancestral tree for the appropriate method until
     * we get to the top.
     * 
     * @param obj
     * @param method
     * @param clz
     * @return Method or null
     */
    public Method getMethod(Class<?> claz, String method, Class<?>...clz)
    {
        switch(strategy) {
        case RECURSIVE:
            return getMethodRecursive(claz, method, clz);
        case ITERATIVE:
        default:
            return getMethodIterative(claz, method, clz);
                
        }
    }
    
    /**
     * Searches the ancestral tree for the named method until
     * we get to the top.
     * 
     * @param obj
     * @param method
     * @return
     */
    public Method getMethodByName(Object obj, String method) {
        if (obj == null)
            return null;
        
        return getMethodByName(obj.getClass(), method);
    }
    
    /**
     * @see ReflectionUtil#getMethodByName(Object, String)
     * 
     * @param claz
     * @param method
     * @return
     */
    public Method getMethodByName(Class<?> claz, String method) {
        switch(strategy) {
        case RECURSIVE:
            return getMethodByNameRecursive(claz, method);
        case ITERATIVE:
        default:
            return getMethodByNameIterative(claz, method);
        }
    }
    
    private final static Method getMethodIterative(Class<?> claz, String method, Class<?>...clz) {
        do {
            try {
                return claz.getDeclaredMethod(method, clz);
            } catch (NoSuchMethodException e) {
                claz = claz.getSuperclass();
                
                if (claz == null)
                    break;
                
            } catch (SecurityException e) { break; }
        } while(true);
        
        return null;
    }
    
    private final static Method getMethodRecursive(Class<?> claz, String method, Class<?>...clz) {
        try {
            return claz.getDeclaredMethod(method, clz);
        } catch (NoSuchMethodException e) {
            claz = claz.getSuperclass();
            if (claz != null)
                return getMethodRecursive(claz, method, clz);
                
        } catch (SecurityException e) { }
        
        return null;
    }
    
    private final static Method getMethodByNameRecursive(Class<?> claz, String method) {
        try {
            Method[] poss = claz.getDeclaredMethods();
            
            Method rv;
            if ((rv = match(poss, method)) != null) {
                return rv;
            }
            
            claz = claz.getSuperclass();
                
            if (claz != null)
                return getMethodByNameRecursive(claz, method);
            
        } catch (SecurityException e) { }
        
        return null;
    }
    
    private final static Method getMethodByNameIterative(Class<?> claz, String method) {
        do {
            try {
                Method[] poss = claz.getDeclaredMethods();
                
                Method rv;
                if ((rv = match(poss, method)) != null) {
                    return rv;
                }
                
                claz = claz.getSuperclass();
                
                if (claz == null)
                    break;
                
            } catch (SecurityException e) { break; }
        } while(true);
        
        return null;
    }
    
    private final static Method match(Method[] methods, String name) {
        for(Method i : methods) {
            if (i.getName().equals(name))
                return i;
        }
        return null;
    }
    
    /**
     * Singleton instance
     * @return ReflectionUtil with default construction
     */
    public final synchronized static ReflectionUtil getInstance() {
        if (_instance == null)
            _instance = new ReflectionUtil();
        
        return _instance;
    }
    
    private static ReflectionUtil _instance;
}
