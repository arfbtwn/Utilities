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
    
    /**
     * Retrieves the file name part of the path
     * 
     * @param file_path
     * @return
     */
    public String getFilename(String file_path) {        
        String[] path = file_path.split(File.separator);
        
        String name = path[path.length - 1];
        
        int idx = name.lastIndexOf(".");
        
        if (idx > -1)
            return name.substring(0, idx);
        
        return name;
    }
    
    /**
     * Retrieves the extension of the path. If there is no extension
     * returns {@link StringUtil.EMPTY_STRING}
     * 
     * @param file_path
     * @return
     */
    public String getExtension(String file_path) {
        int idx = file_path.indexOf(".");
        
        if (idx > -1)
            return file_path.substring(idx + 1);
        
        return StringUtil.EMPTY_STRING;
    }
    
    /**
     * Atomic read operation
     * 
     * @param file
     * @param user
     * @return
     */
    public synchronized boolean read(File file, InputAction user) {
        start();
        
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            read(stream, user);
        } catch (IOException ex) {
            record(ex);
        }
        
        return end();
    }
    
    /**
     * Atomic write operation
     * 
     * @param file
     * @param user
     * @return
     */
    public synchronized boolean write(File file, OutputAction user) {
        start();
        
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            write(stream, user);
        } catch (IOException ex) {
            record(ex);
        }
        
        return end();
    }
}
