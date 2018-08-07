package application;

import java.util.HashMap;

import com.java.bo.BulkHelper;

import javafx.concurrent.Task;



public class TaskExecutor extends Task<HashMap<String,Object>>{

	
	public enum TASK {
		LOGIN("LOGIN"),GENERATE_QUERY("GENERATE_QUERY"),EXPORT("EXPORT");
		
		String executeFor; 
		TASK(String val)
		{	
			executeFor=val;
		}
		String value()
		{
			return executeFor;
		}
	};
	
	public  TASK executeFor =null;
	
	public static TaskExecutor getTaskExecutor(TASK task)
	{
		TaskExecutor taskExecutor = new TaskExecutor();
		for(TASK definedTask : TASK.values())
		{
			if(definedTask.compareTo(task)>0)
			{
				
				taskExecutor.executeFor=definedTask;
			}
		}
		return taskExecutor;
	}
	@Override
	protected HashMap<String, Object> call() throws Exception {
		// TODO Auto-generated method stub
		HashMap<String,Object> resultMap = new HashMap<String, Object>();
		System.out.println("inside call method");	
		
		if(executeFor.compareTo(TASK.LOGIN)>0)
		{
			System.out.println("calling login method");
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
				
				System.out.println("exiting login method");
		}
		else if(executeFor.compareTo(TASK.GENERATE_QUERY)>0)
		{
			
		}
		else if(executeFor.compareTo(TASK.EXPORT)>0)
		{
			
		}else
		{
			System.out.println("create object of TaskExecutor using static method - getTaskExecutor(TASK)");
		}
		
		try {
		Thread.sleep(1000);
		}catch (InterruptedException e) {
			updateMessage("interrupted sleep");
			System.out.println("interrupted sleep");
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
