package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class GetGraphAction implements Action<GetGraphResult> 
{
	private SessionActionModel _sessionElements ;

	public GetGraphAction(final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;

		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
	}

  public GetGraphAction() 
	{
  	super() ;

		_sessionElements = new SessionActionModel() ;
	}

  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
