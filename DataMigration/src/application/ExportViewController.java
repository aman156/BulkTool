package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.sforce.async.SObject;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Cell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
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
	
	ObservableList<String> availableObList = FXCollections.observableArrayList();
	ObservableList<String> clonedAvailableObList = FXCollections.observableArrayList();
	ObservableList<String> selectedObList = FXCollections.observableArrayList();
	ObservableList<String> clonedSelectedObList = FXCollections.observableArrayList();
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (int i = 0; i < ApplicationContext.dgr.getSobjects().length; i++) 
	    {
			availableObList.add(ApplicationContext.dgr.getSobjects()[i].getName());
	    }
		
		clonedAvailbleList.setItems(availableObList);
		clonedAvailbleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		selectedList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		ApplicationContext.selObjectsMap = new HashMap<String, DescribeGlobalSObjectResult>();
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
		System.out.println(ApplicationContext.selObjectsMap);
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
		
	}
	@FXML
	public void getSearchObject(ActionEvent ae) throws IOException {
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
