package com.ldv.client.util_agenda;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import net.fortuna.ical4j.model.TimeZone;

/**
 * Defines a list of iCalendar dates. If no value type is specified a list defaults to DATE-TIME instances.
 * 
 * Forked from net.fortuna.ical4j.model.WeekDayList because net.fortuna.ical4j.model.WeekDay uses Calendar which is not compilable by GWT 
 */
public class CalendarDateList implements List<CalendarDate>
{
	private final CalendarValue      type ;
	private final List<CalendarDate> dates ;
	private       TimeZone           timeZone ;
	private       boolean            utc ;

	/**
	 * Default constructor.
	 */
	public CalendarDateList() {
		this(false) ;
	}

	public CalendarDateList(final boolean unmodifiable)
	{  
		this.type = CalendarValue.DATE_TIME ;
		if (unmodifiable)
			dates = Collections.emptyList() ;
		else 
			dates = new ArrayList<CalendarDate>() ;  
	}

	/**
	 * @param aType the type of dates contained by the instance
	 */
	public CalendarDateList(final CalendarValue aType) {
		this(aType, null) ;
	}
    
	/**
	 * Default constructor.
	 * 
	 * @param aType
	 *            specifies the type of dates (either date or date-time)
	 * @param timezone the timezone to apply to dates contained by the instance
	 */
	public CalendarDateList(final CalendarValue aType, final TimeZone timezone)
	{
		if (null != aType)
			this.type = aType ;
		else
			this.type = CalendarValue.DATE_TIME ;
        
		this.timeZone = timezone ;
		dates = new ArrayList<CalendarDate>() ;
	}

	/**
	 * @param aValue a string representation of a date list
	 * @param aType the date types contained in the instance
	 * @throws ParseException where the specified string is not a valid date list
	 */
	public CalendarDateList(final String aValue, final CalendarValue aType) throws ParseException {
		this(aValue, aType, null) ;
	}
    
	/**
	 * Parses the specified string representation to create a list of dates.
	 * 
	 * @param aValue
	 *            a string representation of a list of dates
	 * @param aType
	 *            specifies the type of dates (either date or date-time)
	 * @param timezone the timezone to apply to contained dates
	 * @throws ParseException
	 *             if an invalid date representation exists in the date list
	 *             string
	 */
	public CalendarDateList(final String aValue, final CalendarValue aType, final TimeZone timezone) throws ParseException
	{  	
		this(aType, timezone) ;
		final StringTokenizer t = new StringTokenizer(aValue, ",") ;
		while (t.hasMoreTokens())
		{
			if (CalendarValue.DATE.equals(type)) 
				add(new CalendarDate(t.nextToken())) ;
			else 
				add(new CalendarDateTime(t.nextToken(), timezone)) ;
		}
	}
    
	/**
	 * Constructs a new date list of the specified type containing
	 * the dates in the specified list.
	 * @param list a list of dates to include in the new list
	 * @param type the type of the new list
	 */
	public CalendarDateList(final CalendarDateList list, final CalendarValue type)
	{
		if (!CalendarValue.DATE.equals(type) && !CalendarValue.DATE_TIME.equals(type))
			throw new IllegalArgumentException("Type must be either DATE or DATE-TIME") ;
        
		this.type = type ;
		dates = new ArrayList<CalendarDate>() ;
        
		if (CalendarValue.DATE.equals(type))
		{
			for (CalendarDate date : list) 
				add(new CalendarDate(date)) ;
		}
		else 
		{
			for (final CalendarDate dateTime : list) 
				add(new CalendarDateTime(dateTime)) ;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public final String toString()
	{
		final StringBuilder b = new StringBuilder() ;
		
		for (final Iterator<CalendarDate> i = iterator() ; i.hasNext() ; )
		{
			/*
			 * if (type != null && Value.DATE.equals(type)) {
			 * b.append(DateFormat.getInstance().format((Date) i.next())); }
			 * else { b.append(DateTimeFormat.getInstance().format((Date)
			 * i.next(), isUtc())); }
			 */
			b.append(i.next()) ;
			
			if (i.hasNext()) 
				b.append(',') ;
           
		}
		return b.toString() ;
	}

	/**
	 * Add a date to the list. The date will be updated to reflect the timezone of this list.
	 * 
	 * @param date
	 *            the date to add
	 * @return true
	 * @see List#add(java.lang.Object)
	 */
	public final boolean add(final CalendarDate date)
	{
		if (!this.isUtc() && this.getTimeZone() == null)
		{
			/* If list hasn't been initialized yet use defaults from the first added date element */
			if (date instanceof CalendarDateTime)
			{
				CalendarDateTime dateTime = (CalendarDateTime) date ;
				if (dateTime.isUtc()) 
					this.setUtc(true) ;
				else 
					this.setTimeZone(dateTime.getTimeZone()) ;
			}
		}
		if (date instanceof CalendarDateTime)
		{
			CalendarDateTime dateTime = (CalendarDateTime) date ;
			if (isUtc()) 
				dateTime.setUtc(true) ;
			else
				dateTime.setTimeZone(getTimeZone()) ;
		}
		else if (!CalendarValue.DATE.equals(getType()))
		{
			final CalendarDateTime dateTime = new CalendarDateTime(date) ;
			dateTime.setTimeZone(getTimeZone()) ;
			return dates.add(dateTime) ;
		}
		return dates.add(date) ;
	}

	/**
	 * Remove a date from the list.
	 * 
	 * @param date
	 *            the date to remove
	 * @return true if the list contained the specified date
	 * @see List#remove(java.lang.Object)
	 */
	public final boolean remove(final CalendarDate date) {
		return remove((Object) date) ;
	}

	/**
	 * Returns the VALUE parameter specifying the type of dates (ie. date or
	 * date-time) stored in this date list.
	 * 
	 * @return Returns a Value parameter.
	 */
	public final CalendarValue getType() {
		return type ;
	}

	/**
	 * Indicates whether this list is in local or UTC format. This property will
	 * have no affect if the type of the list is not DATE-TIME.
	 * 
	 * @return Returns true if in UTC format, otherwise false.
	 */
	public final boolean isUtc() {
		return utc ;
	}

	/**
	 * Sets whether this list is in UTC or local time format.
	 * 
	 * @param utc
	 *            The utc to set.
	 */
	public final void setUtc(final boolean utc)
	{
		if (!CalendarValue.DATE.equals(type))
			for (CalendarDate date: this)
				((CalendarDateTime) date).setUtc(utc) ;

		this.timeZone = null ;
		this.utc = utc ;
	}
    
	/**
	 * Applies the specified timezone to all dates in the list.
	 * All dates added to this list will also have this timezone
	 * applied.
	 * @param timeZone a timezone to apply to contained dates
	 */
	public final void setTimeZone(final TimeZone timeZone)
	{
		if (!CalendarValue.DATE.equals(type))
			for (CalendarDate date: this)
				((CalendarDateTime) date).setTimeZone(timeZone);

		this.timeZone = timeZone ;
		this.utc = false ;
	}

	/**
	 * @return Returns the timeZone.
	 */
	public final TimeZone getTimeZone() {
		return timeZone;
	}

	public final void add(int arg0, CalendarDate arg1) {
		dates.add(arg0, arg1) ;
	}

	public final boolean addAll(Collection<? extends CalendarDate> arg0) {
		return dates.addAll(arg0) ;
	}

	public final boolean addAll(int arg0, Collection<? extends CalendarDate> arg1) {
		return dates.addAll(arg0, arg1) ;
	}

	public final void clear() {
		dates.clear() ;
	}

	public final boolean contains(Object o) {
		return dates.contains(o) ;
	}

	public final boolean containsAll(Collection<?> arg0) {
		return dates.containsAll(arg0) ;
	}

	public final CalendarDate get(int index) {
		return dates.get(index) ;
	}

	public final int indexOf(Object o) {
		return dates.indexOf(o) ;
	}

	public final boolean isEmpty() {
		return dates.isEmpty() ;
	}

	public final Iterator<CalendarDate> iterator() {
		return dates.iterator() ;
	}

	public final int lastIndexOf(Object o) {
		return dates.lastIndexOf(o) ;
	}

	public final ListIterator<CalendarDate> listIterator() {
		return dates.listIterator() ;
	}

	public final ListIterator<CalendarDate> listIterator(int index) {
		return dates.listIterator(index) ;
	}

	public final CalendarDate remove(int index) {
		return dates.remove(index) ;
	}

	public final boolean remove(Object o) {
		return dates.remove(o) ;
	}

	public final boolean removeAll(Collection<?> arg0) {
		return dates.removeAll(arg0) ;
	}

	public final boolean retainAll(Collection<?> arg0) {
		return dates.retainAll(arg0) ;
	}

	public final CalendarDate set(int arg0, CalendarDate arg1) {
		return dates.set(arg0, arg1) ;
	}

	public final int size() {
		return dates.size() ;
	}

	public final List<CalendarDate> subList(int fromIndex, int toIndex) {
		return dates.subList(fromIndex, toIndex) ;
	}

	public final Object[] toArray() {
		return dates.toArray() ;
	}

	public final <T> T[] toArray(T[] arg0) {
		return dates.toArray(arg0) ;
	}
	
	public final boolean equals(Object obj)
	{
		if (!getClass().isAssignableFrom(obj.getClass()))
			return false ;
	
		final CalendarDateList rhs = (CalendarDateList) obj;
		return new EqualsBuilder().append(dates, rhs.dates)
			.append(type, rhs.type)
			.append(timeZone, rhs.timeZone)
			.append(utc, utc)
			.isEquals();
	}
	
	public final int hashCode() 
	{
		return new HashCodeBuilder().append(dates)
			.append(type)
			.append(timeZone)
			.append(utc)
			.toHashCode();
	}
}