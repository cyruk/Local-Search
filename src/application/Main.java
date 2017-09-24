package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.FileChooser;
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
	public void start(Stage primaryStage) throws FileNotFoundException {

		/*TextInputDialog dialog = new TextInputDialog("1");
		dialog.setTitle("Choice");
		dialog.setHeaderText("1: read from file \n2: choose something else");
		dialog.setContentText("Enter number:");

		Optional<String> result = dialog.showAndWait();
		int n = Integer.valueOf(result.get());
		
		Grid gridStruct;
		int size = 0;
		gridStruct = new Grid(n);
		
		/*if (n == 1)
		{
			int row = 0;
			int col = 0;
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) 
			{
				try 
				{
		            Scanner inFile = new Scanner(new FileReader(file.getPath()));
		            size = inFile.nextInt();
		            gridStruct = new Grid(size);
		            System.out.println(size);
		            while(inFile.hasNext()) 
		            {
	            		gridStruct.gridValues[row][col].value = inFile.nextInt();
	            		col++;
	            		if (col == size)
	            		{
	            			col = 0;
	            			row++;
	            		}
		            }   
		            inFile.close();
		            System.out.println(gridStruct);
		            System.out.println(gridStruct.evaluate());
		        }
		        catch(FileNotFoundException ex) 
				{
		            System.out.println("Unable to open file '" + file.getName() + "'");                
		        }
            }
		}*/
		TextInputDialog dialog = new TextInputDialog("11");
		dialog.setTitle("Dimensions");
		dialog.setHeaderText("What are the dimensions of your NxN square");
		dialog.setContentText("Please enter your N:");

		Optional<String> result = dialog.showAndWait();
		int n = Integer.valueOf(result.get());

		Grid gridStruct = new Grid(n); //created a data structure to represent the grid and cells and moved generation code there
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

	    primaryStage.setTitle("Evaluation: " + gridStruct.evaluate());
	    primaryStage.setScene(scene);
	    //primaryStage.show();
	    
	    Grid hello = genetics(gridStruct.gridValues.length, 10000, 3, 10, 100, 10);
	    System.out.println(hello);
	    System.out.println(hello.evaluate());
	}

	private TextInputDialog newTextInputDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Grid genetics(int size, int n, int choice, int crossover, int popSize, int tournamentSize)
	{
		/*Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter initial population size: ");
		int popSize = sc.nextInt();
		
		System.out.print("1: single point, 2: two point, 3: uniform crossover ");
		int choice = sc.nextInt();
		
		int choice3 = 0;
		int choice2 = 0;
		
		if (choice == 1)
		{
			System.out.print("1: fixed, 2: random ");
			choice2 = sc.nextInt();
			if (choice2 == 1)
			{
				System.out.print("What is the crossover point ");
				choice3 = sc.nextInt();
			}
		}*/
		int choice3 = 0;
		//create an initial population
		Grid[] chromosomes = new Grid[popSize];
		Grid[] chosen = new Grid[popSize];
		Grid[] tournament = new Grid[tournamentSize];
		for (int i = 0; i < popSize; i++)
		{
			chromosomes[i] = new Grid(size);
			chosen[i] = new Grid(size);
		}
		
		double[] evaluations = new double[popSize];
		double[] probability = new double[popSize];
		double sum = 0;
		double rand;
		double mutationRate = .015;
		double crossoverRate = .9;
		int max = 0;
		int probCounter = 0;
		int counter = 0;
		Random randomGenerator = new Random();
		int randRow = 0;
		int randCol = 0;
		int randValue = 0;

		for (int w = 0; w < n; w++)
		{
			sum = 0;
			
			
				/*
				//evaluate every chromosome
				for(int i = 0; i < chromosomes.length; i++)
				{
					evaluations[i] = chromosomes[i].evaluate();
					if (evaluations[i] > 0)
					{
						sum += evaluations[i];
					}
					else
					{
						sum += Math.abs(1/(evaluations[i] - 1));
					}
				}
				//calculate the probability and selects the best candidate to move onto the next generation
				//very skewed towards positive values negative values have a nearly zero percent chance of being selected
				for(int i = 0; i < chromosomes.length; i++)
				{
					if (i != 0)
					{
						if (evaluations[i] > 0)
						{
							//stack the probabilities 
							probability[i] = (evaluations[i] / sum) + probability[i - 1];
						}
						else
						{
							probability[i] = (Math.abs(1/(evaluations[i] - 1)) / sum) + probability[i - 1];
						}
					}
					else
					{
						probability[i] = evaluations[i] / sum;
					}
					if (evaluations[i] > evaluations[max])
					{
						max = i;
					}
				}
				
				//chooses the next generation
				for (int i = 0; i < chromosomes.length; i++)
				{
					rand = randomGenerator.nextDouble();				
					if (i != 0)
					{
						probCounter = 0;
						do
						{
							probCounter++;
							if (probCounter == popSize)
							{
								probCounter = popSize - 1;
								break;
							}
						}while(rand > probability[probCounter]);	
					}
					else
					{
						probCounter = max;
					}
					chosen[i].gridValues = chromosomes[probCounter].gridValues;
					probCounter = 0;
				}*/
			
			//get the tournament size then evaluate them all then choose the best ones
			for (int j = 0; j < popSize; j++)
			{
				//select the best from the tournament
				for (int i = 0; i < tournamentSize; i++)
				{
					randValue = randomGenerator.nextInt(popSize);
					if (chromosomes[randValue].evaluate() > max)
					{
						chosen[j].gridValues = chromosomes[randValue].gridValues;
						max = chromosomes[randValue].evaluate();
					}
				}
			}
			
			
			
			//figure out crossovers
			for (int i = 0; i < chromosomes.length; i++)
			{
				//single crossover at a chosen point or at a random crossover point
				if (choice == 1)
				{
					for (int row = 0; row < size; row++)
					{
						for (int col = 0; col < size; col++)
						{
							if (choice == 2 && i % 2 == 0)
							{
								crossover = randomGenerator.nextInt(size * size) + 1; //random crossover
							}
							if (counter < crossover)
							{
								chromosomes[i].gridValues[row][col].value = chosen[i].gridValues[row][col].value;
							}
							else
							{
								if (i % 2 == 0)
								{
									chromosomes[i].gridValues[row][col].value = chosen[i + 1].gridValues[row][col].value;
								}
								else if (i % 2 == 1)
								{
									chromosomes[i].gridValues[row][col].value = chosen[i - 1].gridValues[row][col].value;
								}
							}
							
							counter++;
						}
					}
				}
				else if (choice == 2) //two point cross over
				{
					choice3 = size * size / 4;
					int secondPoint = (int)(size * size / (1.5));
					for (int row = 0; row < size; row++)
					{
						for (int col = 0; col < size; col++)
						{
							if (counter < choice3 || counter > secondPoint)
							{
								chromosomes[i].gridValues[row][col].value = chosen[i].gridValues[row][col].value;
							}
							else
							{
								if (i % 2 == 0)
								{
									chromosomes[i].gridValues[row][col].value = chosen[i + 1].gridValues[row][col].value;
								}
								else if (i % 2 == 1)
								{
									chromosomes[i].gridValues[row][col].value = chosen[i - 1].gridValues[row][col].value;
								}
							}
							
							counter++;
						}
					}
				}
				else //uniform one
				{
					for (int row = 0; row < size; row++)
					{
						for (int col = 0; col < size; col++)
						{
							if (randomGenerator.nextDouble() <= crossoverRate)
							{
								chromosomes[i].gridValues[row][col].value = chosen[i].gridValues[row][col].value;
							}
							else
							{
								if (i % 2 == 0)
								{
									chromosomes[i].gridValues[row][col].value = chosen[i + 1].gridValues[row][col].value;
								}
								else if (i % 2 == 1)
								{
									chromosomes[i].gridValues[row][col].value = chosen[i - 1].gridValues[row][col].value;
								}
							}
							
							counter++;
						}
					}
				}
				counter = 0;
				for (int row = 0; row < size; row++)
				{
					for (int col = 0; col < size; col++)
					{
						if (randomGenerator.nextDouble() <= mutationRate)
						{
							if (randRow >= randCol) 
							{
									if ((size - 1) - randCol >= randRow - 0) 
									{
										do
										{
											randValue = randomGenerator.nextInt((size - 1) - randCol) + 1;
										}while(randValue == chromosomes[i].gridValues[randRow][randCol].value);
										chromosomes[i].gridValues[randRow][randCol].value  = randValue;
									} 
									else 
									{
										do
										{
											randValue = randomGenerator.nextInt(randRow - 0) + 1;
										}while(randValue == chromosomes[i].gridValues[randRow][randCol].value);
										chromosomes[i].gridValues[randRow][randCol].value  = randValue;
									}
							} 
							else 
							{
								if ((size - 1) - randRow >= randCol - 0) 
								{
									do
									{
										randValue = randomGenerator.nextInt((size - 1) - randRow) + 1;
									}while(randValue == chromosomes[i].gridValues[randRow][randCol].value);
									chromosomes[i].gridValues[randRow][randCol].value  = randValue;
								} 
								else 
								{
									do
									{
										randValue =randomGenerator.nextInt(randCol - 0) + 1;
									}while(randValue == chromosomes[i].gridValues[randRow][randCol].value);
									chromosomes[i].gridValues[randRow][randCol].value  = randValue;
								}
							}
						}
					}
				}
			}
		}
		for (int j = 0; j < popSize; j++)
		{
			if (chromosomes[j].evaluate() > max)
			{
				max = j;
			}
		}
		return chromosomes[max];
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

}