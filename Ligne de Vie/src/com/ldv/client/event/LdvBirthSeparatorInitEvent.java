package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.mvp.LdvProjectWindowPresenter;
import com.ldv.client.mvp_toons.LdvBirthSeparatorPresenter;

public class LdvBirthSeparatorInitEvent extends GwtEvent<LdvBirthSeparatorInitEventHandler> 
{	
	public static Type<LdvBirthSeparatorInitEventHandler> TYPE = new Type<LdvBirthSeparatorInitEventHandler>();
	
	private Panel                      _project ;
	private int                        _iX ;
	private LdvBirthSeparatorPresenter _target ;
	private LdvProjectWindowPresenter  _father ;
	
	public static Type<LdvBirthSeparatorInitEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvBirthSeparatorInitEventHandler>();
		return TYPE;
	}
	
	public LdvBirthSeparatorInitEvent(Panel project, LdvBirthSeparatorPresenter target, int iX, LdvProjectWindowPresenter father){
		_project = project ;
		_target  = target ;
		_father  = father ;
		_iX      = iX ;
	}
	
	public Panel getProject(){
		return _project ;
	}
	
	public LdvBirthSeparatorPresenter getTarget(){
		return _target ;
	}
	
	public LdvProjectWindowPresenter getFather(){
		return _father ;
	}
	
	public int getX(){
		return _iX ;
	}
	
	@Override
	protected void dispatch(LdvBirthSeparatorInitEventHandler handler) {
		handler.onInitSend(this);
	}

	@Override
	public Type<LdvBirthSeparatorInitEventHandler> getAssociatedType() {
		return TYPE;
	}
}
