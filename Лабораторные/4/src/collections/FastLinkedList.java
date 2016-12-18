package collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * @author vladislav.trofimov@emc.com
 */
public class FastLinkedList<E> implements Iterable<E> {

    private final HashMap<E, LinkedList<Entity<E>>> map;
    private Entity<E> firstAddedEntity;
    private Entity<E> lastAddedEntity;

    public FastLinkedList() {
        map = new HashMap<>();
    }

    public FastLinkedList<E> put(final E element) {
        LinkedList<Entity<E>> chain = getChain(element);
        Entity<E> entity = new Entity<>(element);
        linkPair(entity);
        chain.add(entity);
        return this;
    }

    public FastLinkedList<E> remove(final E element) {
        LinkedList<Entity<E>> chain = getChain(element);
        Iterator<Entity<E>> iterator = chain.iterator();
        while (iterator.hasNext()) {
            Entity<E> entity = iterator.next();
            unlinkPair(entity);
            iterator.remove();
        }
        return this;
    }

    private LinkedList<Entity<E>> getChain(final E element) {
        if (map.containsKey(element)) {
            return map.get(element);
        } else {
            LinkedList<Entity<E>> chain = new LinkedList<>();
            map.put(element, chain);
            return chain;
        }
    }

    private void linkPair(final Entity<E> entity) {
        if (lastAddedEntity == null) {
            //first in list
            entity.prev = null;
            entity.next = null;
            lastAddedEntity = firstAddedEntity = entity;
        } else {
            lastAddedEntity.next = entity;
            entity.prev = lastAddedEntity;
            entity.next = null;
            lastAddedEntity = entity;
        }
    }

    private void unlinkPair(final Entity<E> entity) {
        Entity<E> prev = entity.prev;
        Entity<E> next = entity.next;
        if (prev == null && next == null) {
            //alone in list
            lastAddedEntity = firstAddedEntity = null;
        } else if (prev == null) {
            //first in list
            firstAddedEntity = next;
            next.prev = null;
            entity.next = null;
        } else if (next == null) {
            //last in list
            prev.next = null;
            entity.prev = null;
            lastAddedEntity = prev;
        } else {
            //in the middle
            entity.next = null;
            entity.prev = null;
            prev.next = next;
            next.prev = prev;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new FastLinkedList.FastLinkedListIterator<>(firstAddedEntity);
    }

    public static class FastLinkedListIterator<E> implements Iterator<E> {
        private Entity<E> current;

        public FastLinkedListIterator(final Entity<E> start) {
            this.current = start;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            E result = current.element;
            current = current.next;
            return result;
        }
    }
}
