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
package little.nj.util.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import little.nj.reflection.IterativeUtil;
import little.nj.reflection.MethodMatcherFactoryImpl;
import little.nj.reflection.RecursiveUtil;
import little.nj.reflection.ReflectionUtil;

import org.junit.BeforeClass;
import org.junit.Test;

@SuppressWarnings("unused")

/**
 * @author Nicholas Little
 *
 */
public class ReflectionUtilTest {
    
    private static class Obj {
        public void Method(String x) { }
        public void Method(int x) { }
    }
    
    private static class Obj2 extends Obj {
        public void Method2(String x) { }
        public void Method2(int x) { }
    }
    
    private static ReflectionUtil RECURSOR;
    private static ReflectionUtil ITERATOR;
    
    private static Obj OBJECT;
    private static Obj2 OBJECT2;
    
    private static final String METHOD = "Method";
    private static final String METHOD2 = "Method2";
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        RECURSOR = new RecursiveUtil();
        ITERATOR = new IterativeUtil();
        
        OBJECT = new Obj();
        OBJECT2 = new Obj2();
    }

    @Test
    public void test_Recursive_Finds_Method_By_Name() {
        Method rv = RECURSOR.getMethodByName(OBJECT, METHOD);
        
        assertNotNull(rv);
        assertEquals(METHOD, rv.getName());
    }
    
    @Test
    public void test_Iterative_Finds_Method_By_Name() {
        Method rv = ITERATOR.getMethodByName(OBJECT, METHOD);
        
        assertNotNull(rv);
        assertEquals(METHOD, rv.getName());
    }
    
    @Test
    public void test_Recursive_Finds_Ancestor_Method() {
        Method rv = RECURSOR.getMethod(OBJECT2, METHOD2, String.class);
        
        assertNotNull(rv);
        assertEquals(METHOD2, rv.getName());
        assertEquals(String.class, rv.getParameterTypes()[0]);
    }
    
    @Test
    public void test_Iterative_Finds_Ancestor_Method() {
        Method rv = ITERATOR.getMethod(OBJECT2, METHOD2, String.class);
        
        assertNotNull(rv);
        assertEquals(METHOD2, rv.getName());
        assertEquals(String.class, rv.getParameterTypes()[0]);
    }
    
    @Test
    public void Test_Recursive_Finds_Method_By_Args() {
        Method rv = RECURSOR.getMethodByArgs(OBJECT, new Class[] { int.class });
        
        assertNotNull(rv);
        assertEquals(METHOD, rv.getName());
        assertEquals(int.class, rv.getParameterTypes()[0]);
    }
    
    @Test
    public void Test_Iterative_Finds_Method_By_Args() {
        Method rv = ITERATOR.getMethodByArgs(OBJECT, new Class[] { int.class });
        
        assertNotNull(rv);
        assertEquals(METHOD, rv.getName());
        assertEquals(int.class, rv.getParameterTypes()[0]);
    }

    @Test
    public void Test_Recursive_Finds_Method_By_Sig() {
        Method rv = RECURSOR.getMethod(OBJECT, METHOD, new Class[] { int.class });
        
        assertNotNull(rv);
        assertEquals(METHOD, rv.getName());
        assertEquals(int.class, rv.getParameterTypes()[0]);
    }
    
    @Test
    public void Test_Iterative_Finds_Method_By_Sig() {
        Method rv = ITERATOR.getMethod(OBJECT, METHOD, new Class[] { int.class });
        
        assertNotNull(rv);
        assertEquals(METHOD, rv.getName());
        assertEquals(int.class, rv.getParameterTypes()[0]);
    }
    
    @Test
    public void Test_Recursive_Finds_Method_By_Return() {
        Method rv = RECURSOR.getMethod(OBJECT, new MethodMatcherFactoryImpl.ReturnMatcher(String.class));
        
        assertNotNull(rv);
    }

    @Test
    public void Test_Iterative_Finds_Method_By_Return() {
        Method rv = ITERATOR.getMethod(OBJECT, new MethodMatcherFactoryImpl.ReturnMatcher(String.class));
        
        assertNotNull(rv);
    }
}
