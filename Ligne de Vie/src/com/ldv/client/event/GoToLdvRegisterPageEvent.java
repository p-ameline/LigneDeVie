package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GoToLdvRegisterPageEvent extends GwtEvent<GoToLdvRegisterPageEventHandler> {
	
	public static Type<GoToLdvRegisterPageEventHandler> TYPE = new Type<GoToLdvRegisterPageEventHandler>();
	
	public static Type<GoToLdvRegisterPageEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<GoToLdvRegisterPageEventHandler>();
		return TYPE;
	}
	
	public GoToLdvRegisterPageEvent(){	
	}
		
	@Override
	protected void dispatch(GoToLdvRegisterPageEventHandler handler) {
		handler.onGoToRegister(this) ;
	}

	@Override
	public Type<GoToLdvRegisterPageEventHandler> getAssociatedType() {
		return TYPE;
	}
}
