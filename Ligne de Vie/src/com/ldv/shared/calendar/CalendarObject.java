package com.ldv.shared.calendar ;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable ;

import com.ldv.shared.model.LdvTime;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * Base class for objects that materialize iCalendar components (contains shared information and methods)
 *
 */
public class CalendarObject implements IsSerializable 
{	
	protected String _sUID ;
	protected String _sDescription ; // description
	protected String _sSummary ;     // summary
	
	/**
	 * Zero parameters constructor
	 */
	public CalendarObject() {
		resetRoot() ;
	}
			
	/**
	 * Copy constructor
	 */
	public CalendarObject(final CalendarObject model) {
		initRoot(model) ;
	}
	
	/**
	 * Initialize from a model
	 */
	protected void initRoot(CalendarObject model)
	{
		resetRoot() ;
		
		if (null == model)
			return ;
		
		_sUID         = model._sUID ;
		_sDescription = model._sDescription ;
		_sSummary     = model._sSummary ;
	}

	/**
	 * Reset all information
	 */
	protected void resetRoot() 
	{
		_sUID         = "" ;
		_sDescription = "" ;
		_sSummary     = "" ;
	}
			
	//getter and setter
	//
	public String getUID() {
		return _sUID ;
	}
	public void setUID(final String sUID) {
		_sUID = sUID ;
	}
	
	public String getDescription() { 
		return _sDescription ;
	}
	public void setDescription(final String sDescription) {
		_sDescription = sDescription ;
	}
	
	public String getSummary() {
		return _sSummary ;
	}
	public void setSummary(final String sSummary) {
		_sSummary = sSummary ;
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
	 * Check if two vectors are equal (same content in the same order)
	 * 
	 * @param reference First comparison element 
	 * @param other     Second comparison element
	 * 
	 * @return <code>true</code> if both vectors contain the same element in the same order, <code>false</code> if not
	 */
	protected boolean areVectorsEqual(final Vector<?> reference, final Vector<?> other)
	{
		if (reference == other)
			return true ;
		
		if ((null == reference) && (null == other))
			return true ;
		if ((null == reference) || (null == other))
			return false ;
			
		if (reference.size() != other.size())
			return false ;
		
		Iterator<?> itR = reference.iterator() ;
		Iterator<?> itO = other.iterator() ;
		for ( ; itR.hasNext() && itO.hasNext() ; )
			if (false == itR.next().equals(itO.next())) 
				return false ;
		
		return true ;
	}
	
	/**
	  * Determine whether two CalendarObject objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other Other CalendarObject to compare to
	  * 
	  */
	public boolean equals(CalendarObject other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sUID,         other._sUID)         && 
				    MiscellanousFcts.areIdenticalStrings(_sDescription, other._sDescription) &&
				    MiscellanousFcts.areIdenticalStrings(_sSummary,     other._sSummary)) ;
	}

	/**
	  * Determine whether an object is exactly similar to this CalendarObject object
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

		final CalendarObject other = (CalendarObject) o ;

		return (this.equals(other)) ;
	}
}
