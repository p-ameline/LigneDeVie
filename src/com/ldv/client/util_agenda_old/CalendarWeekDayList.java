package com.ldv.client.util_agenda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import net.fortuna.ical4j.util.CompatibilityHints;

/**
 * Defines a list of days.
 * 
 * Forked from net.fortuna.ical4j.model.WeekDayList because net.fortuna.ical4j.model.WeekDay uses Calendar which is not compilable by GWT 
 */
public class CalendarWeekDayList extends ArrayList<CalendarWeekDay> 
{
	/**
   * Default constructor.
   */
	public CalendarWeekDayList() {
	}

	/**
	 * Creates a new instance with the specified initial capacity.
	 * @param initialCapacity the initial capacity of the list
	 */
	public CalendarWeekDayList(final int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Constructor.
	 * @param aString a string representation of a day list
	 */
	public CalendarWeekDayList(final String aString) 
	{
		final boolean outlookCompatibility =
            CompatibilityHints.isHintEnabled(CompatibilityHints.KEY_OUTLOOK_COMPATIBILITY) ;
        
		final StringTokenizer t = new StringTokenizer(aString, ",") ;
		while (t.hasMoreTokens()) 
		{
			if (outlookCompatibility) 
				add(new CalendarWeekDay(t.nextToken().replaceAll(" ", ""))) ;
			else 
				add(new CalendarWeekDay(t.nextToken())) ;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString() 
	{
		if (isEmpty())
			return "" ;
		
		final StringBuilder b = new StringBuilder() ;
		for (final Iterator<CalendarWeekDay> i = iterator() ; i.hasNext() ; ) 
		{
			b.append(i.next()) ;
			if (i.hasNext()) 
				b.append(',') ;
		}
		return b.toString() ;
	}
}
