package com.ldv.client.event ;

import com.google.gwt.event.shared.EventHandler;

public interface LdvSentEventHandler extends EventHandler 
{
	void onLoginSent(LdvSentEvent event);
}
