package little.nj.gui.binding;

import little.nj.gui.binding.events.BindingEvent;
import little.nj.gui.binding.events.BindingEventSource;
import little.nj.gui.binding.events.BindingListener;


public class GenericBindingImpl<X, Y> implements Binding {
    
    public static interface Marshal<A, B> {
        B marshal(A value);
    }
    
    public static interface Getter<V> {
        V get();
    }
    
    public static interface Setter<V> {
        void set(V value);
    }
    
    private boolean enabled;
    
    protected Getter<X> get;
    protected Setter<Y> set;
    
    protected Marshal<X, Y> marshal;
    
    protected BindingEventSource events;
    
    protected GenericBindingImpl() 
    { 
        enabled = true;
    }
    
    public GenericBindingImpl(
            Getter<X> get, 
            Setter<Y> set, 
            Marshal<X, Y> marshal
            ) 
    {
        this();
        this.get = get;
        this.set = set;
        this.marshal = marshal;
    }
    
    /*
     * (non-Javadoc)
     * @see little.nj.gui.binding.IBindingMK1#bind()
     */
    @Override
    public void bind() {
        if (!isEnabled())
            return;
        
        X value1 = get.get();
        Y value2 = marshal.marshal(value1);
        set.set(value2);
    }

    /* (non-Javadoc)
     * @see little.nj.gui.binding.IBindingMK1#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return enabled &&
                ( get != null && set != null ) &&
                ( marshal != null );
    }

    /* (non-Javadoc)
     * @see little.nj.gui.binding.IBindingMK1#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setEventSource(BindingEventSource events) {
        if (this.events != null)
            events.removeBindingListener(listener);
        
        this.events = events;
        
        if (this.events != null)
            events.addBindingListener(listener);
    }
    
    private BindingListener listener = new BindingListener() {

        @Override
        public void handleBindingEvent(BindingEvent x) {
            bind();
        }
        
    };
}
