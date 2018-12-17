package app;

import java.io.IOException;

import controller.CentralController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Photos extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			CentralController centralController = new CentralController();
			centralController.start(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		 primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	          public void handle(WindowEvent we) {
	              //System.out.println("Stage is closing");
	          }
	      });    
	}
	
	/*@Override
	public void stop(){
	    CentralController.saveFile();
	}*/
	
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
