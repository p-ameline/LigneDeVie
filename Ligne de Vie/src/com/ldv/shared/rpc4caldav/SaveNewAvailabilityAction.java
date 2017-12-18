package com.ldv.shared.rpc4caldav;

import com.ldv.shared.calendar.Availability;
import com.ldv.shared.rpc.SessionActionModel;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class SaveNewAvailabilityAction implements Action<SaveNewAvailabilityResult> 
{
	private SessionActionModel _sessionElements ;
	private Availability       _newAvailability = new Availability() ;

	public SaveNewAvailabilityAction(final Availability newAvailability, final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;
		
		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;

		_newAvailability.initFromAvailability(newAvailability) ;
	}

  public SaveNewAvailabilityAction() 
	{
  	super() ;

  	_sessionElements = new SessionActionModel() ;
	}

  public Availability getAvailability() {
		return _newAvailability ;
	}
  
  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
}
