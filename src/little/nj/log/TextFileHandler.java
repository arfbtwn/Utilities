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
package little.nj.log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * A simple file logger. It will print a header string, along with records with
 * positional arguments. They are:
 * <ol>
 * <li>Logger name</li>
 * <li>Time stamp, in milliseconds</li>
 * <li>Logging level</li>
 * <li>Source class name</li>
 * <li>Source method name</li>
 * <li>Message String</li>
 * </ol>
 * 
 * @author Nicholas Little
 * 
 */
public class TextFileHandler extends Handler {

    /**
     * Default format string
     */
    public static final String FORMAT        = "%1$-10s \t %2$-12s \t %3$-8s \t "
                                                     + "%4$-30s \t %5$-20s \t %6$s";

    /**
     * Default header repeat interval (100 messages)
     */
    public static final int    HEADER_REPEAT = 100;

    /**
     * Per object format string
     */
    private String             _format;

    /**
     * Per object header repeat interval
     */
    private int                _header_repeat;

    /**
     * The backing writer
     */
    private PrintWriter        _writer;

    /**
     * Basic constructor, default parameters are to overwrite an existing log
     * file and print the header every 100 messages
     * 
     * @param fname
     *            Filename to log to
     * @throws IOException
     */
    public TextFileHandler(String fname) throws IOException {
        this(fname, FORMAT, false, HEADER_REPEAT);
    }

    /**
     * The master constructor with all options available
     * 
     * @param fname
     *            Filename to log to
     * @param format
     *            Format string for header and records
     * @param append
     *            Append to file if exists already
     * @param header_limit
     *            Print header again after this number of messages
     * @throws IOException
     */
    public TextFileHandler(String fname, String format, boolean append,
            int header_limit) throws IOException {
        _writer = new PrintWriter(new FileWriter(fname, append), true);
        _format = format;
        _header_repeat = header_limit;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Handler#close()
     */
    @Override
    public void close() throws SecurityException {
        _writer.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Handler#flush()
     */
    @Override
    public void flush() {
        _writer.flush();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.logging.Handler#publish(java.util.logging.LogRecord)
     */
    @Override
    public void publish(LogRecord record) {
        if (_header_repeat > 1
                && record.getSequenceNumber() % _header_repeat == 0) {
            /*
             * Print the header after the limit number of messages
             */
            _writer.println();
            _writer.println(String.format(_format, "Logger", "Time stamp",
                    "Level", "Class", "Method", "Message"));
        }
        /*
         * Now print the record
         */
        _writer.println(String.format(_format, record.getLoggerName(),
                record.getMillis(), record.getLevel(),
                record.getSourceClassName(), record.getSourceMethodName(),
                record.getMessage()));
    }
}
