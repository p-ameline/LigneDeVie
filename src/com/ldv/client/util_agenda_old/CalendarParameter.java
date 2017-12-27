package com.ldv.client.util_agenda;

import java.net.URISyntaxException;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.fortuna.ical4j.model.Content;
import net.fortuna.ical4j.util.Strings;


/**
 * Defines an iCalendar parameter. Subclasses of this class provide additional validation and typed values for specific
 * iCalendar parameters.
 * <p/>
 * Note that subclasses must provide a reference to the factory used to create the
 * parameter to support parameter cloning (copy). If no factory is specified an
 * {@link UnsupportedOperationException} will be thrown by the {@link #copy()} method.
 *
 * Forked from net.fortuna.ical4j.model.Parameter for GWT compilability reasons
 *
 */
public abstract class CalendarParameter extends Content
{
    /**
     * Region abbreviation.
     */
    public static final String ABBREV = "ABBREV";

    /**
     * Alternate text representation.
     */
    public static final String ALTREP = "ALTREP";

    /**
     * Common name.
     */
    public static final String CN = "CN";

    /**
     * Calendar user type.
     */
    public static final String CUTYPE = "CUTYPE";

    /**
     * Delegator.
     */
    public static final String DELEGATED_FROM = "DELEGATED-FROM";

    /**
     * Delegatee.
     */
    public static final String DELEGATED_TO = "DELEGATED-TO";

    /**
     * Directory entry.
     */
    public static final String DIR = "DIR";

    /**
     * Inline encoding.
     */
    public static final String ENCODING = "ENCODING";

    /**
     * Format type.
     */
    public static final String FMTTYPE = "FMTTYPE";

    /**
     * Free/busy time type.
     */
    public static final String FBTYPE = "FBTYPE";

    /**
     * Language for text.
     */
    public static final String LANGUAGE = "LANGUAGE";

    /**
     * Group or list membership.
     */
    public static final String MEMBER = "MEMBER";

    /**
     * Participation status.
     */
    public static final String PARTSTAT = "PARTSTAT";

    /**
     * Recurrence identifier range.
     */
    public static final String RANGE = "RANGE";

    /**
     * Alarm trigger relationship.
     */
    public static final String RELATED = "RELATED";

    /**
     * Relationship type.
     */
    public static final String RELTYPE = "RELTYPE";

    /**
     * Participation role.
     */
    public static final String ROLE = "ROLE";

    /**
     * RSVP expectation.
     */
    public static final String RSVP = "RSVP";

    /**
     * Schedule agent.
     */
    public static final String SCHEDULE_AGENT = "SCHEDULE-AGENT";

    /**
     * Schedule status.
     */
    public static final String SCHEDULE_STATUS = "SCHEDULE-STATUS";

    /**
     * Sent by.
     */
    public static final String SENT_BY = "SENT-BY";

    /**
     * Type.
     */
    public static final String TYPE = "TYPE";

    /**
     * Reference to time zone object.
     */
    public static final String TZID = "TZID";

    /**
     * Property value data type.
     */
    public static final String VALUE = "VALUE";

    /**
     * Reference to vvenue component.
     */
    public static final String VVENUE = "VVENUE";

	/**
	 * Prefix to all experimental parameters.
	 */
	public static final String EXPERIMENTAL_PREFIX = "X-" ;

	private String name ;

	private final CalendarParameterFactoryImpl factory ;

	/**
	 * @param aName   the parameter identifier
	 * @param factory the factory used to create the parameter
	 */
	public CalendarParameter(final String aName, CalendarParameterFactoryImpl factory)
	{
		this.name    = aName ;
		this.factory = factory ;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString()
	{
		final StringBuilder b = new StringBuilder() ;
		b.append(getName()) ;
		b.append('=') ;
		if (isQuotable())
			b.append(Strings.quote(Strings.valueOf(getValue()))) ;
		else
			b.append(Strings.valueOf(getValue())) ;
		return b.toString() ;
	}

	/**
	 * Indicates whether the current parameter value should be quoted.
	 *
	 * @return true if the value should be quoted, otherwise false
	 */
	protected boolean isQuotable() {
		return Strings.PARAM_QUOTE_PATTERN.matcher(Strings.valueOf(getValue())).find() ;
	}

	/**
	 * @return Returns the name.
	 */
	public final String getName() {
		return name ;
	}

	/**
	 * {@inheritDoc}
	 */
	public final boolean equals(final Object arg0)
	{
		if (arg0 instanceof CalendarParameter)
		{
			final CalendarParameter p = (CalendarParameter) arg0 ;
			return new EqualsBuilder().append(getName(), p.getName()).append(getValue(), p.getValue()).isEquals() ;
		}
		return super.equals(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public final int hashCode() {
		// as parameter name is case-insensitive generate hash for uppercase..
		return new HashCodeBuilder().append(getName().toUpperCase()).append(getValue()).toHashCode() ;
	}

	/**
	 * Deep copy of parameter.
	 *
	 * @return new parameter
	 * @throws URISyntaxException where an invalid URI is encountered
	 */
	public CalendarParameter copy() throws URISyntaxException 
	{
		if (null == factory)
			throw new UnsupportedOperationException("No factory specified") ;
        
		return factory.createParameter(getName(), getValue()) ;
	}
}
