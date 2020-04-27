package com.ldv.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * Date-time management core structure
 * 
 * This object is based on a YYYYMMDDhhmmssmmm string that represents date-time for time zone 0
 * (so all objects that share the same string are synchronous)
 * 
 * @author Philippe
 */
public class LdvTimeStructure implements IsSerializable
{
	protected String _sTimeString ; // YYYYMMDDhhmmssmmm for time zone 0 (UTC unduly called GMT)
	
	protected static String _sEmptyString   = "00000000000000000" ;
	protected static String _sNoLimitString = "99990000000000000" ;
		
	/**
	 * Zero argument constructor in order to have this class become serializable  
	 * 
	 **/
	public LdvTimeStructure() {
		init() ;
	}
	
	/**
	 * Constructor from date time information
	 */
	public LdvTimeStructure(final int year, final int month, final int date, final int hour, final int minute, final int second, final int millis) {
		init(year, month, date, hour, minute, second, millis) ;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param model Model to initialize from
	 * 
	 **/
	public LdvTimeStructure(final LdvTimeStructure model)
	{
		init() ;
		
		if (null == model)
			return ;
		
		initFromLdvTimeStructure(model) ;
	}
		
	/**
	 * Resets the LdvTime object 
	 * 
	 **/
	public void init() { 
		_sTimeString = _sEmptyString ;
	}
		
	/**
	 * Set the LdvTime object to "infinite in the future" 
	 * 
	 **/
	public void setNoLimit() { 
		_sTimeString = _sNoLimitString ;
	}
	
	/**
	 * Initialize from date time information 
	 */
	public void init(final int iYears, final int iMonths, final int iDays, final int iHours, final int iMinutes, final int iSeconds, final int iMillis)
	{
		init() ;

		putYears(iYears) ;
		putMonths(iMonths) ;
		putDays(iDays) ;
		putHours(iHours) ;
		putMinutes(iMinutes) ;
		putSeconds(iSeconds) ;
		putMillis(iMillis) ;
	}
	
	/**
	 * Initializes from a date and time expressed as a String
	 * 
	 * @param sDateTime Either "AAAAMMJJHHmmss" or "AAAAMMJJHHmmssZZZ"
	 * 
	 **/
	public boolean initFromDateTime(final String sDateTime) throws NullPointerException 
	{ 
		if (null == sDateTime)
			throw new NullPointerException() ;
		
		int iDateLen = sDateTime.length() ;		
		if ((14 != iDateLen) && (17 != iDateLen)) 
			return false ;
		
		if (false == MiscellanousFcts.isDigits(sDateTime))
    	return false ;
		
		_sTimeString = sDateTime ;
		
		if (14 == iDateLen)
			_sTimeString += "000" ;

		return true ;
	}
	
	/**
	 * Initializes from a date expressed as a String  
	 * 
	 * @param sDate Either "AAAAMMJJ" or "AAAAMMJJHHmmss"
	 * 
	 **/
	public boolean initFromDate(final String sDate) throws NullPointerException 
	{
		if (null == sDate)
			throw new NullPointerException() ;
		
		int iDateLen = sDate.length() ;		
		if (iDateLen < 8)
			return false ;
		
		if (iDateLen > 8)
			return initFromDateTime(sDate) ;
		
    if (false == MiscellanousFcts.isDigits(sDate))
    	return false ;
    
    _sTimeString = sDate + "000000000" ;
    
		return true ;
	}
	
	/**
	 * Initialize from another LdvTimeStructure object   
	 * 
	 * @param ldvT The {@link LdvTimeStructure LdvTimeStructure} object to initialize from
	 * 
	 **/
	public void initFromLdvTimeStructure(final LdvTimeStructure ldvT) 
	{
		init() ;
		
		if (null == ldvT)
			return ;
		
		_sTimeString = ldvT._sTimeString ;
	}
	
	/**
	 * Get a string resulting from inserting an int value at a given slot into another string 
	 * 
	 * @param iValue   Value to insert
	 * @param sTarget  Reference string
	 * @param iStart   Starting position
	 * @param iLength
	 * @return
	 */
	protected void insertIntAtPos(final int iValue, final int iStart, final int iLength)
	{
		if ((null == _sTimeString) || "".equals(_sTimeString))
			return ;
		
		int iTargetLength = _sTimeString.length() ;
		
		if (0 == iLength)
			return ;
		 
		if (iStart + iLength > iTargetLength)
			return ;
		
		LdvInt iLdvValue = new LdvInt(iValue) ;
		String sReplacer = iLdvValue.intToString(iLength) ;
		
		// Replace at start
		//
		if (0 == iStart)
		{
			// Replace all
			//
			if (iTargetLength == iLength)
				return ;
			
			_sTimeString = sReplacer + _sTimeString.substring(iLength, iTargetLength) ;
			
			return ;
		}
		
		// Replace beyond start
		//
		
		// Replace the entire ending part
		//
		if (iStart + iLength == iTargetLength)
		{
			_sTimeString = _sTimeString.substring(0, iStart) + sReplacer ;
			
			return ;
		}
		
		// Mundane case: replace somewhere in the middle
		//
		_sTimeString = _sTimeString.substring(0, iStart) + sReplacer + _sTimeString.substring(iStart + iLength, iTargetLength) ;
	}

	public void putYears(final int iI) {
		insertIntAtPos(iI, 0, 4) ;
	}
	
	public void putMonths(final int iI) {
		insertIntAtPos(iI, 4, 2) ;
	}
	
	public void putDays(final int iI) {
		insertIntAtPos(iI, 6, 2) ;
	}
	
	public void putHours(final int iI) {
		insertIntAtPos(iI, 8, 2) ;
	}
	
	public void putMinutes(final int iI) {
		insertIntAtPos(iI, 10, 2) ;
	}
	
	public void putSeconds(final int iI) {
		insertIntAtPos(iI, 12, 2) ;
	}
	
	public void putMillis(final int iI) {
		insertIntAtPos(iI, 14, 3) ;
	}
	
	protected int getDateElement(final int iBegin, final int iEnd)
	{
		if (_sTimeString.length() < iEnd)
			return -1 ;
		
		return Integer.parseInt(_sTimeString.substring(iBegin, iEnd)) ;
	}
	
	// Functions operating on internal date representation
	//
	protected int getYears() { 
		return getDateElement(0, 4) ; 
	}
	
	protected int getMonths() {
		return getDateElement(4, 6) ;
	}
	
	protected int getDays() {
		return getDateElement(6, 8) ;
	}
	
	protected int getHours() {
		return getDateElement(8, 10) ;
	}
	
	protected int getMinutes() {
		return getDateElement(10, 12) ;
	}
	
	protected int getSeconds() {
		return getDateElement(12, 14) ;
	}
	
	protected int getMillis() {
		return getDateElement(14, 17) ;
	}
	
	/**
	  * Determine whether two LdvTimeStructure are exactly similar
	  * 
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  * @param other Other LdvTimeStructure to compare to
	  * 
	  */
	public boolean equals(final LdvTimeStructure other) 
	{ 
		if (null == other)
			return false ;
		
		if (this == other)
			return true ;
		
		return _sTimeString.equals(other._sTimeString) ;
	}
	
	/**
	  * Determine whether an object is similar to this LdvTimeStructure
	  * 
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  * @param o Object to compare to
	  * 
	  */
	public boolean equals(Object o) 
	{
		if ((null == o) || (getClass() != o.getClass()))
			return false ;
		
		if (this == o)
			return true ;

		final LdvTimeStructure time = (LdvTimeStructure) o ;

		return equals(time) ;
	}
	
	/**
	 * Is this object uninitialized?
	 */
	final public boolean isEmpty() {
		return _sTimeString.equals(_sEmptyString) ;
	}
	
	/**
	 * Is this object in the infinite future?
	 */
	final public boolean isNoLimit() {
		return _sTimeString.equals(_sNoLimitString) ;
	}
	
	/**
	 * @return the date in AAAAMMJJhhmmssmmm format for time zone 0
	 */
	final public String getFullDateTime() { 
		return _sTimeString ;
	}
}
