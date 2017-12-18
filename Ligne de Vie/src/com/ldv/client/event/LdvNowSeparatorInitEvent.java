package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp_toons.LdvNowSeparatorPresenter;

public class LdvNowSeparatorInitEvent extends GwtEvent<LdvNowSeparatorInitEventHandler> 
{	
	public static Type<LdvNowSeparatorInitEventHandler> TYPE = new Type<LdvNowSeparatorInitEventHandler>();
	
	private Panel                     _project ;
	private int                       _iX ;
	private LdvNowSeparatorPresenter  _target ;
	private LdvProjectWindowPresenter _father ;
	
	public static Type<LdvNowSeparatorInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvNowSeparatorInitEventHandler>();
		return TYPE;
	}
	
	public LdvNowSeparatorInitEvent(Panel project, LdvNowSeparatorPresenter target, int iX, LdvProjectWindowPresenter father){
		_project = project ;
		_target  = target ;
		_father  = father ;
		_iX      = iX ;
	}
	
	public Panel getProject(){
		return _project ;
	}
	
	public LdvNowSeparatorPresenter getTarget(){
		return _target ;
	}
	
	public LdvProjectWindowPresenter getFather(){
		return _father ;
	}
	
	public int getX(){
		return _iX ;
	}
	
	@Override
	protected void dispatch(LdvNowSeparatorInitEventHandler handler) {
		handler.onInitSend(this);
	}

	@Override
	public Type<LdvNowSeparatorInitEventHandler> getAssociatedType() {
		return TYPE;
	}
}
