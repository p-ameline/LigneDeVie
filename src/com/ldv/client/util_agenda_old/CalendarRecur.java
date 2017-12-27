package com.ldv.client.util_agenda;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ldv.shared.model.LdvTime;

import net.fortuna.ical4j.model.NumberList;


/**
 * $Id$ [18-Apr-2004]
 * <p/>
 * Defines a recurrence.
 *
 * @author Ben Fortuna
 * @version 2.0
 */
public class CalendarRecur 
{
	private static final String FREQ       = "FREQ" ;
	private static final String UNTIL      = "UNTIL" ;
	private static final String COUNT      = "COUNT" ;
	private static final String INTERVAL   = "INTERVAL" ;
	private static final String BYSECOND   = "BYSECOND" ;
	private static final String BYMINUTE   = "BYMINUTE" ;
	private static final String BYHOUR     = "BYHOUR" ;
	private static final String BYDAY      = "BYDAY" ;
	private static final String BYMONTHDAY = "BYMONTHDAY" ;
	private static final String BYYEARDAY  = "BYYEARDAY" ;
	private static final String BYWEEKNO   = "BYWEEKNO" ;
	private static final String BYMONTH    = "BYMONTH" ;
	private static final String BYSETPOS   = "BYSETPOS" ;
	private static final String WKST       = "WKST" ;

	/**
	 * Second frequency resolution.
	 */
	public static final String SECONDLY = "SECONDLY" ;

	/**
	 * Minute frequency resolution.
	 */
	public static final String MINUTELY = "MINUTELY" ;

    /**
     * Hour frequency resolution.
     */
    public static final String HOURLY = "HOURLY";

    /**
     * Day frequency resolution.
     */
    public static final String DAILY = "DAILY";

    /**
     * Week frequency resolution.
     */
    public static final String WEEKLY = "WEEKLY";

    /**
     * Month frequency resolution.
     */
    public static final String MONTHLY = "MONTHLY";

    /**
     * Year frequency resolution.
     */
    public static final String YEARLY = "YEARLY";

	/**
	 * When calculating dates matching this recur ({@code getDates()} or {@code getNextDate}),
	 * this property defines the maximum number of attempt to find a matching date by
	 * incrementing the seed.
	 * <p>The default value is 1000. A value of -1 corresponds to no maximum.</p>
	 */
	public static final String KEY_MAX_INCREMENT_COUNT = "net.fortuna.ical4j.recur.maxincrementcount" ;

	private static int maxIncrementCount ;

	static {
        final String value = Configurator.getProperty(KEY_MAX_INCREMENT_COUNT);
        if (value != null && value.length() > 0) {
            maxIncrementCount = Integer.parseInt(value);
        } else {
            maxIncrementCount = 1000;
        }
	}

	private transient Logger log = LoggerFactory.getLogger(CalendarRecur.class);

	private String              frequency ;
	private Date                until ;
	private int                 count = -1 ;
	private int                 interval = -1 ;
	private CalendarNumberList       secondList ;
	private CalendarNumberList       minuteList ;
	private CalendarNumberList       hourList ;
	private CalendarWeekDayList      dayList ;
	private CalendarNumberList       monthDayList ;
	private CalendarNumberList       yearDayList ;
	private CalendarNumberList       weekNoList ;
	private CalendarNumberList       monthList ;
	private CalendarNumberList       setPosList ;
	private CalendarWeekDay.Day      weekStartDay ;
	private int                 calendarWeekStartDay ;
	private Map<String, String> experimentalValues = new HashMap<String, String>() ;

	// Calendar field we increment based on frequency.
	private int calIncField ;

	/**
	 * Default constructor.
	 */
	public CalendarRecur() {
		// default week start is Monday per RFC5545
		calendarWeekStartDay = LdvTime.MONDAY ;
	}

	/**
	 * Constructs a new instance from the specified string value.
	 *
	 * @param aValue a string representation of a recurrence.
	 * @throws ParseException thrown when the specified string contains an invalid representation of an UNTIL date value
	 */
	public CalendarRecur(final String aValue) throws ParseException 
	{
		// default week start is Monday per RFC5545
		calendarWeekStartDay = LdvTime.MONDAY ;

		Iterator<String> tokens = Arrays.asList(aValue.split("[;=]")).iterator() ;
		while (tokens.hasNext()) 
		{
			final String token = tokens.next() ;
            
			if (FREQ.equals(token)) 
				frequency = nextToken(tokens, token) ;
			else if (UNTIL.equals(token)) 
			{
				final String untilString = nextToken(tokens, token) ;
				if ((null != untilString) && untilString.contains("T")) 
				{
					until = new DateTime(untilString) ;
					// UNTIL must be specified in UTC time..
					((DateTime) until).setUtc(true) ;
				}
				else 
					until = new Date(untilString) ;
			}
			else if (COUNT.equals(token)) 
				count = Integer.parseInt(nextToken(tokens, token)) ;
			else if (INTERVAL.equals(token)) 
				interval = Integer.parseInt(nextToken(tokens, token)) ;
			else if (BYSECOND.equals(token)) 
				secondList = new CalendarNumberList(nextToken(tokens, token), 0, 59, false) ;
			else if (BYMINUTE.equals(token)) 
				minuteList = new CalendarNumberList(nextToken(tokens, token), 0, 59, false) ;
			else if (BYHOUR.equals(token)) 
				hourList = new CalendarNumberList(nextToken(tokens, token), 0, 23, false) ;
			else if (BYDAY.equals(token)) 
				dayList = new CalendarWeekDayList(nextToken(tokens, token)) ;
			else if (BYMONTHDAY.equals(token)) 
				monthDayList = new CalendarNumberList(nextToken(tokens, token), 1, 31, true) ;
			else if (BYYEARDAY.equals(token))
				yearDayList = new CalendarNumberList(nextToken(tokens, token), 1, 366, true) ;
			else if (BYWEEKNO.equals(token))
				weekNoList = new CalendarNumberList(nextToken(tokens, token), 1, 53, true) ;
			else if (BYMONTH.equals(token))
				monthList = new CalendarNumberList(nextToken(tokens, token), 1, 12, false) ;
			else if (BYSETPOS.equals(token))
				setPosList = new CalendarNumberList(nextToken(tokens, token), 1, 366, true) ;
			else if (WKST.equals(token))
			{
				weekStartDay = CalendarWeekDay.Day.valueOf(nextToken(tokens, token)) ;
				calendarWeekStartDay = CalendarWeekDay.getLdvTimeDay(CalendarWeekDay.getWeekDay(weekStartDay)) ;
			} 
			else
			{
				if (CompatibilityHints.isHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING))
					// assume experimental value..
					experimentalValues.put(token, nextToken(tokens, token)) ;
				else 
					throw new IllegalArgumentException(String.format("Invalid recurrence rule part: %s=%s", token, nextToken(tokens, token))) ;    
			}
		}
		validateFrequency() ;
	}

	private String nextToken(Iterator<String> tokens, String lastToken) 
	{
		try {
			return tokens.next() ;
		} catch (NoSuchElementException e) {
			throw new IllegalArgumentException("Missing expected token, last token: " + lastToken) ;
		}
	}

	/**
	 * @param frequency a recurrence frequency string
	 * @param until     maximum recurrence date
	 */
	public CalendarRecur(final String frequency, final Date until) 
	{
		// default week start is Monday per RFC5545
		calendarWeekStartDay = LdvTime.MONDAY ;
		
		this.frequency = frequency ;
		this.until     = until ;
		
		validateFrequency() ; 
	}

	/**
	 * @param frequency a recurrence frequency string
	 * @param count     maximum recurrence count
	 */
	public CalendarRecur(final String frequency, final int count) 
	{
		// default week start is Monday per RFC5545
		calendarWeekStartDay = LdvTime.MONDAY ;
		
		this.frequency = frequency ;
		this.count     = count ;
		
		validateFrequency() ;
	}

	/**
	 * @return Returns the dayList.
	 */
	public final CalendarWeekDayList getDayList() 
	{
		if (null == dayList) 
			dayList = new CalendarWeekDayList() ;
        
		return dayList;
	}

	/**
	 * @return Returns the hourList.
	 */
	public final CalendarNumberList getHourList() 
	{
		if (null == hourList) 
			hourList = new CalendarNumberList(0, 23, false) ;
        
		return hourList ;
	}

	/**
	 * @return Returns the minuteList.
	 */
	public final CalendarNumberList getMinuteList()
	{
		if (null == minuteList)
			minuteList = new CalendarNumberList(0, 59, false) ;

		return minuteList ;
	}

	/**
	 * @return Returns the monthDayList.
	 */
	public final CalendarNumberList getMonthDayList()
	{
		if (null == monthDayList)
			monthDayList = new CalendarNumberList(1, 31, true) ;

		return monthDayList ;
	}

	/**
	 * @return Returns the monthList.
	 */
	public final CalendarNumberList getMonthList()
	{
		if (null == monthList)
			monthList = new CalendarNumberList(1, 12, false) ;

		return monthList ;
	}

	/**
	 * @return Returns the secondList.
	 */
	public final CalendarNumberList getSecondList()
	{
		if (null == secondList)
			secondList = new CalendarNumberList(0, 59, false) ;
        
		return secondList ;
	}

	/**
	 * @return Returns the setPosList.
	 */
	public final CalendarNumberList getSetPosList()
	{
		if (null == setPosList)
			setPosList = new CalendarNumberList(1, 366, true) ;

		return setPosList ;
	}

	/**
	 * @return Returns the weekNoList.
	 */
	public final CalendarNumberList getWeekNoList()
	{
		if (null == weekNoList)
			weekNoList = new CalendarNumberList(1, 53, true) ;

		return weekNoList ;
	}

	/**
	 * @return Returns the yearDayList.
	 */
	public final CalendarNumberList getYearDayList()
	{
		if (null == yearDayList)
			yearDayList = new CalendarNumberList(1, 366, true) ;

		return yearDayList ;
	}

	/**
	 * @return Returns the count or -1 if the rule does not have a count.
	 */
	public final int getCount() {
		return count;
	}

	/**
	 * @return Returns the experimentalValues.
	 */
	public final Map<String, String> getExperimentalValues() {
		return experimentalValues;
	}

	/**
	 * @return Returns the frequency.
	 */
	public final String getFrequency() {
		return frequency;
	}

	/**
	 * @return Returns the interval or -1 if the rule does not have an interval defined.
	 */
	public final int getInterval() {
		return interval;
	}

	/**
	 * @return Returns the until or null if there is none.
	 */
	public final Date getUntil() {
		return until;
	}

	/**
	 * @return Returns the weekStartDay or null if there is none.
	 */
	public final CalendarWeekDay.Day getWeekStartDay() {
		return weekStartDay ;
	}

	/**
	 * @param weekStartDay The weekStartDay to set.
	 */
	public final void setWeekStartDay(final CalendarWeekDay.Day weekStartDay) 
	{
		this.weekStartDay = weekStartDay ;
		if (null != weekStartDay)
			calendarWeekStartDay = CalendarWeekDay.getLdvTimeDay(CalendarWeekDay.getWeekDay(weekStartDay)) ; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String toString()
	{
        final StringBuilder b = new StringBuilder();
        b.append(FREQ);
        b.append('=');
        b.append(frequency);
        if (weekStartDay != null) {
            b.append(';');
            b.append(WKST);
            b.append('=');
            b.append(weekStartDay);
        }
        if (until != null) {
            b.append(';');
            b.append(UNTIL);
            b.append('=');
            // Note: date-time representations should always be in UTC time.
            b.append(until);
        }
        if (count >= 1) {
            b.append(';');
            b.append(COUNT);
            b.append('=');
            b.append(count);
        }
        if (interval >= 1) {
            b.append(';');
            b.append(INTERVAL);
            b.append('=');
            b.append(interval);
        }
        if (!getMonthList().isEmpty()) {
            b.append(';');
            b.append(BYMONTH);
            b.append('=');
            b.append(monthList);
        }
        if (!getWeekNoList().isEmpty()) {
            b.append(';');
            b.append(BYWEEKNO);
            b.append('=');
            b.append(weekNoList);
        }
        if (!getYearDayList().isEmpty()) {
            b.append(';');
            b.append(BYYEARDAY);
            b.append('=');
            b.append(yearDayList);
        }
        if (!getMonthDayList().isEmpty()) {
            b.append(';');
            b.append(BYMONTHDAY);
            b.append('=');
            b.append(monthDayList);
        }
        if (!getDayList().isEmpty()) {
            b.append(';');
            b.append(BYDAY);
            b.append('=');
            b.append(dayList);
        }
        if (!getHourList().isEmpty()) {
            b.append(';');
            b.append(BYHOUR);
            b.append('=');
            b.append(hourList);
        }
        if (!getMinuteList().isEmpty()) {
            b.append(';');
            b.append(BYMINUTE);
            b.append('=');
            b.append(minuteList);
        }
        if (!getSecondList().isEmpty()) {
            b.append(';');
            b.append(BYSECOND);
            b.append('=');
            b.append(secondList);
        }
        if (!getSetPosList().isEmpty()) {
            b.append(';');
            b.append(BYSETPOS);
            b.append('=');
            b.append(setPosList);
        }
        return b.toString();
    }

	/**
	 * Returns a list of start dates in the specified period represented by this recur. Any date fields not specified by
	 * this recur are retained from the period start, and as such you should ensure the period start is initialised
	 * correctly.
	 *
	 * @param periodStart the start of the period
	 * @param periodEnd   the end of the period
	 * @param value       the type of dates to generate (i.e. date/date-time)
	 * @return a list of dates
	 */
	public final DateList getDates(final Date periodStart,
                                   final Date periodEnd, final Value value) {
        return getDates(periodStart, periodStart, periodEnd, value, -1);
    }

    /**
     * Convenience method for retrieving recurrences in a specified period.
     *
     * @param seed   a seed date for generating recurrence instances
     * @param period the period of returned recurrence dates
     * @param value  type of dates to generate
     * @return a list of dates
     */
    public final DateList getDates(final Date seed, final Period period,
                                   final Value value) {
        return getDates(seed, period.getStart(), period.getEnd(), value, -1);
    }

    /**
     * Returns a list of start dates in the specified period represented by this recur. This method includes a base date
     * argument, which indicates the start of the fist occurrence of this recurrence. The base date is used to inject
     * default values to return a set of dates in the correct format. For example, if the search start date (start) is
     * Wed, Mar 23, 12:19PM, but the recurrence is Mon - Fri, 9:00AM - 5:00PM, the start dates returned should all be at
     * 9:00AM, and not 12:19PM.
     *
     * @param seed        the start date of this Recurrence's first instance
     * @param periodStart the start of the period
     * @param periodEnd   the end of the period
     * @param value       the type of dates to generate (i.e. date/date-time)
     * @return a list of dates represented by this recur instance
     */
    public final DateList getDates(final Date seed, final Date periodStart,
                                   final Date periodEnd, final Value value) {
        return getDates(seed, periodStart, periodEnd, value, -1);
    }

	/**
	 * Returns a list of start dates in the specified period represented by this recur. This method includes a base date
	 * argument, which indicates the start of the fist occurrence of this recurrence. The base date is used to inject
	 * default values to return a set of dates in the correct format. For example, if the search start date (start) is
	 * Wed, Mar 23, 12:19PM, but the recurrence is Mon - Fri, 9:00AM - 5:00PM, the start dates returned should all be at
	 * 9:00AM, and not 12:19PM.
	 *
	 * @param seed        the start date of this Recurrence's first instance
	 * @param periodStart the start of the period
	 * @param periodEnd   the end of the period
	 * @param value       the type of dates to generate (i.e. date/date-time)
	 * @param maxCount    limits the number of instances returned. Up to one years
	 *                    worth extra may be returned. Less than 0 means no limit
	 * @return a list of dates represented by this recur instance
	 */
	public final DateList getDates(final Date seed, final Date periodStart, final Date periodEnd, final Value value,
                                   final int maxCount)
	{
		final DateList dates = new DateList(value) ;
		if (seed instanceof DateTime) 
		{
			if (((DateTime) seed).isUtc()) 
				dates.setUtc(true);
			else 
				dates.setTimeZone(((DateTime) seed).getTimeZone());
		}
        
		LdvTime cal = new LdvTime(seed) ;
		
		// optimize the start time for selecting candidates
		// (only applicable where a COUNT is not specified)
		if (getCount() < 1) 
		{
			LdvTime seededCal = new LdvTime(0) ;
			seededCal.initFromLdvTime(cal) ;
			while (seededCal.isBefore(cal))
			{
				cal.initFromLdvTime(seededCal) ;
				increment(seededCal) ;
			}
		}

		HashSet<Date> invalidCandidates = new HashSet<Date>() ;
		int noCandidateIncrementCount = 0 ;
		Date candidate = null ;
		while ((maxCount < 0) || (dates.size() < maxCount)) 
		{
			final Date candidateSeed = getInstance(cal.toJavaDate(), value) ;

			if ((getUntil() != null) && (candidate != null) && candidate.after(getUntil())) 
				break ;
            
			if ((periodEnd != null) && (candidate != null) && candidate.after(periodEnd))
				break ;
            
			if ((getCount() >= 1) && (dates.size() + invalidCandidates.size()) >= getCount())
				break ;

//            if (Value.DATE_TIME.equals(value)) {
			if (candidateSeed instanceof DateTime)
			{
				if (dates.isUtc())
					((DateTime) candidateSeed).setUtc(true) ;
				else 
					((DateTime) candidateSeed).setTimeZone(dates.getTimeZone()) ;
			}

			final DateList candidates = getCandidates(candidateSeed, value) ;
			
			if (false == candidates.isEmpty()) 
			{
				noCandidateIncrementCount = 0 ;
                
				// sort candidates for identifying when UNTIL date is exceeded..
				Collections.sort(candidates) ;
				for (Date candidate1 : candidates) 
				{
					candidate = candidate1 ;
					// don't count candidates that occur before the seed date..
					if (false == candidate.before(seed))
					{
						// candidates exclusive of periodEnd..
						if (candidate.before(periodStart) || !candidate.before(periodEnd))
							invalidCandidates.add(candidate) ;
						else if ((getCount() >= 1) && (dates.size() + invalidCandidates.size()) >= getCount())
							break ;
						else if (!(getUntil() != null && candidate.after(getUntil()))) 
							dates.add(candidate) ;
					}
				}
			} 
			else 
			{
				noCandidateIncrementCount++ ;
				if ((maxIncrementCount > 0) && (noCandidateIncrementCount > maxIncrementCount)) 
					break ;
			}
			increment(cal) ;
		}
		// sort final list..
		Collections.sort(dates) ;
		return dates ;
	}

	/**
	 * Returns the next date of this recurrence given a seed date and start date.
	 * The seed date indicates the start of the fist occurrence of this recurrence.
	 * The start date is the starting date to search for the next recurrence.
	 * Return null if there is no occurrence date after start date.
	 *
	 * @param seed      the start date of this Recurrence's first instance
	 * @param startDate the date to start the search
	 * 
	 * @return the next date in the recurrence series after startDate
	 */
	public final Date getNextDate(final Date seed, final Date startDate) 
	{
		LdvTime cal = new LdvTime(seed) ;
		
		// optimize the start time for selecting candidates
		// (only applicable where a COUNT is not specified)
		if (getCount() < 1)
		{
			LdvTime seededCal = new LdvTime(0) ;
			seededCal.initFromLdvTime(cal) ;
			
			while (seededCal.toJavaDate().before(startDate))
			{
				cal.initFromLdvTime(seededCal) ;
				increment(seededCal) ;
			}
		}

		int invalidCandidateCount     = 0 ;
		int noCandidateIncrementCount = 0 ;
		Date candidate = null ;
		final Value value = seed instanceof DateTime ? Value.DATE_TIME : Value.DATE ;

		while (true) 
		{
			final Date candidateSeed = getInstance(cal.toJavaDate(), value) ;

			if ((getUntil() != null) && (candidate != null) && candidate.after(getUntil()))
				break ;

			if ((getCount() > 0) && (invalidCandidateCount >= getCount())) 
				break ;

			if (Value.DATE_TIME.equals(value))
			{
				if (((DateTime) seed).isUtc()) 
					((DateTime) candidateSeed).setUtc(true) ;
				else
					((DateTime) candidateSeed).setTimeZone(((DateTime) seed).getTimeZone()) ;
			}

			final DateList candidates = getCandidates(candidateSeed, value) ;
			if (false == candidates.isEmpty())
			{
				noCandidateIncrementCount = 0 ;
                
				// sort candidates for identifying when UNTIL date is exceeded..
				Collections.sort(candidates) ;

				for (Date candidate1 : candidates)
				{
					candidate = candidate1 ;
					// don't count candidates that occur before the seed date..
					if (false == candidate.before(seed)) 
					{
						// Candidate must be after startDate because
						// we want the NEXT occurrence
						if (false == candidate.after(startDate))
							invalidCandidateCount++ ;
						else if (getCount() > 0 && invalidCandidateCount >= getCount()) 
							break ;
						else if (!(getUntil() != null && candidate.after(getUntil()))) 
							return candidate ;
					}
				}
			}
			else 
			{
				noCandidateIncrementCount++ ;
				if ((maxIncrementCount > 0) && (noCandidateIncrementCount > maxIncrementCount))
					break ;
			}
			increment(cal) ;
		}
		return null ;
	}

	/**
	 * Increments the specified calendar according to the frequency and interval specified in this recurrence rule.
	 *
	 * @param cal a java.util.Calendar to increment
	 */
	private void increment(final LdvTime cal) 
	{
		// initialize interval..
		int calInterval = (getInterval() >= 1) ? getInterval() : 1 ;
		cal.add(calIncField, calInterval, true) ;
	}

    /**
     * Returns a list of possible dates generated from the applicable BY* rules, using the specified date as a seed.
     *
     * @param date  the seed date
     * @param value the type of date list to return
     * @return a DateList
     */
    private DateList getCandidates(final Date date, final Value value) {
        DateList dates = new DateList(value);
        if (date instanceof DateTime) {
            if (((DateTime) date).isUtc()) {
                dates.setUtc(true);
            } else {
                dates.setTimeZone(((DateTime) date).getTimeZone());
            }
        }
        dates.add(date);
        dates = getMonthVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYMONTH processing: " + dates);
        }
        dates = getWeekNoVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYWEEKNO processing: " + dates);
        }
        dates = getYearDayVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYYEARDAY processing: " + dates);
        }
        dates = getMonthDayVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYMONTHDAY processing: " + dates);
        }
        dates = getDayVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYDAY processing: " + dates);
        }
        dates = getHourVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYHOUR processing: " + dates);
        }
        dates = getMinuteVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYMINUTE processing: " + dates);
        }
        dates = getSecondVariants(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after BYSECOND processing: " + dates);
        }
        dates = applySetPosRules(dates);
        // debugging..
        if (log.isDebugEnabled()) {
            log.debug("Dates after SETPOS processing: " + dates);
        }
        return dates;
    }

    /**
     * Applies BYSETPOS rules to <code>dates</code>. Valid positions are from 1 to the size of the date list. Invalid
     * positions are ignored.
     *
     * @param dates
     */
    private DateList applySetPosRules(final DateList dates) {
        // return if no SETPOS rules specified..
        if (getSetPosList().isEmpty()) {
            return dates;
        }
        // sort the list before processing..
        Collections.sort(dates);
        final DateList setPosDates = getDateListInstance(dates);
        final int size = dates.size();
        for (final Integer setPos : getSetPosList()) {
            final int pos = setPos;
            if (pos > 0 && pos <= size) {
                setPosDates.add(dates.get(pos - 1));
            } else if (pos < 0 && pos >= -size) {
                setPosDates.add(dates.get(size + pos));
            }
        }
        return setPosDates;
    }

	/**
	 * Applies BYMONTH rules specified in this Recur instance to the specified date list. If no BYMONTH rules are
	 * specified the date list is returned unmodified.
	 *
	 * @param dates
	 * @return
	 */
	private DateList getMonthVariants(final DateList dates) 
	{
		if (getMonthList().isEmpty())
			return dates ;
        
		final DateList monthlyDates = getDateListInstance(dates) ;
		for (final Date date : dates)
		{
			LdvTime cal     = getLdvTimeInstance(date, true) ;
			
			LdvTime freqEnd = getLdvTimeInstance(date, true) ;
			increment(freqEnd) ;
            
			for (final Integer month : getMonthList()) 
			{
				// Java months are zero-based..
//                cal.set(Calendar.MONTH, month.intValue() - 1);
				cal.roll(LdvTime.MONTH, (month - 1) - cal.get(LdvTime.MONTH)) ;
				if (cal.isAfter(freqEnd)) 
					break; // Do not break out of the FREQ-defined boundary
                
				monthlyDates.add(getInstance(cal.toJavaDate(), monthlyDates.getType())) ;
			}
		}
		return monthlyDates ;
	}

	/**
	 * Applies BYWEEKNO rules specified in this Recur instance to the specified date list. If no BYWEEKNO rules are
	 * specified the date list is returned unmodified.
	 *
	 * @param dates
	 * @return
	 */
	private DateList getWeekNoVariants(final DateList dates) 
	{
		if (getWeekNoList().isEmpty()) 
			return dates ;
        
		final DateList weekNoDates = getDateListInstance(dates) ;
		for (final Date date : dates)
		{
			LdvTime cal = new LdvTime(date) ;
			for (final Integer weekNo : getWeekNoList()) 
			{
				cal.set(LdvTime.WEEK_OF_YEAR, getAbsWeekNo(cal, weekNo)) ;
                weekNoDates.add(Dates.getInstance(cal.getTime(), weekNoDates.getType()));
            }
        }
        return weekNoDates;
    }

    /**
     * Applies BYYEARDAY rules specified in this Recur instance to the specified date list. If no BYYEARDAY rules are
     * specified the date list is returned unmodified.
     *
     * @param dates
     * @return
     */
    private DateList getYearDayVariants(final DateList dates) {
        if (getYearDayList().isEmpty()) {
            return dates;
        }
        final DateList yearDayDates = getDateListInstance(dates);
        for (final Date date : dates) {
            final Calendar cal = getCalendarInstance(date, true);
            for (final Integer yearDay : getYearDayList()) {
                cal.set(Calendar.DAY_OF_YEAR, Dates.getAbsYearDay(cal.getTime(), yearDay));
                yearDayDates.add(Dates.getInstance(cal.getTime(), yearDayDates.getType()));
            }
        }
        return yearDayDates;
    }

    /**
     * Applies BYMONTHDAY rules specified in this Recur instance to the specified date list. If no BYMONTHDAY rules are
     * specified the date list is returned unmodified.
     *
     * @param dates
     * @return
     */
    private DateList getMonthDayVariants(final DateList dates) {
        if (getMonthDayList().isEmpty()) {
            return dates;
        }
        final DateList monthDayDates = getDateListInstance(dates);
        for (final Date date : dates) {
            final Calendar cal = getCalendarInstance(date, false);
            for (final Integer monthDay : getMonthDayList()) {
                try {
                    cal.set(Calendar.DAY_OF_MONTH, Dates.getAbsMonthDay(cal.getTime(), monthDay));
                    monthDayDates.add(Dates.getInstance(cal.getTime(), monthDayDates.getType()));
                } catch (IllegalArgumentException iae) {
                    if (log.isTraceEnabled()) {
                        log.trace("Invalid day of month: " + Dates.getAbsMonthDay(cal
                                .getTime(), monthDay));
                    }
                }
            }
        }
        return monthDayDates;
    }

    /**
     * Applies BYDAY rules specified in this Recur instance to the specified date list. If no BYDAY rules are specified
     * the date list is returned unmodified.
     *
     * @param dates
     * @return
     */
    private DateList getDayVariants(final DateList dates) {
        if (getDayList().isEmpty()) {
            return dates;
        }
        final DateList weekDayDates = getDateListInstance(dates);
        for (final Date date : dates) {
            for (final WeekDay weekDay : getDayList()) {
                // if BYYEARDAY or BYMONTHDAY is specified filter existing
                // list..
                if (!getYearDayList().isEmpty() || !getMonthDayList().isEmpty()) {
                    final Calendar cal = getCalendarInstance(date, true);
                    if (weekDay.equals(WeekDay.getWeekDay(cal))) {
                        weekDayDates.add(date);
                    }
                } else {
                    weekDayDates.addAll(getAbsWeekDays(date, dates.getType(), weekDay));
                }
            }
        }
        return weekDayDates;
    }

    /**
     * Returns a list of applicable dates corresponding to the specified week day in accordance with the frequency
     * specified by this recurrence rule.
     *
     * @param date
     * @param weekDay
     * @return
     */
    private List<Date> getAbsWeekDays(final Date date, final Value type, final WeekDay weekDay) {
        final Calendar cal = getCalendarInstance(date, true);
        final DateList days = new DateList(type);
        if (date instanceof DateTime) {
            if (((DateTime) date).isUtc()) {
                days.setUtc(true);
            } else {
                days.setTimeZone(((DateTime) date).getTimeZone());
            }
        }
        final int calDay = WeekDay.getCalendarDay(weekDay);
        if (calDay == -1) {
            // a matching weekday cannot be identified..
            return days;
        }
        if (DAILY.equals(getFrequency())) {
            if (cal.get(Calendar.DAY_OF_WEEK) == calDay) {
                days.add(Dates.getInstance(cal.getTime(), type));
            }
        } else if (WEEKLY.equals(getFrequency()) || !getWeekNoList().isEmpty()) {
            final int weekNo = cal.get(Calendar.WEEK_OF_YEAR);
            // construct a list of possible week days..
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            while (cal.get(Calendar.DAY_OF_WEEK) != calDay) {
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }
//            final int weekNo = cal.get(Calendar.WEEK_OF_YEAR);
            if (cal.get(Calendar.WEEK_OF_YEAR) == weekNo) {
                days.add(Dates.getInstance(cal.getTime(), type));
//                cal.add(Calendar.DAY_OF_WEEK, Dates.DAYS_PER_WEEK);
            }
        } else if (MONTHLY.equals(getFrequency()) || !getMonthList().isEmpty()) {
            final int month = cal.get(Calendar.MONTH);
            // construct a list of possible month days..
            cal.set(Calendar.DAY_OF_MONTH, 1);
            while (cal.get(Calendar.DAY_OF_WEEK) != calDay) {
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            while (cal.get(Calendar.MONTH) == month) {
                days.add(Dates.getInstance(cal.getTime(), type));
                cal.add(Calendar.DAY_OF_MONTH, Dates.DAYS_PER_WEEK);
            }
        } else if (YEARLY.equals(getFrequency())) {
            final int year = cal.get(Calendar.YEAR);
            // construct a list of possible year days..
            cal.set(Calendar.DAY_OF_YEAR, 1);
            while (cal.get(Calendar.DAY_OF_WEEK) != calDay) {
                cal.add(Calendar.DAY_OF_YEAR, 1);
            }
            while (cal.get(Calendar.YEAR) == year) {
                days.add(Dates.getInstance(cal.getTime(), type));
                cal.add(Calendar.DAY_OF_YEAR, Dates.DAYS_PER_WEEK);
            }
        }
        return getOffsetDates(days, weekDay.getOffset());
    }

    /**
     * Returns a single-element sublist containing the element of <code>list</code> at <code>offset</code>. Valid
     * offsets are from 1 to the size of the list. If an invalid offset is supplied, all elements from <code>list</code>
     * are added to <code>sublist</code>.
     *
     * @param dates
     * @param offset
     */
    private List<Date> getOffsetDates(final DateList dates, final int offset) {
        if (offset == 0) {
            return dates;
        }
        final List<Date> offsetDates = getDateListInstance(dates);
        final int size = dates.size();
        if (offset < 0 && offset >= -size) {
            offsetDates.add(dates.get(size + offset));
        } else if (offset > 0 && offset <= size) {
            offsetDates.add(dates.get(offset - 1));
        }
        return offsetDates;
    }

    /**
     * Applies BYHOUR rules specified in this Recur instance to the specified date list. If no BYHOUR rules are
     * specified the date list is returned unmodified.
     *
     * @param dates
     * @return
     */
    private DateList getHourVariants(final DateList dates) {
        if (getHourList().isEmpty()) {
            return dates;
        }
        final DateList hourlyDates = getDateListInstance(dates);
        for (final Date date : dates) {
            final Calendar cal = getCalendarInstance(date, true);
            for (final Integer hour : getHourList()) {
                cal.set(Calendar.HOUR_OF_DAY, hour);
                hourlyDates.add(Dates.getInstance(cal.getTime(), hourlyDates.getType()));
            }
        }
        return hourlyDates;
    }

    /**
     * Applies BYMINUTE rules specified in this Recur instance to the specified date list. If no BYMINUTE rules are
     * specified the date list is returned unmodified.
     *
     * @param dates
     * @return
     */
    private DateList getMinuteVariants(final DateList dates) {
        if (getMinuteList().isEmpty()) {
            return dates;
        }
        final DateList minutelyDates = getDateListInstance(dates);
        for (final Date date : dates) {
            final Calendar cal = getCalendarInstance(date, true);
            for (final Integer minute : getMinuteList()) {
                cal.set(Calendar.MINUTE, minute);
                minutelyDates.add(Dates.getInstance(cal.getTime(), minutelyDates.getType()));
            }
        }
        return minutelyDates;
    }

    /**
     * Applies BYSECOND rules specified in this Recur instance to the specified date list. If no BYSECOND rules are
     * specified the date list is returned unmodified.
     *
     * @param dates
     * @return
     */
    private DateList getSecondVariants(final DateList dates) {
        if (getSecondList().isEmpty()) {
            return dates;
        }
        final DateList secondlyDates = getDateListInstance(dates);
        for (final Date date : dates) {
            final Calendar cal = getCalendarInstance(date, true);
            for (final Integer second : getSecondList()) {
                cal.set(Calendar.SECOND, second);
                secondlyDates.add(Dates.getInstance(cal.getTime(), secondlyDates.getType()));
            }
        }
        return secondlyDates;
    }

	private void validateFrequency() 
	{
		if (null == frequency) 
			throw new IllegalArgumentException("A recurrence rule MUST contain a FREQ rule part.") ;
        
		if      (SECONDLY.equals(getFrequency())) 
			calIncField = LdvTime.SECOND ;
		else if (MINUTELY.equals(getFrequency()))
			calIncField = LdvTime.MINUTE ;
		else if (HOURLY.equals(getFrequency()))
			calIncField = LdvTime.HOUR_OF_DAY ;
		else if (DAILY.equals(getFrequency()))
			calIncField = LdvTime.DAY_OF_YEAR ;
		else if (WEEKLY.equals(getFrequency()))
			calIncField = LdvTime.WEEK_OF_YEAR ;
		else if (MONTHLY.equals(getFrequency()))
			calIncField = LdvTime.MONTH ;
		else if (YEARLY.equals(getFrequency()))
			calIncField = LdvTime.YEAR ;
		else
			throw new IllegalArgumentException("Invalid FREQ rule part '" + frequency + "' in recurrence rule") ;
	}

    /**
     * @param count The count to set.
     */
    public final void setCount(final int count) {
        this.count = count;
        this.until = null;
    }

    /**
     * @param frequency The frequency to set.
     */
    public final void setFrequency(final String frequency) {
        this.frequency = frequency;
        validateFrequency();
    }

    /**
     * @param interval The interval to set.
     */
    public final void setInterval(final int interval) {
        this.interval = interval;
    }

    /**
     * @param until The until to set.
     */
    public final void setUntil(final Date until) {
        this.until = until;
        this.count = -1;
    }

	/**
	 * Construct a Calendar object and sets the time.
	 *
	 * @param date
	 * @param lenient
	 * @return
	 */
/*
	private Calendar getCalendarInstance(final Date date, final boolean lenient) 
	{
		Calendar cal = Dates.getCalendarInstance(date);
		// A week should have at least 4 days to be considered as such per RFC5545
		cal.setMinimalDaysInFirstWeek(4);
		cal.setFirstDayOfWeek(calendarWeekStartDay);
		cal.setLenient(lenient);
		cal.setTime(date);

		return cal;
	}
*/
	private LdvTime getLdvTimeInstance(final Date date, final boolean lenient) {
		return new LdvTime(date) ;
		
		if (date instanceof DateTime) 
		{
      final DateTime dateTime = (DateTime) date ;
      if (dateTime.getTimeZone() != null) {
          instance = Calendar.getInstance(dateTime.getTimeZone());
      }
      else if (dateTime.isUtc()) {
          instance = Calendar.getInstance(TimeZones.getUtcTimeZone());
      }
      else {
      	// a date-time without a timezone but not UTC is floating
          instance = Calendar.getInstance();
      }
  }
	}

    /**
     * @param stream
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(final java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        log = LoggerFactory.getLogger(CalendarRecur.class);
    }

	/**
	 * Instantiate a new datelist with the same type, timezone and utc settings
	 * as the origList.
	 *
	 * @param origList
	 * @return a new empty list.
	 */
	private static DateList getDateListInstance(final DateList origList)
	{
		final DateList list = new DateList(origList.getType()) ;
		if (origList.isUtc()) 
			list.setUtc(true) ;
		else
			list.setTimeZone(origList.getTimeZone()) ;
		return list ;
	}
	
	/**
   * Returns a new date instance of the specified type. If no type is specified a DateTime instance is returned.
   * @param date a seed Java date instance
   * @param type the type of date instance
   * @return an instance of <code>net.fortuna.ical4j.model.Date</code>
   */
  public static Date getInstance(final java.util.Date date, final Value type) 
  {
  	if (Value.DATE.equals(type)) 
  		return new Date(date) ;
      
  	return new DateTime(date) ;
  }
  
  /**
   * Forked from net.fortuna.ical4j.util.Dates
   * 
   * Returns the absolute week number for the year specified by the supplied date.
   * Note that a value of zero (0) is invalid for the weekNo parameter and an <code>IllegalArgumentException</code> will be thrown.
   * 
   * @param date a date instance representing a week of the year
   * @param weekNo a week number offset
   * 
   * @return the absolute week of the year for the specified offset
   */
  public static int getAbsWeekNo(final LdvTime date, final int weekNo) 
  {
  	if ((0 == weekNo) || (weekNo < -LdvTime.MAX_WEEKS_PER_YEAR) || (weekNo > LdvTime.MAX_WEEKS_PER_YEAR))
  		throw new IllegalArgumentException(MessageFormat.format("Invalid week number [{0}]", new Object[] {weekNo})) ;
      
  	if (weekNo > 0)
  		return weekNo ;
      
    final int year = date.get(LdvTime.YEAR) ;
    
    int iWeeksCount = LdvTime.is53WeeksYear(year) ? 53 : 52 ;
      
    return iWeeksCount + weekNo ;
  }
}
