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
		int[][] sideArray = new int[n][n];

		Grid gridStruct = new Grid(n); //created a data structure to represent the grid and cells and moved generation code there
		GridPane grid = new GridPane();

	    for (int row = 0; row < n; row++) {
	        for (int col = 0; col < n; col++) {
	        	Text numberText;
	            Rectangle rec = new Rectangle();
	            rec.setWidth(40);
	            rec.setHeight(40);
	            rec.setFill(Color.TRANSPARENT);
	            rec.setStroke(Color.BLACK);

	            //Create container to be able to hold text
	            StackPane pane = new StackPane();
	            numberText = new Text(Integer.toString(gridStruct.getCell(row, col).value));

	            //add in text
	            pane.getChildren().addAll(rec, numberText);
	            GridPane.setRowIndex(pane, row);
	            GridPane.setColumnIndex(pane, col);
	            grid.getChildren().add(pane);
	        }
	    }

	    Scene scene = new Scene(grid, 500, 500);
	    
    	int visited = 1;
	    Cell[] cellular;
	    Tree tree = new Tree(gridStruct.gridValues[0][0].value, gridStruct.gridValues[0][0].row, gridStruct.gridValues[0][0].col);
	    gridStruct.gridValues[0][0].isVisited = true;
	    //sideArray keeps track of visited squares
	    sideArray[0][0] = 1;
	    Queue newQueue = new Queue(tree.root);
	    Node temp = tree.root;
    	
	    //using breadth first search here
	    while (!newQueue.isEmpty())
    	{
    		temp = newQueue.dequeue();
    		//breaks when it finds the goal which is not what you want
    		if (temp.row == n - 1 && temp.col == n - 1)
    		{
    			break;
    		}
    		//gets the "children" for the cell
    		//getting an arrayindexoutofboundsexception somewhere here
    		cellular = gridStruct.getAllNeighbors(temp.row, temp.col);
    		for (int i = 0; i < cellular.length; i++)
    		{
    			if (cellular[i] != null && temp.visited != visited)
    			{
    				cellular[i].isVisited = true;
    				temp.visited = visited;
    				sideArray[cellular[i].row][cellular[i].col] = 1;
    				newQueue.enqueue(new Node(cellular[i].value, cellular[i].row, cellular[i].col));
    			}
    		}
    	}
	    
	    for (int i = 0; i < n; i++)
	    {
	    	for (int j = 0; j < n; j++)
	    	{
	    		System.out.print(sideArray[i][j]);
	    	}
	    	System.out.println();
	    }
	    
	    primaryStage.setTitle("Grid");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}