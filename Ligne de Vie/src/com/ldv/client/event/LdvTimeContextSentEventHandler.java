package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvTimeContextSentEventHandler extends EventHandler 
{
	void onTimeContextSend(LdvTimeContextSentEvent event) ;
}