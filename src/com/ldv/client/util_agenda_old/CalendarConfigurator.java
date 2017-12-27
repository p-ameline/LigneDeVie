package com.ldv.client.util_agenda;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * $Id$
 * <p/>
 * Created on 06/02/2008
 * <p/>
 * Provides configuration properties specified either as system properties
 * or in an ical4j.properties configuration file.
 *
 * @author Ben
 */
public final class CalendarConfigurator 
{
	private static final Logger     LOG    = LoggerFactory.getLogger(CalendarConfigurator.class) ;
	private static final Properties CONFIG = new Properties() ;

/* Commented because it doesn't make sense in the client context
	static {
		try {
			CONFIG.load(ResourceLoader.getResourceAsStream("ical4j.properties"));
		} catch (Exception e) {
			LOG.info("ical4j.properties not found.");
		}
	}
*/

	/**
	 * Constructor made private to enforce static nature.
	 */
	private CalendarConfigurator() {
	}

	/**
	 * @param key a compatibility hint key
	 * @return true if the specified compatibility hint is enabled, otherwise false
	 */
	public static String getProperty(final String key)
	{
		String property = CONFIG.getProperty(key) ;
		if (null == property) 
			property = System.getProperty(key) ;
		return property ;
	}
}
