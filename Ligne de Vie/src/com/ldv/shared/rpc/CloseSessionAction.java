package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class CloseSessionAction implements Action<CloseSessionResult> 
{
	private SessionActionModel _sessionElements ;

	public CloseSessionAction(final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;

		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
	}

  public CloseSessionAction() 
	{
  	super() ;

		_sessionElements = new SessionActionModel() ;
	}

	public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
