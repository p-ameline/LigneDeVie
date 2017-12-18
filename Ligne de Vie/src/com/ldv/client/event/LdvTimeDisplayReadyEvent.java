package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LdvTimeDisplayReadyEvent extends GwtEvent<LdvTimeDisplayReadyEventHandler> 
{	
	public static Type<LdvTimeDisplayReadyEventHandler> TYPE = new Type<LdvTimeDisplayReadyEventHandler>() ;
	
	public static Type<LdvTimeDisplayReadyEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvTimeDisplayReadyEventHandler>() ;
		return TYPE ;
	}
	
	public LdvTimeDisplayReadyEvent()
	{
	}
		
	@Override
	protected void dispatch(LdvTimeDisplayReadyEventHandler handler) {
		handler.onTimeDisplayReady(this) ;
	}

	@Override
	public Type<LdvTimeDisplayReadyEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
