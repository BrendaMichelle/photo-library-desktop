package model;
import java.util.ArrayList;
import java.util.Calendar;
import controller.InputController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;
import java.text.SimpleDateFormat;
/**
 * This class includes non-static methods that deal with basic album 
 * getters and setters, as well as photo operations in regards to an album.
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 *
 */

public class Album implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7690987572178701801L;
	protected ArrayList<Photo> photos_arraylist;
	protected transient ObservableList<Photo> photos_obslist;
	protected String albumName;
	protected String startDate;
	protected String endDate;
	protected Photo albumThumbnail;
	
	public Album(String name) {
		this.albumName = name;
		this.photos_arraylist = new ArrayList<Photo>();
		this.photos_obslist = FXCollections.observableArrayList();
		
	}
	/**
	 * This method changes an album's albumName field.
	 * @param newName the name to which a certain album
	 * should be changed.
	 */
	public void renameAlbumTo(String newName) {
		this.albumName = newName;
	}
	/**
	 * This method retrieves an album's observable list
	 * of photos.
	 * @return photos_obslist which is an observableList of photos
	 */
	public ObservableList<Photo> getPhotoObsList(){
		return photos_obslist;
	}
	/**
	 * This method updates all an album's non serializable 
	 * photo lists with the serializable array list version.
	 */
	public void updatePhotoLists() {
		photos_obslist = FXCollections.observableArrayList(photos_arraylist);
		//user_FXList = (ObservableList<Nonadmin>) user_list;
		//user_StringFXList = FXCollections.observableArrayList();
		for(int i = 0; i < photos_arraylist.size(); i++) {
			//user_StringFXList.add(user_list.get(i).getUsername());
			photos_arraylist.get(i).updateTagLists();
		}
	}
	/**
	 * this method allows us to retrieve an album's albumThumbnail variable.
	 * @return a Photo object
	 */
	public Photo getAlbumThumbnail() {
		return albumThumbnail;
	}
	/**
	 * This method allows us to set an album's albumThumbnail variable, 
	 * which is a photo.
	 * @param newThumbnail the thumbnail we wish to set to the album.
	 */
	public void setAlbumThumbnail(Photo newThumbnail) {
		albumThumbnail = newThumbnail;
	}
	/**
	 * This method allows us to find an album's startDate by performing
	 * a search through the photos to find the photo with the earliest
	 * date.
	 * @return a string of the date from the earliest photo.
	 */
	public String getStartDate() {
		Calendar startDate;
		startDate = this.photos_arraylist.size() != 0 ? this.photos_arraylist.get(0).dateStamp : null;
		
		for(Photo p: this.photos_arraylist) {
			if(startDate.compareTo(p.dateStamp) >= 0) {
				startDate = p.dateStamp;
			}
		}
		if(startDate == null) { return null; }
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(startDate.getTime());
	}
	/**
	 * This method allows us to find an album's endDate by performing 
	 * a search through the photos to find the photo with the latest
	 * date.
	 * @return a string of the date from the latest photo.
	 */
	public String getEndDate() {
		Calendar endDate;
		endDate = this.photos_arraylist.size() != 0 ? this.photos_arraylist.get(0).dateStamp : null;
		
		for(Photo p: this.photos_arraylist) {
			if(endDate.compareTo(p.dateStamp) < 0) {
				endDate = p.dateStamp;
			}
		}
		if(endDate == null) { return null; }
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(endDate.getTime());
	}

	/**
	 * This method allows us to set an ablum's startDate variable.
	 * @param newStartDate the newest photo Calendar 
	 */
	public void setStartDate(Calendar newStartDate) {
		
		if(startDate == null) {
			startDate = newStartDate.getTime().toString();
		}
	}
	/**
	 * This method allows us to set an album's endDate variable.
	 * @param newEndDate the lastest photo date
	 */
	public void setEndDate(Calendar newEndDate) {
		
		if(endDate == null) {
			endDate = newEndDate.getTime().toString();
		}
	}
	
	/**
	 * This method returns the name of the album.
	 * @return name of the album.
	 */
	public String getAlbumName() {
		return albumName;
	}
	/**
	 * @param name The name of the photo we're searching for
	 * @return The index of the location of a specified photo represented as a positive
	 * integer.  If a photo cannot be found, then it returns -1.
	 */
	public int getPhotoLocation(String name) {
		for (int i = 0; i < this.photos_arraylist.size(); i++) {
			if (photos_arraylist.get(i).fileName.compareTo(name) == 0) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * This method adds an already existing photo to an album's lists.
	 * @param existingPhoto the photo object we wish to add.
	 * @return true if the add was successful, false otherwise
	 */
	public boolean addPhoto(Photo existingPhoto) {
		String photosName = existingPhoto.getFileName();
		
		if(getPhotoLocation(photosName) != -1) {
			return false;
		}
		
		photos_arraylist.add(existingPhoto);
		photos_obslist.add(existingPhoto);
		return true;
	}
	
	/*
	 * This method creates a new photo object and adds it to an album.
	 * @param photo name This is what the photo is to be called.
	 * @param caption This is the photo's optional caption.
	 * @return true is successful, false if otherwise
	 */
	public boolean addPhoto(String photoName, String caption, String path) {
		if(this.getPhotoLocation(photoName) != -1) {
			return false;
		}

		Photo pic = new Photo(photoName, caption, path);
		

		this.photos_arraylist.add(pic);
		this.photos_obslist.add(pic);
		this.startDate = this.getStartDate();
		this.endDate = this.getEndDate();
		return true;
	}
	/**
	 * This method removes a photo from a specified album.
	 * @param name  This is the name of the photo we want to remove.
	 * @return true if successful, false otherwise 
	 */
	public boolean deletePhoto(String name) {
		int index = this.getPhotoLocation(name);
		if(index == -1) {
			InputController.photoDoesNotExist();
			return false;
		}
		else {
			this.photos_arraylist.remove(index);
			//this.photos_obslist.remove(index);
			this.startDate = this.getStartDate();
			this.endDate = this.getEndDate();
			return true;
		}
	}
	/**
	 * This method is used for testing.  It prints out the photo names of all 
	 * the photos in a given album.
	 */
	public void printPhotoFileNames() {
		for(int i = 0; i < this.photos_arraylist.size(); i++) {
			System.out.println(this.photos_arraylist.get(i).fileName);
		}
	}
	/**
	 * This method retrieves an albums photo array list.
	 * @return this.photos_Arraylist which is an ArrayList of photos
	 */
	public ArrayList<Photo> getPhotoArrayList(){
		return this.photos_arraylist;
	}
	
}
