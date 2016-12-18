package processor.tokens;

/**
 * @author vladislav.trofimov@emc.com
 */
public class VariableToken extends AbstractToken {
    private final String variable;

    public VariableToken(final String variable) {
        this.variable = variable;
        this.tokenType = TokenTypes.VARIABLE;
    }

    @Override
    public String toString() {
        return "VariableToken{" +
                "variable='" + variable + '\'' +
                '}';
    }

    public String getVariable() {
        return variable;
    }
}
