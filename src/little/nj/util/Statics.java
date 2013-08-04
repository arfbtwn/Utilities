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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.ImageIcon;

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
        String[] paths = System.getenv().get("PATH").split(":");
        File ex_file = null;
        for (String i : paths) {
            ex_file = new File(i, comm);
            if (ex_file.exists())
                break;
        }
        return ex_file;
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
     * The empty string, for semantic effect
     */
    public final static String EMPTY_STRING = "";

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

    /**
     * Resizes a buffered image according to some dimensions while 
     * retaining aspect ratio
     * 
     * @param in
     * @param max_w
     * @param max_h
     * @return Resized BufferedImage
     */
    public final static BufferedImage resizeImage(BufferedImage in, int max_w,
            int max_h) {
        int height = in.getHeight();
        int width = in.getWidth();
        double h_factor = (double) max_h / height;
        double w_factor = (double) max_w / width;
        switch (h_factor < w_factor ? 0 : 1) {
        case 0:
            height = (int) (h_factor * height);
            width = (int) (h_factor * width);
            break;
        case 1:
            height = (int) (w_factor * height);
            width = (int) (w_factor * width);
            break;
        }
        BufferedImage out = new BufferedImage(width, height, 2);
        Graphics2D g = out.createGraphics();
        g.drawImage(in, 0, 0, width, height, null);
        g.dispose();
        return out;
    }
    
    /**
     * @see Statics#resizeImage(BufferedImage, int, int)
     * 
     * @param in
     * @param d
     * @return Resized BufferedImage
     */
    public final static BufferedImage resizeImage(BufferedImage in, Dimension d) {
        return resizeImage(in, (int) d.getWidth(), (int) d.getHeight());
    }
    
    /**
     * Retrieves an ImageIcon from the specified resource
     * 
     * @param res
     * @return ImageIcon or null
     */
    public final static ImageIcon getImageIcon(String res) {
        try {
            return new ImageIcon(ClassLoader.getSystemResource(res));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
