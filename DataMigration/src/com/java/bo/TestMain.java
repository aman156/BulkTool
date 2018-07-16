package com.java.bo;

import com.sforce.async.AsyncApiException;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.ws.ConnectionException;

public class TestMain {

	public static void main(String[] args) throws ConnectionException, AsyncApiException {
		String endPoint ="https://login.salesforce.com/services/Soap/u/41.0";
		String apiVer ="42.0";
		SfConnection sfConnc= BulkHelper.login("aman.msharma14@gmail.com", "deloitte.1LjQxdEAfWdMTlvfZsRf2i88G", endPoint, apiVer);
		DescribeGlobalResult dgr= BulkHelper.fetchAllObjects(sfConnc);
		System.out.println("\nDescribe Global Results:\n");
	    for (int i = 0; i < dgr.getSobjects().length; i++) 
	    {
	    	System.out.println(dgr.getSobjects()[i].getName());
	    } 
		
	}

}
