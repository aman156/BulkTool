package application;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.poi.hpsf.Property;

import com.sforce.soap.partner.DescribeGlobalSObjectResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class ExportQueryEditViewController implements Initializable{
	@FXML	private TableView<SelectedObjects> table;
	@FXML	private TableColumn<SelectedObjects, CheckBox> isSelected;
	@FXML	private TableColumn<SelectedObjects, String> name;
	@FXML	private TableColumn<SelectedObjects, String> apiName;
	@FXML	private TableColumn<SelectedObjects, String> query;
	@FXML	private TableColumn<SelectedObjects, Button> editedQuery;
	@FXML	private TableColumn<SelectedObjects, Button> deleteRecord;
	public ObservableList<SelectedObjects> selectedList = FXCollections.observableArrayList();
	

	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		for(DescribeGlobalSObjectResult dgrStr : ApplicationContext.selObjectsMap.values()) {
			CheckBox ch = new CheckBox();
			ch.setSelected(true);
			Button editQueryButton = new Button("Edit Query");
			Button deleteQueryButton = new Button("Delete Query");
			//String fields = dgrStr.getFields();
			SelectedObjects selObj = new SelectedObjects(dgrStr.getLabel(),dgrStr.getName(),"Select * from Obj1__c",editQueryButton,ch,deleteQueryButton);
			selectedList.add(selObj);
		}
		name.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("name"));
		apiName.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("apiName"));
		query.setCellValueFactory(new PropertyValueFactory<SelectedObjects, String>("query"));
		editedQuery.setCellValueFactory(new PropertyValueFactory<SelectedObjects, Button>("editedQuery"));
		isSelected.setCellValueFactory(new PropertyValueFactory<SelectedObjects, CheckBox>("isSelected"));
		deleteRecord.setCellValueFactory(new PropertyValueFactory<SelectedObjects, Button>("deleteQuery"));
		table.setItems(selectedList);
	}
	

}
