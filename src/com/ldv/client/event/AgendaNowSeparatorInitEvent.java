package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

import com.ldv.client.mvp_toons_agenda.AgendaNowSeparatorPresenter;

public class AgendaNowSeparatorInitEvent extends GwtEvent<AgendaNowSeparatorInitEventHandler> 
{	
	public static Type<AgendaNowSeparatorInitEventHandler> TYPE = new Type<AgendaNowSeparatorInitEventHandler>();
	
	private Panel                       _project ;
	private int                         _iX ;
	private AgendaNowSeparatorPresenter _target ;
	
	public static Type<AgendaNowSeparatorInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaNowSeparatorInitEventHandler>();
		return TYPE;
	}
	
	public AgendaNowSeparatorInitEvent(Panel project, AgendaNowSeparatorPresenter target, int iX)
	{
		_project = project ;
		_target  = target ;
		_iX      = iX ;
	}
	
	public Panel getProject(){
		return _project ;
	}
	
	public AgendaNowSeparatorPresenter getTarget(){
		return _target ;
	}
	
	public int getX(){
		return _iX ;
	}
	
	@Override
	protected void dispatch(AgendaNowSeparatorInitEventHandler handler) {
		handler.onInitSend(this);
	}

	@Override
	public Type<AgendaNowSeparatorInitEventHandler> getAssociatedType() {
		return TYPE;
	}
}
