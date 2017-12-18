package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvAgendaSentEventHandler extends EventHandler 
{
	void onAgendaSent(LdvAgendaSentEvent event) ;
}