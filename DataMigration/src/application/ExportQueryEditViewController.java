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

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
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
	public ObservableList<SelectedObjects> selectedList = FXCollections.observableArrayList();
	public ObservableList<SelectedObjects> exportList = FXCollections.observableArrayList();
	public String fieldsStr;
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";

	@FXML public Label filePath;
	private StringProperty filePathStr = new SimpleStringProperty();
	
	
	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
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
			selectedList.add(selObj);
			selectedList.sorted();
			editQueryButton.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	                final Stage dialog = new Stage();
	                Stage primaryStage = new Stage();
	                dialog.initModality(Modality.APPLICATION_MODAL);
	                dialog.initOwner(primaryStage);
	                VBox dialogVbox = new VBox(20);
	                Button edit = new Button("OK");
	                Button cancel = new Button("Cancel");
	                TextArea query = new TextArea(selObj.getQuery());
	                dialogVbox.getChildren().add(query);
	                dialogVbox.getChildren().add(edit);
	                dialogVbox.getChildren().add(cancel);
	                Scene dialogScene = new Scene(dialogVbox,400, 200);
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
							selectedList.get(selObj.getIndex()).setQuery(qStr);
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
	                selectedList.remove(selObj);
	            }
	         });
		}
		name.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("name"));
		apiName.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("apiName"));
		query.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("query"));
		editedQuery.setCellValueFactory(new PropertyValueFactory<SelectedObjects, Button>("editedQuery"));
		isSelected.setCellValueFactory(new PropertyValueFactory<SelectedObjects, CheckBox>("isSelected"));
		deleteRecord.setCellValueFactory(new PropertyValueFactory<SelectedObjects, Button>("deleteQuery"));
		table.setItems(selectedList);
	}
	public void exit(ActionEvent event) {
		System.exit(0);
	}
	public void saveAtLoc(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Csv Files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage primaryStage = new Stage();
        //fileChooser.setInitialFileName(" ");
        File file = fileChooser.showSaveDialog(primaryStage);
        String fileLoc = file.getAbsolutePath();
		System.out.println(fileLoc);
		ApplicationContext.directoryPath = fileLoc;
		filePathStr.set(fileLoc);
		filePath.textProperty().bindBidirectional(filePathStr);
		
	}
	public void exportObjects(ActionEvent event) {
		HashMap<String, ExportWrapper> exportMap = new HashMap<String, ExportWrapper>();
		List<String> objLst = new ArrayList<String>();
		for(SelectedObjects str : selectedList) {
			ExportWrapper expWrap = new ExportWrapper();
			expWrap.objApiName = str.getApiName();
			expWrap.queryString = str.getQuery();
			expWrap.fields = str.fields;
			System.out.println("header fields"+expWrap.fields);
			objLst.add(str.getApiName());
			//expWrap.location = Directory path 
			exportMap.put(str.getApiName(),expWrap);
		}
		BulkHelper.exportAll(exportMap);
		//System.out.println("new identifier"+exportMap.get("Account"));
		for(String objStr : objLst) {
			ExportWrapper exp = exportMap.get(objStr);
			if(!exp.records.isEmpty()) {
				List<SObject> recList = exp.records;
				List<String> fldLst = exp.fields;
				File fileName = new File(ApplicationContext.directoryPath+objStr+".csv");
				FileWriter fileWriter = null;
				try {
					fileWriter = new FileWriter(fileName);
					for(String fldStr : fldLst) {
						fileWriter.append(fldStr);
						fileWriter.append(COMMA_DELIMITER);
					}
					fileWriter.append('\n');
				
					int counter =0;
					for(SObject rec : recList) {
						counter++;
						for(String fldStr : fldLst) {
							System.out.print("field:value "+counter+" ->"+fldStr);
							Object fieldObject =rec.getField(fldStr);
							System.out.println(" : "+String.valueOf(rec.getField(fldStr)));
							System.out.println("condition :"+(fieldObject instanceof XmlObjectWrapper) );
							if(fieldObject instanceof XmlObjectWrapper ) 
							{
								fileWriter.append(null);
							}else
							{
								fileWriter.append("\"").append(String.valueOf(rec.getField(fldStr))).append("\"");
							}
							fileWriter.append(COMMA_DELIMITER);
						}
						fileWriter.append('\n');
					}
					fileWriter.flush();
					fileWriter.close();
					System.out.println("CSV file was created successfully !!!");
				}
				catch(Exception ex) {
					System.out.println("Error in CsvFileWriter !!!");
					ex.printStackTrace();
				}
			}
		}
	}//eof
}
