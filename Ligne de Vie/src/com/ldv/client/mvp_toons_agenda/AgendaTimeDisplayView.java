package com.ldv.client.mvp_toons_agenda;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.google.gwt.widgetideas.client.ResizableWidgetCollection;

// public class LdvTimeWidget extends FlowPanel implements ResizableWidget
public class AgendaTimeDisplayView extends AbsolutePanel implements ResizableWidget, AgendaTimeDisplayPresenter.Display
{	
	protected int _iHourSpanInPixel ;
	
	public AgendaTimeDisplayView()
	{
		super() ;
		
		_iHourSpanInPixel = 0 ;
		
		// Create the outer shell
		//
		// DOM.setStyleAttribute(getElement(), "position", "relative") ;
		// DOM.setStyleAttribute(getElement(), "position", "absolute") ;
		setStyleName("agenda-TimeControl-shell") ;	    
	}
	
	/**
	 * This method is called immediately after a widget becomes attached to the
	 * browser's document.
	 */
	@Override
	protected void onLoad() 
	{
		// Reset the position attribute of the parent element
		// DOM.setStyleAttribute(getElement(), "position", "relative") ;
		ResizableWidgetCollection.get().add(this) ;
		redraw() ;
	}

	@Override
	protected void onUnload() {
		ResizableWidgetCollection.get().remove(this);
	}
	
	/**
	 * Redraw the progress bar when something changes the layout.
	 */
	public void redraw() 
	{
		if (false == isAttached())
			return ;
			
		int iWidth  = DOM.getElementPropertyInt(getElement(), "clientWidth") ;
		int iHeight = DOM.getElementPropertyInt(getElement(), "clientHeight") ;
			
		onResize(iWidth, iHeight) ;
	}
	
	/**
	  * This method is called when the dimensions of the parent element change.
	  * Subclasses should override this method as needed.
	  * 
	  * @param width the new client width of the element
	  * @param height the new client height of the element
	  */
	public void onResize(int iWidth, int iHeight) 
	{  
		_iHourSpanInPixel = iHeight / 24 ;
		
		// Draw time control components
		//
		clear() ;
		
		for (int iHour = 0 ; iHour < 24 ; iHour++)
		{
			int iTopPos = iHour * _iHourSpanInPixel ;
			
			Label timeLabel = new Label(getLabelForTime(iHour, 0)) ;
			timeLabel.setStyleName("agenda-dayDisplay-hourLabel") ;
			
			setTop(timeLabel, iTopPos) ;
			
			add(timeLabel) ;
		}
	}
	
	@Override
	public int getHourSpanInPixel() {
		return _iHourSpanInPixel ;
	}
	
	/**
	  * Provide a standardized label for a given time, eg something like HH:mm
	  * 
	  * @param iHours   the hour during the day, in the 0-23 interval
	  * @param iMinutes the minute during the hour, in the 0-59 interval
	  */
	protected String getLabelForTime(int iHours, int iMinutes)
	{
		String sLabel = "" ;
		
		if ((iHours < 0) || (iHours > 23))
			sLabel = "??" ;
		else
			sLabel = getTwoDigitsLabel(iHours) ;
		
		sLabel += ":" ;
			
		if ((iHours < 0) || (iMinutes > 59))
			sLabel += "??" ;
		else
			sLabel += getTwoDigitsLabel(iMinutes) ;
		
		return sLabel ;
	}
	
	/**
	  * Provide a 2 digits String from an int
	  * 
	  * @param iValue The integer to be expressed as a String
	  * 
	  * @return The integer as a 2 digits String
	  */
	protected String getTwoDigitsLabel(int iValue)
	{
		if ((iValue < 0) || (iValue > 99))
			return "??" ;
		
		String sLabel = "" ;
		if (iValue < 10)
			sLabel = "0" ;
		sLabel += Integer.toString(iValue) ;
		
		return sLabel ;
	}
	
	/**
	  * Set the top position for a Widget
	  * 
	  * @param widget Widget to be positionned
	  * @param iTop   The top position in pixels
	  */
	public void setTop(Widget widget, int iTopPos) {
		widget.getElement().getStyle().setTop(iTopPos, Style.Unit.PX) ;
	}
}
