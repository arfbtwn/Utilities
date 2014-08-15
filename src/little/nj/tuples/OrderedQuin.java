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

public class OrderedQuin <
    A extends Comparable<A>,
    B extends Comparable<B>,
    C extends Comparable<C>,
    D extends Comparable<D>,
    E extends Comparable<E>
>
extends Quin<A, B, C, D, E>
implements Comparable<Quin<A, B, C, D, E>>
{

    public OrderedQuin(A item1, B item2, C item3, D item4, E item5)
    {
        super (item1, item2, item3, item4, item5);
    }

    @Override
    public int compareTo (Quin<A, B, C, D, E> o)
    {
        int comp;

        return
            0 == (comp = Item1.compareTo(o.Item1))
                ? 0 == (comp = Item2.compareTo (o.Item2))
                    ? 0 == (comp = Item3.compareTo (o.Item3))
                        ? 0 == (comp = Item4.compareTo (o.Item4))
                            ? Item5.compareTo (o.Item5)
                            : comp
                        : comp
                    : comp
                : comp;
    }
}