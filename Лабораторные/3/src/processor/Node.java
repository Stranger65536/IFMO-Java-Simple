package processor;

import processor.tokens.*;

/**
 * @author vladislav.trofimov@emc.com
 */
public class Node {
    private final AbstractToken value;
    private Node parent;
    private Node firstExpression;
    private Node secondExpression;

    public Node(final AbstractToken value) {
        this.value = value;
    }

    public Node(final AbstractToken value, final Node parent, final Node firstExpression, final Node secondExpression) {
        this.value = value;
        this.parent = parent;
        this.firstExpression = firstExpression;
        this.secondExpression = secondExpression;
    }

    @SuppressWarnings("ConstantConditions")
    public static Node copyTree(final Node root) {
        if (root == null) {
            return null;
        }
        Node newRoot = new Node(root.value);
        newRoot.parent = root.parent;
        if (root.firstExpression != null) {
            newRoot.firstExpression = copyTree(root.firstExpression);
            newRoot.firstExpression.parent = newRoot;
        }
        if (root.secondExpression != null) {
            newRoot.secondExpression = copyTree(root.secondExpression);
            newRoot.secondExpression.parent = newRoot;
        }
        return newRoot;
    }

    @Override
    public String toString() {
        if (value == null) {
            return "";
        } else if (value instanceof NumberToken) {
            return Double.toString(((NumberToken) value).getValue());
        } else if (value instanceof VariableToken) {
            return ((VariableToken) value).getVariable();
        } else if (value instanceof WrappedExpressionToken) {
            return '(' + firstExpression.toString() + ')';
        } else if (value instanceof ExpressionToken) {
            return ((ExpressionToken) value).getOperator().toString() + ' ' +
                    firstExpression.toString() + ' ' +
                    secondExpression.toString();
        } else {
            throw new IllegalArgumentException("Unknown token type");
        }
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(final Node parent) {
        this.parent = parent;
    }

    public AbstractToken getValue() {
        return value;
    }

    public Node getFirstExpression() {
        return firstExpression;
    }

    public void setFirstExpression(final Node firstExpression) {
        this.firstExpression = firstExpression;
    }

    public Node getSecondExpression() {
        return secondExpression;
    }

    public void setSecondExpression(final Node secondExpression) {
        this.secondExpression = secondExpression;
    }
}
