package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvTimeControllerReadyEventHandler extends EventHandler 
{
	void onTimeControllerReady(LdvTimeControllerReadyEvent event) ;
}