package application;

import java.util.Random;

public class Tree{
    public Node root;
    
    public Tree(int data, int row, int col)
    {
    	root = new Node(data, row, col);
    }
    
    public void add(int data, int row, int col)
    {
    	root.children.add(new Node(data, row, col));
    }
    
    //used to find the node so you can add children to it
    public Node find(int row, int col)
    {
    	Random randomGenerator = new Random();
    	int visited;
    	if (root.visited > 100)
    	{
    		visited = randomGenerator.nextInt(100);
    	}
    	else
    	{
    		visited = randomGenerator.nextInt(100) + root.visited + 1;
    	}
    	root.visited = visited;
    	Node temp;
    	Queue newQueue = new Queue(root);
    	while (!newQueue.isEmpty())
    	{
    		temp = newQueue.dequeue();
    		if (temp.row == row && temp.col == col)
    		{
    			return temp;
    		}
    		for (int i = 0; i < temp.children.size(); i++)
    		{
    			if (temp.children.get(i).visited != visited)
    			{
    				newQueue.enqueue(temp.children.get(i));
    				temp.children.get(i).visited = visited;
    			}
    		}
    	}
    	return null;
    }
}