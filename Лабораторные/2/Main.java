import javafx.util.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

/**
 * @author itp421
 */
public class Main {

    private static int openDepth = 0;
    private static int closeDepth = 0;

    public static void main(String[] args) {
        String input = "(([]{})[][()])";
        Set<Pair<Character, Character>> delimiters = new HashSet<>();
        delimiters.add(new Pair<Character, Character>('{', '}'));
        delimiters.add(new Pair<Character, Character>('(', ')'));
        delimiters.add(new Pair<Character, Character>('[', ']'));
        Tree tree = parseString(input, delimiters, true);
        if (tree == null) {
            System.out.println("Invalid tree");
        } else {
            printTree(tree);
        }
    }

    public static void printTab(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print("  ");
        }
    }

    public static void printTree(Tree tree) {
        if (tree == null) {
            return;
        }
        printTab(tree.getDepth());
        if (!tree.getContent().isEmpty()) {
            System.out.println(tree.getBrackets().getKey());
            for (Tree child : tree.getContent()) {
                printTree(child);
            }
        } else {
            System.out.print(tree.getBrackets().getKey());
        }
        printTab(tree.getDepth());
        System.out.println(tree.getBrackets().getValue());
    }

    public static Tree parseString(String input, Set<Pair<Character, Character>> delimiters, boolean autoClose) {
        if (input.isEmpty()) {
            Tree res = new Tree();
            res.setBrackets(new Pair<Character, Character>(' ', ' '));
            return res;
        }
        Tree tree = null;
        Tree lastEnteredEntity = null;
        int counter = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            Pair<Delimiters, Pair<Character, Character>> parsedChar = parseDelimiter(delimiters, c);
            if (parsedChar.getKey() == Delimiters.NON_DELIMITER) {
                return null;
            } else if (parsedChar.getKey() == Delimiters.OPEN) {
                Pair<Character, Character> brackets = parsedChar.getValue();
                counter++;
                if (tree == null) {
                    tree = new Tree();
                    tree.setBrackets(brackets);
                    lastEnteredEntity = tree;
                } else {
                    if (lastEnteredEntity == null) {
                        lastEnteredEntity = tree;
                    }
                    Tree newLeaf = new Tree();
                    newLeaf.setBrackets(brackets);
                    newLeaf.setParent(lastEnteredEntity);
                    newLeaf.setDepth(lastEnteredEntity.getDepth() + 1);
                    lastEnteredEntity.getContent().add(newLeaf);
                    lastEnteredEntity = newLeaf;
                }
            } else if (parsedChar.getKey() == Delimiters.CLOSE) {
                counter--;
                if (lastEnteredEntity == null) {
                    return null;
                }
                Pair<Character, Character> brackets = parsedChar.getValue();
                char open = brackets.getKey();
                char lastOpen = lastEnteredEntity.getBrackets().getKey();
                if (open != lastOpen) {
                    return null;
                } else {
                    lastEnteredEntity = lastEnteredEntity.getParent();
                }
            }
        }
        if (counter == 0 || autoClose) {
            return tree;
        } else {
            return null;
        }
    }

    public static Pair<Delimiters, Pair<Character, Character>> parseDelimiter(Set<Pair<Character, Character>> delimiters, char c) {
        for (Pair<Character, Character> pair : delimiters) {
            if (c == pair.getKey()) {
                return new Pair<>(Delimiters.OPEN, pair);
            } else if (c == pair.getValue()) {
                return new Pair<>(Delimiters.CLOSE, pair);
            }
        }
        return new Pair<>(Delimiters.NON_DELIMITER, null);
    }

    public static enum Delimiters {
        OPEN,
        CLOSE,
        NON_DELIMITER
    }

    static class Tree {
        private Pair<Character, Character> brackets;
        private LinkedList<Tree> content = new LinkedList<>();
        private Tree parent;
        private int depth = 0;

        public int getDepth() {
            return depth;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public Tree getParent() {
            return parent;
        }

        public void setParent(Tree parent) {
            this.parent = parent;
        }

        public Pair<Character, Character> getBrackets() {
            return brackets;
        }

        public void setBrackets(Pair<Character, Character> brackets) {
            this.brackets = brackets;
        }

        public LinkedList<Tree> getContent() {
            return content;
        }

        public void setContent(LinkedList<Tree> content) {
            this.content = content;
        }
    }
}
