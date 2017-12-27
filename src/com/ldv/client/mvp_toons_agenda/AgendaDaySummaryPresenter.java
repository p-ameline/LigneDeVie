package com.ldv.client.mvp_toons_agenda;

import java.util.Iterator;
import java.util.Vector;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.event.AgendaDaySummaryInitEvent;
import com.ldv.client.event.AgendaDaySummaryInitEventHandler;
import com.ldv.client.event.AgendaRedrawDayEvent;
import com.ldv.client.event.AgendaRedrawDayEventHandler;
import com.ldv.client.util.LdvSupervisor;
import com.ldv.shared.calendar.Event;
import com.ldv.shared.model.LdvTime;

import net.customware.gwt.presenter.client.EventBus;
import net.customware.gwt.presenter.client.widget.WidgetDisplay;
import net.customware.gwt.presenter.client.widget.WidgetPresenter;

/**
 * AgendaDayPresenter manages a day that displays its events according to a time scale 
 */
public class AgendaDaySummaryPresenter extends WidgetPresenter<AgendaDaySummaryPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public FlowPanel getMainPanel() ;
		
		public void      clearAll() ;
		
		public void      addEvent(final Event event) ;
	}

	private final LdvSupervisor _supervisor ;
	
	private 	    LdvTime	      _day = new LdvTime(0) ;
	private       Panel 			  _workspace ;
		
	@Inject
	public AgendaDaySummaryPresenter(final Display display, EventBus eventBus, LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_supervisor = supervisor ;
		
		bind() ;
		
		Log.info("AgendaDaySummaryPresenter constructed") ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(AgendaDaySummaryInitEvent.TYPE, new AgendaDaySummaryInitEventHandler() 
		{
			@Override
			public void onInitAgendaDay(AgendaDaySummaryInitEvent event) 
			{
				connectToAgenda(event) ;
			}
		});
		
		eventBus.addHandler(AgendaRedrawDayEvent.TYPE, new AgendaRedrawDayEventHandler() 
		{
			@Override
			public void onRedrawAgendaDay(AgendaRedrawDayEvent event) 
			{
				Log.info("AgendaDaySummaryPresenter for " + _day.getLocalSimpleDate() + " handling AgendaRedrawDayEvent event for day " + event.getDayToRedraw().getLocalSimpleDate()) ;
				redraw(event) ;
			}
		});
	}
	
	/** 
	 * Initialize all information
	 * 
	 * @param event Event that contains all needed information for initialization
	 */
	protected void connectToAgenda(AgendaDaySummaryInitEvent event)
	{
		if ((null == event) || (event.getTarget() != this))
			return ;
		
		_day.initFromLdvTime(event.getDay()) ;
		
		// Add this project display the Agenda view
		//
		_workspace = event.getHostPanel() ;
		_workspace.add(display.asWidget()) ;
		
		//
		//
		redraw() ;
	}
	
	public void redraw(AgendaRedrawDayEvent event)
	{	
		//Log.info("entering redraw concern line.");
		if (false == _day.equals(event.getDayToRedraw()))
			return ;
		
		redraw() ;
		
		drawEvents(event.getEvents()) ;
	}
	
	public void redraw()
	{	
		display.clearAll() ;
	}
	
	public void drawEvents(final Vector<Event> aEvents)
	{
		if ((null == aEvents) || aEvents.isEmpty())
			return ;
		
		for (Iterator<Event> it = aEvents.iterator() ; it.hasNext() ; )
		{
			Event event = it.next() ;
			
			if (false == isConcernedByEvent(event, _day))
				break ;
						
			display.addEvent(event) ;
		}
	}
	
	public void redrawDocument(){
		
	}
		
	/**
	 * Determine if there is some part of an event to be displayed on this day
	 */
	public static boolean isConcernedByEvent(final Event event, final LdvTime tDay)
	{
		// Event starts on the same day, then Ok
		//
		if (tDay.deltaDaysLocal(event.getDateStart()) == 0)
			return true ;
		
		// If event starts after this day, it doesn't fit here
		//
		if (event.getDateStart().isAfter(tDay))
			return false ;
		
		// If event ends before this day, it doesn't fit here
		//
		LdvTime endDate = event.getDateEnd() ; 
		if (false == endDate.isEmpty())
		{
			// Event ends on the same day, then Ok
			//
			if (tDay.deltaDaysLocal(event.getDateEnd()) == 0)
				return true ;
			
			// If event ends before this day, it doesn't fit here
			//
			if (event.getDateEnd().isBefore(tDay))
				return false ;
		}
		
		return true ;
	}
	
	@Override
	protected void onUnbind() {
	}

	@Override
	public void revealDisplay() {
	}

	@Override
	protected void onRevealDisplay() {
		// TODO Auto-generated method stub
		
	}
}
