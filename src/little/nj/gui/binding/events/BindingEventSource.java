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

import java.util.ArrayList;
import java.util.List;

import little.nj.gui.binding.IBinding;


/**
 * The purpose of this class is to register for events on 
 * different typed objects and raise a BindingEvent in response
 * 
 * @author Nicholas Little
 *
 */
public abstract class BindingEventSource implements IBindingEventSource {
    private IBinding bind;
    
    public BindingEventSource(IBinding binding) {
        bind = binding;
        init();
    }
    
    protected Object getSource() {
        return bind.getSrc();
    }
    
    protected abstract void init();
    
    public final synchronized void addBindingListener(IBindingListener listener) {
        listeners.add(listener);
    }
    
    public final synchronized void removeBindingListener(IBindingListener listener) {
        listeners.remove(listener);
    }
    
    protected final synchronized void fireBindingEvent(BindingEvent evt) {
        if (!bind.isEnabled())
            return;
        
        for(IBindingListener i : listeners)
            i.handleBindingEvent(evt);
    }
    
    protected final void fireBindingEvent() {
        fireBindingEvent(new BindingEvent(getSource(), bind));
    }
    
    private List<IBindingListener> listeners = new ArrayList<>();
}
