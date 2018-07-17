package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;

public class ExportViewController implements Initializable  {
	private static final String EXPORT_FXML = "ExportView.fxml";

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
	}
	
	@FXML
	public void getSelectedList(ActionEvent ae) throws IOException {
		selectedObList = clonedAvailbleList.getSelectionModel().getSelectedItems();
		clonedAvailableObList = clonedAvailbleList.getItems();
		selectedList.getItems().addAll(selectedObList);
		clonedAvailableObList.removeAll(selectedObList);
		clonedAvailbleList.setItems(clonedAvailableObList);
		clonedAvailbleList.getSelectionModel().clearSelection();
	}
	@FXML
	public void getAvailableList(ActionEvent ae) throws IOException {
		clonedSelectedObList = selectedList.getSelectionModel().getSelectedItems();	
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

}
