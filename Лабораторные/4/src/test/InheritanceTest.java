package test;

import inheritance.ColoredPoint;
import inheritance.DoublePoint;
import inheritance.IntPoint;

import java.awt.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class InheritanceTest {

    public static void main(final String[] args) {
        ColoredPoint coloredPoint = new ColoredPoint(0.0, 0.0, SystemColor.text);
        DoublePoint doublePoint = new DoublePoint(0.0, 0.0);
        IntPoint intPoint = new IntPoint(0, 0);

        System.out.println(doublePoint.equals(coloredPoint) + " ");
        System.out.println(doublePoint.equals(intPoint) + " ");
        System.out.println(doublePoint.equals(doublePoint) + " ");

        System.out.println();

        System.out.println(coloredPoint.equals(doublePoint) + " ");
        System.out.println(coloredPoint.equals(intPoint));
        System.out.println(coloredPoint.equals(coloredPoint));

        System.out.println();

        System.out.println(intPoint.equals(doublePoint) + " ");
        System.out.println(intPoint.equals(intPoint));
        System.out.println(intPoint.equals(coloredPoint));

        System.out.println("---------------");

        coloredPoint = new ColoredPoint(0.0, 0.0, SystemColor.activeCaption);

        System.out.println(doublePoint.equals(coloredPoint) + " ");
        System.out.println(doublePoint.equals(intPoint) + " ");
        System.out.println(doublePoint.equals(doublePoint) + " ");

        System.out.println();

        System.out.println(coloredPoint.equals(doublePoint) + " ");
        System.out.println(coloredPoint.equals(intPoint));
        System.out.println(coloredPoint.equals(coloredPoint));

        System.out.println();

        System.out.println(intPoint.equals(doublePoint) + " ");
        System.out.println(intPoint.equals(intPoint));
        System.out.println(intPoint.equals(coloredPoint));

        System.out.println("---------------");

        doublePoint = new DoublePoint(1.0, 0.0);

        System.out.println(doublePoint.equals(coloredPoint) + " ");
        System.out.println(doublePoint.equals(intPoint) + " ");
        System.out.println(doublePoint.equals(doublePoint) + " ");

        System.out.println();

        System.out.println(coloredPoint.equals(doublePoint) + " ");
        System.out.println(coloredPoint.equals(intPoint));
        System.out.println(coloredPoint.equals(coloredPoint));

        System.out.println();

        System.out.println(intPoint.equals(doublePoint) + " ");
        System.out.println(intPoint.equals(intPoint));
        System.out.println(intPoint.equals(coloredPoint));
    }

}
