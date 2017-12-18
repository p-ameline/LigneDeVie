package com.ldv.shared.rpc4caldav;

import com.ldv.shared.rpc.SessionActionModel;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class DeleteComponentAction implements Action<DeleteComponentResult> 
{
	private SessionActionModel _sessionElements ;
	private String             _sUID ;

	public DeleteComponentAction(final String sUID, final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;
		
		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;

		_sUID = sUID ;
	}

  public DeleteComponentAction() 
	{
  	super() ;

  	_sessionElements = new SessionActionModel() ;
	}

  public String getUID() {
		return _sUID ;
	}
  
  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
