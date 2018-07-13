package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class MainWindowController implements Initializable{
	
	private static final String LOGIN_FXML = "LoginView.fxml";
	
	@FXML
	private AnchorPane rootPane;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("initialing Main Window");
		AnchorPane pane=null;
		try {
			 pane = FXMLLoader.load(Main.class.getResource(LOGIN_FXML));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rootPane.getChildren().setAll(pane);
	
	}
	
	
	

}
