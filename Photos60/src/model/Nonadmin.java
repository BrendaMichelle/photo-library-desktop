package model;
import java.util.ArrayList;

import controller.InputController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
/**
 * This class holds an ArrayList of all of a user's
 * photo album_list and the operations that can be done
 * upon the interaction's of the album_list.
 * 
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 *
 */

 public class Nonadmin implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6411687708282573795L;
	private ArrayList<Album> album_list;
	private transient ObservableList<Album> album_FXList;
	
	private String username;
	
	public Nonadmin(String username) {
		this.album_list = new ArrayList<Album>();	
		this.album_FXList = FXCollections.observableArrayList();
		this.username = username;
	}
	
	/**
	 * This method retrieves a user's Album object from a list if it exists.
	 * @param albumName This is the album for which we're looking for.
	 * @return Album object if it exists, null otherwise.
	 */
	public Album getAlbumObject(String albumName) {
		int index = this.searchForAlbum(albumName);
		if(index >= 0) {
			return album_list.get(index);
		}
		
		return null;
	}
	/**
	 * This method updates all a user's non serializable 
	 * album lists with the serializable array list version.
	 */
	public void updateAlbumLists() {
		album_FXList = FXCollections.observableArrayList(album_list);
		//user_FXList = (ObservableList<Nonadmin>) user_list;
		//user_StringFXList = FXCollections.observableArrayList();
		for(int i = 0; i < album_list.size(); i++) {
			//user_StringFXList.add(user_list.get(i).getUsername());
			album_list.get(i).updatePhotoLists();
		}
	}
	/**
	 * This method returns the array list of albums
	 * @return an array list containing the albums
	 */
	public ArrayList<Album> getAlbumList(){
		return album_list;
	}
	/**
	 * This method returns the observable list of albums.
	 * @return an observable list of albums.
	 */
	public ObservableList<Album> getAlbumFXList (){
		return album_FXList;
	}
	/**
	 * This method gets the username.
	 * @return the username string.
	 */
	public String getUsername () {
		return username;
	}
	/**
	 * This method moves a photo from one album to another.
	 * @param pic The photo to be moved.
	 * @param fromAlbum The album from which the photo is being removed.
	 * @param toAlbum The album to which to move the photo.
	 * 
	 */
	public void movePhotoTo(Photo pic, Album fromAlbum, Album toAlbum ) {
		if (toAlbum.addPhoto(pic.fileName, pic.caption, pic.path) == true) {
			fromAlbum.deletePhoto(pic.fileName);
		}	
	}
/**
 	* This method copies a photo from one album to another.
 * @param pic The photo we wish to copy
 * @param toAlbum the destination of the photo
 * 
 */
	public void copyPhotoTo(Photo pic, Album toAlbum) {
	if(toAlbum.getPhotoLocation(pic.fileName) != -1) {
		InputController.copiedPhotoExists();
		return;
	}
	else{ 
		Photo pic_copy = new Photo(pic.fileName, pic.caption, null);
		toAlbum.photos_arraylist.add(pic_copy);
		}
	}
/**
 	* This method searches for the album within the album list.
 * @param albumName the album we are searching for.
 * @return index of the album in the list if it exists. -1 otherwise.
 */
	public int searchForAlbum(String albumName) {
	for(int i = 0; i < album_list.size(); i++ ) {
		if(album_list.get(i).albumName.equalsIgnoreCase(albumName)) {//it exists
			return i;
		}
	}
	return -1; //doesn't exist
}
 
 /**
  * This method creates a new album if the name is unique and 
  * adds it to the album lists.
  * @param albumName This is the name of the album to be added.
  * @return returns the newly created Album
  */
 public Album createAlbum(String albumName){
	 if(searchForAlbum(albumName) == -1) {
		 Album newalbum = new Album(albumName);
		 //album_FXList.add(newalbum);
		 album_list.add(newalbum);
		 return newalbum;
	 }
	 else {
		 InputController.albumExists();
		 return null;
	 	}
	 
 	}
 /**
  * This method creates a new album given an observable list of photos
  *  and adds the album to the user's album list.
  * @param a an observableList of photos
  * @return an album if the album was successfully added, null otherwise
  */
 	public Album addTempAlbumObjectFromPhotoList(ObservableList<Photo> a) {
 		String tempname = Long.toHexString(Double.doubleToLongBits(Math.random()));
 		Album tempAlbum = new Album(tempname);
 		
 		if( a == null ) {
 			return null;
 		}
 		tempAlbum.photos_obslist = FXCollections.observableArrayList(a);
 		for(int i = 0; i < a.size(); i ++) {
 			tempAlbum.photos_arraylist.add(a.get(i));
 		}
 		tempAlbum.startDate = tempAlbum.getStartDate();
 		tempAlbum.endDate = tempAlbum.getEndDate();
 		
 		this.album_list.add(tempAlbum);
 		this.album_FXList.add(tempAlbum);
 		//a.clear();
 		return tempAlbum;
 		
 	}
 	/**
 	 * This method removes an album from a user's album list if it exists.
 	 * @param albumName This is the album name of the album the user wishes 
 	 * to discard.
 	 * @return true if successful, false otherwise
 	 */
 	public boolean deleteAlbum(String albumName) {
 		int index = this.searchForAlbum(albumName); 
 		if(index >= 0 ) {
 			//this.album_FXList.remove(index);
 			this.album_list.remove(index);
 			return true;
 		}
 		else {
 			InputController.albumMissing();
 			return false;
 		}
 	}
 
 	/**
	 * This method locates all the photos that the user has with a specified tag or pair of tags.
	 * @param type This is the type of the tag we are searching for.
	 * @param value This is the value of the tag we are searching for.
	 * @param type2 This is the type of the second tag we are searching for.
	 * @param value2 This is the value of the second tag we are searching for.
	 * @return An observable list containing all the matched photos if any.
 	 * @throws IOException if an object cannot be serialized
 	 * @throws ClassNotFoundException  if class is not found
	 */
	public ObservableList<Photo> searchForPhotosWithTag(String type, String value, String type2, String value2) throws IOException, ClassNotFoundException{
		ObservableList <Photo> photosWithMatchingTags = null;
		photosWithMatchingTags = FXCollections.observableArrayList();
		//ArrayList<Photo> pal = new ArrayList<Photo>();
		Boolean tagfound = false;
		String t;
		String v;
		if(((type2.length() == 0 && value2.length() == 0) && (type.length() > 0 && value.length() > 0)) ^ ((type.length() == 0 && value.length() == 0 ) && (type2.length() > 0 && value2.length() > 0))) {
			t = type2.length() == 0 ? type : type2; 
			v = value2.length() == 0 ? value : value2; 
			//System.out.println("Hello there, we are inside the loop! " + t + " " + v);
			for(Album a: this.album_list) {
				for(Photo p: a.photos_arraylist) {
					for(int i = 0; i < p.tags_arraylist.size(); i++) {
						if(p.tags_arraylist.get(i).getType().compareToIgnoreCase(t) == 0 && p.tags_arraylist.get(i).getValue().compareToIgnoreCase(v) == 0) {   
							//photosWithMatchingTags.add(p);
							ByteArrayOutputStream b = new ByteArrayOutputStream();
							ObjectOutputStream oos = new ObjectOutputStream(b);
							oos.writeObject(p);
							oos.flush();
							oos.close();
							b.close();
							byte[] byteData = b.toByteArray();
							ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
							Photo pic = (Photo) new ObjectInputStream(bais).readObject();
							photosWithMatchingTags.add(pic);
							
							
						}
					}
				}
			}
		}
		
		else if(type2 != null && value2 != null && type != null && value!= null) {
			for(Album a: this.album_list) {
				for(Photo p: a.photos_arraylist) {
					for(int i = 0; i < p.tags_arraylist.size(); i++) {
						if(p.tags_arraylist.get(i).getType().compareToIgnoreCase(type) == 0 && p.tags_arraylist.get(i).getValue().compareToIgnoreCase(value) == 0) {
							if(tagfound == true) {
								photosWithMatchingTags.add(p);
							}
							tagfound = true;
						}
						if(p.tags_arraylist.get(i).getType().compareToIgnoreCase(type2) == 0 && p.tags_arraylist.get(i).getValue().compareToIgnoreCase(value2) == 0) {
							if(tagfound == true) {
								//photosWithMatchingTags.add(p);
								ByteArrayOutputStream b = new ByteArrayOutputStream();
								ObjectOutputStream oos = new ObjectOutputStream(b);
								oos.writeObject(p);
								oos.flush();
								oos.close();
								b.close();
								byte[] byteData = b.toByteArray();
								ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
								Photo pic = (Photo) new ObjectInputStream(bais).readObject();
								photosWithMatchingTags.add(pic);
							}
							tagfound = true;
							}
						}
					tagfound = false;
					}
				}
			}
		return photosWithMatchingTags;
	}
	/**
	 * 
	 * @param fullDate This is a string representing a date in the format yyyy-MM-dd.
	 * @param fullDate2 This is a string representing a date in the format yyyy-MM-dd.
	 * @return photosWithinRange This is an observableList (from JavaFX) of Photo objects,
	 * null otherwise.
	 */
	public ObservableList<Photo> searchPhotosWithinDateRange(String fullDate, String fullDate2){
		ObservableList<Photo> photosWithinRange = FXCollections.observableArrayList();
		if(fullDate == null || fullDate2 == null) { 
			return null; 
			}
		int year = Integer.parseInt(fullDate.substring(0, 3));
		int month = Integer.parseInt(fullDate.substring(5, 6));
		int day = Integer.parseInt(fullDate.substring(8, 9));
		
		int year2 = Integer.parseInt(fullDate2.substring(0, 3));
		int month2 = Integer.parseInt(fullDate2.substring(5, 6));
		int day2 = Integer.parseInt(fullDate2.substring(8, 9));
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		
		
		for(Album a: album_list) {
			for(Photo p: a.photos_arraylist) {
				String tempFullDate = f.format(p.dateStamp.getTime()); //format: yyyy-MM-dd
				int pyear = Integer.parseInt(tempFullDate.substring(0, 3));
				int pmonth = Integer.parseInt(tempFullDate.substring(5, 6));
				int pday = Integer.parseInt(tempFullDate.substring(8, 9));
				
				
				
				if((pyear >= year && pyear <= year2) || (pyear <= year && pyear >= year2)) {
					if((pmonth >= month && pmonth <= month2) || (pmonth <= month && pmonth >= month2)) {
						if((pday >= day && pday <= day2) || (pday <= day && pday >= day2)) {
							photosWithinRange.add(p);
						}
					}
				}
			}
		} 
		return photosWithinRange;
	}
	
}