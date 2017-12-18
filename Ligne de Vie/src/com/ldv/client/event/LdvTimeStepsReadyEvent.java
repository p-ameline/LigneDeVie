package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp_toons.LdvTimeDisplayPresenter;

public class LdvTimeStepsReadyEvent extends GwtEvent<LdvTimeStepsReadyEventHandler> 
{	
	public static Type<LdvTimeStepsReadyEventHandler> TYPE = new Type<LdvTimeStepsReadyEventHandler>() ;
	
	public static Type<LdvTimeStepsReadyEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeStepsReadyEventHandler>() ;
		return TYPE ;
	}
	
	private LdvTimeDisplayPresenter _target ;
	
	public LdvTimeStepsReadyEvent(final LdvTimeDisplayPresenter target) {
		_target = target ;
	}
	
	public LdvTimeDisplayPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeStepsReadyEventHandler handler) {
		handler.onTimeStepsReady(this) ;
	}

	@Override
	public Type<LdvTimeStepsReadyEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
