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

public final class IntByteField extends ByteField {

    public IntByteField(String n) {
        super(4, ByteField.FieldType.INT, n);
    }

    public IntByteField(int o, String n, ByteBuffer r) {
        super(o, 4, ByteField.FieldType.INT, n, r);
    }

    public IntByteField(String n, int d) {
        this(n);
        setValue(d);
    }

    public int getValue() {
        raw.rewind();
        return raw.getInt();
    }

    public void setValue(int x) {
        raw.rewind();
        raw.putInt(x);
    }
}
