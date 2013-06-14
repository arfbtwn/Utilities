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
package little.nj.adts;

import java.util.Collections;
import java.util.LinkedList;

import javax.xml.bind.DatatypeConverter;

/**
 * <p>
 * A class to store and represent <strong>Binary Coded Decimal
 * Numerals</strong>. The class will delay (re)creating the Byte[] until
 * required at instantiation, after serialisation, and configuration changes.
 * </p>
 * 
 * <p>
 * To force a refresh, run {@link #refresh(boolean...)}.
 * </p>
 * 
 * <h4>History</h4>
 * <dl>
 * <dt>0.1</dt>
 * <dd>Initial Revision
 * <dd>
 * <dt>0.2</dt>
 * <dd>Updated to support left justification. Fixed compression bugs.</dd>
 * </dl>
 * 
 * <h4>Todo</h4>
 * <ul>
 * <li>Test incoming Byte[] for Compression</li>
 * </ul>
 * 
 * @author Nicholas Little
 * @version 0.2
 * 
 */
@SuppressWarnings("serial")
public class BCDNumeral extends Number implements Comparable<BCDNumeral> {

    /**
     * Configuration object. Initialised to No Maximum Byte Length Limit, RIGHT
     * justification, Compressed
     * 
     */
    public static class Config {

        /**
         * Default: Compression = true
         */
        private boolean       compressed    = true;

        /*
         * Internal State variable
         */
        private boolean       has_changed   = true;

        /**
         * Default: Justification = Justification.RIGHT
         */
        private Justification justification = Justification.RIGHT;

        /**
         * Default: Max Length = 0
         */
        private int           max_length    = 0;

        /**
         * Default: Min Length = 1
         */
        private int           min_length    = 1;

        /**
         * @return the justification
         */
        public Justification getJustification() {
            return justification;
        }

        /**
         * @return the max_length
         */
        public int getMaxLength() {
            return max_length;
        }

        /**
         * @return the min_length
         */
        public int getMinLength() {
            return min_length;
        }

        /**
         * @return the has_changed
         */
        public boolean isChanged() {
            return has_changed;
        }

        /**
         * @return the compress
         */
        public boolean isCompress() {
            return compressed;
        }

        /**
         * Set Compression
         * 
         * @param c
         *            Compression state
         * @return This Configuration Object
         */
        public Config setCompress(boolean c) {
            if (compressed != c) {
                has_changed = true;
                compressed = c;
            }
            return this;
        }

        /**
         * Set Justification
         * 
         * @param j
         *            Justification.{RIGHT|LEFT}
         * @return This Configuration Object
         */
        public Config setJustification(Justification j) {
            if (justification != j) {
                has_changed = true;
                justification = j;
            }
            return this;
        }

        /**
         * Set Min & Max Lengths
         * 
         * @see #setMinLength(int)
         * @see #setMaxLength(int)
         * @return This Configuration Object
         */
        public Config setLengths(int... limits) {
            if (limits.length > 0)
                setMinLength(limits[0]);
            if (limits.length > 1)
                setMaxLength(limits[1]);
            return this;
        }

        /**
         * Set Maximum Byte Length
         * 
         * @param i
         *            Maximum Byte Length
         * @return This Configuration Object
         */
        public Config setMaxLength(int i) {
            if (max_length != i) {
                has_changed = true;
                max_length = i < 0 ? 0 : i;
                if (max_length > 0 && max_length < min_length)
                    setMinLength(max_length);
            }
            return this;
        }

        /**
         * Set Minimum Byte Length
         * 
         * @param i
         *            Minimum Byte Length
         * @return This Configuration Object
         */
        public Config setMinLength(int i) {
            if (min_length != i) {
                has_changed = true;
                min_length = i <= 0 ? 1 : i;
                if (max_length > 0 && min_length > max_length)
                    setMaxLength(min_length);
            }
            return this;
        }

        /**
         * Return a human readable string representation of this configuration
         */
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Justification:  " + justification + "\n");
            sb.append("Compression:    " + compressed + "\n");
            sb.append("Minimum Length: " + min_length + "\n");
            sb.append("Maximum Length: " + max_length + "\n");
            return sb.toString();
        }
    }

    /**
     * Justification of the resulting Byte[]. RIGHT pads with 0x00 to the Left,
     * LEFT pads with 0xFF to the Right
     * 
     */
    public static enum Justification {
        LEFT, RIGHT
    }

    /**
     * Mask Left 4 Bits
     */
    public static final byte MASK_LEFT  = 0xF;

    /**
     * Mask right 4 Bits
     */
    public static final byte MASK_RIGHT = (byte) 0xF0;

    /**
     * Right Padding Byte
     */
    public static final byte PAD_RIGHT  = (byte) 0xFF;

    /**
     * Zero
     */
    public static final byte ZERO       = 0x0;

    /**
     * Compress Incoming Byte Array
     * 
     * @param in
     *            Uncompressed Byte Array to Compress
     * @return Compressed BCD Byte[]
     */
    public static Byte[] BCDNArrayCompress(Byte[] in) {
        int length = in.length % 2 == 0 ? in.length / 2 : in.length / 2 + 1;
        Byte[] rtn = new Byte[length];
        for (int i = in.length - 1, j = rtn.length - 1; i >= 0; i -= 2, j--)
            rtn[j] = (byte) ((i > 0 ? in[i - 1] << 4 : ZERO) | in[i]
                    & MASK_LEFT);
        return rtn;
    }

    /**
     * Decompress a BCD Byte[]
     * 
     * @param in
     *            Compressed BCD Byte[]
     * @return Uncompressed BCD Byte[] FIXME: No check on whether the input is,
     *         in fact, compressed
     */
    public static Byte[] BCDNArrayDecompress(Byte[] in) {
        Byte[] rtn = new Byte[in.length * 2];
        Byte current;
        for (int i = in.length - 1, j = rtn.length - 1; i >= 0; i--) {
            current = (byte) (in[i] & MASK_LEFT);
            if (current == MASK_LEFT) // Restore Masked Padding Byte
                current = PAD_RIGHT;
            rtn[j--] = current;
            current = (byte) ((in[i] & MASK_RIGHT) >> 4 & MASK_LEFT);
            if (current == MASK_LEFT)
                current = PAD_RIGHT;
            rtn[j--] = current;
        }
        return rtn;
    }

    /**
     * Convert BCD Byte[] to Long Integer
     * 
     * @param in
     *            BCD Byte[] Array
     * @return Long Integer Value
     */
    public static Long BCDNArrayToInteger(Byte[] in, boolean compressed) {
        int pow = 0;
        Long tmp = 0L;
        byte current;
        for (int i = in.length - 1; i >= 0; i--) {
            current = (byte) (in[i] & MASK_LEFT);
            if (current != MASK_LEFT) {
                tmp += (long) (current * Math.pow(10, pow));
                pow++;
            }
            /*
             * TODO: Test for compression
             */
            if (compressed) {
                current = (byte) ((in[i] & MASK_RIGHT) >> 4 & MASK_LEFT);
                if (current != MASK_LEFT) {
                    tmp += (long) (current * Math.pow(10, pow));
                    pow++;
                }
            }
        }
        return tmp;
    }

    /**
     * Convert a primitive byte[] to a Byte[]
     * 
     * @param in
     *            byte[] to convert
     * @return Byte[] copy
     */
    public static Byte[] ConvertPrimitiveArray(byte[] in) {
        Byte[] rtn = new Byte[in.length];
        for (int i = 0; i < rtn.length; i++)
            rtn[i] = in[i];
        return rtn;
    }

    /**
     * Process a string into a BCDNumeral
     * 
     * @param s
     *            String to parse
     * @param compressed
     *            String is a compressed representation
     * @param j
     *            Justification to use when formatting the array
     * @return BCDNumeral
     */
    public static BCDNumeral fromHexString(String s, boolean compressed,
            Justification j) {
        Config conf = new Config().setCompress(compressed).setJustification(j);
        return fromHexString(s, conf);
    }

    /**
     * Process a string into a BCDNumeral
     * 
     * @param s
     *            String to parse
     * @param conf
     *            Configuration to use
     * @return BCDNumeral
     */
    public static BCDNumeral fromHexString(String s, Config conf) {
        String string_amount = s;
        byte[] in;
        if (string_amount.startsWith("0x"))
            string_amount = string_amount.replaceFirst("0x", "");
        in = DatatypeConverter.parseHexBinary(string_amount);
        return new BCDNumeral(ConvertPrimitiveArray(in), conf);
    }

    /**
     * Convert a Long Integer to a BCD Byte[]
     * 
     * @param i
     *            Long value to convert
     * @param compress
     *            Compress 2 digits into one byte
     * @param j
     *            {@link Justification}
     * @param lengths
     *            Optional Minimum + Maximum Byte[] lengths
     * @return Byte[] Representation
     */
    public static Byte[] IntegerToBCDNArray(long i, boolean compress,
            Justification j, int... lengths) {
        Config conf = new Config().setCompress(compress).setJustification(j);
        if (lengths.length > 0)
            conf.setMinLength(lengths[0]);
        if (lengths.length > 1)
            conf.setMaxLength(lengths[1]);
        return IntegerToBCDNArray(i, conf);
    }

    /**
     * Construct a BCDN Byte[]
     * 
     * @param i
     *            Long value to convert
     * @param config
     *            {@link Config}
     * @return Byte[] Representation
     */
    public static Byte[] IntegerToBCDNArray(long i, Config config) {
        boolean max_limit = config.max_length > 0;
        int max_length = config.max_length;
        int min_length = config.min_length;
        if (config.compressed) {
            max_length *= 2;
            min_length *= 2;
        }
        /*
         * Take a List<Byte>
         */
        long value = i;
        LinkedList<Byte> digits = new LinkedList<Byte>();
        while (value > 0) {
            digits.addLast((byte) (value % 10));
            value /= 10;
        }
        /*
         * Truncate to maximum length
         */
        if (max_limit)
            while (digits.size() > max_length)
                digits.remove(max_length);
        /*
         * Justify up to minimum length
         */
        switch (config.justification) {
        case RIGHT:
            while (digits.size() < min_length)
                digits.addLast(ZERO);
            break;
        case LEFT:
            while (digits.size() < min_length)
                digits.addFirst(PAD_RIGHT);
            break;
        }
        /*
         * Reverse the list and transfer to Byte[]
         */
        Collections.reverse(digits);
        Byte[] result = digits.toArray(new Byte[0]);
        /*
         * Compress if required
         */
        if (config.compressed)
            result = BCDNArrayCompress(result);
        return result;
    }

    /**
     * Byte[] Representation
     */
    transient private Byte[] binary_coded;

    /**
     * Configuration & State Tracking
     */
    private Config           config;

    /**
     * Long Integer Value
     */
    private Long             value;

    /**
     * Construct a BCD from the input array and configuration
     * 
     * @param in
     *            Initial Byte[] Input
     * @param config
     *            {@link Config}
     */
    public BCDNumeral(Byte[] in, Config config) {
        this.config = config;
        binary_coded = in;
        value = BCDNArrayToInteger(in, config.compressed);
        config.setLengths(in.length, in.length);
        config.has_changed = false;
    }

    /**
     * Instantiate a BCD Numeral with default configuration
     * 
     * @param value
     *            Long value to encode
     */
    public BCDNumeral(long value) {
        this(value, new Config());
    }

    /**
     * Instantiate a BCD Numeral
     * 
     * @param value
     *            Long value to encode
     * @param config
     *            {@link Config}
     */
    public BCDNumeral(long value, Config config) {
        this.value = value;
        this.config = config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(BCDNumeral that) {
        return value.compareTo(that.value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Number#doubleValue()
     */
    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Number#floatValue()
     */
    @Override
    public float floatValue() {
        return value.floatValue();
    }

    /**
     * Get {@link Config} for this BCD
     * 
     * @return {@link Config} Object
     */
    public Config getConfig() {
        return config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Number#intValue()
     */
    @Override
    public int intValue() {
        return value.intValue();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Number#longValue()
     */
    @Override
    public long longValue() {
        return value.longValue();
    }

    /**
     * Check and Refresh Array
     * 
     * @param force
     *            Optional parameter to force refresh
     */
    public void refresh(boolean... force) {
        boolean force_refresh = force.length > 0 ? force[0] : false;
        force_refresh = force_refresh || binary_coded == null;
        if (config.has_changed || force_refresh) {
            binary_coded = IntegerToBCDNArray(value, config);
            config.has_changed = false;
        }
    }

    /**
     * Copy to Byte[]
     * 
     * @return Copy of internal Byte[]
     */
    public Byte[] toArray() {
        refresh();
        Byte[] rtn = new Byte[binary_coded.length];
        System.arraycopy(binary_coded, 0, rtn, 0, rtn.length);
        return rtn;
    }

    /**
     * Return a hex string representation of the internal Byte[]
     * 
     * @return Hex String
     */
    public String toByteString() {
        refresh();
        StringBuilder sb = new StringBuilder();
        sb.append(DatatypeConverter.printHexBinary(toPrimitiveArray()));
        return sb.toString();
    }

    /**
     * Copy to primitive byte[]
     * 
     * @return Copy of internal Byte[], as byte[]
     */
    public byte[] toPrimitiveArray() {
        refresh();
        byte[] rtn = new byte[binary_coded.length];
        for (int i = 0; i < rtn.length; i++)
            rtn[i] = binary_coded[i];
        return rtn;
    }

    /**
     * {@link #toByteString()}, minus trailing <em>'f'</em> bytes
     */
    @Override
    public String toString() {
        return toByteString().replaceAll("f", "");
    }
}
