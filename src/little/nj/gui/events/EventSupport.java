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

import little.nj.reflection.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Grand Unified Event Support
 *
 * Contains a Set of listener type T, uses reflection
 * to associate the event handler with a signal argument.
 *
 * This class is externally threadsafe
 *
 * @author Nicholas Little
 */
public class EventSupport<T extends EventListener, V> {

    private final Set<T> listeners = new HashSet<T>();

    public final synchronized void addListener(T aListener) {
        listeners.add(aListener);
    }

    public final synchronized void removeListener(T aListener) {
        listeners.remove(aListener);
    }

    /**
     * Fires the appropriate event method, based on
     * event argument type
     *
     * @param args
     */
    public final synchronized void fireEvent(V arg) {
        for(T i : listeners) {
            Method m = getMethod(i, arg);

            if (null == m)
            {
                throw new RuntimeException();
            }

            try {
                m.setAccessible(true);
                m.invoke(i, arg);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException(e1);
            } catch (IllegalArgumentException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RuntimeException(e3);
            }

        }
    }

    /**
     * Gets the Method to execute on the listeners
     *
     * @param listener
     * @param args
     * @return null if no method was found
     */
    protected Method getMethod(T listener, V arg) {
        return ReflectionUtil
            .getInstance()
            .getMethodByArgs(listener, arg.getClass());
    }
}
