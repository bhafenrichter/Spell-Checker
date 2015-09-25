package spellchecker;

import java.util.Arrays;

public class SpellChecker {

    public static Trie trie;

    public static void main(String[] args) {
        trie = new Trie();
        getUserAction();
    }

    public static void getUserAction() {
        System.out.println("Spell Checker: Brandon Hafenrichter");
        System.out.println("");
        System.out.println("1. Add File to Dictionary");
        System.out.println("2. Spellcheck File");
        System.out.println("3. Show Entries");
        System.out.println("4. Exit Program");
        KeyboardInputClass input = new KeyboardInputClass();
        String decision = input.getKeyboardInput("");

        switch (decision) {
            case "1":
                PopulateTree();
                break;
            case "2":
                SpellCheckFile();
                break;
            case "3":
                ShowEntries();
                break;
            case "4":
                System.exit(-1);
                break;
            default:
                System.out.println("Invalid Input Please try again.");
                getUserAction();
        }
    }

    private static void PopulateTree() {
        String[] text = GetTextFromFile();
        String[] split = text[0].split(" ");
        for (int i = 0; i < text.length; i++) {
            String cur = text[i];
            TraverseTree(trie.root, cur, 0);
        }
    }

    public static void TraverseTree(Node n, String word, int index) {

        if (index == word.length() || n.children.size() == 0) {
            //we've reached the end of the word
            if (index == word.length()) {
                n.children.add(new Node('#'));
                return;
            }

            //leaf node, no children therefore add to the tree
            if (n.children.size() == 0) {
                n.children.add(new Node(word.charAt(index)));
                TraverseTree(n.children.get(0), word, ++index);
            }
        } else {
            //traverse the children and see if the current letter is one of them
            char wordData = word.charAt(index);
            boolean hasTraversed = false;
            for (int i = 0; i < n.children.size(); i++) {
                char nodeData = n.children.get(i).letter;
                //child exists, traverse to the next letter
                if (nodeData == wordData) {
                    hasTraversed = true;
                    TraverseTree(n.children.get(i), word, ++index);
                }
            }
            
            //there haven't been any matches, therefore create a new child node 
            if (hasTraversed == false) {
                //child doesn't exist, add node to parent and traverse down it
                n.children.add(new Node(wordData));
                TraverseTree(n.children.get(n.children.size() - 1), word, ++index);
            }
            
        }
    }

    private static void SpellCheckFile() {
        String[] text = GetTextFromFile();

    }

    private static void ShowEntries() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static String[] GetTextFromFile() {
        TextFileClass textFile = new TextFileClass();
        textFile.getFileName("Specify the text file to be read:");
        textFile.getFileContents();

        return textFile.text;
    }
}
