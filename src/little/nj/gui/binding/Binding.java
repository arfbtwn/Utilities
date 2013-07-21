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

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import little.nj.gui.binding.events.IBindingEventSource;
import little.nj.reflection.ReflectionUtil;

@SuppressWarnings({"rawtypes", "unchecked"})

/**
 * A class to bind two objects
 * 
 * @author Nicholas Little
 *
 */
public class Binding implements IBinding {

    private static final ReflectionUtil REFLECTOR = ReflectionUtil.getInstance();
    
    private static final String ERR_METHOD = "Invalid Method '%s' on '%s'"; 
    
    private boolean disabled;
    
    private Method msget, mdset;
    
    ITypeConverter converter;

    private Object value;
    
    private WeakReference<Object> src;
    
    private WeakReference<Object> dst;

    /**
     * Creates a binding between the properties on two objects
     * 
     * @param src the source object
     * @param dst the destination object
     * @param clz the type of the properties to bind
     * @param sget the name of the source get method
     * @param dset the name of the destination set method
     */
    public Binding(Object src, Object dst, 
                   String sget, String dset) {
        
        cfgSrc(src, sget);
        cfgDst(dst, dset);
    }
    
    /**
     * Retrieves the source object, or null
     * @return {@link WeakReference#get()}
     */
    public Object getSrc() { return src.get(); }
    
    /**
     * Retrieves the destination object, or null 
     * 
     * @return {@link WeakReference#get()} 
     */
    public Object getDst() { return dst.get(); }

    
    public Method getSrcMethod() { return msget; }
    
    public Method getDstMethod() { return mdset; }
    
    /**
     * Configures the source of the binding.
     */
    protected void cfgSrc(Object src, String sget) {        
        if (src != null)
            msget = REFLECTOR.getMethod(src, sget);
        
        if (msget == null)
            throw new BindingException(
                    String.format(ERR_METHOD, sget, src.getClass().getName())
                                      );
        
        this.src = new WeakReference<>(src);
    }
    
    /**
     * Configures the destination of the binding.
     */
    protected void cfgDst(Object dst, String dset) {        
        if (dst != null)
            mdset = REFLECTOR.getMethodByName(dst, dset);
        
        if (mdset == null)
            throw new BindingException(
                    String.format(ERR_METHOD, dset, dst.getClass().getName())
                    );
        
        if (mdset.getParameterTypes().length != 1)
            throw new BindingException();
        
        this.dst = new WeakReference<>(dst);
    }
    
    /**
     * Transfers the value from the source to the destination
     */
    public void bind() {
        if (!isEnabled() || !ready())
            return;

        try {
            value = msget.invoke(getSrc(), (Object[]) null);
            
            /*
             * TODO: Call out for marshaling between types here
             */
            if (converter != null)
                value = converter.convert(value);
            
            mdset.invoke(getDst(), value);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isEnabled() {
        return !disabled;
    }
    
    public void isEnabled(boolean enabled) {
        disabled = !enabled;
    }

    /**
     * Checks whether the binding is ready
     * @return true, if objects and methods are not null
     */
    public boolean ready() {
        return (getSrc() != null && getDst() != null) && 
                (msget != null && mdset != null);
    }

    public IBindingEventSource getEventSource() { return null; }

    public void setEventSource(IBindingEventSource source) { }

    public ITypeConverter getConverter() { return converter; }
    
    public void setConverter(ITypeConverter x) { converter = x; }
}
