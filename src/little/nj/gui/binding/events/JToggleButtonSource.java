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
package little.nj.gui.binding.events;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JToggleButton;

import little.nj.gui.binding.IBinding;


/**
 * @author Nicholas Little
 *
 */
public class JToggleButtonSource extends BindingEventSource {

    public static boolean handlesBinding(IBinding bind) {
        return bind.getSrc() instanceof JToggleButton &&
                "isSelected".equals(bind.getSrcMethod().getName());
    }
    
    /**
     * @param binding
     */
    public JToggleButtonSource(IBinding binding) {
        super(binding);
    }

    /* (non-Javadoc)
     * @see little.nj.gui.binding.events.BindingEventSource#init()
     */
    @Override
    protected void init() {
        Object obj = getSource();
        
        if (obj instanceof JToggleButton) {
            JToggleButton jtb = (JToggleButton) obj;
            
            jtb.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED)
                        fireBindingEvent();
                } });
        }

    }

}
