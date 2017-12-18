package com.ldv.shared.rpc4caldav;

import com.ldv.shared.calendar.Event;

import net.customware.gwt.dispatch.shared.Result;

public class SaveNewEventResult implements Result 
{
	private boolean _bSuccess ;
	private Event   _event = new Event() ;
	private String  _sMessage ;
	
	public SaveNewEventResult(final boolean bSuccess, final Event event, final String message) 
	{
		_bSuccess = bSuccess ;
		_sMessage = message ;
		
		if (null != event)
			_event.initFromEvent(event) ;
	}

	@SuppressWarnings("unused")
	private SaveNewEventResult() 
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
	
	public Event getEvent() {
		return _event ;
	}
}
