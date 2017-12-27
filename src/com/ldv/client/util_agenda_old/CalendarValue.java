package com.ldv.client.util_agenda;

import java.net.URISyntaxException;

import net.fortuna.ical4j.util.Strings;

/**
 * Defines a Value Data Type parameter.
 * 
 * Forked from net.fortuna.ical4j.model.Time for GWT compilability reasons
 */
public class CalendarValue extends CalendarParameter
{
	private static final String VALUE_BINARY      = "BINARY";
	private static final String VALUE_BOOLEAN     = "BOOLEAN";
	private static final String VALUE_CAL_ADDRESS = "CAL-ADDRESS";
	private static final String VALUE_DATE        = "DATE";
	private static final String VALUE_DATE_TIME   = "DATE-TIME";
	private static final String VALUE_DURATION    = "DURATION";
	private static final String VALUE_FLOAT       = "FLOAT";
	private static final String VALUE_INTEGER     = "INTEGER";
	private static final String VALUE_PERIOD      = "PERIOD";
	private static final String VALUE_RECUR       = "RECUR";
	private static final String VALUE_TEXT        = "TEXT";
	private static final String VALUE_TIME        = "TIME";
	private static final String VALUE_URI         = "URI";
	private static final String VALUE_UTC_OFFSET  = "UTC-OFFSET";

    /**
     * Binary value type.
     */
    public static final CalendarValue BINARY = new CalendarValue(VALUE_BINARY);

    /**
     * Boolean value type.
     */
    public static final CalendarValue BOOLEAN = new CalendarValue(VALUE_BOOLEAN);

    /**
     * Calendar address value type.
     */
    public static final CalendarValue CAL_ADDRESS = new CalendarValue(VALUE_CAL_ADDRESS);

    /**
     * Date value type.
     */
    public static final CalendarValue DATE = new CalendarValue(VALUE_DATE);

    /**
     * Date-time value type.
     */
    public static final CalendarValue DATE_TIME = new CalendarValue(VALUE_DATE_TIME);

    /**
     * Duration value type.
     */
    public static final CalendarValue DURATION = new CalendarValue(VALUE_DURATION);

    /**
     * Float value type.
     */
    public static final CalendarValue FLOAT = new CalendarValue(VALUE_FLOAT);

    /**
     * Integer value type.
     */
    public static final CalendarValue INTEGER = new CalendarValue(VALUE_INTEGER);

    /**
     * Period value type.
     */
    public static final CalendarValue PERIOD = new CalendarValue(VALUE_PERIOD);

    /**
     * Recurrence value type.
     */
    public static final CalendarValue RECUR = new CalendarValue(VALUE_RECUR);

    /**
     * Text value type.
     */
    public static final CalendarValue TEXT = new CalendarValue(VALUE_TEXT);

    /**
     * Time value type.
     */
    public static final CalendarValue TIME = new CalendarValue(VALUE_TIME);

    /**
     * URI value type.
     */
    public static final CalendarValue URI = new CalendarValue(VALUE_URI);

    /**
     * UTC offset value type.
     */
    public static final CalendarValue UTC_OFFSET = new CalendarValue(VALUE_UTC_OFFSET);

	private String value ;

	/**
	 * @param aValue a string representation of a value data type
	 */
	public CalendarValue(final String aValue)
	{
		super(VALUE, CalendarParameterFactoryImpl.getInstance()) ;
		this.value = Strings.unquote(aValue) ;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String getValue() {
		return value ;
	}

	public static class CalendarFactory extends CalendarContent.CalendarFactory implements CalendarParameterFactory
	{
		public CalendarFactory() {
			super(VALUE) ;
		}

		public CalendarParameter createParameter(final String value) throws URISyntaxException
		{
			CalendarValue parameter = new CalendarValue(value) ;
			
			if (CalendarValue.BINARY.equals(parameter)) {
                parameter = CalendarValue.BINARY;
            } else if (CalendarValue.BOOLEAN.equals(parameter)) {
                parameter = CalendarValue.BOOLEAN;
            } else if (CalendarValue.CAL_ADDRESS.equals(parameter)) {
                parameter = CalendarValue.CAL_ADDRESS;
            } else if (CalendarValue.DATE.equals(parameter)) {
                parameter = CalendarValue.DATE;
            } else if (CalendarValue.DATE_TIME.equals(parameter)) {
                parameter = CalendarValue.DATE_TIME;
            } else if (CalendarValue.DURATION.equals(parameter)) {
                parameter = CalendarValue.DURATION;
            } else if (CalendarValue.FLOAT.equals(parameter)) {
                parameter = CalendarValue.FLOAT;
            } else if (CalendarValue.INTEGER.equals(parameter)) {
                parameter = CalendarValue.INTEGER;
            } else if (CalendarValue.PERIOD.equals(parameter)) {
                parameter = CalendarValue.PERIOD;
            } else if (CalendarValue.RECUR.equals(parameter)) {
                parameter = CalendarValue.RECUR;
            } else if (CalendarValue.TEXT.equals(parameter)) {
                parameter = CalendarValue.TEXT;
            } else if (CalendarValue.TIME.equals(parameter)) {
                parameter = CalendarValue.TIME;
            } else if (CalendarValue.URI.equals(parameter)) {
                parameter = CalendarValue.URI;
            } else if (CalendarValue.UTC_OFFSET.equals(parameter)) {
                parameter = CalendarValue.UTC_OFFSET;
            }
            return parameter;
		}
	}
}
