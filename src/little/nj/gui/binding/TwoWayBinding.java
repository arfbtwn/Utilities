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

import static little.nj.gui.binding.BindingManager.*;

import java.lang.reflect.Method;

public class TwoWayBinding implements IBinding {
    Binding up, down;
    public TwoWayBinding(Object x, Object y,  
                         String sget, String dset,
                         String dget, String sset) {
        up = new Binding(x, y, sget, dset);
        down = new Binding(y, x, dget, sset);
    }
    
    public TwoWayBinding(Object x, Object y,  
            String sprop, String dprop) {
        this(x, y, 
             DEFAULT_GET_PREFIX + sprop,
             DEFAULT_SET_PREFIX + dprop,
             DEFAULT_GET_PREFIX + dprop,
             DEFAULT_SET_PREFIX + sprop);
    }
    
    public void bind() {
        up.bind();
    }
    
    public boolean isEnabled() {
        return up.isEnabled() && down.isEnabled();
    }
    
    public void isEnabled(boolean enabled) {
        up.isEnabled(enabled);
        down.isEnabled(enabled);
    }
    
    public Object getSrc() { return up.getSrc(); }
    public Object getDst() { return up.getDst(); }
    
    public Method getSrcMethod() { return up.getSrcMethod(); }
    public Method getDstMethod() { return up.getDstMethod(); }
}