package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

public class LdvRegisterSentEvent extends GwtEvent<LdvRegisterSentEventHandler> 
{	
	public static Type<LdvRegisterSentEventHandler> TYPE = new Type<LdvRegisterSentEventHandler>();
	
	private Panel _workspace ;
	
	public static Type<LdvRegisterSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvRegisterSentEventHandler>();
		return TYPE;
	}
	
	public LdvRegisterSentEvent(Panel workspace){
		_workspace = workspace ;
	}
	
	public Panel getWorkspace(){
		return _workspace ;
	}
	
	@Override
	protected void dispatch(LdvRegisterSentEventHandler handler) {
		handler.onRegisterSend(this);
	}

	@Override
	public Type<LdvRegisterSentEventHandler> getAssociatedType() {
		return TYPE;
	}
}
