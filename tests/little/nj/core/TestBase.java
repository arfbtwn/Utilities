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
package little.nj.core;

import java.io.File;

/**
 * @author Nicholas
 *
 */
public abstract class TestBase {

    /**
     * Directory for storing static content and test resources
     */
    protected final static String DIR_CONTENT = "test_content";

    /**
     * Directory for storing data generated during test
     */
    protected final static String DIR_OUT     = "test_output";

    /**
     * Gets a file object from the content directory with the specified basename
     *
     * @param basename
     * @return File in {@link TestBase#DIR_CONTENT}
     */
    protected File getContentFile(String basename) {
        File dir = new File(DIR_CONTENT);
        if (!dir.exists())
            dir.mkdir();
        return new File(dir, basename);
    }

    /**
     * Gets a file object from the output directory with the specified basename
     *
     * @param basename
     * @return File in {@link TestBase#DIR_OUT}
     */
    protected File getOutputFile(String basename) {
        File dir = new File(DIR_OUT);
        if (!dir.exists())
            dir.mkdir();
        return new File(dir, basename);
    }
}
