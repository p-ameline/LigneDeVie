package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class LdvValidatorUserResult implements Result 
{
	private boolean _bSuccess ;
	private String  _sMessage ;
	
	public LdvValidatorUserResult(){
		super() ;
		
		_bSuccess = false ;
		_sMessage = "" ;
	}
	
	public LdvValidatorUserResult(boolean bSucess, final String message) {
		_bSuccess = bSucess ;
		_sMessage = message ;
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
}
