package com.ldv.client.event;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.event.shared.GwtEvent;
import com.ldv.shared.calendar.Event;
import com.ldv.shared.model.LdvTime;

/**
 * Event that signal an AgendaDayPresenter that it must redraw its view 
 */
public class AgendaRedrawDayEvent extends GwtEvent<AgendaRedrawDayEventHandler> 
{	
	public static Type<AgendaRedrawDayEventHandler> TYPE = new Type<AgendaRedrawDayEventHandler>();
	
	private LdvTime       _dayToRedraw = new LdvTime(0) ;
	private Vector<Event> _aEvents     = new Vector<Event>() ;
	
	public static Type<AgendaRedrawDayEventHandler> getType() 
	{
		if (null == TYPE)
			TYPE = new Type<AgendaRedrawDayEventHandler>() ;
		return TYPE;
	}

	public AgendaRedrawDayEvent(LdvTime dayToRedraw, final Vector<Event> aEvents) 
	{
		_dayToRedraw.initFromLdvTime(dayToRedraw) ;
		setEvents(aEvents) ;
	}

	public LdvTime getDayToRedraw(){
		return _dayToRedraw ;
	}
	
	public Vector<Event> getEvents() { 
		return _aEvents ;
	}
	public void addEvent(final Event event) 
	{ 
		if (null == event)
			return ;
		
		_aEvents.add(new Event(event)) ;
	}
	public void setEvents(final Vector<Event> aEvents) 
	{ 
		_aEvents.clear() ;
		
		if ((null == aEvents) || aEvents.isEmpty())
			return ;
		
		for (Iterator<Event> it = aEvents.iterator() ; it.hasNext() ; )
			_aEvents.add(new Event(it.next())) ;
	}
	
	@Override
	protected void dispatch(AgendaRedrawDayEventHandler handler) {
		handler.onRedrawAgendaDay(this);
	}

	@Override
	public Type<AgendaRedrawDayEventHandler> getAssociatedType() {
		return TYPE;
	}
}
