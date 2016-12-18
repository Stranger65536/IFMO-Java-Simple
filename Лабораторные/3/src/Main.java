import processor.Processor;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author vladislav.trofimov@emc.com
 */
class Main {
    public static void main(final String[] args) {
        try (Scanner in = new Scanner(System.in, "UTF-8")) {
            String input = null;
            while (!"exit".equals(input)) {
                System.out.print(">");
                System.out.println(Processor.processExpression(input = in.nextLine()));
            }
        } catch (NoSuchElementException | IllegalArgumentException | IllegalStateException ignored) {
        }
    }
}