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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.nj.gui.binding.events.BindingEvent;
import little.nj.gui.binding.events.BindingEventSource;
import little.nj.gui.binding.events.BindingListener;


/**
 * @author Nicholas Little
 *
 */
public class BindingManager {
    
    /**
     * The default source property method prefix
     */
    public static final String DEFAULT_GET_PREFIX = "get";
    
    /**
     * The default destination property method prefix
     */
    public static final String DEFAULT_SET_PREFIX = "set";

    private BindingFactory factory;
    
    private Map<Binding, Binding> bindings;
    
    private List<BindingEventSource> event_sources;
    
    /**
     * Creates a binding manager using the specified factory
     * to create bindings and event sources
     * 
     * @param factory
     */
    public BindingManager(BindingFactory factory) {
        this.factory = factory;
        this.bindings = new HashMap<>();
        this.event_sources = new ArrayList<>();
    }
    
    /**
     * Creates a two way binding between two properties using the default
     * property naming convention
     * 
     * @param x
     * @param y
     * @param clz
     * @param sprop
     * @param dprop
     */
    public void create(Object x, Object y, String sprop, String dprop) {
        create(x, y,  
               DEFAULT_GET_PREFIX + sprop,
               DEFAULT_SET_PREFIX + dprop,
               DEFAULT_GET_PREFIX + dprop,
               DEFAULT_SET_PREFIX + sprop);
    }
    
    /**
     * Creates a two way binding between explicitly named get and set
     * methods
     * 
     * @param x
     * @param y
     * @param clz
     * @param src_get
     * @param dst_set
     * @param dst_get
     * @param src_set
     */
    public void create(Object x, Object y, 
                       String src_get, String dst_set,
                       String dst_get, String src_set) {
        
        Binding2 up = factory.create(x, y, src_get, dst_set), 
                 down = factory.create(y, x, dst_get, src_set);

        bindings.put(up, down);
        bindings.put(down, up);

        up.bind();
        
        BindingEventSource up_evt = factory.createEventSource(up),
                           down_evt = factory.createEventSource(down); 

        if (up_evt != null) {
            up_evt.addBindingListener(listener);
            event_sources.add(up_evt);
        }
        
        if (down_evt != null) {
            down_evt.addBindingListener(listener);
            event_sources.add(down_evt);
        }
        
    }
    
    private final BindingListener listener = new BindingListener() {

        @Override
        public void handleBindingEvent(BindingEvent x) {
            
            Binding bind_src = x.getBinding(),
                    bind_dst = bindings.get(bind_src);
            
            bind_dst.setEnabled(false);
            bind_src.bind();
            bind_dst.setEnabled(true);
        }
        
    };
}
