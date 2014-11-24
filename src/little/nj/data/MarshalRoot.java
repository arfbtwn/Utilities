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

import little.nj.data.MarshalBuilder.TypeMarshal;

public class MarshalRoot
{
    private static final MarshalBuilder BUILDER = new MarshalBuilder ();

    public static MarshalBuilder getInstance ()
    {
        return BUILDER;
    }

    public static void register(Class<?> clz, TypeMarshal marshal)
    {
        BUILDER.register (clz, marshal);
    }

    public static <T> T read (ByteBuffer buffer, Class<T> clz)
    {
        return BUILDER.read (buffer, clz);
    }

    public static <T> void write (ByteBuffer buffer, T struct)
    {
        BUILDER.write (buffer, struct);
    }

    private MarshalRoot () { }
}
