package com.ldv.client.util_agenda;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * A set of keys used to enable compatibility features.
 * 
 * Forked from net.fortuna.ical4j.util.CompatibilityHints because it uses Calendar which is not compilable by GWT
 */
public final class CalendarCompatibilityHints
{
	/**
	 * A system property key to enable relaxed unfolding. Relaxed unfolding is enabled by setting this system property
	 * to "true".
	 */
	public static final String KEY_RELAXED_UNFOLDING = "ical4j.unfolding.relaxed" ;

	/**
	 * A system property key to enable relaxed parsing. Relaxed parsing is enabled by setting this system property to
	 * "true".
	 */
	public static final String KEY_RELAXED_PARSING = "ical4j.parsing.relaxed" ;

	/**
	 * A system property key to enable relaxed validation. Relaxed validation disables validation of certain conformance
	 * rules that many iCalendar implementations do not conform to. Relaxed validation is enabled by setting this system
	 * property to "true".
	 */
	public static final String KEY_RELAXED_VALIDATION = "ical4j.validation.relaxed" ;

	/**
	 * A system property key used to enable compatibility with Outlook/Exchange-generated iCalendar files. Outlook
	 * compatibility is enabled by setting this system property to "true".
	 */
	public static final String KEY_OUTLOOK_COMPATIBILITY = "ical4j.compatibility.outlook" ;

	/**
	 * A system property key used to enable compatibility with Lotus Notes-generated iCalendar files. Notes
	 * compatibility is enabled by setting this system property to "true".
	 */
	public static final String KEY_NOTES_COMPATIBILITY = "ical4j.compatibility.notes" ;
    
	/**
	 * Support for vCard features that are not necessarily compatible with the iCalendar standard.
	 */
	public static final String KEY_VCARD_COMPATIBILITY = "ical4j.compatibility.vcard" ;

	private static final Map<String, Boolean> HINTS = new ConcurrentHashMap<String, Boolean>() ;
	
	// preload known hints from the configurator
	static {
		setHintEnabled(KEY_RELAXED_UNFOLDING,     "true".equals(CalendarConfigurator.getProperty(KEY_RELAXED_UNFOLDING))) ;
		setHintEnabled(KEY_RELAXED_PARSING,       "true".equals(CalendarConfigurator.getProperty(KEY_RELAXED_PARSING))) ;
		setHintEnabled(KEY_RELAXED_VALIDATION,    "true".equals(CalendarConfigurator.getProperty(KEY_RELAXED_VALIDATION))) ;
		setHintEnabled(KEY_OUTLOOK_COMPATIBILITY, "true".equals(CalendarConfigurator.getProperty(KEY_OUTLOOK_COMPATIBILITY))) ;
		setHintEnabled(KEY_NOTES_COMPATIBILITY,   "true".equals(CalendarConfigurator.getProperty(KEY_NOTES_COMPATIBILITY))) ;
	}

	/**
	 * Constructor made private to enforce static nature.
	 */
	private CalendarCompatibilityHints() {
	}

	/**
	 * @param key     a compatibility hint key
	 * @param enabled indicates whether to enable or disable the compatibility hint
	 */
	public static void setHintEnabled(final String key, final boolean enabled) {
		HINTS.put(key, enabled) ;
	}

	/**
	 * @param key a compatibility hint key
	 */
	public static void clearHintEnabled(final String key) {
		HINTS.remove(key) ;
	}

	/**
	 * @param key a compatibility hint key
	 * @return true if the specified compatibility hint is enabled, otherwise false
	 */
	public static boolean isHintEnabled(final String key)
	{
		if (HINTS.get(key) != null) 
			return HINTS.get(key) ;
        
		return "true".equals(CalendarConfigurator.getProperty(key)) ;
	}
}
