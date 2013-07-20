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
package little.nj.gui.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import little.nj.util.ReflectionUtil;

/**
 * @author Nicholas Little
 *
 */
public final class EventSupport<T extends EventListener, E extends EventObject> 
    implements IEventSupport<T, E> {

    Method event = null;
    
    List<T> listeners = new ArrayList<>();
    
    /* (non-Javadoc)
     * @see little.nj.gui.events.IEventSupport#addEventListener(java.lang.Object)
     */
    @Override
    public final synchronized void addEventListener(T aListener) {
        listeners.add(aListener);
    }

    /* (non-Javadoc)
     * @see little.nj.gui.events.IEventSupport#removeEventListener(java.lang.Object)
     */
    @Override
    public final synchronized void removeEventListener(T aListener) {
        listeners.remove(aListener);
    }
    
    public final synchronized void fireEvent(E args) {
        if (findMethod(args)) {
            for(T i : listeners) {
                
                try {
                    event.invoke(i, args);
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
    }
    
    private boolean findMethod(E args) {
        if (event == null && listeners.size() > 0) {
            
            Class<?> t = args.getClass();
            T listener = listeners.get(0);
            
            event = ReflectionUtil.getInstance().getMethodByArgs(listener, t);
        }
        return event != null;
    }
    
}
