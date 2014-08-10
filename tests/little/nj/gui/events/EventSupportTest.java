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

import static org.junit.Assert.assertEquals;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.junit.Test;


public class EventSupportTest {

    @Test
    public void test_Fires_ActionListener_ActionEvent() {

        EventSupport<ActionListener, ActionEvent> es =
                new EventSupport<ActionListener, ActionEvent>();

        MockListener ml = new MockListener();

        es.addListener(ml);

        es.fireEvent(new ActionEvent(this, 0, "Foo"));

        assertEquals(1, ml.count);
        assertEquals("Foo", ml.last_command);
    }

    @Test
    public void test_Handles_Listener_Subtypes() {
        EventSupport<ActionListener, ActionEvent> es =
                new EventSupport<ActionListener, ActionEvent>();

        MockListener ml = new MockListener();
        MockingListener mingl = new MockingListener();

        es.addListener(ml);
        es.addListener(mingl);

        es.fireEvent(new ActionEvent(this, 0, "Foo"));
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

    class MockingListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {

        }
    }
}
