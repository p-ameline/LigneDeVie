package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class LdvGetStatusAction implements Action<LdvGetStatusResult> 
{
	private SessionActionModel _sessionElements ;
	
	public LdvGetStatusAction(final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;

		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
	}
	
	public LdvGetStatusAction() 
	{
		super() ;
		
		_sessionElements = new SessionActionModel() ;
	}
	
	public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
