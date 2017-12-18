package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

import com.ldv.shared.calendar.Available;

/** 
 * Event sent to an AgendaDayPresenter to ask it to initialize itself inside a given panel, for a given day, and 
 * under the control of a given time manager
 * 
 */
public class AgendaAvailableEditEvent extends GwtEvent<AgendaAvailableEditEventHandler> 
{	
	public static Type<AgendaAvailableEditEventHandler> TYPE = new Type<AgendaAvailableEditEventHandler>() ;
	
	private Available _available ;
	private String    _sAvailabilityUid ;
	
	public static Type<AgendaAvailableEditEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaAvailableEditEventHandler>() ;
		return TYPE ;
	}
	
	public AgendaAvailableEditEvent(final Available available, final String sAvailabilityUid) 
	{
		_available        = available ;
		_sAvailabilityUid = sAvailabilityUid ;
	}
		
	public Available getAvailable() {
		return _available ;
	}
	
	public String getAvailabilityUid() {
		return _sAvailabilityUid ;
	}
	
	@Override
	protected void dispatch(AgendaAvailableEditEventHandler handler) {
		handler.onEditAgendaAvailable(this) ;
	}

	@Override
	public Type<AgendaAvailableEditEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
