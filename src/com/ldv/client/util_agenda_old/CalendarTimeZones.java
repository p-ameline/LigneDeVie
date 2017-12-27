package com.ldv.client.util_agenda;

import java.util.TimeZone;

/**
 * Utility methods relevant to Java timezones.
 *
 * Forked from net.fortuna.ical4j.model.Time for GWT compilability reasons
 */
public final class CalendarTimeZones
{
	/**
	 * The timezone identifier for UTC time.
	 */
	public static final String UTC_ID = "Etc/UTC" ;
    
	/**
	 * The timezone identifier for UTC time in the IBM JVM.
	 */
	public static final String IBM_UTC_ID = "GMT" ;
    
	/**
	 * The timezone identifier for GMT time.
	 */
	public static final String GMT_ID = "Etc/GMT" ;

	private static final TimeZone UTC_TIMEZONE ;
	static {
		UTC_TIMEZONE = TimeZone.getTimeZone(UTC_ID);
	}

	/**
	 * Constructor made private to enforce static nature.
	 */
	private CalendarTimeZones() {
	}
    
	/**
	 * Indicates whether the specified timezone is equivalent to UTC time.
	 * @param timezone a timezone instance
	 * @return true if the timezone is UTC time, otherwise false
	 */
	public static boolean isUtc(final TimeZone timezone) 
	{
//        return timezone.hasSameRules(TimeZone.getTimeZone(UTC_ID));
//        return timezone.getRawOffset() == 0;
		return UTC_ID.equals(timezone.getID()) || IBM_UTC_ID.equals(timezone.getID()) ;
	}
    
	/**
	 * Although timezones are not really applicable to DATE instances in iCalendar, the implementation
	 * in iCal4j requires the use of a timezone. Dates in iCal4j may be either "floating", in that they
	 * use the default Java timezone, or alternatively will use UTC (this is the default).
	 * 
	 * The use of floating dates may be configured by specifying the following as a system property or in
	 * a file called "ical4j.properties" in the classpath:
	 * 
	 * <pre>net.fortuna.ical4j.timezone.date.floating=true</pre>
	 * 
	 * @return the timezone used for date instances
	 */
	public static TimeZone getDateTimeZone() 
	{
		if ("true".equals(CalendarConfigurator.getProperty("net.fortuna.ical4j.timezone.date.floating"))) 
			return TimeZone.getDefault() ;
		return getUtcTimeZone() ;
	}

	/**
	 * Get the UTC Timezone.
	 */
	public static TimeZone getUtcTimeZone() {
		return UTC_TIMEZONE ;
	}
}
