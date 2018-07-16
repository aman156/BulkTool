package application;

import com.java.bo.SfConnection;
import com.sforce.soap.partner.DescribeGlobalResult;

public  class ApplicationContext {

	public static SfConnection sfConnection =null;
    public static String loginStatus ="";
    public static DescribeGlobalResult dgr;
	
}
