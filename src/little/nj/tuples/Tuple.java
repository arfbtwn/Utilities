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
package little.nj.tuples;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Tuple
{
    public final List<Object> Items;

    public Tuple (Object...items)
    {
        if (0 == items.length)
        {
            throw new IllegalArgumentException ();
        }

        this.Items = Collections.unmodifiableList (Arrays.asList (items));
    }

    @Override
    public boolean equals (Object obj)
    {
        return obj instanceof Tuple && Items.equals (((Tuple)obj).Items);
    }

    @Override
    public int hashCode ()
    {
        return Items.hashCode ();
    }

    @Override
    public String toString ()
    {
        return Items.toString ();
    }
}
