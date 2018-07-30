package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class SelectedObjects {

	private final SimpleStringProperty name;
	private CheckBox isSelected;
	private final SimpleStringProperty apiName;
	private final SimpleStringProperty query;
	private final Button editedQuery;
	private final Button deleteQuery;
	public SelectedObjects(String name, String apiName, String query, Button editedQuery,CheckBox ch,Button deleteQuery) {
		super();
		this.name = new SimpleStringProperty(name);
		this.apiName = new SimpleStringProperty(apiName);
		this.query = new SimpleStringProperty(query);
		this.editedQuery = editedQuery;
		this.isSelected  = ch;
		this.deleteQuery = deleteQuery;
	}
	public CheckBox getIsSelected() {
		return isSelected; 
	}
	public void setIsSelected(CheckBox isSelected) {
		this.isSelected = isSelected;
	}
	public String getName() {
		return name.getValue();
	}
	public String getApiName() {
		return apiName.getValue();
	}
	public String getQuery() {
		return query.getValue();
	}
	public Button getEditedQuery() {
		return editedQuery;
	}
	public Button getDeleteQuery() {
		return deleteQuery;
		
	}
	
}
