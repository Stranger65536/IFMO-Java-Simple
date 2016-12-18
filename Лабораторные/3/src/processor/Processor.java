package processor;

import processor.tokens.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @author vladislav.trofimov@emc.com
 */
public class Processor {

    private static final Map<String, Node> variables = new LinkedHashMap<>();
    private static final String PRINT_COMMAND = "print";
    private static final String SET_COMMAND = "set";
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("[a-zA-Z]+");

    public static String processExpression(final String expression) {
        String preparedExpression = expression.trim();
        if (preparedExpression.startsWith(PRINT_COMMAND)) {
            return processPrintVariable(preparedExpression);
        } else if (preparedExpression.startsWith(SET_COMMAND)) {
            return processSetVariable(preparedExpression);
        } else {
            return processAnonymousExpression(preparedExpression);
        }
    }

    private static String processPrintVariable(String preparedExpression) {
        preparedExpression = preparedExpression.substring(PRINT_COMMAND.length()).trim();
        StringTokenizer st = new StringTokenizer(preparedExpression);
        if (st.countTokens() < 1) {
            return "Expecting variable name";
        } else if (st.countTokens() > 1) {
            return "Unexpected token, expected variable name";
        } else if (!VARIABLE_PATTERN.matcher(st.nextToken()).find()) {
            return "Unexpected symbol, expected variable name";
        } else {
            return getVariableByName(preparedExpression);
        }
    }

    private static String processSetVariable(String preparedExpression) {
        preparedExpression = preparedExpression.substring(SET_COMMAND.length()).trim();
        StringTokenizer st = new StringTokenizer(preparedExpression);
        if (st.countTokens() < 2) {
            return "Expecting variable name and expression";
        } else {
            String variable = st.nextToken();
            if (!VARIABLE_PATTERN.matcher(variable).find()) {
                return "Unexpected symbol, expected variable name";
            }
            try {
                String nestedExpression = VARIABLE_PATTERN.matcher(preparedExpression).replaceFirst("");
                AbstractToken parsedExpression = Tokenizer.parseToken(nestedExpression);
                return setVariable(variable, parsedExpression);
            } catch (IllegalArgumentException e) {
                return e.getMessage();
            }
        }
    }

    private static String processAnonymousExpression(final String preparedExpression) {
        try {
            AbstractToken parsedExpression = Tokenizer.parseToken(preparedExpression);
            return calculateExpression(parsedExpression);
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private static String calculateExpression(final AbstractToken expression) {
        Node tree = buildExpressionTree(expression);
        tree = simplifyTree(tree);
        tree = insertVariables(tree);
        tree = simplifyTree(tree);
        return tree.toString();
    }

    private static String setVariable(final String variable, final AbstractToken expression) {
        Node tree = buildExpressionTree(expression);
        tree = simplifyTree(tree);
        Node newTree = insertVariables(tree);
        newTree = simplifyTree(newTree);
        variables.put(variable, newTree);
        recalculateVariables(variable);
        return variable + " = " + newTree;
    }

    private static void recalculateVariables(final String exceptVariable) {
        for (final Map.Entry<String, Node> entry : variables.entrySet()) {
            String variable = entry.getKey();
            Node tree = entry.getValue();
            if (!variable.equals(exceptVariable)) {
                Node newTree = Node.copyTree(tree);
                newTree = insertVariables(newTree);
                newTree = simplifyTree(newTree);
                if (!Objects.equals(newTree, tree)) {
                    variables.put(variable, newTree);
                }
            }
        }
    }

    private static Node insertVariables(final Node root) {
        if (root.getFirstExpression() != null) {
            insertVariables(root.getFirstExpression());
        }
        if (root.getSecondExpression() != null) {
            insertVariables(root.getSecondExpression());
        }
        AbstractToken token = root.getValue();
        if (token instanceof VariableToken) {
            Node newNode = variables.get(((VariableToken) token).getVariable());
            if (newNode != null) {
                return replaceChild(root.getParent(), root, newNode);
            }
        }
        return root;
    }

    private static String getVariableByName(final String variableName) {
        Node tree = variables.get(variableName);
        return tree == null ? "Undefined variable " + variableName : tree.toString();
    }

    private static Node getNodeByToken(final Node parent, final AbstractToken token) {
        if (token instanceof NumberToken) {
            return new Node(token, parent, null, null);
        } else if (token instanceof VariableToken) {
            return new Node(token, parent, null, null);
        } else if (token instanceof WrappedExpressionToken) {
            AbstractToken nestedToken = Tokenizer.parseToken(((WrappedExpressionToken) token).getToken());
            return new Node(token, parent, new Node(nestedToken), null);
        } else if (token instanceof ExpressionToken) {
            AbstractToken firstNestedToken = Tokenizer.parseToken(((ExpressionToken) token).getFirstToken());
            AbstractToken secondNestedToken = Tokenizer.parseToken(((ExpressionToken) token).getSecondToken());
            return new Node(token, parent, new Node(firstNestedToken), new Node(secondNestedToken));
        } else {
            throw new IllegalArgumentException("Unknown token type");
        }
    }

    private static Node buildExpressionTree(final AbstractToken rootExpression) throws IllegalArgumentException {
        Node root = getNodeByToken(null, rootExpression);
        root = buildExpressionTree(root);
        return root;
    }

    private static Node buildExpressionTree(final Node root) throws IllegalArgumentException {
        Node firstChild = root.getFirstExpression();
        Node secondChild = root.getSecondExpression();
        if (firstChild != null) {
            root.setFirstExpression(getNodeByToken(root, firstChild.getValue()));
            buildExpressionTree(root.getFirstExpression());
        }
        if (secondChild != null) {
            root.setSecondExpression(getNodeByToken(root, secondChild.getValue()));
            buildExpressionTree(root.getSecondExpression());
        }
        return root;
    }

    private static Node calculateComplexSimpleNumericExpression(final Node root) {
        NumberToken first = (NumberToken) root.getFirstExpression().getValue();
        NumberToken second = (NumberToken) root.getSecondExpression().getValue();
        Operators op = ((ExpressionToken) root.getValue()).getOperator();
        double value;
        switch (op) {
            case SUM:
                value = first.getValue() + second.getValue();
                break;
            case SUB:
                value = first.getValue() - second.getValue();
                break;
            case MUL:
                value = first.getValue() * second.getValue();
                break;
            case DIV:
                value = first.getValue() / second.getValue();
                break;
            default:
                throw new IllegalArgumentException("Unknown operator");
        }
        return new Node(new NumberToken(value), root.getParent(), null, null);
    }

    private static Node replaceChild(final Node parent, final Node oldChild, final Node newChild) {
        if (parent == null) {
            newChild.setParent(null);
            return newChild;
        } else if (Objects.equals(parent.getFirstExpression(), oldChild)) {
            parent.setFirstExpression(newChild);
            newChild.setParent(parent);
        } else if (Objects.equals(parent.getSecondExpression(), oldChild)) {
            parent.setSecondExpression(newChild);
            newChild.setParent(parent);
        }
        return parent;
    }


    @SuppressWarnings("ConstantConditions")
    private static Node simplifyTree(final Node root) {
        if (root.getFirstExpression() != null) {
            simplifyTree(root.getFirstExpression());
        }
        if (root.getSecondExpression() != null) {
            simplifyTree(root.getSecondExpression());
        }
        AbstractToken token = root.getValue();
        if (token instanceof NumberToken || token instanceof VariableToken) {
            return root;
        } else if (token instanceof WrappedExpressionToken) {
            return replaceChild(root.getParent(), root, root.getFirstExpression());
        } else if (token instanceof ExpressionToken) {
            AbstractToken first = root.getFirstExpression().getValue();
            AbstractToken second = root.getSecondExpression().getValue();
            if (first instanceof NumberToken && second instanceof NumberToken) {
                Node newNode = calculateComplexSimpleNumericExpression(root);
                return replaceChild(root.getParent(), root, newNode);
            }
        } else {
            throw new IllegalArgumentException("Unknown token type");
        }
        return root;
    }

}