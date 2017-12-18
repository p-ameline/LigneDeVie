package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class LdvGetLanguagesAction implements Action<LdvGetLanguagesResult> 
{	
	private String _sRoot ;
	
	public LdvGetLanguagesAction() {
		super() ;
		
		_sRoot = "" ;
	}
	
	public LdvGetLanguagesAction(String sRoot) 
	{
		_sRoot = sRoot ;
	}
	
	public String getRoot() {
		return _sRoot ;
	}
	public void setRoot(String sRoot) {
		_sRoot = sRoot ;
	}
}
