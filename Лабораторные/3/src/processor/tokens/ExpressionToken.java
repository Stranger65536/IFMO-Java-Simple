package processor.tokens;

/**
 * @author vladislav.trofimov@emc.com
 */
public class ExpressionToken extends AbstractToken {
    private final Operators operator;
    private final String firstToken;
    private final String secondToken;

    public ExpressionToken(final Operators operator, final String firstToken, final String secondToken) {
        this.operator = operator;
        this.firstToken = firstToken;
        this.secondToken = secondToken;
        this.tokenType = TokenTypes.EXPRESSION;
    }

    public String getSecondToken() {
        return secondToken;
    }

    public String getFirstToken() {
        return firstToken;
    }

    public Operators getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return "ExpressionToken{" +
                "operator=" + operator +
                ", firstToken='" + firstToken + '\'' +
                ", secondToken='" + secondToken + '\'' +
                '}';
    }
}
