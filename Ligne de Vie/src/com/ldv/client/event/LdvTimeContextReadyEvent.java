package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;

public class LdvTimeContextReadyEvent extends GwtEvent<LdvTimeContextReadyEventHandler> 
{	
	public static Type<LdvTimeContextReadyEventHandler> TYPE = new Type<LdvTimeContextReadyEventHandler>() ;
	
	public static Type<LdvTimeContextReadyEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeContextReadyEventHandler>() ;
		return TYPE ;
	}
	
	protected LdvTimeDisplayPresenter _target ;
	
	public LdvTimeContextReadyEvent(LdvTimeDisplayPresenter target) {
		_target = target ;
	}
	
	public LdvTimeDisplayPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeContextReadyEventHandler handler) {
		handler.onTimeContextReady(this) ;
	}

	@Override
	public Type<LdvTimeContextReadyEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
