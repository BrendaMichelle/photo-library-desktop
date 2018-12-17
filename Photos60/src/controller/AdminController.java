package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * This class is the controller for the admin features on the admin GUI page.
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 * 
 * 
 * 
 *
 */
public class AdminController extends CentralController {
	
	@FXML Button addUserButton;
	@FXML Button deleteUserButton;
	@FXML Button logoutButton;
	@FXML TextField userNameField;
	@FXML ListView<String> listOfUsers;
	
	
	public void keyPressed(KeyEvent e) {
		if(e.getCode() == KeyCode.ENTER) {
			addUser();
		}
	}
	/**
	 * This method calls the appropriate method according to whatever button was pushed.
	 * @param e This is an ActionEvent of the button that was pushed.
	 * 
	 * 
	 * 
	 * 
	 */
	public void buttonPressed(ActionEvent e) {		// Determine what button was pushed
		
		Button buttonClicked = (Button)e.getSource();
		
		if(buttonClicked == addUserButton) {
			addUser();
			return;
		}
		if(buttonClicked == deleteUserButton) {
			deleteUser();
			return;
		}
		if(buttonClicked == logoutButton) {
			userNameField.setText(null);
			CentralController.logoutFromAdmin();
		}
	}
	public void initBeforeShow() {
		if(userModel.getNonadminList().size() > 0 ) {
			listOfUsers.setItems(userModel.getNonadminStringFXList());
		}
	}

	/**
	 * This method handles when an admin wishes to add a new user.
	 * 
	 * 
	 * 
	 * 
	 */
	public void addUser() {											// Add user from textField
		String userToBeAdded = userNameField.getText();
		if(userToBeAdded == null || userToBeAdded.equals("") || userToBeAdded.charAt(0) == ' ') {	// check proper text format
			userNameField.setText(null);
			InputController.userNotFound();
			return;
		}
		//System.out.println("Attempting to add new user...");
		userToBeAdded.trim();
		//System.out.println(userToBeAdded);
		
		if(userModel.locateUser(userToBeAdded) == -1 ) { //userToBeAdded is unique
			userModel.addUser(userToBeAdded);
			userNameField.setText(null);
			//System.out.println("Added " + userToBeAdded +" successfully!" );
			userNameField.setText(null);
			updateUserListView();
			return;
		}
		InputController.userNotFound();
		userNameField.setText(null);
		updateUserListView();
		return;
	}
	
	/**
	 * This method lists the users onto the GUI.
	 * 
	 * 
	 * 
	 * 
	 */
	public void updateUserListView() {
		if(userModel.getNonadminList().size() > 0 ) {
			listOfUsers.setItems(userModel.getNonadminStringFXList());
		}
	}
	/**
	 * This method handles when an admin wishes to delete a user.
	 * 
	 * 
	 * 
	 * 
	 */
	public void deleteUser() {										// Delete user from textField if available
		String userToBeDeleted = userNameField.getText();
		if(userToBeDeleted == null || userToBeDeleted.equals("") || userToBeDeleted.charAt(0) == ' ') {
				InputController.userNotFound();
				return;
		}
		//System.out.println("Attempting to delete user...");
		userToBeDeleted.trim();
		int index = userModel.locateUser(userToBeDeleted);
		if (index == -1) { 
			//CentralController.userNotFound();
			InputController.userNotFound();
			return;
		}
		//userToBeDeleted exists within the list
		userModel.deleteUser(userToBeDeleted);
		userNameField.setText(null);
		updateUserListView();
		//System.out.println("Deleted " + userToBeDeleted + " successfully!");
	}

}
