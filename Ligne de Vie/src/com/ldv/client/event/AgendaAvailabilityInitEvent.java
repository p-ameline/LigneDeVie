package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.mvp_toons_agenda.AgendaAvailabilityPresenter;
import com.ldv.shared.calendar.Availability;

/** 
 * Event sent to an AgendaDayPresenter to ask it to initialize itself inside a given panel, for a given day, and 
 * under the control of a given time manager
 * 
 */
public class AgendaAvailabilityInitEvent extends GwtEvent<AgendaAvailabilityInitEventHandler> 
{	
	public static Type<AgendaAvailabilityInitEventHandler> TYPE = new Type<AgendaAvailabilityInitEventHandler>() ;
	
	private Panel                       _hostPanel ;
	private AgendaAvailabilityPresenter _presenter ;
	private Availability                _availability ;
	
	public static Type<AgendaAvailabilityInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaAvailabilityInitEventHandler>() ;
		return TYPE ;
	}
	
	public AgendaAvailabilityInitEvent(final Panel hostPanel, final AgendaAvailabilityPresenter presenter, final Availability availability) 
	{
		_hostPanel    = hostPanel ;
		_presenter    = presenter ;
		_availability = availability ;
	}
	
	public Panel getHostPanel() {
		return _hostPanel ;
	}
	
	public AgendaAvailabilityPresenter getPresenter() {
		return _presenter ;
	}
	
	public Availability getAvailability() {
		return _availability ;
	}
	
	@Override
	protected void dispatch(AgendaAvailabilityInitEventHandler handler) {
		handler.onInitAgendaAvailability(this) ;
	}

	@Override
	public Type<AgendaAvailabilityInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
