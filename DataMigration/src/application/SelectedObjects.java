package application;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

public class SelectedObjects {

	private Integer index;
	private final SimpleStringProperty name;
	private CheckBox isSelected;
	private  SimpleStringProperty apiName;
	private  SimpleStringProperty query;
	private  Button editedQuery;
	private  Button deleteQuery;
	public  List<String> fields;
	
	public SelectedObjects(Integer index,String name, String apiName, String query, Button editedQuery,CheckBox ch,Button deleteQuery,List<String> fields) {
		super();
		this.name = new SimpleStringProperty(name);
		this.apiName = new SimpleStringProperty(apiName);
		this.query = new SimpleStringProperty(query);
		this.editedQuery = editedQuery;
		this.isSelected  = ch;
		this.deleteQuery = deleteQuery;
		this.fields = fields;
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
	public void setApiName(SimpleStringProperty apiName) {
		this.apiName = apiName;
	}
	public void setQuery(SimpleStringProperty query) {
		this.query = query;
	}
	public void setEditedQuery(Button editedQuery) {
		this.editedQuery = editedQuery;
	}
	public void setDeleteQuery(Button deleteQuery) {
		this.deleteQuery = deleteQuery;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
}
