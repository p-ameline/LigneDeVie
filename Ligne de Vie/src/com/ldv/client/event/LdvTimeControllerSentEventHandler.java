package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvTimeControllerSentEventHandler extends EventHandler 
{
	void onTimeControllerSend(LdvTimeControllerSentEvent event) ;
}