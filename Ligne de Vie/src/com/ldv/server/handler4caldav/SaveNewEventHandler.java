package com.ldv.server.handler4caldav;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Uid;

import org.apache.commons.logging.Log;
import org.osaf.caldav4j.credential.CaldavCredential;

import com.ldv.server.model.AgendaCredentialManager;
import com.ldv.shared.calendar.Event;
import com.ldv.shared.model.LdvTime;
import com.ldv.shared.rpc.SessionActionModel;
import com.ldv.shared.rpc4caldav.SaveNewEventAction;
import com.ldv.shared.rpc4caldav.SaveNewEventResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Save a new Event iCalendar component 
 * 
 **/	
public class SaveNewEventHandler implements ActionHandler<SaveNewEventAction, SaveNewEventResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public SaveNewEventHandler(final Log logger,
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
	public SaveNewEventHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public SaveNewEventResult execute(final SaveNewEventAction action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			// CyrusCaldavCredential CyrusCredential = new CyrusCaldavCredential("213.246.42.106", "eve", "K87xux5myJSi") ;
			CaldavCredential credential = AgendaCredentialManager.getCredential(action.getSessionElements()) ;
			
			BaseCaldavClient client = new BaseCaldavClient(credential) ;
			CalDavCollectionManager collectionManager = new CalDavCollectionManager(client) ;
			
			Event event = action.getEvent() ;
			if (null == event)
				return new SaveNewEventResult(false, null, "No event to save") ;
					
			// Create net.fortuna.ical4j.model.DateTime objects from starting and ending dates
			//
			DateTime startDate = new DateTime(event.getDateStart().toJavaDate()) ;
			DateTime endDate   = new DateTime(event.getDateEnd().toJavaDate()) ;
			
			// Build a UID
			//
			String sUid = getUID(action.getSessionElements()) ;
			
			// Create the event object to be recorded
			//
			VEvent vEvent = new VEvent(startDate, endDate, event.getSummary()) ;
			vEvent.getProperties().add(new Description(event.getDescription())) ;
			vEvent.getProperties().add(new Uid(sUid)) ;
			
			// Save Event
			//
			String sSavedUid = collectionManager.saveEvent(vEvent) ;
			
			if ("".equals(sSavedUid))
				return new SaveNewEventResult(false, null, "Error recording event") ;
			
			Event savedEvent = new Event(event) ;
			savedEvent.setUID(sSavedUid) ;
			
			return new SaveNewEventResult(true, savedEvent, "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to know if pseudo exists", cause) ;
   
			return new SaveNewEventResult(false, null, cause.getMessage()) ;
		}
  }
	
	/**
	 * Get a Unique Identifier
	 * 
	 *  RFC5545 : The "UID" itself MUST be a globally unique identifier.
   *  The generator of the identifier MUST guarantee that the identifier
   *  is unique.  There are several algorithms that can be used to
   *  accomplish this.  A good method to assure uniqueness is to put the
   *  domain name or a domain literal IP address of the host on which
   *  the identifier was created on the right-hand side of an "@", and
   *  on the left-hand side, put a combination of the current calendar
   *  date and time of day (i.e., formatted in as a DATE-TIME value)
   *  along with some other currently unique (perhaps sequential)
   *  identifier available on the system (for example, a process id
   *  number).  Using a DATE-TIME value on the left-hand side and a
   *  domain name or domain literal on the right-hand side makes it
   *  possible to guarantee uniqueness since no two hosts should be
   *  using the same domain name or IP address at the same time.  Though
   *  other algorithms will work, it is RECOMMENDED that the right-hand
   *  side contain some domain identifier (either of the host itself or
   *  otherwise) such that the generator of the message identifier can
   *  guarantee the uniqueness of the left-hand side within the scope of
   *  that domain.
	 * 
	 **/
	protected String getUID(final SessionActionModel sessionElements)
	{
		if (null == sessionElements)
			return "" ;
		
		String sUserIdentifier = sessionElements.getUserIdentifier() ;
		
		LdvTime tNow = new LdvTime(0) ;
		tNow.takeTime() ;

		String sDateTime = tNow.getLocalFullDateTime() ;
		
		return "event" + sDateTime + "@" + sUserIdentifier ;
	}
	
	@Override
	public void rollback(final SaveNewEventAction action,
        							 final SaveNewEventResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<SaveNewEventAction> getActionType() {
		return SaveNewEventAction.class ;
	}
}
