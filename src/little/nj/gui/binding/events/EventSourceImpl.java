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

import little.nj.gui.events.EventSupport;


public abstract class EventSourceImpl<T> implements BindingEventSource {
    
    private final EventSupport<BindingListener, BindingEvent> support;
    protected final T obj;
    
    public EventSourceImpl(T src) {
        support = new EventSupport<>();
        
        obj = src;
        
        init();
    }
    
    protected abstract void init();
    
    public void addBindingListener(BindingListener listener) {
        support.addEventListener(listener);
    }
    
    public void removeBindingListener(BindingListener listener) {
        support.removeEventListener(listener);
    }
    
    /**
     * FIXME: I'd like to narrow this to EventObject, but the concrete
     * class is trouble with interface typed events
     * 
     * @param event
     */
    protected void fireBindingEvent(Object event) {
        support.fireEvent(new BindingEvent(event));
    }
}
