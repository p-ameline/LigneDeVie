package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that signal an AgendaDayPresenter that it must redraw its view 
 */
public class AgendaRedrawAvailabilityManagerEvent extends GwtEvent<AgendaRedrawAvailabilityManagerEventHandler> 
{	
	public static Type<AgendaRedrawAvailabilityManagerEventHandler> TYPE = new Type<AgendaRedrawAvailabilityManagerEventHandler>() ;

	public static Type<AgendaRedrawAvailabilityManagerEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaRedrawAvailabilityManagerEventHandler>() ;
		return TYPE;
	}

	public AgendaRedrawAvailabilityManagerEvent() {
	}

	@Override
	protected void dispatch(AgendaRedrawAvailabilityManagerEventHandler handler) {
		handler.onRedrawAgendaAvailabilityManager(this) ;
	}

	@Override
	public Type<AgendaRedrawAvailabilityManagerEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
