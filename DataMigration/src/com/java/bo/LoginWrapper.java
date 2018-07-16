package com.java.bo;

public class LoginWrapper {

	private String userName=null;
	private String password=null;
	private String endpoint=null;
	private String apiVer="42.0";
	
	public LoginWrapper(String userName, String password, String endpoint, String apiVer) {
		super();
		this.userName = userName;
		this.password = password;
		this.endpoint = endpoint;
		this.apiVer = apiVer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getApiVer() {
		return apiVer;
	}

	public void setApiVer(String apiVer) {
		this.apiVer = apiVer;
	}
	
}
