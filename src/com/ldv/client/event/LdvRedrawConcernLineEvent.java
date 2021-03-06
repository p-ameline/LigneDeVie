package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp_toons.LdvConcernLinePresenter;

public class LdvRedrawConcernLineEvent extends GwtEvent<LdvRedrawConcernLineEventHandler> 
{	
	public static Type<LdvRedrawConcernLineEventHandler> TYPE = new Type<LdvRedrawConcernLineEventHandler>();
	
	private LdvProjectWindowPresenter   _father ;
	private LdvConcernLinePresenter 		_target;
	
	public static Type<LdvRedrawConcernLineEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvRedrawConcernLineEventHandler>();
		return TYPE;
	}

	public LdvRedrawConcernLineEvent(LdvProjectWindowPresenter father, LdvConcernLinePresenter target){
		_father = father ;
		_target = target ;
	}

	public LdvConcernLinePresenter getTarget(){
		return _target ;
	}
	
	public LdvProjectWindowPresenter getFather(){
		return _father ;
	}
	
	@Override
	protected void dispatch(LdvRedrawConcernLineEventHandler handler) {
		handler.onRedrawConcernLineSend(this);
	}

	@Override
	public Type<LdvRedrawConcernLineEventHandler> getAssociatedType() {
		return TYPE;
	}
}
