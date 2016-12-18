package processor.tokens;

/**
 * @author vladislav.trofimov@emc.com
 */
public class WrappedExpressionToken extends AbstractToken {
    private final String token;

    public WrappedExpressionToken(final String token) {
        this.token = token;
        this.tokenType = TokenTypes.WRAPPED_EXPRESSION;
    }

    @Override
    public String toString() {
        return "WrappedExpressionToken{" +
                "token='" + token + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }
}
