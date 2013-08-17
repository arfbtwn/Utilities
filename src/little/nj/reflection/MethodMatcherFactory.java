/**
 * Copyright (C) 2013 Nicholas J. Little <arealityfarbetween@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.reflection;

import java.lang.reflect.Method;

import little.nj.expressions.predicates.Predicate;


public interface MethodMatcherFactory {
    
    Predicate<Method> getNameMatcher(String pattern);

    Predicate<Method> getArgumentMatcher(Class<?>... clz);

    Predicate<Method> getSignatureMatcher(String pattern, Class<?>... clz);

    Predicate<Method> getReturnMatcher(Class<?> clz);

}