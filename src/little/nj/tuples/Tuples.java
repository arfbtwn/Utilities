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

public class Tuples
{
    public static Tuple tuple (Object...items)
    {
        return new Tuple (items);
    }

    public static <A, B> Pair<A, B> pair (A item1, B item2)
    {
        return new Pair <A, B> (item1, item2);
    }

    public static <A extends Comparable<A>, B extends Comparable<B>>
    OrderedPair<A, B> pair (A item1, B item2)
    {
        return new OrderedPair <A, B> (item1, item2);
    }

    public static <A, B, C> Trio<A, B, C> trio (A item1, B item2, C item3)
    {
        return new Trio <A, B, C> (item1, item2, item3);
    }

    public static <A extends Comparable<A>, B extends Comparable<B>, C extends Comparable<C>>
    OrderedTrio<A, B, C> trio (A item1, B item2, C item3)
    {
        return new OrderedTrio <A, B, C> (item1, item2, item3);
    }

    private Tuples () { }
}
