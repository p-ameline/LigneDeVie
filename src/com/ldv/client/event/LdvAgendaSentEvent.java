package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

public class LdvAgendaSentEvent extends GwtEvent<LdvAgendaSentEventHandler> 
{	
	public static Type<LdvAgendaSentEventHandler> TYPE = new Type<LdvAgendaSentEventHandler>();
	
	private Panel _workspace ;
	
	public static Type<LdvAgendaSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvAgendaSentEventHandler>();
		return TYPE;
	}
	
	public LdvAgendaSentEvent(Panel workspace){
		_workspace    = workspace ;
	}
	
	public Panel getWorkspace(){
		return _workspace ;
	}
	
	@Override
	protected void dispatch(LdvAgendaSentEventHandler handler) {
		handler.onAgendaSent(this);
	}

	@Override
	public Type<LdvAgendaSentEventHandler> getAssociatedType() {
		return TYPE;
	}
}
