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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil extends StreamUtil {
    
    public boolean writeFile(File file, OutputAction user) {
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            useOutputStream(stream, user);
        } catch (IOException ex) {
            _last = ex;
        }
        
        return _last == null;
    }
    
    public boolean readFile(File file, InputAction user) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            useInputStream(stream, user);
        } catch (IOException ex) {
            _last = ex;
        }
        
        return _last == null;
    }
    
    protected FileUtil() { }
    
    public static synchronized FileUtil getInstance() {
        if (_instance == null)
            _instance = new FileUtil();
        
        return _instance;
    }
    
    private static FileUtil _instance;
}
