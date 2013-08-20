package little.nj.gui.binding;


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
    
    protected Getter<? extends X> get;
    protected Setter<? super Y> set;
    
    protected Marshal<? super X, ? extends Y> marshal;
    
    protected GenericBindingImpl() 
    { 
        enabled = true;
    }
    
    public GenericBindingImpl(
            Getter<? extends X> get, 
            Setter<? super Y> set, 
            Marshal<? super X, ? extends Y> marshal
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
}
