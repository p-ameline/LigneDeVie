package com.ldv.client.mvp_toons_agenda;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widgetideas.client.ResizableWidget;
import com.ldv.shared.calendar.Event;

public class AgendaDaySummaryView extends Composite implements ResizableWidget, AgendaDaySummaryPresenter.Display
{	
	private final FlowPanel _mainPanel ;
	
	public AgendaDaySummaryView()
	{	
		super() ;
		
		_mainPanel = new FlowPanel() ;
		_mainPanel.addStyleName("agenda-DaySummaryDisplay-shell") ;
				
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
	 * Add a visual component for an event  
	 * 
	 * @param event Event to display in created widget
	 */
	@Override
	public void addEvent(final Event event)
	{
		if (null == event)
			return ;
		
		FlowPanel eventPanel = new FlowPanel() ;
		eventPanel.addStyleName("agenda-DaySummaryDisplay-day") ;
		
		String sDateDeb = event.getDateStart().getLocalTime() ;
		String sLabel   = sDateDeb.substring(0, 2) + ":" + sDateDeb.substring(2, 4)  + " " + event.getSummary() ;
		
		Label summaryLabel = new Label(sLabel) ;
		eventPanel.add(summaryLabel) ;
		
		_mainPanel.add(eventPanel) ;		
	}
	
	@Override
	public FlowPanel getMainPanel() {
		return _mainPanel ;
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
