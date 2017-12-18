package com.ldv.shared.rpc4caldav;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.calendar.Event;

import net.customware.gwt.dispatch.shared.Result;

public class GetPlannedEventsResult implements Result 
{
	private boolean       _bSuccess ;
	private Vector<Event> _aEvents = new Vector<Event>() ;
	private String        _sMessage ;
	
	public GetPlannedEventsResult(final boolean bSuccess, final Vector<Event> aEvents, final String message) 
	{
		_bSuccess = bSuccess ;
		_sMessage = message ;
		
		if ((null != aEvents) && (false == aEvents.isEmpty()))
			initFromEvents(aEvents) ;
	}

	@SuppressWarnings("unused")
	private GetPlannedEventsResult() 
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
	
	public Vector<Event> getEvents() {
		return _aEvents ;
	}
	public void addEvent(final Event event)
	{
		if (null == event)
			return ;
		_aEvents.add(new Event(event)) ;
	}
	public void initFromEvents(Vector<Event> aEvents)
	{
		_aEvents.clear() ;
		
		if ((null == aEvents) || aEvents.isEmpty())
			return ;
		
		for (Iterator<Event> it = aEvents.iterator() ; it.hasNext() ; )
			_aEvents.add(new Event(it.next())) ;
	}
}
