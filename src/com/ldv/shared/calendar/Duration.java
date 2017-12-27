package com.ldv.shared.calendar ;

import com.google.gwt.user.client.rpc.IsSerializable ;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * The Duration class represents a duration that can be expressed and parsed from ISO 8601:2004
 */
public class Duration implements IsSerializable 
{
	private boolean _isPlus ;
	private int     _iYears ; 
	private int     _iMonths ;
	private int     _iWeeks ;
	private int     _iDays ;
	private int     _iHours ;
	private int     _iMinutes ;
	private int     _iSeconds ;
	private int     _iMilli ;
	
	/**
	 * Default constructor (does nothing)
	 */
	public Duration() {
		reset() ;
	}
	
	/**
	 * Constructor from a string
	 * 
	 * @param sLatLon A String in the form <code>locparam ";" text</code> containing respectively the text and the file URI
	 */
	public Duration(final String sTextFile) {
		setFromString(sTextFile) ;
	}
	
	/**
	 * Initialize from another Duration object
	 * 
	 * @param other Other Duration object to initialize from
	 */
	public void initFromModel(Duration other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_isPlus   = other._isPlus ;
		_iYears   = other._iYears ; 
		_iMonths  = other._iMonths ;
		_iWeeks   = other._iWeeks ;
		_iDays    = other._iDays ;
		_iHours   = other._iHours ;
		_iMinutes = other._iMinutes ;
		_iSeconds = other._iSeconds ;
		_iMilli   = other._iMilli ;
	}
	
	/**
	 * Reset to a void duration
	 */
	public void reset()
	{
		_isPlus   = true ;
		_iYears   = 0 ; 
		_iMonths  = 0 ;
		_iWeeks   = 0 ;
		_iDays    = 0 ;
		_iHours   = 0 ;
		_iMinutes = 0 ;
		_iSeconds = 0 ;
		_iMilli   = 0 ;
	}
	
	/**
	 * Is the object in its reseted state?
	 */
	public boolean isZero()
	{
		return ((0 == _iYears)   &&
				    (0 == _iMonths)  &&
				    (0 == _iWeeks)   &&
				    (0 == _iDays)    &&
				    (0 == _iHours)   &&
				    (0 == _iMinutes) &&
				    (0 == _iSeconds) &&
				    (0 == _iMilli)) ;
	}
	
	/**
	 * Is there any hours or minutes or seconds duration element
	 */
	public boolean hasHMS()
	{
		return ((0 != _iHours)   ||
				    (0 != _iMinutes) ||
				    (0 != _iSeconds)) ;
	}
	
	/**
	 * Do this object only contains a number of weeks
	 */
	public boolean hasOnlyWeeks()
	{
		return ((0 == _iYears)   &&
		        (0 == _iMonths)  &&
		        (_iWeeks > 0)    &&
		        (0 == _iDays)    &&
		        (0 == _iHours)   &&
		        (0 == _iMinutes) &&
		        (0 == _iSeconds) &&
		        (0 == _iMilli)) ;
	}
	
	/**
	 * Get the Location object as a String
	 * 
	 * According to RFC5545: 
	 * 
	 * This value type is defined by the following notation:
   *
   *   dur-value  = (["+"] / "-") "P" (dur-date / dur-time / dur-week)
   *
   *   dur-date   = dur-day [dur-time]
   *   dur-time   = "T" (dur-hour / dur-minute / dur-second)
   *   dur-week   = 1*DIGIT "W"
	 *	 dur-hour   = 1*DIGIT "H" [dur-minute]
	 *   dur-minute = 1*DIGIT "M" [dur-second]
	 *   dur-second = 1*DIGIT "S"
	 *   dur-day    = 1*DIGIT "D"
	 * 
	 * Note that unlike [ISO.8601.2004], this value type doesn't support the "Y" and "M"
   * designators to specify durations in terms of years and months.
	 * 
	 * @return A String in the above notation if duration is not zero, <code>""</code> if not
	 */
	public String getValue() 
	{
		if (isZero())
			return "" ;
		
		String sResult = "" ;
		
		if (_isPlus)
			sResult += "P" ;
		else
			sResult += "-P" ;
		
		// Remember that the duration value type doesn't support the "Y" and "M" designators to specify years and months.
		// Only weeks and days can appear
		//
		if (0 != _iWeeks)
			sResult += _iWeeks  + "W" ;
		if (0 != _iDays)
			sResult += _iDays   + "D" ;
		
		if (false == hasHMS())
			return sResult ;
		
		if ((0 != _iWeeks) || (0 != _iDays))
			sResult += "T" ;
		
		sResult += _iHours   + "H" ;
		sResult += _iMinutes + "M" ;
		sResult += _iSeconds + "S" ;
		
    return sResult ;
	}
	
	/**
	 * Initialize from a string
	 * 
	 * @param sDuration A String in the ISO 8601:2004 format for duration
	 * 
	 * @return <code>true</code> if the string to parse is valid, <code>false</code> if not valid, or empty
	 */
	public boolean setFromString(final String sDuration)
	{
		reset() ;
		
		if ((null == sDuration) || "".equals(sDuration))
			return false ;
		
		int i = 0 ;
		
		// The string must start with '-', '+' or 'P'
		//
		if ('-' == sDuration.charAt(i))
		{
			_isPlus = false ;
			i++ ;
		}
		else if ('+' == sDuration.charAt(i))
			i++ ;
		
		// Must be a 'P'
		//
		if ('P' == sDuration.charAt(i))
			i++ ;
		else
			return false ;
		
		for ( ; i < sDuration.length() ; )
		{
			// Find the largest j such as sDuration.substring(i, j) is only made of digits
			//
			int j = i ;
			do {
				j++ ;
			} while ((j <= sDuration.length()) && MiscellanousFcts.isDigits(sDuration.substring(i, j))) ;
			
			// At least one digit
			//
			if (j < i + 2)
				return false ;
			
			// Digit(s) must be followed by a unit char 
			//
			if (j > sDuration.length())
				return false ;
			
			int iValue = Integer.parseInt(sDuration.substring(i, j - 1)) ;
			
			switch (sDuration.charAt(j - 1))
			{
				case 'W' : 
					_iWeeks = iValue ;
					break ;
				case 'D' : 
					_iDays = iValue ;
					break ;
				case 'H' : 
					_iHours = iValue ;
					break ;
				case 'M' : 
					_iMinutes = iValue ;
					break ;
				case 'S' : 
					_iSeconds = iValue ;
					break ;
				default :
					return false ;
			}
			
			i = j ;
			
			if ((i < sDuration.length()) && ('T' == sDuration.charAt(i)))
				i++ ;
		}
		
		return true ;
	}
	
	public boolean isPlus() {
		return _isPlus ;
	}
	public void setPlus(final boolean isPlus) {
		_isPlus = isPlus ;
	}
	
	public int getYears() {
		return getSignedValue(_iYears) ;
	}
	public void setYears(final int iYears) {
		checkPlus(iYears) ;
		_iYears = Math.abs(iYears) ;
	}
	
	public int getMonths() {
		return getSignedValue(_iMonths) ;
	}
	public void setMonths(final int iMonths) {
		checkPlus(iMonths) ;
		_iMonths = Math.abs(iMonths) ;
	}
	
	public int getWeeks() {
		return getSignedValue(_iWeeks) ;
	}
	public void setWeeks(final int iWeeks) {
		checkPlus(iWeeks) ;
		_iWeeks = Math.abs(iWeeks) ;
	}
	
	public int getDays() {
		return getSignedValue(_iDays) ;
	}
	public void setDays(final int iDays) {
		checkPlus(iDays) ;
		_iDays = Math.abs(iDays) ;
	}
	
	public int getHours() {
		return getSignedValue(_iHours) ;
	}
	public void setHours(final int iHours) {
		checkPlus(iHours) ;
		_iHours = Math.abs(iHours) ;
	}
	
	public int getMinutes() {
		return getSignedValue(_iMinutes) ;
	}
	public void setMinutes(final int iMinutes) {
		checkPlus(iMinutes) ;
		_iMinutes = Math.abs(iMinutes) ;
	}
	
	public int getSeconds() {
		return getSignedValue(_iSeconds) ;
	}
	public void setSeconds(final int iSeconds) {
		checkPlus(iSeconds) ;
		_iSeconds = Math.abs(iSeconds) ;
	}
	
	public int getMilli() {
		return getSignedValue(_iMilli) ;
	}
	public void setMilli(final int iMilli) {
		checkPlus(iMilli) ;
		_iMilli = Math.abs(iMilli) ;
	}
	
	/**
	 * Check if a passed value is negative. If yes, switch _isPlus to <code>false</code> 
	 */
	protected void checkPlus(final int iValue)
	{
		if (iValue < 0)
			_isPlus = false ;
	}
	
	/**
	 * Check if a passed value is negative. If yes, switch _isPlus to <code>false</code> 
	 */
	protected int getSignedValue(final int iValue) {
		return _isPlus ? iValue : -iValue ; 
	}
	
	/**
	 * Determine whether two Available objects are exactly similar
	 * 
	 * @param  other Other Available to compare to
	 * 
	 * @return <code>true</code> if all data are the same, <code>false</code> if not
	 */
	public boolean equals(final Duration other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
			
		return ((_isPlus   == other._isPlus)   &&
				    (_iYears   == other._iYears)   &&
				    (_iMonths  == other._iMonths)  &&
				    (_iWeeks   == other._iWeeks)   &&
				    (_iDays    == other._iDays)    &&
				    (_iHours   == other._iHours)   &&
				    (_iMinutes == other._iMinutes) &&
				    (_iSeconds == other._iSeconds) &&
				    (_iMilli   == other._iMilli)) ;
	}
	
	/**
	  * Determine whether an object is exactly similar to this Duration object
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param o Event to compare to
	  *
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final Duration other = (Duration) o ;

		return (this.equals(other)) ;
	}
}
