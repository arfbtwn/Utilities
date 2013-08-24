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

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;


public class StringUtil extends StreamUtil {
    
    public static interface ReaderAction {
        void act(Reader reader) throws IOException;
    }
    
    public static interface WriterAction {
        void act(Writer writer) throws IOException;
    }
    
    /**
     * Atomic Reader operation
     * 
     * @param reader
     * @param action
     * @return
     */
    public synchronized boolean read(Reader reader, ReaderAction action) {
        clear();
        
        try {
            action.act(reader);
        } catch (IOException ex) {
            push(ex);
        } finally {
            closeImpl(reader);
        }
        
        return _return();
    }
    
    /**
     * Atomic Writer operation
     * 
     * @param writer
     * @param action
     * @return
     */
    public synchronized boolean write(Writer writer, WriterAction action) {
        clear();
        
        try {
            action.act(writer);
        } catch (IOException ex) {
            push(ex);
        } finally {
            flushImpl(writer);
            closeImpl(writer);
        }
        
        return _return();
    }
    
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
     * Test if two strings are equal, after trimming both
     * 
     * @param x
     * @param y
     * @return
     */
    public final static boolean equalsIgnoreWhiteSpace(String x, String y) {
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
