package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

import com.ldv.shared.calendar.Availability;

/** 
 * Event sent to an AgendaDayPresenter to ask it to initialize itself inside a given panel, for a given day, and 
 * under the control of a given time manager
 * 
 */
public class AgendaAvailabilityDeleteEvent extends GwtEvent<AgendaAvailabilityDeleteEventHandler> 
{	
	public static Type<AgendaAvailabilityDeleteEventHandler> TYPE = new Type<AgendaAvailabilityDeleteEventHandler>() ;
	
	private Availability _availability ;
	
	public static Type<AgendaAvailabilityDeleteEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaAvailabilityDeleteEventHandler>() ;
		return TYPE ;
	}
	
	public AgendaAvailabilityDeleteEvent(final Availability availability) {
		_availability = availability ;
	}
	
	public Availability getAvailability() {
		return _availability ;
	}
	
	@Override
	protected void dispatch(AgendaAvailabilityDeleteEventHandler handler) {
		handler.onDeleteAgendaAvailability(this) ;
	}

	@Override
	public Type<AgendaAvailabilityDeleteEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
