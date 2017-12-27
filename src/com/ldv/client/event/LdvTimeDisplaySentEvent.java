package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;
import com.ldv.client.util.LdvTimeZoomLevel;
import com.ldv.shared.model.LdvTime;

public class LdvTimeDisplaySentEvent extends GwtEvent<LdvTimeDisplaySentEventHandler> 
{	
	public static Type<LdvTimeDisplaySentEventHandler> TYPE = new Type<LdvTimeDisplaySentEventHandler>() ;
	
	private FlowPanel        _workspace ;
	private LdvTime          _topRightTime ;
	private LdvTimeZoomLevel _currentZoomLevel ;
	
	private LdvTimeDisplayPresenter _target ;
	
	public static Type<LdvTimeDisplaySentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeDisplaySentEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeDisplaySentEvent(FlowPanel workspace, LdvTime topRightTime, LdvTimeZoomLevel currentZoomLevel, LdvTimeDisplayPresenter target)
	{
		_workspace        = workspace ;
		_topRightTime     = topRightTime ;
		_currentZoomLevel = currentZoomLevel ;
		_target           = target ;
	}
	
	public FlowPanel getWorkspace() {
		return _workspace ;
	}
	
	public LdvTime getTopRightTime() {
		return _topRightTime ;
	}
	
	public LdvTimeZoomLevel getCurrentZoomLevel() {
		return _currentZoomLevel ;
	}
	
	public LdvTimeDisplayPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeDisplaySentEventHandler handler) {
		handler.onTimeDisplaySend(this) ;
	}

	@Override
	public Type<LdvTimeDisplaySentEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
