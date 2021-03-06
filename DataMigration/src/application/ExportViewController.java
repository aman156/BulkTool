package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

import com.sforce.soap.partner.DescribeGlobalSObjectResult;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExportViewController implements Initializable  {
	private static final String EXPORT_FXML = "ExportQueryEditView.fxml";

	//List fetched from s f
	@FXML
	public ListView<String> availableList  = new ListView<String>();	
	//List fetched from s f cloned
	@FXML
	public ListView<String> clonedAvailbleList  = new ListView<String>();	
	// user selected objects list
	@FXML
	public ListView<String> selectedList = new ListView<String>();
	
	@FXML
	public TextField searchObject;
	
	@FXML public Label selectedNoObj;
	@FXML public Label availableNoObj;

	
	private StringProperty selectedNo = new SimpleStringProperty();
	private StringProperty availableNo = new SimpleStringProperty();
	
	ObservableList<String> availableObList = FXCollections.observableArrayList();
	ObservableList<String> clonedAvailableObList = FXCollections.observableArrayList();
	ObservableList<String> selectedObList = FXCollections.observableArrayList();
	ObservableList<String> clonedSelectedObList = FXCollections.observableArrayList();
	
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {

		System.out.println("Checking queryable objects :"+ApplicationContext.queryableSObjList.size());
		for (int i = 0; i < ApplicationContext.dgr.getSobjects().length; i++) 
	    {
			availableObList.add(ApplicationContext.dgr.getSobjects()[i].getName());
	    }
		
		clonedAvailbleList.setItems(availableObList);
		clonedAvailbleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ApplicationContext.selObjectsMap = new HashMap<String, DescribeGlobalSObjectResult>();
		availableNo.set(String.valueOf(availableObList.size()));
		System.out.println(availableNo);
		availableNoObj.textProperty().bindBidirectional(availableNo);
		selectedNo.set("0");
		selectedNoObj.textProperty().bindBidirectional(selectedNo);
	}
	
	@FXML
	public void getSelectedList(ActionEvent ae) throws IOException {
		selectedObList = clonedAvailbleList.getSelectionModel().getSelectedItems();
		clonedAvailableObList = clonedAvailbleList.getItems();
		selectedList.getItems().addAll(selectedObList);
		clonedAvailableObList.removeAll(selectedObList);
		clonedAvailbleList.setItems(clonedAvailableObList);
		clonedAvailbleList.getSelectionModel().clearSelection();
		selectedObList = selectedList.getItems();
		for(int j=0;j<selectedObList.size();j++) {
			for(int i=0;i<ApplicationContext.dgr.getSobjects().length;i++) {
				if(ApplicationContext.dgr.getSobjects()[i].getName().equals(selectedObList.get(j))) {
					if(!ApplicationContext.selObjectsMap.containsKey(selectedObList.get(j))) {
						ApplicationContext.selObjectsMap.put(ApplicationContext.dgr.getSobjects()[i].getLabel(),ApplicationContext.dgr.getSobjects()[i] );
					}
				}
			}
		}
		availableNo.set(String.valueOf(clonedAvailableObList.size()));
		availableNoObj.textProperty().bindBidirectional(availableNo);
		selectedNo.set(String.valueOf(selectedObList.size()));
		selectedNoObj.textProperty().bindBidirectional(selectedNo);
		//System.out.println(ApplicationContext.selObjectsMap);
	}
	@FXML
	public void getAvailableList(ActionEvent ae) throws IOException {
		clonedSelectedObList = selectedList.getSelectionModel().getSelectedItems();	
		System.out.println(clonedSelectedObList);
		for(String objName : clonedSelectedObList) {
			System.out.println("*****");
			for(String sobj : ApplicationContext.selObjectsMap.keySet()) {
				System.out.println(sobj);
			}
			/*if(ApplicationContext.selObjectsMap.containsKey(objName)) {
				ApplicationContext.selObjectsMap.remove(objName);
				System.out.println("HiThere"); 
			}*/
		}
		//System.out.println("********"+ApplicationContext.selObjectsMap);
		clonedAvailableObList = clonedAvailbleList.getItems();
		selectedObList = selectedList.getItems();
		clonedAvailbleList.getItems().addAll(clonedSelectedObList);
		selectedObList.removeAll(clonedSelectedObList);
		selectedList.setItems(selectedObList);
		selectedList.getSelectionModel().clearSelection();
		availableNo.set(String.valueOf(clonedAvailableObList.size()));
		availableNoObj.textProperty().bindBidirectional(availableNo);
		selectedNo.set(String.valueOf(selectedObList.size()));
		selectedNoObj.textProperty().bindBidirectional(selectedNo);
	}
	@FXML
	public void getSearchObject(ActionEvent ae) throws IOException {
		System.out.println("getSearchObject");
		String searchObjStr = searchObject.getText();
		clonedAvailableObList = clonedAvailbleList.getItems();
		boolean isObjPresent = clonedAvailableObList.contains(searchObjStr);
		if(isObjPresent) {
			clonedAvailableObList.clear();
			clonedAvailableObList.add(searchObjStr);
		}
		clonedAvailbleList.setItems(clonedAvailableObList);
	}
	@FXML
	public void searchAction(KeyEvent ae) throws IOException {
		System.out.println("in ActionEvent --> " + searchObject.getText());
		clonedAvailableObList = clonedAvailbleList.getItems();
		FilteredList<String> filteredObjects = new FilteredList<>(clonedAvailableObList, s -> true);
		searchObject.textProperty().addListener(obs ->{
			String enteredText = searchObject.getText().trim();
			if(enteredText.isEmpty()) {
				if(enteredText==null || enteredText.length()==0 || enteredText.contains(""))
				{
					System.out.println("in ActionEvent --> 2" + enteredText);
					filteredObjects.setPredicate(s->true);
					clonedAvailbleList.setItems(availableObList);
					clonedAvailbleList.getItems().removeAll(selectedList.getItems());
				}
			}
			else
			{
				filteredObjects.setPredicate(s->s.toLowerCase().startsWith(enteredText.toLowerCase()));
				ObservableList<String> filteredObList = FXCollections.observableArrayList();
				filteredObList.addAll(filteredObjects);
				clonedAvailbleList.setItems(filteredObList);
			}
		});
		//filteredObList.addAll(filteredObjects);
	    
		clonedAvailbleList.refresh(); 
	}
	@FXML
	public void download(ActionEvent ae) throws IOException {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage primaryStage = new Stage();
        fileChooser.setInitialFileName("Selectedbjects");
        File file = fileChooser.showSaveDialog(primaryStage);
        String fileLoc = file.getAbsolutePath();

		saveFile();		
	}
	
	private void saveFile() {
		clonedSelectedObList = selectedList.getItems();
		Workbook wb = new HSSFWorkbook();
		try {
			FileOutputStream op = new FileOutputStream("");
			wb.createSheet("Object").createRow(0).createCell(0).setCellValue("Objects");
			for(int i = 1; i<=clonedSelectedObList.size();i++) {
				String cellStr = (String) clonedSelectedObList.get(i-1);
				wb.getSheet("Object").createRow(i).createCell(0).setCellValue(cellStr);
			}
			wb.write(op);
			op.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	@FXML
	public void next(ActionEvent ae) throws IOException {
		URL location = Main.class.getResource(EXPORT_FXML);
		Parent exportPg = FXMLLoader.load(location);
		Scene exportPgV = new Scene(exportPg);
		Main.getPrimaryStage().setScene(exportPgV);
		
		
		//Navigate to newly created scene
	}

}
