package little.nj.gui.binding;

import little.nj.gui.binding.GenericBindingImpl.*;
import little.nj.gui.binding.StdComponents.GetterImpl;
import little.nj.gui.binding.StdComponents.SetterImpl;

public class FluentBindingFactoryImpl implements FluentBindingFactory {

    protected <V> Getter<V> createGet(Object x, Class<V> sample, String method)
    {
        return new GetterImpl<V>(x, method, sample);
    }

    protected <V> Setter<V> createSet(Object x, Class<V> sample, String method)
    {
        return new SetterImpl<V>(x, method, sample);
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
