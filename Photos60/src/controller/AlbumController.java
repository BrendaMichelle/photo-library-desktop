package controller;

import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Nonadmin;
import model.Photo;

/**
 * This class deals with all the buttons and functions on the
 * album view stage.
 * @author Brenda Michelle
 * @author Roy Jacome
 *
 */
public class AlbumController extends CentralController implements EventHandler<MouseEvent>{
	
	@FXML Button logoutButton;
	@FXML Button createAlbumButton;
	@FXML Button deleteAlbumButton;
	@FXML Button renameAlbumButton;
	@FXML Button openAlbumButton;
	@FXML Button searchPhotosButton;
	@FXML Button addPhotoButton;
	@FXML Button viewPhotoButton;
	@FXML Button deletePhotoButton;
	
	@FXML TilePane albumTile;
	@FXML TilePane photoTile;
			
	ObservableList<BorderPane> albumObsList = FXCollections.observableArrayList();
	ObservableList<BorderPane> photoObsList = FXCollections.observableArrayList();
	
	BorderPane currentSelectedPhotoFrame;
	BorderPane currentSelectedAlbumFrame;
	BorderPane currentOpenedAlbumFrame;
	BorderPane tempAlbumFrame;
	
	
	static final String style_green = "-fx-border-color: #b8d0b1;\n" +
										"-fx-border-style: solid;\n" +
										"-fx-border-width: 5;\n";
	static final String style_blue = "-fx-border-color: blue;\n" +
										"-fx-border-style: solid;\n" +
										"-fx-border-width: 5;\n";
	static final String style_lightblue = "-fx-border-color: #42ebf4;\n" +
										"-fx-border-style: solid;\n" +
										"-fx-border-width: 5;\n";
	private static final int THUMBNAIL_WIDTH = 125;
	
	

	@FXML
	public void exitApplication(ActionEvent event) {
		//System.out.println("I run?");
	   Platform.exit();
	}
	/**
	 * Deciphers the button which is pressed by the user.
	 * @param mouseEvent the button pressed
	 */
	public void buttonPressed(ActionEvent mouseEvent) {
		Button buttonClicked = (Button) mouseEvent.getSource();
		
		if(buttonClicked == createAlbumButton) {
			createNewAlbum(); // Central Controller needed to help?
		}
		else if(buttonClicked == deleteAlbumButton) {
			deleteAlbum(); // Central Controller needed to help?
		}
		else if(buttonClicked == renameAlbumButton) {
			renameAlbum();	// Central Controller needed to help?
		}
		else if(buttonClicked == searchPhotosButton) {
			searchPhotos(); // Central Controller needed to help?
		}
		else if(buttonClicked == openAlbumButton) {
			openAlbum(); // Central Controller needed to help?
		}
		else if(buttonClicked == addPhotoButton) {
			addNewPhoto(); // Central Controller needed to help?
		}
		else if(buttonClicked == viewPhotoButton) {
			viewPhoto(); // Central Controller needed to help?
		}
		else if(buttonClicked == deletePhotoButton) {
			deletePhoto(); // Central Controller needed to help?
		}
		else if(buttonClicked == logoutButton) {
			logoutFromAlbum();
		}
	}
	
	
	
	
	@Override
	public void handle(MouseEvent event) {
		//System.out.println("Mouse Event!");
		
		BorderPane borderPaneClicked = (BorderPane)event.getSource();
		
		Object isAlbum = borderPaneClicked.getUserData();
		
		if(isAlbum instanceof Album) {						// Clicked on another album
			//System.out.println("Album Clicked");
			
			//Album albumClicked = (Album)isAlbum;
			
			//System.out.println("This album is: " + albumClicked.getAlbumName());
			
			if(currentSelectedAlbumFrame == null) {
				currentSelectedAlbumFrame = borderPaneClicked;
				currentSelectedAlbumFrame.setStyle(style_blue);
				return;
			}
			
			currentSelectedAlbumFrame.setStyle(style_green);
			borderPaneClicked.setStyle(style_blue);
			currentSelectedAlbumFrame = borderPaneClicked;
			return;
		}
		
		Object isPhoto = borderPaneClicked.getUserData();
		
		if(isPhoto instanceof Photo) {							// Clicked on another photo
			//System.out.println("Photo clicked");
			
			Photo photoClicked = (Photo)borderPaneClicked.getUserData();
			//System.out.println("This photo is: " + photoClicked.getFileName());
			
			currentSelectedPhotoFrame.setStyle(style_green);
			currentSelectedPhotoFrame = borderPaneClicked;
			currentSelectedPhotoFrame.setStyle(style_blue);
			userModel.setCurrentPhoto(photoClicked);
			return;
		}

	}
	
	public void catchEnterKey(TextField photoCaptionField) {
		photoCaptionField.setOnKeyPressed((event) -> { 
			if(event.getCode() == KeyCode.ENTER) 
			{ 
				String newPhotoCaption = photoCaptionField.getText();
				
				Photo photoToCaption = (Photo)((BorderPane)photoCaptionField.getParent()).getUserData();
				
				photoToCaption.setCaption(newPhotoCaption);
				
				//((Photo)currentSelectedPhotoFrame.getUserData()).setCaption(newPhotoCaption);
				InputController.photoCaptioned();
			} 
		});
		
	}
	
	/**
	 * When a user hits upon the "view photo" button, this method is called
	 * and takes the user to the slide show mode scene with the photo with which
	 * they clicked "view button" being the first one displayed.
	 * 
	 * 
	 * 
	 */
	
	private void viewPhoto() {
		
		if(currentSelectedPhotoFrame == null) {
			InputController.noPhotoSelected();
			return;
		}
		
		Photo selectedPhoto = (Photo)currentSelectedPhotoFrame.getUserData();
		userModel.setCurrentPhoto(selectedPhoto);
		
		CentralController.fromAlbum2PhotoView();
	}
	/**
	 * When a user hits upon the "delete photo" button, this method is called
	 * and verifies that the user wishes to continue with deletion.  If so, 
	 * it processed a delete.
	 * 
	 * 
	 * 
	 */
	private void deletePhoto() {
		
		if(currentSelectedPhotoFrame == null) {
			InputController.photoDoesNotExist();
			return;
		}
		
		Album currentOpenedAlbum = (Album)currentOpenedAlbumFrame.getUserData();
		Photo photoToBeDeleted = (Photo)currentSelectedPhotoFrame.getUserData();
		String photoFileName = photoToBeDeleted.getFileName();
		
		
		if(currentOpenedAlbum.getPhotoArrayList().size() == 1) {
			if(InputController.deleteLastPhoto()) {
				currentSelectedAlbumFrame = currentOpenedAlbumFrame;
				deleteAlbum();
				return;
			}
		}
		
		if(!InputController.deletePhoto()) {
			return;
		}
		
		if(!currentOpenedAlbum.deletePhoto(photoFileName)) {
			return;
		}
		
		if(currentOpenedAlbum.getAlbumThumbnail().equals(photoToBeDeleted)) {
			Photo newThumbnailPhoto = currentOpenedAlbum.getPhotoArrayList().get(0);
			
			Image photoImage = InputController.createImageFromPhoto(newThumbnailPhoto);
			
			ImageView newThumbnail = new ImageView(photoImage);
			
			currentOpenedAlbumFrame.setCenter(newThumbnail);
			currentOpenedAlbum.setAlbumThumbnail(newThumbnailPhoto);
		}
		
		int albumSize = currentOpenedAlbum.getPhotoArrayList().size();
		String startDate = currentOpenedAlbum.getStartDate();
		String endDate = currentOpenedAlbum.getEndDate();
		
		String textInput = "Size: " + albumSize + "\nFrom: " + startDate + "\nTo: " + endDate;
		
		
		((Text)currentOpenedAlbumFrame.getBottom()).setText(textInput);
				
		photoObsList.remove(currentSelectedPhotoFrame);
		
		photoTile.getChildren().setAll(photoObsList);
		
		currentSelectedPhotoFrame = (BorderPane) photoTile.getChildren().get(0);
		currentSelectedPhotoFrame.setStyle(style_blue);
		currentSelectedPhotoFrame.setUserData(photoTile.getChildren().get(0).getUserData());
		
		Photo newPhotoNow = (Photo)currentSelectedPhotoFrame.getUserData();
		userModel.setCurrentPhoto(newPhotoNow);
		
		photoTile.getChildren().setAll(photoObsList);
		//albumTile.getChildren().setAll(albumObsList);
		
		try {
			photoStage.setOnHidden(e -> {
				try {
					CentralController.closeFromDifferentStage();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
				}
			});
		}
		catch(NullPointerException n) {
			//System.out.println("Caught delete photo exception");
		}
	}
	/**
	 * When a user hits upon the "add photo" button, this method is called
	 * and takes the user to file chooser where they can choose a photo to 
	 * upload.  It then processes a photo add.
	 * 
	 * 
	 * 
	 */
	private ImageView addNewPhoto() {
		
		Photo newPhotoToBeAdded;
		Image photoImage;
		ImageView photoImageView;
		Album currentOpenedAlbum;
		
		if(currentSelectedAlbumFrame == null || currentOpenedAlbumFrame == null) {
			InputController.albumMissing();
			return null;
		}
		
		currentOpenedAlbum = (Album)currentOpenedAlbumFrame.getUserData();
		
		newPhotoToBeAdded = InputController.getPhoto();
		
		if(newPhotoToBeAdded == null) {
			return null;
		}
		
		photoImage = InputController.createImageFromPhoto(newPhotoToBeAdded);
		
		if(photoImage == null) {
			return null;
		}
		
		photoImageView = new ImageView(photoImage);
		
		
		String photoFileName = newPhotoToBeAdded.getFileName();
		String photoCaption = newPhotoToBeAdded.getCaption();
		String photoFilePath = newPhotoToBeAdded.getPath();
		
		if(!currentOpenedAlbum.addPhoto(photoFileName, photoCaption, photoFilePath)) {
			InputController.photoExists();
			return null;
		}
		
		TextField photoCaptionField = new TextField(photoCaption);
		photoCaptionField.setPrefWidth(THUMBNAIL_WIDTH);
		catchEnterKey(photoCaptionField);
		
		BorderPane photoFrame = new BorderPane();
		photoFrame.setCenter(photoImageView);
		photoFrame.setBottom(photoCaptionField);
		photoFrame.setUserData(newPhotoToBeAdded);
		photoFrame.setOnMouseClicked(this);
		photoFrame.setStyle(style_green);
		
		int albumSize = currentOpenedAlbum.getPhotoArrayList().size();
		String startDate = currentOpenedAlbum.getStartDate();
		String endDate = currentOpenedAlbum.getEndDate();
		
		String textInput = "Size: " + albumSize + "\nFrom: " + startDate + "\nTo: " + endDate;
		
		
		((Text)currentOpenedAlbumFrame.getBottom()).setText(textInput);
		
		photoObsList.add(photoFrame);
		photoTile.getChildren().setAll(photoObsList);
		albumTile.getChildren().setAll(albumObsList);
		
		return null;
	}
	/**
	 * When a user hits upon the "open album" button, this method is called
	 * and opens the selected album, unless the selected album is already open.
	 * 
	 * 
	 */
	private void openAlbum() {
		//System.out.println("Open selected Album");
		
		if(currentSelectedAlbumFrame == null) {
			InputController.albumMissing();
			return;
		}
		
		
		if(currentOpenedAlbumFrame == currentSelectedAlbumFrame) {
			InputController.albumOpened();
			return;
		}
		
		Album currentSelectedAlbum = (Album) currentSelectedAlbumFrame.getUserData(); 
		
		photoObsList.clear();
		
		ArrayList<Photo> photosList = currentSelectedAlbum.getPhotoArrayList();
		
		for(int photoIndex = 0 ; photoIndex < photosList.size() ; photoIndex++) {		// Iterate through first album's photos to dsiplay them
			//System.out.println("Photo index now: " + photoIndex);
			
			Photo photoToFrame = photosList.get(photoIndex);
			
			String photoCaption = photoToFrame.getCaption();
			
			Image photoImage = InputController.createImageFromPhoto(photoToFrame);
			
			ImageView imageViewPhoto = new ImageView(photoImage);
			
			TextField captionTextField = new TextField(photoCaption);
			captionTextField.setPrefWidth(THUMBNAIL_WIDTH);
			catchEnterKey(captionTextField);
			
			BorderPane photosFrame = new BorderPane();
			
			photosFrame.setCenter(imageViewPhoto);
			photosFrame.setBottom(captionTextField);
			photosFrame.setUserData(photoToFrame);
			photosFrame.setOnMouseClicked(this);
			
			photoObsList.add(photosFrame);
			
		}
		
		photoTile.getChildren().setAll(photoObsList);
		
		currentSelectedPhotoFrame = (BorderPane) photoTile.getChildren().get(0);
		currentSelectedPhotoFrame.setStyle(style_blue);
		currentOpenedAlbumFrame = currentSelectedAlbumFrame;
		
		userModel.setCurrentAlbum(currentSelectedAlbum);
		userModel.setCurrentPhoto((Photo)currentSelectedPhotoFrame.getUserData());
		
	}
	/**
	 * When a user hits upon the "search photos" button, this method is called
	 * and it prompts a pop up dialog that records information about their desired 
	 * search.  They can either search by one tag (tag type, tag value), by a pair
	 * of tags (tag type, tag value, tag type 2, tag value 2), or by a date range
	 * (a start date, an end date).
	 * 
	 */
	private void searchPhotos() {
		//System.out.println("Search photos");
		Stage tempStage = new Stage();
		VBox vb = new VBox();
		vb.setSpacing(10);
		Label label = new Label("Let's search!  You may either search for photos by one tag, by a "
	    		+ "pair of tags, or by a date range."); 
		label.setWrapText(true);
		vb.getChildren().add(label);
		
		//tag search section
		TextField tagtype1 = new TextField();
		tagtype1.setPromptText("Tag type");
		TextField tagvalue1 = new TextField();
		tagvalue1.setPromptText("Tag value");
		vb.getChildren().add(tagtype1);
		vb.getChildren().add(tagvalue1);
		TextField tagtype2 = new TextField();
		tagtype2.setPromptText("Second tag type");
		TextField tagvalue2 = new TextField();
		tagvalue2.setPromptText("Second tag value");
		vb.getChildren().add(tagtype2);
		vb.getChildren().add(tagvalue2);
		Button tagButton = new Button();
	    tagButton.setText("Search by tag");
	    vb.getChildren().add(tagButton);
	    
	  
	    
	    
	    //date range search section
	    DatePicker startDate = new DatePicker();
	    startDate.setPromptText("Range start date");
	    DatePicker endDate = new DatePicker();
	    endDate.setPromptText("Range end date");
	    startDate.setOnAction(event -> {
	     LocalDate date = startDate.getValue();
	     //System.out.println("Selected date: " + date);
	    });
	    endDate.setOnAction(event-> {
	    	LocalDate date2 = endDate.getValue();
	    	
	    });

	    vb.getChildren().add(startDate);
	  	vb.getChildren().add(endDate);
		Button dateButton = new Button();
	    dateButton.setText("Search by date range");
	    vb.getChildren().add(dateButton);
	    
	    
		Scene stageScene = new Scene(vb, 300, 500);
		tempStage.setScene(stageScene);
		tempStage.show();
		
		 tagButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	            	//System.out.println("Search by tag button clicked");
	            	
	            	ObservableList<Photo> tempAlbumList = null;// = FXCollections.observableArrayList();
	    			//CentralController.userModel.searchForUser("stock").getAlbumList().get(0).getPhotoArrayList().get(0).addTag("location", "beach");
	    			try {
						tempAlbumList = FXCollections.observableArrayList(CentralController.userModel.getCurrentUser().searchForPhotosWithTag(tagtype1.getText(), tagvalue1.getText(), tagtype2.getText(), tagvalue2.getText()));
					} catch (ClassNotFoundException | IOException e1 ) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
	    			//System.out.println("There's " + tempAlbumList.size() + " photo/s found.");
	    			//There are no photo matches
	    			if(tempAlbumList.size() == 0 || tempAlbumList == null) {
	    				Label label = new Label("There are no matches!"); 
		    			label.setWrapText(true);
		    			vb.getChildren().add(label);
	    			}
	    			//we have photo matches
	    			if(tempAlbumList.size() > 0) {
	    			Album tempAlbum = CentralController.userModel.getCurrentUser().addTempAlbumObjectFromPhotoList(tempAlbumList);
	    			displayAlbumFromSearch(tempAlbum);
	    			openAlbum();
	    			//display photos in tempStage or album stage
				//ask if they'd like to make an album out of it
	    			Label label = new Label("We found " + tempAlbumList.size() + " photo/s!"
	    					+ " Would you like to keep an album of these results?"); 
	    			label.setWrapText(true);
	    			vb.getChildren().add(label);
	    			Button yes = new Button();
	    		    yes.setText("Yes please.");
	    		    yes.setLayoutX(100);
	    		    yes.setLayoutY(100);
	    		    Button no = new Button();
	    		    no.setText("No thank you.");
	    		    no.setLayoutX(20);
	    		    no.setLayoutY(100);
	    		    vb.getChildren().add(yes);
	    		    vb.getChildren().add(no);
	    		    no.setOnAction(new EventHandler<ActionEvent>() {
	    	            @Override public void handle(ActionEvent e) {
	    	            //	System.out.println("Attempting to delete temporary album...");
	    	            //	tempAlbumList.clear();
	    	            	deleteTempAlbum();
	    	            	tempStage.close();
	    	            }
	    	            });
	    		    yes.setOnAction(new EventHandler<ActionEvent>() {
	    	            @Override public void handle(ActionEvent e) {
	    	            	tempStage.close();
	    	            }
	    	            });
	    			}
	    			else {
	    				//System.out.println("There are no matches in your search!");
	    			}

	    		
	            }
	        });
		 
		 dateButton.setOnAction(new EventHandler<ActionEvent>() {
	            @Override public void handle(ActionEvent e) {
	            //	System.out.println("Search by date range button clicked");
	            
	        		//f.format(startDate.getTime());
	            	LocalDate a = startDate.getValue();
	            	LocalDate b = endDate.getValue();
	            	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            	String start = a.format(formatter);
	            	String end = b.format(formatter);
	            	//System.out.println("Searching for photos between " + start + " and " + end);
	            	ObservableList<Photo> tempAlbumList = null;// = FXCollections.observableArrayList();
	    			tempAlbumList = FXCollections.observableArrayList(CentralController.userModel.getCurrentUser().searchPhotosWithinDateRange(start, end));
	    			if(tempAlbumList.size() > 0) {
	    				Album tempAlbum = CentralController.userModel.getCurrentUser().addTempAlbumObjectFromPhotoList(tempAlbumList);
		    			displayAlbumFromSearch(tempAlbum);
		    			openAlbum();
	    				Label label = new Label("We found " + tempAlbumList.size() + " photo/s!"
		    					+ " Would you like to keep an album of these results?"); 
		    			label.setWrapText(true);
		    			vb.getChildren().add(label);
		    			Button yes = new Button();
		    		    yes.setText("Yes please.");
		    		    yes.setLayoutX(100);
		    		    yes.setLayoutY(100);
		    		    Button no = new Button();
		    		    no.setText("No thank you.");
		    		    no.setLayoutX(20);
		    		    no.setLayoutY(100);
		    		    vb.getChildren().add(yes);
		    		    vb.getChildren().add(no);
		    		    no.setOnAction(new EventHandler<ActionEvent>() {
		    	            @Override public void handle(ActionEvent e) {
		    	            //	System.out.println("Attempting to delete temporary album...");
		    	            //	tempAlbumList.clear();
		    	            	deleteTempAlbum();
		    	            	tempStage.close();
		    	            	
		    	            }
		    	            });
		    		    yes.setOnAction(new EventHandler<ActionEvent>() {
		    	            @Override public void handle(ActionEvent e) {
		    	            	tempStage.close();
		    	            	
		    	            }
		    	            });
	    			}
	    			else {
	    				Label label = new Label("We found 0 photos from that date range!"); 
		    			label.setWrapText(true);
		    			vb.getChildren().add(label);
	    				
	    			}
	    			
	            }
	        });
	}
	/**
	 * When a user clicks upon the "rename album" button, it allows them to 
	 * proceed with the renaming process.
	 */
	private void renameAlbum() {
		
		if(currentSelectedAlbumFrame == null) {
			InputController.albumMissing();
			return;
		}
		
		String newAlbumTitle = InputController.getAlbumName();
		
		// Check if there is input
		if(newAlbumTitle == null) {
			return;
		}
		
		// Search if album name already exists
		if(userModel.getCurrentUser().searchForAlbum(newAlbumTitle) != -1){
			InputController.albumExists();
			return;
		}
		
		Album currentSelectedAlbum = (Album)currentSelectedAlbumFrame.getUserData();
		
		currentSelectedAlbum.renameAlbumTo(newAlbumTitle);
		
		((TextField)currentSelectedAlbumFrame.getTop()).setText(newAlbumTitle);
		
		return;
	}
	/**
	 * This method is used in searchPhotos if the user does not wish to 
	 * keep an album of his or her search results.
	 */
	private void deleteTempAlbum() {
		//System.out.println("Deleting temp Album...");
		
		// No albums here or non selected
		if(currentSelectedAlbumFrame == null) {
			InputController.albumMissing();
			return;
		}
		
		Album currentSelectedAlbum = (Album)currentSelectedAlbumFrame.getUserData();
		
		/*if(!InputController.deleteAlbum()) {
			return;
		}*/
		
		// Could not delete album from list
		if(!userModel.getCurrentUser().deleteAlbum(currentSelectedAlbum.getAlbumName())) {
			return;
		}

		// Only one album in album array
		if(albumTile.getChildren().size() == 1) {
			
			albumObsList.clear();
			photoObsList.clear();
			photoTile.getChildren().clear();
			albumTile.getChildren().clear();
			
			currentOpenedAlbumFrame = null;
			currentSelectedPhotoFrame = null;
			currentSelectedAlbumFrame = null;
			
			userModel.setCurrentAlbum(null);
			userModel.setCurrentPhoto(null);
			return;
		}
		
		if(currentOpenedAlbumFrame == currentSelectedAlbumFrame) {
			
			//Temp place holder for now
			
			userModel.setCurrentAlbum(null);
			userModel.setCurrentPhoto(null);
			
			albumObsList.remove(currentSelectedAlbumFrame);
			albumTile.getChildren().setAll(albumObsList);
			
			currentSelectedPhotoFrame = null;
			currentSelectedAlbumFrame = null;
			currentOpenedAlbumFrame = null;
			photoObsList.clear();
			photoTile.getChildren().clear();
			
		}
		else {
			
			albumObsList.remove(currentSelectedAlbumFrame);
			albumTile.getChildren().setAll(albumObsList);
			
			userModel.setCurrentAlbum(null);
			userModel.setCurrentPhoto(null);
			
			currentSelectedPhotoFrame = null;
			currentSelectedAlbumFrame = null;
			currentOpenedAlbumFrame = null;
			
			/*	Bottom code can be used in place of the top to just open the first album by default after a delete
			 currentSelectedAlbumFrame = (BorderPane)albumTile.getChildren().get(0);
			 openAlbum();
			 */
			
			photoTile.getChildren().clear();
		}
		
		return;
	}
	/**
	 * This method is prompted when a user hits upon the "delete album" button
	 * and it proccesses an album deletion.
	 */
	public void deleteAlbum() {
		
		// No albums here or non selected
		if(currentSelectedAlbumFrame == null) {
			InputController.albumMissing();
			return;
		}
		
		Album currentSelectedAlbum = (Album)currentSelectedAlbumFrame.getUserData();
		
		if(!InputController.deleteAlbum()) {
			return;
		}
		
		// Could not delete album from list
		if(!userModel.getCurrentUser().deleteAlbum(currentSelectedAlbum.getAlbumName())) {
			return;
		}

		// Only one album in album array
		if(albumTile.getChildren().size() == 1) {
			
			albumObsList.clear();
			photoObsList.clear();
			photoTile.getChildren().clear();
			albumTile.getChildren().clear();
			
			currentOpenedAlbumFrame = null;
			currentSelectedPhotoFrame = null;
			currentSelectedAlbumFrame = null;
			
			userModel.setCurrentAlbum(null);
			userModel.setCurrentPhoto(null);
			return;
		}
		
		if(currentOpenedAlbumFrame == currentSelectedAlbumFrame) {
			
			//Temp place holder for now
			
			userModel.setCurrentAlbum(null);
			userModel.setCurrentPhoto(null);
			
			albumObsList.remove(currentSelectedAlbumFrame);
			albumTile.getChildren().setAll(albumObsList);
			
			currentSelectedPhotoFrame = null;
			currentSelectedAlbumFrame = null;
			currentOpenedAlbumFrame = null;
			photoObsList.clear();
			photoTile.getChildren().clear();
			
		}
		else {
			
			albumObsList.remove(currentSelectedAlbumFrame);
			albumTile.getChildren().setAll(albumObsList);
			
			userModel.setCurrentAlbum(null);
			userModel.setCurrentPhoto(null);
			
			currentSelectedPhotoFrame = null;
			currentSelectedAlbumFrame = null;
			currentOpenedAlbumFrame = null;
			
			/*	Bottom code can be used in place of the top to just open the first album by default after a delete
			 currentSelectedAlbumFrame = (BorderPane)albumTile.getChildren().get(0);
			 openAlbum();
			 */
			
			photoTile.getChildren().clear();
		}
		
		return;
	}
	/**
	 * This method is prompted when a user clicks upon the "create album"
	 * button.  It prompts a file chooser in which they must start off with
	 * one photo in order to complete the create an album process.
	 */
	private void createNewAlbum() {
		
		Nonadmin currentUser = userModel.getCurrentUser();
		Photo newPhotoToBeAdded;
		Image photoImage;
		ImageView photoImageView;
		ImageView albumThumbnail;
		Album newAlbumToFrame;
		
		String newAlbumName = InputController.getAlbumName();
		
		// check if a name was not given
		if(newAlbumName == null) {
			//System.out.println("No name given");
			return;
		}
		
		// call method that asks for user photo file and makes a photo from it
		newPhotoToBeAdded = InputController.getPhoto();
		
		// checks if photo could be made from user's given photo file
		if(newPhotoToBeAdded == null) {
			//System.out.println("Shouldnt be your fault for crash...");
			return;
		}
		
		// Now we have both a photo and album name
		
		// make an image from the photo for album's thumbnail
		photoImage = InputController.createImageFromPhoto(newPhotoToBeAdded);
		
		// checks if an image was able to be made
		if(photoImage == null) {
			//System.out.println("Also shouldnt be your fault to crash");
			return;
		}
		
		// make a new album and add it to users list
		//System.out.println("Current user " + currentUser.getUsername());
		newAlbumToFrame = currentUser.createAlbum(newAlbumName);
		photoImageView = new ImageView(photoImage);
		albumThumbnail = new ImageView(photoImage);
		
		// tried adding an already existing album
		if(newAlbumToFrame == null ) {
			return;
		}
		
		
		
		String photoFileName = newPhotoToBeAdded.getFileName();
		String photoCaption = newPhotoToBeAdded.getCaption();
		String photoFilePath = newPhotoToBeAdded.getPath();
		
		// Attempts to add the new photo to the album
		if(!newAlbumToFrame.addPhoto(photoFileName, photoCaption, photoFilePath)) {
			//System.out.println("Was not able to add photo to album");
			InputController.photoExists();
			return;
		}
		
		TextField albumsTitleField = new TextField(newAlbumName);
		albumsTitleField.setPrefWidth(THUMBNAIL_WIDTH);
		
		String albumSize = "Size: 1";
		//String dateRange = "From: " + albumToFrame.getStartDate() + "\nTo: " + albumToFrame.getEndDate();
		String dateRange = "From: ".concat(newAlbumToFrame.getStartDate()).concat( "\nTo: ".concat(newAlbumToFrame.getEndDate())); // Place holder for now
		
		Text albumInfoText = new Text(albumSize + "\n" + dateRange);
		albumInfoText.setWrappingWidth(THUMBNAIL_WIDTH);
		
		BorderPane albumsFrame = new BorderPane();
		albumsFrame.setCenter(albumThumbnail);
		albumsFrame.setTop(albumsTitleField);
		albumsFrame.setBottom(albumInfoText);
		albumsFrame.setUserData(newAlbumToFrame);
		albumsFrame.setOnMouseClicked(this);
		
		
		
		// first album to enter (So also first photo)
		if(currentSelectedAlbumFrame == null) {
			currentSelectedAlbumFrame = albumsFrame;
			currentOpenedAlbumFrame = albumsFrame;
			currentSelectedAlbumFrame.setStyle(style_blue);
			userModel.setCurrentAlbum(newAlbumToFrame);
			
			TextField photosTextField = new TextField(newPhotoToBeAdded.getCaption());
			photosTextField.setPrefWidth(THUMBNAIL_WIDTH);
			catchEnterKey(photosTextField);
			
			
			BorderPane photosFrame = new BorderPane();
			
			currentSelectedPhotoFrame = photosFrame;
			currentSelectedPhotoFrame.setCenter(photoImageView);
			currentSelectedPhotoFrame.setBottom(photosTextField);
			currentSelectedPhotoFrame.setOnMouseClicked(this);
			currentSelectedPhotoFrame.setUserData(newPhotoToBeAdded);
			userModel.setCurrentPhoto(newPhotoToBeAdded);
			
			photoObsList.add(photosFrame);
		}
		else {
			//System.out.println("I should run second time..");
			albumsFrame.setStyle(style_green);
		}
		
		newAlbumToFrame.setAlbumThumbnail(newPhotoToBeAdded);
		albumObsList.add(albumsFrame);
		
		photoTile.getChildren().setAll(photoObsList);
		albumTile.getChildren().setAll(albumObsList);
		
		//System.out.println("Please lord");
		
		return;
	}
	
	/**
	 * This method is used when the user wishes to view 
	 * the albums he or she owns upon opening the album view
	 * stage.
	 */
	public void setUpAlbum() {

		Nonadmin currentUser = userModel.getCurrentUser();
		ArrayList<Album> albumsList = currentUser.getAlbumList();
		
		if(albumsList.size() == 0) {	// User has no albums
			return;
		}
		
		if(currentSelectedAlbumFrame != null) {
			return;
		}
		
		
		for(int albumIndex = 0 ; albumIndex < albumsList.size() ; albumIndex++) {	// Iterate through user's albums to display them

			Album albumToFrame = albumsList.get(albumIndex);
			
			Photo albumPhotoThumbnail = albumToFrame.getAlbumThumbnail();
			
			Image imagePhoto = InputController.createImageFromPhoto(albumPhotoThumbnail);
			
			ImageView albumThumbnail = new ImageView(imagePhoto);
			
			String albumSize = "Size: " + albumToFrame.getPhotoArrayList().size();
			//String dateRange = "From: " + albumToFrame.getStartDate() + "\nTo: " + albumToFrame.getEndDate();
			String dateRange = "From: ".concat(albumToFrame.getStartDate()).concat("\nTo: ") .concat(albumToFrame.getEndDate());
			
			TextField albumTitleField = new TextField(albumToFrame.getAlbumName());
			albumTitleField.setPrefWidth(THUMBNAIL_WIDTH);
			
			Text albumInfoText = new Text(albumSize + "\n" + dateRange);
			albumInfoText.setWrappingWidth(THUMBNAIL_WIDTH);
			
			BorderPane albumsFrame = new BorderPane();
			
			albumsFrame.setCenter(albumThumbnail);
			albumsFrame.setTop(albumTitleField);
			albumsFrame.setBottom(albumInfoText);
			albumsFrame.setUserData(albumToFrame);	// Give albumFrame the info of album
			albumsFrame.setOnMouseClicked(this);
			
			if(currentSelectedAlbumFrame == null) {		// First album, select it
				currentSelectedAlbumFrame = albumsFrame;
				currentOpenedAlbumFrame = albumsFrame;
				userModel.setCurrentAlbum(albumToFrame);
				albumsFrame.setStyle(style_blue);
			}
			else {										// Not first album, Just add it to list
				albumsFrame.setStyle(style_green);
			}
			
			albumToFrame.setAlbumThumbnail(albumPhotoThumbnail);
			albumObsList.add(albumsFrame);
			
		}
		
		ArrayList<Photo> photosList = albumsList.get(0).getPhotoArrayList();
		
		for(int photoIndex = 0 ; photoIndex < photosList.size() ; photoIndex++) {		// Iterate through first album's photos to dsiplay them
			
			Photo photoToFrame = photosList.get(photoIndex);
			
			String photoCaption = photoToFrame.getCaption();
			
			Image photoImage = InputController.createImageFromPhoto(photoToFrame);
			
			ImageView imageViewPhoto = new ImageView(photoImage);
			
			TextField captionTextField = new TextField(photoCaption);
			captionTextField.setPrefWidth(THUMBNAIL_WIDTH);
			catchEnterKey(captionTextField);
			
			BorderPane photosFrame = new BorderPane();
			
			photosFrame.setCenter(imageViewPhoto);
			photosFrame.setBottom(captionTextField);
			photosFrame.setUserData(photoToFrame);
			photosFrame.setOnMouseClicked(this);
			
			if(currentSelectedPhotoFrame == null) {
				currentSelectedPhotoFrame = photosFrame;
				currentSelectedPhotoFrame.setStyle(style_blue);
				userModel.setCurrentPhoto(photoToFrame);
			}
			else {
				photosFrame.setStyle(style_green);
			}
			
			photoObsList.add(photosFrame);
			
		}
		
		photoTile.getChildren().setAll(photoObsList);
		albumTile.getChildren().setAll(albumObsList);
		return;
	}
	/**
	 * This method is called to update the GUI open stage switches.
	 */
	public void updateGUI() {
		
		Nonadmin currentUser = userModel.getCurrentUser();
		ArrayList<Album> albumsList = currentUser.getAlbumList();
		
		photoObsList.clear();
		albumObsList.clear();
		
		currentSelectedAlbumFrame = null;
		currentOpenedAlbumFrame = null;
		currentSelectedPhotoFrame = null;
		
		if(albumsList.size() == 0) {	// User has no albums
			return;
		}
		
		for(int albumIndex = 0 ; albumIndex < albumsList.size() ; albumIndex++) {	// Iterate through user's albums to display them

			Album albumToFrame = albumsList.get(albumIndex);
			
			Photo albumPhotoThumbnail = albumToFrame.getAlbumThumbnail();
			
			Image imagePhoto = InputController.createImageFromPhoto(albumPhotoThumbnail);
			
			ImageView albumThumbnail = new ImageView(imagePhoto);
			
			String albumSize = "Size: " + albumToFrame.getPhotoArrayList().size();
			//String dateRange = "From: " + albumToFrame.getStartDate() + "\nTo: " + albumToFrame.getEndDate();
			String dateRange = "From: ".concat(albumToFrame.getStartDate()).concat("\nTo: ") .concat(albumToFrame.getEndDate());
			
			TextField albumTitleField = new TextField(albumToFrame.getAlbumName());
			albumTitleField.setPrefWidth(THUMBNAIL_WIDTH);
			
			Text albumInfoText = new Text(albumSize + "\n" + dateRange);
			albumInfoText.setWrappingWidth(THUMBNAIL_WIDTH);
			
			BorderPane albumsFrame = new BorderPane();
			
			albumsFrame.setCenter(albumThumbnail);
			albumsFrame.setTop(albumTitleField);
			albumsFrame.setBottom(albumInfoText);
			albumsFrame.setUserData(albumToFrame);	// Give albumFrame the info of album
			albumsFrame.setOnMouseClicked(this);
			
			if(currentSelectedAlbumFrame == null) {		// First album, select it
				currentSelectedAlbumFrame = albumsFrame;
				currentOpenedAlbumFrame = albumsFrame;
				userModel.setCurrentAlbum(albumToFrame);
				albumsFrame.setStyle(style_blue);
			}
			else {										// Not first album, Just add it to list
				albumsFrame.setStyle(style_green);
			}
			
			albumToFrame.setAlbumThumbnail(albumPhotoThumbnail);
			albumObsList.add(albumsFrame);
			
		}
		
		ArrayList<Photo> photosList = albumsList.get(0).getPhotoArrayList();
		
		for(int photoIndex = 0 ; photoIndex < photosList.size() ; photoIndex++) {		// Iterate through first album's photos to dsiplay them
			
			Photo photoToFrame = photosList.get(photoIndex);
			
			String photoCaption = photoToFrame.getCaption();
			
			Image photoImage = InputController.createImageFromPhoto(photoToFrame);
			
			ImageView imageViewPhoto = new ImageView(photoImage);
			
			TextField captionTextField = new TextField(photoCaption);
			captionTextField.setPrefWidth(THUMBNAIL_WIDTH);
			catchEnterKey(captionTextField);
			
			BorderPane photosFrame = new BorderPane();
			
			photosFrame.setCenter(imageViewPhoto);
			photosFrame.setBottom(captionTextField);
			photosFrame.setUserData(photoToFrame);
			photosFrame.setOnMouseClicked(this);
			
			if(currentSelectedPhotoFrame == null) {
				currentSelectedPhotoFrame = photosFrame;
				currentSelectedPhotoFrame.setStyle(style_blue);
				userModel.setCurrentPhoto(photoToFrame);
			}
			else {
				photosFrame.setStyle(style_green);
			}
			
			photoObsList.add(photosFrame);
			
		}
		
		photoTile.getChildren().setAll(photoObsList);
		albumTile.getChildren().setAll(albumObsList);

		return;
	}
	
	
	/**
	 * This method is used in searchPhotos() and it outputs the temp album onto
	 * the album view stage.
	 * @param album the album we wish to display onto the GUI
	 */
	public void displayAlbumFromSearch(Album album) {
		Nonadmin currentUser = userModel.getCurrentUser();

		ArrayList<Album> albumsList = currentUser.getAlbumList();
		
		
		
		if(album.getPhotoArrayList().size() == 0) {	// User's new album has no photos

			return;
		}
		/*if(currentSelectedAlbumFrame != null) {
			return;
		}*/
		album.setAlbumThumbnail(album.getPhotoArrayList().get(0));
		int albumIndex = currentUser.searchForAlbum(album.getAlbumName());
		//System.out.println(album.getAlbumName());
		//System.out.println("Album index now: " + albumIndex);
		Album albumToFrame = albumsList.get(albumIndex);	
		Photo albumPhotoThumbnail = albumToFrame.getAlbumThumbnail();
		Image imagePhoto = InputController.createImageFromPhoto(albumPhotoThumbnail);	
		ImageView albumThumbnail = new ImageView(imagePhoto);	
		String albumSize = "Size: " + albumToFrame.getPhotoArrayList().size();
		//String dateRange = "From: " + albumToFrame.getStartDate() + "\nTo: " + albumToFrame.getEndDate();
		String dateRange = "From: ".concat(album.getStartDate()).concat("\nTo: ").concat(album.getEndDate());
		TextField albumTitleField = new TextField(albumToFrame.getAlbumName());
		albumTitleField.setPrefWidth(THUMBNAIL_WIDTH);	
		Text albumInfoText = new Text(albumSize + "\n" + dateRange);
		albumInfoText.setWrappingWidth(THUMBNAIL_WIDTH);	
		BorderPane albumsFrame = new BorderPane();	
			albumsFrame.setCenter(albumThumbnail);
			albumsFrame.setTop(albumTitleField);
			albumsFrame.setBottom(albumInfoText);
			albumsFrame.setUserData(albumToFrame);	// Give albumFrame the info of album
			albumsFrame.setOnMouseClicked(this);
			//albumObsList.remove(albumsFrame);
			currentSelectedAlbumFrame = albumsFrame;
			userModel.setCurrentAlbum(albumToFrame);
			albumsFrame.setStyle(style_lightblue);
			albumToFrame.setAlbumThumbnail(albumPhotoThumbnail);
			albumObsList.add(albumsFrame);
			
		
		ArrayList<Photo> photosList = album.getPhotoArrayList();
		
		for(int photoIndex = 0 ; photoIndex < photosList.size() ; photoIndex++) {		// Iterate through first album's photos to dsiplay them
			
			Photo photoToFrame = photosList.get(photoIndex);
			
			String photoCaption = photoToFrame.getCaption();
			
			Image photoImage = InputController.createImageFromPhoto(photoToFrame);
			
			ImageView imageViewPhoto = new ImageView(photoImage);
			
			TextField captionTextField = new TextField(photoCaption);
			captionTextField.setPrefWidth(THUMBNAIL_WIDTH);
			catchEnterKey(captionTextField);
			
			BorderPane photosFrame = new BorderPane();
			
			photosFrame.setCenter(imageViewPhoto);
			photosFrame.setBottom(captionTextField);
			photosFrame.setUserData(photoToFrame);
			photosFrame.setOnMouseClicked(this);
			
			if(currentSelectedPhotoFrame == null) {
				currentSelectedPhotoFrame = photosFrame;
				currentSelectedPhotoFrame.setStyle(style_blue);
				userModel.setCurrentPhoto(photoToFrame);
			}
			else {
				photosFrame.setStyle(style_green);
			}
			
			photoObsList.add(photosFrame);
			
		}
		
		photoTile.getChildren().setAll(photoObsList);
		albumTile.getChildren().setAll(albumObsList);
	}
	
	/**
	 * This method is called when the user clicks on "log out"
	 * and it makes sure the data is safely saved.
	 */
	public void logoutFromAlbum() {
		
		currentSelectedPhotoFrame = null;
		currentSelectedAlbumFrame = null;
		currentOpenedAlbumFrame = null;
		
		photoTile.getChildren().clear();
		albumTile.getChildren().clear();
		albumObsList.clear();
		photoObsList.clear();
		
		CentralController.fromAlbum2Login();
	}
}
