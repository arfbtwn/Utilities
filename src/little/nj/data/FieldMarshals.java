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

import static little.nj.data.MarshalBuilder.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import little.nj.data.MarshalBuilder.TypeMarshal;

public class FieldMarshals {

    @SuppressWarnings("serial")
    public static class FieldMarshalException extends RuntimeException
    {
        FieldMarshalException() { }

        FieldMarshalException(Throwable cause) { super(cause); }
    }

    public abstract static class FieldMarshal {

        public final Field field;

        protected FieldMarshal(Field field)
        {
            this.field = field;

            field.setAccessible(true);
        }

        abstract void extract (ByteBuffer buffer, Object struct)
            throws IllegalAccessException, InvocationTargetException;

        abstract void insert  (ByteBuffer buffer, Object struct)
            throws IllegalAccessException, InvocationTargetException;
    }

    static class BasicExtractor extends FieldMarshal {

        final TypeMarshal marshal;

        BasicExtractor(Field field, TypeMarshal reader) {
            super(field);

            if (null == reader)
            {
                throw new IllegalArgumentException();
            }

            this.marshal = reader;
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            field.set(struct, marshal.read(buffer));
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            marshal.write(buffer, field.get(struct));
        }
    }

    static class BytesExtractor extends CountExtractor {

        BytesExtractor(Field counted, Field counter) {
            super(counted, counter);
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            int len = count(struct);
            byte[] array = new byte[len];

            buffer.get(array);
            field.set(struct, array);
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            int len = count(struct);
            byte[] array = (byte[]) field.get(struct);

            if (len != array.length)
            {
                throw new FieldMarshalException();
            }

            buffer.put(array, 0, len);
        }
    }

    static class FinalBytesExtractor extends FieldMarshal {

        FinalBytesExtractor(Field field) {
            super(field);

            if (!byte[].class.equals(field.getType()))
            {
                throw new IllegalArgumentException();
            }
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            byte[] bytes = (byte[]) field.get(struct);
            buffer.get(bytes);
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            byte[] bytes = (byte[]) field.get(struct);
            buffer.put(bytes);
        }
    }

    static class FinalArrayExtractor extends FieldMarshal {

        final TypeMarshal marshal;

        FinalArrayExtractor(Field field, TypeMarshal reader) {
            super(field);

            if (null == reader)
            {
                throw new IllegalArgumentException();
            }

            this.marshal = reader;
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            Object array = field.get(struct);
            int count = Array.getLength(array);
            for (int i = 0; i < count; ++i)
            {
                Array.set(array, i, marshal.read(buffer));
            }
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            Object array = field.get(struct);
            int count = Array.getLength(array);

            for (int i = 0; i < count; ++i)
            {
                marshal.write(buffer, Array.get(array, i));
            }
        }
    }

    abstract static class StringExtractor extends FieldMarshal {

        final Charset charset;

        protected StringExtractor(Field field) {
            super(field);

            Encoding encoding = field.getAnnotation(Encoding.class);

            if (null == encoding)
            {
                charset = Charset.defaultCharset();
            }
            else
            {
                charset = Charset.forName(encoding.charset());
            }
        }
    }

    static class FixedStringExtractor extends StringExtractor {

        final byte[] array;

        FixedStringExtractor(Field field) {
            super(field);

            FixedLength hint = field.getAnnotation(FixedLength.class);

            if (null == hint)
            {
                throw new IllegalArgumentException();
            }

            array = new byte[hint.length()];
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            buffer.get(array);
            field.set(struct, new String(array, charset));
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            String value = (String) field.get(struct);
            toBuffer (value);
            buffer.put(array, 0, array.length);
        }

        private void toBuffer (String value)
        {
            Arrays.fill (array, (byte) 0);
            byte[] string = value.getBytes (charset);
            System.arraycopy (
                string,
                0,
                array,
                0,
                string.length < array.length
                    ? string.length
                    : array.length
            );
        }
    }

    static class CountStringExtractor extends StringExtractor {

        final Field counter;

        CountStringExtractor(Field field, Field counter) {
            super(field);

            if (null == counter)
            {
                throw new IllegalArgumentException();
            }

            this.counter = counter;
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            byte[] recv = new byte[(Integer) counter.get(struct)];
            buffer.get(recv);
            field.set(struct, new String(recv, charset));
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            int length = (Integer) counter.get(struct);
            String value = (String) field.get(struct);
            buffer.put(value.getBytes(charset), 0, length);
        }
    }

    abstract static class CountExtractor extends FieldMarshal {

        final Field counter;
        final int adjustment;

        CountExtractor(Field counted, Field counter)
        {
            super(counted);
            this.counter = counter;

            Counted ctd = counted.getAnnotation(Counted.class);
            Counter ctr = counter.getAnnotation(Counter.class);

            if (null == ctd || null == ctr || ctd.counter() != ctr.id())
            {
                throw new IllegalArgumentException();
            }

            adjustment = ctr.adjustment();
        }

        int count(Object struct)
        {
            try
            {
                return counter.getInt(struct) - adjustment;
            }
            catch (IllegalArgumentException e)
            {
                throw new FieldMarshalException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new FieldMarshalException(e);
            }
        }
    }

    static class CountArrayExtractor extends CountExtractor {

        final TypeMarshal marshal;

        CountArrayExtractor(Field counted, Field counter, TypeMarshal reader)
        {
            super(counted, counter);
            this.marshal = reader;
        }

        @Override
        void extract(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            int count = count(struct);

            Object array = Array.newInstance(
                field.getType().getComponentType(),
                count
            );

            for (int i = 0; i < count; ++i)
            {
                Array.set(array, i, marshal.read(buffer));
            }

            field.set(struct, array);
        }

        @Override
        void insert(ByteBuffer buffer, Object struct)
                throws IllegalAccessException, InvocationTargetException
        {
            Object array = field.get(struct);
            int count = Array.getLength(array);

            if (count != count(struct))
            {
                throw new FieldMarshalException();
            }

            for (int i = 0; i < count; ++i)
            {
                marshal.write(buffer, Array.get(array, i));
            }
        }
    }

    private FieldMarshals() { }
}
