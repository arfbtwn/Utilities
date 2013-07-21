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

import little.nj.gui.binding.IBinding;
import little.nj.gui.events.EventSupport;


/**
 * The purpose of this class is to register for events on 
 * different typed objects and raise a BindingEvent in response
 * 
 * @author Nicholas Little
 *
 */
public abstract class BindingEventSource implements IBindingEventSource {
    
    private EventSupport<IBindingListener, BindingEvent> support;
    private IBinding bind;
    
    public BindingEventSource(IBinding binding) {
        support = new EventSupport<>();
        
        bind = binding;
        
        init();
    }
    
    protected Object getSource() {
        return bind.getSrc();
    }
    
    protected abstract void init();
    
    public void addBindingListener(IBindingListener listener) {
        support.addEventListener(listener);
    }
    
    public void removeBindingListener(IBindingListener listener) {
        support.removeEventListener(listener);
    }
    
    protected void fireBindingEvent(BindingEvent evt) {
        if (bind.isEnabled())
            support.fireEvent(evt);
    }
    
    protected void fireBindingEvent(Object source) {
        fireBindingEvent(new BindingEvent(source, bind));
    }
    
    protected void fireBindingEvent() {
        fireBindingEvent(this);
    }
}
