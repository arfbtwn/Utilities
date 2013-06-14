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
package little.nj.gui.binding.tests;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A set of mock objects to test the binding infrastructure
 * 
 * @author Nicholas Little
 *
 */
public class MockObjects {

    /*
     * @formatter: off
     */
    
    /**
     * A basic class containing an integer primitive
     *
     */
    public static class Ob {
        public Ob(int x) { field = x; }
        private int field;
        public int getField() { return field; }
        public void setField(int x) { field = x; }
    }

    /**
     * A generic class of type T
     *
     * @param <T>
     */
    public static class ObGeneric<T> {
        public ObGeneric(T x) { field = x; }
        private T field;
        public T getField() { return field; }
        public void setField(T x) { field = x; }
    }
    
    /**
     * A basic class supporting change notification
     *
     */
    public static class ObChangeNotify extends Ob {
        public ObChangeNotify(int x) { super(x); }
        public void setField(int x) {
            int old = super.getField();
            super.setField(x);
            firePropertyChange("Field", old, x);
        }
        public synchronized void addPropertyChangeListener(PropertyChangeListener x) {
            list.add(x);
        }
        public synchronized void removePropertyChangeListener(PropertyChangeListener x) {
            list.remove(x);
        }
        private synchronized void firePropertyChange(String property, 
                                                     Object old, 
                                                     Object nnew) {
            PropertyChangeEvent evt = new PropertyChangeEvent(this, property, old, nnew);
            for(PropertyChangeListener i : list)
                i.propertyChange(evt);
        }
        List<PropertyChangeListener> list = new ArrayList<>();
    }
    
    /**
     * A generic class of type T supporting change notification
     *
     * @param <T>
     */
    public static class ObGenericChangeNotify<T> extends ObGeneric<T> {
        public ObGenericChangeNotify(T x) { super(x); }
        public void setField(T x) {
            T old = super.getField();
            super.setField(x);
            firePropertyChange("Field", old, x);
        }
        public synchronized void addPropertyChangeListener(PropertyChangeListener x) {
            list.add(x);
        }
        public synchronized void removePropertyChangeListener(PropertyChangeListener x) {
            list.remove(x);
        }
        private synchronized void firePropertyChange(String property, 
                                                     Object old, 
                                                     Object nnew) {
            PropertyChangeEvent evt = new PropertyChangeEvent(this, property, old, nnew);
            for(PropertyChangeListener i : list)
                i.propertyChange(evt);
        }
        List<PropertyChangeListener> list = new ArrayList<>();
    }
}
