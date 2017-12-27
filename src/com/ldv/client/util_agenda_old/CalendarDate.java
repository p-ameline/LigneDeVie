package com.ldv.client.util_agenda;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import net.fortuna.ical4j.util.CompatibilityHints;
import net.fortuna.ical4j.util.Dates;
import net.fortuna.ical4j.util.TimeZones;

/**
 * A representation of the DATE object defined in RFC5445.
 * <p/>
 * <strong>NOTE:</strong> iCal4j calculates the timezone of Date objects by default. In the iCalendar specification a
 * DATE object doesn't really have a timezone associated with it, but in the ical4j implementation it does (as it is
 * based on a java.util.Date object). An unfortunate side effect of this is that a Date object can be affected by
 * daylight savings rules for the associated timezone. So currently ical4j offers two approaches to setting the
 * timezone:
 * <p/>
 * <ol>
 *     <li>Use UTC time (no DST rules, but depending on how the Date is constructed it may change the displayed value</li>
 *     <li>Use local timezone (in rare cases the DST rules may affect the displayed value).</li>
 * </ol>
 * <p/>
 * By default option 1 is used, but you can override this with the following flag in the ical4j.properties file:
 * <code>net.fortuna.ical4j.timezone.date.floating=true</code>
 * <p/>
 * Alternatively you can avoid using the constructor that accepts a java.util.Date object, and instead provide a date
 * string value. This should always display the correct value, however when the local timezone is used any calculations
 * (recurrences, etc.) may still be affect by DST rules.
 * <p/>
 * <em>Extract from RFC5545:</em>
 * <p/>
 * <pre>
 * 3.3.4.  Date
 * 
 * Value Name:  DATE
 * 
 * Purpose:  This value type is used to identify values that contain a
 * calendar date.
 * 
 * Format Definition:  This value type is defined by the following
 * notation:
 * 
 * date               = date-value
 * 
 * date-value         = date-fullyear date-month date-mday
 * date-fullyear      = 4DIGIT
 * date-month         = 2DIGIT        ;01-12
 * date-mday          = 2DIGIT        ;01-28, 01-29, 01-30, 01-31
 * ;based on month/year
 * 
 * Description:  If the property permits, multiple "date" values are
 * specified as a COMMA-separated list of values.  The format for the
 * value type is based on the [ISO.8601.2004] complete
 * representation, basic format for a calendar date.  The textual
 * format specifies a four-digit year, two-digit month, and two-digit
 * day of the month.  There are no separator characters between the
 * year, month, and day component text.
 * 
 * No additional content value encoding (i.e., BACKSLASH character
 * encoding, see Section 3.3.11) is defined for this value type.
 * 
 * Example:  The following represents July 14, 1997:
 * 
 * 19970714
 * 
 * </pre>
 * 
 * Forked from net.fortuna.ical4j.model.Date because it uses Calendar which is not compilable by GWT
 */
public class CalendarDate extends CalendarIso8601 
{
	private static final String DEFAULT_PATTERN = "yyyyMMdd" ;
	private static final String VCARD_PATTERN   = "yyyy'-'MM'-'dd" ;

	/**
	 * Default constructor.
	 */
	public CalendarDate() {
		super(DEFAULT_PATTERN, Dates.PRECISION_DAY, TimeZones.getDateTimeZone()) ;
	}
    
	/**
	 * Creates a new date instance with the specified precision. This
	 * constructor is only intended for use by sub-classes.
	 * @param precision the date precision
	 * @param tz the timezone
	 * @see Dates#PRECISION_DAY
	 * @see Dates#PRECISION_SECOND
	 */
	protected CalendarDate(final int precision, TimeZone tz) {
		super(DEFAULT_PATTERN, precision, tz) ;
	}

	/**
	 * @param time a date value in milliseconds
	 */
	public CalendarDate(final long time) {
		super(time, DEFAULT_PATTERN, Dates.PRECISION_DAY, TimeZones.getDateTimeZone()) ;
	}
    
	/**
	 * Creates a new date instance with the specified precision. This
	 * constructor is only intended for use by sub-classes.
	 * @param time a date value in milliseconds
	 * @param precision the date precision
	 * @param tz the timezone
	 * @see Dates#PRECISION_DAY
	 * @see Dates#PRECISION_SECOND
	 */
	protected CalendarDate(final long time, final int precision, TimeZone tz) {
		super(time, DEFAULT_PATTERN, precision, tz) ;
	}
    
	/**
	 * @param date a date value
	 */
	public CalendarDate(final java.util.Date date) {
		this(date.getTime(), Dates.PRECISION_DAY, TimeZones.getDateTimeZone()) ;
	}

	/**
	 * @param value a string representation of a date
	 * @throws ParseException where the specified string is not a valid date
	 */
	public CalendarDate(final String value) throws ParseException
	{
		this() ;
        
		try {
			setTime(getFormat().parse(value).getTime()) ;
		} 
		catch (ParseException pe) 
		{
			if (CompatibilityHints.isHintEnabled(CompatibilityHints.KEY_VCARD_COMPATIBILITY)) 
			{
				final DateFormat parseFormat = new SimpleDateFormat(VCARD_PATTERN);
				parseFormat.setTimeZone(TimeZones.getDateTimeZone());
				setTime(parseFormat.parse(value).getTime());
			}
			else
				throw pe ;
		}
	}  
    
	/**
	 * @param value a string representation of a date
	 * @param pattern a date pattern to apply when parsing
	 * @throws ParseException where the specified string is not a valid date
	 */
	public CalendarDate(String value, String pattern) throws ParseException 
	{
		super(DEFAULT_PATTERN, Dates.PRECISION_DAY, TimeZones.getDateTimeZone()) ;
		final DateFormat parseFormat = new SimpleDateFormat(pattern) ;
		parseFormat.setTimeZone(TimeZones.getDateTimeZone()) ;
		setTime(parseFormat.parse(value).getTime()) ;
	}
}
