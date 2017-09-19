package application;

import java.util.*;

public class Node{
	//visited is here in order to allow the tree to be searched multiple times if it was a boolean every node would have to be changed each time a search is called
	public int data, row, col, visited;
	public Node next, parent;
	public ArrayList<Node> children = new ArrayList<Node>();
	public Cell contents;


	public Node(Cell contents)
	{
		this.contents = contents;
		visited = 0;
	}

	public boolean hasChildren()
	{
		return children.isEmpty();
	}
}