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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import little.nj.core.tests.MockObjects.ObGeneric;
import little.nj.expressions.ExpressionEngine;
import little.nj.expressions.IExpressionEngine;
import little.nj.expressions.predicates.Predicate;

import org.junit.Test;


public class PredicateTest {
    
    @Test
    public void test() {
        
        List<ObGeneric<String>> obs = new ArrayList<>();
        
        for(int i=0; i<100; ++i) {
            obs.add(new ObGeneric<>(i % 20 == 0 ? "Hello World" 
                                                : i % 10 == 0 ? "Hello, World" 
                                                              : "World, Hello"));
        }
        
        Predicate<ObGeneric<String>> pred = new Predicate<ObGeneric<String>>() {

            @Override
            public Boolean evaluateImpl(ObGeneric<String> obj) {
                return "Hello World".equals(obj.getField());
            }
        };
        
        Predicate<ObGeneric<String>> pred2 = new Predicate<ObGeneric<String>>() {

            @Override
            public Boolean evaluateImpl(ObGeneric<String> obj) {
                return "Hello, World".equals(obj.getField());
            }
        };
        
        Predicate<ObGeneric<String>> pred3 = pred.or(pred2);
        
        Predicate<ObGeneric<String>> pred4 = pred3.not();
        
        IExpressionEngine<ObGeneric<String>> start = new ExpressionEngine<>(obs);
        
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

}
