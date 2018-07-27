package application;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.java.bo.BulkHelper;
import com.java.bo.SfConnection;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.DescribeSObject_element;
import com.sforce.ws.ConnectionException;

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
		String endPoint ="https://login.salesforce.com/services/Soap/u/41.0";
		String apiVer ="42.0";
		SfConnection sfConnc=null;
		try {
			sfConnc = BulkHelper.login("aman.msharma14@gmail.com", "deloitte.1LjQxdEAfWdMTlvfZsRf2i88G", endPoint, apiVer);
		} catch (ConnectionException | AsyncApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DescribeGlobalResult result= BulkHelper.fetchAllObjects(sfConnc);
		ApplicationContext.dgr=result;
		DescribeSObjectResult[] t = null;
		
		System.out.println("\nDescribe Global Results:\n");
	    for (int i = 0; i < result.getSobjects().length; i++) 
	    {
	    	//result.getSobjects()[i].
	    	System.out.println(result.getSobjects()[i].getName());
	    	//System.out.println(result.getSobjects()[i].getD
	    } 
		//Update Status on Login UI
	    ApplicationContext.loginStatus="Successful";
		//Create new scene 
		URL location = Main.class.getResource(HOME_FXML);
		Parent homePg = FXMLLoader.load(location);
		Scene homePgV = new Scene(homePg);
		Main.getPrimaryStage().setScene(homePgV);
		//Navigate to newly created scene
		System.out.println("Login Completed.Please wait !");
		
		//Thread.sleep(4000);
		
	}
	
	@FXML
	public void handleCancel(ActionEvent ae) {
		System.exit(0);
		System.out.println("Cancel Pressed");
	}
	
}
