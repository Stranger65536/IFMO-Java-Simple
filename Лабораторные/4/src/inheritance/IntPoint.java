package inheritance;

/**
 * @author vladislav.trofimov@emc.com
 */
public class IntPoint extends Point {
    int x, y;

    public IntPoint(int x, int y) {
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
