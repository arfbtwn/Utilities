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

import static org.junit.Assert.*;

public class Tuples
{
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main (String [] args)
    {
        OrderedTriple tuple1 = tuple ("Hello, World", 1, "foo"),
                      tuple2 = tuple ("World, Hello", 2, "bar");

        System.out.printf (
            "Testing %2$s: %1$s%n" + "Against %4$s: %3$s%n",
            tuple1.toString (), tuple1.getClass ().getSimpleName (),
            tuple2.toString (), tuple2.getClass ().getSimpleName ()
        );

        assertTrue (!tuple1.equals (tuple2));
        assertTrue (!tuple2.equals (tuple1));
        assertTrue (tuple1.equals(tuple1));
        assertTrue (tuple2.equals (tuple2));
        assertTrue (0 > tuple1.compareTo (tuple2));
        assertTrue (0 < tuple2.compareTo (tuple1));

        System.out.println ("Assertions Pass...");

    }

    public static Tuple tuple (Object...items)
    {
        return new Tuple (items);
    }

    public static <A, B>
    Pair<A, B> tuple (A item1, B item2)
    {
        return new Pair <A, B> (item1, item2);
    }

    public static <A extends Comparable<A>,
                   B extends Comparable<B>>
    OrderedPair<A, B> tuple (A item1, B item2)
    {
        return new OrderedPair <A, B> (item1, item2);
    }

    public static <A, B, C>
    Triple<A, B, C> tuple (A item1, B item2, C item3)
    {
        return new Triple <A, B, C> (item1, item2, item3);
    }

    public static <A extends Comparable<A>,
                   B extends Comparable<B>,
                   C extends Comparable<C>>
    OrderedTriple<A, B, C> tuple (A item1, B item2, C item3)
    {
        return new OrderedTriple <A, B, C> (item1, item2, item3);
    }

    public static <A, B, C, D>
    Quad<A, B, C, D> tuple (A item1, B item2, C item3, D item4)
    {
        return new Quad <A, B, C, D> (item1, item2, item3, item4);
    }

    public static <A extends Comparable<A>,
                   B extends Comparable<B>,
                   C extends Comparable<C>,
                   D extends Comparable<D>>
    OrderedQuad<A, B, C, D> tuple (A item1, B item2, C item3, D item4)
    {
        return new OrderedQuad <A, B, C, D> (item1, item2, item3, item4);
    }

    public static <A, B, C, D, E>
    Quin<A, B, C, D, E> tuple (A item1, B item2, C item3, D item4, E item5)
    {
        return new Quin <A, B, C, D, E> (item1, item2, item3, item4, item5);
    }

    public static <A extends Comparable<A>,
                   B extends Comparable<B>,
                   C extends Comparable<C>,
                   D extends Comparable<D>,
                   E extends Comparable<E>>
    OrderedQuin<A, B, C, D, E> tuple (A item1, B item2, C item3, D item4, E item5)
    {
        return new OrderedQuin <A, B, C, D, E> (item1, item2, item3, item4, item5);
    }

    private Tuples () { }
}
