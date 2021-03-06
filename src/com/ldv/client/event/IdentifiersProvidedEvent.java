package com.ldv.client.event ;

import com.google.gwt.event.shared.GwtEvent;

public class IdentifiersProvidedEvent extends GwtEvent<IdentifiersProvidedEventHandler>
{
	public static Type<IdentifiersProvidedEventHandler> TYPE = new Type<IdentifiersProvidedEventHandler>() ;
	
	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Type<IdentifiersProvidedEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<IdentifiersProvidedEventHandler>();
		return TYPE;
	}
	
	
	private final String _identifier ;
	private final String _password ;
	
	public IdentifiersProvidedEvent(final String identifier, final String password) 
	{
		_identifier = identifier ;
		_password   = password ;
	}
	
	public String getIdentifier() 
	{
		return _identifier ;
	}
	
	public String getPassword() 
	{
		return _password ;
	}
	
	@Override
	public Type<IdentifiersProvidedEventHandler> getAssociatedType() 
	{
		return TYPE;
	}

	@Override
	protected void dispatch(final IdentifiersProvidedEventHandler handler) 
	{
		handler.onIdentifierProvided(this);
	}
}