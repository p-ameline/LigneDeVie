package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class OpenGraphAction implements Action<OpenGraphResult> 
{
	private SessionActionModel _sessionElements ;
	private String             _sKey ;

	public OpenGraphAction(final String sLdvId, final String sUserId, final String sToken, final String sKey) 
	{
		super() ;

		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
		_sKey            = sKey ;
	}

  public OpenGraphAction() 
	{
  	super() ;

		_sessionElements = new SessionActionModel() ;
  	_sKey            = "" ;
	}

  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
	
	public String getKey() {
		return _sKey ;
	}
}
