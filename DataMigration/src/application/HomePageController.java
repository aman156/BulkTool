package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class HomePageController implements Initializable {
	
	@FXML public Label userName;
	private StringProperty name = new SimpleStringProperty();

	private static final String EXPORT_FXML = "ExportView.fxml";
	
	@FXML
	public void export(ActionEvent ae) throws IOException {
		
		URL location = Main.class.getResource(EXPORT_FXML);
		Parent exportPg = FXMLLoader.load(location);
		Scene exportPgV = new Scene(exportPg);
		Main.getPrimaryStage().setScene(exportPgV);
		//Navigate to newly created scene
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		name.set("Welcome User");
		userName.textProperty().bindBidirectional(name);
	}
}
