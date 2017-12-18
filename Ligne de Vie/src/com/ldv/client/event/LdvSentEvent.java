package com.ldv.client.event ;

import com.google.gwt.event.shared.GwtEvent;

public class LdvSentEvent extends GwtEvent<LdvSentEventHandler>
{
	public static Type<LdvSentEventHandler> TYPE = new Type<LdvSentEventHandler>() ;
	
	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<LdvSentEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvSentEventHandler>();
		return TYPE;
	}
	
	private final String _sessionId ;
	private final String _message ;
	
	public LdvSentEvent(final String sessionId, final String message) 
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
	public Type<LdvSentEventHandler> getAssociatedType() 
	{
		return TYPE ;
	}

	@Override
	protected void dispatch(final LdvSentEventHandler handler) 
	{
		// When activated, will get intercepted by whatever object whose event handler manages "onLoginSent" action
		//
		handler.onLoginSent(this) ;
	}
}
