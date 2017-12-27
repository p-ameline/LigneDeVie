package com.ldv.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface OpenArchetypeEventHandler extends EventHandler {
	/**
	 * Called when SendLoginActionEvent is fired.
	 *
	 * @param event
	 *            the {@link WizardCompletionEvent} that was fired
	 */
	
	void onOpenArchetype(OpenArchetypeEvent event);
}
