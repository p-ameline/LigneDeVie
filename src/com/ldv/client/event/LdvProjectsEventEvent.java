package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class LdvProjectsEventEvent extends GwtEvent<LdvProjectsEventEventHandler> 
{	
	public enum EventType { eventMouseUp  } ;
	private EventType _eventType ; 
	
	public static Type<LdvProjectsEventEventHandler> TYPE = new Type<LdvProjectsEventEventHandler>();
	
	public static Type<LdvProjectsEventEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<LdvProjectsEventEventHandler>();
		return TYPE;
	}
	
	public LdvProjectsEventEvent(EventType eventType) {
		_eventType = eventType ;
	}
	
	public EventType getEventType() {
		return _eventType ;
	}
		
	@Override
	protected void dispatch(LdvProjectsEventEventHandler handler) {
		handler.onProjectsEventSend(this);
	}

	@Override
	public Type<LdvProjectsEventEventHandler> getAssociatedType() {
		return TYPE;
	}
}
