package application;

import java.util.HashMap;

import com.java.bo.SfConnection;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;

public  class ApplicationContext {

	public static SfConnection sfConnection =null;
    public static String loginStatus ="";
    public static DescribeGlobalResult dgr;
    public static DescribeGlobalSObjectResult selectedObjectsGlobalList;
    public static HashMap<String, DescribeGlobalSObjectResult> selObjectsMap;
    public static DescribeSObjectResult[] fields;
    public static HashMap<String, DescribeSObjectResult> objectMap;
}
