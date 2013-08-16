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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import little.nj.reflection.ReflectionUtil;
import little.nj.gui.binding.Binding2;

/**
 * @author Nicholas Little
 *
 */
public class PropertyChangeSource extends EventSourceImpl {

    private final static ReflectionUtil REFLECTOR = ReflectionUtil.getInstance();
    
    public static boolean handlesBinding(Binding2 bind) {
        
        Method listen = REFLECTOR.getMethod(bind.getSrc(), "addPropertyChangeListener", 
                PropertyChangeListener.class);
        
        return listen != null;
        
    }
    
    /**
     * @param obj
     * @param binding
     */
    public PropertyChangeSource(Binding2 binding) {
        super(binding, binding.getSrc());
    }
    
    @Override
    protected void init() {
        
        Method listen = REFLECTOR.getMethod(obj, "addPropertyChangeListener", 
                                  PropertyChangeListener.class);

        if (listen == null)
            return;
        
        try {
            listen.invoke(obj, new PropertyChangeListener() {
            
                @Override
                public void propertyChange(PropertyChangeEvent e) {
                    fireBindingEvent(e);
                } 
              
            });
        } catch (IllegalAccessException | IllegalArgumentException
          | InvocationTargetException e) {
            e.printStackTrace();
        } 
    }

}
