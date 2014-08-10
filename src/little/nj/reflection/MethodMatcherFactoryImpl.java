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

import little.nj.expressions.predicates.FluentPredicate;
import little.nj.expressions.predicates.Predicate;
import little.nj.util.ConversionUtil;

import java.lang.reflect.Method;

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
        extends FluentPredicate<Method> { }

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
                if (!ConversionUtil.assignCompatible(margs[i], args[i]))
                {
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
            return ConversionUtil.assignCompatible(rv, obj.getReturnType());
        }
    }
}
