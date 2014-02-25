/**
 * Copyright (C) 2014 
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

import java.nio.ByteBuffer;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import little.nj.adts.ByteField;

public class IntegerFieldRenderer extends AbstractFieldRenderer {

    @Override
    protected JComponent create(ByteField field) {
        final JSpinner component = new JSpinner();
        final int len = field.getLength();
        
        component.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                
                Long value = ((Number)component.getValue()).longValue();
                
                ByteBuffer buf = ByteBuffer.allocate(len);
                
                switch(len * 8) {
                case Short.SIZE:
                    buf.putShort(value.shortValue());
                    break;
                case Integer.SIZE:
                    buf.putInt(value.intValue());
                    break;
                case Long.SIZE:
                    buf.putLong(value);
                    break;
                default:
                    break;
                }
                
                signal(component, buf.array());
            } });
        
        return component;
    }

    @Override
    protected void render(ByteField field, JComponent component) {
        JSpinner spinner = (JSpinner) component;
        
        int len = field.getLength();
        
        ByteBuffer buf = ByteBuffer.wrap(field.getBytes());
        Long value = 0L;
        
        switch(len * 8) {
        case Short.SIZE:
            value = (long) buf.getShort();
            break;
        case Integer.SIZE:
            value = (long) buf.getInt();
            break;
        case Long.SIZE:
            value = buf.getLong();
            break;
        default:
            break;
        }
        
        spinner.setValue(value);
    }

}
