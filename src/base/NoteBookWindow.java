package base;




import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	
	Stage stage;
	
	String currentNote = "";

	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		this.stage = stage;
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());

		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Label label = new Label("Search : ");
		TextField textField = new TextField();
		Button searchButton = new Button("Search");
		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				currentSearch = textField.getText();
				textAreaNote.setText("");
				List<Note> notes = noteBook.searchNotes(currentSearch);
				List<String> list = new ArrayList<>();
				for(Note n:notes){
					list.add(n.getTitle());
				}

				ObservableList<String> combox2 = FXCollections.observableArrayList(list);
				titleslistView.setItems(combox2);
				textAreaNote.setText("");

			}
		});
		Button clearButton = new Button("Clear Search");
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				currentSearch = "";
				textField.setText("");
				textAreaNote.setText("");
				updateListView();
			}
		});
		
		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				
				if(file != null) {
					//TODO
					loadNoteBook(file);
				}
			}
		});
		//buttonLoad.setDisable(true);
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please Choose An File Which Contains a NoteBook Object!");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("serialized Object File (*.ser)", "*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(stage);
				
				if(file != null) {
					//TODO
					noteBook.save(file.getAbsolutePath());
					
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("You file has been saved to file " + file.getName());
					alert.showAndWait().ifPresent(rs -> {
					    if (rs == ButtonType.OK) {
					        System.out.println("Pressed OK.");
					    }
					});
				}
				
			}
		});
		//buttonSave.setDisable(true);

		hbox.getChildren().addAll(buttonLoad, buttonSave, label, textField, searchButton, clearButton);

		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes

		// TODO: This line is a fake folder list. We should display the folders in noteBook variable! Replace this with your implementation
		//foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");
		for(int i=0; i<noteBook.getFolders().size(); i++)
			foldersComboBox.getItems().add(noteBook.getFolders().get(i).getName());

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				currentFolder = t1.toString();
				// this contains the name of the folder selected
				// TODO update listview
				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";

				for(Folder f: noteBook.getFolders()){
					for(Note n:f.getNotes()){
						if(n.getTitle().equals(title)){
							content = ((TextNote)n).getContent();
							currentNote = n.getTitle();
							break;
							
						}
					}
				}
				
				textAreaNote.setText(content);
				
			}
		});
		HBox hbox = new HBox();
		hbox.setSpacing(8); // Gap between nodes
		Button buttonAdd = new Button("Add a Folder");
		buttonAdd.setPrefSize(100, 20);
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
			    dialog.setTitle("Input");
			    dialog.setHeaderText("Add a new folder for your notebook:");
			    dialog.setContentText("Please enter the name you want to create:");

			    // Traditional way to get the response value.
			    Optional<String> result = dialog.showAndWait();
			    if (result.isPresent()){
			        // TODO 
			    	if(result.get() == "" ) {
			    		//empty
			    		Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setContentText("Please input an valid folder name");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
			    	}
			    	else {
			    		for(Folder f:(noteBook.getFolders())) {
			    			//System.out.println(f.getName());
			    			if(f.getName().equals(result.get())) {
			    				
			    				Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Warning");
								alert.setContentText("You already have a folder named with " + result.toString());
								alert.showAndWait().ifPresent(rs -> {
								    if (rs == ButtonType.OK) {
								        System.out.println("Pressed OK.");
								    }
								});
								return;
			    			}
			    				
			    		}
			    		//valid
			    		Folder newFolder = new Folder(result.get());
			    		noteBook.getFolders().add(newFolder);
			    		
			    		foldersComboBox.getItems().add(noteBook.getFolders().get(noteBook.getFolders().size()-1).getName());

			    		//System.out.println(noteBook.getFolders().toString());
			    	}
			    	
			    }

			}
		});
		hbox.getChildren().add(foldersComboBox);
		hbox.getChildren().add(buttonAdd);
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox);
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		Button addANote = new Button("Add a Note");
		vbox.getChildren().add(addANote);
		addANote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

			    	if(currentFolder == "-----") {
	    				Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setContentText("Please choose a folder first ");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
						
			    	}
			    	else {
						TextInputDialog dialog = new TextInputDialog("Add a Note");
					    dialog.setTitle("Input");
					    dialog.setHeaderText("Add a new note to current folder:");
					    dialog.setContentText("Please enter the name of your note:");

					    // Traditional way to get the response value.
					    Optional<String> result = dialog.showAndWait();
					    //if(result.get())       existing note name
					    if (result.isPresent()){
		    				Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successful");
							alert.setContentText("Inserted note " +result.get()+ " to folder "+currentFolder+ " successfully");
							alert.showAndWait().ifPresent(rs -> {
							    if (rs == ButtonType.OK) {
							        System.out.println("Pressed OK.");
							    }
							});
							noteBook.createTextNote(currentFolder, result.get());
							updateListView();
					    }
			    	}
			    
			}
		});
		return vbox;
	}

	private void updateListView() {
		ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		for(int i=0; i<noteBook.getFolders().size(); i++){
			if(noteBook.getFolders().get(i).getName().equals(currentFolder)){
				for(Note n:noteBook.getFolders().get(i).getNotes()){
					if(n instanceof TextNote){
						list.add(n.getTitle());
					}
				}
			}
		}

		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");

	}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		ImageView saveView = new ImageView(new Image(new File("C:\\Users\\ljimm\\Desktop\\comp3021\\save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);

		Button saveNote = new Button("Save Note");
		saveNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

			    	if(currentFolder == "-----" || currentNote == "") {
	    				Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setContentText("Please choose a folder and a note ");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
						
			    	}
			    	else {
			    		//save to note content
			    		for(Folder f:noteBook.getFolders()) {
			    			if(f.getName().equals(currentFolder)) {
			    				for(Note n:f.getNotes()) {
			    					if(n.getTitle().equals(currentNote)) {
			    						((TextNote) n).setContent(textAreaNote.getText());
			    						
			    					}
			    				}
			    					
			    			}
			    		}
			    			
			    	}
			}
		});
		Button deleteNote = new Button("Delete Note");
		deleteNote.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {

			    	if(currentFolder == "-----" || currentNote == "") {
	    				Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setContentText("Please choose a folder and a note ");
						alert.showAndWait().ifPresent(rs -> {
						    if (rs == ButtonType.OK) {
						        System.out.println("Pressed OK.");
						    }
						});
						
			    	}
			    	else {
			    		for(Folder f:noteBook.getFolders()) {
			    			if(f.getName().equals(currentFolder)) {
			    				if(f.removeNotes(currentNote)) {
			    					Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("Succeed!");
									alert.setContentText("Your note has been successfully removed ");
									alert.showAndWait().ifPresent(rs -> {
									    if (rs == ButtonType.OK) {
									        System.out.println("Pressed OK.");
									    }
									});
									updateListView();
			    				}
			    				
			    			}
			    		}
			    	}
			}
		});
		HBox hbox = new HBox();
		hbox.setSpacing(8);
		hbox.getChildren().add(saveView);
		hbox.getChildren().add(saveNote);
		hbox.getChildren().add(deleteNote);
		grid.add(hbox, 0, 0);
		grid.add(textAreaNote, 0, 1);
		
		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3021", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3021", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3021", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called ¡§the most shocking play in NFL history¡¨ and the Washington Redskins dubbed the ¡§Throwback Special¡¨: the November 1985 play in which the Redskins¡¦ Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award¡Vwinning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything¡Xuntil it wasn¡¦t. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant¡Xa part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether¡¦s Daddy Was a Number Runner and Dorothy Allison¡¦s Bastard Out of Carolina, Jacqueline Woodson¡¦s Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood¡Xthe promise and peril of growing up¡Xand exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
	private void loadNoteBook(File file) {
		   // TODO 
		NoteBook nb = new NoteBook(file.getAbsolutePath());
		noteBook = nb;
		for(int i=0; i<noteBook.getFolders().size(); i++)
			foldersComboBox.getItems().add(noteBook.getFolders().get(i).getName());
				
	}

	
	
}

