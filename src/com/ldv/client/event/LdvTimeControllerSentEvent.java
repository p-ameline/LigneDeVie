package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.ldv.client.mvp_toons.LdvTimeControllerPresenter;
import com.ldv.client.util.LdvTimeZoomLevel;
import com.ldv.shared.model.LdvTime;

public class LdvTimeControllerSentEvent extends GwtEvent<LdvTimeControllerSentEventHandler> 
{	
	public static Type<LdvTimeControllerSentEventHandler> TYPE = new Type<LdvTimeControllerSentEventHandler>() ;
	
	private AbsolutePanel    _workspace ;
	private LdvTime          _topRightTime ;
	private LdvTimeZoomLevel _currentZoomLevel ;
	private Button           _rightScrollButton ;
	private Button           _leftScrollButton ;

	private LdvTimeControllerPresenter _target ;
	
	public static Type<LdvTimeControllerSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeControllerSentEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeControllerSentEvent(AbsolutePanel workspace,
			                              LdvTime topRightTime,
			                              LdvTimeZoomLevel currentZoomLevel,
			                              Button rightScrollButton,
			                              Button leftScrollButton,
			                              final LdvTimeControllerPresenter target)
	{
		_workspace         = workspace ;
		_topRightTime      = topRightTime ;
		_currentZoomLevel  = currentZoomLevel ;
		_rightScrollButton = rightScrollButton ;
		_leftScrollButton  = leftScrollButton ;
		
		_target            = target ;
	}
	
	public AbsolutePanel getWorkspace() {
		return _workspace ;
	}
	
	public LdvTime getTopRightTime() {
		return _topRightTime ;
	}
	
	public LdvTimeZoomLevel getCurrentZoomLevel() {
		return _currentZoomLevel ;
	}
	
	public Button getRightScrollButton() {
		return _rightScrollButton ;
	}
	
	public Button getLeftScrollButton() {
		return _leftScrollButton ;
	}
	
	public LdvTimeControllerPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeControllerSentEventHandler handler) {
		handler.onTimeControllerSend(this) ;
	}

	@Override
	public Type<LdvTimeControllerSentEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
