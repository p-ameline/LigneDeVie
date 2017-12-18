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
 *  the following are REQUIRED but MUST NOT occur more than once
 *
 *   dtstamp / dtstart / uid /
 *
 *  either 'dtend' or 'duration' MAY appear in an 'availableprop', but 'dtend' and 'duration' 
 *  MUST NOT occur in the same 'availableprop'.
 *
 *   dtend / duration /
 *
 *  the following are OPTIONAL but MUST NOT occur more than once
 *
 *   created / description / last-mod / location / recurid / rrule / summary /
 *
 * the following are OPTIONAL and MAY occur more than once
 *
 *   categories / comment / contact / exdate / rdate / x-prop / iana-prop
 * 
 */
public class Available extends CalendarObject implements IsSerializable 
{
	// The following are required, but must not occur more than once
	//
	// dtstart / dtstamp / uid / 
	//
	protected LdvTime  _tDateStart = new LdvTime(0) ;
	protected LdvTime  _tDateStamp = new LdvTime(0) ;
	
	// Either 'dtend' or 'duration' may appear in a 'eventprop', but 'dtend' and 'duration' MUST NOT occur in the same 'availableprop'
	//
	// dtend / duration /
	//
	protected LdvTime  _tDateEnd = new LdvTime(0) ;
	protected Duration _duration = new Duration() ;
	
	// The following are optional, but must not occur more than once
	//
	// created / description / last-mod / location / recurid / rrule / summary / 
	//
	protected LdvTime       _tDateCreated  = new LdvTime(0) ;      // created
	protected LdvTime       _tLastModified = new LdvTime(0) ;      // last-mod
	protected Location      _Location      = new Location() ;      // location
	protected String        _sRecurrenceId ;                       // recurid
	protected CalendarRecur _Recurrence    = new CalendarRecur() ; // rrule
	
	// The following are optional, and MAY occur more than once
	//
	// categories / comment / contact / exdate / rdate / x-prop / iana-prop
	//
	protected Vector<String>  _aCategories = new Vector<String>() ;
	protected Vector<String>  _aComments   = new Vector<String>() ;
	protected Vector<String>  _aContacts   = new Vector<String>() ;
	protected Vector<LdvTime> _aExDates    = new Vector<LdvTime>() ; // exdate (list of date/time exceptions for a recurring calendar component)
	protected Vector<LdvTime> _aRDates     = new Vector<LdvTime>() ; // rdate (list of date/times for a recurrence set)
	protected Vector<String>  _aXProps     = new Vector<String>() ;
	protected Vector<String>  _aIanaProps  = new Vector<String>() ;
	
	/**
	 * Zero parameters constructor
	 */
	public Available() {
		reset() ;
	}
		
	public Available(final String sUID, final String sSummary, final String sLocation, final LdvTime tDateStart, final LdvTime tDateEnd, final String sDescription) 
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
	public Available(final Available model) {
		initFromAvailable(model) ;
	}
	
	/**
	 * Initialize from a model
	 */
	public void initFromAvailable(final Available model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		initRoot(model) ;
		
		_sRecurrenceId = model._sRecurrenceId ;
		
		_Recurrence.initializeFromCalendarRecur(model._Recurrence) ;
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
		
		initFrom4Dates(_aExDates, model._aExDates) ;
		initFrom4Dates(_aRDates, model._aRDates) ;
	}

	/**
	 * Reset all information
	 */
	public void reset() 
	{
		resetRoot() ;
		
		_sRecurrenceId = "" ;

		_Recurrence.reset() ;
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
		_aExDates.clear() ;
		_aRDates.clear() ;
		_aXProps.clear() ;
		_aIanaProps.clear() ;
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
	
	public CalendarRecur getRecurrence() {
		return _Recurrence ;
	}
	public void setRecurrence(final CalendarRecur recurrence) {
		_Recurrence.initializeFromCalendarRecur(recurrence) ;
	}
	
	public String getRecurrenceId() {
		return _sRecurrenceId ;
	}
	public void setRecurrenceId(final String sRecurId) {
		_sRecurrenceId = sRecurId ;
	}
		
	public Vector<String> getCategories() {
		return _aCategories ;
	}
	public void addToCategories(final String sCategory) 
	{
		if (null == sCategory)
			return ;
		
		_aCategories.add(sCategory) ;
	}
	public void setCategories(final Vector<String> aCategories) {
		initFrom(_aCategories, aCategories) ;
	}
	
	public Vector<String> getComments() {
		return _aComments ;
	}
	public void addToComments(final String sComment) 
	{
		if (null == sComment)
			return ;
		
		_aComments.add(sComment) ;
	}
	public void setComments(final Vector<String> aComments) {
		initFrom(_aComments, aComments) ;
	}
	
	public Vector<String> getContacts() {
		return _aContacts ;
	}
	public void addToContacts(final String sContact) 
	{
		if (null == sContact)
			return ;
		
		_aContacts.add(sContact) ;
	}
	public void setContacts(final Vector<String> aContacts) {
		initFrom(_aContacts, aContacts) ;
	}
	
	public Vector<LdvTime> getExDate() {
		return _aExDates ;
	}
	public void addToExDates(final LdvTime tExDate) 
	{
		if (null == tExDate)
			return ;
		
		_aExDates.add(new LdvTime(tExDate)) ;
	}
	public void addToExDates(final Date javaDate) 
	{
		if (null == javaDate)
			return ;
		
		_aExDates.add(new LdvTime(javaDate)) ;
	}
	public void setExDate(final Vector<LdvTime> aExDate) {
		initFrom4Dates(_aExDates, aExDate) ;
	}
	
	public Vector<LdvTime> getRDates() {
		return _aRDates ;
	}
	public void addToRDates(final LdvTime tExDate) 
	{
		if (null == tExDate)
			return ;
		
		_aRDates.add(new LdvTime(tExDate)) ;
	}
	public void addToRDates(final Date javaDate) 
	{
		if (null == javaDate)
			return ;
		
		_aRDates.add(new LdvTime(javaDate)) ;
	}
	public void setRDates(final Vector<LdvTime> aRDates) {
		initFrom4Dates(_aRDates, aRDates) ;
	}
		
	public Vector<String> getXProps() {
		return _aXProps ;
	}
	public void setXProps(final Vector<String> aXProps) {
		initFrom(_aXProps, aXProps) ;
	}
	
	public Vector<String> getIanaProps() {
		return _aIanaProps ;
	}
	public void setIanaProps(final Vector<String> aIanaProps) {
		initFrom(_aIanaProps, aIanaProps) ;
	}
	
	/**
	 * Copy a vector of String into another one
	 * 
	 * @param target List to be filled
	 * @param model  Filler
	 */
	protected void initFrom(Vector<String> target, final Vector<String> model)
	{
		target.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<String> it = model.iterator() ; it.hasNext() ; )
			target.add(new String(it.next())) ;
	}
	
	/**
	 * Copy a vector of LdvTime into another one
	 * 
	 * @param target List to be filled
	 * @param model  Filler
	 */
	protected void initFrom4Dates(Vector<LdvTime> target, final Vector<LdvTime> model)
	{
		target.clear() ;
		
		if ((null == model) || model.isEmpty())
			return ;
		
		for (Iterator<LdvTime> it = model.iterator() ; it.hasNext() ; )
			target.add(new LdvTime(it.next())) ;
	}
	
	/**
	 * Determine whether two Available objects are exactly similar
	 * 
	 * @param  other Other Available to compare to
	 * 
	 * @return <code>true</code> if all data are the same, <code>false</code> if not
	 */
	public boolean equals(final Available other)
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
		
		return (_Location.equals(other._Location) &&
				    _duration.equals(other._duration) &&
				    _Recurrence.equals(other._Recurrence)) ;
	}
	
	/**
	 * Determine whether two Available objects's String data are equal
	 * 
	 * @param  other Other Available which String data are to be compared with
	 * 
	 * @return <code>true</code> if all the String data are the same, <code>false</code> if not
	 */
	protected boolean hasSameStrings(final Available other) {
		return (MiscellanousFcts.areIdenticalStrings(_sRecurrenceId, other._sRecurrenceId)) ; 
	}
 
	/**
	 * Determine whether two Available objects's times data are equal
	 * 
	 * @param  other Other Available which LdvTime data are to be compared with
	 * 
	 * @return <code>true</code> if all the LdvTime data are the same, <code>false</code> if not
	 */
	protected boolean hasSameDates(final Available other) {
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
	protected boolean hasSameVectors(final Available other) {
		return (areVectorsEqual(_aCategories, other._aCategories) &&
				    areVectorsEqual(_aComments,   other._aComments)   &&
				    areVectorsEqual(_aContacts,   other._aContacts)   &&
				    areVectorsEqual(_aExDates,    other._aExDates)    &&
				    areVectorsEqual(_aRDates,     other._aRDates)     &&
				    areVectorsEqual(_aXProps,     other._aXProps)     &&
				    areVectorsEqual(_aIanaProps,  other._aIanaProps)) ;
	}
	
	/**
	  * Determine whether an object is exactly similar to this Available object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param o Available to compare to
	  *
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false ;
		}

		final Available other = (Available) o ;

		return (this.equals(other)) ;
	}
	
	public static class AvailableComparator implements Comparator<Available> 
	{
    @Override
    public int compare(Available o1, Available o2) {
        return o1.getDateStart().getLocalFullDateTime().compareTo(o2.getDateStart().getLocalFullDateTime()) ;
    }
	}
}
