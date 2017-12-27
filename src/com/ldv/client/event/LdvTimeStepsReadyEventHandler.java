package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvTimeStepsReadyEventHandler extends EventHandler 
{
	void onTimeStepsReady(LdvTimeStepsReadyEvent event) ;
}