package inheritance;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("EqualsAndHashcode")
public abstract class Point {
    abstract Number getX();
    abstract Number getY();
    abstract Boolean overrides();

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return !point.overrides() && !(getX() != null ? !(getX().doubleValue() == point.getX().doubleValue()) : point.getX() != null) &&
                !(getY() != null ? !(getY().doubleValue() == point.getY().doubleValue()) : point.getY() != null);
    }
}
