package controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Nonadmin;

/**
 * 
 * This class handles the login GUI page, which is the initial scene upon
 * beginning the program.
 * 
 * @author Brenda Michelle
 * @author Roy Jacome
 * 
 * 
 *
 */
public class LoginController extends CentralController{
	
	@FXML private Button loginButton;
	@FXML private TextField userName;
	
	public void enterPressed(KeyEvent e) {		// Enter key pressed
		if(e.getCode() == KeyCode.ENTER) {
			ActionEvent event = null;
			checkLogin(event);
		}
	}
	
	/**
	 * This method checks whether the user name input is valid or not.
	 * Upon, verifying that the user is a current user or admin, they are sent to
	 * the appropriate scene.
	 * 
	 * @param e The action that prompted this method to run.
	 * 
	 * 
	 */
	public void checkLogin(ActionEvent e) {	// check if user input is ok
		String checkUser = userName.getText();
		
		if(checkUser == null || checkUser.equals("") || checkUser.charAt(0) == ' ' ) {	// check good input
			InputController.emptyFieldError();
			userName.setText(null);
			return;
		}
		else if((checkUser.trim()).equalsIgnoreCase("admin")){
			userName.setText(null);
			CentralController.switchToAdmin();
			return;
		}
		
		Nonadmin currentUser = userModel.searchForUser(checkUser.trim());
		
		if(currentUser == null) {
			userName.setText(null);
			InputController.userNotFound();
		}
		else {
			userModel.setCurrentUser(currentUser);
			//albumController = new AlbumController();
			//albumController.setUpAlbum();
			//AlbumController.setUpAlbum();
			fromLogin2Album();
			//update album with user info before sending them
		}
	}
}
