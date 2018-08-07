package application;
import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.TaskExecutor.TASK;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController implements Initializable  {
 
	private static final String HOME_FXML = "HomePage.fxml";
	private static final String spinnerPath = "\\spinner.gif";
	@FXML
	public TextField username;
	@FXML
	public PasswordField password;	
	@FXML
	public PasswordField securityToken;
	@FXML
	public TextField endPoint;
	@FXML
	public TextField apiVersion;
	@FXML
	public Label authStatus;
	@FXML 
	public ImageView spinner;
	@FXML
	public Button loginBtn;
	
	@FXML
	public Button cancelBtn;
	
	TaskExecutor taskExecutor =null;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
	@FXML
	public void handleLogin(ActionEvent ae) throws IOException, InterruptedException {
		
		spinner.setFitHeight(25);
		spinner.setFitWidth(25);
		spinner.setImage(new Image(Main.class.getResourceAsStream("spinner.gif")));
		ApplicationContext.username="aman.msharma14@gmail.com";//username.getText();
		ApplicationContext.password="Deloitte@1As05Ayx2ytm0yDRpaQuScie1k";//password.getText();
		ApplicationContext.endPoint=endPoint.getText();
		//ApplicationContext.apiVersion=apiVersion.getText();
		
		taskExecutor =TaskExecutor.getTaskExecutor(TASK.LOGIN);
		authStatus.textProperty().bind(taskExecutor.messageProperty());
		
		new Thread(taskExecutor).start();
		
		taskExecutor.setOnSucceeded(new javafx.event.EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				spinner.setImage(null);
				URL location = Main.class.getResource(HOME_FXML);
				try {
				Parent homePg = FXMLLoader.load(location);
				Scene homePgV = new Scene(homePg);
				Main.getPrimaryStage().setScene(homePgV);
				}catch(Exception e)
				{
					e.printStackTrace();
				}
				
				//Navigate to newly created scene
			}
		});
		//Create new scene 
				
				//Thread.sleep(4000);
	}
	
	@FXML
	public void handleCancel(ActionEvent ae) {
		spinner.setImage(null);
		taskExecutor.cancelled();
		System.out.println("Cancel Pressed");
	}
	
}
