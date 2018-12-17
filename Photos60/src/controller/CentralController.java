package controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import model.Nonadmin;
import model.UserHelper;

/**
 * This class is the central part that makes changing scenes and stages easier.
 * It also contains the serialize and de-serialize functions from where
 * the application opens and closes attaining or saving data respectively.
 * 
 * 
 * @author Brenda Michelle
 * @author Roy Jacome
 * @version 1
 * 
 * 
 *
 */
public class CentralController {
	
	protected static Scene loginScene;
	
	protected static Scene adminScene;
	
	protected static Scene albumScene;
	
	protected static Scene photoScene;
	
	protected static Stage loginStage;
	
	protected static Stage albumStage;
	
	protected static Stage photoStage;
	
	protected static Stage currentStage;
	
	protected static AdminController adminController;
	
	protected static LoginController loginController;
	
	protected static AlbumController albumController;
	
	protected static PhotoController photoController;
	
	protected static UserHelper userModel;
	

	@SuppressWarnings("static-access")
	public void start(Stage primaryStage) throws Exception{

		
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));   			
			Parent root = loader.load();
			loginScene = new Scene(root,600,400);
			loginStage = primaryStage;
			
			loginStage.setScene(loginScene);
			loginStage.setTitle("Photos Login");
			loginStage.show();
			loginController = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
			Parent root = loader.load();
			adminScene = new Scene(root);
			adminController = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumView.fxml"));
			Parent root = loader.load();
			albumScene = new Scene(root);
			albumController = loader.getController();
		}
		
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));			
			Parent root = loader.load();
			photoScene = new Scene(root);
			photoController = loader.getController();
		}
		
		
		
		currentStage = loginStage;
		currentStage.setResizable(false);
		albumStage = new Stage();

		albumStage.setResizable(false);
		
		userModel = deserialize(userModel);	
		
		//update all observable lists with the de-serialized user_list array list.
		userModel.updateLists();
		
		
		primaryStage.setOnHidden(event -> {try {
			CentralController.serialize(userModel);
		} catch (IOException a) {
			a.printStackTrace();
		}});
		
		
		albumController.albumStage.setOnHidden(e -> {
			try {
				//CentralController.closeFromDifferentStage();
				CentralController.serialize(userModel);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		 
	}

	/**
	 * This is a method that can be used to close the application from another stage.
	 * It makes sure to save the data by calling the serialize() method.
	 * @throws IOException if the serialization was not successful
	 */
	 public static void closeFromDifferentStage() throws IOException {
	       // System.out.println("Forcing quit from AlbumView or PhotoView! Saving files...");
	        CentralController.serialize(CentralController.userModel);
	        Platform.exit();
	    }
	

	/**
	 * This method is called from central controller when the primary stage
	 * is being forced to close.  It also calls serialize().
	 * 
	 */
	public static void saveFile() {
		currentStage.setOnHidden(event -> {try {
		CentralController.serialize(userModel);
			} catch (IOException a) {
				//System.out.println("Tried to save on window closing but couldnt (Central Controller)");
				a.printStackTrace();
			}});
	}
	
	
	/**
	 * This method is used to save all the information of the program
	 * by serializing the data into a .dat file.
	 * @param userModel the model of the user
	 * @throws IOException if data cannot be serialized.
	 * 
	 */
	public static void serialize(UserHelper userModel) throws IOException {
		try {
			FileOutputStream file = new FileOutputStream("photolibdata.dat");
			ObjectOutputStream outstream = new ObjectOutputStream(file);
			outstream.writeObject(userModel);
			outstream.close();
			file.close();
			}
		catch (IOException e) {
			e.printStackTrace();
			}
	}
	/**
	 * This method is used to retrieve the data by de-serializing the .dat file
	 * containing the program data.  If there is no data to be retrieved, which
	 * will occur the first time around, the method adds a user named "Stock"
	 * and fills an album containing some stock photos.
	 * @param userModel the model of the user
	 * @throws IOException if the desrialization is not possible
	 * @throws ClassNotFoundException if the class is not found
	 * @return userModel which is the model of the user

	 */
	public static UserHelper deserialize(UserHelper userModel) throws IOException, ClassNotFoundException {
		//System.out.println("Deserializing...");
		try {
			FileInputStream file = new FileInputStream("photolibdata.dat");
			ObjectInputStream instream = new ObjectInputStream(file);
			userModel = (UserHelper) instream.readObject();
			instream.close();
			file.close();
			//System.out.println("Reading file...");
		} catch (IOException | ClassNotFoundException c) {
			//c.printStackTrace();
			userModel = null;
			//System.out.println("First time opening application:");
		}
		if(userModel == null) {
			//System.out.println("Creating Stock...");
			userModel = new UserHelper();
			userModel.addUser("Stock");
			userModel.setCurrentUser(null);
			
			Nonadmin stock = userModel.searchForUser("Stock");
			String albumName = "Landscapes~";
			stock.createAlbum(albumName);
			stock.getAlbumObject(albumName).addPhoto("Beach1.jpg", "I'm in miami beach!", "File:stockphotos/Beach1.jpg");
			stock.getAlbumObject(albumName).addPhoto("ScaryForest.jpg", "So scary out here.", "File:stockphotos/ScaryForest.jpg");
			stock.getAlbumObject(albumName).addPhoto("ForestLantern.jpg", "Show me da way", "File:stockphotos/ForestLantern.jpg");
			stock.getAlbumObject(albumName).addPhoto("MountainRange1.jpg", "Hourglass curves", "File:stockphotos/MountainRange1.jpg");
			stock.getAlbumObject(albumName).addPhoto("Skyline1.jpg", "Earth and Space meet-up for coding a view.", "File:stockphotos/Skyline1.jpg");
			//stock.getAlbumObject(albumName).addPhoto("Sunset1.jpg", "Anotha day, anotha dolla", "File:stockphotos/Sunset1.jpg");
			stock.getAlbumObject(albumName).setAlbumThumbnail(stock.getAlbumObject(albumName).getPhotoArrayList().get(0));
			
			serialize(userModel);
			return userModel;
		}
		else {
			userModel.setCurrentUser(null);
			return userModel;
		}
	}
	
	
   
	/**
	 * This method gets the file of a photo.
	 * @return File
	 * 
	 */
	public static File getNewPhotoFile() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("Open Resource File");
		
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("Image Files",
						"*.bmp", "*.png", "*.jpg", "*.gif", ".jpeg", "*.gif")
				);
		 File file = fileChooser.showOpenDialog(currentStage);
		
		return file;
	}
	/**
	 * This method is called from the login LoginController.
	 * It is called when the admin wishes to login.
	 */
	public static void switchToAdmin()  {
		adminController.updateUserListView();
		currentStage.setTitle("Admin");
		currentStage.setScene(adminScene);
	}
	/**
	 * This method is called when the admin wishes to logout.
	 */
	public static void logoutFromAdmin() {
		currentStage.setTitle("Photos Login");
		currentStage.setScene(loginScene);
	}
	/**
	 * This is a method called from LoginController.  It takes us to the stage and scene
	 *  that displays  albums.
	 */
	public static void fromLogin2Album() {
		currentStage.hide();
		currentStage = albumStage;
		String user = userModel.getCurrentUserNameString();
		albumController.setUpAlbum();
		currentStage.setTitle(user + "'s Photos");
		currentStage.setScene(albumScene);
		currentStage.show();
	}
	
	/**
	 * This method hides the photo stage.
	 */
	public static void lastPhotoCopied() {
		photoStage.hide();
		photoStage = null;
	}
	
	/**
	 * This is a  method called from LoginController.  It takes us to the scene that
	 * displays the photo slide show view.
	 * 
	 */
	public static void fromAlbum2PhotoView() {
		
		if(photoStage == null) {
			photoStage = new Stage();
			photoStage.setTitle(userModel.getCurrentAlbum().getAlbumName());
			photoStage.setResizable(false);
			photoStage.setScene(photoScene);
			photoController.setUpWindowToViewPhoto();
			photoStage.show();
		}
		else {
			photoStage.hide();
			photoStage = new Stage();
			photoStage.setTitle(userModel.getCurrentAlbum().getAlbumName());
			photoStage.setResizable(false);
			photoStage.setScene(photoScene);
			photoController.setUpWindowToViewPhoto();
			photoStage.show();
		}
		
	}
	
	/**
	 * This is a  method called from LoginController.  It takes us to 
	 * initial login scene.
	 * 
	 * 
	 */
	public static void fromAlbum2Login() {
		currentStage.hide();
		currentStage = loginStage;
		currentStage.setTitle("Photos Login");
		currentStage.setScene(loginScene);
		currentStage.show();
	}
}
