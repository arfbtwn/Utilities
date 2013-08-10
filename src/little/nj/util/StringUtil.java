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

    /**
     * Test if a string is null or the empty string (after trimming)
     * 
     * @param x
     *            String to test
     * @return boolean
     */
    public final static boolean isNullOrWhiteSpace(String x) {
        return x == null || x.trim().isEmpty();
    }
    
    /**
     * The empty string, for semantic effect
     */
    public final static String EMPTY_STRING = "";
}
