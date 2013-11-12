/**
 * Copyright (C) 2013
 * Nicholas J. Little <arealityfarbetween@googlemail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.util;

import javax.xml.bind.DatatypeConverter;


/**
 * @author Nicholas Little
 *
 */
public class ConversionUtil {

    private ConversionUtil() { }

    /**
     * A mask to removeListener sign bits from a single byte.
     */
    public final static int MASK_BYTE_UNSIGN = 0xff;

    /**
     * Convert an unsigned byte to an int.
     *
     * @param i
     *            byte to convert
     * @return int Unsigned value
     */
    public final static int unsign(byte i) {
        return i & MASK_BYTE_UNSIGN;
    }

    /**
     * IIRC, these two object types don't autobox/unbox.
     *
     * @param abPrim
     *            Primitive byte[]
     * @return Byte[]
     */
    public final static Byte[] convert(byte[] abPrim) {
        Byte[] abObjs = new Byte[abPrim.length];
        int j = 0;
        for (byte i : abPrim)
            abObjs[j++] = i;
        return abObjs;
    }

    /**
     * IIRC, these two object types don't autobox/unbox.
     *
     * @param abObjs
     *            Object Byte[]
     * @return byte[]
     */
    public final static byte[] convert(Byte[] abObjs) {
        byte[] abPrim = new byte[abObjs.length];
        int j = 0;
        for (byte i : abObjs)
            abPrim[j++] = i;
        return abPrim;
    }

    /**
     * Wrapper around {@link DatatypeConverter#printHexBinary(byte[])} to
     * prevent {@link NullPointerException}
     *
     * @param in
     *            byte[] to format as a string
     * @return Hexadecimal output string, or &quot;null&quot;
     */
    public final static String printHexBinary(byte[] in) {
        return in == null ? "null" : DatatypeConverter.printHexBinary(in);
    }
}
