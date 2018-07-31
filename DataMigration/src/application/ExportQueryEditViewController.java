package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import com.java.bo.BulkHelper;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.Field;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
	String fieldsStr;

	@Override 
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		for(DescribeGlobalSObjectResult dgrStr : ApplicationContext.selObjectsMap.values()) {
			CheckBox ch = new CheckBox();
			ch.setSelected(true);
			Button editQueryButton = new Button("Edit Query");
			Button deleteQueryButton = new Button("Delete Query");
			List<String> selectedObject= new ArrayList<String>();
			selectedObject.add(dgrStr.getName());
			HashMap<String,List<Field>> objectFieldMap = BulkHelper.getFieldsForSObjects(selectedObject); 
			List<Field> fields = objectFieldMap.get(dgrStr.getName());
			fieldsStr= "Select ";
			for(Field f : fields) {
					fieldsStr = fieldsStr.concat(f.getName()+", ");
			}
			fieldsStr = fieldsStr.substring(0,fieldsStr.length()-2);
			fieldsStr = fieldsStr.concat(" from "+dgrStr.getName());
			System.out.println(fieldsStr);
			SelectedObjects selObj = new SelectedObjects(dgrStr.getLabel(),dgrStr.getName(),fieldsStr,editQueryButton,ch,deleteQueryButton);
			selectedList.add(selObj);
			selectedList.sorted();
			editQueryButton.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	                final Stage dialog = new Stage();
	                Stage primaryStage = new Stage();
	                dialog.initModality(Modality.APPLICATION_MODAL);
	                dialog.initOwner(primaryStage);
	                VBox dialogVbox = new VBox(20);
	                //String strText = fieldsStr;
	                dialogVbox.getChildren().add(new TextArea(fieldsStr));
	                Scene dialogScene = new Scene(dialogVbox,400, 200);
	                dialog.setScene(dialogScene);
	                dialog.show();
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
		
	}
}
