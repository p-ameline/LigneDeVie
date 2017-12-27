package com.ldv.client.ui;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * A widget that implements this interface provides registration for
 * {@link CodeChangedHandler} instances.
 */
public interface HasDateChangedHandlers extends HasHandlers 
{
	/**
   * Adds a {@link ClickEvent} handler.
   * 
   * @param handler the click handler
   * @return {@link HandlerRegistration} used to remove this handler
   */
  HandlerRegistration addDateChangedHandler(DateChangedHandler handler) ;  
}