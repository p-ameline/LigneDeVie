package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvTimeStepsSentEventHandler extends EventHandler 
{
	void onTimeStepsSend(LdvTimeStepsSentEvent event) ;
}