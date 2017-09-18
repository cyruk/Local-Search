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
						gridValues[row][col] = new Cell(0);
					} else {
						if ((size - 1) - col >= row - 0) {
							gridValues[row][col]  = new Cell(randomGenerator.nextInt((size - 1) - col) + 1);
						} else {
							gridValues[row][col]  = new Cell(randomGenerator.nextInt(row - 0) + 1);
						}
					}
				} else {
					if ((size - 1) - row >= col - 0) {
						gridValues[row][col]  = new Cell(randomGenerator.nextInt((size - 1) - row) + 1);
					} else {
						gridValues[row][col]  = new Cell(randomGenerator.nextInt(col - 0) + 1);
					}
				}
			}
		}
	}

	public Cell getCell(int row, int col){
		return gridValues[row][col];
	}
}
