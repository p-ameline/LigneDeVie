package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GoToLdvManagerEvent extends GwtEvent<GoToLdvManagerEventHandler> 
{	
	public static Type<GoToLdvManagerEventHandler> TYPE = new Type<GoToLdvManagerEventHandler>();
	
	public static Type<GoToLdvManagerEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<GoToLdvManagerEventHandler>() ;
		return TYPE;
	}
	
	public GoToLdvManagerEvent(){	
	}
		
	@Override
	protected void dispatch(GoToLdvManagerEventHandler handler) {
		handler.onGoToMain(this) ;
	}

	@Override
	public Type<GoToLdvManagerEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
