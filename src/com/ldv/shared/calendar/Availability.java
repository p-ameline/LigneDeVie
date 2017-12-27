package com.ldv.shared.calendar ;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable ;

import com.ldv.shared.model.LdvTime;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * The Event class represents an iCalendar Available component
 *
 * Reference is RFC 7953:
 * 
 *                       Time Range
 *  <=========================================================>
 *
 *      +-------------------------------------------------+
 *      |              VAVAILABILITY                      |
 *      +-------------------------------------------------+
 *         +------------+       +------------+
 *         | AVAILABLE  |       | AVAILABLE  |
 *         +------------+       +------------+
 *
 *      <->              <----->              <-----------> Busy Time
 *
 *  the following are REQUIRED but MUST NOT occur more than once
 *
 *   dtstamp / uid
 *
 *  either 'dtend' or 'duration' MAY appear in an 'availableprop', but 'dtend' and 'duration' 
 *  MUST NOT occur in the same 'availabilityprop'.
 *  'duration' MUST NOT be present if 'dtstart' is not present
 *
 *   dtend / duration /
 *
 *  the following are OPTIONAL but MUST NOT occur more than once
 *
 *   busytype / class / created / description / dtstart / last-mod / location / organizer / priority /seq / summary / url /
 *
 * the following are OPTIONAL and MAY occur more than once
 *
 *   categories / comment / contact / x-prop / iana-prop
 * 
 */
public class Availability extends CalendarObject implements IsSerializable 
{
	// The following are required, but must not occur more than once
	//
	// dtstamp / uid / 
	//
	protected LdvTime  _tDateStamp = new LdvTime(0) ;
	
	// Either 'dtend' or 'duration' may appear in a 'eventprop', but 'dtend' and 'duration' MUST NOT occur in the same 'availabilityprop'
	// 'duration' MUST NOT be present if 'dtstart' is not present
	//
	// dtend / duration /
	//
	protected LdvTime  _tDateEnd = new LdvTime(0) ;      // dtend
	protected Duration _duration = new Duration() ;      // duration
	
	// The following are optional, but must not occur more than once
	//
	// busytype / class / created / description / dtstart / last-mod / location / organizer / priority /seq / summary / url / 
	//
	protected String   _sDefaultBusyTimeType ;            // busytype
	protected String   _sAccessClassification ;           // class
	protected LdvTime  _tDateCreated  = new LdvTime(0) ;  // created
	protected LdvTime  _tDateStart    = new LdvTime(0) ;  // dtstart
	protected LdvTime  _tLastModified = new LdvTime(0) ;  // last-mod
	protected Location _Location      = new Location() ;  // location
	protected String   _sOrganizer ;                      // organizer
	protected int      _iPriority ;                       // priority
	protected String   _sSeq ;                            // seq
	protected String   _sUrl ;                            // url
	
	// The following are optional, and MAY occur more than once
	//
	// categories / comment / contact / x-prop / iana-prop
	//
	protected Vector<String> _aCategories = new Vector<String>() ;
	protected Vector<String> _aComments   = new Vector<String>() ;
	protected Vector<String> _aContacts   = new Vector<String>() ;
	protected Vector<String> _aXProps     = new Vector<String>() ;
	protected Vector<String> _aIanaProps  = new Vector<String>() ;
	
	// Available subcomponents
	//
	protected Vector<Available> _aAvailables = new Vector<Available>() ;
	
	/**
	 * Zero parameters constructor
	 */
	public Availability() {
		reset() ;
	}
		
	public Availability(final String sUID, final String sSummary, final String sLocation, final LdvTime tDateStart, final LdvTime tDateEnd, final String sDescription) 
	{
		_sUID         = sUID ;
		_sSummary     = sSummary ;
		_sDescription = sDescription ;

		_tDateStart.initFromLdvTime(tDateStart) ;
		_tDateEnd.initFromLdvTime(tDateEnd) ;
		
		_Location.setFromString(sLocation) ;
	}
	
	/**
	 * Copy constructor
	 */
	public Availability(final Availability model) {
		initFromAvailability(model) ;
	}
	
	/**
	 * Initialize from a model
	 */
	public void initFromAvailability(Availability model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		initRoot(model) ;
		
		_sDefaultBusyTimeType  = model._sDefaultBusyTimeType ;
		_sAccessClassification = model._sAccessClassification ;
		_sOrganizer            = model._sOrganizer ;
		_sSeq                  = model._sSeq ;
		_sUrl                  = model._sUrl ;
		
		_iPriority             = model._iPriority ;
		
		_duration.initFromModel(model._duration) ;
		
		_tDateStart.initFromLdvTime(model._tDateStart) ;
		_tDateEnd.initFromLdvTime(model._tDateEnd) ;
		_tDateStamp.initFromLdvTime(model._tDateStamp) ;
		_tDateCreated.initFromLdvTime(model._tDateCreated) ;
		_tLastModified.initFromLdvTime(model._tLastModified) ;
		
		_Location.initFromLocation(model._Location) ;
		
		initFrom(_aCategories, model._aCategories) ;
		initFrom(_aComments, model._aComments) ;
		initFrom(_aContacts, model._aContacts) ;
		initFrom(_aXProps, model._aXProps) ;
		initFrom(_aIanaProps, model._aIanaProps) ;
		
		setAvailables(model.getAvailables()) ;
	}

	/**
	 * Reset all information
	 */
	public void reset() 
	{
		resetRoot() ;
		
		_sDefaultBusyTimeType  = "" ;
		_sAccessClassification = "" ;
		_sOrganizer            = "" ;
		_sSeq                  = "" ;
		_sUrl                  = "" ;
		
		_iPriority = 0 ;       // Warning, priority 0 is the lowest one

		_duration.reset() ;
		
		_tDateStart.init() ;
		_tDateEnd.init() ;
		_tDateStamp.init() ;
		_tDateCreated.init() ;
		_tLastModified.init() ;
		
		_Location.reset() ;
		
		_aCategories.clear() ;
		_aComments.clear() ;
		_aContacts.clear() ;
		_aXProps.clear() ;
		_aIanaProps.clear() ;
		
		_aAvailables.clear() ;
	}
			
	// getter and setter
	//
	public String getUID() {
		return _sUID ;
	}
	public void setUID(final String sUID) {
		_sUID = sUID ;
	}

	public String getLocationText() {
		return _Location.getText() ;
	}
	public void setLocationText(final String sLocation) {
		_Location.setText(sLocation) ;
	}

	public LdvTime getDateStart() {
		return _tDateStart ;
	}
	public void setDateStart(final LdvTime tDateStart) {
		_tDateStart.initFromLdvTime(tDateStart) ;
	}
	public void setDateStart(final String sDateStart) {
		_tDateStart.initFromLocalDate(sDateStart) ;
	}
	public void setDateStart(final Date dDateStart) {
		_tDateStart.initFromJavaDate(dDateStart) ;
	}

	public LdvTime getDateEnd() {
		return _tDateEnd ;
	}
	public void setDateEnd(final LdvTime tDateEnd) {
		_tDateEnd.initFromLdvTime(tDateEnd) ;
	}
	public void setDateEnd(final String sDateEnd) {
		_tDateEnd.initFromLocalDate(sDateEnd) ;
	}
	public void setDateEnd(final Date dDateEnd) {
		_tDateEnd.initFromJavaDate(dDateEnd) ;
	}
	
	public Duration getDuration() {
		return _duration ;
	}
	public String getDurationAsString() {
		return _duration.getValue() ;
	}
	public void setDuration(final String sDuration) {
		_duration.setFromString(sDuration) ;
	}
	
	public String getDefaultBusyTimeType() {
		return _sDefaultBusyTimeType ;
	}
	public void setDefaultBusyTimeType(final String sDefaultBusyTimeType) {
		_sDefaultBusyTimeType = sDefaultBusyTimeType ;
	}
	
	public String getAccessClassification() {
		return _sAccessClassification ;
	}
	public void setAccessClassification(final String sAccessClassification) {
		_sAccessClassification = sAccessClassification ;
	}
	
	public LdvTime getDateCreated() {
		return _tDateCreated ;
	}
	public void setDateCreated(final LdvTime tDateCreated) {
		_tDateCreated.initFromLdvTime(tDateCreated) ;
	}
	public void setDateCreated(final String sDateCreated) {
		_tDateCreated.initFromLocalDate(sDateCreated) ;
	}
	public void setDateCreated(final Date dDateCreated) {
		_tDateCreated.initFromJavaDate(dDateCreated) ;
	}

	public LdvTime getLastModified() {
		return _tLastModified ;
	}
	public void setLastModified(final LdvTime tLastModified) {
		_tLastModified.initFromLdvTime(tLastModified) ;
	}
	public void setLastModified(final String sLastModified) {
		_tLastModified.initFromLocalDate(sLastModified) ;
	}
	public void setLastModified(final Date dLastModified) {
		_tLastModified.initFromJavaDate(dLastModified) ;
	}
	
	public LdvTime getDateStamp() {
		return _tDateStamp ;
	}
	public void setDateStamp(final LdvTime tDateStamp) {
		_tDateStamp.initFromLdvTime(tDateStamp) ;
	}
	public void setDateStamp(final String sDateStamp) {
		_tDateStamp.initFromLocalDate(sDateStamp) ;
	}
	public void setDateStamp(final Date dDateStamp) {
		_tDateStamp.initFromJavaDate(dDateStamp) ;
	}
	
	public String getSeq() {
		return _sSeq ;
	}
	public void setSeq(final String sSeq) {
		_sSeq = sSeq ;
	}
	
	public String getOrganizer() { 
		return _sOrganizer ;
	}
	public void setOrganizer(final String sOrganizer) {
		_sOrganizer = sOrganizer ;
	}
	
	public int getPriority() { 
		return _iPriority ;
	}
	public void setPriority(final int iPriority) {
		_iPriority = iPriority ;
	}
	
	public String getUrl() { 
		return _sUrl ;
	}
	public void setUrl(final String sUrl) {
		_sUrl = sUrl ;
	}
		
	public Vector<String> getCategories() {
		return _aCategories ;
	}
	public void addToCategories(final String sCategory) {
		_aCategories.add(sCategory) ;
	}
	public void setCategories(final Vector<String> aCategories) {
		initFrom(_aCategories, aCategories) ;
	}
	
	public Vector<String> getComments() {
		return _aComments ;
	}
	public void addToComments(final String sComment) {
		_aComments.add(sComment) ;
	}
	public void setComments(final Vector<String> aComments) {
		initFrom(_aComments, aComments) ;
	}
	
	public Vector<String> getContacts() {
		return _aContacts ;
	}
	public void addToContacts(final String sContact) {
		_aContacts.add(sContact) ;
	}
	public void setContacts(final Vector<String> aContacts) {
		initFrom(_aContacts, aContacts) ;
	}
	
	public Vector<String> getXProps() {
		return _aXProps ;
	}
	public void addToXProps(final String sXProp) {
		_aXProps.add(sXProp) ;
	}
	public void setXProps(final Vector<String> aXProps) {
		initFrom(_aXProps, aXProps) ;
	}
	
	public Vector<String> getIanaProps() {
		return _aIanaProps ;
	}
	public void addToIanaProp(final String sIanaProp) {
		_aIanaProps.add(sIanaProp) ;
	}
	public void setIanaProps(final Vector<String> aIanaProps) {
		initFrom(_aIanaProps, aIanaProps) ;
	}
		
	public Vector<Available> getAvailables() {
		return _aAvailables ;
	}
	public void addToAvailables(final Available available) 
	{
		if (null == available)
			return ;
		
		_aAvailables.add(new Available(available)) ;
	}
	public void setAvailables(final Vector<Available> model) 
	{
		_aAvailables.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<Available> it = model.iterator() ; it.hasNext() ; )
			_aAvailables.add(new Available(it.next())) ;
	}
	
	/** 
	  * Determine whether two Event objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other Other Event to compare to
	  * 
	  */
	public boolean equals(Availability other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		// Check common information
		//
		CalendarObject thisAsCalendar  = (CalendarObject) this ;
		CalendarObject otherAsCalendar = (CalendarObject) other ;
		if (false == thisAsCalendar.equals(otherAsCalendar)) 
			return false ;
		
		// Check specific information
		//
		if (false == hasSameStrings(other))
			return false ;
			
		if (false == hasSameDates(other))
			return false ;
			
		if (false == hasSameVectors(other))
			return false ;
		
		return (_duration.equals(other._duration) &&
				    _Location.equals(other._Location) && 
				    (_iPriority == other._iPriority)) ;
	}
 
	/**
	 * Determine whether two Available objects's String data are equal
	 * 
	 * @param  other Other Available which String data are to be compared with
	 * 
	 * @return <code>true</code> if all the String data are the same, <code>false</code> if not
	 */
	protected boolean hasSameStrings(final Availability other) {
		return (MiscellanousFcts.areIdenticalStrings(_sDefaultBusyTimeType,  other._sDefaultBusyTimeType)  &&
				    MiscellanousFcts.areIdenticalStrings(_sAccessClassification, other._sAccessClassification) &&
				    MiscellanousFcts.areIdenticalStrings(_sOrganizer,            other._sOrganizer)            &&
				    MiscellanousFcts.areIdenticalStrings(_sSeq,                  other._sSeq)                  &&
				    MiscellanousFcts.areIdenticalStrings(_sUrl,                  other._sUrl)) ; 
	}
 
	/**
	 * Determine whether two Available objects's times data are equal
	 * 
	 * @param  other Other Available which LdvTime data are to be compared with
	 * 
	 * @return <code>true</code> if all the LdvTime data are the same, <code>false</code> if not
	 */
	protected boolean hasSameDates(final Availability other) {
		return (_tDateStart.equals(other._tDateStart)     &&
		        _tDateStamp.equals(other._tDateStamp)     &&
		        _tDateEnd.equals(other._tDateEnd)         &&
		        _tDateCreated.equals(other._tDateCreated) &&
		        _tLastModified.equals(other._tLastModified)) ;
	}
	
	/**
	 * Determine whether two Available objects's vector data are equal
	 * 
	 * @param  other Other Available which vector data are to be compared with
	 * 
	 * @return <code>true</code> if all the vector data are the same, <code>false</code> if not
	 */
	protected boolean hasSameVectors(final Availability other) {
		return (areVectorsEqual(_aCategories, other._aCategories) &&
				    areVectorsEqual(_aComments,   other._aComments)   &&
				    areVectorsEqual(_aContacts,   other._aContacts)   &&
				    areVectorsEqual(_aXProps,     other._aXProps)     &&
				    areVectorsEqual(_aIanaProps,  other._aIanaProps)  &&
				    areVectorsEqual(_aAvailables, other._aAvailables)) ;
	}
	
	/**
	  * Determine whether an object is exactly similar to this Availability object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param o Event to compare to
	  * 
	  * @return true if all data are the same, false if not
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final Availability other = (Availability) o ;

		return (this.equals(other)) ;
	}
	
	public static class AvailableComparator implements Comparator<Availability> 
	{
    @Override
    public int compare(Availability o1, Availability o2) {
    	return o1.getDateStart().getLocalFullDateTime().compareTo(o2.getDateStart().getLocalFullDateTime()) ;
    }
	}
}
