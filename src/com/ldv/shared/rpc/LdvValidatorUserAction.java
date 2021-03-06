package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class LdvValidatorUserAction implements Action<LdvValidatorUserResult> 
{	
	private String _sId ;
	private String _sPass ;
	private String _sKey ;
	
	public LdvValidatorUserAction() 
	{
		super() ;
		
		_sId   = "" ;
		_sKey  = "" ;
		_sPass = "" ;
	}
	
	public LdvValidatorUserAction(String sId, String sPass, String sKey) 
	{
		_sId   = sId ;
		_sPass = sPass ;
		_sKey  = sKey ;
	}
	
	public String getId() {
		return _sId ;
	}
	
	public String getPassword() {
		return _sPass ;
	}
	
	public String getKey() {
		return _sKey ;
	}
}
