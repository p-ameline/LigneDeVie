package com.ldv.client.util_agenda;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Implements a collection of utility methods relevant to date processing.
 * 
 * Forked from net.fortuna.ical4j.model.Time for GWT compilability reasons
 */
public final class CalendarDates 
{
	/**
	 * Number of milliseconds in one second.
	 */
	public static final long MILLIS_PER_SECOND = 1000 ;

	/**
	 * Number of milliseconds in one minute.
	 */
	public static final long MILLIS_PER_MINUTE = 60000 ;

	/**
	 * Number of milliseconds in one hour.
	 */
	public static final long MILLIS_PER_HOUR = 3600000 ;

	/**
	 * Number of milliseconds in one day.
	 */
	public static final long MILLIS_PER_DAY = 86400000 ;

	/**
	 * Number of milliseconds in one week.
	 */
	public static final long MILLIS_PER_WEEK = 604800000 ;
    
	/**
	 * Number of days in one week.
	 */
	public static final int DAYS_PER_WEEK = 7 ;

	/**
	 * Constant indicating precision to the second.
	 */
	public static final int PRECISION_SECOND = 0 ;

	/**
	 * Constant indicating precision to the day.
	 */
	public static final int PRECISION_DAY = 1 ;

	/**
	 * Maximum number of weeks per year.
	 */
	public static final int MAX_WEEKS_PER_YEAR = 53 ;

	/**
	 * Maximum number of days per year.
	 */
	public static final int MAX_DAYS_PER_YEAR = 366 ;

	/**
	 * Maximum number of days per month.
	 */
	public static final int MAX_DAYS_PER_MONTH = 31 ;

	private static final String INVALID_WEEK_MESSAGE      = "Invalid week number [{0}]" ;  
	private static final String INVALID_YEAR_DAY_MESSAGE  = "Invalid year day [{0}]" ; 
	private static final String INVALID_MONTH_DAY_MESSAGE = "Invalid month day [{0}]" ;
    
	/**
	 * Constructor made private to prevent instantiation.
	 */
	private CalendarDates() {
	}

	/**
	 * Returns the absolute week number for the year specified by the
	 * supplied date. Note that a value of zero (0) is invalid for the
	 * weekNo parameter and an <code>IllegalArgumentException</code>
	 * will be thrown.
	 * @param date a date instance representing a week of the year
	 * @param weekNo a week number offset
	 * @return the absolute week of the year for the specified offset
	 */
	public static int getAbsWeekNo(final Date date, final int weekNo)
	{
		if ((0 == weekNo) || (weekNo < -MAX_WEEKS_PER_YEAR) || (weekNo > MAX_WEEKS_PER_YEAR))
			throw new IllegalArgumentException(MessageFormat.format(INVALID_WEEK_MESSAGE, new Object[] {weekNo})) ;
        
		if (weekNo > 0) 
			return weekNo ;
        
		final Calendar cal = Calendar.getInstance() ;
		cal.setTime(date) ;
		final int year = cal.get(Calendar.YEAR) ;
		// construct a list of possible week numbers..
		final List<Integer> weeks = new ArrayList<Integer>() ;
		cal.set(Calendar.WEEK_OF_YEAR, 1) ;
		while (cal.get(Calendar.YEAR) == year)
		{
			weeks.add(cal.get(Calendar.WEEK_OF_YEAR)) ;
			cal.add(Calendar.WEEK_OF_YEAR, 1) ;
		}
		return weeks.get(weeks.size() + weekNo) ;
	}

	/**
	 * Returns the absolute year day for the year specified by the supplied date.
	 * Note that a value of zero (0) is invalid for the yearDay parameter and an <code>IllegalArgumentException</code>
	 * will be thrown.
	 * @param date a date instance representing a day of the year
	 * @param yearDay a day of year offset
	 * @return the absolute day of month for the specified offset
	 */
	public static int getAbsYearDay(final Date date, final int yearDay)
	{
		if ((0 == yearDay) || (yearDay < -MAX_DAYS_PER_YEAR) || (yearDay > MAX_DAYS_PER_YEAR)) 
			throw new IllegalArgumentException(MessageFormat.format(INVALID_YEAR_DAY_MESSAGE, new Object[] {yearDay})) ;
        
		if (yearDay > 0) 
			return yearDay ;
        
		final Calendar cal = Calendar.getInstance() ;
		cal.setTime(date) ;
		final int year = cal.get(Calendar.YEAR) ;
		// construct a list of possible year days..
		final List<Integer> days = new ArrayList<Integer>() ;
		cal.set(Calendar.DAY_OF_YEAR, 1) ;
		while (cal.get(Calendar.YEAR) == year)
		{
			days.add(cal.get(Calendar.DAY_OF_YEAR)) ;
			cal.add(Calendar.DAY_OF_YEAR, 1) ;
		}
		return days.get(days.size() + yearDay) ;
	}

	/**
	 * Returns the absolute month day for the month specified by the
	 * supplied date. Note that a value of zero (0) is invalid for the
	 * monthDay parameter and an <code>IllegalArgumentException</code>
	 * will be thrown.
	 * @param date a date instance representing a day of the month
	 * @param monthDay a day of month offset
	 * @return the absolute day of month for the specified offset
	 */
	public static int getAbsMonthDay(Date date, final int monthDay)
	{
		if ((0 == monthDay) || (monthDay < -MAX_DAYS_PER_MONTH) || (monthDay > MAX_DAYS_PER_MONTH))
			throw new IllegalArgumentException(MessageFormat.format(INVALID_MONTH_DAY_MESSAGE, new Object[] {monthDay})) ;
        
		if (monthDay > 0) 
			return monthDay ;
        
		final Calendar cal = Calendar.getInstance() ;
		cal.setTime(date) ;
		final int month = cal.get(Calendar.MONTH) ;
		// construct a list of possible month days..
		final List<Integer> days = new ArrayList<Integer>() ;
		cal.set(Calendar.DAY_OF_MONTH, 1) ;
		while (cal.get(Calendar.MONTH) == month) 
		{
			days.add(cal.get(Calendar.DAY_OF_MONTH)) ;
			cal.add(Calendar.DAY_OF_MONTH, 1) ;
		}
		return days.get(days.size() + monthDay) ;
	}
    
	/**
	 * Returns a new date instance of the specified type. If no type is
	 * specified a DateTime instance is returned.
	 * @param date a seed Java date instance
	 * @param type the type of date instance
	 * @return an instance of <code>net.fortuna.ical4j.model.Date</code>
	 */
	public static CalendarDate getInstance(final Date date, final CalendarValue type) 
	{
		if (CalendarValue.DATE.equals(type)) 
			return new CalendarDate(date) ;
        
		return new CalendarDateTime(date) ;
	}
    
	/**
	 * Returns an instance of <code>java.util.Calendar</code> that is suitably
	 * initialised for working with the specified date.
	 * @param date a date instance
	 * @return a <code>java.util.Calendar</code>
	 */
	public static Calendar getCalendarInstance(final CalendarDate date)
	{
		Calendar instance ;
		if (date instanceof CalendarDateTime)
		{
			final CalendarDateTime dateTime = (CalendarDateTime) date ;
			if (dateTime.getTimeZone() != null) 
				instance = Calendar.getInstance(dateTime.getTimeZone()) ;
			else if (dateTime.isUtc())
				instance = Calendar.getInstance(TimeZones.getUtcTimeZone()) ;
			else 
				// a date-time without a timezone but not UTC is floating
				instance = Calendar.getInstance() ;
		}
		else 
			instance = Calendar.getInstance(TimeZones.getDateTimeZone()) ;
		return instance ;
	}
    
	/**
	 * @param time the time value to round
	 * @param precision the rounding precision
	 * @return a round time value
	 * @deprecated It is not all that useful to perform rounding without specifying an
	 * explicit timezone.
	 */
	public static long round(final long time, final int precision) 
	{
		return round(time, precision, TimeZone.getDefault()) ;
//        return round(time, precision, TimeZone.getTimeZone(TimeZones.UTC_ID));
        /*
        long newTime = time;
        if (precision == PRECISION_DAY) {
            long remainder = newTime%(1000*60*60); // get the mod remainder using milliseconds*seconds*min
            newTime = newTime-remainder;
              // remove the remainder from the time to clear the milliseconds, seconds and minutes
        }
        else if (precision == PRECISION_SECOND) {
            long remainder = newTime%(1000); // get the mod remainder using milliseconds
            newTime = newTime-remainder;  // remove the remainder from the time to clear the milliseconds
        }
        return newTime;
	*/
	}
    
	/**
	 * Rounds a time value to remove any precision smaller than specified.
	 * @param time the time value to round
	 * @param precision the rounding precision
	 * @param tz the timezone of the rounded value
	 * @return a round time value
	 */
	public static long round(final long time, final int precision, final TimeZone tz)
	{
		if ((precision == PRECISION_SECOND) && ((time % CalendarDates.MILLIS_PER_SECOND) == 0)) 
			return time ;
        
        final Calendar cal = Calendar.getInstance(tz);
        cal.setTimeInMillis(time);
        if (precision == PRECISION_DAY) {
//            return (long) Math.floor(time / (double) Dates.MILLIS_PER_DAY) * Dates.MILLIS_PER_DAY;
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.clear(Calendar.MINUTE);
            cal.clear(Calendar.SECOND);
            cal.clear(Calendar.MILLISECOND);
        }
        else if (precision == PRECISION_SECOND) {
//            return (long) Math.floor(time / (double) Dates.MILLIS_PER_SECOND) * Dates.MILLIS_PER_SECOND;
            cal.clear(Calendar.MILLISECOND);
        }
        // unrecognised precision..
        return cal.getTimeInMillis();
    }

	/**
	 * Returns the {@code System.currentTimeMillis()}, rounded to the second.
	 * <p>By doing a rough rounding here, we avoid an expensive java.util.Calendar based
	 *  rounding later on.</p>
	 * @return the current time in millisec.
	 */
	public static long getCurrentTimeRounded() {
		return (long) Math.floor(System.currentTimeMillis() / (double) CalendarDates.MILLIS_PER_SECOND) * CalendarDates.MILLIS_PER_SECOND ;
	}
}
