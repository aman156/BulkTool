package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.java.bo.BulkHelper;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;

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
