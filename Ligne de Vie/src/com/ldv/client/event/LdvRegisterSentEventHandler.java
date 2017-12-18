package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvRegisterSentEventHandler extends EventHandler 
{
	void onRegisterSend(LdvRegisterSentEvent event) ;
}