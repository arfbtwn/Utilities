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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamUtil {
    
    protected IOException _last;
    
    public IOException getLastException() { return _last; }
        
    public synchronized boolean close(Closeable stream) 
    {
        try {
            stream.close();
            return true;
        } catch (IOException ex) {
            _last = ex;
            return false;
        }
    }
    
    public synchronized boolean useInput(InputStream stream, 
                                         InputAction user) 
    {
        try {
            user.act(stream);
            _last = null;
        } catch (IOException ex) {
            _last = ex;
        } finally {
            close(stream);
        }

        return _last == null;
    }
    
    public synchronized boolean useOutput(OutputStream stream, 
                                          OutputAction user) 
    {
        try {
            user.act(stream);
            _last = null;
        } catch (IOException ex) {
            _last = ex;
        } finally {
            close(stream);
        }
        
        return _last == null;
    }

    public static synchronized StreamUtil getInstance() {
        if (_instance == null)
            _instance = new StreamUtil();
        
        return _instance;
    }
    
    private static StreamUtil _instance;
    
    protected interface StreamAction { }
    
    public interface InputAction extends StreamAction {
        void act(InputStream stream) throws IOException;
    }
    
    public interface OutputAction extends StreamAction {
        void act(OutputStream stream) throws IOException;
    }
}
