package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Panel;

public class LdvValidatorSentEvent extends GwtEvent<LdvValidatorSentEventHandler> 
{	
	public static Type<LdvValidatorSentEventHandler> TYPE = new Type<LdvValidatorSentEventHandler>();
	
	private Panel  _workspace ;
	private String _sId ;
	
	public static Type<LdvValidatorSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvValidatorSentEventHandler>();
		return TYPE;
	}
	
	public LdvValidatorSentEvent(Panel workspace, String sId){
		_workspace = workspace ;
		_sId       = sId ;
	}
	
	public Panel getWorkspace(){
		return _workspace ;
	}
	
	public String getId(){
		return _sId ;
	}
	
	@Override
	protected void dispatch(LdvValidatorSentEventHandler handler) {
		handler.onValidatorSend(this);
	}

	@Override
	public Type<LdvValidatorSentEventHandler> getAssociatedType() {
		return TYPE;
	}
}
