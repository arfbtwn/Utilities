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
import java.nio.charset.Charset;

public class StringByteField extends ByteField {

    private Charset charset;

    public StringByteField(int l, String n, Charset c) {
        super(l, ByteField.FieldType.STRING, n);
        charset = c;
    }

    public StringByteField(int o, int l, String n, ByteBuffer r, Charset c) {
        super(o, l, ByteField.FieldType.STRING, n, r);
        charset = c;
    }

    public StringByteField(int l, String n, Charset c, String d) {
        this(l, n, c);
        setValue(d);
    }

    protected StringByteField(StringByteField x) {
        super(x);
        charset = x.charset;
    }

    @Override
    protected ByteField clone() {
        return new StringByteField(this);
    }

    public String getValue() {
        return new String(getBytes(), charset).trim();
    }

    /**
     * Sets the string value, but will not overflow the field
     * @param x
     */
    public void setValue(String x) {
        byte[] in = x.trim().getBytes(charset);
        setBytes(in);
    }
}
