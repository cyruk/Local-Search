package application;

import java.util.ArrayList;

public class Queue{
		/*public ArrayList<Cell> list = new ArrayList<Cell>();

		public void push(Cell cell){
			this.list.add(cell);
		}

		public Cell pop(){

			Cell tail = this.isEmpty() ? null : this.list.get(0);
			this.list.remove(0);
			return tail;
		}

		public boolean isEmpty(){
			return this.list.isEmpty();
		}

		public String toString(){
			String listString = "";

			for (Cell c : this.list)
			{
			    listString += c+"|";
			}

			return listString;
		}*/
	//I think this is more efficient than arraylists
	
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
		Node temp = tail.next;
		do
		{
			listString += temp.contents.toString()+"|";
			temp = temp.next;
		}while (temp != tail);
		return listString;
	}
}