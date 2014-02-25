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

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;

import little.nj.adts.ByteField;
import little.nj.gui.components.ByteFieldPanel.FieldChangeListener;
import little.nj.gui.components.ByteFieldPanel.FieldRenderer;

public abstract class AbstractFieldRenderer implements FieldRenderer {

    private Map<ByteField, JComponent> components;
    private Map<JComponent, FieldChangeListener> listeners;
    
    public AbstractFieldRenderer() {
        components = new HashMap<ByteField, JComponent>();
        listeners = new HashMap<JComponent, FieldChangeListener>();
    }
    
    @Override
    public final synchronized JComponent register(ByteField field, FieldChangeListener listener) {
        JComponent component = create(field);
        
        components.put(field, component);
        listeners.put(component, listener);
        
        return component;
    }
    
    protected abstract JComponent create(ByteField field);

    @Override
    public final synchronized void render(ByteField field) {
        JComponent component = components.get(field);
        
        render(field, component);
    }
    
    protected abstract void render(ByteField field, JComponent component);
    
    protected final synchronized void signal(JComponent component, byte[] data) {
        FieldChangeListener listener = listeners.get(component);
        
        listener.fieldChange(data);
    }
}