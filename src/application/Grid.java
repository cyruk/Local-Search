package application;

import java.util.Random;

public class Grid {

	Cell[][] gridValues;

	public Grid(int size) {

		Random randomGenerator = new Random();
		gridValues = new Cell[size][size];

		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				if (row >= col) {
					if (row == size - 1 && col == size - 1) {
						gridValues[row][col] = new Cell(0, row, col);
					} else {
						if ((size - 1) - col >= row - 0) {
							gridValues[row][col]  = new Cell(randomGenerator.nextInt((size - 1) - col) + 1, row, col);
						} else {
							gridValues[row][col]  = new Cell(randomGenerator.nextInt(row - 0) + 1, row, col);
						}
					}
				} else {
					if ((size - 1) - row >= col - 0) {
						gridValues[row][col]  = new Cell(randomGenerator.nextInt((size - 1) - row) + 1, row, col);
					} else {
						gridValues[row][col]  = new Cell(randomGenerator.nextInt(col - 0) + 1, row, col);
					}
				}
			}
		}
	}

	public Cell getCell(int row, int col){
		if(row>=0 && row<gridValues.length && col>=0 && col<gridValues[0].length){
			return gridValues[row][col];
		}
		return null;
	}

	public void clearVisitations(){
		for (int row = 0; row < gridValues.length; row++) {
			for (int col = 0; col < gridValues[0].length; col++) {
				this.gridValues[row][col].isVisited = false;
			}
		}
	}

	public Cell getTopNeighbor(int row, int col){
		return getCell(row-getCell(row, col).value, col);
	}

	public Cell getRightNeighbor(int row, int col){
		return getCell(row, col+getCell(row, col).value);
	}

	public Cell getBottomNeighbor(int row, int col){
		return getCell(row+getCell(row, col).value, col);
	}

	public Cell getLeftNeighbor(int row, int col){
		return getCell(row, col-getCell(row, col).value);
	}

	//returns all of the surrounding possible moves, null if not possible
	public Cell[] getAllNeighbors(int row, int col){
		Cell[] neigbors = {getTopNeighbor(row, col),
			getRightNeighbor(row, col),
			getBottomNeighbor(row, col),
			getLeftNeighbor(row, col)};

		return neigbors;
	}



	public void setUpFullGridVisualization(){
		bfs(0,0);
	}

	public int evaluate(){
		//this.clearVisitations();
		//bfs(0, 0);
		if(this.getCell(gridValues.length-1, gridValues[0].length-1).depth == -1){
			int unreachableCount = 0;
			for (int row = 0; row < gridValues.length; row++) {
				for (int col = 0; col < gridValues[0].length; col++) {
					if(this.gridValues[row][col].depth == -1){
						unreachableCount++;
					}
				}
			}
			return -1*unreachableCount;
		}
		return this.getCell(gridValues.length-1, gridValues[0].length-1).depth;
	}


	public boolean bfs(int startRow, int startCol){
		Queue unvisited = new Queue();
		unvisited.enqueue(this.getCell(startRow, startCol));
		this.getCell(0, 0).depth = 0;
		this.getCell(0, 0).isVisited = true;
		Cell currentCell;

		while(!unvisited.isEmpty()){
			System.out.println(unvisited.toString()); 
			currentCell = unvisited.dequeue();

			Cell[] possibleMoves = this.getAllNeighbors(currentCell.row, currentCell.col);
    		for (int i = 0; i < possibleMoves.length; i++)
    		{
    			if (possibleMoves[i] != null && !possibleMoves[i].isVisited)
    			{
    				if(possibleMoves[i].value == 0){
    					possibleMoves[i].isVisited = true;
    					possibleMoves[i].depth = currentCell.depth+1;
    				}
    				else
    				{
	    				possibleMoves[i].depth = currentCell.depth+1;
	    				//this is required because for some reason some cells get queued twice and it changes the depth
	    				possibleMoves[i].isVisited = true;
	    				unvisited.enqueue(possibleMoves[i]);
    				}
    			}
    		}
		}
		return false;
	}
}