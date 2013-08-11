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
package little.nj.gui.binding.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import javax.swing.JCheckBox;
import javax.swing.JTextArea;

import little.nj.core.tests.MockObjects.ObGenericChangeNotify;
import little.nj.gui.binding.BindingFactoryImpl;
import little.nj.gui.binding.BindingManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class BindingManagerTest {

    protected BindingManager manager;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        manager = new BindingManager(new BindingFactoryImpl());
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_JTextComponent() {
        
        ObGenericChangeNotify<String> 
            one = new ObGenericChangeNotify<>("Hello? World!");
        
        JTextArea two = new JTextArea();
            
        System.out.println("Initial Values:");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.getText());
        System.out.println("--");
        
        assertNotSame(one.getField(), two.getText());
        
        manager.create(one, two, "Field", "Text");
        
        System.out.println("Binding (Manager):");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.getText());
        System.out.println("--");
        
        assertEquals(one.getField(), two.getText());
        
        one.setField("Hello, World!");
        
        System.out.println("Change Field (one):");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.getText());
        System.out.println("--");
        
        assertEquals(one.getField(), two.getText());
        
        two.setText("World? Hello!");
        
        System.out.println("Change Field (two):");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.getText());
        System.out.println("--");
        
        assertEquals(one.getField(), two.getText());
    }
    
    @Test
    public void test_JCheckBox() {
    
        ObGenericChangeNotify<Boolean> 
            one = new ObGenericChangeNotify<>(true);
    
        JCheckBox two = new JCheckBox();
        
        System.out.println("Initial Values:");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.isSelected());
        System.out.println("--");
        
        assertNotSame(one.getField(), two.isSelected());
        
        manager.create(one, two,  
                "getField", "setSelected",
                "isSelected", "setField");
        
        System.out.println("Binding (Manager):");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.isSelected());
        System.out.println("--");
        
        assertEquals(one.getField(), two.isSelected());
        
        one.setField(false);
        
        System.out.println("Change Field (one):");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.isSelected());
        System.out.println("--");
        
        assertEquals(one.getField(), two.isSelected());
        
        two.setSelected(true);
        
        System.out.println("Change Field (two):");
        System.out.println("one: " + one.getField());
        System.out.println("two: " + two.isSelected());
        System.out.println("--");
        
        assertEquals(one.getField(), two.isSelected());
    
    }
    
//    @Test
//    public void test_ItemSelectable() {
//    
//        ObGenericChangeNotify<Object> 
//            one = new ObGenericChangeNotify<>((Object)"Hello? World");
//
//        JComboBox<Object> two = new JComboBox<Object>();
//        
//        System.out.println("Initial Values:");
//        System.out.println("one: " + one.getField());
//        System.out.println("two: " + two.getSelectedItem());
//        System.out.println("--");
//        
//        assertNotSame(one.getField(), two.getSelectedObjects());
//        
//        manager.create(one, two, Object[].class, 
//                "getField", "setSelectedItem",
//                "isSelected", "setField");
//        
//        System.out.println("Binding (Manager):");
//        System.out.println("one: " + one.getField());
//        System.out.println("two: " + two.getSelectedObjects());
//        System.out.println("--");
//        
//        assertArrayEquals(one.getField(), two.getSelectedObjects());
//        
//        one.setField(new Object[] { "World? Hello", "Hello, World!" });
//        
//        System.out.println("Change Field (one):");
//        System.out.println("one: " + one.getField());
//        System.out.println("two: " + two.getSelectedObjects());
//        System.out.println("--");
//        
//        assertArrayEquals(one.getField(), two.getSelectedObjects());
//        
//        two.setSelectedItem("World? Hello");
//        
//        System.out.println("Change Field (two):");
//        System.out.println("one: " + one.getField());
//        System.out.println("two: " + two.isSelected());
//        System.out.println("--");
//        
//        assertEquals(one.getField(), two.isSelected());
//        
//    }

}
