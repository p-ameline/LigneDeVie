package com.ldv.shared.calendar ;

import java.util.Comparator;
import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable ;

import com.ldv.shared.model.LdvTime;

/**
 * The Event class represents an iCal event
 */
public class Event extends CalendarObject implements IsSerializable 
{
	// The following are optional, but MUST NOT occur more than once
	//
	// class / created / description / dtstart / geo / last-mod / location / organizer / priority / dtstamp / seq / 
	// status / summary / transp / uid / url / recurid /
	//
	private String     _sAccessClassification ;           // class
	private LdvTime    _tDateCreated  = new LdvTime(0) ;  // created
	private String     _sDescription ;                    // description
	private LdvTime    _tDateStart    = new LdvTime(0) ;  // dtstart
	// private Geo        _GeoLatLon = new Geo() ;        // geo
	private LdvTime    _tLastModified = new LdvTime(0) ;  //
	private Location   _Location = new Location() ;       // location
	private String     _sOrganizer ;
	private String     _sPriority ;
	private LdvTime    _tDateStamp = new LdvTime(0) ;
	private String     _sSeq ;
	private String     _sStatus ;
	private String     _sSummary ;
	private String     _sTransp ;
	private String     _sURL ;
	private String     _sRecurId ;
	
	// Either 'dtend' or 'duration' may appear in a 'eventprop', but 'dtend' and 'duration' MUST NOT occur in the same 'eventprop'
	//
	// dtend / duration /
	//
	private LdvTime  _tDateEnd = new LdvTime(0) ;
	private Duration _duration = new Duration() ;
	
	// The following are optional, and MAY occur more than once
	//
	// attach / attendee / categories / comment / contact / exdate / exrule / rstatus / related / resources / rdate / 
	// rrule / x-prop
	//
	private Vector<String>  _aAttachs    = new Vector<String>() ;
	private Vector<String>  _aAttendees  = new Vector<String>() ;
	private Vector<String>  _aCategories = new Vector<String>() ;
	private Vector<String>  _aComments   = new Vector<String>() ;
	private Vector<String>  _aContacts   = new Vector<String>() ;
	private Vector<LdvTime> _aExDate     = new Vector<LdvTime>() ;
	private Vector<String>  _aExRule     = new Vector<String>() ;
	private Vector<String>  _aRStatus    = new Vector<String>() ;
	private Vector<String>  _aRelated    = new Vector<String>() ;
	private Vector<String>  _aRessources = new Vector<String>() ;
	private Vector<LdvTime> _aRDates     = new Vector<LdvTime>() ;
	private Vector<String>  _aRRules     = new Vector<String>() ;
	private Vector<String>  _aXProps     = new Vector<String>() ;
	
	/**
	 * Zero parameters constructor
	 */
	public Event() {
		reset() ;
	}
		
	public Event(final String sUID, final String sSummary, final String sLocation, final LdvTime tDateStart, final LdvTime tDateEnd, final String sDescription) 
	{
		reset() ;
		
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
	public Event(final Event model) {
		initFromEvent(model) ;
	}
	
	/**
	 * Initialize from a model
	 */
	public void initFromEvent(Event model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		initRoot(model) ;
		
		_sAccessClassification = model._sAccessClassification ;
		_sSummary              = model._sSummary ;
		_sDescription          = model._sDescription ;
		_sOrganizer            = model._sOrganizer ;
		_sPriority             = model._sPriority ;
		_sSeq                  = model._sSeq ;
		_sStatus               = model._sStatus ;
		_sTransp               = model._sTransp ;
		_sURL                  = model._sURL ;
		_sRecurId              = model._sRecurId ;
		
		_duration.initFromModel(model._duration) ;
		
		_tDateStart.initFromLdvTime(model._tDateStart) ;
		_tDateEnd.initFromLdvTime(model._tDateEnd) ;
		_tDateCreated.initFromLdvTime(model._tDateCreated) ; ;
		_tLastModified.initFromLdvTime(model._tLastModified) ; ;
		_tDateStamp.initFromLdvTime(model._tDateStamp) ; ;
		
		_Location.initFromLocation(model._Location) ;
		
		initFrom(_aAttachs,    model._aAttachs) ;
		initFrom(_aAttendees,  model._aAttendees) ;
		initFrom(_aCategories, model._aCategories) ;
		initFrom(_aComments,   model._aComments) ;
		initFrom(_aContacts,   model._aContacts) ;
		initFrom(_aExRule,     model._aExRule) ;
		initFrom(_aRStatus,    model._aRStatus) ;
		initFrom(_aRelated,    model._aRelated) ;
		initFrom(_aRessources, model._aRessources) ;
		initFrom(_aRRules,     model._aRRules) ;
		initFrom(_aXProps,     model._aXProps) ;
		
		initFrom4Dates(_aExDate, model._aExDate) ;
		initFrom4Dates(_aRDates, model._aRDates) ;
	}

	/**
	 * Reset
	 */
	public void reset() 
	{
		resetRoot() ;
		
		_sAccessClassification = "" ;
		_sSummary              = "" ;
		_sDescription          = "" ;
		_sOrganizer            = "" ;
		_sPriority             = "" ;
		_sSeq                  = "" ;
		_sStatus               = "" ;
		_sTransp               = "" ;
		_sURL                  = "" ;
		_sRecurId              = "" ;
		
		_duration.reset() ;
		
		_tDateStart.init() ;
		_tDateEnd.init() ;
		_tDateCreated.init() ;
		_tLastModified.init() ;
		_tDateStamp.init() ;
		
		_Location.reset() ;
		
		_aAttachs.clear() ;
		_aAttendees.clear() ;
		_aCategories.clear() ;
		_aComments.clear() ;
		_aContacts.clear() ;
		_aExRule.clear() ;
		_aRStatus.clear() ;
		_aRelated.clear() ;
		_aRessources.clear() ;
		_aRRules.clear() ;
		_aXProps.clear() ;
		
		_aExDate.clear() ;
		_aRDates.clear() ;
	}

	public String getAccessClassification() {
		return _sAccessClassification ;
	}
	public void setAccessClassification(final String sAccessClassification) {
		_sAccessClassification = sAccessClassification ;
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
	
	public String getSummary() {
		return _sSummary ;
	}
	public void setSummary(final String sSummary) {
		_sSummary = sSummary ;
	}
	
	public String getDescription() {
		return _sDescription ;
	}
	public void setDescription(final String sDescription) {
		_sDescription = sDescription ;
	}
	
/*
	public Geo getGeoLatLon() {
		return _GeoLatLon ;
	}
*/
	/**
	 * Initialize latitude and longitude from a "geo" property with respectively latitude and longitude
	 * in the form <code>float ";" float</code>
	 */
/*
	public void setGeoLatLon(final String sGeo) 
	{
		if ((null == sGeo) || "".equals(sGeo))
			return ;
			
		_GeoLatLon.setFromString(sGeo) ;
	}
*/	
	public String getOrganizer() {
		return _sOrganizer ;
	}
	public void setOrganizer(final String sOrganizer) {
		_sOrganizer = sOrganizer ;
	}
	
	public String getPriority() {
		return _sPriority ;
	}
	public void setPriority(final String sPriority) {
		_sPriority = sPriority ;
	}
	
	public String getSeq() {
		return _sSeq ;
	}
	public void setSeq(final String sSeq) {
		_sSeq = sSeq ;
	}
	
	public String getStatus() {
		return _sStatus ;
	}
	public void setStatus(final String sStatus) {
		_sStatus = sStatus ;
	}
	
	public String getTransp() {
		return _sTransp ;
	}
	public void setTransp(final String sTransp) {
		_sTransp = sTransp ;
	}
	
	public String getURL() {
		return _sURL ;
	}
	public void setURL(final String sURL) {
		_sURL = sURL ;
	}
	
	public String getRecurId() {
		return _sRecurId ;
	}
	public void setRecurId(final String sRecurId) {
		_sRecurId = sRecurId ;
	}
	
	public Vector<String> getAttachs() {
		return _aAttachs ;
	}
	public void setAttachs(final Vector<String> aAttachs) {
		initFrom(_aAttachs, aAttachs) ;
	}
	
	public Vector<String> getAttendees() {
		return _aAttendees ;
	}
	public void setAttendees(final Vector<String> aAttendees) {
		initFrom(_aAttendees, aAttendees) ;
	}
	
	public Vector<String> getCategories() {
		return _aCategories ;
	}
	public void setCategories(final Vector<String> aCategories) {
		initFrom(_aCategories, aCategories) ;
	}
	
	public Vector<String> getComments() {
		return _aComments ;
	}
	public void setComments(final Vector<String> aComments) {
		initFrom(_aComments, aComments) ;
	}
	
	public Vector<String> getContacts() {
		return _aContacts ;
	}
	public void setContacts(final Vector<String> aContacts) {
		initFrom(_aContacts, aContacts) ;
	}
	
	public Vector<LdvTime> getExDate() {
		return _aExDate ;
	}
	public void setExDate(final Vector<LdvTime> aExDate) {
		initFrom4Dates(_aExDate, aExDate) ;
	}
	
	public Vector<String> getExRule() {
		return _aExRule ;
	}
	public void setExRule(final Vector<String> aExRule) {
		initFrom(_aExRule, aExRule) ;
	}
	
	public Vector<String> getRStatus() {
		return _aRStatus ;
	}
	public void setRStatus(final Vector<String> aRStatus) {
		initFrom(_aRStatus, aRStatus) ;
	}
	
	public Vector<String> getRelated() {
		return _aRelated ;
	}
	public void setRelated(final Vector<String> aRelated) {
		initFrom(_aRelated, aRelated) ;
	}
	
	public Vector<String> getRessources() {
		return _aRessources ;
	}
	public void setRessources(final Vector<String> aRessources) {
		initFrom(_aRessources, aRessources) ;
	}
	
	public Vector<LdvTime> getRDates() {
		return _aRDates ;
	}
	public void setRDates(final Vector<LdvTime> aRDates) {
		initFrom4Dates(_aRDates, aRDates) ;
	}
	
	public Vector<String> getRRules() {
		return _aRRules ;
	}
	public void setRRules(final Vector<String> aRRules) {
		initFrom(_aRRules, aRRules) ;
	}
	
	public Vector<String> getXProps() {
		return _aXProps ;
	}
	public void setXProps(final Vector<String> aXProps) {
		initFrom(_aXProps, aXProps) ;
	}
		
	/**
	  * Determine whether two Event objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other Other Event to compare to
	  * 
	  */
	public boolean equals(Event other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (_sUID.equals(other._sUID)) ;
	}
 
	/**
	  * Determine whether an object is exactly similar to this Lexicon object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Event to compare to
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final Event other = (Event) o ;

		return (this.equals(other)) ;
	}
	
	public static class EventComparator implements Comparator<Event> 
	{
    @Override
    public int compare(Event o1, Event o2) {
        return o1.getDateStart().getLocalFullDateTime().compareTo(o2.getDateStart().getLocalFullDateTime()) ;
    }
	}
}
