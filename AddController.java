package lib.view;

/***************************************
 * Authors: Devin Ling
 * AddController
 **************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;

public class AddController {

    @FXML private Button finaladd;
    @FXML private Button cancelbutton;
    @FXML private TextField nameField;
    @FXML private TextField artistField;
    @FXML private TextField albumField;
    @FXML private TextField yearField;
    @FXML private AnchorPane rootPane;
    
    @FXML
    void addSong(ActionEvent event) throws IOException {
    	
    	String name = nameField.getCharacters().toString().trim();
    	String artist = artistField.getCharacters().toString().trim();
    	String album = albumField.getCharacters().toString().trim();
    	String year = yearField.getCharacters().toString().trim();
    	
    	
    	if (name.length()==0 || artist.length()==0) {
    		Alert alert = new Alert(AlertType.WARNING);
    		alert.setTitle("Warning!");
    		alert.setHeaderText("Must Fill Required(*) Fields");
    		alert.showAndWait();
    	} else {
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setHeaderText("Add Song?");
    		ButtonType buttonAdd = new ButtonType("Add");
    		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    		alert.getButtonTypes().setAll(buttonAdd,buttonCancel);
    		
    		Optional<ButtonType> res = alert.showAndWait();
    		
    		if (res.get()==buttonAdd) {
    			
    			if (name.contains("|") || artist.contains("|") || album.contains("|")) {
    				Alert alert2 = new Alert(AlertType.WARNING);
    				alert2.setTitle("Warning!");
    				alert2.setHeaderText("Cannot Include Pipe(|) Symbol in Fields");
    				alert2.showAndWait();
    				return;
    			}
    			
    			try {
    	    		if (year.length()!=0) {
    	    			int temp = Integer.parseInt(year);
    	    		}
    	    	} catch (NumberFormatException e){
    	    		Alert alert1 = new Alert(AlertType.WARNING);
    	    		alert1.setTitle("Warning!");
    	    		alert1.setHeaderText("Year Must Be Number");
    	    		alert1.showAndWait();
    	    		return;
    	    	}
    			
    			String line="";
    	    	try {
    	    		File songFile = new File("songlist.csv");
    	    		if (!songFile.createNewFile()) {
    	    			BufferedReader br = new BufferedReader(new FileReader("songlist.csv"));
    	    	    	while ((line=br.readLine())!=null) {
    	    	    		String[] songs = line.split("\\|");
    	    	    		if (songs[0].equalsIgnoreCase(name) && songs[1].equalsIgnoreCase(artist)) {
    	    	    			Alert alert1 = new Alert(AlertType.ERROR);
    	    	    			alert1.setTitle("Error");
    	    	    			alert1.setHeaderText("Song Name and Artist Already Exist");
    	    	    			alert1.showAndWait();
    	    	    			br.close();
    	    	    			return;
    	    	    		}
    	    	    	}
    	    	    	br.close();
    	    		}
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}
    	    	
    	    	
    			FileWriter writer = new FileWriter("songlist.csv",true);
        		writer.append(name);
        		writer.append("|");
        		writer.append(artist);
        		writer.append("|");
        		writer.append(album);
        		writer.append("|");
        		writer.append(year);
        		writer.append("\n");
        		
        		writer.flush();
        		writer.close();

        		
        		FXMLLoader loader = new FXMLLoader();
        		loader.setLocation(getClass().getResource("/lib/view/lib.fxml"));
        		AnchorPane root = loader.load();
        		LibController libController = loader.getController();
        		libController.added(name, artist, album, year);
        		
        		//root = FXMLLoader.load(getClass().getResource("lib.fxml"));
            	rootPane.getChildren().setAll(root);
    		}
    	}
    }
    
    @FXML 
    void cancelAdd(ActionEvent event) throws IOException {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Are you sure you want to cancel?");
		ButtonType buttonYes = new ButtonType("Yes");
		ButtonType buttonNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonYes,buttonNo);
		
		Optional<ButtonType> res = alert.showAndWait();
		
		if (res.get()==buttonYes) {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/lib/view/lib.fxml"));
			AnchorPane root = loader.load();
			LibController libController = loader.getController();
			libController.canceled();
			
			//root = FXMLLoader.load(getClass().getResource("lib.fxml"));
	    	rootPane.getChildren().setAll(root);
		}
    }
    
}
