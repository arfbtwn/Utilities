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

import little.nj.expressions.predicates.IPredicate;


/**
 * Searches the ancestral tree by recursion
 * 
 * @author Nicholas Little
 *
 */
public class RecursiveUtil extends ReflectionUtil {

    public RecursiveUtil() {
        super();
    }
    
    public RecursiveUtil(IMethodMatcherFactory factory) {
        super(factory);
    }
    
    /* (non-Javadoc)
     * @see little.nj.reflection.ReflectionUtil#getMethodImpl(java.lang.Class, little.nj.reflection.ReflectionUtil.IMethodMatcher)
     */
    @Override
    protected Method getMethodImpl(Class<?> claz, IPredicate<Method> matcher) {
        if (claz == null)
            return null;
        
        try {
            Method[] poss = claz.getDeclaredMethods();
            
            Method rv;
            if ((rv = matchImpl(poss, matcher)) != null) {
                return rv;
            }
            
        } catch (SecurityException e) { }
        
        return getMethodImpl(claz.getSuperclass(), matcher);
    }

}
