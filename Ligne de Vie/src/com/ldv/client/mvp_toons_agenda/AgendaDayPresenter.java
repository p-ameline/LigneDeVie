package com.ldv.client.mvp_toons_agenda;

import java.util.Iterator;
import java.util.Vector;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.inject.Inject;

import com.ldv.client.event.AgendaDayInitEvent;
import com.ldv.client.event.AgendaDayInitEventHandler;
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
public class AgendaDayPresenter extends WidgetPresenter<AgendaDayPresenter.Display>
{
	public interface Display extends WidgetDisplay 
	{
		public AbsolutePanel getMainPanel() ;
		
		public void          clearAll() ;
		public void          addHourSeparator(int iTop) ;
		public void          setWidth(int iWidthInPixel) ;
		
		public void          addEvent(final Event event, int iStartY, int iEndY) ;
	}

	private final LdvSupervisor              _supervisor ;
	
	private 	    LdvTime	                   _day = new LdvTime(0) ;
	
	private       Panel 						         _workspace ;
	private       AgendaTimeDisplayPresenter _controler ;
		
	@Inject
	public AgendaDayPresenter(final Display display, EventBus eventBus, LdvSupervisor supervisor) 
	{	
		super(display, eventBus) ;
		
		_supervisor = supervisor ;
		_controler  = null ;
		
		bind();
		
		Log.info("AgendaDayPresenter constructed") ;
	}

	@Override
	protected void onBind() 
	{			
		eventBus.addHandler(AgendaDayInitEvent.TYPE, new AgendaDayInitEventHandler() 
		{
			@Override
			public void onInitAgendaDay(AgendaDayInitEvent event) 
			{
				connectToAgenda(event) ;
			}
		});
		
		eventBus.addHandler(AgendaRedrawDayEvent.TYPE, new AgendaRedrawDayEventHandler() 
		{
			@Override
			public void onRedrawAgendaDay(AgendaRedrawDayEvent event) 
			{
				Log.info("AgendaDayPresenter for " + _day.getLocalSimpleDate() + " handling AgendaRedrawDayEvent event for day " + event.getDayToRedraw().getLocalSimpleDate()) ;
				redraw(event) ;
			}
		});		
	}
	
	/** 
	 * Initialize all information
	 * 
	 * @param event Event that contains all needed information for initialization
	 */
	protected void connectToAgenda(AgendaDayInitEvent event)
	{
		if ((null == event) || (event.getTarget() != this))
			return ;
		
		_day.initFromLdvTime(event.getDay()) ;
		
		_controler = event.getTimeManager() ;
		
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
		display.setWidth(_workspace.getOffsetWidth()) ;
		drawHourSeparators() ;		
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
			
			// Get the position of this event by processing start and end time
			//
			int iStartY = 0 ;
			int iEndY   = 0 ;
			
			LdvTime timeStart = event.getDateStart() ;
			if (timeStart.deltaDaysLocal(_day) == 0)
				iStartY = getPhysicalTopFromTime(timeStart.getLocalHours(), timeStart.getLocalMinutes()) ;
			
			iEndY = iStartY ;
			
			LdvTime timeEnd = event.getDateEnd() ;
			if (false == timeEnd.isEmpty())
			{
				if (timeEnd.deltaDaysLocal(_day) == 0)
					iEndY = getPhysicalTopFromTime(timeEnd.getLocalHours(), timeEnd.getLocalMinutes()) ;
				else
					iEndY = getPhysicalTopFromTime(23, 59) ;
			}
			
			display.addEvent(event, iStartY, iEndY) ;
		}
	}
	
	/**
	 * Redraw the horizontal bars that delineate hours
	 * 
	 */
	public void drawHourSeparators()
	{
		for (int iHour = 0 ; iHour < 24 ; iHour++)
		{
			int iTopPos = getPhysicalTopFromTime(iHour, 0) ;
			display.addHourSeparator(iTopPos) ;
		}
	}
	
	public void redrawDocument(){
		
	}
	
	/**
	 * Get the number of pixels corresponding to a time in hours:minutes
	 * 
	 * @param iHours   Number of hours from 00:00
	 * @param iMinutes Number of minutes in this hour
	 * 
	 * @return Number of corresponding pixels as int
	 */
	public int getPhysicalTopFromTime(int iHours, int iMinutes) {
		return _controler.getPhysicalTopFromTime(iHours, iMinutes) ;
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
