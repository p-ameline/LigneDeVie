package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

/** 
 * Event sent to an AgendaTimeDisplayPresenter to ask it to initialize itself inside a given panel, for a given day, and 
 * under the control of a given time manager
 * 
 */
public class AgendaDayControllerInitEvent extends GwtEvent<AgendaDayControllerInitEventHandler> 
{	
	public static Type<AgendaDayControllerInitEventHandler> TYPE = new Type<AgendaDayControllerInitEventHandler>();
	
	private Panel _hostPanel ;
	
	public static Type<AgendaDayControllerInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaDayControllerInitEventHandler>();
		return TYPE;
	}
	
	public AgendaDayControllerInitEvent(final Panel hostPanel) {
		_hostPanel   = hostPanel ;
	}
	
	public Panel getHostPanel() {
		return _hostPanel ;
	}
		
	@Override
	protected void dispatch(AgendaDayControllerInitEventHandler handler) {
		handler.onInitAgendaDayController(this) ;
	}

	@Override
	public Type<AgendaDayControllerInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
