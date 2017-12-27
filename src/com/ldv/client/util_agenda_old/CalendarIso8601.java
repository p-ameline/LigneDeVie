package com.ldv.client.util_agenda;

import java.text.DateFormat;
import java.util.Date;

import net.fortuna.ical4j.model.CalendarDateFormatFactory;
import net.fortuna.ical4j.model.TimeZone;

/**
 * $Id$
 *
 * Created on 30/06/2005
 *
 * Base class for date and time representations as defined
 * by the ISO 8601 standard. Sub-classes must ensure that either the correct
 * precision is used in constructor arguments, or that <code>Object.equals()</code>
 * is overridden to ensure equality checking is consistent with the type.
 * 
 * Forked from net.fortuna.ical4j.model.Iso8601
 */
public abstract class CalendarIso8601 extends Date
{    
	private DateFormat format ;
  private DateFormat gmtFormat ;
    
	private int precision ;

	/**
	 * @param time a time value in milliseconds
	 * @param pattern the formatting pattern to apply
	 * @param precision the precision to apply
	 * @param tz the timezone for the instance
	 * 
	 * @see Dates#PRECISION_DAY
	 * @see Dates#PRECISION_SECOND
	 */
	public CalendarIso8601(final long time, final String pattern, final int precision, java.util.TimeZone tz) 
	{
		super(CalendarDates.round(time, precision, tz)) ;
		
		format = CalendarDateFormatFactory.getInstance(pattern) ;
		format.setTimeZone(tz) ;
		format.setLenient(CalendarCompatibilityHints.isHintEnabled(CalendarCompatibilityHints.KEY_RELAXED_PARSING)) ;
		
		this.precision = precision ;
	}
    
	/**
	 * @param pattern the formatting pattern to apply
	 * @param precision the precision to apply
	 * @param tz the timezone for the instance
	 * @see Dates#PRECISION_DAY
	 * @see Dates#PRECISION_SECOND
	 */
	public CalendarIso8601(final String pattern, final int precision, java.util.TimeZone tz) {
		this(CalendarDates.getCurrentTimeRounded(), pattern, precision, tz) ;
	}

	/**
	 * @param time a time value as a date
	 * @param pattern the formatting pattern to apply
	 * @param precision the precision to apply
	 * @param tz the timezone for the instance
	 * @see Dates#PRECISION_DAY
	 * @see Dates#PRECISION_SECOND
	 */
	public CalendarIso8601(final Date time, final String pattern, final int precision, java.util.TimeZone tz) {
		this(time.getTime(), pattern, precision, tz) ;
	}
    
	/**
	 * {@inheritDoc}
	 */
	public String toString() 
	{
		// if time is floating avoid daylight saving rules when generating string representation of date..
		//
		final java.util.TimeZone timeZone = format.getTimeZone() ;
		if (!(timeZone instanceof TimeZone)) 
		{
			if (gmtFormat == null)
			{
				gmtFormat = (DateFormat) format.clone() ;
				gmtFormat.setTimeZone(TimeZone.getTimeZone(CalendarTimeZones.GMT_ID)) ;
			}
            
			if (timeZone.inDaylightTime(this) && timeZone.inDaylightTime(new Date(getTime() - 1)))
				return gmtFormat.format(new Date(getTime() + timeZone.getRawOffset() + timeZone.getDSTSavings())) ;

			return gmtFormat.format(new Date(getTime() + timeZone.getRawOffset())) ;
		}
		return format.format(this) ;
	}

	/**
	 * @return Returns the format.
	 */
	protected final DateFormat getFormat() {
		return format ;
	}
    
	/**
	 * {@inheritDoc}
	 */
	public void setTime(final long time) 
	{
		// need to check for null format due to Android java.util.Date(long) constructor calling this method..
		if (null != format)
			super.setTime(CalendarDates.round(time, precision, format.getTimeZone())) ;
		else 
			// XXX: what do we do here??
			super.setTime(time) ;     
	}
}
