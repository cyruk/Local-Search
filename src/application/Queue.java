package application;

import java.util.ArrayList;

public class Queue{
	public ArrayList<Cell> list = new ArrayList<Cell>();
	
	public Node tail;
	public void enqueue(Cell cell){
		Node temp = new Node(cell);
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
	
	public Cell dequeue(){
		Node temp = tail.next;
		if (tail == tail.next)
		{
			tail = null;
		}
		else
		{
			tail.next = tail.next.next;
		}
		return temp.contents;
	}
	
	public Node peek()
	{
		return tail.next;
	}
	
	public boolean isEmpty()
	{
		return tail == null;
	}
	
	public String toString()
	{
		String listString = "";
		Node temp = tail;
		do
		{
			listString += tail.contents.toString()+"|";
			temp = temp.next;
		}while (temp != tail);
		return listString;
	}
}