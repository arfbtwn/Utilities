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

public class ShortByteField extends ByteField {

    protected ShortByteField(ShortByteField x) {
        super(x);
    }

    public ShortByteField(String n) {
        super(2, ByteField.FieldType.SHORT, n);
    }

    public ShortByteField(int o, String n, ByteBuffer r) {
        super(o, 2, ByteField.FieldType.SHORT, n, r);
    }

    public ShortByteField(String n, short d) {
        this(n);
        setValue(d);
    }

    @Override
    protected ByteField clone() {
        return new ShortByteField(this);
    }

    public short getValue() {
        raw.rewind();
        return raw.getShort();
    }

    public void setValue(short x) {
        raw.rewind();
        raw.putShort(x);
    }
}
