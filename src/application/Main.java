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

		Grid gridStruct = new Grid(n); //created a data structure to represent the grid and cells and moved generation code there
		Grid gridStruct1 = new Grid(n);
	    Grid gridStruct2 = new Grid(n);
		GridPane grid = new GridPane();

		gridStruct.setUpFullGridVisualization();//sets up the grid to be visualized with colors, and adds depths(num of moves) from start

	    for (int row = 0; row < n; row++) {
	        for (int col = 0; col < n; col++) {
	        	Text numberText;
	            Rectangle rec = new Rectangle();
	            rec.setWidth(40);
	            rec.setHeight(40);

	            if(gridStruct.getCell(row, col).depth == 0 && gridStruct.getCell(row, col).isVisited){
	            	rec.setFill(Color.LIGHTBLUE);
	            }else if(gridStruct.getCell(row, col).isVisited){
	            	rec.setFill(Color.GREEN);
	            }else{
	            	rec.setFill(Color.RED);
	            }
	            rec.setStroke(Color.BLACK);

	            //Create container to be able to hold text
	            StackPane pane = new StackPane();
	            numberText = new Text(gridStruct.getCell(row, col).value+":"+gridStruct.getCell(row, col).depth);

	            //add in text
	            pane.getChildren().addAll(rec, numberText);
	            GridPane.setRowIndex(pane, row);
	            GridPane.setColumnIndex(pane, col);
            grid.getChildren().add(pane);
	        }
	    }

	    Scene scene = new Scene(grid, 500, 500);

	    primaryStage.setTitle("Grid");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    System.out.println(gridStruct);
	    System.out.println(gridStruct.evaluate());
	    System.out.println(gridStruct1);
	    System.out.println(gridStruct1.evaluate());
	    System.out.println(gridStruct2);
	    System.out.println(gridStruct2.evaluate());
	    
	    Grid[] hello = {gridStruct, gridStruct1, gridStruct2};
	    hello = genetics(hello, 3, 5, 1);
	    
	    System.out.println(hello[0]);
	    System.out.println(hello[0].evaluate());
	    System.out.println(hello[1]);
	    System.out.println(hello[1].evaluate());
	    System.out.println(hello[2]);
	    System.out.println(hello[2].evaluate());
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
		Cell[][] overallBestGridValues = gridStruct.gridValues.clone();
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
			if(newEval>=currentBestEval || acceptDownhill){
				currentBestEval = newEval;
				if(currentBestEval>overallBestEval){
					overallBestEval = currentBestEval;
					overallBestGridValues = gridStruct.gridValues.clone();
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
		gridStruct.gridValues = overallBestGridValues;
		gridStruct.evaluate();
		return gridStruct;
	}

	public Grid simulatedAnnealing(int iterations, double initTemp, double decayRate, int size){
		double currentTemp = initTemp;
		Grid gridStruct = new Grid(size);

		int currentBestEval = gridStruct.evaluate();

		//keeps track of overall best so it never ignores a peak that it may have reached and not returned to
		int overallBestEval = currentBestEval;
		Cell[][] overallBestGridValues = gridStruct.gridValues.clone();
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
			if(acceptDownhill){
				currentBestEval = newEval;
				if(newEval>overallBestEval){
					overallBestEval = currentBestEval;
					overallBestGridValues = gridStruct.gridValues.clone();
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
			currentTemp *= decayRate;
		}
		gridStruct.gridValues = overallBestGridValues;
		gridStruct.evaluate();
		return gridStruct;
	}

	public void testAllSearches(){
	    //Testing code
		System.out.println("Basic Hill Climbing");
	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
	    for(int i = 0; i <= 5000; i+=100){
	    	int total5 = 0;
	    	int total7 = 0;
	    	int total9 = 0;
	    	int total11 = 0;
	    	for(int j = 0; j < 50; j++){
	    		total5 += this.basicHillClimb(i, 5).evaluate();
	    		total7 += this.basicHillClimb(i, 7).evaluate();
	    		total9 += this.basicHillClimb(i, 9).evaluate();
	    		total11 += this.basicHillClimb(i, 11).evaluate();
	    	}
	    	int avgForIterations5 = total5/50;
	    	int avgForIterations7 = total7/50;
	    	int avgForIterations9 = total9/50;
	    	int avgForIterations11 = total11/50;
	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
	    }

  	    System.out.println("Hill Climb With Random Restarts (50 restarts)");
	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
	    for(int i = 0; i <= 5000; i+=100){
	    	int total5 = 0;
	    	int total7 = 0;
	    	int total9 = 0;
	    	int total11 = 0;
	    	for(int j = 0; j < 50; j++){
	    		total5 += this.hillClimbWithRandomRestarts(50, i/50, 5).evaluate();
	    		total7 += this.hillClimbWithRandomRestarts(50, i/50, 7).evaluate();
	    		total9 += this.hillClimbWithRandomRestarts(50, i/50, 9).evaluate();
	    		total11 += this.hillClimbWithRandomRestarts(50, i/50, 11).evaluate();
	    	}
	    	int avgForIterations5 = total5/50;
	    	int avgForIterations7 = total7/50;
	    	int avgForIterations9 = total9/50;
	    	int avgForIterations11 = total11/50;
	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
	    }

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
	
	public Grid[] genetics(Grid[] chromosomes, int crossover, int size, int n)
	{
		int randRow = 0;
		int randCol = 0;
		for (int w = 0; w < n; w++)
		{
			double a = chromosomes[0].evaluate();
			double b = chromosomes[1].evaluate();
			double c = chromosomes[2].evaluate();
			
			//figure out a better way
			if (a < 0)
			{
				a = 1/Math.abs(a - 1);
			}
			
			if (b < 0)
			{
				b = 1/Math.abs(b - 1);
			}
			
			if (c < 0)
			{
				c = 1/Math.abs(c - 1);
			}
			
			double prob1 = a / (a + b + c);
			double prob2 = b / (a + b + c);
			
			Random randomGenerator = new Random();
			double randChoser;
			Grid[] chosen = new Grid[3];
			prob1 = .333;
			prob2 = .333;
			//choose which one has children based on probability and works correctly
			for (int i = 0; i < 3; i++)
			{
				randChoser = randomGenerator.nextDouble();
				if (randChoser <= prob1)
				{
					System.out.println("1");
					chosen[i] = chromosomes[0];
				}
				else if (randChoser <= prob1 + prob2)
				{
					System.out.println("2");
					chosen[i] = chromosomes[1];
				}
				else
				{
					System.out.println("3");
					chosen[i] = chromosomes[2];
				}
			}
			
			System.out.println();
			System.out.println("chromosome " + 0);
			System.out.println(chromosomes[0]);
			System.out.println();
			System.out.println("chromosome " + 1);
			System.out.println(chromosomes[1]);
			System.out.println();
			System.out.println("chromosome " + 2);
			System.out.println(chromosomes[2]);
			//crossover and mutation
			for (int i = 0; i < 3; i++)
			{
				for(int row = 0; row < size; row++)
				{
					for(int col = 0; col < size; col++)
					{
						//anything less than the crossover value is the original
						if (col < crossover)
						{
							chromosomes[i].gridValues[row][col] = chosen[i].gridValues[row][col];
						}
						else
						{
							if (i == 2)
							{
								chromosomes[i].gridValues[row][col] = chosen[0].gridValues[row][col];
							}
							else
							{
								chromosomes[i].gridValues[row][col] = chosen[i + 1].gridValues[row][col];
							}
						}
					}
				}
				System.out.println();
				System.out.println("chromosome " + i);
				System.out.println(chromosomes[i]);
				do{
					randRow = randomGenerator.nextInt(size-1);
					randCol = randomGenerator.nextInt(size-1);
				}while(randRow == size - 1 && randCol == size - 1);
	
				int tempValue;
				if (randRow >= randCol) 
				{
						if ((size - 1) - randCol >= randRow - 0) 
						{
							do
							{
								tempValue = randomGenerator.nextInt((size - 1) - randCol) + 1;
							} while(tempValue == chromosomes[i].gridValues[randRow][randCol].value);
							chromosomes[i].gridValues[randRow][randCol].value  = tempValue;
						} 
						else 
						{
							do
							{
								tempValue = randomGenerator.nextInt(randRow - 0) + 1;
							} while(tempValue == chromosomes[i].gridValues[randRow][randCol].value);
							chromosomes[i].gridValues[randRow][randCol].value  = tempValue;
						}
				} 
				else 
				{
					if ((size - 1) - randRow >= randCol - 0) 
					{
						do
						{
							tempValue = randomGenerator.nextInt((size - 1) - randRow) + 1;
						} while(tempValue == chromosomes[i].gridValues[randRow][randCol].value);
						chromosomes[i].gridValues[randRow][randCol].value  = tempValue;
					} 
					else 
					{
						do
						{
							tempValue = randomGenerator.nextInt(randCol - 0) + 1;
						} while(tempValue == chromosomes[i].gridValues[randRow][randCol].value);
						chromosomes[i].gridValues[randRow][randCol].value  = tempValue;
					}
				}
				/*System.out.println();
				System.out.println("chromosome " + i);
				System.out.println(chromosomes[i]);*/
			}
		}
		
		return chromosomes;
	}
}