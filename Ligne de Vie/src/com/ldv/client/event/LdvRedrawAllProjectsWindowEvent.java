package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LdvRedrawAllProjectsWindowEvent extends GwtEvent<LdvRedrawAllProjectsWindowEventHandler> 
{	
	public static Type<LdvRedrawAllProjectsWindowEventHandler> TYPE = new Type<LdvRedrawAllProjectsWindowEventHandler>();
	
	public static Type<LdvRedrawAllProjectsWindowEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvRedrawAllProjectsWindowEventHandler>();
		return TYPE;
	}
	
	public LdvRedrawAllProjectsWindowEvent(){
	}
		
	@Override
	protected void dispatch(LdvRedrawAllProjectsWindowEventHandler handler) {
		handler.onRedrawAllProjectsWindowSend(this);
	}

	@Override
	public Type<LdvRedrawAllProjectsWindowEventHandler> getAssociatedType() {
		return TYPE;
	}
}
