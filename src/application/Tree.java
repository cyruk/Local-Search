package application;

import java.util.*;

public class Tree{
    private Node root;
    private Node current;

    public Tree(int data) {
        root = new Node(data);
        current = root;
    }
    
    public Node getRoot()
    {
    	return root;
    }
    
    public void add(int data)
    {
    	current.add(data, current);
    }
}