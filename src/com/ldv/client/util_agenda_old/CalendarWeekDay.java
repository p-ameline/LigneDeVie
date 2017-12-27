package com.ldv.client.util_agenda;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.ldv.shared.model.LdvTime;

import net.fortuna.ical4j.util.Numbers;

/**
 * Defines a day of the week
 *
 * Forked from net.fortuna.ical4j.model.WeekDay which uses Calendar which is not compilable by GWT
 */
public class CalendarWeekDay 
{
	/**
	 * Sunday.
	 */
	public static final CalendarWeekDay SU = new CalendarWeekDay(Day.SU, 0) ;

	/**
	 * Monday.
	 */
	public static final CalendarWeekDay MO = new CalendarWeekDay(Day.MO, 0) ;

	/**
	 * Tuesday.
	 */
	public static final CalendarWeekDay TU = new CalendarWeekDay(Day.TU, 0) ;

	/**
	 * Wednesday.
	 */
	public static final CalendarWeekDay WE = new CalendarWeekDay(Day.WE, 0) ;

	/**
	 * Thursday.
	 */
	public static final CalendarWeekDay TH = new CalendarWeekDay(Day.TH, 0) ;

	/**
	 * Friday.
	 */
	public static final CalendarWeekDay FR = new CalendarWeekDay(Day.FR, 0) ;

	/**
	 * Saturday.
	 */
	public static final CalendarWeekDay SA = new CalendarWeekDay(Day.SA, 0) ;

	public enum Day { SU, MO, TU, WE, TH, FR, SA }

	private Day day ;
    
	private int offset ;
    
	/**
	 * @param value a string representation of a week day
	 */
	public CalendarWeekDay(final String value) 
	{
		if (value.length() > 2) 
			offset = Numbers.parseInt(value.substring(0, value.length() - 2)) ;
		else
			offset = 0 ;
		
		day = Day.valueOf(value.substring(value.length() - 2)) ;
		validateDay() ;
	}
    
	/**
	 * @param day a string representation of a week day
	 * @param offset a month offset value
	 */
	private CalendarWeekDay(final Day day, final int offset) 
	{
		this.day = day ;
		this.offset = offset ;
	}
    
	/**
	 * Constructs a new weekday instance based on the specified
	 * instance and offset.
	 * @param weekDay a week day template for the instance
	 * @param offset a month offset value
	 */
	public CalendarWeekDay(final CalendarWeekDay weekDay, final int offset)
	{
		this.day = weekDay.getDay() ;
		this.offset = offset ;
	}
    
	private void validateDay() 
	{
		if ((false == SU.day.equals(day)) &&
				(false == MO.day.equals(day)) &&
				(false == TU.day.equals(day)) &&
				(false == WE.day.equals(day)) &&
        (false == TH.day.equals(day)) &&
        (false == FR.day.equals(day)) &&
        (false == SA.day.equals(day))) 
			throw new IllegalArgumentException("Invalid day: " + day);
	}
	
	/**
	 * @return Returns the day.
	 */
	public final Day getDay() {
		return day ;
	}
    
	/**
	 * @return Returns the offset.
	 */
	public final int getOffset() {
		return offset ;
	}
        
	/**
	 * {@inheritDoc}
	 */
	public final String toString() 
	{
		final StringBuilder b = new StringBuilder() ;
		if (getOffset() != 0) 
			b.append(getOffset()) ;
		b.append(getDay()) ;
		return b.toString() ;
	}

	public static CalendarWeekDay getWeekDay(Day day) 
	{
		switch (day) 
		{
			case SU: return SU ;
			case MO: return MO ;
			case TU: return TU ;
			case WE: return WE ;
			case TH: return TH ;
			case FR: return FR ;
			case SA: return SA ;
			default: return null ;
		}
	}

	/**
	 * Returns a weekday representation of the specified calendar.
	 * @param cal a calendar (java.util)
	 * @return a weekday instance representing the specified calendar
	 */
/*
	public static LdvWeekDay getWeekDay(final Calendar cal) {
		return new LdvWeekDay(getDay(cal.get(Calendar.DAY_OF_WEEK)), 0) ;
	}
*/
	/**
	 * Returns a weekday/offset representation of the specified calendar.
	 * @param cal a calendar (java.util)
	 * @return a weekday instance representing the specified calendar
	 */
/*
	public static LdvWeekDay getMonthlyOffset(final Calendar cal) {
		return new LdvWeekDay(getDay(cal.get(Calendar.DAY_OF_WEEK)), cal.get(Calendar.DAY_OF_WEEK_IN_MONTH)) ;
	}
*/    
	/**
	 * Returns a weekday/negative offset representation of the specified calendar.
	 * @param cal a calendar (java.util)
	 * @return a weekday instance representing the specified calendar
	 */
/*
	public static LdvWeekDay getNegativeMonthlyOffset(final Calendar cal) {
		return new LdvWeekDay(getDay(cal.get(Calendar.DAY_OF_WEEK)), cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) - 6) ;
	}
*/    
	/**
	 * Returns the corresponding day constant to the specified
	 * java.util.Calendar.DAY_OF_WEEK property.
	 * @param calDay a property value of java.util.Calendar.DAY_OF_WEEK
	 * @return a string, or null if an invalid DAY_OF_WEEK property is specified
	 */
	public static CalendarWeekDay getLdvDay(final int calDay) 
	{
		CalendarWeekDay day = null ;
		switch (calDay) 
		{
			case LdvTime.SUNDAY:
				day = SU ;
				break ;
			case LdvTime.MONDAY:
				day = MO ;
				break ;
			case LdvTime.TUESDAY:
				day = TU ;
				break ;
			case LdvTime.WEDNESDAY:
				day = WE ;
				break ;
			case LdvTime.THURSDAY:
				day = TH ;
				break ;
			case LdvTime.FRIDAY:
				day = FR ;
				break ;
			case LdvTime.SATURDAY:
				day = SA ;
				break ;
			default:
				break ;
		}
		return day ;
	}
	
/*
	public static LdvWeekDay getDay(final int calDay) 
	{
		LdvWeekDay day = null ;
		switch (calDay) 
		{
			case Calendar.SUNDAY:
				day = SU ;
				break ;
			case Calendar.MONDAY:
				day = MO ;
				break ;
			case Calendar.TUESDAY:
				day = TU ;
				break ;
			case Calendar.WEDNESDAY:
				day = WE ;
				break ;
			case Calendar.THURSDAY:
				day = TH ;
				break ;
			case Calendar.FRIDAY:
				day = FR ;
				break ;
			case Calendar.SATURDAY:
				day = SA ;
				break ;
			default:
				break ;
		}
		return day ;
	}
*/
   
	/**
	 * Returns the corresponding <code>LdvTime.DAY_OF_WEEK</code>
	 * constant for the specified <code>WeekDay</code>.
	 * @param weekday a week day instance
	 * @return the corresponding <code>LdvInt</code> day
	 */
	public static int getLdvTimeDay(final CalendarWeekDay weekday) 
	{
    int calendarDay = -1 ;
    
    if (SU.getDay().equals(weekday.getDay())) 
    	calendarDay = LdvTime.SUNDAY ;
    else if (MO.getDay().equals(weekday.getDay()))
    	calendarDay = LdvTime.MONDAY ;
    else if (TU.getDay().equals(weekday.getDay()))
    	calendarDay = LdvTime.TUESDAY ;
    else if (WE.getDay().equals(weekday.getDay()))
    	calendarDay = LdvTime.WEDNESDAY ;
    else if (TH.getDay().equals(weekday.getDay()))
    	calendarDay = LdvTime.THURSDAY ;
    else if (FR.getDay().equals(weekday.getDay()))
    	calendarDay = LdvTime.FRIDAY ;
    else if (SA.getDay().equals(weekday.getDay()))
    	calendarDay = LdvTime.SATURDAY ;

    return calendarDay ;
	}
	
	/**
	 * Returns the corresponding <code>java.util.Calendar.DAY_OF_WEEK</code>
	 * constant for the specified <code>WeekDay</code>.
	 * @param weekday a week day instance
	 * @return the corresponding <code>java.util.Calendar</code> day
	 */
/*
	public static int getCalendarDay(final LdvWeekDay weekday) {
        int calendarDay = -1;
        if (SU.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.SUNDAY;
        }
        else if (MO.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.MONDAY;
        }
        else if (TU.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.TUESDAY;
        }
        else if (WE.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.WEDNESDAY;
        }
        else if (TH.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.THURSDAY;
        }
        else if (FR.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.FRIDAY;
        }
        else if (SA.getDay().equals(weekday.getDay())) {
            calendarDay = Calendar.SATURDAY;
        }
        return calendarDay;
    }
*/
    
	/**
	 * {@inheritDoc}
	 */
	public final boolean equals(final Object arg0) 
	{
		if (null == arg0) 
			return false ;
        
		if (false == (arg0 instanceof CalendarWeekDay)) 
			return false ;
        
		final CalendarWeekDay wd = (CalendarWeekDay) arg0 ;
			return ObjectUtils.equals(wd.getDay(), getDay()) && wd.getOffset() == getOffset() ;
	}
    
	/**
	 * {@inheritDoc}
	 */
	public final int hashCode() {
		return new HashCodeBuilder().append(getDay()).append(getOffset()).toHashCode() ;
	}
}
