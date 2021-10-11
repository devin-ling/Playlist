package lib.app;

/***************************************
 * Authors: Devin Ling & Darren Zhou
 * SongLib Main Application
 **************************************/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lib.view.LibController;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/lib/view/lib.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LibController libController = loader.getController();
		libController.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}
