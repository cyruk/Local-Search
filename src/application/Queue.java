package application;

import java.util.ArrayList;

public class Queue{
	public ArrayList<Cell> list = new ArrayList<Cell>();

	public void push(Cell cell){
		this.list.add(cell);
	}

	public Cell pop(){

		Cell tail = this.isEmpty() ? null : this.list.get(list.size()-1);
		this.list.remove(list.size()-1);
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
	}
}