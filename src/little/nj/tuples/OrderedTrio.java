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

public class OrderedTrio<
    A extends Comparable<A>,
    B extends Comparable<B>,
    C extends Comparable<C>
>
extends Trio<A, B, C>
implements Comparable<Trio<A, B, C>>
{
    public OrderedTrio(A item1, B item2, C item3)
    {
        super (item1, item2, item3);
    }

    @Override
    public int compareTo (Trio<A, B, C> o)
    {
        int comp = Item1.compareTo (o.Item1);

        if (0 == comp)
        {
            comp = Item2.compareTo (o.Item2);
        }

        if (0 == comp)
        {
            comp = Item3.compareTo (o.Item3);
        }

        return comp;
    }
}
