package application;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HomePageController {

	private static final String EXPORT_FXML = "ExportView.fxml";
	
	@FXML
	public void export(ActionEvent ae) throws IOException {
		
		URL location = Main.class.getResource(EXPORT_FXML);
		Parent exportPg = FXMLLoader.load(location);
		Scene exportPgV = new Scene(exportPg);
		Main.getPrimaryStage().setScene(exportPgV);
		//Navigate to newly created scene
	}
}
