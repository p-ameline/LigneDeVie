package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface LdvRedrawProjectRecursiveEventHandler extends EventHandler 
{
	void onRedrawProjectRecursiveSend(LdvRedrawProjectRecursiveEvent event) ;
}