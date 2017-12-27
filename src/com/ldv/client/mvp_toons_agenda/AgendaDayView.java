package com.ldv.client.mvp_toons_agenda;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.ldv.shared.calendar.Event;

import com.google.gwt.dom.client.Style; 
import com.google.gwt.dom.client.Style.Position;

public class AgendaDayView extends Composite implements ResizableWidget, AgendaDayPresenter.Display
{	
	private final AbsolutePanel _mainPanel ;
	
	public AgendaDayView()
	{	
		super() ;
		
		_mainPanel = new AbsolutePanel() ;
		_mainPanel.addStyleName("agenda-DayDisplay-shell") ;
		_mainPanel.getElement().getStyle().setPosition(Position.ABSOLUTE) ;
				
		initWidget(_mainPanel) ;
	}	
	
	/**
	 * Remove all child widgets 
	 * 
	 */
	@Override
	public void clearAll() {
		_mainPanel.clear() ;
	}

	/**
	 * Add a hour separator 
	 * 
	 * @param iTop Top position for line
	 */
	@Override
	public void addHourSeparator(int iTop)
	{
		SimplePanel separator = new SimplePanel() ;
		separator.setStyleName("agenda-Toon-HourSeparator") ;
		
		_mainPanel.add(separator) ;
		
		setWidgetPosition(separator, 0, iTop) ;
	}
	
	/**
	 * Add a visual component for an event  
	 * 
	 * @param event Event to display in created widget
	 */
	@Override
	public void addEvent(final Event event, int iStartY, int iEndY)
	{
		if (null == event)
			return ;
		
		FlowPanel eventPanel = new FlowPanel() ;
		eventPanel.addStyleName("agenda-DayDisplay-day") ;
		
		Label summaryLabel = new Label(event.getSummary()) ;
		eventPanel.add(summaryLabel) ;
		
		_mainPanel.add(eventPanel) ;
		
		setWidgetPosition(eventPanel, 0, iStartY) ;
		
		int iHeight = iEndY - iStartY ;
		eventPanel.setHeight(iHeight + "px") ;
	}
	
	/**
	 * Set concern line's left and bottom coordinates 
	 * 
	 * @param widget Widget to set position to
	 * @param left   Left position for line
	 * @param top    Top position for line
	 */
	public void setWidgetPosition(Widget widget, int iLeft, int iTop)
	{
		widget.getElement().getStyle().setLeft(iLeft, Style.Unit.PX) ;
		widget.getElement().getStyle().setTop(iTop, Style.Unit.PX) ;		
	}
			
	@Override
	public AbsolutePanel getMainPanel() {
		return _mainPanel ;
	}
	
	@Override
	public void setWidth(int iWidthInPixel) {
		setWidth(iWidthInPixel + "px") ;
	}
	
	@Override
	public Widget asWidget() {
		return this ;
	}

	@Override
	public void onResize(int width, int height) {
		// TODO Auto-generated method stub		
	}
}
