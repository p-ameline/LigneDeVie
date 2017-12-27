package com.ldv.client.util_agenda;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * A type used to represent iCalendar time values.
 * 
 * Forked from net.fortuna.ical4j.model.Time for GWT compilability reasons
 */
public class CalendarTime extends CalendarIso8601 
{
	private boolean utc = false ;
    
	/**
	 * FORM #1: LOCAL TIME.
	 */
	private static final String DEFAULT_PATTERN = "HHmmss" ;
    
	/**
	 * FORM #2: UTC TIME.
	 */
	private static final String UTC_PATTERN = "HHmmss'Z'" ;

	/**
	 * @param timezone a timezone for the instance
	 */
	public CalendarTime(final TimeZone timezone) {
		this(timezone, CalendarTimeZones.isUtc(timezone)) ;
	}
    
	/**
	 * @param timezone a timezone for the instance
	 * @param utc indicates if the time is in UTC
	 */
	public CalendarTime(final TimeZone timezone, boolean utc)
	{
		super(utc ? UTC_PATTERN : DEFAULT_PATTERN, CalendarDates.PRECISION_SECOND, timezone) ;
		getFormat().setTimeZone(timezone) ;
		this.utc = utc ;
	}

	/**
	 * @param time a time value in milliseconds from the epoch
	 * @param timezone a timezone for the instance
	 */
	public CalendarTime(final long time, final TimeZone timezone) {
		this(time, timezone, CalendarTimeZones.isUtc(timezone)) ;
	}
    
	/**
	 * @param time a time value in milliseconds from the epoch
	 * @param timezone a timezone for the instance
	 * @param utc indicates if the time is in UTC
	 */
	public CalendarTime(final long time, final TimeZone timezone, boolean utc)
	{
		super(time, (utc ? UTC_PATTERN : DEFAULT_PATTERN), CalendarDates.PRECISION_SECOND, timezone) ;
		getFormat().setTimeZone(timezone) ;
		this.utc = utc ;
	}

	/**
	 * @param time a time value in milliseconds from the epoch
	 * @param timezone a timezone for the instance
	 */
	public CalendarTime(final java.util.Date time, final TimeZone timezone) {
		this(time, timezone, CalendarTimeZones.isUtc(timezone)) ;
	}
    
	/**
	 * @param time a time value as a Java date instance
	 * @param timezone a timezone for the instance
	 * @param utc indicates if the time is in UTC
	 */
	public CalendarTime(final java.util.Date time, final TimeZone timezone, boolean utc)
	{
		super(time.getTime(), (utc ? UTC_PATTERN : DEFAULT_PATTERN), CalendarDates.PRECISION_SECOND, timezone) ;
		getFormat().setTimeZone(timezone) ;
		this.utc = utc ;
	}
    
	/**
	 * @param value
	 * @param timezone
	 * @throws ParseException where the specified value is not a valid time string
	 */
	public CalendarTime(String value, TimeZone timezone) throws ParseException {
		this(value, timezone, CalendarTimeZones.isUtc(timezone)) ;
	}
    
	/**
	 * @param value
	 * @param timezone
	 * @param utc
	 * @throws ParseException where the specified value is not a valid time string
	 */
	public CalendarTime(String value, TimeZone timezone, boolean utc) throws ParseException {
		this(parseDate(value, timezone), timezone, utc) ;
	}
    
	private static java.util.Date parseDate(String value, TimeZone timezone) throws ParseException
	{
		DateFormat df = new SimpleDateFormat(DEFAULT_PATTERN) ;
		df.setTimeZone(timezone) ;
        
		try {
			return df.parse(value) ;
		}
		catch (ParseException e) {
			df = new SimpleDateFormat(UTC_PATTERN) ;
			df.setTimeZone(timezone) ;
		}
		return df.parse(value) ;
	}
    
	/**
	 * @return true if time is utc
	 */
	public final boolean isUtc() {
		return utc ;
	}
}
