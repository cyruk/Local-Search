package application;

import java.util.*;

public class Node{
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