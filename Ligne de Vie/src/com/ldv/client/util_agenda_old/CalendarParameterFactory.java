package com.ldv.client.util_agenda;

import java.net.URISyntaxException;

/**
 * Implementors provide parameter creation services.
 *
 * Forked from net.fortuna.ical4j.model.ParameterFactory for GWT compilability reasons
 */
public interface CalendarParameterFactory<T extends CalendarParameter>
{
	/**
	 * Returns a parameter instance of the appropriate type with the specified value.
	 *
	 * @param value a value to assign to the returned parameter
	 * @return a parameter instance, or null if this factory is unable to create an
	 * appropriate parameter
	 * @throws URISyntaxException where an invalid URI is encountered
	 */
	T createParameter(String value) throws URISyntaxException ;

	boolean supports(String name) ;
}
