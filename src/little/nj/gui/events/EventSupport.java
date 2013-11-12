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
import java.util.List;

import little.nj.reflection.ReflectionUtil;

/**
 * Grand Unified Event Support
 *
 * @author Nicholas Little
 *
 */
public class EventSupport<T extends EventListener, E>
    implements IEventSupport<T> {

    private Method event = null;

    private final List<T> listeners = new ArrayList<>();

    /* (non-Javadoc)
     * @see little.nj.gui.events.IEventSupport#addListener(java.lang.Object)
     */
    @Override
    public final synchronized void addListener(T aListener) {
        listeners.add(aListener);
    }

    /* (non-Javadoc)
     * @see little.nj.gui.events.IEventSupport#removeListener(java.lang.Object)
     */
    @Override
    public final synchronized void removeListener(T aListener) {
        listeners.remove(aListener);
    }

    /**
     * Fires the appropriate event method, based on
     * event argument type
     *
     * @param args
     */
    public final synchronized void fireEvent(E args) {
        Method m = getMethod(args);
        if (m != null) {
            for(T i : listeners) {

                try {
                    m.invoke(i, args);
                } catch (IllegalAccessException | IllegalArgumentException
                        | InvocationTargetException e) {

                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Gets the Method to execute on the listeners
     *
     * @param args
     * @return
     * @throws IllegalArgumentException if no method taking parameter E is found
     */
    protected Method getMethod(E args) {
        if (event == null && listeners.size() > 0) {

            Class<?> t = args.getClass();
            T listener = listeners.get(0);

            event = ReflectionUtil.getInstance().getMethodByArgs(listener, t);

            if (event == null)
                throw new IllegalArgumentException(
                        String.format(
                                "No method on '%s' receiving parameter '%s'",
                                listener.getClass(), t
                                ));

            event.setAccessible(true);
        }
        return event;
    }
}
