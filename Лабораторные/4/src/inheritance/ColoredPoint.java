package inheritance;

import java.awt.*;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("EqualsAndHashcode")
public class ColoredPoint extends DoublePoint {
    final SystemColor systemColor;

    public ColoredPoint(final double x, final double y, final SystemColor systemColor) {
        super(x, y);
        this.systemColor = systemColor;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof ColoredPoint)) return false;
        ColoredPoint point = (ColoredPoint) o;
        return !(getX() != null ? !(getX().doubleValue() == point.getX().doubleValue()) : point.getX() != null) &&
                !(getY() != null ? !(getY().doubleValue() == point.getY().doubleValue()) : point.getY() != null) &&
                systemColor != null ? !systemColor.equals(point.systemColor) : point.systemColor != null;
    }

    @Override
    Boolean overrides() {
        return true;
    }
}
