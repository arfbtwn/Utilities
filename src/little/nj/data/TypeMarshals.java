/**
 * Copyright (C) 2014
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
package little.nj.data;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import little.nj.data.MarshalBuilder.TypeMarshal;

public class TypeMarshals {

    final static Map<Class<?>, TypeMarshal> defaultTypeMarshals;

    public final static TypeMarshal byteReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.get();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.put((Byte) struct);
        }
    };

    public final static TypeMarshal charReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.getChar();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.putChar((Character) struct);
        }
    };

    public final static TypeMarshal shortReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.getShort();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.putShort((Short) struct);
        }
    };

    public final static TypeMarshal integerReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.getInt();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.putInt((Integer) struct);
        }
    };

    public final static TypeMarshal longReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.getLong();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.putLong((Long) struct);
        }
    };

    public final static TypeMarshal floatReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.getFloat();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.putFloat((Float) struct);
        }
    };

    public final static TypeMarshal doubleReader = new TypeMarshal()
    {
        @Override
        public Object read(ByteBuffer buffer) {
            return buffer.getDouble();
        }

        @Override
        public void write(ByteBuffer buffer, Object struct) {
            buffer.putDouble((Double) struct);
        }
    };

    static {
        defaultTypeMarshals = new HashMap<Class<?>, TypeMarshal>();

        defaultTypeMarshals.put(byte.class, byteReader);
        defaultTypeMarshals.put(Byte.class, byteReader);
        defaultTypeMarshals.put(char.class, charReader);
        defaultTypeMarshals.put(Character.class, charReader);
        defaultTypeMarshals.put(short.class, shortReader);
        defaultTypeMarshals.put(Short.class, shortReader);
        defaultTypeMarshals.put(int.class, integerReader);
        defaultTypeMarshals.put(Integer.class, integerReader);
        defaultTypeMarshals.put(long.class, longReader);
        defaultTypeMarshals.put(Long.class, longReader);
        defaultTypeMarshals.put(float.class, floatReader);
        defaultTypeMarshals.put(Float.class, floatReader);
        defaultTypeMarshals.put(double.class, doubleReader);
        defaultTypeMarshals.put(Double.class, doubleReader);
    }

    public static final Map<Class<?>, TypeMarshal> getDefaultMarshals()
    {
        return Collections.unmodifiableMap(defaultTypeMarshals);
    }
}
