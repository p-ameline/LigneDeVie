package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GoToLdvMainEvent extends GwtEvent<GoToLdvMainEventHandler> 
{	
	public static Type<GoToLdvMainEventHandler> TYPE = new Type<GoToLdvMainEventHandler>() ;
	
	public static Type<GoToLdvMainEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<GoToLdvMainEventHandler>() ;
		return TYPE ;
	}
	
	public GoToLdvMainEvent() {	
	}
			
	@Override
	protected void dispatch(GoToLdvMainEventHandler handler) {
		handler.onGoToMain(this) ;
	}

	@Override
	public Type<GoToLdvMainEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
