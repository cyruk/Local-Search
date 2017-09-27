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
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


public class Main extends Application {

	Stage window;
	Stage window2;
    Button button;
    Button button1;
    TextField hello;
    TextField hello2;
    TextField hello3;
    TextField hello4;
    TextField hello5;
    TextField hello6;
    Text description;
    Text description2;
    Text description3;
    Text description4;
    Text description5;
    Text description6;
    Grid gridStruct;
	int size = 0;

	@Override
	public void start(Stage primaryStage) throws FileNotFoundException {

		
		window = primaryStage;
        window.setTitle("Choice");
        description = new Text("1: read in a file, 2: random generation, 3: basic hill climbing");
        description2 = new Text("4: hill climbing random restart 5: hill climbing random walk");
        description3 = new Text("6: simulated annealing, 7: genetic algorithm");
        hello = new TextField();
        button = new Button("Button");
        
        button.setOnAction(e -> {
            if (hello.getText().equals("1"))
            {
            	//read from file
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
    		            showGrid(gridStruct, primaryStage);
    		        }
    		        catch(FileNotFoundException ex) 
    				{
    		            System.out.println("Unable to open file '" + file.getName() + "'");                
    		        }
                }
            }
            else if (hello.getText().equals("2"))
            {
            	//random generation
            	description = new Text("Enter the size");
                hello = new TextField();
                button1 = new Button("Button");
                VBox layout = new VBox(10);
                layout.getChildren().addAll(description, hello, button1);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout, 400, 400);
                button1.setOnAction(f -> {
                	gridStruct = new Grid(Integer.parseInt(hello.getText()));
	                showGrid(new Grid(Integer.parseInt(hello.getText())), primaryStage);
                });
                window.setScene(scene);
                window.show();  
            }
            else if (hello.getText().equals("3"))
            {
            	//hill climbing 
            	description = new Text("Enter the size");
                hello = new TextField();
                description2 = new Text("Enter the iterations");
                hello2 = new TextField();
                button1 = new Button("Button");
                VBox layout = new VBox(10);
                layout.getChildren().addAll(description, hello, description2, hello2, button1);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout, 400, 400);
                button1.setOnAction(f -> {
                	gridStruct = basicHillClimb(Integer.parseInt(hello2.getText()), Integer.parseInt(hello.getText()));
	                showGrid(gridStruct, primaryStage);
                });
                window.setScene(scene);
                window.show();  
            }
            else if (hello.getText().equals("4"))
            {
            	//hill climbing random restart
            	description = new Text("Enter the size");
                hello = new TextField();
                description2 = new Text("Enter the iterations");
                hello2 = new TextField();
                description3 = new Text("Enter the restarts");
                hello3 = new TextField();
                button1 = new Button("Button");
                VBox layout = new VBox(10);
                layout.getChildren().addAll(description, hello, description2, hello2, description3, hello3, button1);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout, 400, 400);
                button1.setOnAction(f -> {
                	gridStruct = hillClimbWithRandomRestarts(Integer.parseInt(hello3.getText()), Integer.parseInt(hello2.getText()), Integer.parseInt(hello.getText()));
	                showGrid(gridStruct, primaryStage);
                });
                window.setScene(scene);
                window.show();  
            }
            else if (hello.getText().equals("5"))
            {
            	//hill climbing random walk
            	description = new Text("Enter the size");
                hello = new TextField();
                description2 = new Text("Enter the iterations");
                hello2 = new TextField();
                description3 = new Text("Enter the downhill probability (double)");
                hello3 = new TextField();
                button1 = new Button("Button");
                VBox layout = new VBox(10);
                layout.getChildren().addAll(description, hello, description2, hello2, description3, hello3, button1);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout, 400, 400);
                button1.setOnAction(f -> {
                	gridStruct = hillClimbWithRandomWalk(Double.parseDouble(hello3.getText()), Integer.parseInt(hello2.getText()), Integer.parseInt(hello.getText()));
	                showGrid(gridStruct, primaryStage);
                });
                window.setScene(scene);
                window.show();  
            }        
            else if (hello.getText().equals("6"))
            {
            	//simulated annealing
            	description = new Text("Enter the size");
                hello = new TextField();
                description2 = new Text("Enter the iterations");
                hello2 = new TextField();
                description3 = new Text("Enter the initial Temp (double)");
                hello3 = new TextField();
                description4 = new Text("Enter the decay rate (double)");
                hello4 = new TextField();
                button1 = new Button("Button");
                VBox layout = new VBox(10);
                layout.getChildren().addAll(description, hello, description2, hello2, description3, hello3, description4, hello4, button1);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout, 400, 400);
                button1.setOnAction(f -> {
                	gridStruct = simulatedAnnealing(Integer.parseInt(hello2.getText()), Double.parseDouble(hello3.getText()), 
                			Double.parseDouble(hello4.getText()), Integer.parseInt(hello.getText()));
	                showGrid(gridStruct, primaryStage);
                });
                window.setScene(scene);
                window.show();  
            }
            else if (hello.getText().equals("7"))
            {
            	//genetics
            	description = new Text("Enter the size");
                hello = new TextField();
                description2 = new Text("Enter the iterations");
                hello2 = new TextField();
                description3 = new Text("Enter the initial population");
                hello3 = new TextField();
                description4 = new Text("Enter the tournament size");
                hello4 = new TextField();
                description5 = new Text("Enter the elite size");
                hello5 = new TextField();
                description6 = new Text("Enter the mutation rate (double)");
                hello6 = new TextField();
                button1 = new Button("Button");
                VBox layout = new VBox(10);
                layout.getChildren().addAll(description, hello, description2, hello2, description3, 
                		hello3, description4, hello4, description5, hello5, 
                		 description6, hello6, button1);
                layout.setAlignment(Pos.CENTER);
                Scene scene = new Scene(layout, 400, 400);
                button1.setOnAction(f -> {
                	gridStruct = genetics(Integer.parseInt(hello2.getText()), Integer.parseInt(hello3.getText()), Integer.parseInt(hello4.getText()), 
                			Double.parseDouble(hello6.getText()), Integer.parseInt(hello5.getText()), Integer.parseInt(hello.getText()));
	                showGrid(gridStruct, primaryStage);
                });
                window.setScene(scene);
                window.show();  
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(description, description2, description3, hello, button);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 400, 400);
        window.setScene(scene);
        window.show();  
		
		/*long startTime;
		long endTime;
		int[] values = new int[50];
		int[] time = new int[50];
		
		System.out.println("popSize 40 with 5% tourny 30% elitism 1% mutation");
		for (int i = 0; i < 50; i++)
		{
			startTime = System.nanoTime();
			Grid gridStruct = this.genetics(8000, 40, 4, .01, 0, 11);
			endTime = System.nanoTime();
			values[i] = gridStruct.evaluate();
			time[i] = (int) ((endTime - startTime)/1000000);
		}
		
		for(int i = 0; i < 50; i++)
		{
			System.out.println(values[i]);
		}
		
		for(int i = 0; i < 50; i++)
		{
			System.out.println(time[i]);
		}*/
		
		//testAllSearches();
		/*long endTime;
		long startTime = System.nanoTime();
		Grid gridStruct = this.genetics(10000, 40, 4, .01, 12, 11);
		endTime = System.nanoTime();
		int value = gridStruct.evaluate();
		int time = (int) ((endTime - startTime)/1000000);

		System.out.println("Genetic Algorithm|N:11|Value:" + value + "|SearchTime:" + time);
		System.out.println(gridStruct);*/
	}
	
	public void showGrid(Grid gridStruct, Stage primaryStage)
	{
		GridPane grid = new GridPane();

		gridStruct.setUpFullGridVisualization();//sets up the grid to be visualized with colors, and adds depths(num of moves) from start
		
	    for (int row = 0; row < gridStruct.gridValues.length; row++) {
	        for (int col = 0; col < gridStruct.gridValues.length; col++) {
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
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public int partition(Grid[] grid, int low, int high)
    {
        int pivot = grid[high].evaluate(); 
        int i = (low - 1);
        for (int j = low; j < high; j++)
        {
            if (grid[j].evaluate() <= pivot)
            {
                i++;
                Grid temp = grid[i];
                grid[i] = grid[j];
                grid[j] = temp;
            }
        }
        Grid temp = grid[i+1];
        grid[i + 1] = grid[high];
        grid[high] = temp;
        return i + 1;
    }
	
	public Grid[] sort(Grid[] grid, int low, int high)
    {
        if (low < high)
        {
            int pi = partition(grid, low, high);
            sort(grid, low, pi-1);
            sort(grid, pi+1, high);
        }
        return grid;
    }

	public Grid genetics(int repeat, int popSize, int tournySize, double mutationRate2, int eliteSize, int size)
	{
		//generate the initial population and setting them in an array
		Grid[] parents = new Grid[popSize];
		//grid holder
		Grid[] holder = new Grid[popSize];
		//children of the parents
		//stores the fitness values so evaluate doesn't need to be called multiple times
		int[] fitness = new int[popSize];
		//create probability that the parent is selected for the next generation
		double[] probability = new double[popSize];
		//tournament may be used here
		Grid[] tournamentPop = new Grid[tournySize];
		//tournament number
		int tournyIndex = 0;
		//elitism passes the fittest to the next generation
		int maxIndex = 0;
		int maxFitness = 0;
		//sum adds up the fitness
		double sum = 0;
		//counter for crossover
		int counter = 0;
		//crossover int
		int crossover = 0;
		//random generator
		Random randomGenerator = new Random();
		//random number
		double rand = 0;
		//random value
		int randValue = 0;
		//mutation chance
		//double mutationRate = mutationRate2;
		int mutationRate = (int)(size * size * mutationRate2);
		if(mutationRate ==0)
		{
			mutationRate++;
		}
		//elitism switch 1 = yes
		Grid[] elite = new Grid[popSize];
		int eliteNum = eliteSize;
		int max = 0;
		int asdf = repeat + 1;
		//string array
		String[] chromosomes = new String[popSize];
		
		//random generator to generate the parents for the next generation
		for (int i = 0; i < popSize; i++)
		{
			parents[i] = new Grid(size);
			holder[i] = new Grid(size);
			elite[i] = new Grid(size);
			chromosomes[i] = parents[i].toString();
			if (i < tournySize)
			{
				tournamentPop[i] = new Grid(size);
			}
		}
        
		do
		{
			asdf--;
			for(int i = 0; i < popSize; i++)
			{
					for (int row = 0; row < parents[i].gridValues.length; row++)
					{
						for (int col = 0; col < parents[i].gridValues.length; col++)
						{
							elite[i].gridValues[row][col].value = parents[i].gridValues[row][col].value;
						}
					}
			}
			elite = sort(elite, 0, elite.length - 1);
			
			for (int j = eliteNum; j < popSize; j++)
			{
				max = 0;
				maxIndex = 0;
				for (int i = 0; i < tournySize; i++)
				{
					randValue = randomGenerator.nextInt(popSize);
					tournamentPop[i] = parents[randValue];
					if (tournamentPop[i].evaluate() > max)
					{
						maxIndex = i;
					}
				}
				for (int row = 0; row < parents[j].gridValues.length; row++)
				{
					for (int col = 0; col < parents[j].gridValues.length; col++)
					{
						holder[j].gridValues[row][col].value = tournamentPop[maxIndex].gridValues[row][col].value;
					}
				}
			}
			
			//crossover and mutation work fine
			for(int i = 0; i < popSize; i++)
			{
				//elite are moved to the next generation without changes
				if (i < eliteNum)
				{
					for (int row = 0; row < parents[i].gridValues.length; row++)
					{
						for (int col = 0; col < parents[i].gridValues.length; col++)
						{
							parents[i].gridValues[row][col].value = elite[elite.length - (i + 1)].gridValues[row][col].value;
						}
					}
				}
				else
				{
					//don't change crossover point for the parent until both have been crossed over
					if (i % 2 == 0)
					{
						crossover = randomGenerator.nextInt(parents[i].gridValues.length * parents[i].gridValues.length);
					}
					for (int row = 0; row < parents[i].gridValues.length; row++)
					{
						for (int col = 0; col < parents[i].gridValues.length; col++)
						{
							if (counter < crossover)
							{
								parents[i].gridValues[row][col].value = holder[i].gridValues[row][col].value;
							}
							else
							{
								//even take the values of the next one and odds take the values of the previous
								if (i % 2 == 0)
								{
									parents[i].gridValues[row][col].value = holder[i + 1].gridValues[row][col].value;
								}
								else if (i % 2 == 1)
								{
									parents[i].gridValues[row][col].value = holder[i - 1].gridValues[row][col].value;
								}
							}
							counter++;
						}
					}
					//mutation
					for (int k = 0; k < mutationRate; k++)
					{
						 int randRow = 0;
						   int randCol = 0;
							do
							{
								randRow = randomGenerator.nextInt(parents[i].gridValues.length);
								randCol = randomGenerator.nextInt(parents[i].gridValues.length-1);
							}while(randRow == size - 1 && randCol == size - 1);
							
				
							if (randRow >= randCol) 
							{
								if ((size - 1) - randCol >= randRow - 0) 
								{
									do
									{
										randValue  = randomGenerator.nextInt((size - 1) - randCol) + 1;
									}while(randValue == parents[i].gridValues[randRow][randCol].value);
									parents[i].gridValues[randRow][randCol].value = randValue;
								} 
								else 
								{
									do
									{
										randValue  = randomGenerator.nextInt(randRow - 0) + 1;
									}while(randValue == parents[i].gridValues[randRow][randCol].value);
									parents[i].gridValues[randRow][randCol].value = randValue;
								}
							} 
							else 
							{
								if ((size - 1) - randRow >= randCol - 0) 
								{
									do
									{
										randValue = randomGenerator.nextInt((size - 1) - randRow) + 1;
									}while(randValue == parents[i].gridValues[randRow][randCol].value);
									parents[i].gridValues[randRow][randCol].value = randValue;
								} 
								else 
								{
									do
									{
										randValue =randomGenerator.nextInt(randCol - 0) + 1;
									}while(randValue == parents[i].gridValues[randRow][randCol].value);
									parents[i].gridValues[randRow][randCol].value = randValue;
								}
							}
						}
						 
					
				}
				counter = 0;
			}
		}while(asdf > 0);
		return parents[0];
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
		/*System.out.println("Basic Hill Climbing");
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
	    }*/
		
		System.out.println("Genetic Algorithm (popSize = 100, tournamentSize = 10, mutationRate = .015, eliteSize = 30)");
	    System.out.println("Iter\t5x5\t7x7\t9x9\t11x11");
	    for(int i = 0; i <= 5000; i+=100){
	    	int total5 = 0;
	    	int total7 = 0;
	    	int total9 = 0;
	    	int total11 = 0;
	    	for(int j = 0; j < 50; j++){
	    		total5 += this.genetics(i, 40, 4, .01, 12, 5).evaluate();
	    		total7 += this.genetics(i, 40, 4,  .01, 12, 7).evaluate();
	    		total9 += this.genetics(i, 40, 4, .01, 12, 9).evaluate();
	    		total11 += this.genetics(i, 40, 4, .01, 12, 11).evaluate();
	    	}
	    	int avgForIterations5 = total5/50;
	    	int avgForIterations7 = total7/50;
	    	int avgForIterations9 = total9/50;
	    	int avgForIterations11 = total11/50;
	    	System.out.println(i + "\t" + avgForIterations5 + "\t" + avgForIterations7 + "\t" + avgForIterations9 + "\t" + avgForIterations11);
	    }
	}

}