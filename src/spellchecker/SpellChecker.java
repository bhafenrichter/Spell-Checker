//Program: Hafenrichter.java
//Course: COSC 420
//Description: An implementation of the Spell Checker assignment using the Trie data structure
//Author: Brandon Hafenrichter
//Revised: October 6, 2015
//Language: Java
//IDE: Netbeans 8.0.2
//***********************************************************************************************************
//***********************************************************************************************************
//Class Main.java
//Description: Contains the essential methods and calls required to construct, traverse, and read the Keyword Trie
//***********************************************************************************************************
//***********************************************************************************************************

package spellchecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SpellChecker {

    public static Trie trie;    //The trie that will be populated with all of the words we will use in Spellchecker
    public static ArrayList<String> entries;    //Keeps track of all of the words placed in the tree
    
//Method: Main
//Description: Prompts the user for the information required to run the Spell Checker
//Returns: None
    
    public static void main(String[] args) {
        //Initializes core components of the spellchecker
        trie = new Trie();
        entries = new ArrayList<String>();
        
        //Get the users response
        getUserAction();
    }
    
//***********************************************************************************************************
    //Method: getUserAction
    //Description: The process of getting all of the user input required in order to run the Spell Checker's algorithms
    //Parameters: None
    //Calls: PopulateTree(), SpellCheckFile(), ShowEntries()
    //Globals: None
//***********************************************************************************************************
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

//***********************************************************************************************************
    //Method: PopulateTree
    //Description: This method, combined with the TraverseTree() method populates the Tree based on the string
    //collected by the TextFileClass supplied by the user 
    //Parameters: None
    //Returns: None
    //Globals: Trie trie, ArrayList<String> entries
//***********************************************************************************************************
    private static void PopulateTree() {
        String[] text = GetTextFromFile();
        
        if(text.length == 0){
            System.out.println("Invalid Submission.  Please try again.");
            getUserAction();
        }
        if(text[0] == null){
            System.out.println("Invalid Submission.  Please try again.");
            getUserAction();
        }
        
        String[] split = text[0].split(" ");
        for (int i = 0; i < split.length; i++) {
            String cur = split[i];
            TraverseTree(trie.root, cur, 0);
            
            //add unique values to entry list
            boolean isUnique = true;
            for (int j = 0; j < entries.size(); j++) {
                if(entries.get(j).equals(cur)){
                    isUnique = false;
                    break;
                }
            }
            if(isUnique){
                entries.add(cur);
            }
        }
        getUserAction();
    }

//***********************************************************************************************************
    //Method: TraverseTree
    //Description: This recursive method takes the word its currently reading and Traverses the tree based on the chars
    //within it, adding nodes or going down other ones based on what is currently in the trie
    //Parameters:   None n -> Current Node that the traversal is currently on
    //              String word -> Current word the loop is working with
    //              int index -> Helps us keep track of which character we currently are in the word
    //Returns: None
    //Globals: Trie trie, ArrayList<String> entries
//***********************************************************************************************************
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
    
//***********************************************************************************************************
    //Method: SpellCheckFile
    //Description: This method takes in text input from the user and traverses the already generated tree
    //and checks which words are in the trie
    //Parameters: None
    //Returns: None
    //Globals: Trie trie
//***********************************************************************************************************
    
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
    
//***********************************************************************************************************
    //Method: ShowEntries
    //Description: Prints out all of the words that are currently in the trie
    //Parameters: None
    //Returns: None
    //Globals: ArrayList<String> entries
//***********************************************************************************************************
    private static void ShowEntries() {
        Collections.sort(entries);
        System.out.println("Entries in Dictionary: " + entries.toString());
        getUserAction();
    }

//***********************************************************************************************************
    //Method: GetTextFromFile
    //Description: This method gets the String[] text from the file supplied by the user
    //Parameters: None
    //Returns:      String[] text -> Array of text that is pulled from the text file specified by user
    //Globals: None
//***********************************************************************************************************
    public static String[] GetTextFromFile() {
        TextFileClass textFile = new TextFileClass();
        textFile.getFileName("Specify the text file to be read:");
        textFile.getFileContents();
        
        for (int i = 0; i < textFile.text.length; i++) {
            if(textFile.text[i] == null) break;
            
            String cur = textFile.text[i];
            textFile.text[0] += " " + cur;
        }
        
        return textFile.text;
    }
}