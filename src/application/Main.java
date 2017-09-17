package application;

import java.util.Optional;
import java.util.Random;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
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

		GridPane grid = new GridPane();
		Random randomGenerator = new Random();
	    int randomInt;

	    for (int row = 0; row < n; row++) {
	        for (int col = 0; col < n; col++) {
	        	Text hello;
	            Rectangle rec = new Rectangle();
	            rec.setWidth(40);
	            rec.setHeight(40);
	            rec.setFill(Color.TRANSPARENT);
	            rec.setStroke(Color.BLACK);

	            //Create container to be able to hold text
	            StackPane pane = new StackPane();
	            
	            //checks what numbers can be generated in that square
	            if (row >= col)
	            {
	            	if (row == n-1 && col == n-1)
	            	{
	            		hello = new Text("0");
	            	}
	            	else
	            	{
		            	if ((n-1) - col >= row - 0)
		            	{
		            		hello = new Text(Integer.toString(randomGenerator.nextInt((n-1) - col) + 1));
		            	}
		            	else
		            	{
		            		hello = new Text(Integer.toString(randomGenerator.nextInt(row - 0) + 1));
		            	}
	            	}
	            }
	            else 
	            {
	            	if ((n-1) - row >= col - 0)
	            	{
	            		hello = new Text(Integer.toString(randomGenerator.nextInt((n-1) - row) + 1));
	            	}
	            	else
	            	{
	            		hello = new Text(Integer.toString(randomGenerator.nextInt(col - 0) + 1));
	            	}
	            }
	            
	            //add in text
	            pane.getChildren().addAll(rec, hello);

	            GridPane.setRowIndex(pane, row);
	            GridPane.setColumnIndex(pane, col);
	            grid.getChildren().add(pane);
	        }
	    }

	    Scene scene = new Scene(grid, 500, 500);

	    primaryStage.setTitle("Grid");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}