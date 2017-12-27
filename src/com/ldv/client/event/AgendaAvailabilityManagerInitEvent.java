package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

/** 
 * Event sent to an AgendaAvailabilityManagementPresenter to ask it to install itself as an Availability components workspace
 * 
 */
public class AgendaAvailabilityManagerInitEvent extends GwtEvent<AgendaAvailabilityManagerInitEventHandler> 
{	
	public static Type<AgendaAvailabilityManagerInitEventHandler> TYPE = new Type<AgendaAvailabilityManagerInitEventHandler>() ;
	
	private Panel _hostPanel ;
	
	public static Type<AgendaAvailabilityManagerInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaAvailabilityManagerInitEventHandler>() ;
		return TYPE ;
	}
	
	public AgendaAvailabilityManagerInitEvent(final Panel hostPanel) {
		_hostPanel   = hostPanel ;
	}
	
	public Panel getHostPanel() {
		return _hostPanel ;
	}
	
	@Override
	protected void dispatch(AgendaAvailabilityManagerInitEventHandler handler) {
		handler.onInitAgendaAvailabilityManager(this) ;
	}

	@Override
	public Type<AgendaAvailabilityManagerInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
