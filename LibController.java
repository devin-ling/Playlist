package lib.view;

/***************************************
 * Authors: Devin Ling
 * LibController
 **************************************/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LibController {

    @FXML private ListView<String> songView;
    @FXML private Button addbutton;
    @FXML private Button editbutton;
    @FXML private Button deletebutton;
    @FXML private AnchorPane rootPane;
    @FXML private TextField nameField;
    @FXML private TextField albumField;
    @FXML private TextField artistField;
    @FXML private TextField yearField;
    @FXML private Label namelabel;
    @FXML private Label artistlabel;
    @FXML private Label songdetails;
    @FXML private Button finaledit;
    @FXML private Button canceleditbutton;
    @FXML private Label editdetails;
    @FXML private Label editnamelabel;
    @FXML private Label editartistlabel;

    ObservableList<String> songList;
    int saveind;
    
    public void start(Stage mainStage) throws IOException {
		/*
		 * String re = ""; FileWriter writer = new FileWriter("songlist.csv",false); for
		 * (int i=0; i<10; i++) { writer.append(re); } writer.flush(); writer.close();
		 */
    	finaledit.setVisible(false);
    	canceleditbutton.setVisible(false);
    	editdetails.setVisible(false);
    	editnamelabel.setVisible(false);
    	editartistlabel.setVisible(false);
    	
    	songList = putSongs(songList);
    	songView.setItems(songList);
    	
    	songView.getSelectionModel().selectedItemProperty()
    		.addListener((obs, oldVal, newVal) -> {
				try {
					showDetails();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

    	songView.getSelectionModel().select(0);
    }
    
    @FXML
    private void add(ActionEvent event) throws IOException {
    	AnchorPane pane = FXMLLoader.load(getClass().getResource("add.fxml"));
    	rootPane.getChildren().setAll(pane);
    }
    
    public void added(String name, String artist, String album, String year) throws IOException {
    	songList = putSongs(songList);
    	songView.setItems(songList);
    	songView.getSelectionModel().selectedItemProperty()
			.addListener((obs, oldVal, newVal) -> {
				try {
					showDetails();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});	
    	songView.getSelectionModel().select(name+" - "+artist);
    	
    	nameField.setText(name);
    	artistField.setText(artist);
    	albumField.setText(album);
    	yearField.setText(year);
    	
    	finaledit.setVisible(false);
    	canceleditbutton.setVisible(false);
    	editdetails.setVisible(false);
    	editnamelabel.setVisible(false);
    	editartistlabel.setVisible(false);
    }
    
    public void canceled() {
    	songList = putSongs(songList);
    	songView.setItems(songList);
    	songView.getSelectionModel().selectedItemProperty()
			.addListener((obs, oldVal, newVal) -> {
				try {
					showDetails();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
	
		songView.getSelectionModel().select(0);
		
		finaledit.setVisible(false);
    	canceleditbutton.setVisible(false);
    	editdetails.setVisible(false);
    	editnamelabel.setVisible(false);
    	editartistlabel.setVisible(false);
    }

    @FXML
    private void delete(ActionEvent e) throws IOException {
    	int size = songView.getItems().size();
    	if (size==0) {
    		Alert alert2 = new Alert(AlertType.ERROR);
    		alert2.setTitle("Error!");
    		alert2.setHeaderText("No Songs to Delete");
    		alert2.showAndWait();
    		return;
    	}
    	
    	Button b = (Button)e.getSource();
		if (b == deletebutton) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Song?");
			alert.setHeaderText("Are you sure you want to delete selected song?");
				
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				int index = songView.getSelectionModel().getSelectedIndex();
				BufferedReader br = new BufferedReader(new FileReader("songlist.csv"));
				List<String> lines = new ArrayList<>();
		    	String str = "";
		    	while ((str = br.readLine()) != null) {
		    	    lines.add(str);
		    	}
		    	
		    	Collections.sort(lines, new Comparator<String>() {
					@Override
					public int compare(String s1, String s2) {
						int length1 = s1.length();  
					    int length2 = s2.length();  
					    int limit = Math.min(length1, length2);  
					    char v1[] = new char[length1];
					    char v2[] = new char[length2];
					    for (int i=0; i<length1; i++) {
					    	v1[i]=Character.toLowerCase(s1.charAt(i));
					    }
					    for (int j=0; j<length2; j++) {
					    	v2[j]=Character.toLowerCase(s2.charAt(j));
					    }
					   
					    int i = 0;  
					    while (i < limit) {  
					        char ch1 = v1[i];  
					        char ch2 = v2[i];
					        if (ch1==('|') && ch2!=('|')) {
					        	return -1;
					        } else if (ch1!=('|') && ch2==('|')) {
					        	return 1;
					        }
					        if (ch1 != ch2) { 
					            return ch1 - ch2;  
					        }  
					        i++;
					    }  
					    return length1 - length2;  
					} 
				});
		    	
		    	lines.remove(index);
		    	//System.out.println(lines);
				br.close();
				
				FileWriter writer = new FileWriter("songlist.csv",false);
				for (int i=0;i<lines.size();i++) {
					writer.append(lines.get(i));
					writer.append("\n");
				}
				writer.flush();
				writer.close();
				songList.remove(index);
		    	songView.setItems(songList);
		    	//System.out.println(index);
		    	//System.out.println(size);
		    	if (size>1) {
			    	if (index+1==size) {
			    		songView.getSelectionModel().select(index-1);
			    	} else {
			    		songView.getSelectionModel().select(index);
			    	}
		    	} else {
		    		nameField.setText("");
		    		artistField.setText("");
		    		albumField.setText("");
		    		yearField.setText("");
		    	}
		    	finaledit.setVisible(false);
		    	canceleditbutton.setVisible(false);
		    	editdetails.setVisible(false);
		    	editnamelabel.setVisible(false);
		    	editartistlabel.setVisible(false);
			}
			
		}
    }
    
    String name;
    String artist;
    String album;
    String year;
    
    @FXML
    private void edit(ActionEvent event) throws IOException {
    	int size = songView.getItems().size();
    	if (size==0) {
    		Alert alert2 = new Alert(AlertType.ERROR);
    		alert2.setTitle("Error!");
    		alert2.setHeaderText("No Songs to Edit");
    		alert2.showAndWait();
    		return;
    	}
    	
    	name = nameField.getCharacters().toString().trim();
    	artist = artistField.getCharacters().toString().trim();
    	album = albumField.getCharacters().toString().trim();
    	year = yearField.getCharacters().toString().trim();
    	
    	saveind = songView.getSelectionModel().getSelectedIndex();
    	namelabel.setVisible(false);
    	artistlabel.setVisible(false);
    	songdetails.setVisible(false);
    	finaledit.setVisible(true);
    	canceleditbutton.setVisible(true);
    	editdetails.setVisible(true);
    	editnamelabel.setVisible(true);
    	editartistlabel.setVisible(true);
    	nameField.setEditable(true);
    	artistField.setEditable(true);
    	albumField.setEditable(true);
    	yearField.setEditable(true);
    	songView.setMouseTransparent(true);
    	addbutton.setMouseTransparent(true);
    	editbutton.setMouseTransparent(true);
    	deletebutton.setMouseTransparent(true);
    }
    
    @FXML
    void canceledit(ActionEvent event) {
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setHeaderText("Are you sure you want to cancel?");
		ButtonType buttonYes = new ButtonType("Yes");
		ButtonType buttonNo = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(buttonYes,buttonNo);
		
		Optional<ButtonType> res = alert.showAndWait();
		
		if (res.get()==buttonYes) {
	    	namelabel.setVisible(true);
	    	artistlabel.setVisible(true);
	    	songdetails.setVisible(true);
	    	finaledit.setVisible(false);
	    	canceleditbutton.setVisible(false);
	    	editdetails.setVisible(false);
	    	editnamelabel.setVisible(false);
	    	editartistlabel.setVisible(false);
	    	nameField.setEditable(false);
	    	artistField.setEditable(false);
	    	albumField.setEditable(false);
	    	yearField.setEditable(false);
	    	songView.setMouseTransparent(false);
	    	addbutton.setMouseTransparent(false);
	    	editbutton.setMouseTransparent(false);
	    	deletebutton.setMouseTransparent(false);
	    	
	    	nameField.setText(name);
	    	artistField.setText(artist);
	    	albumField.setText(album);
	    	yearField.setText(year);
		}
    }

    @FXML
    void completeEdit(ActionEvent event) throws IOException {
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
    		alert.setHeaderText("Edit Song?");
    		ButtonType buttonEdit = new ButtonType("Edit");
    		ButtonType buttonCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    		alert.getButtonTypes().setAll(buttonEdit,buttonCancel);
    		
    		Optional<ButtonType> res = alert.showAndWait();
    		
    		if (res.get()==buttonEdit) {
    			
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
    			
				BufferedReader br = new BufferedReader(new FileReader("songlist.csv"));
				List<String> lines = new ArrayList<>();
		    	String str = "";
		    	while ((str = br.readLine()) != null) {
		    	    lines.add(str);
		    	}
		    	br.close();
		    	Collections.sort(lines, new Comparator<String>() {
					@Override
					public int compare(String s1, String s2) {
						int length1 = s1.length();  
					    int length2 = s2.length();  
					    int limit = Math.min(length1, length2);  
					    char v1[] = new char[length1];
					    char v2[] = new char[length2];
					    for (int i=0; i<length1; i++) {
					    	v1[i]=Character.toLowerCase(s1.charAt(i));
					    }
					    for (int j=0; j<length2; j++) {
					    	v2[j]=Character.toLowerCase(s2.charAt(j));
					    }
					   
					    int i = 0;  
					    while (i < limit) {  
					        char ch1 = v1[i];  
					        char ch2 = v2[i];
					        if (ch1==('|') && ch2!=('|')) {
					        	return -1;
					        } else if (ch1!=('|') && ch2==('|')) {
					        	return 1;
					        }
					        if (ch1 != ch2) { 
					            return ch1 - ch2;  
					        }  
					        i++;
					    }  
					    return length1 - length2;  
					} 
				});
    			
		    	for (int i=0; i<lines.size();i++) {
		    		String[] song = lines.get(i).split("\\|");
		    		if (i!=saveind && song[0].equalsIgnoreCase(name) && song[1].equalsIgnoreCase(artist)) {
		    			Alert alert1 = new Alert(AlertType.ERROR);
    	    			alert1.setTitle("Error");
    	    			alert1.setHeaderText("Song Name and Artist Already Exist");
    	    			alert1.showAndWait();
    	    			return;
		    		}
		    	}
		    	
		    	lines.remove(saveind);
		    	//System.out.println(lines);
				
				FileWriter writer = new FileWriter("songlist.csv",false);
				for (int i=0;i<lines.size();i++) {
					writer.append(lines.get(i));
					writer.append("\n");
				}
				writer.flush();
				writer.close();
    	    	
    	    	FileWriter writer1 = new FileWriter("songlist.csv",true);
        		writer1.append(name);
        		writer1.append("|");
        		writer1.append(artist);
        		writer1.append("|");
        		writer1.append(album);
        		writer1.append("|");
        		writer1.append(year);
        		writer1.append("\n");
        		
        		writer1.flush();
        		writer1.close();
        		
        		
        		namelabel.setVisible(true);
            	artistlabel.setVisible(true);
            	songdetails.setVisible(true);
            	finaledit.setVisible(false);
            	canceleditbutton.setVisible(false);
            	editdetails.setVisible(false);
            	editnamelabel.setVisible(false);
            	editartistlabel.setVisible(false);
            	nameField.setEditable(false);
            	artistField.setEditable(false);
            	albumField.setEditable(false);
            	yearField.setEditable(false);
            	songView.setMouseTransparent(false);
            	addbutton.setMouseTransparent(false);
            	editbutton.setMouseTransparent(false);
            	deletebutton.setMouseTransparent(false);
            	
            	nameField.setText(name);
            	artistField.setText(artist);
            	albumField.setText(album);
            	yearField.setText(year);
            	
            	songList = putSongs(songList);
            	songView.setItems(songList);
            	
            	songView.getSelectionModel().select(name+" - "+artist);
    		}
    	}
    }
    
    private void showDetails() throws IOException {
    	String line = "";
    	BufferedReader br = new BufferedReader(new FileReader("songlist.csv"));
    	int index = songView.getSelectionModel().getSelectedIndex();
    	
    	//add all lines into an array list
    	List<String> lines = new ArrayList<>();
    	String str = null;
    	while ((str = br.readLine()) != null) {
    	    lines.add(str);
    	}
    	br.close();
    	if (index<0) return;
    	//System.out.println(lines);
    	Collections.sort(lines, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				int length1 = s1.length();  
			    int length2 = s2.length();  
			    int limit = Math.min(length1, length2);  
			    char v1[] = new char[length1];
			    char v2[] = new char[length2];
			    for (int i=0; i<length1; i++) {
			    	v1[i]=Character.toLowerCase(s1.charAt(i));
			    }
			    for (int j=0; j<length2; j++) {
			    	v2[j]=Character.toLowerCase(s2.charAt(j));
			    }
			    int i = 0;  
			    while (i < limit) {  
			        char ch1 = v1[i];  
			        char ch2 = v2[i];
			        if (ch1==('|') && ch2!=('|')) {
			        	return -1;
			        } else if (ch1!=('|') && ch2==('|')) {
			        	return 1;
			        }
			        if (ch1 != ch2) { 
			            return ch1 - ch2;  
			        }  
			        i++;
			    }  
			    return length1 - length2;  
			} 
		});
    	//System.out.println(index);
    	//System.out.println(lines);
    	line = lines.get(index);
		String[] details = line.split("\\|");
		nameField.setText(details[0]);
		artistField.setText(details[1]);
		if (details.length==4) {
    		albumField.setText(details[2]);
    		yearField.setText(details[3]);
		} else if (details.length==3) {
			albumField.setText(details[2]);
			yearField.setText(null);
		} else {
			albumField.setText(null);
			yearField.setText(null);
		}
    }
    
    public static ObservableList<String> putSongs(ObservableList<String> songList) {
	    songList=FXCollections.observableArrayList();
		String line="";
		try {
			File songFile = new File("songlist.csv");
			if (!songFile.createNewFile()) {
				BufferedReader br = new BufferedReader(new FileReader("songlist.csv"));
		    	while ((line=br.readLine())!=null) {
		    		String[] songs = line.split("\\|");
		    		songList.add(songs[0]+" - "+songs[1]);
		    		//System.out.println(Arrays.toString(songs));
		    	}
		    	br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//alphabetical order list
		if (!songList.isEmpty()) {
			FXCollections.sort(songList, new Comparator<String>() {
	
					@Override
					public int compare(String s1, String s2) {
						return s1.compareToIgnoreCase(s2);
					} 
				});
		}
		return songList;
    }    
}
