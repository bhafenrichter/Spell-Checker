package spellchecker;

import java.util.ArrayList;

public class Node {
    ArrayList<Node> children;
    char letter;
    Node next;
    
    public Node(char letter){
        this.letter = letter;
        children = new ArrayList<Node>();
    }
}
