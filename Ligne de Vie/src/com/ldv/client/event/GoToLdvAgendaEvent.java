package com.ldv.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class GoToLdvAgendaEvent extends GwtEvent<GoToLdvAgendaEventHandler> 
{	
	public static Type<GoToLdvAgendaEventHandler> TYPE = new Type<GoToLdvAgendaEventHandler>() ;
	
	public static Type<GoToLdvAgendaEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<GoToLdvAgendaEventHandler>() ;
		return TYPE ;
	}
	
	public GoToLdvAgendaEvent() {	
	}
			
	@Override
	protected void dispatch(GoToLdvAgendaEventHandler handler) {
		handler.onGoToAgenda(this) ;
	}

	@Override
	public Type<GoToLdvAgendaEventHandler> getAssociatedType() {
		return TYPE ;
	}
}
