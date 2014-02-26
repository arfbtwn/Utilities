/**
 * Copyright (C) 2013 
 * Nicholas J. Little <arealityfarbetween@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.adts.tests;

import static little.nj.adts.BCDNumeral.PAD_RIGHT;
import static little.nj.adts.BCDNumeral.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import little.nj.adts.BCDNumeral;
import little.nj.adts.BCDNumeral.Config;
import little.nj.adts.BCDNumeral.Justification;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author nicholas
 * 
 */
public class BCDNumeralTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    public BCDNumeral data;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        Config conf = new Config();
        data = new BCDNumeral(1234567L, conf);
        System.out.println("---- Initial State");
        System.out.println(data.toByteString());
        System.out.println(conf.toString());
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_compression() {
        data.getConfig().setCompress(false);
        
        int origin = data.toArray().length, 
            half = origin / 2 + origin % 2;
        
        System.out.println("---- Compress");
        data.getConfig().setCompress(true);
        System.out.println(data.toByteString());
        assertEquals(half, data.toArray().length);
        System.out.println("---- Decompress");
        data.getConfig().setCompress(false);
        System.out.println(data.toByteString());
        assertEquals(origin, data.toArray().length);
        System.out.println();
    }

    @Test
    public void test_encoding() {
        long intBCD = data.longValue(), result;
        Byte[] arrBCD = new Byte[] { 1, 2, 3, 4, 5, 6, 7 };
        Config conf = data.getConfig().setCompress(false);
        
        System.out.println("---- Encode");
        BCDNumeral bcd = new BCDNumeral(intBCD, conf);
        
        System.out.println(bcd.toByteString());
        assertArrayEquals(arrBCD, bcd.toArray());
        
        System.out.println("---- Long Conversion Test");
        result = BCDNumeral.BCDNArrayToInteger(arrBCD, conf.isCompress());
        
        System.out.println(result);
        assertEquals(intBCD, result);
        System.out.println();
    }

    @Test
    public void test_justification() {
        Config conf = data.getConfig().setLength(data.toArray().length + 1);
        System.out.println("---- Justification.RIGHT");
        conf.setJustification(Justification.RIGHT);
        System.out.println(data.toByteString());
        assertEquals(ZERO, (byte) data.toArray()[0]);
        System.out.println("---- Justification.LEFT");
        conf.setJustification(Justification.LEFT);
        System.out.println(data.toByteString());
        
        Byte[] bs = data.toArray();
        
        assertEquals(Byte.valueOf(PAD_RIGHT), bs[bs.length - 1]);
        
        System.out.println();
    }

    @Test
    public void test_length() {
        Config conf = data.getConfig();
        int norm = data.toArray().length;
        
        int len = 4;
        System.out.println("---- Set Len: " + len);
        conf.setLength(len);
        System.out.println(data.toByteString());
        assertEquals(conf.getLength(), data.toArray().length);
        
        len = 0;
        System.out.println("---- Set Len: " + len);
        conf.setLength(len);
        System.out.println(data.toByteString());
        assertEquals(norm, data.toArray().length);
        
        System.out.println();
    }
    
    @Test
    public void test_encode_zero() {
        Config conf = data.getConfig();
        
        Byte[] arr = BCDNumeral.IntegerToBCDNArray(0, conf);
        
        assertEquals(1, arr.length);
        assertEquals(0, (byte)arr[0]);
    }
}
