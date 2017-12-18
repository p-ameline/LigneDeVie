package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;
import com.ldv.client.util.LdvGraphManager;

public class LdvMainSentEvent extends GwtEvent<LdvMainSentEventHandler> 
{	
	public static Type<LdvMainSentEventHandler> TYPE = new Type<LdvMainSentEventHandler>();
	
	private Panel _workspace ;
	
	public static Type<LdvMainSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvMainSentEventHandler>();
		return TYPE;
	}
	
	public LdvMainSentEvent(Panel workspace){
		_workspace    = workspace ;
	}
	
	public Panel getWorkspace(){
		return _workspace ;
	}
	
	@Override
	protected void dispatch(LdvMainSentEventHandler handler) {
		handler.onMainSend(this);
	}

	@Override
	public Type<LdvMainSentEventHandler> getAssociatedType() {
		return TYPE;
	}
}
