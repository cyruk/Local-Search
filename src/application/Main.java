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

		TextInputDialog dialog = new TextInputDialog("10");
		dialog.setTitle("Dimensions");
		dialog.setHeaderText("What are the dimensions of your NxN square");
		dialog.setContentText("Please enter your N:");

		Optional<String> result = dialog.showAndWait();
		int n = Integer.valueOf(result.get());

		GridPane grid = new GridPane();

	    for (int row = 0; row < n; row++) {
	        for (int col = 0; col < n; col++) {
	            Rectangle rec = new Rectangle();
	            rec.setWidth(40);
	            rec.setHeight(40);
	            rec.setFill(Color.TRANSPARENT);
	            rec.setStroke(Color.BLACK);

	            //Create container to be able to hold text
	            StackPane pane = new StackPane();
	            pane.getChildren().addAll(rec, new Text(row+","+col));

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