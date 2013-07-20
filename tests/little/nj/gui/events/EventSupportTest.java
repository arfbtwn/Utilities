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
package little.nj.gui.events;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author Nicholas Little
 *
 */
public class EventSupportTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_Fires_ActionListener_ActionEvent() {
        
        EventSupport<ActionListener, ActionEvent> es = new EventSupport<>();
        
        MockListener ml = new MockListener();
        
        es.addEventListener(ml);
        
        es.fireEvent(new ActionEvent(this, 0, "Foo"));
        
        assertEquals(1, ml.count);
        assertEquals("Foo", ml.last_command);
    }

    class MockListener implements ActionListener {

        int count = 0;
        String last_command = null;
        
        @Override
        public void actionPerformed(ActionEvent e) {
            ++count;
            last_command = e.getActionCommand();
        }
        
    }
}
