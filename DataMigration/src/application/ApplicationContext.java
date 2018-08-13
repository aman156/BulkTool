package application;

import java.util.HashMap;

import com.java.bo.ExportWrapper;
import com.java.bo.SfConnection;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;

import javafx.collections.ObservableList;
import javafx.scene.Scene;

public  class ApplicationContext {

	public static SfConnection sfConnection =null;
    public static String loginStatus ="";
    public static DescribeGlobalResult dgr;
    public static DescribeGlobalSObjectResult selectedObjectsGlobalList;
    public static HashMap<String, DescribeGlobalSObjectResult> selObjectsMap;
    public static DescribeSObjectResult[] fields;
    public static HashMap<String, DescribeSObjectResult> objectMap;
    public static String directoryPath =null;
    public static String fieldStr ="";
	public static final String protocol = "file:";
	public static String rootpath = System.getProperty("user.dir");
	public static final String imgPath = "\\win.png";
	public static String username ="";
	public static String password ="";
	public static String endPoint ="";
	public static String apiVersion ="";
	public static Scene currentScene =null;
	public static ObservableList<DescribeGlobalSObjectResult> queryableSObjList=null;
	public static final String spinnerPath = "\\spinner.gif";
	public static HashMap<String, ExportWrapper> exportMap;
	public static final String COMMA_DELIMITER = ",";
	public static final String NEW_LINE_SEPARATOR = "\n";
	public static ObservableList<SelectedObjects> selectedList ;

}
