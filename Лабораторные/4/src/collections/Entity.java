package collections;

/**
 * @author vladislav.trofimov@emc.com
 */
class Entity<E> {
    final E element;
    Entity<E> prev;
    Entity<E> next;

    public Entity(final E element) {
        this.element = element;
    }
}
