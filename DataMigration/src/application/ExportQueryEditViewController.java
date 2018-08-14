package application;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.java.bo.BulkHelper;
import com.java.bo.ExportWrapper;
import com.sforce.async.CSVReader;
import com.sforce.bulk.CsvWriter;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.bind.XmlObjectWrapper;

import application.TaskExecutor.TASK;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class ExportQueryEditViewController implements Initializable{
	@FXML	private TableView<SelectedObjects> table;
	@FXML	private TableColumn<SelectedObjects, CheckBox> isSelected;
	@FXML	private TableColumn<SelectedObjects, String> name;
	@FXML	private TableColumn<SelectedObjects, String> apiName;
	@FXML	private TableColumn<SelectedObjects, String> query;
	@FXML	private TableColumn<SelectedObjects, Button> editedQuery;
	@FXML	private TableColumn<SelectedObjects, Button> deleteRecord;
	@FXML 	public ImageView spinner;
	@FXML 	public Label exportSts;
	@FXML   public Button exportBtn;
	//public ObservableList<SelectedObjects> selectedList = FXCollections.observableArrayList();
	public String fieldsStr;

	@FXML public Label filePath;
	private StringProperty filePathStr = new SimpleStringProperty();
	
	
	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ObservableList<SelectedObjects> tempselectedList = FXCollections.observableArrayList();
		ApplicationContext.selectedList=tempselectedList;
		Integer counter=-1;
		for(DescribeGlobalSObjectResult dgrStr : ApplicationContext.selObjectsMap.values()) {
			counter++;
			CheckBox ch = new CheckBox();
			ch.setSelected(true);
			Button editQueryButton = new Button("Edit Query");
			Button deleteQueryButton = new Button("Delete Query");
			List<String> selectedObject= new ArrayList<String>();
			selectedObject.add(dgrStr.getName());
			HashMap<String,List<Field>> objectFieldMap = BulkHelper.getFieldsForSObjects(selectedObject);
			List<String> fields = new ArrayList<String>();
			String fieldsStr= "Select ";
			for(Field f :  objectFieldMap.get(dgrStr.getName())) {
					fieldsStr = fieldsStr.concat(f.getName()+", ");
					fields.add(f.getName());
			}
			fieldsStr = fieldsStr.substring(0,fieldsStr.length()-2);
			fieldsStr = fieldsStr.concat(" from "+dgrStr.getName());
			System.out.println(counter);
			SelectedObjects selObj = new SelectedObjects(counter,dgrStr.getLabel(),dgrStr.getName(),fieldsStr,editQueryButton,ch,deleteQueryButton,fields);
			tempselectedList.add(selObj);
			tempselectedList.sorted();
			editQueryButton.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	                final Stage dialog = new Stage();
	                Stage primaryStage = new Stage();
	                dialog.initModality(Modality.APPLICATION_MODAL);
	                dialog.initOwner(primaryStage);
	                VBox dialogVbox = new VBox(20);
	                HBox dialogHbox = new HBox(20);
	                Button edit = new Button("OK");
	                Button cancel = new Button("Cancel");
	                TextArea query = new TextArea(selObj.getQuery());
	                query.setWrapText(true);
	                dialogVbox.getChildren().add(query);
	                dialogHbox.getChildren().add(edit);
	                dialogHbox.getChildren().add(cancel);
	                dialogVbox.getChildren().add(dialogHbox);
	                Scene dialogScene = new Scene(dialogVbox,400,200);
	                dialog.setScene(dialogScene);
	                dialog.show();
	                edit.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							System.out.println(query.getText());
							SimpleStringProperty qStr = new SimpleStringProperty();
							qStr.set(query.getText());
							selObj.setQuery(qStr);
							System.out.println(selObj.getIndex());
							tempselectedList.get(selObj.getIndex()).setQuery(qStr);
							table.refresh();
							dialog.close();
						}
	                	
	                });
					cancel.setOnAction(new EventHandler<ActionEvent>() {

						@Override
						public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							dialog.close();
						}
						
					});
	            }
	         });
			deleteQueryButton.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	            	tempselectedList.remove(selObj);
	            }
	         });
		}
		name.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("name"));
		apiName.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("apiName"));
		query.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("query"));
		editedQuery.setCellValueFactory(new PropertyValueFactory<SelectedObjects, Button>("editedQuery"));
		isSelected.setCellValueFactory(new PropertyValueFactory<SelectedObjects, CheckBox>("isSelected"));
		deleteRecord.setCellValueFactory(new PropertyValueFactory<SelectedObjects, Button>("deleteQuery"));
		table.setItems(tempselectedList);
	}
	public void exit(ActionEvent event) {
		System.exit(0);
	}
	public void saveAtLoc(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Csv Files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage primaryStage = new Stage();
        fileChooser.setInitialFileName(" ");
        File file = fileChooser.showSaveDialog(primaryStage);
        String fileLoc = file.getAbsolutePath();
		System.out.println(fileLoc);
		ApplicationContext.directoryPath = fileLoc;
		filePathStr.set(fileLoc);
		filePath.textProperty().bindBidirectional(filePathStr);
		
	}
	public void exportObjects(ActionEvent event) {
		exportBtn.setDisable(true);
		spinner.setFitHeight(25);
		spinner.setFitWidth(25);
		spinner.setImage(new Image(Main.class.getResourceAsStream(ApplicationContext.spinnerPath)));
		
		
		//BulkHelper.exportAll(exprtMap );
		TaskExecutor	extrct =TaskExecutor.getTaskExecutor(TASK.EXPORT);
		exportSts.textProperty().bind(extrct.messageProperty());
		new Thread(extrct).start();
		

		extrct.setOnSucceeded(new javafx.event.EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				spinner.setImage(null);
				
			}
		});
		//System.out.println("new identifier"+exportMap.get("Account"));
	}//eof
}
