package com.ldv.server.handler4caldav;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VAvailability;

import org.apache.commons.logging.Log;
import org.osaf.caldav4j.credential.CaldavCredential;

import com.ldv.server.model.AgendaCredentialManager;
import com.ldv.server.model4calendar.BackOfficeAvailability;
import com.ldv.shared.calendar.Availability;
import com.ldv.shared.rpc4caldav.GetAvailabilityAction;
import com.ldv.shared.rpc4caldav.GetAvailabilityResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get user's list of Availability components 
 * 
 **/	
public class GetAvailabilityHandler implements ActionHandler<GetAvailabilityAction, GetAvailabilityResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public GetAvailabilityHandler(final Log logger,
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
	public GetAvailabilityHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public GetAvailabilityResult execute(final GetAvailabilityAction action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			// CyrusCaldavCredential CyrusCredential = new CyrusCaldavCredential("213.246.42.106", "eve", "K87xux5myJSi") ;
			CaldavCredential credential = AgendaCredentialManager.getCredential(action.getSessionElements()) ;
			
			BaseCaldavClient client = new BaseCaldavClient(credential) ;
			CalDavCollectionManager collectionManager = new CalDavCollectionManager(client) ;
						
			List<Calendar> calendars = collectionManager.getAvailabilityResources() ;
			
			// If there, it means that no exception was fired
			//
			if (null == calendars)
				return new GetAvailabilityResult(true, null, "") ;
						
			GetAvailabilityResult result = new GetAvailabilityResult(true, null, "") ;
			
			if (fillAvailabilitiesFromCalendars(calendars, result))
				return result ;
			
			return new GetAvailabilityResult(false, null, "Error parsing calendar elements") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Call to getAvailabilityResources failed", cause) ;
			return new GetAvailabilityResult(false, null, cause.getMessage()) ;
		}
  }
		
	/**
	 * Parse the list of calendars to extract the Availability components they contain
	 * 
	 * @param calendars The calendars list
	 * @param result    The structure to fill with Availability objects
	 * 
	 * @return <code>true</code> if all went well, <code>false</code> if not
	 */
	protected boolean fillAvailabilitiesFromCalendars(final List<Calendar> calendars, GetAvailabilityResult result)
	{
		if ((null == calendars) || calendars.isEmpty() || (null == result))
			return false ;
		
		for (Iterator<Calendar> it = calendars.iterator() ; it.hasNext() ; )
			fillAvailabilitiesFromCalendar(it.next(), result) ;
		
		return true ;
	}
	
	/**
	 * Parse a Calendar object in order to extract the Availability components it contains 
	 * and insert them into the result
	 */
	protected boolean fillAvailabilitiesFromCalendar(final Calendar calendar, GetAvailabilityResult result)
	{
		if (null == calendar)
			return false ;
		
		ComponentList<CalendarComponent> calendarComponents = calendar.getComponents() ;
		
		if ((null == calendarComponents) || calendarComponents.isEmpty())
			return true ;
		
		// Treat all the components that are VAvailability objects
		//
		for (Iterator<CalendarComponent> it = calendarComponents.iterator() ; it.hasNext() ; )
		{
			VAvailability availabilityComponent = (VAvailability) it.next() ;
			
			if (null != availabilityComponent)
			{
				BackOfficeAvailability boAvailability = new BackOfficeAvailability() ;
				boAvailability.fillFromVAvailability(availabilityComponent) ;
				
				result.addAvailability(new Availability(boAvailability)) ;
			}
		}
		
		return true ;
	}
				
	@Override
	public void rollback(final GetAvailabilityAction action,
        							 final GetAvailabilityResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<GetAvailabilityAction> getActionType()
	{
		return GetAvailabilityAction.class ;
	}
}
