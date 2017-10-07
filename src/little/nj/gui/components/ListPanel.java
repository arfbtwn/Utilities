/**
 * Copyright (C) 2014
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
package little.nj.gui.components;

import little.nj.util.ImageUtil;
import little.nj.util.StringUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ListPanel extends JPanel {

    private final DefaultListModel model;
    private final JList	list;
    private final JScrollPane scroller;

    private AddAction	 addAction;
    private RemoveAction removeAction;
    private UpAction 	 upAction;
    private DownAction	 downAction;

    private ControlPanel controls;

    public ListPanel(DefaultListModel model) {
    	super(new BorderLayout());

    	this.model = model;
    	this.list = new RestrictedList(model);
    	this.scroller = new JScrollPane(list);

    	addAction = new AddAction();
    	removeAction = new RemoveAction();
    	upAction = new UpAction();
    	downAction = new DownAction();

    	controls = new ControlPanel();

    	init();
    }

    public ListPanel() {
    	this(new DefaultListModel());
    }

    private void init() {
    	list.addListSelectionListener(listListener);

    	add(scroller, BorderLayout.CENTER);
    	add(controls, BorderLayout.PAGE_END);
    }

    public JPanel asJPanel() { return controls; }

    public JList asJList() { return list; }

    private class ControlPanel extends JPanel {
		JButton add = new JButton(addAction),
				remove = new JButton(removeAction),
				up = new JButton(upAction),
				down = new JButton(downAction);

		ControlPanel() {
			super();

			add.setText(StringUtil.EMPTY_STRING);
			remove.setText(StringUtil.EMPTY_STRING);
			up.setText(StringUtil.EMPTY_STRING);
			down.setText(StringUtil.EMPTY_STRING);

			add(add);
			add(remove);
			add(up);
			add(down);
		}
	}

    private class RestrictedList extends JList {
    	RestrictedList(DefaultListModel model) { super(model); }
    	@Override
    	public void setModel(ListModel model) {
			throw new UnsupportedOperationException();
    	}
    }

    private ListSelectionListener listListener = new ListSelectionListener() {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting())
				return;

			boolean add = addAction.testEnabled(),
					rem = removeAction.testEnabled(),
					up  = upAction.testEnabled(),
					dn  = downAction.testEnabled();

			addAction.setEnabled(add);
			removeAction.setEnabled(rem);
			upAction.setEnabled(up);
			downAction.setEnabled(dn);
		}
	};

	private class AddAction extends AbstractAction {
		AddAction() {
			super("+", ImageUtil.getImageIcon("images/Add24.gif"));
		}

		public boolean testEnabled() { return true; }

		@Override
		public void actionPerformed(ActionEvent e) {
			/*
			 * Delegate to a user provided object?
			 */
			if (true)
				return;

			// TODO
			@SuppressWarnings("unused")
			Object value = null; // User Function
			model.addElement(value);
		}
	}

	private class RemoveAction extends AbstractAction {

		RemoveAction() {
			super("-", ImageUtil.getImageIcon("images/Delete24.gif"));
			setEnabled(testEnabled());
		}

		public boolean testEnabled() {
			return !list.isSelectionEmpty();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for(Object i : list.getSelectedValuesList ())
				model.removeElement(i);
		}

	}

	private class UpAction extends AbstractAction {
		UpAction() {
			super("/\\", ImageUtil.getImageIcon("images/Up24.gif"));
			setEnabled(testEnabled());
		}

		public boolean testEnabled() {
			return list.getSelectedIndices().length == 1 &&
					list.getSelectedIndex() > 0;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int idx = list.getSelectedIndex();
			Object safe = model.get(idx - 1);
			model.set(idx - 1, list.getSelectedValue());
			model.set(idx, safe);
		}
	}

	private class DownAction extends AbstractAction {
		DownAction() {
			super("\\/", ImageUtil.getImageIcon("images/Down24.gif"));
			setEnabled(testEnabled());
		}

		public boolean testEnabled() {
			return list.getSelectedIndices().length == 1 &&
					list.getSelectedIndex() < model.size() - 1;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int idx = list.getSelectedIndex();
			Object safe = model.get(idx + 1);
			model.set(idx + 1, list.getSelectedValue());
			model.set(idx, safe);
		}
	}
}
