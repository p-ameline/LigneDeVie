package com.ldv.client.ui;

import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Handler for {@link DateChangedEvent} events.
 */
public class DateChangedEvent extends GwtEvent<DateChangedHandler> {
	/**
   * Event type for click events. Represents the meta-data associated with this
   * event.
   */
  private static final Type<DateChangedHandler> TYPE = new Type<DateChangedHandler>() ;

  /**
   * Gets the event type associated with click events.
   * 
   * @return the handler type
   */
  public static Type<DateChangedHandler> getType() {
    return TYPE;
  }

  /**
   * Protected constructor, use
   * {@link DomEvent#fireNativeEvent(com.google.gwt.dom.client.NativeEvent, com.google.gwt.event.shared.HasHandlers)}
   * to fire click events.
   */
  protected DateChangedEvent() {
  }

  @Override
  public final Type<DateChangedHandler> getAssociatedType() {
    return TYPE ;
  }

  @Override
  protected void dispatch(DateChangedHandler handler) {
    handler.onDateChanged(this) ;
  }
}
