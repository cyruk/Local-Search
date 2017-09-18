package application;

public class Queue{
	private Node root;
	private Node current;
	
	public Queue(int data)
	{
		root = new Node(data);
		current = root;
	}
	
	public Node peek()
	{
		return current;
	}
	
	public Node dequeue()
	{
		Node temp = root;
		root = root.getNext();
		return temp;
	}
	
	public void enqueue(int data)
	{
		current.add(data);
	}
}
