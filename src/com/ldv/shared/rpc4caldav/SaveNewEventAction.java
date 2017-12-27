package com.ldv.shared.rpc4caldav;

import com.ldv.shared.calendar.Event;
import com.ldv.shared.rpc.SessionActionModel;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class SaveNewEventAction implements Action<SaveNewEventResult> 
{
	private SessionActionModel _sessionElements ;
	private Event              _newEvent = new Event() ;

	public SaveNewEventAction(final Event newEvent, final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;
		
		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;

		_newEvent.initFromEvent(newEvent) ;
	}

  public SaveNewEventAction() 
	{
  	super() ;

  	_sessionElements = new SessionActionModel() ;
	}

  public Event getEvent() {
		return _newEvent ;
	}
  
  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
