package little.nj.gui.binding.tests;

import little.nj.core.MockObjects.Ob;
import little.nj.core.MockObjects.ObGeneric;
import little.nj.core.MockObjects.ObGenericChangeNotify;
import little.nj.gui.binding.*;
import little.nj.gui.binding.GenericBinding.*;
import little.nj.gui.binding.Bindings;
import little.nj.gui.binding.StdComponents.IntToStringMarshal;
import little.nj.gui.binding.StdComponents.StringToIntMarshal;
import little.nj.gui.binding.events.AbstractEventSource;

import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class FluentBindingTest {

    private static Marshal<Integer, String> intToString = new IntToStringMarshal();

    private static Marshal<String, Integer> stringToInt = new StringToIntMarshal();

    @Test
    public void test_Prototype() {
        final Ob ob1 = new Ob(10);
        final ObGeneric<String> ob2 = new ObGeneric<String>("Hello, World");

        GenericBinding<Integer, String> bind = new GenericBinding<Integer, String>(
                new Getter<Integer>() {
                    @Override
                    public Integer get() {
                        return ob1.getField();
                    } },
                new Setter<String>() {
                    @Override
                    public void set(String value) {
                        ob2.setField(value);
                    } },
                intToString
                );

        bind.bind();

        assertEquals("10", ob2.getField());
    }

    @Test
    public void test_Fluent() {
        final Ob ob1 = new Ob(10);
        final ObGeneric<String> ob2 = new ObGeneric<String>("Hello, World");

        FluentBinding<Integer, String> bind =
                Bindings.bind(int.class, String.class)
                    .from(new Getter<Integer>() {

                        @Override
                        public Integer get() {
                            return ob1.getField();
                        } })
                    .to(new Setter<String>() {

                        @Override
                        public void set(String value) {
                            ob2.setField(value);
                        } })
                    .via(intToString);

        bind.bind();

        assertEquals("10", ob2.getField());
    }

    @Test
    public void test_Fluent2() {
        Ob ob1 = new Ob(10);
        ObGeneric<String> ob2 = new ObGeneric<String>("Hello, World");

        FluentBindingFactory fac = Bindings.getDefaultFactory();

        FluentBinding<Integer, String> bind =
                fac.bind(ob1, int.class, "getField", ob2, String.class, "setField");

        bind.via(intToString);

        bind.bind();

        assertEquals("10", ob2.getField());

        FluentBinding<String, Integer> bind2 =
                fac.bind(ob2, String.class, "getField", ob1, int.class, "setField");

        bind2.via(stringToInt);

        ob2.setField("20");

        bind2.bind();

        assertEquals(20, ob1.getField());
    }

    @Test
    public void test_Fluent3() {
        final Ob ob1 = new Ob(10);
        final ObGeneric<String> ob2 = new ObGeneric<String>("Hello, World");

        FluentBindingFactory fac = Bindings.getDefaultFactory();

        try {
            @SuppressWarnings("unused")
            FluentBinding<Integer, String> bind = fac.bind(ob2, int.class, "getField", ob1, String.class, "setField");
            fail();
        } catch (ClassCastException e) {

        }

    }

    @Test
    public void test_Fluent_Bind_Event()
    {
        ObGenericChangeNotify<String> ob1 = new ObGenericChangeNotify<String>("Hello, World");
        Ob ob2 = new Ob(10);


        @SuppressWarnings("unused")
        FluentBinding<String, Integer> bind = Bindings.getDefaultFactory()
            .bind(ob1, String.class, "getField", ob2, int.class, "setField")
            .via(stringToInt)
            .when(new AbstractEventSource<ObGenericChangeNotify<String>>(ob1) {

                @Override
                protected void init(ObGenericChangeNotify<String> obj) {
                    obj.addPropertyChangeListener(new PropertyChangeListener() {

                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            fireBindingEvent();
                        }
                    });
                }
            });

        ob1.setField("35");

        assertEquals(35, ob2.getField());

    }

    @Test
    public void test_Bind_Primitive_Array() {

        class ObPrim {
            byte[] field;
            byte[] getField() { return field; }
            void setField(byte[] value) { field = value; }
        }

        final ObPrim ob1 = new ObPrim(),
                     ob2 = new ObPrim();

        final byte[] field1 = new byte[] { 0x0, 0x1, 0x2, 0x3 },
                     field2 = new byte[] { 0x3, 0x2, 0x1, 0x0 };

        ob1.setField(field1);
        ob2.setField(field2);

        FluentBinding<byte[], byte[]> bind = Bindings.bind(byte[].class, byte[].class);

        bind.from(new Getter<byte[]>() {
            @Override
            public byte[] get() {
                return ob1.getField();
            }
        }).to(new Setter<byte[]>() {
            @Override
            public void set(byte[] value) {
                ob2.setField(value);
            }
        }).via(new Marshal<byte[], byte[]>() {
            @Override
            public byte[] marshal(byte[] value) {
                return Arrays.copyOf(value, value.length);
            } });

        bind.bind();

        assertEquals(true, Arrays.equals(field1, ob2.getField()));

    }
}
