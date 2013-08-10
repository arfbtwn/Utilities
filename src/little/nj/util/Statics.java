/**
 * Copyright (C) 2013 Nicholas J. Little <arealityfarbetween@googlemail.com>
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package little.nj.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import little.nj.util.StreamUtil.InputAction;
import little.nj.util.StreamUtil.OutputAction;

/**
 * Useful static functions and variables
 * 
 * @author Nicholas
 * 
 */
public final class Statics {
    
    private Statics() { }
    
    /**
     * Returns the absolute path to executables in the user's path
     * argument
     * 
     * @param comm
     *            Name of the desired command
     * @return File object, or null if not found
     */
    public final static File findSysExe(String comm) {
        String[] paths = System.getenv().get("PATH").split(File.pathSeparator);
        File ex_file = null;
        for (String i : paths) {
            ex_file = new File(i, comm);
            if (ex_file.exists())
                break;
        }
        return ex_file;
    }

    /**
     * Reads a file, returns a byte array
     * 
     * @param file
     * @return byte[]
     */
    public final static byte[] readFile(File file) {
        FileUtil util = FileUtil.getInstance();
        
        long len = file.length();
        
        final byte[] raw = new byte[(int) len];
        
        util.readFile(file, new InputAction() {

            @Override
            public void act(InputStream stream) throws IOException {
                stream.read(raw);
            } });
        
        return raw;
    }

    /**
     * Writes a byte array directly to a file
     * 
     * @param file
     * @param bytes
     * @throws IOException
     */
    public final static boolean writeFile(File file, final byte[] bytes) {
        FileUtil util = FileUtil.getInstance();
        
        return util.writeFile(file, new OutputAction() {

            @Override
            public void act(OutputStream stream) throws IOException {
                stream.write(bytes);
            } });
    }
}
