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

class StaticMarshal extends AbstractMarshal {

    StaticMarshal(StructInfo info)
    {
        super(info);

        if (null != info.length)
        {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Object read(ByteBuffer buffer)
    {
        Object struct = null;

        try
        {
            struct = info.clz.newInstance();

            for (FieldMarshal i : sequence)
            {
                i.extract(buffer, struct);
            }
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        catch (InstantiationException e)
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
            for (FieldMarshal i : sequence)
            {
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