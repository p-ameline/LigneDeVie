package com.ldv.server.handler4caldav;

import static org.osaf.caldav4j.util.UrlUtils.stripHost;

import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VAvailability;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Version;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.webdav.DavException;
import org.apache.jackrabbit.webdav.MultiStatusResponse;
import org.osaf.caldav4j.exceptions.CalDAV4JException;
import org.osaf.caldav4j.CalDAVCalendarCollectionBase;
import org.osaf.caldav4j.CalDAVConstants;
import org.osaf.caldav4j.CalDAVResource;
import org.osaf.caldav4j.exceptions.ResourceNotFoundException;
import org.osaf.caldav4j.methods.CalDAV4JMethodFactory;
import org.osaf.caldav4j.methods.CalDAVReportMethod;
import org.osaf.caldav4j.methods.DeleteMethod;
import org.osaf.caldav4j.methods.GetMethod;
import org.osaf.caldav4j.methods.HttpClient;
import org.osaf.caldav4j.methods.MkCalendarMethod;
import org.osaf.caldav4j.methods.PutMethod;
import org.osaf.caldav4j.model.request.CalDAVProp;
import org.osaf.caldav4j.model.request.CalendarData;
import org.osaf.caldav4j.model.request.CalendarQuery;
import org.osaf.caldav4j.model.request.Comp;
import org.osaf.caldav4j.model.request.CompFilter;
import org.osaf.caldav4j.model.request.PropFilter;
import org.osaf.caldav4j.model.request.TextMatch;
import org.osaf.caldav4j.model.request.TimeRange;
import org.osaf.caldav4j.model.response.CalendarDataProperty;
import org.osaf.caldav4j.util.CalendarComparator;
import org.osaf.caldav4j.util.ICalendarUtils;
import org.osaf.caldav4j.util.MethodUtil;
import org.osaf.caldav4j.util.UrlUtils;

import com.ldv.shared.model.LdvTime;

import org.osaf.caldav4j.util.CaldavStatus;;


/**
 * this class binds a CalendarCollection (a folder of events) to a Caldav Client
 * giving a complete Caldav Browser
 * @author rpolli@babel.it
 *
 */
public class CalDavCollectionManager extends CalDAVCalendarCollectionBase {

	private BaseCaldavClient client = null ;
	private CalDAV4JMethodFactory methodFactory = new CalDAV4JMethodFactory() ;

	
	/**
	 * associates a CalendarCollection to a BaseCalDavClient, 
	 * pointing to a default folder
	 */
	public CalDavCollectionManager(BaseCaldavClient c)
	{
		super() ;
		client = c ;

		setHostConfiguration(c.hostConfig) ;
		setMethodFactory(methodFactory) ;
    	
		setCalendarCollectionRoot() ;
	}
	
	// BaseCaldavClient Wrappers
	public String getUsername() {
		return client.getCalDavSeverUsername() ;
	}
	
	public void deletePath(String path)
	{
		DeleteMethod delete = new DeleteMethod(path) ;
		try {
			client.executeMethod(getHostConfiguration(), delete);
		} catch (HttpException e) {
			// TODO catch and throw a Caldav4jException
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
      
	/**
	 * assumes that URL is function of uid
	 * @param uid
	 * @throws CalDAV4JException
	 */
	public void deleteComponentByUid(String uid) throws CalDAV4JException 
	{
		if ((uid!=null) && (! "".equals(uid)))
		{
			deletePath(getCalendarCollectionRoot() + "/" + uid + ".ics") ;
			return ;
		}
		throw new CalDAV4JException("Item not found" + getCalendarCollectionRoot() + "/" + uid + ".ics") ;
	}
    
	/**
	 * crea una nuova cartella in path
	 * @param path
	 * @return 0 if ok statusCode on error
	 * @throws Exception
	 */
	public int mkDirectory(String path)
	{
		MkCalendarMethod mk = new MkCalendarMethod("") ;
		mk.setPath(path) ;
       
		try {
			client.executeMethod(getHostConfiguration(), mk) ;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
        
		int statusCode = mk.getStatusCode() ;
		return  ( statusCode == CaldavStatus.SC_CREATED)  ? 0 : statusCode ;   
	}
        
	/**
	 * list file/folders 
	 * @param path
	 * @return
	 * @throws IOException 
	 */
	public  int listCalendar(String path) throws IOException 
	{
		//now let's try and get it, make sure it's there
		// GetMethod get = new GetMethod();
		GetMethod get = getMethodFactory().createGetMethod() ;
		get.setPath(path) ;
        
		try {
			client.executeMethod(getHostConfiguration(), get) ;
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}

		System.out.println( "listCalendar()" +get.getResponseBodyAsString() );

		int statusCode = get.getStatusCode() ;
		return  ( statusCode == CaldavStatus.SC_OK)  ? 0 : statusCode ;      
	}    
   
	/** 
	 * Save an Event iCalendar component and returns its UID
	 * 
	 * @param event The event to save
	 * @return The attributed UID if successful, <code>""</code> if not
	 * @throws CalDAV4JException 
	 */
	public String saveEvent(final VEvent event) throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return add(client, event, null) ;
	}
	
	/** 
	 * Save an Availability iCalendar component and returns its UID
	 * 
	 * @param availability The Availability iCalendar component to save
	 * @return The attributed UID if successful, <code>""</code> if not
	 * @throws CalDAV4JException 
	 */
	public String saveAvailability(final VAvailability availability) throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return add(client, availability, null) ;
	}
	
	/** 
	 * Update an Availability iCalendar component
	 * 
	 * @param availability The Availability iCalendar component to save
	 * @return <code>true</code> if successful, <code>false</code> if not
	 * @throws CalDAV4JException 
	 */
	public boolean updateAvailability(final VAvailability availability) throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return update(client, availability, null) ;
	}
	
	/**
	  * Create a PUT method setting If-None-Match: * 
	  * this tag causes PUT fails if a given event exist   
	  * 
	  * RFC 4791
	  * If the client intends to create a new non-collection resource, such as a new VEVENT,
	  * the client SHOULD use the HTTP request header "If-None-Match: *" on the PUT request.
	  * The Request-URI on the PUT request MUST include the target collection, where the 
	  * resource is to be created, plus the name of the resource in the last path segment.
	  * The "If-None-Match: *" request header ensures that the client will not inadvertently
	  * overwrite an existing resource if the last path segment turned out to already be used.
	  * 
	  * @param resourceName 
	  * @param calendar 
	  * @return a PutMethod for creating events 
	  */ 
	PutMethod createPutMethodForNewResource(String resourceName, Calendar calendar) 
	{ 
		PutMethod putMethod = methodFactory.createPutMethod() ; 

		putMethod.setPath(getCalendarCollectionRoot() + "/" + resourceName) ; 
		putMethod.setAllEtags(true) ; 
		putMethod.setIfNoneMatch(true) ; 
		putMethod.setRequestBody(calendar) ;
		
		return putMethod ; 
	 } 
	
	/**
	  * Create a PUT method setting If-None-Match: * 
	  * this tag causes PUT fails if a given event exist   
	  * 
	  * RFC 4791
	  * If the client intends to create a new non-collection resource, such as a new VEVENT,
	  * the client SHOULD use the HTTP request header "If-None-Match: *" on the PUT request.
	  * The Request-URI on the PUT request MUST include the target collection, where the 
	  * resource is to be created, plus the name of the resource in the last path segment.
	  * The "If-None-Match: *" request header ensures that the client will not inadvertently
	  * overwrite an existing resource if the last path segment turned out to already be used.
	  * 
	  * @param resourceName 
	  * @param calendar 
	  * @return a PutMethod for creating events 
	  */ 
	PutMethod createPutMethodForResourceUpdate(String resourceName, Calendar calendar) 
	{ 
		PutMethod putMethod = methodFactory.createPutMethod() ;

		putMethod.setPath(getCalendarCollectionRoot() + "/" + resourceName) ;
		putMethod.setAllEtags(true) ;
		putMethod.setIfNoneMatch(false) ;
		putMethod.setRequestBody(calendar) ;
		
		return putMethod ; 
	 }
	
	/**
	  * Adds a new Calendar with the given Component and VTimeZone to the collection. 
	  *  
	  * Tries to use the event UID followed by ".ics" as the name of the  
	  * resource, otherwise will use the UID followed by a random number and  
	  * ".ics"  
	  *  
	  * @param httpClient the httpClient which will make the request 
	  * @param vevent The VEvent to put in the Calendar 
	  *  
	  * @param timezone The VTimeZone of the VEvent if it references one,  
	  *                 otherwise null 
	  * @throws CalDAV4JException 
	  * @todo specify somewhere the kind of caldav error... 
	  */ 
	protected String add(HttpClient httpClient, CalendarComponent component, VTimeZone timezone) throws CalDAV4JException 
	{ 
	  Calendar calendar = new Calendar() ; 
	  // calendar.getProperties().add(new ProdId(prodId)) ; 
	  calendar.getProperties().add(Version.VERSION_2_0) ; 
	  calendar.getProperties().add(CalScale.GREGORIAN) ;
	  
	  if (null != timezone) 
	   calendar.getComponents().add(timezone) ; 
	  
	  calendar.getComponents().add(component); 
	 
	  // 
	  // retry 3 times while caldav server returns PRECONDITION_FAILED 
	  // 
	  boolean bDidIt = false ;
	  
	  Random random = new Random() ;
	  String resourceName = "" ;
	  
	  for (int x = 0; (x < 3) && (false == bDidIt); x++) 
	  {
	  	String sUid = ICalendarUtils.getUIDValue(component) ;
	  	if (null == sUid)
	  		sUid = "" ;
	 
	  	// change UID at second attempt 
	  	if ((x > 0) || "".equals(sUid)) 
	  	{ 
	  		sUid += "-" + random.nextInt() ;     
	  		ICalendarUtils.setUIDValue(calendar, sUid) ;
	  	} 
	  	resourceName = sUid + ".ics" ; 
	 
	  	PutMethod putMethod = createPutMethodForNewResource(resourceName, calendar) ; 
	  	try 
	  	{ 
	  		httpClient.executeMethod(getHostConfiguration(), putMethod) ; 
	    
	  		//fixed for nullpointerexception 
	  		// A caldav server should always return a valid ETAG for given event, but google doesn't 
	  		String etag = StringUtils.defaultString(UrlUtils.getHeaderPrettyValue(putMethod, HEADER_ETAG),"") ;  
	  		CalDAVResource calDAVResource = new CalDAVResource(calendar, etag, getHref((putMethod.getPath()))) ; 
	 
	  		cache.putResource(calDAVResource) ; 
	 
	  	} catch (Exception e) { 
	  		throw new CalDAV4JException("Trouble executing PUT", e) ; 
	  	} 
	 
	  	int statusCode = putMethod.getStatusCode() ;    
	  	switch (statusCode) { 
	  		case CaldavStatus.SC_CREATED: 
	  		case CaldavStatus.SC_NO_CONTENT: 
	  			bDidIt = true ; 
	  			break ;
	  		case CaldavStatus.SC_PRECONDITION_FAILED: 
	  			// event not added, retry 
	  			break ;    
	  		default: 
	  			MethodUtil.StatusToExceptions(putMethod); 
	  	} // switch 
	  }
	  
	  return resourceName ;
	} 
	
	/**
	  * Update a Calendar with the given Component and VTimeZone to the collection. 
	  *  
	  * @param httpClient the httpClient which will make the request 
	  * @param vevent The VEvent to put in the Calendar 
	  * @param timezone The VTimeZone of the VEvent if it references one, otherwise null
	  * @return <code>true</code> if successful, <code>false</code> if not 
	  * @throws CalDAV4JException 
	  * @todo specify somewhere the kind of caldav error... 
	  */ 
	protected boolean update(HttpClient httpClient, CalendarComponent component, VTimeZone timezone) throws CalDAV4JException 
	{ 
		String sUid = ICalendarUtils.getUIDValue(component) ;
  	if (null == sUid)
  		throw new CalDAV4JException("Cannot update a component with no UID") ;
		
	  Calendar calendar = new Calendar() ; 
	  // calendar.getProperties().add(new ProdId(prodId)) ; 
	  calendar.getProperties().add(Version.VERSION_2_0) ; 
	  calendar.getProperties().add(CalScale.GREGORIAN) ;
	  
	  if (null != timezone) 
	   calendar.getComponents().add(timezone) ; 
	  
	  calendar.getComponents().add(component) ;
	 
	  String resourceName = sUid + ".ics" ;
	  PutMethod putMethod = createPutMethodForResourceUpdate(resourceName, calendar) ; 
	  try 
	  { 
	  	httpClient.executeMethod(getHostConfiguration(), putMethod) ; 
	    
	  	//fixed for nullpointerexception 
	  	// A caldav server should always return a valid ETAG for given event, but google doesn't 
	  	String etag = StringUtils.defaultString(UrlUtils.getHeaderPrettyValue(putMethod, HEADER_ETAG),"") ;  
	  	CalDAVResource calDAVResource = new CalDAVResource(calendar, etag, getHref((putMethod.getPath()))) ; 
	 
	  	cache.putResource(calDAVResource) ; 
	 
	  } catch (Exception e) { 
	  	throw new CalDAV4JException("Trouble executing PUT", e) ; 
	  } 
	 
	  int statusCode = putMethod.getStatusCode() ;    
	  switch (statusCode) { 
	  	case CaldavStatus.SC_OK: 
	  	case CaldavStatus.SC_ACCEPTED: 
	  		return true ;
	  	default: 
	  		return false ; 
	  } // switch 
	}
	
	/** 
	 * Save an event and returns its UID
	 * 
	 * @param event The event to save
	 * @return The attributed UID if successful, <code>""</code> if not
	 * @throws CalDAV4JException 
	 */
	public String saveEvent(HttpClient client, final VEvent event) throws CalDAV4JException
	{
		PutMethod method = methodFactory.createPutMethod() ; 
	  method.setRequestBody(event) ; 
	   
		try {
			client.executeMethod(getHostConfiguration(), method) ;
		} catch (Exception he) {
			throw new CalDAV4JException("Problem executing method", he) ;
		}
		
		try {
			return method.getResponseBodyAsString() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "" ;
	}
	
	/** 
	 * Get a collection of events in the given time interval
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return a collection of events in the given time-interval
	 * @throws CalDAV4JException 
	 */
	public List<Calendar> getEventResources(Date beginDate, Date endDate) throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return getEventResources(client, beginDate, endDate) ;
	}

	/**
	 * @param beginDate First day of the interval to get events for
	 * @param endDate   Last day, of <code>null</code> is the interval is limited to beginDate
	 * 
	 * @return The list of events for the time interval, of <code>null</code> is something went wrong
	 * 
	 * @throws CalDAV4JException 
	 */
	public List<Calendar> getEventResourcesForInterval(final LdvTime beginDate, final LdvTime endDate) throws CalDAV4JException 
	{
		if ((null == beginDate) || beginDate.isNoLimit())
			return null ;
		
		// Warning: months are zero indexed (eg. January is '0').
		//
		Date dBeginDate = ICalendarUtils.createDateTime(beginDate.getUTCFullYear(), beginDate.getUTCMonth() - 1, beginDate.getUTCDate(), null, true) ; // uses no timezone, and UTC

		if (null == endDate)
		{
			LdvTime beginDatePlusOne = new LdvTime(0) ;
			beginDatePlusOne.initFromLdvTime(beginDate) ;
			beginDatePlusOne.addDays(1, true) ;
			
			Date dBeginDatePlusOne = ICalendarUtils.createDateTime(beginDatePlusOne.getUTCFullYear(), beginDatePlusOne.getUTCMonth() - 1, beginDatePlusOne.getUTCDate(), null, true) ; // uses no timezone, and UTC
			
			return getEventResources(dBeginDate, dBeginDatePlusOne) ;
		}
		
		if (endDate.isNoLimit())
			return null ; // TODO change this by getting all events from beginDate
				
		Date dEndDate = ICalendarUtils.createDateTime(endDate.getUTCFullYear(), endDate.getUTCMonth() - 1, endDate.getUTCDate(), null, true) ; // uses no timezone, and UTC
    	
		return getEventResources(dBeginDate, dEndDate) ;
	}
	
	/**
	 * @param y year
	 * @param m month
	 * @return a list of event in the given month/year
	 * @throws CalDAV4JException 
	 */
	public List<Calendar> getEventResourcesByMonth(int y, int m) throws CalDAV4JException 
	{
		Date beginDate = ICalendarUtils.createDateTime(y, m, 0, null, true) ; // uses no timezone, and UTC
    	
		GregorianCalendar c = new GregorianCalendar() ;
		c.setTime(beginDate) ;
		c.add(GregorianCalendar.MONTH, 1) ;
    	
		Date endDate = ICalendarUtils.createDateTime(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DATE), null, true) ;
    	
		return getEventResources(beginDate, endDate) ;
	}
	
	/**
	 * Returns all Calendars which contain events which have instances who fall within 
	 * the two dates. Note that recurring events are NOT expanded. 
	 * 
	 * @param httpClient the httpClient which will make the request
	 * @param beginDate the beginning of the date range. Must be a UTC date
	 * @param endDate the end of the date range. Must be a UTC date.
	 * @return a List of Calendars
	 * @throws CalDAV4JException if there was a problem
	 * 
	 * Query will be of the kind:
	 * 
	 * <?xml version="1.0" encoding="utf-8" ?>
	 * <C:calendar-query xmlns:D="DAV:" xmlns:C="urn:ietf:params:xml:ns:caldav">
	 *   <D:prop>
	 *     <D:getetag/>
	 *     <C:calendar-data/>
	 *   </D:prop>
	 *   <C:filter>
	 *     <C:comp-filter name="VCALENDAR">
	 *       <C:comp-filter name="VEVENT">
	 *         <C:time-range start="20060104T000000Z" end="20060105T000000Z"/>
	 *       </C:comp-filter>
	 *     </C:comp-filter>
	 *   </C:filter>
	 * </C:calendar-query>
	 */
	public List<Calendar> getEventResources(HttpClient httpClient, Date beginDate, Date endDate) throws CalDAV4JException 
	{
		// first create the calendar query
		// CalendarQuery query = new CalendarQuery("C", "D");
		CalendarQuery query = new CalendarQuery() ;

		query.addProperty(CalDAVConstants.DNAME_GETETAG) ;
		
		// This adds the "<C:calendar-data/>" part in order to get all properties
		//
		CalendarData calendarData = new CalendarData();
		query.setCalendarDataProp(calendarData);
		
		// Add a time range 
		//
		CompFilter vCalendarCompFilter = new CompFilter(Calendar.VCALENDAR) ;

		CompFilter vEventCompFilter = new CompFilter(Component.VEVENT) ;
		vEventCompFilter.setTimeRange(new TimeRange(beginDate, endDate)) ;

		vCalendarCompFilter.addCompFilter(vEventCompFilter) ;
		
		query.setCompFilter(vCalendarCompFilter) ;

		return getComponentByQuery(httpClient, Component.VEVENT, query) ;
	}
	
	/**
	 * @param beginDate First day of the interval to get events for
	 * @param endDate   Last day, of <code>null</code> is the interval is limited to beginDate
	 * 
	 * @return The list of events for the time interval, of <code>null</code> is something went wrong
	 * 
	 * @throws CalDAV4JException 
	 */
	public List<String> getEventUIDsForInterval(final LdvTime beginDate, final LdvTime endDate) throws CalDAV4JException 
	{
		if ((null == beginDate) || beginDate.isNoLimit())
			return null ;
		
		// Warning: months are zero indexed (eg. January is '0').
		//
		Date dBeginDate = ICalendarUtils.createDateTime(beginDate.getUTCFullYear(), beginDate.getUTCMonth() - 1, beginDate.getUTCDate(), null, true) ; // uses no timezone, and UTC

		if (null == endDate)
			return getEventUIDs(dBeginDate, dBeginDate) ;
		
		if (endDate.isNoLimit())
			return null ; // TODO change this by getting all events from beginDate
				
		Date dEndDate = ICalendarUtils.createDateTime(endDate.getUTCFullYear(), endDate.getUTCMonth() - 1, endDate.getUTCDate(), null, true) ; // uses no timezone, and UTC
    	
		return getEventUIDs(dBeginDate, dEndDate) ;
	}
	
	/**
	 * @param y year
	 * @param m month
	 * @return a list of event in the given month/year
	 * @throws CalDAV4JException 
	 */
	public List<String> getEventUIDsByMonth(int y, int m) throws CalDAV4JException 
	{
		Date beginDate = ICalendarUtils.createDateTime(y, m, 0, null, true) ; // uses no timezone, and UTC
    	
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(beginDate) ;
		c.add(GregorianCalendar.MONTH, 1) ;
    	
		Date endDate = ICalendarUtils.createDateTime(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DATE), null, true) ;
    	
		return getEventUIDs(beginDate, endDate);
	}
	
	/**
	 * @param y year
	 * @param m month
	 * @return a list of event in the given month/year
	 * @throws CalDAV4JException 
	 */
	public CalendarQuery getQueryUIDsByMonth(int y, int m) throws CalDAV4JException 
	{
		Date beginDate = ICalendarUtils.createDateTime(y, m, 0, null, true) ; // uses no timezone, and UTC
    	
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(beginDate) ;
		c.add(GregorianCalendar.MONTH, 1) ;
    	
		Date endDate = ICalendarUtils.createDateTime(c.get(GregorianCalendar.YEAR), c.get(GregorianCalendar.MONTH), c.get(GregorianCalendar.DATE), null, true) ;
    	
		return getQueryUIDs(beginDate, endDate);
	}
    
	/** sort a Calendar list by date
	 * 
	 * @param calendars
	 * @return
	 */
	public List<Calendar> sortByStartDate(List<Calendar> calendars) 
	{
		Collections.sort(calendars, new CalendarComparator()) ;
		return calendars ;	
	}
	
	/**
	 * 
	 * @return VEvent marked with an UID if success, null on error
	 * TODO should throw more exceptions
	 * @throws CalDAV4JException 
	 */
	
/*
	public VEvent addEvent(VEvent ve, VTimeZone vtz) throws CalDAV4JException  
	{
		if (ve.getProperty(Property.UID) == null) 
		{
			Uid uid = new Uid( new DateTime().toString()
					+"-" + UUID.randomUUID().toString()
					+"-" + getUsername() ) ;
			
			ve.getProperties().add(uid) ; 
		}

		addEvent(client, ve, null) ;
		return ve ;
	}
*/
	
	/**
	 * modify an event with the same Uid of the given one
	 * @param ve
	 * @param vtz
	 * @return ve on success, null on failure
	 * @throws ValidationException 
	 */
	
/*
	public VEvent editEvent(VEvent ve, VTimeZone vtz) throws ValidationException
	{
		try {
			ICalendarUtils.addOrReplaceProperty(ve, new DtStamp()) ;
			ve.validate() ;
			updateMasterEvent(client, ve, vtz) ;
			return ve ;
		} catch (CalDAV4JException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
*/
    
	/** sets Collection home .. ;) */
	public void setCalendarCollectionRoot(String path) 
	{
		if (null == path) 
			path = getRoot() ;
		 
		super.setCalendarCollectionRoot(path);
	}
	
	public void setCalendarCollectionRoot() {
		setCalendarCollectionRoot(null);
	}
    
	public String getCalendarCollectionRoot() {
		return super.getCalendarCollectionRoot();
	}
    
	public void setRelativePath(String path) 
	{
		String home = getRoot() ;
		
		if (null == path)
			path = home ;
		else
			home += path ;
    	
		super.setCalendarCollectionRoot(home) ;
	}
	
	protected String getRoot()
	{
		if (null == client)
			return "" ;
		
		String sRoot = client.getCalDavSeverWebDAVRoot() ;
		
		if (client.mustAddUserToRoot())
			sRoot += "/" + getUsername() + "/" ;
		
		return sRoot ;
	}
    
	/**
	 * @see super()
	 * @param uid
	 * @return Calendar if object exists, null if not exists
	 * 
	 * @throws CalDAV4JException
	 */
	public Calendar getCalendarForEventUID(String uid) throws CalDAV4JException, ResourceNotFoundException {
		// return super.getCalendarForEventUID(client, uid) ;
		return getCalendarForEventUID(client, uid) ;
	}

	/**
	 * Returns the icalendar object which contains the event with the specified
	 * UID.
	 * 
	 * @param httpClient the httpClient which will make the request
	 * @param uid The uniqueID of the event to find
	 * @return the Calendar object containing the event with this UID
	 * @throws CalDAV4JException if there was a problem, or if the resource could 
	 *         not be found.
	 */
	public Calendar getCalendarForEventUID(HttpClient httpClient, String uid)
	throws CalDAV4JException, ResourceNotFoundException {
		return getCalDAVResourceForEventUID(httpClient, uid).getCalendar();
	}
	
	/** 
	 * @param beginDate
	 * @param endDate
	 * @return a collection of events in the given time-interval
	 * @throws CalDAV4JException 
	 */
	public List<String> getEventUIDs(Date beginDate, Date endDate) throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return getEventPropertyByTimestamp(client, "UID", beginDate, endDate) ;
	}
	
	/** 
	 * @param beginDate
	 * @param endDate
	 * @return a collection of events in the given time-interval
	 * @throws CalDAV4JException 
	 */
	public CalendarQuery getQueryUIDs(Date beginDate, Date endDate) throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return getQueryPropertyByTimestamp(client, "UID", beginDate, endDate) ;
	}
	
	/**
	 * @param httpClient the httpClient which will make the request
	 * @param propertyName the iCalendar property name ex. Property.UID @see Property
	 * @param beginDate the beginning of the date range. Must be a UTC date
	 * @param endDate the end of the date range. Must be a UTC date.
	 * @return a List of Property values (eg. a List of VEVENT UIDs
	 * @throws CalDAV4JException if there was a problem
	 */
	public CalendarQuery getQueryPropertyByTimestamp(HttpClient httpClient, String propertyName, Date beginDate, Date endDate)
			throws CalDAV4JException 
	{
		// first create the calendar query
		CalendarQuery query = new CalendarQuery() ;

		query.addProperty(CalDAVConstants.DNAME_GETETAG) ;

		// create the query fields 
		CalendarData calendarData = new CalendarData() ;

		Comp vCalendarComp = new Comp();
		vCalendarComp.setName(Calendar.VCALENDAR);

		Comp vEventComp = new Comp();
		vEventComp.setName(Component.VEVENT);
		vEventComp.addProp(new CalDAVProp(propertyName, false, false)); // @see modification to CalDAVProp

		List <Comp> comps = new ArrayList<Comp> ();
		comps.add(vEventComp);
		vCalendarComp.setComps(comps);
		calendarData.setComp(vCalendarComp);
		query.setCalendarDataProp(calendarData);

		// search for events matching...
		CompFilter vCalendarCompFilter = new CompFilter("C");
		vCalendarCompFilter.setName(Calendar.VCALENDAR);

		CompFilter vEventCompFilter = new CompFilter("C");
		vEventCompFilter.setName(Component.VEVENT);

		// TODO check the support from the caldav server. bedework is ok
		// XXX if endDate is undefined, check into ine year
		PropFilter pFilter = new PropFilter();
		pFilter.setName("DTSTAMP");
		if (endDate == null) {
			endDate = new DateTime(beginDate.getTime()+86400*364);
			((DateTime)endDate).setUtc(true);
		}
		pFilter.setTimeRange(beginDate, endDate);

		vEventCompFilter.addPropFilter(pFilter);
		vCalendarCompFilter.addCompFilter(vEventCompFilter);
		query.setCompFilter(vCalendarCompFilter);

		CalDAVReportMethod reportMethod = methodFactory.createCalDAVReportMethod("");
		reportMethod.setPath(getCalendarCollectionRoot());
		try {
			reportMethod.setReportRequest(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return query ;
	}
 
	/**
	 * @see super
	 * @param propertyName
	 * @param beginDate
	 * @param endDate
	 * @throws CalDAV4JException
	 * @Deprecated  {@link getComponentPropertyByTimestamp} 
	 */
	public List <String> getEventPropertyByTimestamp(String propertyName, Date beginDate, Date endDate) throws CalDAV4JException 
	{
		// return super.getEventPropertyByTimestamp(client, propertyName, beginDate, endDate);
		return getEventPropertyByTimestamp(client, propertyName, beginDate, endDate);
	}
    
	/**
	 * see super
	 * @param componentName
	 * @param propertyName
	 * @param propertyFilter
	 * @param beginDate
	 * @param endDate
	 * @throws CalDAV4JException if can't connect
	 */
	public List <String> getComponentPropertyByTimestamp(String componentName, String propertyName, String propertyFilter, Date beginDate, Date endDate)
    	throws CalDAV4JException 
	{
		// return super.getComponentPropertyByTimestamp(client, componentName, propertyName, propertyFilter, beginDate, endDate);
		return getComponentPropertyByTimestamp(client, componentName, propertyName, propertyFilter, beginDate, endDate);
	}
    
	/**
	 * 
	 * @param uid
	 * @return
	 * @throws CalDAV4JException if can't connect
	 * @throws ResourceNotFoundException if can't find object
	 * @see super
	 */
	public String getPathToResourceForEventId(String uid) 
		throws CalDAV4JException, ResourceNotFoundException {
			// return  super.getPathToResourceForEventId(client, uid);
		return getPathToResourceForEventId(client, uid);
	}
	
	public List<Calendar> getComponentByQuery(HttpClient httpClient, String componentName, CalendarQuery query) throws CalDAV4JException 
	{
		CalDAVReportMethod reportMethod = methodFactory.createCalDAVReportMethod("");
		reportMethod.setPath(getCalendarCollectionRoot());
		try {
			reportMethod.setReportRequest(query);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			httpClient.executeMethod(getHostConfiguration(), reportMethod);
		} catch (Exception he) {
			throw new CalDAV4JException("Problem executing method", he);
		}

		try 
		{
			MultiStatusResponse[] responses = reportMethod.getResponseBodyAsMultiStatus().getResponses() ;
			
			if ((null != responses) && reportMethod.succeeded()) 
	    {
				List<Calendar> list = new ArrayList<Calendar>() ;
				
	    	for (MultiStatusResponse r : responses) 
	    	{
	    		if (r.isPropStat())
	    		{
	    			Calendar calendar = CalendarDataProperty.getCalendarfromResponse(r) ;
	    			
	    			if (null != calendar)
	    				list.add(calendar) ;
	    		}
	    	}
	    	
	    	return list ;
	    }
		} catch (IOException | DavException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

/*
		Enumeration<CalDAVResponse> e = reportMethod.getResponses();
		List<Calendar> list = new ArrayList<Calendar>();
		while (e.hasMoreElements()){
			CalDAVResponse response  = e.nextElement();
			String etag = response.getETag();
			CalDAVResource resource = getCalDAVResource(httpClient, stripHost(response.getHref()), etag);
			list.add(resource.getCalendar());
		}
*/

		return null ;
	}
	
	/**
	 * Returns all Calendars (VCALENDAR/VEVENTS) which contain events with DTSTAMP between beginDate - endDate
	 * TODO Note that recurring events are NOT expanded. 
	 * TODO we can parametrize too the Component.VEVENT field to get a more flexible method
	 * TODO This method doesn't use cache
	 *   the search is the following...
<C:calendar-query xmlns:C="urn:ietf:params:xml:ns:caldav">
       <D:prop xmlns:D="DAV:">
           <D:getetag/>
           <C:calendar-data>
               <C:comp name="VCALENDAR">
                   <C:comp name="VEVENT">
                       <C:prop name="UID"/>
                   </C:comp>
               </C:comp>
           </C:calendar-data>
       </D:prop>
       <C:filter>
           <C:comp-filter name="VCALENDAR">
               <C:comp-filter name="VEVENT">
               		<C:time-range end="20081210T000000Z" start="20080607T000000Z"/> XXX
                   <C:prop-filter name="DTSTAMP">
                       <C:time-range end="20071210T000000Z" start="20070607T000000Z"/>
                   </C:prop-filter>
               </C:comp-filter>
           </C:comp-filter>
       </C:filter>
   </C:calendar-query>

	 * @param httpClient the httpClient which will make the request
	 * @param propertyName the iCalendar property name ex. Property.UID @see Property
	 * @param beginDate the beginning of the date range. Must be a UTC date
	 * @param endDate the end of the date range. Must be a UTC date.
	 * @return a List of Property values (eg. a List of VEVENT UIDs
	 * @throws CalDAV4JException if there was a problem
	 */
	public List<String> getEventPropertyByTimestamp(HttpClient httpClient, String propertyName, Date beginDate, Date endDate) throws CalDAV4JException 
	{
		// first create the calendar query
		CalendarQuery query = new CalendarQuery();

		query.addProperty(CalDAVConstants.DNAME_GETETAG);

		// create the query fields 
		CalendarData calendarData = new CalendarData();

		Comp vCalendarComp = new Comp("C");
		vCalendarComp.setName(Calendar.VCALENDAR);

		Comp vEventComp = new Comp("C");
		vEventComp.setName(Component.VEVENT);
		vEventComp.addProp(new CalDAVProp(propertyName, false, false)); // @see modification to CalDAVProp  

		List <Comp> comps = new ArrayList<Comp> ();
		comps.add(vEventComp);
		vCalendarComp.setComps(comps);
		calendarData.setComp(vCalendarComp);
		query.setCalendarDataProp(calendarData);

		// search for events matching...
		CompFilter vCalendarCompFilter = new CompFilter("C");
		vCalendarCompFilter.setName(Calendar.VCALENDAR);

		CompFilter vEventCompFilter = new CompFilter("C");
		vEventCompFilter.setName(Component.VEVENT);

		// TODO check the support from the caldav server. bedework is ok
		// XXX if endDate is undefined, check into ine year
/*
		PropFilter pFilter = new PropFilter();
		pFilter.setName("DTSTAMP");
		if (endDate == null) {
			endDate = new DateTime(beginDate.getTime()+86400*364);
			((DateTime)endDate).setUtc(true);
		}
		pFilter.setTimeRange(beginDate, endDate);

		vEventCompFilter.addPropFilter(pFilter);
*/
		vCalendarCompFilter.addCompFilter(vEventCompFilter);
		query.setCompFilter(vCalendarCompFilter);

		CalDAVReportMethod reportMethod = methodFactory.createCalDAVReportMethod("");
		reportMethod.setPath(getCalendarCollectionRoot());
		try {
			reportMethod.setReportRequest(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getComponentPropertyByQuery(httpClient, Component.VEVENT, propertyName, query);
	}

	/**
	 * Returns all Calendars (VCALENDAR/VEVENTS) which contain events with DTSTAMP between beginDate - endDate
	 * TODO Note that recurring events are NOT expanded. 
	 * TODO we can parametrize too the Component.VEVENT field to get a more flexible method
	 * TODO This method doesn't use cache
	 *   the search is the following...
<C:calendar-query xmlns:C="urn:ietf:params:xml:ns:caldav">
       <D:prop xmlns:D="DAV:">
           <D:getetag/>
           <C:calendar-data>
               <C:comp name="VCALENDAR">
                   <C:comp name="VEVENT">
                       <C:prop name="UID"/>
                   </C:comp>
               </C:comp>
           </C:calendar-data>
       </D:prop>
       <C:filter>
           <C:comp-filter name="VCALENDAR">
               <C:comp-filter name="VEVENT">
                   <C:prop-filter name="DTSTAMP">
                       <C:time-range end="20071210T000000Z" start="20070607T000000Z"/>
                   </C:prop-filter>
               </C:comp-filter>
           </C:comp-filter>
       </C:filter>
   </C:calendar-query>

	 * @param httpClient the httpClient which will make the request
	 * @param componentName the iCalendar component name ex. Component.VEVENT @see Property
	 * @param propertyName the iCalendar property name ex. Property.UID @see Property
	 * @param propertyFilter the iCalendar property key name ex. Property.DTSTAMP Property.LAST-MODIFIED @see Property
	 * @param beginDate the beginning of the date range. Must be a UTC date
	 * @param endDate the end of the date range. Must be a UTC date.
	 * @return a List of Property values (eg. a List of VEVENT UIDs
	 * @throws CalDAV4JException if there was a problem
	 * XXX check if you must cast Date to DateTime
	 */
	public List<String> getComponentPropertyByTimestamp(HttpClient httpClient, String componentName, String propertyName, String propertyFilter,
			Date beginDate, Date endDate)
			throws CalDAV4JException {
		// first create the calendar query
		CalendarQuery query = new CalendarQuery();

		query.addProperty(CalDAVConstants.DNAME_GETETAG);

		// create the query fields 
		CalendarData calendarData = new CalendarData();

		Comp vCalendarComp = new Comp("C");
		vCalendarComp.setName(Calendar.VCALENDAR);

		Comp vEventComp = new Comp("C");
		vEventComp.setName(componentName);
		vEventComp.addProp(new CalDAVProp(propertyName, false, false)); // @see modification to CalDAVProp  

		List <Comp> comps = new ArrayList<Comp> ();
		comps.add(vEventComp);
		vCalendarComp.setComps(comps);
		calendarData.setComp(vCalendarComp);
		query.setCalendarDataProp(calendarData);

		// search for events matching...
		CompFilter vCalendarCompFilter = new CompFilter("C");
		vCalendarCompFilter.setName(Calendar.VCALENDAR);

		CompFilter vEventCompFilter = new CompFilter("C");
		vEventCompFilter.setName(componentName);

		// TODO check the support from the caldav server. bedework is ok
		// XXX if endDate is undefined, set it one year later
		// set the filter name Property.LAST_MODIFIED
		PropFilter pFilter = new PropFilter();
		pFilter.setName(propertyFilter);
		if (endDate == null) {
			endDate = new DateTime(beginDate.getTime()+86400*364);
			((DateTime)endDate).setUtc(true);
		}
		pFilter.setTimeRange(beginDate, endDate);

		vEventCompFilter.addPropFilter(pFilter);
		vCalendarCompFilter.addCompFilter(vEventCompFilter);
		query.setCompFilter(vCalendarCompFilter);

		return getComponentPropertyByQuery(httpClient, componentName, propertyName, query);
	}
	
	protected List<String> getComponentPropertyByQuery(HttpClient httpClient, String componentName, String propertyName, CalendarQuery query) throws CalDAV4JException 
	{
		CalDAVReportMethod reportMethod = methodFactory.createCalDAVReportMethod("");
		reportMethod.setPath(getCalendarCollectionRoot());
		try {
			reportMethod.setReportRequest(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			httpClient.executeMethod(getHostConfiguration(), reportMethod);
		} catch (Exception he) {
			he.printStackTrace();
			throw new CalDAV4JException("Problem executing method", he);
		}

		List<String> propertyList = new ArrayList<String>();
		
		MultiStatusResponse[] responses;
		try {
			responses = reportMethod.getResponseBodyAsMultiStatus().getResponses();
			
			if (responses != null && reportMethod.succeeded()) 
	    {
	    	for (MultiStatusResponse r : responses) 
	    	{
	    		if (r.isPropStat())
	    		{
	    			Calendar calendar = CalendarDataProperty.getCalendarfromResponse(r) ;
	    			
	    			if (null != calendar)
	    			{
	    				ComponentList<CalendarComponent> components = calendar.getComponents() ;
	    				
	    				for (Component component : components)
	    				{
	    					// The calendar components are several calendar properties which create a calendar schematic (design). 
	    					// 
	    					// For example, the calendar component can specify an event, a to-do list, a journal entry, time zone information, 
	    					// or free/busy time information, or an alarm.
	    					//
	    					if (Component.VEVENT.equals(component.getName()))
	    					{
	    						Property prop = component.getProperty("UID") ;
	    						if (null != prop)
	    							propertyList.add(prop.getValue()) ;
	    					}
	    				}
	    				
	    				Property property = calendar.getProperty("UID") ;
	    				if (null != property)
	    					propertyList.add(property.getValue()) ;
	    			}
/*
	    			DavPropertySet props = r.getProperties(CaldavStatus.SC_OK) ;
	    			
	    			// Get the "calendar-data" information
	    			//
	    			DavProperty property = props.get(CALDAV_CALENDAR_DATA) ;
	    			if (null != property)
	    			{
	    				DavProperty value_property = (DavProperty) property.getValue() ;
	    				propertyList.add(property.getName().getName()) ;
	    			}
*/
	    		}
	    	}
	    }
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DavException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
		
/*
		Enumeration<CalDAVResponse> e = reportMethod.getResponses();
		while (e.hasMoreElements()){
			CalDAVResponse response  = e.nextElement();
			Calendar cal =  response.getCalendar();
			if (cal.getComponent(componentName) != null) {
				propertyList.add( cal.getComponent(componentName)
						.getProperty(propertyName).getValue() );
			}
		}
*/
//			former code with cache
//			String etag = response.getETag();
//			CalDAVResource resource = getCalDAVResource(httpClient,
//			stripHost(response.getHref()), etag);
//			list.add(resource.getCalendar());
		

		return propertyList;
	}
	
	/**
	 * Returns the path to the resource that contains the VEVENT with the
	 * specified uid
	 * 
	 * @param uid
	 */
	protected String getPathToResourceForEventId(HttpClient httpClient, String uid) throws CalDAV4JException
	{
		// first create the calendar query
		CalendarQuery query = new CalendarQuery();

		query.addProperty(CalDAVConstants.DNAME_GETETAG);

		CompFilter vCalendarCompFilter = new CompFilter("C");
		vCalendarCompFilter.setName(Calendar.VCALENDAR);

		CompFilter vEventCompFilter = new CompFilter("C");
		vEventCompFilter.setName(Component.VEVENT);

		PropFilter propFilter = new PropFilter();
		propFilter.setName(Property.UID);
		propFilter.setTextMatch(new TextMatch(false, null, null, uid)) ;
		vEventCompFilter.addPropFilter(propFilter);

		vCalendarCompFilter.addCompFilter(vEventCompFilter);
		query.setCompFilter(vCalendarCompFilter);

		CalDAVReportMethod reportMethod = methodFactory.createCalDAVReportMethod("");
		reportMethod.setPath(getCalendarCollectionRoot());
		try {
			reportMethod.setReportRequest(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			httpClient.executeMethod(getHostConfiguration(), reportMethod);
		} catch (Exception he) {
			throw new CalDAV4JException("Problem executing method", he);
		}

/* TODO recreate this

		Enumeration<CalDAVResponse> e = reportMethod.getResponses();
		if (!e.hasMoreElements()) {
			throw new ResourceNotFoundException(
					ResourceNotFoundException.IdentifierType.UID, uid);
		}

		return stripHost(e.nextElement().getHref());
*/
		return "" ;
	}
	
	
	/**
	 * get a calendar by UID
	 * it tries
	 *  - first by a REPORT
	 *  - then by GET /path
	 * @param httpClient
	 * @param uid
	 * @return
	 * @throws CalDAV4JException
	 * @throws ResourceNotFoundException
	 */
	protected CalDAVResource getCalDAVResourceForEventUID(HttpClient httpClient, String uid) throws CalDAV4JException, ResourceNotFoundException 
	{
		//first check the cache!
		String href = cache.getHrefForEventUID(uid);
		CalDAVResource calDAVResource = null;

		if (href != null) {
			calDAVResource = getCalDAVResource(httpClient, stripHost(href));

			if (calDAVResource != null) {
				return calDAVResource;
			}
		}

		// first create the calendar query
		CalendarQuery query = new CalendarQuery();
		query.setCalendarDataProp(new CalendarData());
		query.addProperty(CalDAVConstants.DNAME_GETETAG);

		CompFilter vCalendarCompFilter = new CompFilter("C");
		vCalendarCompFilter.setName(Calendar.VCALENDAR);

		CompFilter vEventCompFilter = new CompFilter("C");
		vEventCompFilter.setName(Component.VEVENT);

		PropFilter propFilter = new PropFilter();
		propFilter.setName(Property.UID);
		propFilter.setTextMatch(new TextMatch(null, null, null, uid)); // rpolli s/false/null/
		vEventCompFilter.addPropFilter(propFilter);

		vCalendarCompFilter.addCompFilter(vEventCompFilter);
		query.setCompFilter(vCalendarCompFilter);

		CalDAVReportMethod reportMethod = methodFactory.createCalDAVReportMethod("");
		reportMethod.setPath(getCalendarCollectionRoot());
		try {
			reportMethod.setReportRequest(query);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			httpClient.executeMethod(getHostConfiguration(), reportMethod);
		} catch (Exception he) {
			throw new CalDAV4JException("Problem executing method", he);
		}

/* TODO
		Enumeration<CalDAVResponse> e = reportMethod.getResponses();
		if (!e.hasMoreElements()) {
			throw new ResourceNotFoundException(
					ResourceNotFoundException.IdentifierType.UID, uid);
		}

		calDAVResource = new CalDAVResource(e.nextElement());
		cache.putResource(calDAVResource);
*/
		return calDAVResource ;
	}

	/**
	 * Gets the resource at the given path. Will check the cache first, and compare that to the
	 * latest etag obtained using a HEAD request.
	 * @param httpClient
	 * @param path
	 * @return
	 * @throws CalDAV4JException
	 */
	protected CalDAVResource getCalDAVResource(HttpClient httpClient, String path) throws CalDAV4JException 
	{
		String currentEtag = getETag(httpClient, path);
		return getCalDAVResource(httpClient, path, currentEtag);
	}
	
	/**
	 * Gets the resource for the given href. Will check the cache first, and if a cached
	 * version exists that has the etag provided it will be returned. Otherwise, it goes
	 * to the server for the resource.
	 * 
	 * @param httpClient
	 * @param path
	 * @param currentEtag
	 * @return
	 * @throws CalDAV4JException
	 */
	protected CalDAVResource getCalDAVResource(HttpClient httpClient, String path, String currentEtag) throws CalDAV4JException 
	{
		//first try getting from the cache
		CalDAVResource calDAVResource = cache.getResource(getHref(path));

		//ok, so we got the resource...but has it been changed recently?
		if (calDAVResource != null){
			String cachedEtag = calDAVResource.getResourceMetadata().getETag();
			if (cachedEtag.equals(currentEtag)){
				return calDAVResource;
			}
		}

		//either the etag was old, or it wasn't in the cache so let's get it
		//from the server       
		return getCalDAVResourceFromServer(httpClient, path);

	}
	
	/**
	 * Gets a CalDAVResource from the server - in other words DOES NOT check the cache.
	 * Adds the new resource to the cache, replacing any preexisting version.
	 * 
	 * The calendar is Thread-locally build by getResponseBodyAsCalendar()
	 * 
	 * @param httpClient
	 * @param path
	 * @return
	 * @throws CalDAV4JException
	 */
	protected CalDAVResource getCalDAVResourceFromServer(HttpClient httpClient, String path) throws CalDAV4JException 
	{
		CalDAVResource calDAVResource = null;
		GetMethod getMethod = getMethodFactory().createGetMethod();
		getMethod.setPath(path);
		try {
			httpClient.executeMethod(getHostConfiguration(), getMethod);
			if (getMethod.getStatusCode() != CaldavStatus.SC_OK){
				throw new CalDAV4JException(
						"Unexpected Status returned from Server: "
						+ getMethod.getStatusCode());
			}
		} catch (Exception e){
			throw new CalDAV4JException("Problem executing get method",e);
		}

		String href = getHref(path);
		String etag = getMethod.getResponseHeader("ETag").getValue();
		Calendar calendar = null;
		try {
			calendar = getMethod.getResponseBodyAsCalendar();
		} catch (Exception e){
			throw new CalDAV4JException("Malformed calendar resource returned.", e);
		}

		calDAVResource = new CalDAVResource();
		calDAVResource.setCalendar(calendar);
		calDAVResource.getResourceMetadata().setETag(etag);
		calDAVResource.getResourceMetadata().setHref(href);

		cache.putResource(calDAVResource);
		return calDAVResource;
	}
	
	protected String getETag(HttpClient httpClient, String path) throws CalDAV4JException
	{
		HeadMethod headMethod = new HeadMethod(path);

		try {
			httpClient.executeMethod(getHostConfiguration(), headMethod);
			int statusCode = headMethod.getStatusCode();

			if (statusCode == CaldavStatus.SC_NOT_FOUND) {
				throw new ResourceNotFoundException(
						ResourceNotFoundException.IdentifierType.PATH, path);
			}

			if (statusCode != CaldavStatus.SC_OK){
				throw new CalDAV4JException(
						"Unexpected Status returned from Server: "
						+ headMethod.getStatusCode());
			}
		} catch (IOException e){
			throw new CalDAV4JException("Problem executing get method",e);
		}

		Header h = headMethod.getResponseHeader("ETag");
		String etag = null;
		if (h != null) {
			etag = h.getValue();
		}
		return etag;
	}
	
	/** 
	 * Get all Availability components
	 * 
	 * @return a collection of events in the given time-interval
	 * @throws CalDAV4JException 
	 */
	public List<Calendar> getAvailabilityResources() throws CalDAV4JException
	{
		CalDAV4JMethodFactory mf = new CalDAV4JMethodFactory() ;
		setMethodFactory(mf) ;

		return getAvailabilityResources(client) ;
	}
	
	/**
	 * Returns all Calendars which contain Availability ressources. 
	 * 
	 * @param httpClient the httpClient which will make the request
	 * @return a List of Calendars
	 * @throws CalDAV4JException if there was a problem
	 * 
	 * Query will be of the kind:
	 * 
	 * <?xml version="1.0" encoding="utf-8" ?>
	 * <C:calendar-query xmlns:D="DAV:" xmlns:C="urn:ietf:params:xml:ns:caldav">
	 *   <D:prop>
	 *     <D:getetag/>
	 *     <C:calendar-data/>
	 *   </D:prop>
	 *   <C:filter>
	 *     <C:comp-filter name="VCALENDAR">
	 *       <C:comp-filter name="VAVAILABILITY">
	 *       </C:comp-filter>
	 *     </C:comp-filter>
	 *   </C:filter>
	 * </C:calendar-query>
	 */
	public List<Calendar> getAvailabilityResources(HttpClient httpClient) throws CalDAV4JException 
	{
		// first create the calendar query
		CalendarQuery query = new CalendarQuery() ;

		query.addProperty(CalDAVConstants.DNAME_GETETAG) ;
		
		// This adds the "<C:calendar-data/>" part in order to get all properties
		//
		CalendarData calendarData = new CalendarData();
		query.setCalendarDataProp(calendarData);
		
		// Add the VAVAILABILITY filter
		//
		CompFilter vCalendarCompFilter = new CompFilter(Calendar.VCALENDAR) ;

		CompFilter vEventCompFilter = new CompFilter(Component.VAVAILABILITY) ;
		vCalendarCompFilter.addCompFilter(vEventCompFilter) ;
		
		query.setCompFilter(vCalendarCompFilter) ;

		return getComponentByQuery(httpClient, Component.VEVENT, query) ;
	}
	
	// ******************* test test test ******************
    /**
     * Test method
     * @param args
     * @throws CalDAV4JException
     * @throws SocketException
     * @throws URISyntaxException
     */
		public static void main(String[] args) throws CalDAV4JException, SocketException, URISyntaxException {
/*			
			final String CALDAV_SERVER = CaldavCredential.CALDAV_SERVER_HOST;
			final String CALDAV_PORT = String.valueOf(CaldavCredential.CALDAV_SERVER_PORT); 
			final String CALDAV_USER = CaldavCredential.CALDAV_SERVER_USERNAME; 
			final String CALDAV_PASS = CaldavCredential.CALDAV_SERVER_PASSWORD;
			
			System.out.println("Creating Caldav Client..");
			BaseCaldavClient cli = new BaseCaldavClient(CALDAV_SERVER, CALDAV_PORT,
					CaldavCredential.CALDAV_SERVER_PROTOCOL,
					CaldavCredential.CALDAV_SERVER_WEBDAV_ROOT, CALDAV_USER, CALDAV_PASS);
			
			System.out.println("Opening a collection..");
        	CalDavCollectionManager cdm = new CalDavCollectionManager(cli);
        	cdm.setRelativePath("events");
        	
        	Random r = new Random();
        	int newDay = r.nextInt(30);
        	
			System.out.println("create a dummy event..");
        	// create date for event
           	Date beginDate = ICalendarUtils.createDateTime(2007, 8, 9, newDay,0, null, true);
    		Date endDate = ICalendarUtils.createDateTime(2007, 8, 7, null, true);
    		Dur duration = new Dur("3H"); 
    		
    		// test for getByTimestamp
    		Date startDTSTART = ICalendarUtils.createDateTime(2007, 5, 7, null, true);
    		Date endDTSTART = ICalendarUtils.createDateTime(2007, 11, 10, null, true);

    		// create new event
        	VEvent nve = new VEvent(beginDate, duration, "My new event");
        	cdm.addEvent(nve, null);
        	
			System.out.println("modify the event..");
        	// modify event
        	Description d = new Description("Caldav4j Event");
        	
        	ParameterList pl = new ParameterList();
        	pl.add(PartStat.ACCEPTED);
        	Attendee invitato = new Attendee(pl, "mailto:rpolli@example.com");
        	
        	
        	ICalendarUtils.addOrReplaceProperty(nve, nve.getUid());
        	ICalendarUtils.addOrReplaceProperty(nve, new Summary("I changed the summary!"));
        	ICalendarUtils.addOrReplaceProperty(nve, invitato);
        	ICalendarUtils.addOrReplaceProperty(nve, invitato);
        	ICalendarUtils.addOrReplaceProperty(nve, d);
        	
        	// re-get event
			System.out.println("Retrieve the event..");
        	Calendar pippo =  cdm.getCalendarForEventUID( ((HttpClient) cdm.client), nve.getUid().getValue());
        	try {
				cdm.editEvent(nve, null);
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("dump the event..\n"+pippo.toString()+"\n");


			List <String> lc = cdm.getEventPropertyByTimestamp( cdm.client, Property.UID, beginDate, endDate);
			for (String cal : lc) {
				System.out.println( "UID="+cal);
			}
*/ 
	}
}