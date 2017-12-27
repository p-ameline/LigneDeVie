package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class BackToWelcomePageEvent extends GwtEvent<BackToWelcomePageEventHandler> 
{	
	public static Type<BackToWelcomePageEventHandler> TYPE = new Type<BackToWelcomePageEventHandler>() ;
	
	public static Type<BackToWelcomePageEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<BackToWelcomePageEventHandler>() ;
		return TYPE ;
	}
	
	public BackToWelcomePageEvent(){	
	}
		
	@Override
	protected void dispatch(BackToWelcomePageEventHandler handler) {
		handler.onBackToWelcome(this) ;
	}

	@Override
	public Type<BackToWelcomePageEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
