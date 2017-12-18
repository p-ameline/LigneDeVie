package com.ldv.client.ui;

import com.google.gwt.event.shared.EventHandler;

/**
 * Handler for {@link CodeChangedEvent} events.
 */
public interface DateChangedHandler extends EventHandler {
	/**
   * Called when a native click event is fired.
   * 
   * @param event the {@link CodeChangedEvent} that was fired
   */
  void onDateChanged(DateChangedEvent event);
}