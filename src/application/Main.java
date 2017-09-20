package application;

import java.util.Optional;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Main extends Application {



	@Override
	public void start(Stage primaryStage) {

//		TextInputDialog dialog = new TextInputDialog("11");
//		dialog.setTitle("Dimensions");
//		dialog.setHeaderText("What are the dimensions of your NxN square");
//		dialog.setContentText("Please enter your N:");
//
//		Optional<String> result = dialog.showAndWait();
//		int n = Integer.valueOf(result.get());
//
//		Grid gridStruct = new Grid(n); //created a data structure to represent the grid and cells and moved generation code there
//		GridPane grid = new GridPane();
//
//		gridStruct.setUpFullGridVisualization();//sets up the grid to be visualized with colors, and adds depths(num of moves) from start
//
//	    for (int row = 0; row < n; row++) {
//	        for (int col = 0; col < n; col++) {
//	        	Text numberText;
//	            Rectangle rec = new Rectangle();
//	            rec.setWidth(40);
//	            rec.setHeight(40);
//
//	            if(gridStruct.getCell(row, col).depth == 0 && gridStruct.getCell(row, col).isVisited){
//	            	rec.setFill(Color.LIGHTBLUE);
//	            }else if(gridStruct.getCell(row, col).isVisited){
//	            	rec.setFill(Color.GREEN);
//	            }else{
//	            	rec.setFill(Color.RED);
//	            }
//	            rec.setStroke(Color.BLACK);
//
//	            //Create container to be able to hold text
//	            StackPane pane = new StackPane();
//	            numberText = new Text(gridStruct.getCell(row, col).value+":"+gridStruct.getCell(row, col).depth);
//
//	            //add in text
//	            pane.getChildren().addAll(rec, numberText);
//	            GridPane.setRowIndex(pane, row);
//	            GridPane.setColumnIndex(pane, col);
//	            grid.getChildren().add(pane);
//	        }
//	    }
//
//	    Scene scene = new Scene(grid, 500, 500);
//
//	    primaryStage.setTitle("Grid");
//	    primaryStage.setScene(scene);
//	    primaryStage.show();

	    //this evaluates the shortest number of moves from the given start to the end
	    //System.out.println(gridStruct);
	    //System.out.println(gridStruct.evaluate());

	    System.out.println(this.basicHillClimb(100000).evaluate());
	    System.out.println(this.hillClimbWithRandomRestarts(100, 100000).evaluate());
	    System.out.println(this.hillClimbWithRandomWalk(.00001, 100000).evaluate());
	    System.out.println(this.simulatedAnnealing(100000, 10, .99).evaluate());
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Grid basicHillClimb(int iterations){
		int size = 5;
		Grid gridStruct = new Grid(size);

		int currentBestEval = gridStruct.evaluate();
		//System.out.println(currentBestEval);
		//System.out.println(gridStruct);
		int newEval;
		int tempVal, tempRow, tempCol;//holds the old value
		for(int i = 0; i <= iterations; i++){

			//gets a random row and column and figures out an acceptable result
			Random randomGenerator = new Random();
			int randRow = 0;
			int randCol = 0;
			do{
				randRow = randomGenerator.nextInt(gridStruct.gridValues.length-1);
				randCol = randomGenerator.nextInt(gridStruct.gridValues.length-1);
			}while(randRow == size - 1 && randCol == size - 1);
			int randVal;

			if (randRow >= randCol) {
					if ((size - 1) - randCol >= randRow - 0) {
						randVal  = randomGenerator.nextInt((size - 1) - randCol) + 1;
					} else {
						randVal  = randomGenerator.nextInt(randRow - 0) + 1;
					}
			} else {
				if ((size - 1) - randRow >= randCol - 0) {
					randVal = randomGenerator.nextInt((size - 1) - randRow) + 1;
				} else {
					randVal =randomGenerator.nextInt(randCol - 0) + 1;
				}
			}

			//System.out.println("current best evaluation: " + currentBestEval);
			//save the old state of the box temporarily while checking if new one is better
			tempVal = gridStruct.gridValues[randRow][randCol].value;
			//System.out.println("Saving temporary value: " + tempVal);

			gridStruct.gridValues[randRow][randCol].value = randVal;
			//System.out.println("changing "+randRow + ", " + randCol+" from: "+tempVal+" to: "+randVal);

			//System.out.println("grid after change:");
			newEval = gridStruct.evaluate();
			//System.out.println(gridStruct);

			//System.out.println("New Evaluation:" + newEval);
			if(newEval>=currentBestEval){
				currentBestEval = newEval;
			}else{
				//System.out.println("Rejected transform, changing back");
				gridStruct.gridValues[randRow][randCol].value = tempVal;
				//System.out.println("---");
				gridStruct.evaluate();
				//System.out.println(gridStruct);
				//System.out.println("---");
			}
			//System.out.println("-------");
		}

		return gridStruct;
	}

	public Grid hillClimbWithRandomRestarts(int restarts, int iterations){
		Grid currentBestGrid = null;
		int currentBestEval = 0;
		for(int i = 0; i <= restarts; i++){
			if(currentBestGrid == null){
				currentBestGrid = this.basicHillClimb(iterations);
				currentBestEval = currentBestGrid.evaluate();
			}else{
				Grid newGrid = this.basicHillClimb(iterations);
				int newEval = newGrid.evaluate();
				if(newEval>currentBestEval){
					currentBestGrid = newGrid;
					currentBestEval = newEval;
				}
			}
		}
		return currentBestGrid;
	}

	public Grid hillClimbWithRandomWalk(double downhillProb, int iterations){
		int size = 5;
		Grid gridStruct = new Grid(size);

		int currentBestEval = gridStruct.evaluate();
		//System.out.println(currentBestEval);
		//System.out.println(gridStruct);
		int newEval;
		int tempVal, tempRow, tempCol;//holds the old value
		for(int i = 0; i <= iterations; i++){

			//gets a random row and column and figures out an acceptable result
			Random randomGenerator = new Random();
			int randRow = 0;
			int randCol = 0;
			do{
				randRow = randomGenerator.nextInt(gridStruct.gridValues.length-1);
				randCol = randomGenerator.nextInt(gridStruct.gridValues.length-1);
			}while(randRow == size - 1 && randCol == size - 1);
			int randVal;

			if (randRow >= randCol) {
					if ((size - 1) - randCol >= randRow - 0) {
						randVal  = randomGenerator.nextInt((size - 1) - randCol) + 1;
					} else {
						randVal  = randomGenerator.nextInt(randRow - 0) + 1;
					}
			} else {
				if ((size - 1) - randRow >= randCol - 0) {
					randVal = randomGenerator.nextInt((size - 1) - randRow) + 1;
				} else {
					randVal =randomGenerator.nextInt(randCol - 0) + 1;
				}
			}

			//System.out.println("current best evaluation: " + currentBestEval);
			//save the old state of the box temporarily while checking if new one is better
			tempVal = gridStruct.gridValues[randRow][randCol].value;
			//System.out.println("Saving temporary value: " + tempVal);

			gridStruct.gridValues[randRow][randCol].value = randVal;
			//System.out.println("changing "+randRow + ", " + randCol+" from: "+tempVal+" to: "+randVal);

			//System.out.println("grid after change:");
			newEval = gridStruct.evaluate();
			//System.out.println(gridStruct);

			//System.out.println("New Evaluation:" + newEval);
			//vvv this is the important part vvv

			boolean acceptDownhill = (new Random().nextDouble()<downhillProb);
			//System.out.println(acceptDownhill);
			if(newEval>=currentBestEval || acceptDownhill){
				currentBestEval = newEval;
			}else{
				//System.out.println("Rejected transform, changing back");
				gridStruct.gridValues[randRow][randCol].value = tempVal;
				//System.out.println("---");
				gridStruct.evaluate();
				//System.out.println(gridStruct);
				//System.out.println("---");
			}
			//System.out.println("-------");
		}

		return gridStruct;
	}

	public Grid simulatedAnnealing(int iterations, int initTemp, double decayRate){
		int currentTemp = initTemp;
		int size = 5;
		Grid gridStruct = new Grid(size);

		int currentBestEval = gridStruct.evaluate();
		//System.out.println(currentBestEval);
		//System.out.println(gridStruct);
		int newEval;
		int tempVal, tempRow, tempCol;//holds the old value
		for(int i = 0; i <= iterations; i++){

			//gets a random row and column and figures out an acceptable result
			Random randomGenerator = new Random();
			int randRow = 0;
			int randCol = 0;
			do{
				randRow = randomGenerator.nextInt(gridStruct.gridValues.length-1);
				randCol = randomGenerator.nextInt(gridStruct.gridValues.length-1);
			}while(randRow == size - 1 && randCol == size - 1);
			int randVal;

			if (randRow >= randCol) {
					if ((size - 1) - randCol >= randRow - 0) {
						randVal  = randomGenerator.nextInt((size - 1) - randCol) + 1;
					} else {
						randVal  = randomGenerator.nextInt(randRow - 0) + 1;
					}
			} else {
				if ((size - 1) - randRow >= randCol - 0) {
					randVal = randomGenerator.nextInt((size - 1) - randRow) + 1;
				} else {
					randVal =randomGenerator.nextInt(randCol - 0) + 1;
				}
			}

			//System.out.println("current best evaluation: " + currentBestEval);
			//save the old state of the box temporarily while checking if new one is better
			tempVal = gridStruct.gridValues[randRow][randCol].value;
			//System.out.println("Saving temporary value: " + tempVal);

			gridStruct.gridValues[randRow][randCol].value = randVal;
			//System.out.println("changing "+randRow + ", " + randCol+" from: "+tempVal+" to: "+randVal);

			//System.out.println("grid after change:");
			newEval = gridStruct.evaluate();
			//System.out.println(gridStruct);

			//System.out.println("New Evaluation:" + newEval);
			//vvv this is the important part vvv
			//System.out.println("downhill prob:" + Math.exp((newEval-currentBestEval)/currentTemp));
			boolean acceptDownhill = (new Random().nextDouble()<Math.exp((newEval-currentBestEval)/currentTemp));
			//System.out.println(acceptDownhill);
			if(acceptDownhill){
				currentBestEval = newEval;
			}else{
				//System.out.println("Rejected transform, changing back");
				gridStruct.gridValues[randRow][randCol].value = tempVal;
				//System.out.println("---");
				gridStruct.evaluate();
				//System.out.println(gridStruct);
				//System.out.println("---");
			}
			//System.out.println("-------");
		}
		currentTemp *= decayRate;
		return gridStruct;
	}
}