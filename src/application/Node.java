package application;

import java.util.*;

public class Node{
	private int data;
	private Node parent;
	private Node next;
	private List<Node> children = new ArrayList<Node>();
	
	//used for queuing and creating the root
	public Node(int data)
	{
		this.data = data;
		parent = null;
	}
	
	//used for adding in children for the tree
	public Node(int data, Node parent) {
        this.data = data;
        this.parent = parent;
    }
	
	//adds in a node and the node's parent
	public void add(int data, Node parent)
	{
		children.add(new Node(data, parent));
	}
	
	//adds in a queued node
	public void add(int data)
	{
		this.next = new Node(data);
	}
	
	//gets the parent of the current node;
	public Node getParent()
	{
		return parent;
	}
	
	public int getData()
	{
		return data;
	}
	
	public Node getNext()
	{
		return next;
	}
}

