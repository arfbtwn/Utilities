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

import static org.junit.Assert.*;

import javax.swing.JCheckBox;

import little.nj.core.tests.MockObjects.ObGeneric;
import little.nj.gui.binding.Binding;
import little.nj.gui.binding.ITypeConverter;

import org.junit.Test;


/**
 * @author Nicholas Little
 *
 */
public class BindingConversionTest {

    @Test
    public void test_Prototype_Converter() {
        
        JCheckBox box = new JCheckBox();
        
        assertEquals(false, box.isSelected());
        
        ObGeneric<String> obj = new ObGeneric<>("true");
        
        Binding bind = new Binding(obj, box, "getField", "setSelected");

        bind.setConverter(converter);
        
        bind.bind();
        
        assertEquals(true, box.isSelected());
        
        obj.setField("false");
        
        bind.bind();
        
        assertEquals(false, box.isSelected());
        
    }
    
    private ITypeConverter<String, Boolean> converter = new ITypeConverter<String, Boolean>() {

        @Override
        public Boolean convert(String in) {
            in = in.trim();
            
            return Boolean.parseBoolean(in); 
        }
    };

}
