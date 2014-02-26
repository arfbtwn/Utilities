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
package little.nj.adts;

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
 * <dt>0.3</dt>
 * <dd>Some performance tweaks.</dd>
 * </dl>
 * 
 * <h4>Todo</h4>
 * <ul>
 * <li>Test incoming Byte[] for Compression</li>
 * </ul>
 * 
 * @author Nicholas Little
 * @version 0.3
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
         * Default: Length = 0
         */
        private int           length = 0;

        /**
         * @return the justification
         */
        public Justification getJustification() {
            return justification;
        }

        /**
         * @return the max_length
         */
        public int getLength() {
            return length;
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
         * Set Byte[] Length
         * 
         * @param i
         *            Byte Length
         * @return This Configuration Object
         */
        public Config setLength(int i) {
            if (length != i) {
                has_changed = true;
                length = i < 0 ? 0 : i;
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
            sb.append("Length: " + length + "\n");
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
     * Half an octet
     */
    public static final int SIZE_NIBBLE = 4;

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
        int length = in.length / 2 + in.length % 2;
        Byte[] rtn = new Byte[length];
        
        for (int i = in.length - 1, j = rtn.length - 1; i >= 0; i -= 2, j--) {
            int high = (i > 0 ? in[i - 1] << SIZE_NIBBLE : ZERO),
                low  = in[i] & MASK_LEFT;
            
            rtn[j] = (byte) (high | low);
        }
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
            if (current == MASK_LEFT) {
                // Restore Masked Padding Byte
                current = PAD_RIGHT;
            }
            rtn[j--] = current;
            current = (byte) ((in[i] & MASK_RIGHT) >> SIZE_NIBBLE & MASK_LEFT);
            
            if (current == MASK_LEFT) {
                current = PAD_RIGHT;
            }
            
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
                current = (byte) ((in[i] & MASK_RIGHT) >> SIZE_NIBBLE & MASK_LEFT);
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
        
        Config conf = new Config()
            .setCompress(compressed)
            .setJustification(j);
        
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
    public static BCDNumeral fromHexString(String hex, Config conf) {
        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }
        byte[] in = DatatypeConverter.parseHexBinary(hex);
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
            Justification j, int length) {
        
        Config conf = new Config()
            .setCompress(compress)
            .setJustification(j)
            .setLength(length);
        
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
        
        /*
         * Record length parameter for use and expand if
         * we'll be compressing
         */
        int length = config.compressed ? config.length * 2 : config.length;
        boolean hasLength = length > 0;
        
        /*
         * Take a List<Byte>
         */
        long value = i;
        LinkedList<Byte> digits = new LinkedList<Byte>();
        do {
            digits.addFirst((byte) (value % 10));
            value /= 10;
        } while(value > 0);
        /*
         * Truncate to maximum length
         */
        if (hasLength) {
            while (digits.size() > length) {
                digits.removeFirst();
            }
        }
        /*
         * Justify up to minimum length
         */
        switch (config.justification) {
        case RIGHT:
            while (digits.size() < length)
                digits.addFirst(ZERO);
            break;
        case LEFT:
            while (digits.size() < length)
                digits.addLast(PAD_RIGHT);
            break;
        }
        /*
         * Transfer to Byte[]
         */
        Byte[] result = digits.toArray(new Byte[digits.size()]);
        
        /*
         * Compress if required
         */
        if (config.compressed) {
            result = BCDNArrayCompress(result);
        }
        
        return result;
    }

    /**
     * Byte[] Representation
     */
    transient private Byte[] binary_coded;

    /**
     * Configuration & State Tracking
     */
    private final Config config;

    /**
     * Long Integer Value
     */
    private final Long value;

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
        binary_coded = in.clone();
        value = BCDNArrayToInteger(in, config.compressed);
        config.setLength(in.length);
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

    /**
     * Get {@link Config} for this BCD
     * 
     * @return {@link Config} Object
     */
    public Config getConfig() { return config; }

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
    public void refresh(boolean force) {
        boolean refresh = config.has_changed || null == binary_coded;
        
        if (refresh || force) {
            binary_coded = IntegerToBCDNArray(value, config);
            config.has_changed = false;
        }
    }
    
    /**
     * @see {@link BCDNumeral#refresh(boolean)}
     */
    public void refresh() { refresh(false); }

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
        
        return DatatypeConverter.printHexBinary(toPrimitiveArray());
    }

    /**
     * Copy to primitive byte[]
     * 
     * @return Copy of internal Byte[], as byte[]
     */
    public byte[] toPrimitiveArray() {
        refresh();
        
        byte[] rtn = new byte[binary_coded.length];
        for (int i = 0; i < rtn.length; i++) {
            rtn[i] = binary_coded[i];
        }
        
        return rtn;
    }
    
    @Override
    public String toString() { return value.toString(); }
}
