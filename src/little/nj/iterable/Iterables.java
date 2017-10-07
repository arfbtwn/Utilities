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
package little.nj.iterable;

import little.nj.expressions.Expression;
import little.nj.expressions.predicates.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Iterables
{
    public static void main (String [] args)
    {
        List<String> test = new ArrayList<String> ();

        test.addAll (
            Arrays.asList ("Hello", "FooBar", "World", "Foo", "Bar")
        );

        final Predicate<String> fooPredicate = new Predicate<String>() {
            @Override
            public boolean evaluate (String obj)
            {
                return obj != null && obj.contains ("Foo");
            }
        };

        final Expression<String, Boolean> expPred = new Expression<String, Boolean> ()
        {
            @Override
            public Boolean evaluate (String obj)
            {
                return fooPredicate.evaluate (obj);
            }

        };

        final Folder<Long, String> folder = new Summator<String> ()
        {
            @Override
            public Long fold (Long fold, String element)
            {
                return fold + element.length ();
            }

        };

        Iterable<String> foos = filter (test, fooPredicate);

        Iterable<Boolean> foobools = transform (test, expPred);

        Long length       = fold (test, 0L, folder),
             filterLength = fold (foos, 0L, folder);

        iter (test, new Predicate<String> () {

            @Override
            public boolean evaluate (String obj)
            {
                if ("Foo".equals (obj))
                {
                    System.out.println ();
                    return true;
                }

                System.out.print (obj + " ");
                return false;
            }
        });

        String[] obj = toArray (foos);
        Boolean[] bools = toArray (foobools);

        System.out.println (Arrays.deepToString (obj));
        System.out.println (Arrays.deepToString (bools));
        System.out.println ("Sum of Lengths: " + length);
        System.out.println ("Filter Lengths: " + filterLength);
    }

    public static <T> boolean contains (T[] sequence, Predicate<? super T> predicate)
    {
        return contains(Arrays.asList (sequence), predicate);
    }

    public static <T> boolean contains (Iterable<T> sequence, Predicate<? super T> predicate)
    {
        return null != first(sequence, predicate);
    }

    public static <T> T first (Iterable<T> sequence, Predicate<? super T> predicate)
    {
        Iterator<T> filtered = filter (sequence, predicate).iterator ();

        if (!filtered.hasNext ())
        {
            return null;
        }

        return filtered.next ();
    }

    public static <T> T first (T[] sequence, Predicate<? super T> predicate)
    {
        return first (Arrays.asList (sequence), predicate);
    }

    public static <T> Iterable<T> filter (Iterable<T> sequence, Predicate<? super T> predicate)
    {
        return new PredicateIterable<T> (sequence, predicate);
    }

    public static <T> Iterable<T> filter (T[] sequence, Predicate<? super T> predicate)
    {
        return filter (Arrays.asList (sequence), predicate);
    }

    public static <T, E> Iterable<E> transform (Iterable<T> sequence, Expression<T, E> transform)
    {
        return new TransformIterable<T, E> (sequence, transform);
    }

    public static <T, E> Iterable<E> transform (T[] sequence, Expression<T, E> transform)
    {
        return transform (Arrays.asList (sequence), transform);
    }

    public static <T> void iter (Iterable<T> sequence, Predicate<? super T> function)
    {
        for (T i : sequence)
        {
            if (function.evaluate (i))
                break;
        }
    }

    public static <T> void iter (T[] sequence, Predicate<? super T> function)
    {
        iter (Arrays.asList (sequence), function);
    }

    public static <T, E> E fold (Iterable<T> sequence, E init, Folder<E, T> folder)
    {
        for (T i : sequence)
        {
            init = folder.fold (init, i);
        }

        return init;
    }

    public static <T, E> E fold (T[] sequence, E init, Folder<E, T> folder)
    {
        return fold (Arrays.asList (sequence), init, folder);
    }

    public static <T> T[] toArray (Iterable<T> sequence, T... arr)
    {
        List<T> array = new ArrayList<T> ();

        for (T i : sequence)
        {
            array.add (i);
        }

        return array.toArray (arr);
    }

    private Iterables () { }
}
