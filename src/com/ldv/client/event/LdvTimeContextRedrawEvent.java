package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp_toons.LdvTimeContextPresenter;
import com.ldv.shared.model.LdvTime;

public class LdvTimeContextRedrawEvent extends GwtEvent<LdvTimeContextRedrawEventHandler> {
	
	public static Type<LdvTimeContextRedrawEventHandler> TYPE = new Type<LdvTimeContextRedrawEventHandler>();
	
	private LdvTime                 _leftLimit = null ;
	private LdvTimeContextPresenter _target ;
	
	public static Type<LdvTimeContextRedrawEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeContextRedrawEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeContextRedrawEvent(final LdvTime leftLimit, final LdvTimeContextPresenter target)
	{
		_leftLimit = new LdvTime(0) ;
		_leftLimit.initFromLdvTime(leftLimit) ;
		
		_target = target ;
	}
	
	public LdvTime getLeftLimit(){
		return _leftLimit ;
	}
	
	public LdvTimeContextPresenter getTarget() {
		return _target ;
	}
	
	@Override
	protected void dispatch(LdvTimeContextRedrawEventHandler handler) {
		handler.onRedrawTimeContext(this) ;
	}

	@Override
	public Type<LdvTimeContextRedrawEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
