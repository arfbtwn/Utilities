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

import static little.nj.data.TypeMarshals.defaultTypeMarshals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import little.nj.data.FieldMarshals.BasicExtractor;
import little.nj.data.FieldMarshals.BytesExtractor;
import little.nj.data.FieldMarshals.CountArrayExtractor;
import little.nj.data.FieldMarshals.CountStringExtractor;
import little.nj.data.FieldMarshals.FieldMarshal;
import little.nj.data.FieldMarshals.FinalArrayExtractor;
import little.nj.data.FieldMarshals.FinalBytesExtractor;
import little.nj.data.FieldMarshals.FixedStringExtractor;

public final class MarshalBuilder {

    public static interface TypeMarshal {
        Object read(ByteBuffer buffer);
        void write(ByteBuffer buffer, Object struct);
    }

    final Map<Class<?>, TypeMarshal> typeMarshals;

    public MarshalBuilder(Map<Class<?>, TypeMarshal> typeMarshals)
    {
        this.typeMarshals = Collections.synchronizedMap(
            new HashMap<Class<?>, TypeMarshal>(typeMarshals)
        );
    }

    public MarshalBuilder()
    {
        this(defaultTypeMarshals);
    }

    public MarshalBuilder register(Class<?> clz, TypeMarshal marshal)
    {
        typeMarshals.put(clz, marshal);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T read(ByteBuffer buffer, Class<T> type)
    {
        TypeMarshal reader = typeMarshal(type);

        return (T) reader.read(buffer);
    }

    public void write(ByteBuffer buffer, Object struct)
    {
        Class<?> type = struct.getClass();
        TypeMarshal writer = typeMarshal(type);

        writer.write(buffer, struct);
    }

    public final TypeMarshal typeMarshal(Class<?> type)
    {
        TypeMarshal marshal = typeMarshals.get(type);

        if (null == marshal)
        {
            marshal = createTypeMarshal(type);

            typeMarshals.put(type, marshal);
        }

        return marshal;
    }

    protected TypeMarshal createTypeMarshal(Class<?> type)
    {
        StructInfo info = new StructInfo(type);

        if (null == info.length)
            return new StaticMarshal(info);
        else
            return new VariableMarshal(info);
    }

    private FieldMarshal fieldMarshal(StructInfo info, Field field)
    {
        Class<?> fieldType = field.getType();

        if (fieldType.isArray())
        {
            boolean isFinal = Modifier.isFinal(field.getModifiers());

            if (byte[].class.equals(fieldType))
            {
                if (isFinal)
                    return new FinalBytesExtractor(field);
                else
                    return new BytesExtractor(field, info.counter(field));
            }
            else if (isFinal)
            {
                return new FinalArrayExtractor(field, typeMarshal(fieldType.getComponentType()));
            }
            else
            {
                return new CountArrayExtractor(field, info.counter(field), typeMarshal(fieldType.getComponentType()));
            }
        }
        else if (String.class.equals(fieldType))
        {
            FixedLength hint = field.getAnnotation(FixedLength.class);

            if (null != hint)
                return new FixedStringExtractor(field);
            else
                return new CountStringExtractor(field, info.counter(field));
        }
        else
        {
            return new BasicExtractor(field, typeMarshal(fieldType));
        }
    }

    class StructInfo {

        final Class<?> clz;

        final Field length;

        final Map<Integer, Field> counters = new HashMap<Integer, Field>();
        final Map<Field, Integer> counted = new HashMap<Field, Integer>();

        final FieldMarshal[] sequence;

        StructInfo(Class<?> clz)
        {
            this.clz = clz;

            Field[] fields = clz.getDeclaredFields();

            List<FieldMarshal> required = new ArrayList<FieldMarshal>();

            for (Field i : fields)
            {
                boolean ignore = Modifier.isStatic(i.getModifiers()) ||
                                 null != i.getAnnotation(Ignore.class);

                if (ignore)
                {
                    continue;
                }

                Counted ctd = i.getAnnotation(Counted.class);
                Counter ctr = i.getAnnotation(Counter.class);

                if (null != ctd)
                {
                    counted.put(i, ctd.counter());
                }
                else if (null != ctr)
                {
                    counters.put(ctr.counter(), i);
                }

                required.add(fieldMarshal(this, i));
            }

            Counted struct = clz.getAnnotation(Counted.class);
            if (null != struct)
            {
                if (!counters.containsKey(struct.counter()))
                {
                    throw new IllegalArgumentException();
                }

                length = counters.get(struct.counter());
            }
            else
            {
                length = null;
            }

            sequence = required.toArray(new FieldMarshal[required.size()]);
        }

        Field counter(Field field)
        {
            return counters.get(counted.get(field));
        }

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Constant { }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.FIELD })
    public @interface Counted
    {
        int counter();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Counter
    {
        int counter();
        int adjustment() default 0;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface FixedLength
    {
        int length();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Encoding
    {
        String charset() default "US-ASCII";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Ignore { }
}
