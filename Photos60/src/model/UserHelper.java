package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import controller.InputController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * This class handles admin duties and user data transferring.
 * 
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 * 
 * 
 * 
 * 
 * 
 */
public class UserHelper implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8607030169966139192L;
	private  ArrayList<Nonadmin> user_list;
	private  transient ObservableList<Nonadmin> user_FXList;
	private  transient ObservableList<String> user_StringFXList;
	private  Nonadmin currentUser;
	private Album currentAlbum;
	private Photo currentPhoto;
	
	public UserHelper() {
			user_list = new ArrayList<Nonadmin>();
			user_FXList = FXCollections.observableArrayList();
			user_StringFXList = FXCollections.observableArrayList();
		}
	/**
	 * Sets the current photo the user is viewing
	 * @param newPhoto the photo the user wants to select
	 */
	public void setCurrentPhoto(Photo newPhoto) {
		currentPhoto = newPhoto;
	}
	/**
	 * Gets the current photo that the user is viewing.
	 * @return currentPhoto a Photo object which we wish to locate
	 * 
	 * 
	 * 
	 */
	public Photo getCurrentPhoto() {
		return currentPhoto;
	}
	/**
	 * This method sets the UserHelper's currentAlbum field according to the
	 * given Album object parameter.
	 * @param 
	 * 		album This is the album that the user is currently clicked on.
	 * 
	 * 
	 */
	public void setCurrentAlbum(Album album) {
		currentAlbum = album;
	}
	/**
	 * This method gets the current Album object.
	 * 
	 * @return an Album object
	 * 
	 * 
	 */
	public Album getCurrentAlbum() {
		return currentAlbum;
	}
	/**
	 * This method tracks who the logged in user is.
	 * @param user 
	 * 		This is the user object of the current user.
	 * 
	 * 
	 * 
	 * 
	 */
	public void setCurrentUser(Nonadmin user) {
		currentUser = user;
	}
	/**
	 * This method returns the user name of the user currently logged in.
	 * @return currentUser The user object of the user logged in.
	 * 
	 * 
	 * 
	 * 
	 */
	public Nonadmin getCurrentUser() {
		return currentUser;
	}
	/**
	 * This get the current user's user name.
	 * @return The current user's user name.
	 * 
	 * 
	 * 
	 */
	public String getCurrentUserNameString() {
		return currentUser.getUsername();
	}
	/**
	 * This method returns the array list of nonadmin users
	 * @return an array list containing the nonadmin users
	 * 
	 * 
	 * 
	 */
	public ArrayList<Nonadmin> getNonadminList(){
		return user_list;
	}
	/**
	 * This method returns the observable list of nonadmin users.
	 * @return an observable list of nonadmin users.
	 * 
	 * 
	 * 
	 */
	public ObservableList<Nonadmin> getNonadminFXList (){
		return user_FXList;
	}
	/**
	 * This method returns the observable list of nonadmin string user names.
	 * @return an observable list of string user names.
	 * 
	 * 
	 * 
	 */
	public ObservableList<String> getNonadminStringFXList (){
		return user_StringFXList;
	}
	/**
	 * This method is called when admin wishes to add a new user.
	 * It firsts checks if the user already exists and if such
	 * a user doesn't, then it adds the new user to both the 
	 * list of user_list used in the backe nd and the list of user_list
	 * in the observable list.
	 * 
	 * @param username This is the new user name of the user to be added.
	 * 
	 * 
	 * 
	 * 
	 */
	public void addUser(String username) {
		String a = "admin";
		if((locateUser(username) != -1) || a.equalsIgnoreCase(username)){
			InputController.userExists();
			return;
		}
		Nonadmin new_user = new Nonadmin(username);
		user_list.add(new_user);
		if(user_FXList == null) { user_FXList = FXCollections.observableArrayList(); }
		if(user_StringFXList == null) { user_StringFXList = FXCollections.observableArrayList(); }
		user_FXList.add(new_user);
		user_StringFXList.add(new_user.getUsername());

	}
	/**
	 * This method searches the user list for a given user name and
	 * if it is found, it returns the index of that user within the
	 * lists.
	 * 
	 * @param username This is the user name of the user we wish to locate.
	 * @return -1 if the user is not found.  If the user is found,
	 * it returns 0 or greater, indicating its index.
	 * 
	 * 
	 */
	public int locateUser(String username) {
		for(int i = 0; i < user_list.size(); i ++) { 
			if(user_StringFXList.get(i).equalsIgnoreCase(username)){
				return i;			
				}
		}
		return -1;
	}
	/**
	 * This method deletes a user from the back end user list
	 * as well as the observable list if the given user to delete exists.
	 * 
	 * @param username This is the user name of the user to be deleted.
	 * @throws NoSuchElementException if the user name cannot be located.
	 * 
	 * 
	 */
	public void deleteUser(String username) throws NoSuchElementException{
		int index = locateUser(username);
		if(index == -1) {
			throw new NoSuchElementException();
		}
		else {
			user_list.remove(index);
			user_FXList.remove(index);
			user_StringFXList.remove(index);
		}
	}
	/**
	 * This method finds a specified user if they exist and returns them as a nonadmin object.
	 * @param username This is a string of the user's user name.
	 * @return nonadmin This is the nonadmin user that was found if any.
	 * 
	 * 
	 * 
	 */
	public Nonadmin searchForUser(String username) {
		int index = locateUser(username);
		if( index >= 0) {
			return user_FXList.get(index);
		}
		
		return null;
	}
	/**
	 * This method prints out the list of users.
	 * 
	 * 
	 */
	public void printUserList() {
		for(int i = 0; i < user_list.size(); i++) {
			System.out.println(user_list.get(i).getUsername());
		}
	}
	
	
	/**
	 * This method updates the observablelists based on the serializable 
	 * array list called user_list.
	 * 
	 * 
	 * 
	 */
	public void updateLists() {
		user_FXList = FXCollections.observableArrayList(user_list);
		user_StringFXList = FXCollections.observableArrayList();
		for(int i = 0; i < user_list.size(); i++) {
			user_StringFXList.add(user_list.get(i).getUsername());
			user_list.get(i).updateAlbumLists();
		}
	}
}
