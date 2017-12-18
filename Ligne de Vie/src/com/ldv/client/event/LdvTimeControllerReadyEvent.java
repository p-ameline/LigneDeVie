package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LdvTimeControllerReadyEvent extends GwtEvent<LdvTimeControllerReadyEventHandler> 
{	
	public static Type<LdvTimeControllerReadyEventHandler> TYPE = new Type<LdvTimeControllerReadyEventHandler>() ;
	
	public static Type<LdvTimeControllerReadyEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeControllerReadyEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeControllerReadyEvent()
	{
	}
		
	@Override
	protected void dispatch(LdvTimeControllerReadyEventHandler handler) {
		handler.onTimeControllerReady(this) ;
	}

	@Override
	public Type<LdvTimeControllerReadyEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
