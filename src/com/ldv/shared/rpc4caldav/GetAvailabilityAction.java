package com.ldv.shared.rpc4caldav;

import com.ldv.shared.rpc.SessionActionModel;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class GetAvailabilityAction implements Action<GetAvailabilityResult> 
{
	private SessionActionModel _sessionElements ;

	public GetAvailabilityAction(final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;
		
		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
	}

  public GetAvailabilityAction() 
	{
  	super() ;

  	_sessionElements = new SessionActionModel() ;
	}

  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
