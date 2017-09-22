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

		TextInputDialog dialog = new TextInputDialog("11");
		dialog.setTitle("Dimensions");
		dialog.setHeaderText("What are the dimensions of your NxN square");
		dialog.setContentText("Please enter your N:");

		Optional<String> result = dialog.showAndWait();
		int n = Integer.valueOf(result.get());

		String searchType = "Simulated Annealing";
		long startTime = System.nanoTime();
		Grid gridStruct = this.simulatedAnnealing(5000, 2, .8, n);
		long endTime = System.nanoTime();
		int gridValue = gridStruct.evaluate();

		int searchTime = (int) ((endTime - startTime)/1000000);
		//System.out.println(searchType + " | N: "+n+ " | Value: "+gridValue+ " | Search Time: " + searchTime+ "ms");

//		GridPane grid = new GridPane();
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
//	    primaryStage.setTitle(searchType + " | N: "+n+ " | Value: "+gridValue+ " | Search Time: " + searchTime+ "ms");
//	    primaryStage.setScene(scene);
//	    primaryStage.show();

	    //this evaluates the shortest number of moves from the given start to the end
	    //System.out.println(gridStruct);
	    //System.out.println(gridStruct.evaluate());

	    //System.out.println(this.basicHillClimb(100000, n).evaluate());
	    //System.out.println(this.hillClimbWithRandomRestarts(100, 1000, n).evaluate());
	    //System.out.println(this.hillClimbWithRandomWalk(.01, 5000, n).evaluate());
	    //System.out.println(this.simulatedAnnealing(5000, 10, .95, n).evaluate());

		printTestGrids();
		//this.testAllSearches();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Grid basicHillClimb(int iterations, int size){
		Grid gridStruct = new Grid(size);

		int currentBestEval = gridStruct.evaluate();
		//System.out.println(currentBestEval);
		//System.out.println(gridStruct);
		int newEval;
		int tempVal;//holds the old value
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

	public Grid hillClimbWithRandomRestarts(int restarts, int iterations, int size){
		Grid currentBestGrid = null;
		int currentBestEval = 0;
		for(int i = 0; i <= restarts; i++){
			if(currentBestGrid == null){
				currentBestGrid = this.basicHillClimb(iterations, size);
				currentBestEval = currentBestGrid.evaluate();
			}else{
				Grid newGrid = this.basicHillClimb(iterations, size);
				int newEval = newGrid.evaluate();
				if(newEval>currentBestEval){
					currentBestGrid = newGrid;
					currentBestEval = newEval;
				}
			}
		}
		return currentBestGrid;
	}

	public Grid hillClimbWithRandomWalk(double downhillProb, int iterations, int size){
		Grid gridStruct = new Grid(size);
		int currentBestEval = gridStruct.evaluate();

		//keeps track of overall best so it never ignores a peak that it may have reached and not returned to
		int overallBestEval = currentBestEval;
		Grid overallBestGrid = new Grid(gridStruct.gridValues.length);
		overallBestGrid.cloneFrom(gridStruct);
		//System.out.println(currentBestEval);
		//System.out.println(gridStruct);
		int newEval;
		int tempVal;//holds the old value
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
			//System.out.println(overallBestGrid.evaluate() + " | " + currentBestEval);
			if(newEval>=currentBestEval || acceptDownhill){
				currentBestEval = newEval;

				if(currentBestEval>overallBestEval){
					overallBestEval = currentBestEval;
					overallBestGrid.cloneFrom(gridStruct);
					//overallBestGridValues = gridStruct.gridValues.clone();

				}
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
		gridStruct = overallBestGrid;
		gridStruct.evaluate();
		return gridStruct;
	}

	public Grid simulatedAnnealing(int iterations, double initTemp, double decayRate, int size){
		double currentTemp = initTemp;
		Grid gridStruct = new Grid(size);

		int currentBestEval = gridStruct.evaluate();

		//keeps track of overall best so it never ignores a peak that it may have reached and not returned to
		int overallBestEval = currentBestEval;
		Grid overallBestGrid = new Grid(gridStruct.gridValues.length);
		overallBestGrid.cloneFrom(gridStruct);
		//System.out.println(currentBestEval);
		//System.out.println(gridStruct);
		int newEval;
		int tempVal;//holds the old value
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
			if(newEval>currentBestEval || acceptDownhill){
				currentBestEval = newEval;
				if(newEval>overallBestEval){
					overallBestEval = currentBestEval;
					overallBestGrid.cloneFrom(gridStruct);
				}
			}else{
				//System.out.println("Rejected transform, changing back");
				gridStruct.gridValues[randRow][randCol].value = tempVal;
				//System.out.println("---");
				gridStruct.evaluate();
				//System.out.println(gridStruct);
				//System.out.println("---");
			}
			//System.out.println("-------");
			currentTemp = decayRate*currentTemp;
		}

		gridStruct = overallBestGrid;
		gridStruct.evaluate();
		return gridStruct;
	}

	public void printTestGrids(){

		int[] testSizes = {5, 7, 9, 11};

		for(int i = 0; i < testSizes.length; i++){
			String searchType = "Basic Hill Climbing";
			long startTime = System.nanoTime();
			Grid gridStruct = this.basicHillClimb(100000, testSizes[i]);
			long endTime = System.nanoTime();
			int gridValue = gridStruct.evaluate();

			int searchTime = (int) ((endTime - startTime)/1000000);
			System.out.println(searchType + " | N: "+testSizes[i]+ " | Value: "+gridValue+ " | Search Time: " + searchTime+ "ms");
			System.out.println(gridStruct);
		}

		for(int i = 0; i < testSizes.length; i++){
			String searchType = "Hill Climbing With Random Restarts";
			long startTime = System.nanoTime();
			Grid gridStruct = this.hillClimbWithRandomRestarts(50, 2000, testSizes[i]);
			long endTime = System.nanoTime();
			int gridValue = gridStruct.evaluate();

			int searchTime = (int) ((endTime - startTime)/1000000);
			System.out.println(searchType + " | N: "+testSizes[i]+ " | Value: "+gridValue+ " | Search Time: " + searchTime+ "ms");
			System.out.println(gridStruct);
		}

		for(int i = 0; i < testSizes.length; i++){
			String searchType = "Hill Climb With Random Walk";
			long startTime = System.nanoTime();
			Grid gridStruct = this.hillClimbWithRandomWalk(.01, 100000, testSizes[i]);
			long endTime = System.nanoTime();
			int gridValue = gridStruct.evaluate();

			int searchTime = (int) ((endTime - startTime)/1000000);
			System.out.println(searchType + " | N: "+testSizes[i]+ " | Value: "+gridValue+ " | Search Time: " + searchTime+ "ms");
			System.out.println(gridStruct);
		}

		for(int i = 0; i < testSizes.length; i++){
			String searchType = "Simulated Annealing";
			long startTime = System.nanoTime();
			Grid gridStruct = this.simulatedAnnealing(100000, 2, .95, testSizes[i]);
			long endTime = System.nanoTime();
			int gridValue = gridStruct.evaluate();

			int searchTime = (int) ((endTime - startTime)/1000000);
			System.out.println(searchType + " | N: "+testSizes[i]+ " | Value: "+gridValue+ " | Search Time: " + searchTime+ "ms");
			System.out.println(gridStruct);
		}

	}

	public void testAllSearches(){
	    //Testing code
//		System.out.println("Basic Hill Climbing");
//	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
//	    for(int i = 0; i <= 5000; i+=100){
//	    	int total5 = 0;
//	    	int total7 = 0;
//	    	int total9 = 0;
//	    	int total11 = 0;
//	    	for(int j = 0; j < 50; j++){
//	    		total5 += this.basicHillClimb(i, 5).evaluate();
//	    		total7 += this.basicHillClimb(i, 7).evaluate();
//	    		total9 += this.basicHillClimb(i, 9).evaluate();
//	    		total11 += this.basicHillClimb(i, 11).evaluate();
//	    	}
//	    	int avgForIterations5 = total5/50;
//	    	int avgForIterations7 = total7/50;
//	    	int avgForIterations9 = total9/50;
//	    	int avgForIterations11 = total11/50;
//	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
//	    }
//
//  	    System.out.println("Hill Climb With Random Restarts (50 restarts)");
//	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
//	    for(int i = 0; i <= 5000; i+=100){
//	    	int total5 = 0;
//	    	int total7 = 0;
//	    	int total9 = 0;
//	    	int total11 = 0;
//	    	for(int j = 0; j < 50; j++){
//	    		total5 += this.hillClimbWithRandomRestarts(50, i/50, 5).evaluate();
//	    		total7 += this.hillClimbWithRandomRestarts(50, i/50, 7).evaluate();
//	    		total9 += this.hillClimbWithRandomRestarts(50, i/50, 9).evaluate();
//	    		total11 += this.hillClimbWithRandomRestarts(50, i/50, 11).evaluate();
//	    	}
//	    	int avgForIterations5 = total5/50;
//	    	int avgForIterations7 = total7/50;
//	    	int avgForIterations9 = total9/50;
//	    	int avgForIterations11 = total11/50;
//	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
//	    }

	    System.out.println("Hill Climb with Random Walk (p = .01)");
	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
	    for(int i = 0; i <= 5000; i+=100){
	    	int total5 = 0;
	    	int total7 = 0;
	    	int total9 = 0;
	    	int total11 = 0;
	    	for(int j = 0; j < 50; j++){
	    		total5 += this.hillClimbWithRandomWalk(.01, i, 5).evaluate();
	    		total7 += this.hillClimbWithRandomWalk(.01, i, 7).evaluate();
	    		total9 += this.hillClimbWithRandomWalk(.01, i, 9).evaluate();
	    		total11 += this.hillClimbWithRandomWalk(.01, i, 11).evaluate();
	    	}
	    	int avgForIterations5 = total5/50;
	    	int avgForIterations7 = total7/50;
	    	int avgForIterations9 = total9/50;
	    	int avgForIterations11 = total11/50;
	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
	    }

	    System.out.println("Simulated Annealing (temp = 2, decay = .95)");
	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
	    for(int i = 0; i <= 5000; i+=100){
	    	int total5 = 0;
	    	int total7 = 0;
	    	int total9 = 0;
	    	int total11 = 0;
	    	for(int j = 0; j < 50; j++){
	    		total5 += this.simulatedAnnealing(i, 2, .95, 5).evaluate();
	    		total7 += this.simulatedAnnealing(i, 2, .95, 7).evaluate();
	    		total9 += this.simulatedAnnealing(i, 2, .95, 9).evaluate();
	    		total11 += this.simulatedAnnealing(i, 2, .95, 11).evaluate();
	    	}
	    	int avgForIterations5 = total5/50;
	    	int avgForIterations7 = total7/50;
	    	int avgForIterations9 = total9/50;
	    	int avgForIterations11 = total11/50;
	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
	    }
	}
}