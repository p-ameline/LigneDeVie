package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.shared.calendar.Availability;

/**
 * Event that signal an AgendaDayPresenter that it must redraw its view 
 */
public class AgendaRedrawAvailabilityEvent extends GwtEvent<AgendaRedrawAvailabilityEventHandler> 
{	
	public static Type<AgendaRedrawAvailabilityEventHandler> TYPE = new Type<AgendaRedrawAvailabilityEventHandler>() ;

	private Availability _availability ;
	
	public static Type<AgendaRedrawAvailabilityEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaRedrawAvailabilityEventHandler>() ;
		return TYPE;
	}

	public AgendaRedrawAvailabilityEvent(Availability availability) {
		_availability = availability ;
	}

	public Availability getAvailability() {
		return _availability ;
	}
	
	@Override
	protected void dispatch(AgendaRedrawAvailabilityEventHandler handler) {
		handler.onRedrawAgendaAvailability(this) ;
	}

	@Override
	public Type<AgendaRedrawAvailabilityEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
