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

import little.nj.reflection.ReflectionUtil;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PropertyChangeSource extends AbstractEventSource<Object>
{

    private final static ReflectionUtil REFLECTOR = ReflectionUtil.getInstance();

    public PropertyChangeSource(Object source) {
        super(source);
    }

    @Override
    protected void init(Object obj) {

        Method listen = REFLECTOR.getMethodByArgs(obj,
                                  PropertyChangeListener.class);

        if (listen == null)
            return;

        try {
            listen.invoke(obj, listener);
        } catch (IllegalAccessException e1) {
        	throw new RuntimeException(e1);
        } catch (IllegalArgumentException e2) {
        	throw new RuntimeException(e2);
        } catch (InvocationTargetException e3) {
            throw new RuntimeException(e3);
        }
    }

    private PropertyChangeListener listener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            fireBindingEvent();
        }

    };
}
