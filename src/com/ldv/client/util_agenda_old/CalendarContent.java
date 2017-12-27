package com.ldv.client.util_agenda;

import java.util.Arrays;
import java.util.List;

/**
 * Base class for calendar content in the form of properties and parameters.
 *
 * Forked from net.fortuna.ical4j.model.Content for GWT compilability reasons
 */
public abstract class CalendarContent
{
	/**
	 * @return the cotent name
	 */
	public abstract String getName() ;

	/**
	 * @return the content value
	 */
	public abstract String getValue() ;

	public static abstract class CalendarFactory
	{
		private final List<String> supportedNames ;

		public CalendarFactory(String... supportedNames) {
			this.supportedNames = Arrays.asList(supportedNames) ;
		}

		public final boolean supports(String name) {
			return supportedNames.contains(name) ;
		}
	}
}
