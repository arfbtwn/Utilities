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
package little.nj.expressions.predicates;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.tests.MockObjects.Ob;
import little.nj.core.tests.MockObjects.ObGeneric;
import little.nj.expressions.ExpressionEngineImpl;
import little.nj.expressions.ExpressionEngine;
import little.nj.expressions.predicates.FluentPredicateImpl;

import org.junit.Test;


public class PredicateTest {
    
    @Test
    public void test() {
        
        List<ObGeneric<String>> obs = new ArrayList<ObGeneric<String>>();
        
        for(int i=0; i<100; ++i) {
            obs.add(new ObGeneric<String>(i % 20 == 0 ? "Hello World" 
                                                : i % 10 == 0 ? "Hello, World" 
                                                              : "World, Hello"));
        }
        
        FluentPredicate<ObGeneric<String>> pred = new FluentPredicateImpl<ObGeneric<String>>() {

            @Override
            public boolean evaluate(ObGeneric<String> obj) {
                return "Hello World".equals(obj.getField());
            }
        };
        
        FluentPredicate<ObGeneric<String>> pred2 = new FluentPredicateImpl<ObGeneric<String>>() {

            @Override
            public boolean evaluate(ObGeneric<String> obj) {
                return "Hello, World".equals(obj.getField());
            }
        };
        
        FluentPredicate<ObGeneric<String>> pred3 = pred.or(pred2);
        
        FluentPredicate<ObGeneric<String>> pred4 = pred3.not();
        
        ExpressionEngine<ObGeneric<String>> start = new ExpressionEngineImpl<ObGeneric<String>>(obs);
        
        assertEquals(100, start.count());
        
        assertEquals(5, start.where(pred).count());
        
        assertEquals(5, start.where(pred2).count());
        
        assertEquals(10, start.where(pred3).count());
        
        assertEquals(90, start.where(pred4).count());
        
        assertEquals(5, start.where(pred3).where(pred).count());
        
        assertEquals(5, start.where(pred3).where(pred2).count());
        
        assertEquals(10, start.where(pred).union(start.where(pred2)).count());
        
        assertEquals(100, start.toList().size());
    }
    
    private class BoxedPredicate<T> extends FluentPredicateImpl<T> {

        Predicate<T> misbehaved = new Predicate<T>() {

            @SuppressWarnings("null")
            @Override
            public boolean evaluate(T obj) {
                Boolean rv = null;
                return rv;
            }
            
        };
        
        /* (non-Javadoc)
         * @see little.nj.expressions.predicates.IPredicate#evaluate(java.lang.Object)
         */
        @Override
        public boolean evaluate(T obj) {
            return protect(misbehaved.evaluate(obj));
        } 
        
        private final boolean protect(Boolean b) {
            return b == null ? false : b;
        }
    }
    
    @Test
    public void test_Boxed_Protect_Fails() {
        BoxedPredicate<String> pred = new BoxedPredicate<String>();
        
        try {
            pred.evaluate("Hello, World");
            fail();
        } catch (NullPointerException ex) {
            assertNotNull(ex);
        }
    }
    
    @Test
    public void test_Contravariance() {
        FluentPredicate<Ob> pred = new FluentPredicateImpl<Ob>() {

            @Override
            public boolean evaluate(Ob obj) {
                return obj.getField() > 0;
            }
        }.and(new Predicate<Object>() {
            
            @Override
            public boolean evaluate(Object obj) {
                return true;
            } });
        
        Ob ob1 = new Ob(0), ob2 = new Ob(1);
        
        assertFalse(pred.evaluate(ob1));
        assertTrue(pred.evaluate(ob2));
    }

}
