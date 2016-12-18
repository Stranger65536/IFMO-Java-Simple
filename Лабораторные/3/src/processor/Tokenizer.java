package processor;

import processor.tokens.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author vladislav.trofimov@emc.com
 */
@SuppressWarnings("HardcodedFileSeparator")
class Tokenizer {

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^-?\\d+$");
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("^[a-zA-Z]+$");
    private static final Pattern WRAPPED_EXPRESSION_PATTERN = Pattern.compile("^[(].+[)]$");
    private static final Pattern COMPLEX_EXPRESSION_BOTH_EXTENDED_PATTERN = Pattern.compile(
            "(?<root>^(?<op>[/*+-])\\s+" +
                    "(?<first>(?<fop>[/*+-])\\s+(?<ffe>.*)\\s+(?<fse>.*))\\s+" +
                    "(?<second>(?<sop>[/*+-])\\s+(?<sfe>.*)\\s+(?<sse>.*))$)");
    private static final Pattern COMPLEX_EXPRESSION_LEFT_EXTENDED_PATTERN = Pattern.compile(
            "(?<root>^(?<op>[/*+-])\\s+" +
                    "(?<first>(?<fop>[/*+-])\\s+(?<ffe>.*)\\s+(?<fse>.*))\\s+" +
                    "(?<second>(?<snum>-?\\d+)|(?<svar>[a-zA-Z]+)|(?<swrap>\\(.*\\)))$)");
    private static final Pattern COMPLEX_EXPRESSION_RIGHT_EXTENDED_PATTERN = Pattern.compile(
            "(?<root>^(?<op>[/*+-])\\s+" +
                    "(?<first>(?<fnum>-?\\d+)|(?<fvar>[a-zA-Z]+)|(?<fwrap>\\(.*\\)))\\s+" +
                    "(?<second>(?<sop>[/*+-])\\s+(?<sfe>.*)\\s+(?<sse>.*))$)");
    private static final Pattern COMPLEX_EXPRESSION_SIMPLE_PATTERN = Pattern.compile(
            "(?<root>^(?<op>[/*+-])\\s+" +
                    "(?<first>(?<fnum>-?\\d+)|(?<fvar>[a-zA-Z]+)|(?<fwrap>\\(.*\\)))\\s+" +
                    "(?<second>(?<snum>-?\\d+)|(?<svar>[a-zA-Z]+)|(?<swrap>\\(.*\\)))$)");

    private static boolean isNumber(final String s) {
        return NUMERIC_PATTERN.matcher(s).matches();
    }

    private static boolean isVariable(final String s) {
        return VARIABLE_PATTERN.matcher(s).matches();
    }

    private static boolean isWrappedExpression(final String s) {
        return WRAPPED_EXPRESSION_PATTERN.matcher(s).matches();
    }

    private static ExpressionToken parseExpressionToken(final String s, final Pattern pattern) {
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String op = matcher.group("op").trim();
            String first = matcher.group("first").trim();
            String second = matcher.group("second").trim();
            return createExpressionToken(op, first, second);
        }
        return null;
    }

    private static ExpressionToken parseComplexExpression(final String s) {
        ExpressionToken result;
        if ((result = parseExpressionToken(s, COMPLEX_EXPRESSION_BOTH_EXTENDED_PATTERN)) != null) {
            return result;
        } else if ((result = parseExpressionToken(s, COMPLEX_EXPRESSION_LEFT_EXTENDED_PATTERN)) != null) {
            return result;
        } else if ((result = parseExpressionToken(s, COMPLEX_EXPRESSION_RIGHT_EXTENDED_PATTERN)) != null) {
            return result;
        } else if ((result = parseExpressionToken(s, COMPLEX_EXPRESSION_SIMPLE_PATTERN)) != null) {
            return result;
        }
        return null;
    }

    public static AbstractToken parseToken(final String input) throws IllegalArgumentException {
        String preparedString;
        if (input == null || (preparedString = input.trim()).isEmpty()) {
            throw new IllegalArgumentException("Empty string is not a valid expression");
        }
        ExpressionToken result;
        if (isNumber(preparedString)) {
            return createNumberToken(preparedString);
        } else if (isVariable(preparedString)) {
            return createVariableToken(preparedString);
        } else if (isWrappedExpression(preparedString)) {
            return createWrappedExpressionToken(preparedString);
        } else if ((result = parseComplexExpression(preparedString)) != null) {
            return result;
        }
        throw new IllegalArgumentException("Illegal expression format: \"" + input + '"');
    }

    private static ExpressionToken createExpressionToken(final String op, final String firstExpression, final String secondExpression) {
        Operators operator = Operators.getOperator(op.trim());
        return new ExpressionToken(operator, firstExpression.trim(), secondExpression.trim());
    }

    private static WrappedExpressionToken createWrappedExpressionToken(final String tokenString) {
        int firstDelimiter = tokenString.indexOf('(');
        int lastDelimiter = tokenString.lastIndexOf(')');
        String expression = tokenString.substring(firstDelimiter + 1, lastDelimiter);
        return new WrappedExpressionToken(expression);
    }

    private static NumberToken createNumberToken(final String tokenString) {
        double value = Double.parseDouble(tokenString);
        return new NumberToken(value);
    }

    private static VariableToken createVariableToken(final String tokenString) {
        return new VariableToken(tokenString);
    }

}