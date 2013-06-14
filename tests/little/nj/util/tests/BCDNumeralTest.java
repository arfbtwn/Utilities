/**
 * Copyright (C) 2013 Nicholas J. Little <arealityfarbetween@googlemail.com>
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
package little.nj.util.tests;

import static little.nj.adts.BCDNumeral.PAD_RIGHT;
import static little.nj.adts.BCDNumeral.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

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
        Config conf = new Config().setCompress(false)
                .setJustification(Justification.LEFT).setLengths(1, 0);
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
        int origin = data.toArray().length, half = origin % 2 == 0 ? origin / 2
                : origin / 2 + 1;
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
        System.out.println("---- Encode");
        BCDNumeral bcd = new BCDNumeral(intBCD, data.getConfig());
        System.out.println(bcd.toByteString());
        assertTrue(String.format("Invalid Return: %s", bcd.toByteString()),
                Arrays.deepEquals(arrBCD, bcd.toArray()));
        System.out.println("---- Long Conversion Test");
        result = BCDNumeral.BCDNArrayToInteger(arrBCD, false);
        System.out.println(result);
        assertEquals(intBCD, result);
        System.out.println();
    }

    @Test
    public void test_justification() {
        Config conf = data.getConfig().setMinLength(data.toArray().length + 1);
        System.out.println("---- Justification.RIGHT");
        conf.setJustification(Justification.RIGHT);
        System.out.println(data.toByteString());
        assertEquals(ZERO, (byte) data.toArray()[0]);
        System.out.println("---- Justification.LEFT");
        conf.setJustification(Justification.LEFT);
        System.out.println(data.toByteString());
        assertEquals(PAD_RIGHT,
                (byte) data.toArray()[data.toArray().length - 1]);
        System.out.println();
    }

    @Test
    public void test_length() {
        final String ERR_LENGTH = "Length Error. " + "Min: %d, " + "Max: %d, "
                + "Length: %d";
        int min = 1, max = 0, norm = data.toArray().length;
        Config conf = data.getConfig();
        min = 10;
        System.out.println("---- Set Min: " + min);
        data.getConfig().setMinLength(min);
        System.out.println(data.toByteString());
        assertEquals(
                String.format(ERR_LENGTH, conf.getMinLength(),
                        conf.getMaxLength(), data.toArray().length),
                conf.getMinLength(), data.toArray().length);
        max = 4;
        System.out.println("---- Set Max: " + max);
        data.getConfig().setMaxLength(4);
        System.out.println(data.toByteString());
        assertTrue(
                String.format(ERR_LENGTH, conf.getMinLength(),
                        conf.getMaxLength(), data.toArray().length),
                data.toArray().length <= conf.getMaxLength());
        System.out.println("---- Set Min: 1, Max: 0");
        data.getConfig().setLengths(1, 0);
        System.out.println(data.toByteString());
        assertEquals(
                String.format(ERR_LENGTH, conf.getMinLength(),
                        conf.getMaxLength(), data.toArray().length), norm,
                data.toArray().length);
        System.out.println();
    }
}
