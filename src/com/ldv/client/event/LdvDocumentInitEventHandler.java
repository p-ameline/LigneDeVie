package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvDocumentInitEventHandler extends EventHandler 
{
	void onInitSend(LdvDocumentInitEvent event) ;
}