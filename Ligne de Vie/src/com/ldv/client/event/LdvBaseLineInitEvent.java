package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp_toons.LdvBaseLinePresenter;

public class LdvBaseLineInitEvent extends GwtEvent<LdvBaseLineInitEventHandler> 
{	
	public static Type<LdvBaseLineInitEventHandler> TYPE = new Type<LdvBaseLineInitEventHandler>();
	
	private Panel                     _baseLinePanel ;
	private LdvBaseLinePresenter      _target ;
	private LdvProjectWindowPresenter _father ;
	
	public static Type<LdvBaseLineInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvBaseLineInitEventHandler>() ;
		return TYPE ;
	}
	
	public LdvBaseLineInitEvent(Panel baseLinePanel, LdvBaseLinePresenter target, LdvProjectWindowPresenter father)
	{
		_baseLinePanel = baseLinePanel ;
		_target        = target ;
		_father        = father ;
	}
	
	public Panel getBaseLinePanel() {
		return _baseLinePanel ;
	}
	
	public LdvBaseLinePresenter getTarget() {
		return _target ;
	}
	
	public LdvProjectWindowPresenter getFather() {
		return _father ;
	}
	
	@Override
	protected void dispatch(LdvBaseLineInitEventHandler handler) {
		handler.onInitSend(this) ;
	}

	@Override
	public Type<LdvBaseLineInitEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
