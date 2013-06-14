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
package little.nj.gui;

import java.awt.AWTEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import little.nj.exceptions.NotImplementedException;

@SuppressWarnings("serial")
public class NotifyLinkedList<E> extends LinkedList<E> {

    private class Itr<I> implements ListIterator<I> {

        ListIterator<I> wrap;

        Itr(ListIterator<I> listIterator) {
            this.wrap = listIterator;
        }

        @Override
        public void add(I arg0) {
            NotifyLinkedList.this.fireListeners(arg0,
                    NotifyLinkedList.ListEvent.Type.ADD);
            this.wrap.add(arg0);
        }

        @Override
        public boolean hasNext() {
            return this.wrap.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.wrap.hasPrevious();
        }

        @Override
        public I next() {
            return this.wrap.next();
        }

        @Override
        public int nextIndex() {
            return this.wrap.nextIndex();
        }

        @Override
        public I previous() {
            return this.wrap.previous();
        }

        @Override
        public int previousIndex() {
            return this.wrap.previousIndex();
        }

        @Override
        public void remove() {
            NotifyLinkedList.this.fireListeners(null,
                    NotifyLinkedList.ListEvent.Type.REMOVE);
            this.wrap.remove();
        }

        @Override
        public void set(I arg0) {
            NotifyLinkedList.this.fireListeners(arg0,
                    NotifyLinkedList.ListEvent.Type.SET);
            this.wrap.set(arg0);
        }
    }

    public static class ListEvent extends AWTEvent {

        public static enum Type {
            ADD, CLEAR, REMOVE, SET;
        }

        Object item = null;

        Type   type;

        public ListEvent(Object src, Object item, Type type) {
            super(src, 1999);
            this.item = item;
            this.type = type;
        }

        public Object getItem() {
            return this.item;
        }

        public Type getType() {
            return this.type;
        }
    }

    public static abstract interface ListListener {

        public abstract void listChanged(
                NotifyLinkedList.ListEvent paramListEvent);
    }

    private List<ListListener> listeners = new LinkedList<ListListener>();

    public NotifyLinkedList() {
    }

    public NotifyLinkedList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public boolean add(E e) {
        fireListeners(e, NotifyLinkedList.ListEvent.Type.ADD);
        return super.add(e);
    }

    @Override
    public void add(int index, E element) {
        fireListeners(element, NotifyLinkedList.ListEvent.Type.ADD);
        super.add(index, element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        fireListeners(null, NotifyLinkedList.ListEvent.Type.ADD);
        return super.addAll(index, c);
    }

    @Override
    public void addFirst(E e) {
        fireListeners(e, NotifyLinkedList.ListEvent.Type.ADD);
        super.addFirst(e);
    }

    @Override
    public void addLast(E e) {
        fireListeners(e, NotifyLinkedList.ListEvent.Type.ADD);
        super.addLast(e);
    }

    public void addListener(ListListener i) {
        this.listeners.add(i);
    }

    @Override
    public void clear() {
        fireListeners(null, NotifyLinkedList.ListEvent.Type.CLEAR);
        super.clear();
    }

    @Override
    public Iterator<E> descendingIterator() {
        throw new NotImplementedException();
    }

    private void fireListeners(Object e, NotifyLinkedList.ListEvent.Type type) {
        ListEvent ev = new ListEvent(this, e, type);
        for (ListListener i : this.listeners)
            i.listChanged(ev);
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new Itr<E>(super.listIterator());
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new Itr<E>(super.listIterator(index));
    }

    @SuppressWarnings("unchecked")
    @Override
    public E remove() {
        Object item = super.remove();
        fireListeners(item, NotifyLinkedList.ListEvent.Type.REMOVE);
        return (E) item;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        fireListeners(null, NotifyLinkedList.ListEvent.Type.REMOVE);
        return super.removeAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public E removeFirst() {
        Object item = super.removeFirst();
        fireListeners(item, NotifyLinkedList.ListEvent.Type.REMOVE);
        return (E) item;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E removeLast() {
        Object item = super.removeLast();
        fireListeners(item, NotifyLinkedList.ListEvent.Type.REMOVE);
        return (E) item;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        boolean sux = super.removeLastOccurrence(o);
        if (sux)
            fireListeners(o, NotifyLinkedList.ListEvent.Type.REMOVE);
        return sux;
    }
}
