package com.ldv.shared.model;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * Date-time management
 * 
 * This object is based on a YYYYMMDDhhmmssmmm string that represents date-time for time zone 0
 * (so all objects that share the same string are synchronous)
 * 
 * @author Philippe
 */
public class LdvTime implements IsSerializable
{
	protected LdvTimeStructure _time = new LdvTimeStructure() ; // structure holding time zone 0 information (UTC unduly called GMT)
	
	protected long   _lJulianDays ;    // Julian day for time zone 0 (UTC unduly called GMT)
	protected int    _iTimeZone ;      // seconds to add to UTC in order to get the local time (negative for time zones west of UTC0)
		
	private static int[][] daytab = {
    {0,31,28,31,30,31,30,31,31,30,31,30,31},
    {0,31,29,31,30,31,30,31,31,30,31,30,31}
  };
	
	public static int SECONDS_IN_DAY  = 86400 ;
	public static int SECONDS_IN_HOUR = 3600 ;
	public static int SECONDS_IN_MIN  = 60 ;
	
	public static final int MONDAY    = 1 ;
	public static final int TUESDAY   = 2 ;
	public static final int WEDNESDAY = 3 ;
	public static final int THURSDAY  = 4 ;
	public static final int FRIDAY    = 5 ;
	public static final int SATURDAY  = 6 ;
	public static final int SUNDAY    = 7 ;

	// compatible with java.util.Calendar Field Values
	//
	public static final int YEAR                 =  1 ;
	public static final int MONTH                =  2 ;
	public static final int WEEK_OF_YEAR         =  3 ;
	public static final int WEEK_OF_MONTH        =  4 ;
	public static final int DATE                 =  5 ;
	public static final int DAY_OF_MONTH         =  5 ;
	public static final int DAY_OF_YEAR          =  6 ;
	public static final int DAY_OF_WEEK          =  7 ; 
	public static final int DAY_OF_WEEK_IN_MONTH =  8 ;
	public static final int AM_PM                =  9 ; 
	public static final int HOUR                 = 10 ;
	public static final int HOUR_OF_DAY          = 11 ;
	public static final int MINUTE               = 12 ;
	public static final int SECOND               = 13 ;
	public static final int MILLISECOND          = 14 ;
	
	public static final int MAX_WEEKS_PER_YEAR =  53 ;
	public static final int MAX_DAYS_PER_YEAR  = 366 ;
	
	/**
	 * Zero argument constructor in order to have this class become serializable  
	 * 
	 **/
	@SuppressWarnings("unused")
	private LdvTime()
	{
		init() ;
		
		_iTimeZone = 0 ;
	}
	
	/**
	 * Default constructor 
	 * 
	 * @param iHourTimeZone time zone expressed in hours
	 * 
	 **/
	public LdvTime(final int iHourTimeZone)
	{
		init() ;
		
		_iTimeZone = SECONDS_IN_HOUR * iHourTimeZone ;
	}
	
	/**
	 * Constructor from a time zone and time information expressed in local time
	 */
	public LdvTime(final int iHourTimeZone, final int year, final int month, final int date, final int hour, final int minute, final int second)
	{
		init() ;
		
		_iTimeZone = SECONDS_IN_HOUR * iHourTimeZone ;
		
		_time.putYears(year) ;
		_time.putMonths(month) ;
		_time.putDays(date) ;
		_time.putHours(hour) ;
		_time.putMinutes(minute) ;
		_time.putSeconds(second) ;
		
		applyOffset() ;
		
		setJulianDay() ;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param model Model to initialize from
	 * 
	 **/
	public LdvTime(final LdvTime model)
	{
		if (null == model)
		{
			init() ;
			return ;
		}
		
		initFromLdvTime(model) ;
	}
	
	/**
	 * Constructor from a Java Date
	 * 
	 * @param javaDate 
	 * 
	 **/
	public LdvTime(final Date javaDate)
	{
		if (null == javaDate)
		{
			init() ;
			return ;
		}
		
		initFromJavaDate(javaDate) ;
	}
	
	/**
	 * Resets the LdvTime object 
	 * 
	 **/
	public void init()       
	{ 
		_time.init() ;
		
		_lJulianDays = 0 ;
		_iTimeZone   = 0 ;
	}
	
	private void resetJulianDays()       
	{
		_lJulianDays = 0 ;
	}
	
	/**
	 * Set the LdvTime object to "infinite in the future" 
	 * 
	 **/
	public void setNoLimit()
	{ 
		_time.setNoLimit() ;
		resetJulianDays() ;
	}
	
	/**
	 * Compute UTC date time from local date time and time zone offset<br>
	 * <br>
	 * 19770422010000 time zone -5 -> 19770422060000 UTC
	 */
	public void applyOffset() 
	{ 
		if ((false == isEmpty()) && (false == isNoLimit()))
			addSeconds(-_iTimeZone, true) ;

		resetJulianDays() ;
	}
	
	/**
	 * Initialize the LdvTime object to current time 
	 * 
	 **/
	public void takeTime() 
	{ 
		Date tNow = new Date() ;
		initFromJavaDate(tNow) ;
	}
	
	/**
	  * Determine whether two LdvTime are exactly similar
	  * 
	  * @return <code>true</code> if all data are the same, <code>false</code> if not
	  * @param otherTime Other LdvTime to compare to
	  * 
	  */
	public boolean equals(final LdvTime otherTime) 
	{ 
		if (null == otherTime)
			return false ;
		
		if (this == otherTime)
			return true ;
		
		return _time.equals(otherTime._time) ;
	}
	
	/**
	  * Determine whether an object is similar to this LdvTime
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

		final LdvTime LdvTimeO = (LdvTime) o ;

		return equals(LdvTimeO) ;
	}
	
	/**
	 * Initialize from another LdvTime object   
	 * 
	 * @param ldvT The {@link LdvTime LdvTime} object to initialize from
	 * 
	 **/
	public void initFromLdvTime(final LdvTime ldvT) 
	{
		init() ;
		
		if (null == ldvT)
			return ;
		
		_iTimeZone = ldvT._iTimeZone ;
		
		initFromUTCDateTime(ldvT.getUTCFullDateTime())  ;
	}
	
	/**
	 * Initializes from a date expressed as a String in local time zone  
	 * 
	 * @param sDate Either "AAAAMMJJ" or "AAAAMMJJHHmmss"
	 * 
	 **/
	public boolean initFromLocalDate(final String sDate) 
	{ 
		if (null == sDate)
			return false ;
		
		int iDateLen = sDate.length() ;		
		if (iDateLen < 8)
			return false ;
		
		if (iDateLen > 8)
			return initFromLocalDateTime(sDate) ;
		
		boolean bReturn = _time.initFromDate(sDate) ;
		
		if (false == bReturn)
    	return false ;
		
    applyOffset() ;
    
    setJulianDay() ;
    
		return true ;
	}
	
	/**
	 * Initializes from a date expressed as a String in time zone 0  
	 * 
	 * @param sDate Either "AAAAMMJJ" or "AAAAMMJJHHmmss"
	 * 
	 **/
	public boolean initFromUTCDate(final String sDate) throws NullPointerException 
	{ 
		if (null == sDate)
			throw new NullPointerException() ;
		
		int iDateLen = sDate.length() ;		
		if (iDateLen < 8)
			return false ;
		
		if (iDateLen > 8)
			return initFromUTCDateTime(sDate) ;
		
		boolean bReturn = _time.initFromDate(sDate) ;
		
		if (false == bReturn)
    	return false ;
		
    setJulianDay() ;
    
		return true ;
	}
	
	/**
	 * Initializes from a date and time expressed as a String in local time zone
	 * 
	 * @param sDateTime Either "AAAAMMJJHHmmss" or "AAAAMMJJHHmmssZZZ"
	 * 
	 **/
	public boolean initFromLocalDateTime(final String sDateTime) throws NullPointerException 
	{ 
		if (null == sDateTime)
			throw new NullPointerException() ;
		
		boolean bReturn = _time.initFromDateTime(sDateTime) ;
		
		if (false == bReturn)
    	return false ;
		
		applyOffset() ;
		
		setJulianDay() ;
		
		return true ;
	}
	
	/**
	 * Initializes from a date and time expressed as a String in time zone 0
	 * 
	 * @param sDateTime Either "AAAAMMJJHHmmss" or "AAAAMMJJHHmmssZZZ"
	 * 
	 **/
	public boolean initFromUTCDateTime(final String sDateTime) throws NullPointerException 
	{ 
		if (null == sDateTime)
			throw new NullPointerException() ;
		
		boolean bReturn = _time.initFromDateTime(sDateTime) ;
		
		if (false == bReturn)
    	return false ;

		setJulianDay() ;
		
		return true ;
	}
	
	/**
	 * Initialize from a java.util.Date object   
	 * 
	 * @param javaDate The {@link java.util.Date Date} object to initialize from
	 * 
	 **/
	@SuppressWarnings("deprecation")
	public void initFromJavaDate(final Date javaDate)
	{
		if (null == javaDate)
			return ;
		
		// So far, neither SimpleDateFormat nor Calendar are available with GWT
		// We have to use deprecated methods from Date				
		int iYear  = javaDate.getYear() + 1900 ;
		int iMonth = javaDate.getMonth() + 1 ;
		int iDay   = javaDate.getDate() ;
		int iHour  = javaDate.getHours() ;
		int iMin   = javaDate.getMinutes() ;
		int iSec   = javaDate.getSeconds() ;
		int iMilli = 0 ;
		
		_time.init(iYear, iMonth, iDay, iHour, iMin, iSec, iMilli) ;
		
		_iTimeZone = javaDate.getTimezoneOffset() * 60 ;
		
		applyOffset() ;
		
		setJulianDay() ;
	}
	
	/**
	 * Return a Java Date object synchronized with this date (returns <code>null</code> if empty or no limit)
	 **/
	@SuppressWarnings("deprecation")
	final public Date toJavaDate()
	{
		if (isEmpty() || isNoLimit())
			return null ;
		
		Date dResult = new Date(getYears() - 1900, getMonths() - 1, getDays(), getHours(), getMinutes(), getSeconds()) ; 
		return dResult ;
	}
	
	@SuppressWarnings("deprecation")
	final public long getUTC() { 
		return Date.UTC(getYears() - 1900, getMonths() - 1, getDays(), getHours(), getMinutes(), getSeconds()) ; 
	}
	
	final public int getHourTimeZone() {
		return _iTimeZone / SECONDS_IN_HOUR ; 
	}
	
	/**
	 * Get the time zone in seconds
	 */
	final public int getTimeZone() {
		return _iTimeZone ;
	}
	
	/**
	 * Set the time zone in seconds
	 */
	public void setTimeZone(final int iTZ) {
		_iTimeZone = iTZ ;
	}
	
	/**
	 * Is this object uninitialized?
	 **/
	final public boolean isEmpty() {
		return _time.isEmpty() ;
	}
	
	/**
	 * Is this object in the infinite future?
	 **/
	final public boolean isNoLimit() {
		return _time.isNoLimit() ;
	}
	
	/**
	 * @return the date in AAAAMMJJhhmmssmmm format for time zone 0
	 **/
	final public String getUTCFullDateTime() { 
		return _time.getFullDateTime() ;
	}
	
	/**
	 * @return the date in AAAAMMJJhhmmssmmm format for local time zone 
	 **/
	final public String getLocalFullDateTime() 
	{
		String sTimeString = _time.getFullDateTime() ;
		
		if (isEmpty() || isNoLimit())
			return sTimeString ;
		
		return addSeconds(sTimeString, _iTimeZone, true) ;
	}
	
	/**
	 * @return the date in AAAAMMJJhhmmss format for time zone 0
	 **/
	final public String getUTCDateTime() {
		return getUTCFullDateTime().substring(0, 14) ;
	}
	
	/**
	 * @return the date in AAAAMMJJhhmmss format for local time zone
	 **/
	final public String getLocalDateTime() {
		return getLocalFullDateTime().substring(0, 14) ;
	}
	
	/**
	 * @return the date in AAAAMMJJ format for time zone 0
	 **/
	final public String getUTCSimpleDate() {
		return getUTCFullDateTime().substring(0, 8) ; 
	}
	
	/**
	 * @return the date in AAAAMMJJ format for local time zone
	 **/
	final public String getLocalSimpleDate() {
		return getLocalFullDateTime().substring(0, 8) ; 
	}
	
	/**
	 * @return the time in hhmmssmmm format for time zone 0
	 **/
	final public String getUTCFullTime() {
		return getUTCFullDateTime().substring(8, 17) ;
	}
	
	/**
	 * @return the time in hhmmssmmm format for local time zone
	 **/
	final public String getLocalFullTime() {
		return getLocalFullDateTime().substring(8, 17) ;
	}
	
	/**
	 * @return the time in hhmmss format for time zone 0
	 **/
	final public String getUTCTime() {
		return getUTCFullDateTime().substring(8, 14) ;
	}
	
	/**
	 * @return the time in hhmmss format for local time zone
	 **/
	final public String getLocalTime() {
		return getLocalFullDateTime().substring(8, 14) ;
	}
	
	/**
	 * Return a date time string in ISO 8601 format (for time 0) from a AAAAMMJJhhmmss... format<br>
	 * <br>
	 * Note, this function is protected, for true ISO 8601 strings, use getUTCISO8601DateTime() or getLocalISO8601DateTime()
	 * 
	 * @param   sDateTime : Date-time in the form AAAAMMJJhhmmss or more accurate
	 * 
	 * @return  A string in the form AAAA-MM-JJThh:mm:ss if sDateTime is valid, <code>null</code> if not
	 */
	final protected String getISO8601(final String sDateTime) throws NullPointerException
	{
		if (null == sDateTime)
			throw new NullPointerException() ;
			
		if ((sDateTime.length() < 14) || (false == MiscellanousFcts.isDigits(sDateTime)))
			return null ;

		return sDateTime.substring(0, 4)  + "-" + sDateTime.substring(4, 6)   + "-" + sDateTime.substring(6, 8) + "T" +
		       sDateTime.substring(8, 10) + ":" + sDateTime.substring(10, 12) + ":" + sDateTime.substring(12, 14) ;
	}
	
	/**
	 * Return the date for time zone 0 in ISO 8601 format (AAAA-MM-JJThh:mm:ssZ) 
	 */
	final public String getUTCISO8601DateTime() {
		return getISO8601(getUTCDateTime()) + "Z" ;
	}
	
	/**
	 * Return the date for local time zone in ISO 8601 format (AAAA-MM-JJThh:mm:ss+th:tm)<br>
	 * <br>
	 * Keep in mind that "2007-04-05T14:30Z" and "2007-04-05T12:30-02:00" are the same moment
	 * 
	 * @param bForceLong If true, then get "2007-04-05T12:30-02:00" instead of "2007-04-05T12:30-02" (have tm appear even if "00")
	 */
	final public String getLocalISO8601DateTime(boolean bForceLong)
	{
		String sISO = getISO8601(getLocalDateTime()) ;
		
		if (null == sISO)
			return null ;
		
		return sISO + getTimeZoneISO8601(bForceLong) ; 
	}
	
	/**
	 * Return the time zone in the form +HH or -HH or +HH:mm or -HH:mm  
	 */
	final public String getTimeZoneISO8601(boolean bForceLong)
	{
		if (0 == _iTimeZone)
			return "" ;
		
		String sReturn = "" ;
		if (_iTimeZone >= 0)
			sReturn = "+" ;
		else
			sReturn = "-" ;
		
		int iAbsTimeZone = Math.abs(_iTimeZone) ;
		
		int iHoursCount  = iAbsTimeZone / SECONDS_IN_HOUR ;
		int iRemains     = iAbsTimeZone - (iHoursCount * SECONDS_IN_HOUR) ;
		
		
		sReturn += LdvInt.setStringToSize("" + Math.abs(iHoursCount), 2) ;
		
		if (0 == iRemains)
		{
			if (bForceLong)
				sReturn += ":00" ;
			
			return sReturn ;
		}
		
		int iMinutesCount = iRemains / SECONDS_IN_MIN ;
		
		return sReturn + ":" + LdvInt.setStringToSize("" + iMinutesCount, 2) ;
	}
	
	final public int getUTCFullYear()       { return isNoLimit() ? -1 : getYears() ; }
	final public int getUTCMonth()          { return isNoLimit() ? -1 : getMonths() ; }
	final public int getUTCDate()           { return isNoLimit() ? -1 : getDays() ; }
	final public int getUTCHours()          { return isNoLimit() ? -1 : getHours() ; }
	final public int getUTCMinutes()        { return isNoLimit() ? -1 : getMinutes() ; }
	final public int getUTCSeconds()        { return isNoLimit() ? -1 : getSeconds() ; }
	final public int getUTCMilliseconds()   { return isNoLimit() ? -1 : getMillis() ; }
	
	final public int getLocalFullYear()     { return isNoLimit() ? -1 : getYY(getLocalFullDateTime()) ; }
	final public int getLocalMonth()        { return isNoLimit() ? -1 : getMM(getLocalFullDateTime()) ; }
	final public int getLocalDate()         { return isNoLimit() ? -1 : getDD(getLocalFullDateTime()) ; }
	final public int getLocalHours()        { return isNoLimit() ? -1 : getHr(getLocalFullDateTime()) ; }
	final public int getLocalMinutes()      { return isNoLimit() ? -1 : getMn(getLocalFullDateTime()) ; }
	final public int getLocalSeconds()      { return isNoLimit() ? -1 : getSe(getLocalFullDateTime()) ; }
	final public int getLocalMilliseconds() { return isNoLimit() ? -1 : getMs(getLocalFullDateTime()) ; }
	
	// ---------------------------------------------------
	//           Getters for UTC strings
	// ---------------------------------------------------
	
	/**
	 * @return the date in ISO 8601 format
	 **/
	final public String getISO8601UTCDateTime() {
		return getISO8601(_time.getFullDateTime()) + "Z" ;
	}
	
	/**
	 * Returns the value of the given calendar field for time zone 0
	 */
	final public int getUTC(int field) throws ArrayIndexOutOfBoundsException
	{
		switch (field)
		{
			case YEAR :
				return getUTCFullYear() ;
			case MONTH :
				return getUTCMonth() ;
			case WEEK_OF_YEAR :
				return getUTCWeekOfYear() ;
			case DATE :
				return getUTCDate() ;
			case DAY_OF_YEAR :
				return getUTCDayOfYear() ;
			case HOUR :
			case HOUR_OF_DAY :
				return getUTCHours() ;
			case MINUTE :
				return getUTCMinutes() ;
			case SECOND :
				return getUTCSeconds() ;
			case MILLISECOND :
				return getUTCMilliseconds() ;
		}
		
		throw new ArrayIndexOutOfBoundsException("Field " + field + " is incorect.") ;
	}
	
	/**
	 * Returns the value of the given calendar field for local time zone
	 */
	final public int getLocal(int field) throws ArrayIndexOutOfBoundsException
	{
		switch (field)
		{
			case YEAR :
				return getLocalFullYear() ;
			case MONTH :
				return getLocalMonth() ;
			case WEEK_OF_YEAR :
				return getLocalWeekOfYear() ;
			case DATE :
				return getLocalDate() ;
			case DAY_OF_YEAR :
				return getLocalDayOfYear() ;
			case HOUR :
			case HOUR_OF_DAY :
				return getLocalHours() ;
			case MINUTE :
				return getLocalMinutes() ;
			case SECOND :
				return getLocalSeconds() ;
			case MILLISECOND :
				return getLocalMilliseconds() ;
		}
		
		throw new ArrayIndexOutOfBoundsException("Field " + field + " is incorect.") ;
	}
	
	/**
	 * Get the count of seconds that lapsed from the beginning of the day for time zone 0 
	 */
	final public int getUTCSecondsOfDay()
	{
		// Doing this would be slow since if would call several times
		// return getSecondsOfDay(getUTCHours(), getUTCMinutes(), getUTCSeconds()) ;
		
		if (isNoLimit() || isEmpty())
			return -1 ;
		
		String sUTCFull = getUTCFullDateTime() ;
		if (null == sUTCFull)
			return -1 ;
		
		return getSecondsOfDay(getHr(sUTCFull), getMn(sUTCFull), getSe(sUTCFull)) ;
	} 
	
	/**
	 * Get the count of milliseconds that lapsed from the beginning of the day for time zone 0 
	 */
	final public long getUTCMillisecondsOfDay()
	{
		// Doing this would be slow since if would call several times
		// return getMillisecondsOfDay(getUTCHours(), getUTCMinutes(), getUTCSeconds(), getUTCMilliseconds()) ;
		
		if (isNoLimit() || isEmpty())
			return -1 ;
		
		String sUTCFull = getUTCFullDateTime() ;
		if (null == sUTCFull)
			return -1 ;
		
		return getMillisecondsOfDay(getHr(sUTCFull), getMn(sUTCFull), getSe(sUTCFull), getMs(sUTCFull)) ;
	}
	
	/**
	 * Get the count of seconds that lapsed from the beginning of the day for local time zone 
	 */
	final public int getLocalSecondsOfDay() {
		return getSecondsOfDay(getLocalHours(), getLocalMinutes(), getLocalSeconds()) ;
	}
	
	/**
	 * Get the count of milliseconds that lapsed from the beginning of the day for local time zone 
	 */
	final public long getLocalMillisecondsOfDay() {
		return getMillisecondsOfDay(getLocalHours(), getLocalMinutes(), getLocalSeconds(), getLocalMilliseconds()) ;
	}
	
	/**
	 * Get the count of seconds that lapsed from the beginning of the day from HH:mm:ss information
	 * 
	 * @param iHours   HH count from HH:mm:ss information
	 * @param iMinutes mm count from HH:mm:ss information
	 * @param iSeconds ss count from HH:mm:ss information
	 */
	protected static int getSecondsOfDay(int iHours, int iMinutes, int iSeconds) {
		return (iHours * SECONDS_IN_HOUR) + (iMinutes * SECONDS_IN_MIN) + iSeconds ;
	}
	
	/**
	 * Get the count of milliseconds that lapsed from the beginning of the day from HH:mm:ss:mmm information
	 * 
	 * @param iHours        HH count from HH:mm:ss:mmm information
	 * @param iMinutes      mm count from HH:mm:ss:mmm information
	 * @param iSeconds      ss count from HH:mm:ss:mmm information
	 * @param iMilliseconds mmm count from HH:mm:ss:mmm information
	 */
	protected static long getMillisecondsOfDay(int iHours, int iMinutes, int iSeconds, int iMilliseconds) {
		return 1000 * getSecondsOfDay(iHours, iMinutes, iSeconds) + iMilliseconds ;
	}
	 
	/**
	 * Set the year
	 * @param iYear Complete year (for example <code>2017</code>)
	 **/
	public void putYear(final int iYear)
	{ 
		_time.putYears(iYear) ;
		setJulianDay() ; 
	}
	
	/**
	 * Set the month
	 * @param iMonth Month (in the 1 (for January) - 12 (for December) range)
	 **/
	public void putMonth(final int iMonth) throws IllegalArgumentException
	{
		if ((iMonth < 1) || (iMonth > 12))
			throw new IllegalArgumentException("" + iMonth + " is not a valid month") ;

		_time.putMonths(iMonth) ;
		setJulianDay() ;
	}
	
	/**
	 * Set the day slot
	 * @param iDay Day (in the 1-31 range)
	 **/
	public void putDate(final int iDay) throws IllegalArgumentException
	{
		if ((iDay < 1) || (iDay > 31))
			throw new IllegalArgumentException("" + iDay + " is not a valid date") ;
		
		_time.putDays(iDay) ;
		setJulianDay() ;
	}
	
	/**
	 * Set the hours
	 * 
	 * @param iHour Hours (in the [0-23] interval)
	 * @throws IllegalArgumentException
	 **/
	public void putHours(final int iHour) throws IllegalArgumentException
	{
		if ((iHour < 0) || (iHour > 23))
			throw new IllegalArgumentException("" + iHour + " is not a valid hours count (should be in the [0-23] interval)") ;
		
		_time.putHours(iHour) ;
		
		// no need to call setJulianDay() since setting hours in a [0-23] interval doesn't change the day
	}
	
	/**
	 * Set the minutes
	 *  
	 * @param iMinutes Minutes (in the [0-59] interval)
	 * @throws IllegalArgumentException
	 */
	public void putMinutes(final int iMinutes) throws IllegalArgumentException      
	{ 
		if ((iMinutes < 0) || (iMinutes > 59))
			throw new IllegalArgumentException("" + iMinutes + " is not a valid minutes count (should be in the [0-59] interval)") ;
		
		_time.putMinutes(iMinutes) ;
		
		// no need to call setJulianDay() since setting minutes in a [0-59] interval doesn't change the day
	}
	
	/**
	 * Set the seconds
	 * 
	 * @param iSeconds Seconds (in the [0-59] interval)
	 * @throws IllegalArgumentException
	 */
	public void putSeconds(final int iSeconds) throws IllegalArgumentException
	{ 
		if ((iSeconds < 0) || (iSeconds > 59))
			throw new IllegalArgumentException("" + iSeconds + " is not a valid seconds count (should be in the [0-59] interval)") ;
		
		_time.putSeconds(iSeconds) ; 
		
		// no need to call setJulianDay() since setting seconds in a [0-59] interval doesn't change the day
	}
	
	/**
	 * Set the milliseconds
	 * 
	 * @param iMilli Milliseconds (in the [0-999] interval)
	 * @throws IllegalArgumentException
	 */
	public void putMilliseconds(final int iMilli) throws IllegalArgumentException
	{ 
		if ((iMilli < 0) || (iMilli > 999))
			throw new IllegalArgumentException("" + iMilli + " is not a valid milliseconds count (should be in the [0-999] interval)") ;
		
		_time.putMillis(iMilli) ;
		
		// no need to call setJulianDay() since setting milliseconds in a [0-999] interval doesn't change the day
	}
	
	/**
	 * If not the case, adapt time structure to a valid date time
	 */
	public void normalize()
	{
		// Year
		if (getYears() <= 0)
			putYear(1900) ;
		
		// Month
		if (getMonths() <= 0)
			putMonth(1) ;
		if (getMonths() > 12)
			putMonth(12) ;
		
		// Date
		int iDate = getDays() ; 
		if (iDate <= 0)
		{
			putDate(1) ;
			return ;
		}
		if (iDate < 29)
			return ;
		
		int iMonth = getMonths() ; 
		
		int iLeap = 0 ;
		if (isLeapYear(getYears()))
			iLeap++ ;
		
		int iD = daytab[iLeap][iMonth] ;
		if (iDate > iD)
			putDate(iD) ;
	}
	
	//---------------------------------------------------
	//                 Add functions
	// --------------------------------------------------
	
	/**
	 * Adds or subtracts the specified amount of time to the given calendar field, based on the calendar's rules.
	 * 
	 * @param field 
	 * @param amount
	 * @param bAdjust
	 */
	public void add(int field, int amount, final boolean bAdjust)
	{
		switch (field)
		{
			case YEAR :
				addYears(amount, bAdjust) ;
				break ;
			case MONTH :
				addMonths(amount, bAdjust) ;
				break ;
			case WEEK_OF_YEAR :
				addMonths(amount, bAdjust) ;
				break ;
			case DATE :
			case DAY_OF_YEAR :
				addDays(amount, bAdjust) ;
				break ;
			case HOUR :
			case HOUR_OF_DAY :
				addHours(amount, bAdjust) ;
				break ;
			case MINUTE :
				addMinutes(amount, bAdjust) ;
				break ;
			case SECOND :
				addSeconds(amount, bAdjust) ;
				break ;
		}
	}
	
	public void addYears(final int iAdd, final boolean bAdjust) 
	{ 
		if (0 == iAdd)
			return ;
		
		addMonths(12 * iAdd, bAdjust) ; 
	}
	
	public void addMonths(final int iAdd, final boolean bAdjust)
	{  
		if (0 == iAdd)
			return ;
		
		/*
		* Add a certain amount of months
		* bAjust is true when we want to adjust the date in order to get a genuine one
		* for example february the 31th becomes february the 28th
		* When bAjust is set to false, it allows to get "fake" dates such as "every end
		* of month" when the day is set to the 31th ; these fake dates will be adjusted
		* by the donneXXX methods
		*/
		int iYear = getYears() ;
		int iMonthsFrom1800 = (iYear - 1800) * 12 + getMonths() + iAdd ;

		float iNbrOfYears = iMonthsFrom1800 / 12 ;
		int   iYearsFloor = (int) Math.floor(iNbrOfYears) ;

		if (0 == (iMonthsFrom1800 % 12))
		{
			putMonth(12) ;
			putYear(iYearsFloor - 1 + 1800) ;
		}
		else 
		{
			putMonth(iMonthsFrom1800 % 12) ;
			putYear(iYearsFloor + 1800) ;
		}

		if (true == bAdjust)
			normalize() ;
		
		setJulianDay() ;
	}
	
	public void addWeeks(final int iAdd, final boolean bAdjust)    
	{ 
		if (0 == iAdd)
			return ;
		
		addDays(7 * iAdd, bAdjust) ;		
	}
	
	public void addDays(final int iAdd, final boolean bAdjust)    
	{ 
		if (0 == iAdd)
			return ;
		
		setJulianDay() ;
		_lJulianDays += iAdd ;
		initFromJulian() ;		
	}
	
	public void addHours(final int iAdd, final boolean bAdjust) { 
		addSeconds(iAdd * SECONDS_IN_HOUR, bAdjust) ;
	}
	
	public void addMinutes(final int iAdd, final boolean bAdjust) {
		addSeconds(iAdd * SECONDS_IN_MIN, bAdjust) ;
	}
	
	/**
	 * Adds (or removes) seconds
	 * 
	 * @param iAdd    Count of seconds to add (or remove if negative)
	 * @param bAdjust 
	 */
	public void addSeconds(final int iAdd, final boolean bAdjust) 
	{ 
		if (0 == iAdd)
			return ;
		
		int sec_of_day = getSecondsOfDay() ;
		sec_of_day += iAdd ;
		
		// Since we got the seconds of day to add it later, we have to reinitialize time information
		//
		putHours(0) ;
		putMinutes(0) ;
		putSeconds(0) ;
		
		dispatchSecondsOfDay(sec_of_day) ;
		
		setJulianDay() ;
	}
	
	/**
	 * Return a date time computed from adding seconds to another one
	 * 
	 * @param sDateTime Reference date time
	 * @param iAdd      Number of seconds to add
	 * @param bAdjust   
	 * 
	 * @return
	 */
	public static String addSeconds(final String sDateTime, final int iAdd, final boolean bAdjust) throws NullPointerException 
	{ 
		if (null == sDateTime)
			throw new NullPointerException() ;
		
		if (0 == iAdd)
			return sDateTime ;
		
		int iHours   = getHr(sDateTime) ;
		int iMinutes = getMn(sDateTime) ;
		int iSeconds = getSe(sDateTime) ;
		int sec_of_day = getSecondsOfDay(iHours, iMinutes, iSeconds) ;
		
		sec_of_day += iAdd ;
		
		return dispatchSecondsOfDay(sDateTime, sec_of_day) ;
	}
	
	/**
	 * Adds or subtracts (up/down) a single unit of time on the given time field without changing larger fields.
	 */
	public void roll(int field, boolean up)
	{
		int amount = up ? 1 : -1 ;
		roll(field, amount) ;
	}
		
	/**
	 * Adds the specified (signed) amount to the specified calendar field without changing larger fields
	 */
	public void roll(int field, int amount)
	{
		switch (field)
		{
			case YEAR :
				addYears(amount, true) ;
				break ;
			case MONTH :
				int iMonth = getMonths() + amount ;
				while ((iMonth < 1) && (iMonth > 12))
				{
					if (iMonth < 1)
						iMonth += 12 ;
					else
						iMonth -= 12 ;
				}
				putMonth(iMonth) ;
				break ;
			case WEEK_OF_YEAR :
				roll(DATE, 7 * amount) ;
				break ;
			case DATE :
			case DAY_OF_YEAR :
				int iDate   = getDays() + amount ;
				int iMaxDay = daysUTCCountWithinMonth() ;
				while ((iDate < 1) && (iDate > iMaxDay))
				{
					if (iDate < 1)
						iDate += iMaxDay ;
					else
						iDate -= iMaxDay ;
				}
				putDate(iDate) ;
				break ;
			case HOUR :
			case HOUR_OF_DAY :
				int iHours = getHours() + amount ;
				while ((iHours < 0) && (iHours > 23))
				{
					if (iHours < 0)
						iHours += 24 ;
					else
						iHours -= 24 ;
				}
				putHours(iHours) ;
				break ;
			case MINUTE :
				int iMinutes = getMinutes() + amount ;
				while ((iMinutes < 0) && (iMinutes > 59))
				{
					if (iMinutes < 0)
						iMinutes += 60 ;
					else
						iMinutes -= 60 ;
				}
				putMinutes(iMinutes) ;
				break ;
			case SECOND :
				int iSeconds = getSeconds() + amount ;
				while ((iSeconds < 0) && (iSeconds > 59))
				{
					if (iSeconds < 0)
						iSeconds += 60 ;
					else
						iSeconds -= 60 ;
				}
				putSeconds(iSeconds) ;
				break ;
			case MILLISECOND :
				int iMillis = getMillis() + amount ;
				while ((iMillis < 0) && (iMillis > 999))
				{
					if (iMillis < 0)
						iMillis += 1000 ;
					else
						iMillis -= 1000 ;
				}
				putMilliseconds(iMillis) ;
				break ;
		}
	}
	
	//---------------------------------------------------
	//                 Diff functions
	// ---------------------------------------------------
	
	/**
	 * Is this time strictly before the other one?
	 * 
	 * Always returns <code>false</code> if other is "no limit" (even if this one is also "no limit") 
	 */
	public boolean isBefore(final LdvTime other) throws NullPointerException, IllegalArgumentException
	{
		if (null == other)
			throw new NullPointerException() ;
		
		if (other.isEmpty())
			throw new IllegalArgumentException("Uninitialized parameter") ;
		
		if (this.isEmpty())
			return true ;
		if (this.isNoLimit())
			return false ;
		if (other.isNoLimit())
			return true ;
		
    long day_dif = this.getUTCJulianDay() - other.getUTCJulianDay() ;
    if (day_dif < 0)
    	return true ;
    if (day_dif > 0) 
    	return false ;
    
    return (this.getUTCMillisecondsOfDay() < other.getUTCMillisecondsOfDay()) ;
 	}
	
	/**
	 * Is this time strictly after the other one?
	 * 
	 * Always returns <code>false</code> if other is "no limit" (even if this one is also "no limit") 
	 */
	public boolean isAfter(final LdvTime other) throws NullPointerException, IllegalArgumentException
	{
		if (null == other)
			throw new NullPointerException() ;
		
		if (other.isEmpty())
			throw new IllegalArgumentException("Uninitialized parameter") ;
		
		if (other.isNoLimit())
			return false ;
		
		if (this.isEmpty())
			return false ;
		if (this.isNoLimit())
			return true ;
		
    long day_dif = this.getUTCJulianDay() - other.getUTCJulianDay() ;
    if (day_dif > 0)
    	return true ;
    if (day_dif < 0) 
    	return false ;
    
    return (this.getUTCMillisecondsOfDay() > other.getUTCMillisecondsOfDay()) ;
 	}
	
	/**
	 * Add or remove seconds to second 0 of current day
	 * 
	 * @param iSecondsOfDay count of seconds to add (if > 0) or to remove (if < 0) 
	 */
	private void dispatchSecondsOfDay(final int iSecondsOfDay)
	{
		// We will add or remove seconds to the number of Julian days for current date-time
		//
		setJulianDay() ;
		
		int iSecOfDay = iSecondsOfDay ;
		
		// While the count of seconds to add is greater than the number of seconds in a day, we add days
		//
    while (iSecOfDay >= SECONDS_IN_DAY)
    {
    	iSecOfDay -= SECONDS_IN_DAY ;
       _lJulianDays++ ;
    }
    
    // While the count of seconds is negative, we remove a plain day and add the corresponding seconds to the count 
    //
    while (iSecOfDay < 0)
    {
    	iSecOfDay += SECONDS_IN_DAY ;
       _lJulianDays-- ;
    }
    
    // Converting back from Julian days
    //
    initFromJulian() ;
    
    // At this point, iSecOfDay is in the [0, SECONDS_IN_DAY[ interval
    //
    
    // Count the number of plain hours
    //
    int iHour = 0 ;
    while (iSecOfDay >= SECONDS_IN_HOUR)
    {
    	iSecOfDay -= SECONDS_IN_HOUR ;
    	iHour++ ;
    }
    
    // Count the number of plain minutes
    //
    int iMinute = 0 ;
    while (iSecOfDay >= SECONDS_IN_MIN)
    {
    	iSecOfDay -= SECONDS_IN_MIN ;
    	iMinute++ ;
    }
    
    putHours(iHour) ;
  	putMinutes(iMinute) ;
  	putSeconds(iSecOfDay) ;   
	}
	
	private static String dispatchSecondsOfDay(String sDateTime, int iSecOfDay)
	{
		int iDD = getDD(sDateTime) ;
		int iMM = getMM(sDateTime) ;
		int iYY = getYY(sDateTime) ;
		
		long lJulianDays = getJulianDaysFromGregorian(iDD, iMM, iYY) ;
		
		if (0 == lJulianDays)
			lJulianDays = getGlobalJulianDay(iDD, iMM, iYY) ;
		
    while (iSecOfDay >= SECONDS_IN_DAY)
    {
    	iSecOfDay -= SECONDS_IN_DAY ;
    	lJulianDays++ ;
    }
    while (iSecOfDay < 0)
    {
    	iSecOfDay += SECONDS_IN_DAY ;
    	lJulianDays-- ;
    }
    
    String sResult = initFromJulian(lJulianDays, sDateTime) ;
    
    int iHour = 0 ;
    while (iSecOfDay >= SECONDS_IN_HOUR)
    {
    	iSecOfDay -= SECONDS_IN_HOUR ;
    	iHour++ ;
    }
    
    int iMinute = 0 ;
    while (iSecOfDay >= SECONDS_IN_MIN)
    {
    	iSecOfDay -= SECONDS_IN_MIN ;
    	iMinute++ ;
    }
    
    sResult = putHr(sResult, iHour) ;
    sResult = putMn(sResult, iMinute) ;
    sResult = putSe(sResult, iSecOfDay) ;
  	
  	return sResult ;
	}
	
	/**
	 * Add milliseconds to current date-time
	 * 
	 * @param iAdd
	 * @param bAdjust
	 */
	// @SuppressWarnings("deprecation")
  public void addMilliseconds(int iAdd, boolean bAdjust) 
	{
		if (iAdd > 0)
		{
			int iNewMilli = getMillis() + iAdd ;
			if (iNewMilli < 1000)
				putMilliseconds(iNewMilli) ;
			else
			{
				double dSeconds = iNewMilli / 1000 ;
				
				int iSeconds = (int) Math.floor(dSeconds) ;
				int iMilli   = iNewMilli - (iSeconds * 1000) ;
				
				putMilliseconds(iMilli) ;
				addSeconds(iSeconds, bAdjust) ;
				return ;
			}
		}
		else if (iAdd < 0)
		{
			int iNewMilli = getMillis() + iAdd ;
			if (iNewMilli >= 0)
				putMilliseconds(iNewMilli) ;
			else
			{
				iNewMilli = Math.abs(iNewMilli) ;
				
				double dSeconds = iNewMilli / 1000 ;
				
				int iSeconds = (int) Math.floor(dSeconds) ;
				int iMilli   = iNewMilli - (iSeconds * 1000) ;
				
				putMilliseconds(1000 - iMilli) ;
				addSeconds(-iSeconds-1, bAdjust) ;
				return ;
			}
		}
		
		if (true == bAdjust)
			this.normalize() ;
		
		resetJulianDays() ;
		
/*		
		if (getUTCFullYear() < 1970)
			return ;
		
		long numMs = getUTC() ;

		Date previousDate = new Date(numMs) ;
		int iOffset = previousDate.getTimezoneOffset() ;
		
		numMs += iAdd + 60000 * iOffset ;

		Date addedDate = new Date(numMs) ;
		this.initFromJavaDate(addedDate) ;

		if (true == bAdjust)
			this.normalize() ;
		
		resetJulianDays() ;
*/
	}

  /**
   * Get the interval of time, in milliseconds, between this date:time and another one
   * 
   * @return The interval of time (positive if <code>this</code> is after other) if all went well, <code>Long.MAX_VALUE</code> if not
   */
	public long deltaMilliseconds(final LdvTime otherDate)
	{
		if (null == otherDate)
			return Long.MAX_VALUE ;
		
		long thisUTC  = getUTC() ;
		long otherUTC = otherDate.getUTC() ;
		
		return thisUTC - otherUTC ;
	}
	
	/**
   * Get the interval of time, in seconds, between this date:time and another one
   * 
   * @return The interval of time (positive if <code>this</code> is after other) if all went well, <code>Long.MAX_VALUE</code> if not
   */
	public long deltaSeconds(final LdvTime otherDate)
	{
		if (null == otherDate)
			return Long.MAX_VALUE ;
		
		return (long) ((float) deltaMilliseconds(otherDate) / (float) 1000) ;
	}
	
	/**
   * Get the interval of time, in minutes, between this date:time and another one
   * 
   * @return The interval of time (positive if <code>this</code> is after other) if all went well, <code>Long.MAX_VALUE</code> if not
   */
	public long deltaMinutes(LdvTime otherDate)
	{
		if (null == otherDate)
			return Long.MAX_VALUE ;
		
		return (long) ((float) deltaMilliseconds(otherDate) / (float) 60000) ;
	}
	
	/**
	 * Get an interval in days between this date and otherDate for time zone 0<br>
	 * <br>
	 * Take care that 20170101:230000(-1Z) and 20170102:20170102:000000(-1Z) are in the same day in UTC but not in local time 
	 * 
	 * @param otherDate The other date
	 * 
	 * @return Number of days between the two dates (positive if <code>this</code> is after other)
	 */
	public long deltaDaysUTC(final LdvTime otherDate) { 
		return _lJulianDays - otherDate._lJulianDays ;
	}
	
	/**
	 * Get an interval in days between this date and otherDate for local time zone<br>
	 * <br>
	 * Take care that 20170101:230000(-1Z) and 20170102:20170102:000000(-1Z) are in the same day in UTC but not in local time 
	 * 
	 * @param otherDate The other date
	 * 
	 * @return Number of days between the two dates (positive if <code>this</code> is after other)
	 */
	public long deltaDaysLocal(final LdvTime otherDate) {
		return getLocalJulianDay() - otherDate.getLocalJulianDay() ;
	}
	
	/**
	 * Is a given year a leap year?
	 * 
	 * @param iYear Complete year (for example <code>2017</code>)
	 * 
	 * @return <code>true</code> if a leap year, <code>false</code> if not
	 * 
	 **/
	public static boolean isLeapYear(final int iYear)
	{
		// Rule : If year is a multiple of 4 AND not a multiple of 100 OR a multiple of 400, it is bisextile
		return (iYear % 4) == 0 && (iYear % 100 != 0 || iYear % 400 == 0) ;
	}

	/**
	 * Is a given date valid?
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return <code>true</code> if this day is valid, <code>false</code> if not
	 * 
	 **/
	public static boolean isValidDay(final int iYear, final int iMonth, final int iDay)
	{
		if ((iMonth < 1) || (iMonth > 12) || (iDay < 1))
			return false ;
			
		return isDayWithinMonth(iMonth, iDay, iYear) ;
	}
	
	/**
	 * Get the day of the week from a given date
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The day of the week (1 represents Monday, 7 represents Sunday). 
	 * 
	 **/
	public static int getDayOfWeek(final int iYear, final int iMonth, final int iDay) throws IllegalArgumentException
	{
		if (false == isValidDay(iYear, iMonth, iDay))
			throw new IllegalArgumentException("" + iYear + "-" + iMonth + "-" + iDay + " is not a valid date") ;
		
		// Optimized version of Zeller's congruence (https://en.wikipedia.org/wiki/Zeller%27s_congruence)
		//             from https://codereview.stackexchange.com/questions/67722/its-friday-zellers-congruence-revisited
		//
		final boolean isJanOrFeb = iMonth < 3 ;
		final int     iM = iMonth + (isJanOrFeb ? 12 : 0) + 1 ;
		final int     iCalcyear = iYear - (isJanOrFeb ? 1 : 0) ;
		final int     iK = iCalcyear % 100 ;
		final int     iJ = iCalcyear / 100 ;

		int offset = iDay + (13 * iM) / 5 + iK + (iK / 4) + (iJ * 5) + (iJ / 4) ;

		return ((offset + 5) % 7) + 1 ;		
	}
	
	/**
	 * Get the day of the week for local time zone
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The day of the week (1 represents Monday, 7 represents Sunday) or 0 if no limit 
	 * 
	 **/
	public int getLocalDayOfWeek()
	{
		if (isNoLimit())
			return 0 ;

		int iDoW = 0 ;
		try {
			iDoW = getDayOfWeek(getLocalFullYear(), getLocalMonth(), getLocalDate()) ;
		}
		finally {	
		}
		
		return iDoW ; 
	}
	
	/**
	 * Get the day of the week for time zone 0
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The day of the week (1 represents Monday, 7 represents Sunday) or 0 if no limit 
	 * 
	 **/
	public int getUTCDayOfWeek()
	{
		if (isNoLimit())
			return 0 ;

		int iDoW = 0 ;
		try {
			iDoW = getDayOfWeek(getYears(), getMonths(), getDays()) ;
		}
		finally {	
		}
		
		return iDoW ; 
	}
	
	/**
	 * Is a given year a year that contains 53 weeks?
	 * 
	 * @param iYear Complete year (for example <code>2017</code>)
	 * 
	 * @return <code>true</code> if a year containing 53 weeks, <code>false</code> if not
	 * 
	 **/
	public static boolean is53WeeksYear(final int iYear)
	{
		// From https://en.wikipedia.org/wiki/ISO_week_date
		//
		// The long years, with 53 weeks in them, can be described by any of the following equivalent definitions:
    // - any year starting on Thursday (dominical letter D or DC) and any leap year starting on Wednesday (ED)
    // - any year ending on Thursday (D, ED) and any leap year ending on Friday (DC)
    // - years in which 1 January and 31 December (in common years) or either (in leap years) are Thursdays
		//
		// We use the first rule
		//
		int iDoWForJanuaryOne = getDayOfWeek(iYear, 1, 1) ;
		
		if (4 == iDoWForJanuaryOne)
			return true ;
		
		if (isLeapYear(iYear))
			return (3 == iDoWForJanuaryOne) ; 
		
		return false ;
	}
	
	/**
	 * Is time zone 0 in a year that contains 53 weeks?
	 * 
	 * @return <code>true</code> if a year containing 53 weeks, <code>false</code> if not
	 * 
	 **/
	public boolean isUTC53WeeksYear() {
		return is53WeeksYear(getYears()) ;
	}
	
	/**
	 * Is local time in a year that contains 53 weeks?
	 * 
	 * @return <code>true</code> if a year containing 53 weeks, <code>false</code> if not
	 * 
	 **/
	public boolean isLocal53WeeksYear()
	{
		String sLocalDate = getLocalFullDateTime() ;
		
		return is53WeeksYear(getYY(sLocalDate)) ;
	}
	
	/**
	 * Get the week-of-year (in the ISO 8601 definition) for a given date
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The week-of-year in the 1-53 interval 
	 * 
	 **/
	public static int getWeekOfYear(final int iYear, final int iMonth, final int iDay) throws IllegalArgumentException
	{
		if (false == isValidDay(iYear, iMonth, iDay))
			throw new IllegalArgumentException("" + iYear + "-" + iMonth + "-" + iDay + " is not a valid date") ;
		
		// From https://en.wikipedia.org/wiki/ISO_week_date#First_week
		// 
		// The ISO 8601 definition for week 01 is the week with the Gregorian year's first Thursday in it.
		//
		// The following definitions based on properties of this week are mutually equivalent, since the ISO week starts with Monday:
		//
    // It is the first week with a majority (4 or more) of its days in January.
    // Its first day is the Monday nearest to 1 January.
    // It has 4 January in it. Hence the earliest possible first week extends from Monday 29 December (previous Gregorian year) to Sunday 4 January, the latest possible first week extends from Monday 4 January to Sunday 10 January.
    // It has the year's first working day in it, if Saturdays, Sundays and 1 January are not working days.
		//
		// If 1 January is on a Monday, Tuesday, Wednesday or Thursday, it is in week 01. If 1 January is on a Friday, it is part of week 53 of the previous year; if on a Saturday, it is part of week 52 (or 53 if the previous Gregorian year was a leap year); if on a Sunday, it is part of week 52 of the previous year.
		//
		
		// Get the date for the Monday inside the same week
		//
		int iDay4Monday   = iDay ;
		int iMonth4Monday = iMonth ;
		int iYear4Monday  = iYear ;
		
		int iDoW = getDayOfWeek(iYear, iMonth, iDay) ;
		if (iDoW > 1)
		{
			for (int iToM = 1 ; iToM < iDoW ; iToM++)
			{
				iDay4Monday-- ;
				if (iDay4Monday < 1)
				{
					iMonth4Monday-- ;
					if (iMonth4Monday < 1)
					{
						iMonth4Monday = 12 ;
						iYear4Monday-- ;
					}
					
					int iLeap = 0 ;
					if ((2 == iMonth4Monday) && isLeapYear(iYear4Monday))
						iLeap++ ;
					
					iDay4Monday = daytab[iLeap][iMonth4Monday] ;
				}
			}
		}
		
		// Special case, Monday belongs to previous year
		//
		if (iYear4Monday < iYear)
		{
			// The earliest possible first week extends from Monday 29 December (previous Gregorian year) to Sunday 4 January
			// So, if Monday is >= 29/12, then Thursday is in January and we are inside the first week
			//
			if (iDay4Monday >= 29)
				return 1 ;
			
			// Since Monday is < 29/12 then it is the latest week of previous year
			// We have to check if it is week 52 or week 53
			//
			if (is53WeeksYear(iYear4Monday))
				return 53 ;
			return 52 ;
		}
		
		// Get the day of year for Monday 
		//
		int iDoY4Monday = getDayOfYear(iYear4Monday, iMonth4Monday, iDay4Monday) ;
		
		// First week of the year is week 1, not week 0, so we have to add 1
		//
		int iWeeksCount = iDoY4Monday / 7 + 1 ;
		
		// Count the number of days in January before the first Monday
		// For the previous Thursday to belong to previous week, we must have at least 4 remaining days (Thursday, Friday, Saturday, Sunday) 
		//
		int iRemaining = iDoY4Monday - (7 * iWeeksCount) ;
		if (iRemaining >= 4)
			iWeeksCount++ ;
		
		return iWeeksCount ;
	}
	
	/**
	 * Get the week-of-year (in the ISO 8601 definition) for time zero
	 * 
	 * @return The week-of-year in the 1-53 interval 
	 * 
	 **/
	public int getUTCWeekOfYear()
	{
		if (isNoLimit())
			return 0 ;

		int iWoY = 0 ;
		try {
			iWoY = getWeekOfYear(_time.getYears(), _time.getMonths(), _time.getDays()) ;
		}
		finally {	
		}
		
		return iWoY ;
	}
	
	/**
	 * Get the week-of-year (in the ISO 8601 definition) for local time
	 * 
	 * @return The week-of-year in the 1-53 interval 
	 * 
	 **/
	public int getLocalWeekOfYear()
	{
		if (isNoLimit())
			return 0 ;

		String sTimeString = getLocalFullDateTime() ;
		
		int iWoY = 0 ;
		try {
			iWoY = getWeekOfYear(getYY(sTimeString), getMM(sTimeString), getDD(sTimeString)) ;
		}
		finally {	
		}
		
		return iWoY ;
	}
	
	/**
	 * Get the day-of-year 
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The day-of-year in the 1-365 interval 
	 * 
	 **/
	public static int getDayOfYear(final int iYear, final int iMonth, final int iDay) throws IllegalArgumentException
	{
		if (false == isValidDay(iYear, iMonth, iDay))
			throw new IllegalArgumentException("" + iYear + "-" + iMonth + "-" + iDay + " is not a valid date") ;

		if (1 == iMonth)
			return iDay ;
		
		int iDoY = iDay ;
		
		for (int iM = 1 ; iM < iMonth ; iM++)
		{
			int iLeap = 0 ;
			if ((2 == iM) && isLeapYear(iYear))
				iLeap++ ;
			
			iDoY += daytab[iLeap][iM] ;
		}
		
		return iDoY ;
	}
	
	/**
	 * Get the day of the year for time zone 0
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The day of the year in the 1-365 interval 
	 * 
	 **/
	public int getUTCDayOfYear()
	{
		if (isNoLimit())
			return 0 ;

		int iDoW = 0 ;
		try {
			iDoW = getDayOfYear(_time.getYears(), _time.getMonths(), _time.getDays()) ;
		}
		finally {	
		}
		
		return iDoW ; 
	}
	
	/**
	 * Get the day of the year for local time
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return The day of the year in the 1-365 interval 
	 * 
	 **/
	public int getLocalDayOfYear()
	{
		if (isNoLimit())
			return 0 ;

		String sTimeString = getLocalFullDateTime() ;
		
		int iDoW = 0 ;
		try {
			iDoW = getDayOfYear(getYY(sTimeString), getMM(sTimeString), getDD(sTimeString)) ;
		}
		finally {	
		}
		
		return iDoW ; 
	}
	
	/**
	 * Sets the internal Julian day from internal day, month, year information  
	 * 
	 **/
	public void setJulianDay() {		
		_lJulianDays = computeUTCJulianDays() ;
	}
	
	/**
	 * Get the Julian day for local time zone 
	 */
	public final long getLocalJulianDay()
	{
		setJulianDay() ;
		
		long lJulDay = _lJulianDays ;

		// Add offset to the seconds of day for local zone
		//
		int iSecOfDay = getLocalSecondsOfDay() ;
		iSecOfDay += _iTimeZone ;
		
		// While the count of seconds to add is greater than the number of seconds in a day, we add days
		//
		while (iSecOfDay >= SECONDS_IN_DAY)
		{
			iSecOfDay -= SECONDS_IN_DAY ;
			lJulDay++ ;
		}
	    
		// While the count of seconds is negative, we remove a plain day and add the corresponding seconds to the count 
		//
		while (iSecOfDay < 0)
		{
			iSecOfDay += SECONDS_IN_DAY ;
			lJulDay-- ;
		}
		
		return lJulDay ;
	}
	
	/**
	 * Get the Julian day for time zone 0 
	 */
	public final long getUTCJulianDay()
	{
		// Compute the Julian day
		//
		setJulianDay() ;
		
		return _lJulianDays ;
	}
	
	/**
	 * Sets the internal Julian day for local time zone from internal day, month, year information  
	 * 
	 **/
	private void computeLocalJulianDays()
	{
		int iDD = getLocalDate() ;
		int iMM = getLocalMonth() ;
		int iYY = getLocalFullYear() ;
		
		_lJulianDays = computeJulianDays(iDD, iMM, iYY) ;
	}

	/**
	 * Compute the Julian day from day, month, year information 
	 *  
	 * @param iDD day of the month
	 * @param iMM month of the year
	 * @param iYY year
	 */
	private static long computeJulianDays(int iDD, int iMM, int iYY)
	{
		long lJulianDays = getJulianDaysFromGregorian(iDD, iMM, iYY) ;
		
		if (0 == lJulianDays)
			lJulianDays = getGlobalJulianDay(iDD, iMM, iYY) ;
		
		return lJulianDays ;
	}
	
	/**
	 * Sets the internal Julian day for time zone 0 from internal day, month, year information  
	 * 
	 **/
	private long computeUTCJulianDays()
	{
		int iDD = getDays() ;
		int iMM = getMonths() ;
		int iYY = getYears() ;
		
		return computeJulianDays(iDD, iMM, iYY) ;
	}
	
	//
	// Convert Gregorian calendar date to the corresponding Julian day
	// number j.  Algorithm 199 from Communications of the ACM, Volume 6, No.
	// 8, (Aug. 1963), p. 444.  Gregorian calendar started on Sep. 14, 1752.
	// This function not valid before that.
	// Returns 0 if the date is invalid.
	//	
	protected static long getJulianDaysFromGregorian(int iDD, int iMM, int iYY) 
	{
		long num_days_in_four_jul_years = 1461 ;
		long num_jul_days_to_zero       = 1721119L ;
		long num_jul_days_in_century    = 146097L ;
		
		if (false == isDayWithinMonth(iMM, iDD, iYY))
			return 0 ;

		if (iMM > 2)
			iMM -= 3 ;
		else
		{
			iMM += 9 ;
			iYY-- ;
		}

		long c = iYY / 100 ;
		long ya = iYY - 100 * c ;
		return ((num_jul_days_in_century * c) >> 2) + 
		       ((num_days_in_four_jul_years * ya) >> 2) + 
		       (153 * iMM + 2) / 5 + iDD + num_jul_days_to_zero ;
	}
	
	/**
   * @return The Julian day number that begins at noon of this day
   * Positive year signifies A.D., negative year B.C.
   * Remember that the year after 1 B.C. was 1 A.D.
   *
   * A convenient reference point is that May 23, 1968 noon is Julian day 2440000.
   *
   * Julian day 0 is a Monday.
   *
   * This algorithm is from Press et al., Numerical Recipes in C, 2nd ed., Cambridge University Press 1992
   */
	private static long getGlobalJulianDay(int iDD, int iMM, int iYY) 
	{
     int jy = iYY ;
     if (iYY < 0) 
    	 jy++ ;
     
     int jm = iMM ;
     if (iMM > 2) 
    	 jm++ ;
     else
     {  
    	 jy-- ;
       jm += 13 ;
     }
     
     int jul = (int) (java.lang.Math.floor(365.25 * jy) + java.lang.Math.floor(30.6001*jm) + iDD + 1720995.0) ;

     int IGREG = 15 + 31 * (10 + 12 * 1582) ;
     // Gregorian Calendar adopted Oct. 15, 1582

     if (iDD + 31 * (iMM + 12 * iYY) >= IGREG)
     // change over to Gregorian calendar
     {  
    	 int ja = (int)(0.01 * jy) ;
       jul += 2 - ja + (int)(0.25 * ja);
     }
     return jul ;
  }

	public boolean initFromJulian() 
	{
		_time.initFromDateTime(initFromJulian(_lJulianDays, _time.getFullDateTime())) ;
		return true ;
	}
	
	public boolean initFromJulian(long lJulianDay) 
	{
		_time.initFromDateTime(initFromJulian(lJulianDay, _time.getFullDateTime())) ;
		return true ;
	}
	
	/**
   * Converts a Julian day to a calendar date
   * This algorithm is from Press et al., Numerical Recipes in C, 2nd ed., Cambridge University Press 1992
   */
	public static String initFromJulian(long lJulianDay, final String sDateTime) 
	{
		int JGREG = 2299161 ; //Julian day of adoption of Gregorian cal.

   	int ja = (int) lJulianDay ;

   	if (lJulianDay >= JGREG)
     /* cross-over to Gregorian Calendar produces this
        correction
     */
   	{
   		int jalpha = (int)(((float)(lJulianDay - 1867216) - 0.25) / 36524.25) ;
      ja += 1 + jalpha - (int)(0.25 * jalpha);
    }
    int jb = ja + 1524 ;
    int jc = (int)(6680.0 + ((float)(jb-2439870) - 122.1) /365.25) ;
    int jd = (int)(365 * jc + (0.25 * jc)) ;
    int je = (int)((jb - jd) / 30.6001) ;
    int day = jb - jd - (int)(30.6001 * je) ;
    int month = je - 1 ;
    if (month > 12)
    	month -= 12 ;
    int year = jc - 4715 ;
    if (month > 2) 
    	--year ;
    if (year <= 0) 
    	--year ;
    
    String sResult = sDateTime ;
    
    sResult = putYY(sResult, year) ;
    sResult = putMM(sResult, month) ;
    sResult = putDD(sResult, day) ;
    
    return sResult ;
	}

	/**
	 * Is the day (in the 1 - 31 interval) valid in the given month  
	 * 
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iDay   Day in the 1 - 31 interval 
	 * 
	 * @return <code>true</code> if the day exists in this month for this year, <code>false</code> if not 
	 * 
	 **/
	protected static boolean isDayWithinMonth(int iMonth, int iDay, int iYear)
	{
		if ((iMonth < 1) || (iMonth > 12) || (iDay <= 0) || (iDay > 31))
			return false ;
		
		int iLeap = 0 ;
		if ((2 == iMonth) && isLeapYear(iYear))   // no need to evaluate leap year if month is not February
			iLeap++ ;
		
		return iDay <= daytab[iLeap][iMonth] ;
	}
	
	/**
	 * Get the count of days (in the 28 - 31 interval) in the given month  
	 * 
	 * @param iMonth Month in the 1 (for January) to 12 (for December) interval
	 * @param iYear  Complete year (for example <code>2017</code>)
	 * 
	 * @return The count of days if month is valid, 0 if not 
	 * 
	 **/
	protected static int daysCountWithinMonth(int iMonth, int iYear) throws IllegalArgumentException
	{
		if ((iMonth < 1) || (iMonth > 12))
			throw new IllegalArgumentException("" + iMonth + " is not a valid month") ;
		
		int iLeap = 0 ;
		if ((2 == iMonth) && isLeapYear(iYear))   // no need to evaluate leap year if month is not February
			iLeap++ ;
		
		return daytab[iLeap][iMonth] ;
	}
	
	/**
	 * Get the count of days (in the 28 - 31 interval) in the given month
	 * 
	 * @return The count of days if month is valid, 0 if not 
	 * 
	 **/
	public int daysUTCCountWithinMonth()
	{
		if (isNoLimit())
			return 0 ;

		int iDayCount = 0 ;
		try {
			iDayCount = daysCountWithinMonth(getMonths(), getYears()) ;
		}
		finally {	
		}
		
		return iDayCount ; 
	}
	
	public static boolean checkProperDateString(String sDate)
	{
		if (null == sDate)
			return false ;
		
		if (false == sDate.matches("\\d+"))
    	return false ;
		
		return true ;
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
	public static String insertIntAtPos(final int iValue, final String sTarget, final int iStart, final int iLength)
	{
		if ((null == sTarget) || "".equals(sTarget))
			return sTarget ;
		
		int iTargetLength = sTarget.length() ;
		
		if (0 == iLength)
			return sTarget ;
		 
		if (iStart + iLength > iTargetLength)
			return sTarget ;
		
		LdvInt iLdvValue = new LdvInt(iValue) ;
		String sReplacer = iLdvValue.intToString(iLength) ;
		
		// Replace at start
		//
		if (0 == iStart)
		{
			// Replace all
			//
			if (iTargetLength == iLength)
				return sReplacer ;
			
			return sReplacer + sTarget.substring(iLength, iTargetLength) ;
		}
		
		// Replace beyond start
		//
		
		// Replace the entire ending part
		//
		if (iStart + iLength == iTargetLength)
			return sTarget.substring(0, iStart) + sReplacer ;
		
		// Mundane case: replace somewhere in the middle
		//
		return sTarget.substring(0, iStart) + sReplacer + sTarget.substring(iStart + iLength, iTargetLength) ;
	}

	protected static String putYY(final String sDate, final int iI) { return insertIntAtPos(iI, sDate,  0, 4) ; }
	protected static String putMM(final String sDate, final int iI) { return insertIntAtPos(iI, sDate,  4, 2) ; }
	protected static String putDD(final String sDate, final int iI) { return insertIntAtPos(iI, sDate,  6, 2) ; }
	protected static String putHr(final String sDate, final int iI) { return insertIntAtPos(iI, sDate,  8, 2) ; }
	protected static String putMn(final String sDate, final int iI) { return insertIntAtPos(iI, sDate, 10, 2) ; }
	protected static String putSe(final String sDate, final int iI) { return insertIntAtPos(iI, sDate, 12, 2) ; }
	protected static String putMs(final String sDate, final int iI) { return insertIntAtPos(iI, sDate, 14, 3) ; }
	
	public  static int getYY(final String sDate) { return getDateElement(sDate, 0, 4) ; }
	public  static int getMM(final String sDate) { return getDateElement(sDate, 4, 6) ; }
	public  static int getDD(final String sDate) { return getDateElement(sDate, 6, 8) ; }
	public  static int getHr(final String sDate) { return getDateElement(sDate, 8, 10) ; }
	public  static int getMn(final String sDate) { return getDateElement(sDate, 10, 12) ; }
	public  static int getSe(final String sDate) { return getDateElement(sDate, 12, 14) ; }
	public  static int getMs(final String sDate) { return getDateElement(sDate, 14, 17) ; }

	private static int getDateElement(final String sDate, final int iBegin, final int iEnd)
	{
		if (sDate.length() < iEnd)
			return -1 ;
		
		return Integer.parseInt(sDate.substring(iBegin, iEnd)) ;
	}
	
	// Functions operating on internal date representation
	//
	protected int getYears()   { return _time.getYears() ; }
	protected int getMonths()  { return _time.getMonths() ; }
	protected int getDays()    { return _time.getDays() ; }
	protected int getHours()   { return _time.getHours() ; }
	protected int getMinutes() { return _time.getMinutes() ; }
	protected int getSeconds() { return _time.getSeconds() ; }
	protected int getMillis()  { return _time.getMillis() ; }
	
	/**
	 * Get the count of seconds that lapsed from the beginning of the day for internal date representation 
	 */
	protected int getSecondsOfDay()
	{
		if (isNoLimit() || isEmpty())
			return -1 ;
		
		return getSecondsOfDay(getHours(), getMinutes(), getSeconds()) ;
	}
}
