package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Album;
import model.Photo;
import model.Tag;

public class PhotoController extends CentralController{
	
	@FXML 
	Button movePhotoButton;
	@FXML 
	Button copyPhotoButton;
	@FXML 
	TextField movePhotoField;
	@FXML 
	TextField copyPhotoField;
	
	@FXML 
	Button addTagsButton;
	@FXML 
	Button deleteTagsButton;
	@FXML 
	TextField tagTypeField;
	@FXML 
	TextField tagValueField;
	
	@FXML
	TextField captionTextField;
	@FXML 
	TextField dateTextField;
	
	@FXML 
	Button previousPhotoButton;
	@FXML 
	Button nextPhotoButton;
	
	@FXML
	ImageView 
	currentPhotoInView;
	
	@FXML 
	ListView<Tag> tagsList;
	/**
	 * Deciphers the button pressed.
	 * @param event the event from the pressed button
	 */
	public void buttonPressed(ActionEvent event) {
		Button buttonClicked = (Button)event.getSource();
		
		if(buttonClicked == movePhotoButton) {
			movePhoto();
		}
		else if(buttonClicked == copyPhotoButton) {
			copyPhoto();
		}
		else if(buttonClicked == addTagsButton) {
			addTags();
		}
		else if(buttonClicked == deleteTagsButton) {
			deleteTags();
		}
		else if (buttonClicked == previousPhotoButton) {
			showPreviousphoto();
		}
		else if(buttonClicked == nextPhotoButton) {
			showNextPhoto();
		}
	}

	public void handleMouseClick(MouseEvent e) {
		// Populates the tag ListView
		
		if(userModel.getCurrentPhoto().getTagArrayList().size() > 0) {			
			tagTypeField.setText(tagsList.getSelectionModel().getSelectedItem().getType());
			tagValueField.setText(tagsList.getSelectionModel().getSelectedItem().getValue());
		}
	}
	/**
	 * This method is prompted when the user clicks on the next arrow and 
	 * it loads up the next photo in the album.
	 */
	private void showNextPhoto() {
		
		if(currentPhotoInView.getUserData() == null) {
			InputController.noPhotoSelectedPhotoStage();
			return;
		}
		
		String currentPhotoName = ((Photo)currentPhotoInView.getUserData()).getFileName();
		
		int photoIndex = userModel.getCurrentAlbum().getPhotoLocation(currentPhotoName);
		
		if(photoIndex == userModel.getCurrentAlbum().getPhotoArrayList().size() - 2) {
			nextPhotoButton.setDisable(true);
		}
		
		Photo nextPhoto = userModel.getCurrentAlbum().getPhotoArrayList().get(photoIndex + 1);
		
		Image photoImage = InputController.createImageFromPhoto(nextPhoto);
		
		if(photoImage == null) {
			if(nextPhotoButton.isDisabled()) {
				nextPhotoButton.setDisable(false);
			}
			InputController.imageCannotLoad();
			return;
		}
		
		tagsList.setItems(nextPhoto.getFXTagArrayList());
		
		tagsList.setItems(nextPhoto.getFXTagArrayList());
		if(nextPhoto.getFXTagArrayList().size() == 0) {
			tagTypeField.setText(null);
			tagValueField.setText(null);
		}
		else {
			tagsList.getSelectionModel().select(0);
			tagTypeField.setText(tagsList.getSelectionModel().getSelectedItem().getType());
			tagValueField.setText(tagsList.getSelectionModel().getSelectedItem().getValue());
		}
		
		previousPhotoButton.setDisable(false);
		currentPhotoInView.setImage(photoImage);
		currentPhotoInView.setUserData(nextPhoto);
		captionTextField.setText(nextPhoto.getCaption());
		dateTextField.setText(nextPhoto.getDate());
		
	}
	/**
	 * This method is prompted when the user clicks on the previous arrow and 
	 * it loads up the previous photo in the album.
	 */
	private void showPreviousphoto() {
		
		if(currentPhotoInView.getUserData() == null) {
			InputController.noPhotoSelectedPhotoStage();
			return;
		}
		
		String currentPhotoName = ((Photo)currentPhotoInView.getUserData()).getFileName();
		
		int photoIndex = userModel.getCurrentAlbum().getPhotoLocation(currentPhotoName);
		
		if(photoIndex == 1) {
			previousPhotoButton.setDisable(true);
		}
		
		Photo previousPhoto = userModel.getCurrentAlbum().getPhotoArrayList().get(photoIndex - 1);
		
		Image photoImage = InputController.createImageFromPhoto(previousPhoto);
		
		if(photoImage == null) {
			if(previousPhotoButton.isDisabled()) {
				previousPhotoButton.setDisable(false);
			}
			InputController.imageCannotLoad();
			return;
		}
		
		tagsList.setItems(previousPhoto.getFXTagArrayList());
		if(previousPhoto.getFXTagArrayList().size() == 0) {
			tagTypeField.setText(null);
			tagValueField.setText(null);
		}
		else {
			tagsList.getSelectionModel().select(0);
			tagTypeField.setText(tagsList.getSelectionModel().getSelectedItem().getType());
			tagValueField.setText(tagsList.getSelectionModel().getSelectedItem().getValue());
		}
		
		nextPhotoButton.setDisable(false);
		currentPhotoInView.setImage(photoImage);
		currentPhotoInView.setUserData(previousPhoto);
		currentPhotoInView.setUserData(previousPhoto);
		captionTextField.setText(previousPhoto.getCaption());
		dateTextField.setText(previousPhoto.getDate());
	}
	/**
	 * This method is prompted when the user clicks on the move photo button and 
	 * it completes the move if the text fields are properly filled in and if the 
	 * appropriate albums exist.
	 */
	private void movePhoto() {
		
		String albumName = movePhotoField.getText();
		
		//System.out.println("Move photo to album");
		
		if(currentPhotoInView.getUserData() == null) {
			movePhotoField.setText(null);
			InputController.noPhotoSelectedPhotoStage();
			return;
		}
		
		if(albumName == null || albumName.equals("")) {
			movePhotoField.setText(null);
			InputController.emptyFieldErrorPhotoStage();
			return;
		}
		else if(albumName.charAt(0) == ' ') {
			movePhotoField.setText(null);
			InputController.beginsBlankPhotoStage();
			return;
		}
		
		albumName.trim();
		
		if(userModel.getCurrentUser().searchForAlbum(albumName) == -1) {
			InputController.albumMissingPhotoView();
			return;
		}
		
		Album albumToMoveTo = userModel.getCurrentUser().getAlbumObject(albumName);
		
		Album currentAlbumToDeleteFrom = userModel.getCurrentAlbum();
		
		Photo photoToMove = (Photo) currentPhotoInView.getUserData();
		
		if(!albumToMoveTo.addPhoto(photoToMove)) {
			movePhotoField.setText(null);
			InputController.photoExistsPhotoStage();
			return;
		}
		
		currentAlbumToDeleteFrom.deletePhoto(photoToMove.getFileName());
		
		if(currentAlbumToDeleteFrom.getPhotoArrayList().size() == 0) {
			userModel.getCurrentUser().getAlbumList().remove(currentAlbumToDeleteFrom);
			currentPhotoInView.setUserData(null);
			currentPhotoInView.setImage(null);
			CentralController.lastPhotoCopied();
		}
		
		Photo displayAnotherImage = currentAlbumToDeleteFrom.getPhotoArrayList().get(0);
		
		Image photoImage = InputController.createImageFromPhoto(displayAnotherImage);
		
		tagsList.setItems(displayAnotherImage.getFXTagArrayList());
		if(displayAnotherImage.getFXTagArrayList().size() == 0) {
			tagTypeField.setText(null);
			tagValueField.setText(null);
		}
		else {
			tagsList.getSelectionModel().select(0);
			tagTypeField.setText(tagsList.getSelectionModel().getSelectedItem().getType());
			tagValueField.setText(tagsList.getSelectionModel().getSelectedItem().getValue());
		}
		
		currentPhotoInView.setImage(photoImage);
		currentPhotoInView.setUserData(displayAnotherImage);
		captionTextField.setText(displayAnotherImage.getCaption());
		dateTextField.setText(displayAnotherImage.getDate());
		movePhotoField.setText(null);
		albumController.updateGUI();
	}

	private void copyPhoto() {
		
		String albumName = copyPhotoField.getText();
		
		//System.out.println("Copy photo to album");
		
		if(currentPhotoInView.getUserData() == null) {
			copyPhotoField.setText(null);
			InputController.noPhotoSelectedPhotoStage();
			return;
		}
		
		if(albumName == null || albumName.equals("")) {
			copyPhotoField.setText(null);
			InputController.emptyFieldErrorPhotoStage();
			return;
		}
		else if(albumName.charAt(0) == ' ') {
			copyPhotoField.setText(null);
			InputController.beginsBlankPhotoStage();
			return;
		}
		
		albumName.trim();
		
		if(userModel.getCurrentUser().searchForAlbum(albumName) == -1) {
			copyPhotoField.setText(null);
			InputController.albumMissingPhotoView();
			return;
		}
		
		Album albumToCopyTo = userModel.getCurrentUser().getAlbumObject(albumName);
		
		Photo photoToCopy = (Photo) currentPhotoInView.getUserData();

		if(!albumToCopyTo.addPhoto(photoToCopy)) {
			InputController.photoExistsPhotoStage();
			return;
		}
		
		copyPhotoField.setText(null);
		albumController.updateGUI();

	}
	/**
	 * This method is prompted when the user clicks on the "add tags" button.
	 * It adds the tags to the current photo if the tag is original.
	 */
	private void addTags() {
		
		String inputTagValue = tagValueField.getText();
		String inputTagType = tagTypeField.getText();
		
		System.out.println("Add tags");
		
		if(inputTagValue == null || inputTagType == null || inputTagValue.equals("") || inputTagType.equals("")) {
			tagValueField.setText(null);
			tagTypeField.setText(null);
			InputController.emptyFieldErrorPhotoStage();
		}
		else if(inputTagValue.charAt(0) == ' ' || inputTagType.charAt(0) == ' ') {
			tagValueField.setText(null);
			tagTypeField.setText(null);
			InputController.beginsBlankPhotoStage();
		}
		inputTagValue.trim();
		inputTagType.trim();
		
		Photo currentPhoto = (Photo)currentPhotoInView.getUserData();
		
		if(currentPhoto.getTagLocation(inputTagType, inputTagValue) != -1) {
			tagValueField.setText(null);
			tagTypeField.setText(null);
			InputController.tagExists();
			return;
		}
		
		currentPhoto.addTag(inputTagType, inputTagValue);
		
		tagsList.setItems(currentPhoto.getFXTagArrayList());
	}
	/**
	 * This method is prompted when the user clicks on the "delete tag" button.  
	 * If the tag exists within the photo's tag list, then it is deleted.
	 */
	private void deleteTags() {
		
		String inputTagValue = tagValueField.getText();
		String inputTagType = tagTypeField.getText();
		
		System.out.println("Delete tags");
		
		if(inputTagValue == null || inputTagType == null || inputTagValue.equals("") || inputTagType.equals("")) {
			tagValueField.setText(null);
			tagTypeField.setText(null);
			InputController.emptyFieldErrorPhotoStage();
		}
		else if(inputTagValue.charAt(0) == ' ' || inputTagType.charAt(0) == ' ') {
			tagValueField.setText(null);
			tagTypeField.setText(null);
			InputController.beginsBlankPhotoStage();
		}
		inputTagValue.trim();
		inputTagType.trim();
		
		Photo currentPhoto = (Photo)currentPhotoInView.getUserData();
		
		if(!currentPhoto.deleteTag(inputTagType, inputTagValue)){
			tagValueField.setText(null);
			tagTypeField.setText(null);
			InputController.tagDoesNotExists();
			return;
		}
		
		tagsList.setItems(currentPhoto.getFXTagArrayList());
	}
	/**
	 * This method captures when a user hits enter after the
	 * caption field changes.
	 * @param key the key pressed
	 */
	public void enterPressed(KeyEvent key) {
		if(key.getCode() == KeyCode.ENTER) {
			TextField fieldEntered = (TextField)key.getSource();
			
			if(fieldEntered == captionTextField) {
				updateCaptionField();
			}
		}
	}
	/**
	 * This method is prompted when the user wishes to
	 * caption or re-caption the current photo.
	 */
	private void updateCaptionField() {
		String caption = captionTextField.getText();
		
		if(caption == null) {
			InputController.emptyFieldErrorPhotoStage();
		}
		
		caption.trim();
		
		((Photo)currentPhotoInView.getUserData()).setCaption(caption);
		InputController.recaptioned();
	}

	/**
	 * This method is prompted upon opening of the stage to properly
	 * prepare the stage to display photos.
	 */
	public void setUpWindowToViewPhoto() {
		
		//System.out.println("Setting up photo view");
		
		Photo currentPhoto = userModel.getCurrentPhoto();
		
		Image photoImage = InputController.createImageFromPhoto(currentPhoto);
		
		if(photoImage == null) {
			InputController.imageCannotLoad();
			return;
		}
		
		previousPhotoButton.setDisable(false);
		nextPhotoButton.setDisable(false);
		
		if(userModel.getCurrentAlbum().getPhotoArrayList().size() == 1) {
			System.out.println("Huh?");
			previousPhotoButton.setDisable(true);
			nextPhotoButton.setDisable(true);
		}
		
		String photoName = currentPhoto.getFileName();
		
		int currentPhotoIndex = userModel.getCurrentAlbum().getPhotoLocation(photoName);
		
		if(currentPhotoIndex == 0) {
			//System.out.println("Photo is the first index");
			previousPhotoButton.setDisable(true);
		}
		else if(currentPhotoIndex == userModel.getCurrentAlbum().getPhotoArrayList().size() - 1) {
			//System.out.println("Photo is the last index");
			nextPhotoButton.setDisable(true);
		}
		
		//System.out.println("Current album is: " + userModel.getCurrentAlbum().getAlbumName());
		//System.out.println("Photo trying to view is: " + currentPhoto.getFileName());

		tagsList.setItems(currentPhoto.getFXTagArrayList());
		if(currentPhoto.getFXTagArrayList().size() == 0) {
			tagTypeField.setText(null);
			tagValueField.setText(null);
		}
		else {
			tagsList.getSelectionModel().select(0);
			tagTypeField.setText(tagsList.getSelectionModel().getSelectedItem().getType());
			tagValueField.setText(tagsList.getSelectionModel().getSelectedItem().getValue());
		}
		currentPhotoInView.setImage(photoImage);
		currentPhotoInView.setUserData(currentPhoto);
		captionTextField.setText(currentPhoto.getCaption());
		dateTextField.setText(currentPhoto.getDate());		
	}
}
