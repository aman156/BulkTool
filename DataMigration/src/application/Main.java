package application;
	
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	
	private static final String imgPath = "\\win.png";
	private static final String protocol = "file:";
	private static final String TITLE = "Data Migration Tool";
	private static final String ROOT_FXML = "MainWindow.fxml";
	public  static Stage primaryStage  ;

	@Override
	public void start(Stage primaryStage) { 
		try {
			this.primaryStage = primaryStage;
			//BorderPane root = new BorderPane();
			//Scene scene = new Scene(root,400,400);
			String rootpath = System.getProperty("user.dir");
			System.out.println(rootpath);
			URL url = Main.class.getResource(ROOT_FXML);
			System.out.println("location: "+url );
			Parent root = FXMLLoader.load(url);
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setTitle(TITLE);
			primaryStage.getIcons().add(new Image(protocol+rootpath+imgPath));
			primaryStage.setScene(scene);
			 primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	
}
