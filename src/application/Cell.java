package application;

public class Cell {

	public int value, row, col;
	public int depth = -1;
	public boolean isVisited;

	public Cell(int value, int row, int col){
		this.value = value;
		this.row = row;
		this.col = col;
	}

	public String toString(){
		return (this.row+", "+this.col);
	}
}
