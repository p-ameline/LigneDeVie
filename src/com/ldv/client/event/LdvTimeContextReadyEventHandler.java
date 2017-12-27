package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvTimeContextReadyEventHandler extends EventHandler 
{
	void onTimeContextReady(LdvTimeContextReadyEvent event) ;
}