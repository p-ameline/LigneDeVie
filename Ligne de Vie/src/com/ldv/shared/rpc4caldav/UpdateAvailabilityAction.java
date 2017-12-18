package com.ldv.shared.rpc4caldav;

import com.ldv.shared.calendar.Availability;
import com.ldv.shared.rpc.SessionActionModel;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class UpdateAvailabilityAction implements Action<UpdateAvailabilityResult> 
{
	private SessionActionModel _sessionElements ;
	private Availability       _newAvailability = new Availability() ;

	public UpdateAvailabilityAction(final Availability newAvailability, final String sLdvId, final String sUserId, final String sToken) 
	{
		super() ;
		
		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;

		_newAvailability.initFromAvailability(newAvailability) ;
	}

  public UpdateAvailabilityAction() 
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
