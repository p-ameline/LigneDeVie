package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

import com.ldv.shared.calendar.Available;

/**
 * Event that signal an AgendaDayPresenter that it must redraw its view 
 */
public class AgendaRedrawAvailableEvent extends GwtEvent<AgendaRedrawAvailableEventHandler> 
{	
	public static Type<AgendaRedrawAvailableEventHandler> TYPE = new Type<AgendaRedrawAvailableEventHandler>() ;

	private Available _available ;
	
	public static Type<AgendaRedrawAvailableEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaRedrawAvailableEventHandler>() ;
		return TYPE;
	}

	public AgendaRedrawAvailableEvent(Available available) {
		_available = available ;
	}

	public Available getAvailable() {
		return _available ;
	}
	
	@Override
	protected void dispatch(AgendaRedrawAvailableEventHandler handler) {
		handler.onRedrawAgendaAvailable(this) ;
	}

	@Override
	public Type<AgendaRedrawAvailableEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
