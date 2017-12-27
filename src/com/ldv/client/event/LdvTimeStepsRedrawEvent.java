package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp_toons.LdvTimeStepsPresenter;
import com.ldv.shared.model.LdvTime;

public class LdvTimeStepsRedrawEvent extends GwtEvent<LdvTimeStepsRedrawEventHandler> {
	
	public static Type<LdvTimeStepsRedrawEventHandler> TYPE = new Type<LdvTimeStepsRedrawEventHandler>();
	
	private LdvTime               _leftLimit ;
	private LdvTimeStepsPresenter _target ;
	
	public static Type<LdvTimeStepsRedrawEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeStepsRedrawEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeStepsRedrawEvent(LdvTime leftLimit, LdvTimeStepsPresenter target)
	{
		_leftLimit = new LdvTime(0) ;
		_leftLimit.initFromLdvTime(leftLimit) ;
		
		_target = target ;
	}
	
	public LdvTime getLeftLimit(){
		return _leftLimit ;
	}
	
	public LdvTimeStepsPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeStepsRedrawEventHandler handler) {
		handler.onRedrawTimeSteps(this) ;
	}

	@Override
	public Type<LdvTimeStepsRedrawEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
