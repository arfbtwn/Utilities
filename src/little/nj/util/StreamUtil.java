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

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;


public class StreamUtil {
    
    public static interface InputAction {
        void act(InputStream stream) throws IOException;
    }
    
    public static interface OutputAction {
        void act(OutputStream stream) throws IOException;
    }
    
    private Stack<IOException> exceptions = new Stack<>();
    
    /**
     * An atomic operation on an InputStream
     * 
     * @param stream
     * @param user
     * @return
     */
    public synchronized boolean read(InputStream stream, 
                                     InputAction user) 
    {
        clear();
        
        try {
            user.act(stream);
        } catch (IOException ex) {
            push(ex);
        } finally {
            closeImpl(stream);
        }
        
        return _return();
    }
    
    /**
     * An atomic operation on an OutputStream
     * 
     * @param stream
     * @param user
     * @return
     */
    public synchronized boolean write(OutputStream stream, 
                                      OutputAction user) 
    {
        clear();
        
        try {
            user.act(stream);
        } catch (IOException ex) {
            push(ex);
        } finally {
            flushImpl(stream);
            closeImpl(stream);
        }
        
        return _return();
    }
    

    /**
     * A return value subclasses can use
     * 
     * @return
     */
    protected boolean _return() {
        return exceptions.isEmpty();
    }
    
    /**
     * Clears the stack for a new operation
     */
    protected void clear() {
        exceptions.clear();
    }
    
    /**
     * Pushes an exception onto the stack
     * 
     * @param ex
     */
    protected void push(IOException ex) {
        exceptions.push(ex);
    }
    
    /**
     * Pops the last exception off the stack
     * 
     * @return
     */
    public IOException getLastException() { return exceptions.pop(); }
       
    /**
     * Flushes a Flushable, clearing the exception stack first
     * 
     * @param flushable
     * @return
     */
    public synchronized boolean flush(Flushable flushable) {
        clear();
        return flushImpl(flushable);
    }
    
    /**
     * Flushes a flushable
     * 
     * @param flushable
     * @return
     */
    protected synchronized boolean flushImpl(Flushable flushable) {
        try {
            flushable.flush();
            return true;
        } catch (IOException ex) {
            push(ex);
            return false;
        }
    }
    
    /**
     * Closes a Closeable, clearing the exception stack first
     *  
     * @param closeable
     * @return
     */
    public synchronized boolean close(Closeable closeable) 
    {
        clear();
        return closeImpl(closeable);
    }
    
    /**
     * Closes a Closeable
     * 
     * @param closeable
     * @return
     */
    protected boolean closeImpl(Closeable closeable) 
    {
        try {
            closeable.close();
            return true;
        } catch (IOException ex) {
            push(ex);
            return false;
        }
    }

    public static synchronized StreamUtil getInstance() {
        if (_instance == null)
            _instance = new StreamUtil();
        
        return _instance;
    }
    
    private static StreamUtil _instance;
}
