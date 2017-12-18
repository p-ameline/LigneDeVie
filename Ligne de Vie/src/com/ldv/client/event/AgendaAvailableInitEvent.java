package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

import com.ldv.client.mvp_toons_agenda.AgendaAvailablePresenter;
import com.ldv.shared.calendar.Available;

/** 
 * Event sent to an AgendaDayPresenter to ask it to initialize itself inside a given panel, for a given day, and 
 * under the control of a given time manager
 * 
 */
public class AgendaAvailableInitEvent extends GwtEvent<AgendaAvailableInitEventHandler> 
{	
	public static Type<AgendaAvailableInitEventHandler> TYPE = new Type<AgendaAvailableInitEventHandler>() ;
	
	private Panel                    _hostPanel ;
	private AgendaAvailablePresenter _presenter ;
	private Available                _available ;
	private String                   _sAvailabilityUid ;
	
	public static Type<AgendaAvailableInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaAvailableInitEventHandler>() ;
		return TYPE ;
	}
	
	public AgendaAvailableInitEvent(final Panel hostPanel, final AgendaAvailablePresenter presenter, final Available available, final String sAvailabilityUid) 
	{
		_hostPanel        = hostPanel ;
		_presenter        = presenter ;
		_available        = available ;
		_sAvailabilityUid = sAvailabilityUid ;
	}
	
	public Panel getHostPanel() {
		return _hostPanel ;
	}
	
	public AgendaAvailablePresenter getPresenter() {
		return _presenter ;
	}
	
	public Available getAvailable() {
		return _available ;
	}
	
	public String getAvailabilityUid() {
		return _sAvailabilityUid ;
	}
	
	@Override
	protected void dispatch(AgendaAvailableInitEventHandler handler) {
		handler.onInitAgendaAvailable(this) ;
	}

	@Override
	public Type<AgendaAvailableInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
