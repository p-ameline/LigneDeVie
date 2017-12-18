package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp_toons.LdvTimeContextPresenter;
import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;

public class LdvTimeContextSentEvent extends GwtEvent<LdvTimeContextSentEventHandler> 
{	
	public static Type<LdvTimeContextSentEventHandler> TYPE = new Type<LdvTimeContextSentEventHandler>() ;
	
	private LdvTimeDisplayPresenter _fatherTimeDisplay ;
	private LdvTimeContextPresenter _target ;
	
	public static Type<LdvTimeContextSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeContextSentEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeContextSentEvent(final LdvTimeDisplayPresenter fatherTimeDisplay, final LdvTimeContextPresenter target)
	{
		_fatherTimeDisplay = fatherTimeDisplay ;
		_target            = target ;
	}
	
	public LdvTimeDisplayPresenter getFatherTimeDisplay() {
		return _fatherTimeDisplay ;
	}
	
	public LdvTimeContextPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeContextSentEventHandler handler) {
		handler.onTimeContextSend(this) ;
	}

	@Override
	public Type<LdvTimeContextSentEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
