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
package little.nj.gui.components;

import little.nj.adts.ByteField;
import little.nj.adts.IntByteField;
import little.nj.adts.ShortByteField;
import little.nj.adts.StringByteField;


/**
 * @author Nicholas Little
 *
 */
public class FieldPanelFactory {

    public static ByteFieldPanel create(ByteField field) {
        
        ByteFieldPanel panel = new ByteFieldPanel(field);
        
        if (field instanceof StringByteField)
            panel.setRenderer(render_str);
        else if (field instanceof IntByteField)
            panel.setRenderer(render_int);
        else if (field instanceof ShortByteField)
            panel.setRenderer(render_int);
        
        return panel;
    }
    
    private static IntegerFieldRenderer render_int = new IntegerFieldRenderer();
    private static StringFieldRenderer render_str = new StringFieldRenderer();
}
