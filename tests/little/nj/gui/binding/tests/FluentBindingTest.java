package little.nj.gui.binding.tests;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

import little.nj.core.tests.MockObjects.Ob;
import little.nj.core.tests.MockObjects.ObGeneric;
import little.nj.core.tests.MockObjects.ObGenericChangeNotify;
import little.nj.gui.binding.FluentBinding;
import little.nj.gui.binding.FluentBindingFactoryImpl;
import little.nj.gui.binding.FluentBindingImpl;
import little.nj.gui.binding.GenericBindingImpl;
import little.nj.gui.binding.GenericBindingImpl.Getter;
import little.nj.gui.binding.GenericBindingImpl.Marshal;
import little.nj.gui.binding.GenericBindingImpl.Setter;
import little.nj.gui.binding.events.EventSourceImpl;

import org.junit.Test;


public class FluentBindingTest {
    
    private static Marshal<Integer, String> intToString = 
            new Marshal<Integer, String>() {

        @Override
        public String marshal(Integer value) {
            return value.toString();
        }
    };
    
    private static Marshal<String, Integer> stringToInt = 
            new Marshal<String, Integer>() {

        @Override
        public Integer marshal(String value) {
            try {
                return Integer.decode(value);
            } catch (NumberFormatException ex) {
                return 0;
            }
            
        }
    };
    
    @Test
    public void test_Prototype() {
        final Ob ob1 = new Ob(10);
        final ObGeneric<String> ob2 = new ObGeneric<>("Hello, World");
        
        GenericBindingImpl<Integer, String> bind = new GenericBindingImpl<>(
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
        final ObGeneric<String> ob2 = new ObGeneric<>("Hello, World");
        
        FluentBindingImpl<Integer, String> bind = 
                FluentBindingImpl.bind(int.class, String.class)
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
        ObGeneric<String> ob2 = new ObGeneric<>("Hello, World");
        
        FluentBindingFactoryImpl fac = new FluentBindingFactoryImpl();
        
        FluentBinding<Integer, String> bind = 
                fac.bind(ob1, int.class, "getField", ob2, String.class, "setField");
        
        bind.via(intToString);
        
        bind.bind();
        
        assertEquals("10", ob2.getField());
        
        FluentBindingImpl<String, Integer> bind2 = 
                fac.bind(ob2, String.class, "getField", ob1, int.class, "setField");
        
        bind2.via(stringToInt);
        
        ob2.setField("20");
        
        bind2.bind();
        
        assertEquals(20, ob1.getField());
    }
    
    @Test
    public void test_Fluent3() {
        final Ob ob1 = new Ob(10);
        final ObGeneric<String> ob2 = new ObGeneric<>("Hello, World");
        
        FluentBindingFactoryImpl fac = new FluentBindingFactoryImpl();
        
        try {
            @SuppressWarnings("unused")
            FluentBindingImpl<Integer, String> bind = fac.bind(ob2, int.class, "getField", ob1, String.class, "setField");
            fail();
        } catch (ClassCastException e) {
            
        }
        
    }
    
    @Test
    public void test_Fluent4() {
        
        final Ob ob1 = new Ob(10);
        final ObGeneric<String> ob2 = new ObGeneric<>("Hello, World");
        
        FluentBindingImpl<Integer, String> bind = new FluentBindingImpl<>();
                
        bind.from(new FluentBindingFactoryImpl.GetterImpl<>(ob1, "getField", int.class))
            .to(new FluentBindingFactoryImpl.SetterImpl<>(ob2, "setField", String.class))
            .via(intToString);
        
        bind.bind();
        
        assertEquals("10", ob2.getField());
    }
    
    @Test
    public void test_Fluent_Bind_Event()
    {
        ObGenericChangeNotify<String> ob1 = new ObGenericChangeNotify<>("Hello, World");
        Ob ob2 = new Ob(10);
        
        
        FluentBindingImpl<String, Integer> bind = new FluentBindingImpl<>();
        bind.from(new FluentBindingFactoryImpl.GetterImpl<>(ob1, "getField", String.class))
            .to(new FluentBindingFactoryImpl.SetterImpl<>(ob2, "setField", int.class))
            .via(stringToInt)
            .when(new EventSourceImpl<ObGenericChangeNotify<String>>(ob1) {

                @Override
                protected void init() {
                    obj.addPropertyChangeListener(new PropertyChangeListener() {
                        
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            fireBindingEvent(evt.getSource());
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
        
        FluentBindingImpl<byte[], byte[]> bind = new FluentBindingImpl<>(byte[].class, byte[].class);
        
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
