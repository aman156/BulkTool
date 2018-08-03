package com.java.bo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sforce.soap.partner.sobject.SObject;

public class ExportWrapper {
	
	public String objApiName;
	public  File location;
	public String queryString;
	public List<SObject> records = new ArrayList<>();
	public List<String> fields = new ArrayList<String>();
	@Override
	public String toString() {
		return "ExportWrapper [objApiName=" + objApiName + ", location=" + location + ", queryString=" + queryString
				+ ", records=" + records + ", fields=" + fields + "]";
	}
	
}
