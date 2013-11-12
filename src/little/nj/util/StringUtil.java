/**
 * Copyright (C) 2013
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
package little.nj.util;


public class StringUtil {

    private StringUtil() { }

    /**
     * String join for arrays. A call to this function is equivalent
     * to calling <tt>join(sep, 0, array.length, array)</tt>
     *
     * @param sep Separator character to use
     * @param array The array to traverse
     * @return
     */
    public static String join(char sep, Object...array)
    {
        return join(sep, 0, array.length, array);
    }

    /**
     * String join for arrays
     *
     * @param sep Separator character to use
     * @param start Index into array to start from
     * @param end Index into array to end before
     * @param array The array to traverse
     * @return
     */
    public static String join(char sep, int start, int end, Object...array)
    {
        if ((start > array.length || start < 0) ||
                (end > array.length || end < 0))
            throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder();

        for(int i=start; i<end; ++i)
        {
            sb.append(array[i] == null ? StringUtil.EMPTY_STRING : array[i].toString());

            if (i < end - 1)
                sb.append(sep);
        }

        return sb.toString();
    }

    /**
     * Test if a string is null or the empty string (after trimming)
     *
     * @param x
     *            String to test
     * @return boolean
     */
    public static boolean isNullOrWhiteSpace(String x) {
        return x == null || x.trim().isEmpty();
    }

    /**
     * Test if two strings are equal, after trimming both
     *
     * @param x
     * @param y
     * @return
     */
    @SuppressWarnings("unused")
    public static boolean equalsIgnoreWhiteSpace(String x, String y) {
        if (x == null && y == null)
            return true;

        if (x == null || y == null)
            return false;

        return x.trim().equals(y.trim());
    }

    /**
     * The empty string, for semantic effect
     */
    public final static String EMPTY_STRING = "";
}
