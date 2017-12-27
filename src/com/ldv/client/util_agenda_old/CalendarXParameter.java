package com.ldv.client.util_agenda;

/**
 * Defines an extension parameter.
 * 
 * Forked from net.fortuna.ical4j.model.parameter.XParameter for GWT compilability reasons
 */
public class CalendarXParameter extends CalendarParameter
{
	private String value ;

	/**
	 * @param aName parameter name
	 * @param aValue parameter value
	 */
	public CalendarXParameter(final String aName, final String aValue) 
	{
		super(aName, CalendarParameterFactoryImpl.getInstance()) ;
		this.value = CalendarStrings.unquote(aValue) ;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getValue() {
		return value;
	}
}
