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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import little.nj.gui.binding.events.EventSourceImpl;
import little.nj.gui.binding.events.ItemSelectableSource;
import little.nj.gui.binding.events.JTextComponentSource;
import little.nj.gui.binding.events.JToggleButtonSource;
import little.nj.gui.binding.events.PropertyChangeSource;
import little.nj.reflection.ReflectionUtil;


public class BindingFactoryImpl implements BindingFactory {

    private static final ReflectionUtil REFLECTOR = ReflectionUtil.getInstance();
    
    private Class<?>[] classes = new Class<?>[] {
            JToggleButtonSource.class,
            ItemSelectableSource.class,
            JTextComponentSource.class,
            PropertyChangeSource.class
            };
    
    /* (non-Javadoc)
     * @see little.nj.gui.binding.IBindingFactory#create(java.lang.Object, java.lang.Object, java.lang.String, java.lang.String)
     */
    @Override
    public Binding2 create(Object src, Object dst, String smth, String dmth) {
        return new BindingImpl(src, dst, smth, dmth);
    }

    /* (non-Javadoc)
     * @see little.nj.gui.binding.IBindingFactory#createEventSource(java.lang.Object)
     */
    @Override
    public EventSourceImpl createEventSource(Binding2 bind) {
        
        /*
         * FIXME: Do something different here
         */
        
        for(Class<?> i : classes) {
            Method m = REFLECTOR.getMethod(i, "handlesBinding", Binding2.class);
            
            if (m != null) {
                try {
                    boolean handles = (boolean)m.invoke(null, bind);
                    
                    if (handles) {
                        Constructor<?> c = i.getConstructor(Binding2.class);
                        
                        EventSourceImpl event_source = 
                                (EventSourceImpl)c.newInstance(bind);
                        
                        return event_source;
                    }
                    
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException | NoSuchMethodException 
                        | SecurityException | InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return null;
    }

}
