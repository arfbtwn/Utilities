package little.nj.gui.binding;

import little.nj.gui.binding.GenericBindingImpl.Getter;
import little.nj.gui.binding.GenericBindingImpl.Setter;
import little.nj.gui.binding.GenericBindingImpl.Marshal;
import little.nj.reflection.ReflectionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StdComponents
{
    public abstract static class ReflectingImpl {
        protected final Object obj;
        protected final String mth;
        protected final Class<?> sample;
        protected Method mth_inf;

        protected ReflectingImpl(Object x, String method, Class<?> sample)
        {
            this.obj = x;
            this.mth = method;
            this.sample = sample;

            testOrThrow();
        }

        private void testOrThrow()
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

    public static class ThroughMarshal<T> implements Marshal<T, T>
    {
        /* (non-Javadoc)
         * @see little.nj.gui.binding.GenericBindingImpl.Marshal#marshal(java.lang.Object)
         */
        @Override
        public T marshal(T value) {
            return value;
        }
    }

    public static class IntToStringMarshal implements Marshal<Integer, String>
    {
        @Override
        public String marshal(Integer value)
        {
            return String.valueOf(value);
        }
    }

    public static class StringToIntMarshal implements Marshal<String, Integer>
    {
        @Override
        public Integer marshal(String value)
        {
            try {
                return Integer.decode(value);
            } catch (NumberFormatException ex) {
                return 0;
            }
        }
    }
}
