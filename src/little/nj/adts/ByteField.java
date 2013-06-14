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

import java.nio.ByteBuffer;

import javax.xml.bind.DatatypeConverter;

/**
 * A class to represent a field mapped to a set of bytes
 * 
 * @author Nicholas Little
 *
 */
public class ByteField implements Comparable<ByteField>, Cloneable {

    /**
     * 4 different field types are supported
     * 
     * @author Nicholas Little
     *
     */
    public static enum FieldType {
        BYTE, INT, SHORT, STRING;
    }

    private String       name;

    private Integer      offset;

    protected ByteBuffer raw;

    private FieldType    type;

    protected ByteField(ByteField x) {
        offset = x.offset;
        type = x.type;
        name = x.name;
        raw = ByteBuffer.allocate(x.getLength());
        setBytes(x.raw.array());
    }

    ByteField(int o) {
        offset = Integer.valueOf(o);
    }

    public ByteField(int l, FieldType t, String n) {
        type = t;
        name = n;
        raw = ByteBuffer.allocate(l);
        offset = Integer.valueOf(-1);
    }

    public ByteField(int l, FieldType t, String n, byte[] d) {
        this(l, t, n);
        setBytes(d);
    }

    public ByteField(int o, int l, FieldType t, String n, ByteBuffer r) {
        this(l, t, n);
        parse(r);
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    protected ByteField clone() {
        return new ByteField(this);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(ByteField b) {
        return offset.compareTo(b.offset);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return offset.hashCode();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ByteField)
            return compareTo((ByteField) obj) == 0;
        return false;
    }

    public ByteBuffer getBuffer() {
        raw.rewind();
        return raw.duplicate();
    }

    public byte[] getBytes() {
        return raw.array();
    }

    public int getLength() {
        return raw.capacity();
    }

    public String getName() {
        return name;
    }

    public int getOffset() {
        return offset.intValue();
    }

    public FieldType getType() {
        return type;
    }

    public void parse(ByteBuffer r) {
        r.position(offset.intValue());
        byte[] tmp = new byte[raw.capacity()];
        r.get(tmp);
        setBytes(tmp);
    }

    public void setBytes(byte[] bytes) {
        raw.rewind();
        raw.put(bytes, 0, raw.capacity() < bytes.length ? raw.capacity()
                : bytes.length);
        
        while(raw.position() < raw.capacity())
            raw.put((byte)0x0);
    }

    public void setName(String n) {
        name = n;
    }

    public void setOffset(int i) {
        offset = Integer.valueOf(i);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("Name: %s, Offset: %d, Length: %d, Type: %s\n", 
                name, offset, getLength(), type));
        
        sb.append(String.format("Data: %s", DatatypeConverter.printHexBinary(getBytes())));
        
        if (type != FieldType.BYTE)
            sb.append(String.format(", As %s: %s", 
                    type,
                    type == FieldType.STRING 
                        ? getAsString() 
                        : type == FieldType.INT 
                            ? getAsInt()
                            : type == FieldType.SHORT 
                                ? getAsShort() 
                                : null));
        
        return sb.toString();
    }
    
    private int getAsInt() {
        raw.rewind();
        return raw.getInt();
    }

    private short getAsShort() {
        raw.rewind();
        return raw.getShort();
    }

    private String getAsString() {
        return new String(raw.array());
    }
}
