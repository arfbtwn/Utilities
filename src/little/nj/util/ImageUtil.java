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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;


public class ImageUtil {

    private ImageUtil() { }
    
    /**
     * Resizes a buffered image according to some dimensions while 
     * retaining aspect ratio
     * 
     * @param in
     * @param max_w
     * @param max_h
     * @return Resized BufferedImage
     */
    public final static Image resizeImage(
    		Image image, 
    		int max_w,
            int max_h
            ) {
    	
    	if (!(image instanceof BufferedImage))
    		return image.getScaledInstance(max_w, max_h, Image.SCALE_SMOOTH);
    	
    	BufferedImage in = (BufferedImage) image;
    	
        int height = in.getHeight();
        int width = in.getWidth();
        
        if (height < 0 || width < 0)
        	return in;
        
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
     * @see ImageUtil#resizeImage(BufferedImage, int, int)
     * 
     * @param in
     * @param d
     * @return Resized BufferedImage
     */
    public final static Image resizeImage(Image in, Dimension d) {
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

    /**
     * Reads an image from the specified sequence of bytes, if possible.
     *
     * @param bytes
     * @return a BufferedImage reference or null
     */
    public final static BufferedImage readImage (byte[] bytes)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
        ImageInputStream iis;
        BufferedImage img;
        try
        {
            iis = ImageIO.createImageInputStream (bis);
            img = ImageIO.read (iis);

            return img;
        }
        catch (IOException ex)
        {
            ex.printStackTrace ();
            return null;
        }
    }
}
