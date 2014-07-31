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

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;

import little.nj.data.FieldMarshals.FieldMarshal;
import little.nj.data.MarshalBuilder.StructInfo;

class VariableMarshal extends AbstractMarshal
{
    VariableMarshal(StructInfo info) {
        super(info);

        if (null == info.length)
        {
            throw new IllegalArgumentException();
        }
    }

    int length(Object struct)
    {
        int value = 0;

        try
        {
            value = info.length.getInt(struct);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return 0 == value ? Integer.MAX_VALUE : value;
    }

    @Override
    public Object read(ByteBuffer buffer)
    {
        Object struct = null;

        try
        {
            struct = info.clz.newInstance();
            int pos = buffer.position();

            for (FieldMarshal i : sequence)
            {
                int len = length(struct);

                if (len <= buffer.position() - pos)
                {
                    break;
                }

                i.extract(buffer, struct);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        catch (InstantiationException e )
        {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return struct;
    }

    @Override
    public void write(ByteBuffer buffer, Object struct)
    {
        try
        {
            int pos = buffer.position();
            int len = length(struct);

            for (FieldMarshal i : sequence)
            {
                if (len <= buffer.position() - pos)
                {
                    break;
                }

                i.insert(buffer, struct);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}