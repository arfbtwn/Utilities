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

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import little.nj.adts.ByteField;
import little.nj.adts.IntByteField;
import little.nj.adts.ShortByteField;


/**
 * @author Nicholas Little
 *
 */
@SuppressWarnings("serial")
public class IntegerFieldPanel extends ByteFieldPanel {

    private JSpinner spinner;
    
    public IntegerFieldPanel(IntByteField field) {
        super(field);     
    }
    
    public IntegerFieldPanel(ShortByteField field) {
        super(field);
    }
    
    /* (non-Javadoc)
     * @see gui.components.fieldpanels.ByteFieldPanel#getView()
     */
    @Override
    protected JComponent getView() {
        if (spinner == null) {
            spinner = new JSpinner();
            
            spinner.addChangeListener(new ChangeListener() { 
                /* (non-Javadoc)
                 * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
                 */
                @Override
                public void stateChanged(ChangeEvent e) {
                    ByteField f = getField();
                    
                    if (f instanceof IntByteField)
                        ((IntByteField)f).setValue((int)spinner.getValue());
                    else if (f instanceof ShortByteField)
                        ((ShortByteField)f).setValue((short)spinner.getValue());
                    
                    refresh();
                }
            });
        }
        return spinner;
    }

    /* (non-Javadoc)
     * @see gui.components.fieldpanels.ByteFieldPanel#refresh()
     */
    @Override
    protected void refresh() {
        super.refresh();
        
        ByteField f = getField();
        if (f instanceof IntByteField)
            spinner.setValue(((IntByteField)f).getValue());
        else if (f instanceof ShortByteField) {
            spinner.setValue(((ShortByteField)f).getValue());
        }
        
    }
}
