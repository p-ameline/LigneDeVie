package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class LdvRegisterUserResult implements Result 
{
	private boolean _bSuccess ;
	private String  _sMessage ;
	private String  _sGeneratedId ;
	
	public LdvRegisterUserResult(){
		super() ;
		
		_bSuccess     = false ;
		_sMessage     = "" ;
		_sGeneratedId = "" ;
	}
	
	public LdvRegisterUserResult(boolean bSucess) {
		_bSuccess     = bSucess ;
		_sMessage     = "" ;
		_sGeneratedId = "" ;
	}
	
	public void setSuccess(boolean bSuccess) {
		_bSuccess = bSuccess ;
	}
	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public void setMessage(String sMessage) {
		_sMessage = sMessage ;
	}
	public String getMessage() {
		return _sMessage ;
	}
	
	public void setGeneratedId(String sId) {
		_sGeneratedId = sId ;
	}
	public String getGeneratedId() {
		return _sGeneratedId ;
	}
}
