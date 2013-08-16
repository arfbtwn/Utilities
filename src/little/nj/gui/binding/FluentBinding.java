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

import little.nj.gui.binding.GenericBindingImpl.Getter;
import little.nj.gui.binding.GenericBindingImpl.Marshal;
import little.nj.gui.binding.GenericBindingImpl.Setter;
import little.nj.gui.binding.events.BindingEventSource;


public interface FluentBinding<X, Y> extends Binding {
    
    // TODO: Assess type parameters
    
    /**
     * The get accessor
     * 
     * @param get
     * @return
     */
    FluentBinding<X, Y> from(Getter<X> get);
    
    /**
     * The set mutator
     * 
     * @param set
     * @return
     */
    FluentBinding<X, Y> to(Setter<Y> set);
    
    /**
     * The marshaling component
     * 
     * @param marshal
     * @return
     */
    FluentBinding<X, Y> via(Marshal<X, Y> marshal);
    
    /**
     * The event source
     * 
     * @param source
     * @return
     */
    FluentBinding<X, Y> when(BindingEventSource source);
    
    /**
     * The twin binding
     * 
     * @param twin
     * @return
     */
    FluentBinding<X, Y> twin(FluentBinding<Y, X> twin);
}
