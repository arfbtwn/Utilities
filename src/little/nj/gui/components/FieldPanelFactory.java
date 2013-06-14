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
        if (field instanceof StringByteField)
            return  new StringFieldPanel((StringByteField)field);
        else if (field instanceof IntByteField)
            return new IntegerFieldPanel((IntByteField)field);
        else if (field instanceof ShortByteField)
            return new IntegerFieldPanel((ShortByteField)field);
        else
            return new ByteFieldPanel(field);
    }
}
