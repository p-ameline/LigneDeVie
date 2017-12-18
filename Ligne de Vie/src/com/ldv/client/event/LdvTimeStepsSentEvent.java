package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;
import com.ldv.client.mvp_toons.LdvTimeStepsPresenter;

public class LdvTimeStepsSentEvent extends GwtEvent<LdvTimeStepsSentEventHandler> 
{	
	public static Type<LdvTimeStepsSentEventHandler> TYPE = new Type<LdvTimeStepsSentEventHandler>() ;
	
	private LdvTimeDisplayPresenter _fatherTimeDisplay ;
	private LdvTimeStepsPresenter   _target ;
	
	public static Type<LdvTimeStepsSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeStepsSentEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeStepsSentEvent(final LdvTimeDisplayPresenter fatherTimeDisplay, final LdvTimeStepsPresenter target)
	{
		_fatherTimeDisplay = fatherTimeDisplay ;
		_target            = target ;
	}
	
	public LdvTimeDisplayPresenter getFatherTimeDisplay() {
		return _fatherTimeDisplay ;
	}
	
	public LdvTimeStepsPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeStepsSentEventHandler handler) {
		handler.onTimeStepsSend(this) ;
	}

	@Override
	public Type<LdvTimeStepsSentEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
