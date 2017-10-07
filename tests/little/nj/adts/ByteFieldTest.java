package little.nj.adts;

import static org.junit.Assert.*;

import java.nio.charset.Charset;

import little.nj.adts.ByteField;
import little.nj.adts.ByteFieldMapSet;
import little.nj.adts.IntByteField;
import little.nj.adts.StringByteField;

import org.junit.Test;


public class ByteFieldTest {

    @Test
    public void test_Set_Clone() {
        final Charset chs = Charset.forName("US-ASCII");

        ByteFieldMapSet bfms1 = new ByteFieldMapSet();

        StringByteField bf11 = new StringByteField(32, "Test String1", chs);
        StringByteField bf12 = new StringByteField(32, "Test String2", chs);
        IntByteField bf13 = new IntByteField("Test Int1", 10);

        bfms1.add(bf11);
        bfms1.add(bf12);
        bfms1.add(bf13);

        int of11 = bf11.getOffset(),
            of12 = bf12.getOffset(),
            of13 = bf13.getOffset();

        ByteFieldMapSet bfms2 = bfms1.clone();

        StringByteField bf21 = (StringByteField)bfms2.get(of11);
        StringByteField bf22 = (StringByteField)bfms2.get(of12);
        IntByteField    bf23 = (IntByteField)bfms2.get(of13);

        assertNotSame(bfms1, bfms2);

        assertNotSame(bf11, bf21);
        assertNotSame(bf12, bf22);
        assertNotSame(bf13, bf23);

        assertEquals(bf11.getValue(), bf21.getValue());
        assertEquals(bf13.getValue(), bf23.getValue());

        bf13.setValue(bf23.getValue() + 10);

        assertFalse(bf13.getValue() == bf23.getValue());
    }

    @Test
    public void testClone() {

        IntByteField x = new IntByteField("Field: x");

        x.setValue(10);

        ByteField y = x.clone();

        assertNotSame(x, y);

        assertEquals(x.getOffset(), y.getOffset());
        assertEquals(x.getName(), y.getName());

        assertArrayEquals(x.getBytes(), y.getBytes());

        assertTrue(y instanceof IntByteField);

        assertEquals(x.getValue(), ((IntByteField)y).getValue());
    }

}
