package application;
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
		System.out.println("LoginController-inside handleLogin");
		spinner.setFitHeight(25);
		spinner.setFitWidth(25);
		spinner.setImage(new Image(Main.class.getResourceAsStream(ApplicationContext.spinnerPath)));
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
				System.out.println("new task to extract queryable objects");
				TaskExecutor	extrct =TaskExecutor.getTaskExecutor(TASK.QUERYABLE);
				new Thread(extrct).start();
				}catch(Exception e)
				{
					e.printStackTrace();
				}				
			}
		});
		
		System.out.println("LoginController-exit handleLogin");
	}
	
	@FXML
	public void handleCancel(ActionEvent ae) {
		spinner.setImage(null);
		taskExecutor.cancelled();
		System.out.println("Cancel Pressed");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		authStatus.textProperty().unbind();
		authStatus.setText("");
	}
	
}
