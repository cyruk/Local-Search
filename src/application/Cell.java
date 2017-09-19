package application;

public class Cell {

	public int value, row, col;
	public boolean isVisited;

	public Cell(int value, int row, int col){
		this.value = value;
		this.row = row;
		this.col = col;
	}
}
