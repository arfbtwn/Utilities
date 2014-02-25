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

import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import little.nj.adts.ByteField;

public class StringFieldRenderer extends AbstractFieldRenderer {

    @Override
    protected JComponent create(ByteField field) {
        final int max = field.getLength();
        final JTextField component = new JTextField(max);
        
        AbstractDocument doc = (AbstractDocument) component.getDocument();
        
        doc.setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset,
                    String string, AttributeSet attr)
                    throws BadLocationException {
                
                if (max <= offset)
                    return;
                
                if (max <= offset + string.length()) {
                    string = string.substring(0, max - offset);
                }
                
                super.insertString(fb, offset, string, attr);
                signal(component, component.getText().getBytes());
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length,
                    String text, AttributeSet attrs)
                    throws BadLocationException {
                
                if (max <= offset)
                    return;
                
                if (max <= offset + text.length()) {
                    text = text.substring(0, max - offset);
                }
                
                super.replace(fb, offset, length, text, attrs);
                signal(component, component.getText().getBytes());
            }
            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws BadLocationException {
                
                super.remove(fb, offset, length);
                signal(component, component.getText().getBytes());
            }
        });
        
        return component;
    }

    @Override
    protected void render(ByteField field, JComponent component) {
        JTextField comp = (JTextField)component;
        
        comp.setText(new String(field.getBytes()));
    }

}
