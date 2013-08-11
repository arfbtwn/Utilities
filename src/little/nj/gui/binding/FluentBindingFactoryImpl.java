package little.nj.gui.binding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import little.nj.gui.binding.GenericBindingImpl.*;
import little.nj.reflection.ReflectionUtil;

public class FluentBindingFactoryImpl implements FluentBindingFactory {

    private abstract static class ReflectingImpl {
        protected final Object obj; protected final String mth;
        protected final Class<?> sample;
        protected Method mth_inf;
        
        protected ReflectingImpl(Object x, String method, Class<?> sample)
        {
            obj = x;
            mth = method;
            this.sample = sample;
            
            testOrThrow();
        }
        
        private final void testOrThrow()
        {
            mth_inf = ReflectionUtil.getInstance().getMethodByName(obj, mth);
            
            if (mth_inf == null)
                throw new RuntimeException();
                
            mth_inf.setAccessible(true);
            
            testOrThrowImpl();
        }
        
        protected abstract void testOrThrowImpl();
    }
    
    public static class GetterImpl<V> extends ReflectingImpl implements Getter<V>
    {
        public GetterImpl(Object x, String method, Class<V> sample) 
        { 
            super(x, method, sample);
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public V get() {
            try {
                return (V) mth_inf.invoke(obj, (Object[]) null);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        protected void testOrThrowImpl() {            
            Class<?> rt = mth_inf.getReturnType();
            
            sample.asSubclass(rt);
        }
    }
    
    public static class SetterImpl<V> extends ReflectingImpl implements Setter<V>
    {
        public SetterImpl(Object x, String method, Class<V> sample) 
        { 
            super(x, method, sample);
        }

        @Override
        public void set(V value) {
            try {
                mth_inf.invoke(obj, value);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        protected void testOrThrowImpl() {            
            Class<?> param = mth_inf.getParameterTypes()[0];
            
            sample.asSubclass(param);
        }
    }
    
    protected <V> Getter<V> createGet(Object x, Class<V> sample, String method)
    {
        return new GetterImpl<>(x, method, sample);
    }
    
    protected <V> Setter<V> createSet(Object x, Class<V> sample, String method)
    {
        return new SetterImpl<>(x, method, sample);
    }
    
    /* (non-Javadoc)
     * @see little.nj.gui.binding.IFluentBindingFactory#bind(java.lang.Object, java.lang.Class, java.lang.String, java.lang.Object, java.lang.Class, java.lang.String)
     */
    @Override
    public <X, Y> FluentBindingImpl<X, Y> bind(
            Object btm, Class<X> btm_sample, String btm_method,
            Object top, Class<Y> top_sample, String top_method)
    {
        Getter<X> get = createGet(btm, btm_sample, btm_method);
        Setter<Y> set = createSet(top, top_sample, top_method); 
        
        return new FluentBindingImpl<X, Y>().from(get).to(set);
    }
}
