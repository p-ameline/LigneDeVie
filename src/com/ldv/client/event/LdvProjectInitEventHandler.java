package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvProjectInitEventHandler extends EventHandler 
{
	void onInitSend(LdvProjectInitEvent event) ;
}