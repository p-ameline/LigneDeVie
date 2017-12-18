package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

import com.ldv.client.mvp_toons_agenda.AgendaDaySummaryPresenter;
import com.ldv.client.mvp_toons_agenda.AgendaTimeDisplayPresenter;
import com.ldv.shared.model.LdvTime;

/** 
 * Event sent to an AgendaDayPresenter to ask it to initialize itself inside a given panel, for a given day, and 
 * under the control of a given time manager
 * 
 */
public class AgendaDaySummaryInitEvent extends GwtEvent<AgendaDaySummaryInitEventHandler> 
{	
	public static Type<AgendaDaySummaryInitEventHandler> TYPE = new Type<AgendaDaySummaryInitEventHandler>();
	
	private AgendaDaySummaryPresenter  _target ;
	
	private Panel                      _hostPanel ;
	private LdvTime                    _day ;
	private AgendaTimeDisplayPresenter _timeManager ;
	
	public static Type<AgendaDaySummaryInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaDaySummaryInitEventHandler>();
		return TYPE;
	}
	
	public AgendaDaySummaryInitEvent(final AgendaDaySummaryPresenter target, final Panel hostPanel, final LdvTime day, final AgendaTimeDisplayPresenter timeManager)
	{
		_target      = target ;
		_hostPanel   = hostPanel ;
		_day         = day ;
		_timeManager = timeManager ;
	}
	
	public AgendaDaySummaryPresenter getTarget() {
		return _target ;
	}
	
	public Panel getHostPanel() {
		return _hostPanel ;
	}
	
	public LdvTime getDay() {
		return _day ;
	}
	
	public AgendaTimeDisplayPresenter getTimeManager() {
		return _timeManager ;
	}
		
	@Override
	protected void dispatch(AgendaDaySummaryInitEventHandler handler) {
		handler.onInitAgendaDay(this) ;
	}

	@Override
	public Type<AgendaDaySummaryInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
