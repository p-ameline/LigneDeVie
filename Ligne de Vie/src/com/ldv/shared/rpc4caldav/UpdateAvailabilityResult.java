package com.ldv.shared.rpc4caldav;

import com.ldv.shared.calendar.Availability;

import net.customware.gwt.dispatch.shared.Result;

public class UpdateAvailabilityResult implements Result 
{
	private boolean      _bSuccess ;
	private Availability _availability = new Availability() ;
	private String       _sMessage ;
	
	public UpdateAvailabilityResult(final boolean bSuccess, final Availability availability, final String message) 
	{
		_bSuccess = bSuccess ;
		_sMessage = message ;
		
		if (null != availability)
			_availability.initFromAvailability(availability) ;
	}

	@SuppressWarnings("unused")
	private UpdateAvailabilityResult() 
	{
		_bSuccess = false ;
		_sMessage = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public String getMessage() {
		return _sMessage ;
	}
	
	public Availability getAvailability() {
		return _availability ;
	}
}
