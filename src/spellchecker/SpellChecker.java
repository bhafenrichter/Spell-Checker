package spellchecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SpellChecker {

    public static Trie trie;
    public static ArrayList<String> entries;
    
    public static void main(String[] args) {
        trie = new Trie();
        entries = new ArrayList<String>();
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
        for (int i = 0; i < split.length; i++) {
            String cur = split[i];
            TraverseTree(trie.root, cur, 0);
            entries.add(cur);
        }
        getUserAction();
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
        String[] split = text[0].split(" ");

        ArrayList<String> incorrectWords = new ArrayList<String>();
        for (int i = 0; i < split.length; i++) {
            String word = split[i];
            boolean isIncorrect = false;
            //this node is the "!" node, that extends to the actual nodes in the tree
            Node root = trie.root;
            for (int j = 0; j < word.length() + 1; j++) {
                //we've reached the end of the word, see if the last node contains stop value
                if(j == word.length()){
                    //flag to see whether word has stop value
                    boolean isComplete = false;
                    for (int k = 0; k < root.children.size(); k++) {
                        char cur = root.children.get(k).letter;
                        if(cur == '#'){
                            isComplete = true;
                        }
                    }
                    if(isComplete){
                        break;
                    }else{
                        isIncorrect = true;
                        break;
                    }
                }
                char cur = word.charAt(j);
                //set to true if current letter is in the nodes children
                boolean containsLetter = false;
                //check to see if cur is a child of current node
                for (int k = 0; k < root.children.size(); k++) {
                    if(root.children.get(k).letter == cur){
                        containsLetter = true;
                        root = root.children.get(k);
                    }
                }
                
                //letter is not in tree, this word is spelled incorrectly
                if(!containsLetter){
                    isIncorrect = true;
                }
            }
            
            //if the word is incorrect, add it to the list of incorrect words
            if(isIncorrect){
                incorrectWords.add(word);
            }
        }
        
        //Prints out all of the incorrect words 
        if(incorrectWords.size() == 0){
            System.out.println("All words are spelled correctly.");
        }else{
            System.out.println("The following words are spelled incorrectly: " + Arrays.toString(incorrectWords.toArray()));
        }
        
        getUserAction();
    }

    private static void ShowEntries() {
        Collections.sort(entries);
        System.out.println("Entries in Dictionary: " + entries.toString());
        getUserAction();
    }

    public static String[] GetTextFromFile() {
        TextFileClass textFile = new TextFileClass();
        textFile.getFileName("Specify the text file to be read:");
        textFile.getFileContents();

        return textFile.text;
    }
}