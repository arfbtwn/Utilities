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
package little.nj.gui.binding;

import little.nj.gui.binding.events.BindingEventSource;


public class FluentBindingImpl<X, Y> extends GenericBindingImpl<X, Y> implements FluentBinding<X, Y> {

    public final static <X, Y> FluentBindingImpl<X, Y> bind(Class<X> sample1, Class<Y> sample2) 
    {
        return new FluentBindingImpl<>();
    }

    public FluentBindingImpl() { }
    
    public FluentBindingImpl(Class<X> sample1, Class<Y> sample2) { }
    
    public FluentBindingImpl<X, Y> from(Getter<X> get) {
        this.get = get;
        return this;
    }
    
    public FluentBindingImpl<X, Y> to(Setter<Y> set) {
        this.set = set;
        return this;
    }
    
    public FluentBindingImpl<X, Y> via(Marshal<X, Y> marshal) {
        this.marshal = marshal;
        return this;
    }
    
    public FluentBindingImpl<X, Y> when(BindingEventSource events) {
        setEventSource(events);
        return this;
    }
    
    
}
