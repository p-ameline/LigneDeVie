package com.ldv.client.event ;

import com.google.gwt.event.shared.GwtEvent;

public class LdvOpenEvent extends GwtEvent<LdvOpenEventHandler>
{
	public static Type<LdvOpenEventHandler> TYPE = new Type<LdvOpenEventHandler>() ;
	
	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<LdvOpenEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvOpenEventHandler>();
		return TYPE;
	}
	
	private final String _sessionId ;
	private final String _message ;
	
	public LdvOpenEvent(final String sessionId, final String message) 
	{
		this._sessionId = sessionId ;
		this._message   = message;
	}
	
	public String getSessionId() 
	{
		return _sessionId ;
	}
	
	public String getMessage() 
	{
		return _message ;
	}
	
	@Override
	public Type<LdvOpenEventHandler> getAssociatedType() 
	{
		return TYPE ;
	}

	@Override
	protected void dispatch(final LdvOpenEventHandler handler) 
	{
		// When activated, will get intercepted by whatever object whose event handler manages "onLoginSent" action
		//
		handler.onPatientOpen(this) ;
	}
}
