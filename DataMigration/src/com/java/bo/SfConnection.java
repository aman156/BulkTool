package com.java.bo;

import com.sforce.async.BulkConnection;
import com.sforce.soap.partner.PartnerConnection;

public class SfConnection {

	public BulkConnection bConnection = null;
	public PartnerConnection partnerConnection=null;
	public LoginWrapper credentials =null;
	
	public BulkConnection getbConnection() {
		return bConnection;
	}
	public void setbConnection(BulkConnection bConnection) {
		this.bConnection = bConnection;
	}
	public PartnerConnection getPartnerConnection() {
		return partnerConnection;
	}
	public void setPartnerConnection(PartnerConnection partnerConnection) {
		this.partnerConnection = partnerConnection;
	}
	
	
}
