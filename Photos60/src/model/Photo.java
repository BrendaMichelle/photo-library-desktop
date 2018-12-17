package model;
import java.util.ArrayList;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serializable;

/**
 * This class consists of non-static methods that modify a Photo object's
 * attributes including a photo's list of Tag objects. 
 * 
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 * 
 *
 */

public class Photo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4221182973538904793L;
	protected String fileName;
	protected String caption;
	protected Calendar dateStamp;
	protected ArrayList<Tag> tags_arraylist;
	protected transient ObservableList<Tag> tags_obslist;
	protected String path;
		
	public Photo(String fileName, String caption, String path) {
		
		this.fileName = fileName;
		this.caption = caption;
		this.path = path;
		this.dateStamp = Calendar.getInstance();
		dateStamp.set(Calendar.MILLISECOND,0);	
		this.tags_arraylist = new ArrayList<Tag>();
		this.tags_obslist = FXCollections.observableArrayList();
		
	}
	/**
	 * This method retrieves a photo's observable array list of tags.
	 * @return tags_obslist this is the ObservableList of tags of the photo
	 */
	public ObservableList<Tag> getFXTagArrayList(){
		return tags_obslist;
	}
	/**
	 * This method retrieve's a photo's array list of tags.
	 * @return tags_arraylist the ArrayList of tags of the photo
	 */
	public ArrayList<Tag> getTagArrayList(){
		return tags_arraylist;
	}
	/**
	 * This method updates a photo's non serializable tag lists with the 
	 * serializable array list version.
	 */
	public void updateTagLists() {
		tags_obslist = FXCollections.observableArrayList(tags_arraylist);
	}

	/**
	 * This method retrieves a photo's path variable.
	 * @return path String
	 */
	public String getPath() {
		return path;
	}
	/**
	 * This method retrieves a photo's dateStamp variable.
	 * @return path String
	 */
	public String getDate() {
		return dateStamp.getTime().toString();
	}
	/**
	 * This method changes a photo's caption variable.
	 * @param newCaption the new caption of the photo
	 */
	public void setCaption(String newCaption) {
		caption = newCaption;
	}
	/**
	 * This method retrieves a photo's caption variable.
	 * @return caption
	 */
	public String getCaption() {
		return caption;
	}
	/**
	 * This method retrieves a photo's fileName variable.
	 * @return fileName
	 */
	public String getFileName() {
		
		return fileName;
	}
	
	/**
	 * This method allows a caption to be updated or edited.
	 * @param caption The new caption for the photo
	 */
	public void addCaption( String caption) {
		this.caption = caption;
	}
	/**
	 * This caption allows a tag to be added to the
	 * picture's tag list.
	 * @param type The type of tag 
	 * @param value The actual tag itself
	 * 
	 */
	public void addTag(String type, String value) {
		this.tags_arraylist.add(new Tag(type, value));
		this.tags_obslist.add(new Tag(type, value));
		updateTagLists();
	}
	
	
	/**
	 * @param type The type of tag 
	 * @param value The actual tag itself
	 * @return The index of the location of a specified tag represented as a positive
	 * integer.  If a tag cannot be found, then it returns -1.
	 */
	public int getTagLocation(String type, String value) {
		for (int i = 0; i < this.tags_arraylist.size(); i++) {
			if (tags_arraylist.get(i).equals(new Tag(type, value))) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * This method deletes a specified tag from the picture's tag list.
	 * @param type The type of tag 
	 * @param value The actual tag itself
	 * @return true if successful, false otherwise 
	 */
	public boolean deleteTag(String type, String value) {
		int index = this.getTagLocation(type, value);
		if (index != -1) {
			this.tags_arraylist.remove(index);
			this.tags_obslist.remove(index);
			return true;
		}
		else {
			return false;
		}
	}
	
	/*
	 * 
	 * This method renames the photo.
	 * @param name The name the user wants to name the photo.
	 * @return void
	 */
	public void renamePhoto(String name) {
		this.fileName = name;
	}	
	/**
	 * This method compares two photo objects.
	 * @return true if they match, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		
		if(o == null || !(o instanceof Photo)) {
			return false;
		}
		
		Photo checkPhoto = (Photo)o;
		
		if(!(checkPhoto.fileName).equals(fileName)) {
			return false;
		}
		
		return true;
	}
}
