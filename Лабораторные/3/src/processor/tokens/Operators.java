package processor.tokens;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("HardcodedFileSeparator")
public enum Operators {
    SUM("+"),
    SUB("-"),
    MUL("*"),
    DIV("/");

    private final String text;

    Operators(final String text) {
        this.text = text;
    }

    public static Operators getOperator(final String value) {
        for (final Operators operator : values()) {
            if (operator.text.equals(value)) {
                return operator;
            }
        }
        throw new IllegalArgumentException('"' + value + '"' + " is not a valid operator");
    }

    @Override
    public String toString() {
        return text;
    }
}
