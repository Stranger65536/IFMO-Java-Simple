package inheritance;

/**
 * @author vladislav.trofimov@emc.com
 */
public class DoublePoint extends Point {
    double x, y;

    public DoublePoint(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    Number getX() {
        return x;
    }

    @Override
    Number getY() {
        return y;
    }

    @Override
    Boolean overrides() {
        return false;
    }
}
