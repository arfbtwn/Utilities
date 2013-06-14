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

import java.awt.event.FocusAdapter;
import javax.swing.JComponent;
import javax.swing.JTextField;
import little.nj.adts.StringByteField;


/**
 * @author Nicholas Little
 *
 */
@SuppressWarnings("serial")
public class StringFieldPanel extends ByteFieldPanel {

    private JTextField text; 
    
    public StringFieldPanel(StringByteField field) {
        super(field);
    }
    
    /* (non-Javadoc)
     * @see gui.components.ByteFieldPanel#getView()
     */
    @Override
    protected JComponent getView() {
        if (text == null) {
            text = new JTextField();
            
            text.addFocusListener(new FocusAdapter() {
                /*
                 * (non-Javadoc)
                 * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
                 */
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    ((StringByteField)getField()).setValue(text.getText());
                    refresh();
                }
            });
        }
        
        return text;
    }
    
    /* (non-Javadoc)
     * @see gui.components.fieldpanels.ByteFieldPanel#refresh()
     */
    @Override
    protected void refresh() {
        super.refresh();
        
        text.setText(((StringByteField)getField()).getValue());
    }

}
