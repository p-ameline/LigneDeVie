package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.FlowPanel;

public class LdvWelcomePageEvent extends GwtEvent<LdvWelcomePageEventHandler> {
	
	public static Type<LdvWelcomePageEventHandler> TYPE = new Type<LdvWelcomePageEventHandler>();
	
	private FlowPanel workspace;
	
	public static Type<LdvWelcomePageEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvWelcomePageEventHandler>();
		return TYPE;
	}
	
	public LdvWelcomePageEvent(FlowPanel flowPanel){
		this.workspace = flowPanel;		
	}
	
	public FlowPanel getWorkspace(){
		return workspace ;
	}

/*	public VerticalPanel getBody(){
		return body ;
	}
	*/
	
	@Override
	protected void dispatch(LdvWelcomePageEventHandler handler) {
		handler.onWelcome(this);
	}

	@Override
	public Type<LdvWelcomePageEventHandler> getAssociatedType() {
		return TYPE;
	}

}
