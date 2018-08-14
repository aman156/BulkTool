package application;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.java.bo.BulkHelper;
import com.java.bo.ExportWrapper;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.bind.XmlObjectWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;



public class TaskExecutor extends Task<HashMap<String,Object>>{

	
	public enum TASK {
		LOGIN,GENERATE_QUERY,EXPORT,QUERYABLE;
	};
	
	public  TASK executeFor =null;
	
	public static TaskExecutor getTaskExecutor(TASK task)
	{
		TaskExecutor taskExecutor = new TaskExecutor();
		for(TASK definedTask : TASK.values())
		{
			if(definedTask.equals(task))
			{
				taskExecutor.executeFor=definedTask;
				return taskExecutor;
			}
		}
		return taskExecutor;
	}
	@Override
	protected HashMap<String, Object> call() throws Exception {
		// TODO Auto-generated method stub
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		if(executeFor.equals(TASK.LOGIN))
		{
			System.out.println("inside login block");
			updateMessage("Connecting");
			ApplicationContext.sfConnection =BulkHelper.login(ApplicationContext.username,
												   ApplicationContext.password, 
												   ApplicationContext.endPoint, 
												   ApplicationContext.apiVersion);
			if(ApplicationContext.sfConnection==null)
			{
				updateMessage("Authentication Failed");
				return null;
			}
			
			ApplicationContext.dgr=BulkHelper.fetchAllObjects(ApplicationContext.sfConnection);
			
			if(ApplicationContext.dgr==null)
			{
				updateMessage("Unable to access metadata");
				return null;
			}
			
				updateMessage("Authentication Sucessful");
				try {
					Thread.sleep(1000);
					}catch (InterruptedException e) {
						updateMessage("Canceled");
						System.out.println("interrupted sleep");
					}
		}
		else if(executeFor.equals(TASK.GENERATE_QUERY))
		{
			
		}
		else if(executeFor.equals(TASK.EXPORT))
		{
			updateMessage("Initializing..");
			HashMap<String, ExportWrapper> exprtMap =new HashMap<String, ExportWrapper>();
			ApplicationContext.exportMap=exprtMap;
			List<String> objLst = new ArrayList<String>();
			for(SelectedObjects str : ApplicationContext.selectedList) {
				ExportWrapper expWrap = new ExportWrapper();
				expWrap.objApiName = str.getApiName();
				expWrap.queryString = str.getQuery();
				expWrap.fields = str.fields;
				System.out.println("header fields"+expWrap.fields);
				objLst.add(str.getApiName());
				//expWrap.location = Directory path 
				exprtMap.put(str.getApiName(),expWrap);
			}
			BulkHelper.exportAll(ApplicationContext.exportMap);
			for(String objStr : objLst) {
				updateMessage("Processing "+objStr+"..");
				ExportWrapper exp = ApplicationContext.exportMap.get(objStr);
				
					List<SObject> recList = exp.records;
					List<String> fldLst = exp.fields;
					File fileName = new File(ApplicationContext.directoryPath+objStr+".csv");
					FileWriter fileWriter = null;
					try {
						fileWriter = new FileWriter(fileName);
						for(String fldStr : fldLst) {
							fileWriter.append(fldStr);
							fileWriter.append(ApplicationContext.COMMA_DELIMITER);
						}
						fileWriter.append('\n');
						if(!exp.records.isEmpty()) { 
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
								fileWriter.append(ApplicationContext.COMMA_DELIMITER);
							}
							fileWriter.append('\n');
						}

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
			updateMessage("Exported "+objLst.size()+" Objects");
		}else if(executeFor.equals(TASK.QUERYABLE))
		{
			System.out.println("inside queryable block");
			ObservableList<DescribeGlobalSObjectResult> queryableSObjList =FXCollections.observableArrayList();
			DescribeGlobalSObjectResult[] objList =ApplicationContext.dgr.getSobjects();
			for(DescribeGlobalSObjectResult res : objList)
			{
				if(res.isQueryable())
				{
					queryableSObjList.add(res);
				}
			}
			ApplicationContext.queryableSObjList=queryableSObjList;
		}
		else
		{
			System.out.println("create object of TaskExecutor using static method - getTaskExecutor(TASK)");
		}
		
		return resultMap;
	}
	
	@Override
	protected void cancelled() {
		// TODO Auto-generated method stub
		System.out.println("task canceled");
		this.cancel();
		updateMessage("Canceled");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateMessage("Canceled");
	}
	

}
