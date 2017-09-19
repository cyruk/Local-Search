package application;


public class Queue{
	public Node tail;
	
	public Queue(int data, int row, int col)
	{
		tail = new Node(data, row, col);
		tail.next = tail;
	}
	
	public Queue(Node node)
	{
		tail = node;
		tail.next = tail;
	}
	public void enqueue(int data, int row, int col){
		Node temp = new Node(data, row, col);
		if (tail == null)
		{
			tail = temp;
			tail.next = tail;
		}
		else
		{
			temp.next = tail.next;
			tail.next = temp;
			tail = temp;
		}
	}
	
	public void enqueue(Node node)
	{
		Node temp = node;
		if (tail == null)
		{
			tail = temp;
			tail.next = tail;
		}
		else
		{
			temp.next = tail.next;
			tail.next = temp;
			tail = temp;
		}
	}
	
	public Node dequeue(){
		Node temp = tail.next;
		if (tail == tail.next)
		{
			tail = null;
		}
		else
		{
			tail.next = tail.next.next;
		}
		return temp;
	}
	
	public Node peek()
	{
		return tail.next;
	}
	
	public boolean isEmpty()
	{
		return tail == null;
	}
}