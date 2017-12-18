package com.ldv.client.util_agenda;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import net.fortuna.ical4j.util.Numbers;

/**
 * Defines a list of numbers.
 * 
 * Forked from net.fortuna.ical4j.model.WeekDayList because net.fortuna.ical4j.model.WeekDay uses Calendar which is not compilable by GWT 
 */
public class CalendarNumberList extends ArrayList<Integer>
{
	private final int     minValue ;
	private final int     maxValue ;
	private final boolean allowsNegativeValues ;
    
	/**
	 * Default constructor.
	 */
	public CalendarNumberList() {
		this(Integer.MIN_VALUE, Integer.MAX_VALUE, true) ;
	}

	/**
	 * Constructor with limits.
	 * @param minValue the minimum allowable value
	 * @param maxValue the maximum allowable value
	 * @param allowsNegativeValues indicates whether negative values are allowed
	 */
	public CalendarNumberList(int minValue, int maxValue, boolean allowsNegativeValues)
	{
		this.minValue = minValue ;
		this.maxValue = maxValue ;
		this.allowsNegativeValues = allowsNegativeValues ;
	}

	/**
	 * Constructor.
	 * @param aString a string representation of a number list
	 */
	public CalendarNumberList(final String aString) {
		this(aString, Integer.MIN_VALUE, Integer.MAX_VALUE, true) ;
	}
    
	/**
	 * @param aString a string representation of a number list
	 * @param minValue the minimum allowable value
	 * @param maxValue the maximum allowable value
	 * @param allowsNegativeValues indicates whether negative values are allowed
	 */
	public CalendarNumberList(final String aString, int minValue, int maxValue, boolean allowsNegativeValues) 
	{
		this(minValue, maxValue, allowsNegativeValues) ;

		final StringTokenizer t = new StringTokenizer(aString, ",") ;
		while (t.hasMoreTokens()) 
		{
			final int value = Numbers.parseInt(t.nextToken()) ;
			add(value) ;
		}
	}

	/**
	 * @param aNumber a number to add to the list
	 * @return true if the number was added, otherwise false
	 */
	public final boolean add(final Integer aNumber)
	{
		int abs = aNumber ;
		if ((abs >> 31 | -abs >>> 31) < 0)
		{
			if (!allowsNegativeValues)
				throw new IllegalArgumentException("Negative value not allowed: " + aNumber) ;

			abs = Math.abs(abs) ;
		}
		if (abs < minValue || abs > maxValue) 
			throw new IllegalArgumentException("Value not in range [" + minValue + ".." + maxValue + "]: " + aNumber) ;
    	
		return super.add(aNumber) ;
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString()
	{
		final StringBuilder b = new StringBuilder() ;
		for (final Iterator<Integer> i = iterator(); i.hasNext();)
		{
			b.append(i.next()) ;
			if (i.hasNext()) 
				b.append(',') ;
		}
		return b.toString() ;
	}
}
