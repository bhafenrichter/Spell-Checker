//***********************************************************************************************************
//***********************************************************************************************************
//Class:    Node
//Description: The node class contains the character at that specific node and the ArrayList of children
//that extend from this node
//***********************************************************************************************************
//***********************************************************************************************************
package spellchecker;

import java.util.ArrayList;

public class Node {
    ArrayList<Node> children;   //this arraylist contains all of the nodes that stem from this node
    char letter;                //the letter in this node of the keyword tree
    
    public Node(char letter){
        this.letter = letter;
        children = new ArrayList<Node>();
    }
}
