package processor.tokens;

/**
 * @author vladislav.trofimov@emc.com
 */
public class NumberToken extends AbstractToken {
    private final double value;

    public NumberToken(final double value) {
        this.value = value;
        this.tokenType = TokenTypes.NUMBER;
    }

    @Override
    public String toString() {
        return "NumberToken{" +
                "value=" + value +
                '}';
    }

    public double getValue() {
        return value;
    }
}
