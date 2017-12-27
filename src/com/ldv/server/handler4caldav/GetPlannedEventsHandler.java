package com.ldv.server.handler4caldav;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.DateProperty;

import org.apache.commons.logging.Log;
import org.osaf.caldav4j.credential.CaldavCredential;

import com.ldv.server.model.AgendaCredentialManager;
import com.ldv.shared.calendar.Event;
import com.ldv.shared.model.LdvTime;
import com.ldv.shared.rpc4caldav.GetPlannedEventsAction;
import com.ldv.shared.rpc4caldav.GetPlannedEventsResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get a list of Events for a time interval 
 * 
 **/	
public class GetPlannedEventsHandler implements ActionHandler<GetPlannedEventsAction, GetPlannedEventsResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public GetPlannedEventsHandler(final Log logger,
                                   final Provider<ServletContext> servletContext,       
                                   final Provider<HttpServletRequest> servletRequest)
	{
		super() ;
		
		_logger         = logger ;
		_servletContext = servletContext ;
		_servletRequest = servletRequest ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public GetPlannedEventsHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public GetPlannedEventsResult execute(final GetPlannedEventsAction action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			// CyrusCaldavCredential CyrusCredential = new CyrusCaldavCredential("213.246.42.106", "eve", "K87xux5myJSi") ;
			CaldavCredential credential = AgendaCredentialManager.getCredential(action.getSessionElements()) ;
			
			BaseCaldavClient client = new BaseCaldavClient(credential) ;
			CalDavCollectionManager collectionManager = new CalDavCollectionManager(client) ;
			
			LdvTime startDate = new LdvTime(0) ;
			startDate.initFromLocalDate(action.getStartDate()) ;
						
			List<Calendar> intervalCalendars = null ;
			
			if ((null == action.getEndDate()) || "".equals(action.getEndDate()))
				intervalCalendars = collectionManager.getEventResourcesForInterval(startDate, null) ;
			else
			{
				LdvTime endDate = new LdvTime(0) ;
				endDate.initFromLocalDate(action.getEndDate()) ;
			
				intervalCalendars = collectionManager.getEventResourcesForInterval(startDate, endDate) ;
			}
				
			if (null == intervalCalendars)
				return new GetPlannedEventsResult(false, null, "Call to calendar failed.") ;
						
			GetPlannedEventsResult result = new GetPlannedEventsResult(true, null, "") ;
			
			if (fillEventsFromCalendars(intervalCalendars, result))
				return result ;
			
			String sResult = "" ; // trace.getResponseBodyAsString() ;
			
			return new GetPlannedEventsResult(false, null, "Error parsing calendar elements") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to know if pseudo exists", cause) ;
   
			return new GetPlannedEventsResult(false, null, cause.getMessage()) ;
		}
  }
		
	protected boolean fillEventsFromCalendars(final List<Calendar> intervalCalendars, GetPlannedEventsResult result)
	{
		if ((null == intervalCalendars) || intervalCalendars.isEmpty() || (null == result))
			return false ;
		
		for (Iterator<Calendar> it = intervalCalendars.iterator() ; it.hasNext() ; )
		{
			Calendar calendar = it.next() ;
			
			Event event = fillEventFromCalendar(calendar) ;
			if (null != event)
				result.addEvent(event) ;
		}
		
		return true ;
	}
	
	protected Event fillEventFromCalendar(Calendar calendar)
	{
		if (null == calendar)
			return null ;
		
		CalendarComponent eventComponent = calendar.getComponent(Component.VEVENT) ;
		if (null == eventComponent)
			return null ;
		
		Event event = new Event() ;
		
		Property UidProp = eventComponent.getProperty(Property.UID) ;
		if (null != UidProp)
			event.setUID(UidProp.getValue()) ;
		
		Property SummaryProp = eventComponent.getProperty(Property.SUMMARY) ;
		if (null != SummaryProp)
			event.setSummary(SummaryProp.getValue()) ;
		
		Property DescriptionProp = eventComponent.getProperty(Property.DESCRIPTION) ;
		if (null != DescriptionProp)
			event.setDescription(DescriptionProp.getValue()) ;
		
		DateProperty DateStartProp = (DateProperty) eventComponent.getProperty(Property.DTSTART) ;
		if (null != DateStartProp)
			event.setDateStart((java.util.Date) DateStartProp.getDate()) ;
		
		DateProperty DateEndProp = (DateProperty) eventComponent.getProperty(Property.DTEND) ;
		if (null != DateEndProp)
			event.setDateEnd((java.util.Date) DateEndProp.getDate()) ;
		
		return event ;
	}
	
	@Override
	public void rollback(final GetPlannedEventsAction action,
        							 final GetPlannedEventsResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<GetPlannedEventsAction> getActionType()
	{
		return GetPlannedEventsAction.class ;
	}
}
