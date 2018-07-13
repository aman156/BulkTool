package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController implements Initializable  {
 
	private static final String HOME_FXML = "HomePage.fxml";
	
	@FXML
	public TextField username;
	@FXML
	public PasswordField password;	
	@FXML
	public PasswordField securityToken;
	@FXML
	public TextField endPoint;
	@FXML
	public Label authStatus;
	 	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	@FXML
	public void handleLogin(ActionEvent ae) throws IOException, InterruptedException {
		//Call Login Method 
		//Update Status on Login UI 
		//Create new scene 
		URL location = Main.class.getResource(HOME_FXML);
		Parent homePg = FXMLLoader.load(location);
		Scene homePgV = new Scene(homePg);
		Main.getPrimaryStage().setScene(homePgV);
		//Navigate to newly created scene
		System.out.println("Login Completed.Please wait !");
		authStatus.setText("Login Pressed");
		//Thread.sleep(4000);
		
	}
	
	@FXML
	public void handleCancel(ActionEvent ae) {
		System.exit(0);
		System.out.println("Cancel Pressed");
	}
	
}
