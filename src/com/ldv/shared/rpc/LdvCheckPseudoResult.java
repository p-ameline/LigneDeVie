package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class LdvCheckPseudoResult implements Result 
{
	private boolean _bAlreadyExists ;
	private boolean _bSuccessfulRequest ;
	
	public LdvCheckPseudoResult(){
		super() ;
		
		_bAlreadyExists     = false ;
		_bSuccessfulRequest = false ;
	}
	
	public LdvCheckPseudoResult(boolean bExists, boolean bSuccess) {
		_bAlreadyExists     = bExists ;
		_bSuccessfulRequest = bSuccess ;
	}
	
	public void setAlreadyExists(boolean bExists) {
		_bAlreadyExists = bExists ;
	}
	public boolean doesAlreadyExist() {
		return _bAlreadyExists ;
	}
	
	public void setRequestSuccess(boolean bSuccess) {
		_bSuccessfulRequest = bSuccess ;
	}
	public boolean wasRequestSuccessful() {
		return _bSuccessfulRequest ;
	}
}
