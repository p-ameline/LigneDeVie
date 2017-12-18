package com.ldv.shared.rpc4caldav;

import com.ldv.shared.rpc.SessionActionModel;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class GetPlannedEventsAction implements Action<GetPlannedEventsResult> 
{
	private SessionActionModel _sessionElements ;
	private String             _sStartDate ;
	private String             _sEndDate ;

	public GetPlannedEventsAction(final String sStartDate, final String sEndDate, final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;
		
		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;

		_sStartDate = sStartDate  ;
		_sEndDate   = sEndDate ;
	}

  public GetPlannedEventsAction() 
	{
  	super() ;

  	_sessionElements = new SessionActionModel() ;
  	
  	_sStartDate = "" ;
  	_sEndDate   = "" ;
	}

  public String getStartDate() {
		return _sStartDate ;
	}
  
  public String getEndDate() {
		return _sEndDate ;
	}
  
  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
