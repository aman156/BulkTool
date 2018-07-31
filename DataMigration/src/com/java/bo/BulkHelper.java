package com.java.bo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.sforce.async.AsyncApiException;
import com.sforce.async.BulkConnection;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import application.ApplicationContext;

public class BulkHelper {
	
	private static PartnerConnection partnerConnection=null;
	private static BulkConnection bulkConnection=null;
	private static SfConnection sfConnection = null;
 private static BufferedReader reader = new BufferedReader(
	         new InputStreamReader(System.in));

	/**
	 * Create the BulkConnection used to call Bulk API operations.
	 */
	public static SfConnection login(String userName, String password,String endPoint,String apiVer)
	      throws ConnectionException, AsyncApiException {
		sfConnection = new SfConnection();
	    ConnectorConfig connectorConfig = new ConnectorConfig();
	    connectorConfig.setUsername(userName);
	    connectorConfig.setPassword(password);
	    connectorConfig.setAuthEndpoint("https://login.salesforce.com/services/Soap/u/42.0");
	    partnerConnection =	new PartnerConnection(connectorConfig);
	    // When PartnerConnection is instantiated, a login is implicitly
	    // executed and, if successful,
	    // a valid session is stored in the ConnectorConfig instance.
	    // Use this key to initialize a BulkConnection:
	    ConnectorConfig connectorconfig2 = new ConnectorConfig();
	    connectorconfig2.setSessionId(connectorConfig.getSessionId());
	    // The endpoint for the Bulk API service is the same as for the normal
	    // SOAP uri until the /Soap/ part. From here it's '/async/versionNumber'
	    String soapEndpoint = connectorConfig.getServiceEndpoint();
	    String apiVersion = "42.0";
	    String restEndpoint = soapEndpoint.substring(0, soapEndpoint.indexOf("Soap/"))
	        + "async/" + apiVersion;
	    connectorconfig2.setRestEndpoint(restEndpoint);
	    // This should only be false when doing debugging.
	    connectorconfig2.setCompression(true);
	    // Set this to true to see HTTP requests and responses on stdout
	    connectorconfig2.setTraceMessage(false);
	    bulkConnection = new BulkConnection(connectorconfig2);
	    
	    sfConnection.setbConnection(bulkConnection);
	    sfConnection.setPartnerConnection(partnerConnection);
	    return sfConnection;
	}
	
	public static File getBulkObjectData(SfConnection sfConnection)
	{
		
		return null;
	}
	
	public static void exportAll(HashMap<String, ExportWrapper> exportMap)
	{
		try {
		QueryResult qResult = null;
		for(String objName : exportMap.keySet())
		{
			boolean done=false;
			ExportWrapper expWrapper 	= exportMap.get(objName);
			String queryStr				= expWrapper.queryString;
			sfConnection.getPartnerConnection().setQueryOptions(2000);
			System.out.println("Fetching started for "+objName+" Object.");
			qResult 					=sfConnection.getPartnerConnection().query(queryStr);
			if(qResult.getSize()>0)
			{
				 while (!done)
				 {
					 SObject[] records = qResult.getRecords() ;
					 expWrapper.records.addAll(Arrays.asList(records));
					 
					 if(qResult.isDone())
					 {
						 done = true;
					 }else
					 {
						 qResult = sfConnection.getPartnerConnection().queryMore(qResult.getQueryLocator());
					 }
				 }
			}else
			{
				 System.out.println("No records found.");
			}
				 System.out.println("\nQuery succesfully executed.");
		}
		}catch(ConnectionException ex)
		{
			ex.printStackTrace();
		}
		
		return;
	}
	public static DescribeGlobalResult fetchAllObjects(SfConnection sfConnection) 
	{
		if(sfConnection==null || sfConnection.getPartnerConnection()==null)
		{
			return null;
		}
		DescribeGlobalResult dgr=null;
		
		try {
			dgr = sfConnection.getPartnerConnection().describeGlobal();
		} catch (ConnectionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    return dgr;
	}
	
	public static HashMap<String,List<Field>> getFieldsForSObjects(List<String> objectName) 
	{
		HashMap<String,List<Field>> map = new HashMap<>();
		DescribeSObjectResult dsr = null;
		try{		
			for(String obj: objectName)
			{
				Field[] fields=null;
					dsr =sfConnection.getPartnerConnection().describeSObject(obj);
				
				if(dsr !=null)
				{
					fields = dsr.getFields();
				}
				if(fields !=null)
				{
					map.put(obj,Arrays.asList(fields));
				}	
			}
		 }catch (ConnectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return map;
	}

	public PartnerConnection getPartnerConnection() {
		return partnerConnection;
	}

	
	public BulkConnection getConnection() {
		return bulkConnection;
	}

	public String getUserInput(String prompt) {
	      String result = "";
	      try {
	         System.out.print(prompt);
	         result = reader.readLine();
	      } catch (IOException ioe) {
	         ioe.printStackTrace();
	      }

	      return result;
	   }
	public void logout() {
	      try {
	    	  partnerConnection.logout();
	         System.out.println("Logged out.");
	      } catch (ConnectionException ce) {
	         ce.printStackTrace();
	      }
	   }

	
	 public List<SObject> querySObject(String soqlQuery) {
		  
		 List<SObject> recordList = new ArrayList<>();
	     // soqlQuery = "SELECT FirstName, LastName FROM Contact";
	      try {
	         QueryResult qr = partnerConnection.query(soqlQuery);
	         boolean done = false;

	         if (qr.getSize() > 0) {
	            System.out.println("\nLogged-in user can see "
	                  + qr.getRecords().length + " contact records.");

	            while (!done) {
	               System.out.println("");
	               recordList.addAll(Arrays.asList(qr.getRecords()));
	               if (qr.isDone()) {
	                  done = true;
	               } else {
	                  qr = partnerConnection.queryMore(qr.getQueryLocator());
	               }
	            }
	         } else {
	            System.out.println("No records found.");
	         }
	      } catch (ConnectionException ce) {
	         ce.printStackTrace();
	      }
	      
	      return recordList;
	   }

	 public boolean validateQuery()
	 {
		 
		 return false;
	 }
	
	
}
