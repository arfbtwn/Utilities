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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import little.nj.gui.binding.Binding2;


/**
 * @author Nicholas Little
 *
 */
public class JTextComponentSource extends EventSourceImpl {

    public static boolean handlesBinding(Binding2 binding) {
        
        if (!(binding.getSrc() instanceof JTextComponent))
            return false;
        
        String src_mthd = binding.getSrcMethod().getName();
        
        if (!"getText".equals(src_mthd)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * @param obj
     * @param binding
     */
    public JTextComponentSource(Binding2 binding) {
        super(binding, binding.getSrc());
    }

    protected void init() {        
        JTextComponent jtc = (JTextComponent) obj;
        jtc.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                fireBindingEvent(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireBindingEvent(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fireBindingEvent(e);
            }
            
        });
    }
}
