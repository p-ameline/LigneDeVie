package com.ldv.server.handler4caldav;

import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.Available;
import net.fortuna.ical4j.model.component.VAvailability;
import net.fortuna.ical4j.model.property.Uid;

import org.apache.commons.logging.Log;
import org.osaf.caldav4j.credential.CaldavCredential;

import com.ldv.server.model.AgendaCredentialManager;
import com.ldv.server.model4calendar.BackOfficeAvailability;
import com.ldv.shared.calendar.Availability;
import com.ldv.shared.model.LdvTime;
import com.ldv.shared.rpc.SessionActionModel;
import com.ldv.shared.rpc4caldav.UpdateAvailabilityAction;
import com.ldv.shared.rpc4caldav.UpdateAvailabilityResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Update an Availability iCalendar component 
 * 
 **/	
public class UpdateAvailabilityHandler implements ActionHandler<UpdateAvailabilityAction, UpdateAvailabilityResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public UpdateAvailabilityHandler(final Log logger,
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
	public UpdateAvailabilityHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public UpdateAvailabilityResult execute(final UpdateAvailabilityAction action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			// CyrusCaldavCredential CyrusCredential = new CyrusCaldavCredential("213.246.42.106", "eve", "K87xux5myJSi") ;
			CaldavCredential credential = AgendaCredentialManager.getCredential(action.getSessionElements()) ;
			
			BaseCaldavClient client = new BaseCaldavClient(credential) ;
			CalDavCollectionManager collectionManager = new CalDavCollectionManager(client) ;
			
			if (null == action.getAvailability())
				return new UpdateAvailabilityResult(false, null, "No Availability to save") ;
			
			BackOfficeAvailability boAvailability = new BackOfficeAvailability(action.getAvailability()) ;
			
			VAvailability vAvailability = new VAvailability() ;
			boAvailability.fillVAvailability(vAvailability) ;
			
			// Set UIDs for new Available components
			//
			setUidToNewAvailable(vAvailability) ;
			
			// Update Availability component
			//
			if (false == collectionManager.updateAvailability(vAvailability))
				return new UpdateAvailabilityResult(false, null, "Error updating availability") ;
			
			BackOfficeAvailability savedAvailability = new BackOfficeAvailability() ;
			savedAvailability.fillFromVAvailability(vAvailability) ;
			
			return new UpdateAvailabilityResult(true, new Availability(savedAvailability), "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to update Availability component", cause) ;
			return new UpdateAvailabilityResult(false, null, cause.getMessage()) ;
		}
  }
	
	/**
	 * Set a calculated UID to all VAvailable components that don't already have one (i.e. new ones)
	 * 
	 * @param vAvailability iCalendar VAvailability component which VAvailable are to be completed
	 */
	protected void setUidToNewAvailable(VAvailability vAvailability)
	{
		if (null == vAvailability)
			return ;
		
		ComponentList<Available> availables = vAvailability.getAvailable() ;
		if ((null == availables) || availables.isEmpty())
			return ;
		
		String sUidRoot      = getUidRoot(vAvailability) ;
		String sUidExtension = getUidExtension(vAvailability) ;
		
		for (Iterator<Available> it = availables.iterator() ; it.hasNext() ; )
		{
			Available vAvail = it.next() ;
			Property UIDprop = vAvail.getProperty(Property.UID) ;
			if (null == UIDprop)
			{
				String sNewUid = getNextAvailableUid(sUidRoot, vAvailability) ;
				if (false == "".equals(sNewUid))
					vAvail.getProperties().add(new Uid(sNewUid + sUidExtension)) ;
			}
		}
	}
	
	/**
	 * Find the highest UID already affected to a VAvailable component and returns its increment by one unit
	 * 
	 * @param sUidRoot      The root for all UIDs. UIDs are in the form <code>sUidRoot + "_" + counter</code>
	 * @param vAvailability The iCalendar VAvailability that contains the VAvailable components to process
	 * 
	 * @return The next UID to allocate if everything went properly, <code>sUidRoot + "_001"</code> if no VAvailable can be found
	 */
	protected String getNextAvailableUid(final String sUidRoot, final VAvailability vAvailability)
	{
		if (null == vAvailability)
			return sUidRoot + "_001" ;
		
		ComponentList<Available> availables = vAvailability.getAvailable() ;
		if ((null == availables) || availables.isEmpty())
			return sUidRoot + "_001" ;
		
		String sNextCounter = "000" ;
		
		for (Iterator<Available> it = availables.iterator() ; it.hasNext() ; )
		{
			Available vAvail = it.next() ;
			
			String sCounter = getUidCounter(vAvail) ;
			if (sCounter.compareTo(sNextCounter) > 0)
				sNextCounter = sCounter ; 
		}
		
		return sUidRoot + "_" + getNextCounter(sNextCounter) ;
	}
	
	/**
	 * The UID of an Availability component is in the form <code>"avbty" + sDateTime + "@" + sUserIdentifier</code><br>
	 * This function returns the substring before the first '@'
	 */
	protected String getUidExtension(final VAvailability vAvailability)
	{
		if (null == vAvailability)
			return "" ;
		
		Property UIDprop = vAvailability.getProperty(Property.UID) ;
		if (null == UIDprop)
			return "" ;
		
		return getRightPartOfUid(UIDprop) ;
	}
	
	/**
	 * The UID of an Availability component is in the form <code>"avbty" + sDateTime + "@" + sUserIdentifier</code><br>
	 * This function returns the substring after, and including, the first '@' (i.e. <code>"@" + sUserIdentifier</code>)
	 */
	protected String getUidRoot(final VAvailability vAvailability)
	{
		if (null == vAvailability)
			return "" ;
		
		Property UIDprop = vAvailability.getProperty(Property.UID) ;
		if (null == UIDprop)
			return "" ;
		
		return getLeftPartOfUid(UIDprop) ;
	}
	
	/**
	 * The UID of an Available component is in the form <code>"avbty" + sDateTime + "_" + counter  + "@" + sUserIdentifier</code><br>
	 * This function returns the counter
	 */
	protected String getUidCounter(final Available vAvailable)
	{
		if (null == vAvailable)
			return "" ;
		
		Property UIDprop = vAvailable.getProperty(Property.UID) ;
		if (null == UIDprop)
			return "" ;
		
		String sLeftPart = getLeftPartOfUid(UIDprop) ;
		if ("".equals(sLeftPart))
			return "" ;
		
		String[] splits = sLeftPart.split("_") ;
		if (splits.length < 2)
			return "" ;
		
		return splits[1] ;
	}
	
	/**
	 * This function returns the substring before the first '@' in a property value
	 */
	protected String getLeftPartOfUid(final Property UIDprop)
	{
		if (null == UIDprop)
			return "" ;
		
		String sUID = UIDprop.getValue() ;
		if ("".equals(sUID))
			return "" ;
		
		String[] splits = sUID.split("@") ;
		if (splits.length == 0)
			return "" ;
		
		return splits[0] ;
	}
	
	/**
	 * This function returns the substring after, and including, the first '@' in a property value
	 */
	protected String getRightPartOfUid(final Property UIDprop)
	{
		if (null == UIDprop)
			return "" ;
		
		String sUID = UIDprop.getValue() ;
		if ("".equals(sUID))
			return "" ;
		
		String[] splits = sUID.split("@") ;
		if (splits.length == 0)
			return sUID ;
		
		return "@" + splits[1] ;
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
	protected String getAvailableUID(final SessionActionModel sessionElements)
	{
		if (null == sessionElements)
			return "" ;
		
		String sUserIdentifier = sessionElements.getUserIdentifier() ;
		
		LdvTime tNow = new LdvTime(0) ;
		tNow.takeTime() ;

		String sDateTime = tNow.getLocalFullDateTime() ;
		
		return "avble" + sDateTime + "@" + sUserIdentifier ;
	}
	
	protected String getNextCounter(final String sPreviousCounter)
	{
		if ((null == sPreviousCounter) || "".equals(sPreviousCounter))
			return "001" ;
		
		String sReturn = "" ;
		
		try {
			sReturn = getNextId(sPreviousCounter) ; 
		} catch (NumberFormatException e) {
			return getNextCounter("0" + sPreviousCounter) ;
		}
		
		return sReturn ; 
	}
	
	/**
	 * Computes an increment in base 36 (0-9 A-Z).
	 * @param id the String id in base 36.
	 * @return the following id.
	 * @throws NumberFormatException if the counter is full (for example "ZZZ")
	 */
	static public String getNextId(final String id) throws NumberFormatException
	//====================================================================
	{
		if (null == id)
		  return null ;
		
		int len = id.length() ;
		if (0 == len)
		  return null ;
  
		StringBuffer nextId = new StringBuffer(id) ;	//make a copy of the id
		int i = len - 1 ;
		while (true)
		{
			char j = id.charAt(i) ;
			j++ ;
			if ((j >= '0' && j <= '9') || (j >= 'A' && j <= 'Z'))
			{
				nextId.setCharAt(i, j) ;
				break ;
			}
			else if (j > '9' && j < 'A')
			{
				nextId.setCharAt(i, 'A') ;
				break ;
			}
			else
			{
				nextId.setCharAt(i, '0') ;
				if (0 == i)
				  throw new NumberFormatException() ;
				i-- ;
			}
		}
		return nextId.toString() ;    
	}
	
	@Override
	public void rollback(final UpdateAvailabilityAction action,
        							 final UpdateAvailabilityResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<UpdateAvailabilityAction> getActionType() {
		return UpdateAvailabilityAction.class ;
	}
}
