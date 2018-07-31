package com.java.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sforce.soap.partner.sobject.SObject;

public class ExportWrapper {
	
	public String objApiName;
	public  File location;
	public String queryString;
	List<SObject> records = new ArrayList<>();
	
	
}
