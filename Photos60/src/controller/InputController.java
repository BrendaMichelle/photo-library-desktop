package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import model.Photo;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 *	This class handles all the pop ups that 
 * ask for user input in specific situations.
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 *
 */

public class InputController extends CentralController{
	
	private static final int THUMBNAIL_HEIGHT = 125;
	
	private static final int THUMBNAIL_WIDTH = 125;
	
	/**
	 * This method retrieves a photo if it could be properly located.
	 * @return Photo
	 */
	public static Photo getPhoto() {
		
		File photoFile = CentralController.getNewPhotoFile();
		
		if(photoFile == null) {
			InputController.imageCannotLoad();
			return null;
		}
		else {
			String fileName = photoFile.getName();
			String absPath = photoFile.getAbsolutePath();
			return new Photo(fileName, null, absPath);
		}
	}
	
	/**
	 * This method attempts to create an image to be placed
	 * onto the GUI if it has the proper data necessary.
	 * @param photoImage This returns an Image object if it is properly made.
	 * @return This returns null if the Image object could not be properly 
	 * made.
	 */
	public static Image createImageFromPhoto(Photo photoImage) {
		
		Image image = null;
		
		String imagePath = photoImage.getPath();
		
		File filePath = new File(imagePath);
		
		//System.out.println("Confused: " + imagePath);
		//System.out.println("Short path: " + filePath.getPath());
		//System.out.println("Path: " + filePath.getAbsolutePath());
				
		try {
			image = new Image(filePath.getPath(), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, false, true);
		}
		catch(IllegalArgumentException i){
			try {
				image = new Image(new FileInputStream(filePath.getAbsolutePath()), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, false, true);
			} 
				catch (FileNotFoundException e) {
					//System.out.println("FILE NOT FOUND EXCEPTION IN ALBUMCONTROLLER createImageFromPhoto");
					//e.printStackTrace();
					InputController.imageCannotLoad();
					return null;
			}
		}
		
		return image;
	}
	/**
	 * This method gives a pop up when the user wishes to rename the album.  It
	 * gives the name of an album from the GUI if it can be 
	 * properly located.
	 * @return returnString This is the name of the album, null otherwise.
	 */
	public static String getAlbumName() {
		TextInputDialog inputText = new TextInputDialog();
		inputText.setTitle("New Album");
		inputText.setHeaderText("Enter name for album");
		inputText.setContentText("Album name:");
		String returnString = null;
		
		while(true) {
			Optional<String> input = inputText.showAndWait();
			if(input.isPresent()) {
				returnString = input.get();
				if(returnString.equals("")) {
					emptyFieldError();
					continue;
				}
				if(returnString.charAt(0) == ' '){
					beginsBlank();
					continue;
				}
				else {
					break;
				}
			}
			else {
				return null;
			}
		}
			return returnString;
	}
	
	/**
	 * This creates a pop up alert when the user wishes to
	 * recaption a photo in the photo stage.
	 */
	public static void photoCaptioned() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Recaption");
		alert.setHeaderText("Photo Recaptioned");
		alert.showAndWait();
	}
	/**
	 * This creates a pop up alert when the user wishes to
	 * recaption a photo in the photo stage.
	 */
	public static void recaptioned() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Recaption");
		alert.setHeaderText("Photo Recaptioned");
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user wishes to delete
	 * an album.
	 * @return true This returns true if the user is sure they
	 * want to delete an album,false otherwise
	 */
	public static boolean deleteAlbum() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.initOwner(currentStage);
		alert.setTitle("Delete Album");
		alert.setHeaderText("ARE YOU SURE YOU WANT TO DELETE THIS ALBUM?");
		String content = "Please make a decision.";
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * This method gives a pop up when they wish to delete a photo
	 * that happens to also be the last existing photo in an album.
	 * 
	 * @return true if the user wishes to delete the last photo,
	 * which also deletes the album, false otherwise.
	 */
	public static boolean deleteLastPhoto() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.initOwner(currentStage);
		alert.setTitle("Delete Photo");
		alert.setHeaderText("ARE YOU SURE YOU WANT TO DELETE THE LAST PHOTO?");
		String content = "Doing so will also delete the whole album.";
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * This method creates a pop up when the user clicks on delete photo
	 * on the album stage.  
	 * @return true if they are sure they want to delete the chosen 
	 * photo, false otherwise.
	 */
	public static boolean deletePhoto() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.initOwner(currentStage);
		alert.setTitle("Delete Photo");
		alert.setHeaderText("ARE YOU SURE YOU WANT TO DELETE THIS PHOTO?");
		String content = "Please make a decision.";
		alert.setContentText(content);
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		else {
			return false;
		}
	}
	/**
	 * This method creates a pop up that notifies the user
	 * that a tag they tried to delete does not exist in the
	 * photo's list of tags.
	 * 
	 */
	public static void tagDoesNotExists() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Tag does not exist in the photo";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up that alerts the user that 
	 * they tag they tried to add already exists in the photo's list
	 * of tags.
	 */
	public static void tagExists() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Tag already exists in the photo";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user tries to 
	 * copy a photo into an album where it already exists.
	 */
	public static void copiedPhotoExists() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Photo already exists in the album";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user tries to delete a photo
	 * that does not exist within the given album's list of photos.
	 */
	public static void photoDoesNotExist() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Photo does not exist in the list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user tries to 
	 * add a photo into an album where it already exists
	 * in the photo stage.
	 */
	public static void photoExistsPhotoStage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Photo already exists in the list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user tries to 
	 * add a photo into an album where it already exists.
	 */
	public static void photoExists() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Photo already exists in the list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user tries to open
	 * an album that is already opened.
	 */
	public static void albumOpened() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Album already opened in the list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This creates a pop up that alert the user the album they 
	 * are trying to add already exists within his/her list of
	 * albums.
	 */
	public static void albumExists() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Album already exists in the list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up to alert the admin
	 * that the user they are trying to add already exists
	 * in the program's list of users.
	 */
	public static void userExists() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "User already exists in the list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user does not
	 * select on a specific photo onto which to complete an
	 * action.  This is for the photo stage.
	 */
	public static void noPhotoSelectedPhotoStage() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "No photo selected";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up when the user does not
	 * select on a specific photo onto which to complete an
	 * action.
	 */
	public static void noPhotoSelected() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "No photo selected";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method creates a pop up to alert the user that
	 * an image could not be loaded.
	 */
	public static void imageCannotLoad() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Cannot load image, try again?";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method alerts the user of an invalid album entry
	 * and that is does not exist in the photo view.
	 */
	public static void albumMissingPhotoView() {
	
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "No album in list";
		alert.setContentText(content);
		alert.showAndWait();
		
	}
	/**
	 * This method alerts the user of an invalid album entry
	 * and that is does not exist.
	 */
	public static void albumMissing() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Album not selected or no album in list";
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	/**
	 * This method alerts the user in the photo stage that a field they are typing into
	 * cannot begin with a blank space.
	 */
	public static void beginsBlankPhotoStage() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Field cannot be begin with a blank space";
		alert.setContentText(content);
		alert.showAndWait();

	}

	/**
	 * This method alerts the user that a field they are typing into
	 * cannot begin with a blank space.
	 */
	public static void beginsBlank() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Field cannot be begin with a blank space";
		alert.setContentText(content);
		alert.showAndWait();
		
	}
	/**
	 * This method alerts the user in the photo stage when a field cannot be empty
	 * and they tried to submit it as such.
	 */
	public static void emptyFieldErrorPhotoStage() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(photoStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Field cannot be empty";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method alerts the user when a field cannot be empty
	 * and they tried to submit it as such.
	 */
	public static void emptyFieldError() {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Entry");
		String content = "Field cannot be empty";
		alert.setContentText(content);
		alert.showAndWait();
	}
	/**
	 * This method alerts the user at login when the given user name could 
	 * not be matched.
	 */
	public static void userNotFound() {
		
		// This code should probably change to check for empty field?
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(currentStage);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid Username");
		String content = "Username not found or invalid format.";
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	
}
