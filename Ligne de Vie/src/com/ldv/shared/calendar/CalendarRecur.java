package com.ldv.shared.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.model.LdvTime;

/**
 * Defines a recurrence.
 */
public class CalendarRecur implements IsSerializable
{
	/**
	 * Frequency resolution.
	 */
	public static final String SECONDLY = "SECONDLY" ;
	public static final String MINUTELY = "MINUTELY" ;
	public static final String HOURLY   = "HOURLY" ;
	public static final String DAILY    = "DAILY" ;
	public static final String WEEKLY   = "WEEKLY" ;
	public static final String MONTHLY  = "MONTHLY" ;
	public static final String YEARLY   = "YEARLY" ;

	private String              _sFrequency ;
	private LdvTime             _dUntil = new LdvTime(0) ;
	private int                 _iCount ;
	private int                 _iInterval ;
	
	private NumberList          _aSecondList   = new NumberList(0, 59, false) ;
	private NumberList          _aMinuteList   = new NumberList(0, 59, false) ;
	private NumberList          _aHourList     = new NumberList(0, 23, false) ;
	private NumberList          _aDayList      = new NumberList(1,  7, true) ;
	private NumberList          _aMonthDayList = new NumberList(1, 31, true) ;
	private NumberList          _aYearDayList  = new NumberList(1, 366, true) ;
	private NumberList          _aWeekNoList   = new NumberList(1, 53, true) ;
	private NumberList          _aMonthList    = new NumberList(1, 12, false) ;
	private NumberList          _aSetPosList   = new NumberList(1, 366, true) ;
	
	private Map<String, String> _mExperimentalValues = new HashMap<String, String>() ;
	
	/**
	 * Default constructor.
	 */
	public CalendarRecur() {
		initialize() ;
	}
	
	/**
	 * Copy constructor.
	 */
	public CalendarRecur(final CalendarRecur model) {
		initialize() ;
	}

	/**
	 * Initialize object's variables at construction time
	 */
	protected void initialize()
	{
		_sFrequency = "" ;
		_iCount     = -1 ;
		_iInterval  = -1 ;
	}

	/**
	 * Reset all object's variables.
	 */
	public void reset()
	{
		_sFrequency = "" ;
		_iCount     = -1 ;
		_iInterval  = -1 ;
		
		_dUntil.init() ;
		
		_aSecondList.clear() ;
		_aMinuteList.clear() ;
		_aHourList.clear() ;
		_aDayList.clear() ;
		_aMonthDayList.clear() ;
		_aYearDayList.clear() ;
		_aWeekNoList.clear() ;
		_aMonthList.clear() ;
		_aSetPosList.clear() ;
		
		_mExperimentalValues.clear() ;
	}
	
	/**
	 * Initialize object's variables.
	 */
	public void initializeFromCalendarRecur(final CalendarRecur model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_sFrequency = model._sFrequency ;
		_iCount     = model._iCount ;
		_iInterval  = model._iInterval ;
		
		_dUntil.initFromLdvTime(model.getUntil()) ;
		
		setSecondList(model._aSecondList) ;
		setMinuteList(model._aMinuteList) ;
		setHourList(model._aHourList) ;
		setDayList(model._aDayList) ;
		setMonthDayList(model._aMonthDayList) ;
		setYearDayList(model._aYearDayList) ;
		setWeekNoList(model._aWeekNoList) ;
		setMonthList(model._aMonthList) ;
		setSetPosList(model._aSetPosList) ;
		
		setExperimentalValues(model._mExperimentalValues) ;
	}
	
	public final NumberList getDayList() {
		return _aDayList ;
	}
	public void setDayList(NumberList aNumList) {
		_aDayList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getHourList() {
		return _aHourList ;
	}
	public void setHourList(NumberList aNumList) {
		_aHourList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getMinuteList() {
		return _aMinuteList ;
	}
	public void setMinuteList(NumberList aNumList) {
		_aMinuteList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getMonthDayList() {
		return _aMonthDayList ;
	}
	public void setMonthDayList(NumberList aNumList) {
		_aMonthDayList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getMonthList() {
		return _aMonthList ;
	}
	public void setMonthList(NumberList aNumList) {
		_aMonthList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getSecondList() {
		return _aSecondList ;
	}
	public void setSecondList(NumberList aNumList) {
		_aSecondList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getSetPosList() {
		return _aSetPosList ;
	}
	public void setSetPosList(NumberList aNumList) {
		_aSetPosList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getWeekNoList() {
		return _aWeekNoList ;
	}
	public void setWeekNoList(NumberList aNumList) {
		_aWeekNoList.InitializeFromNumberList(aNumList) ;
	}

	public final NumberList getYearDayList() {
		return _aYearDayList ;
	}
	public void setYearDayList(NumberList aNumList) {
		_aYearDayList.InitializeFromNumberList(aNumList) ;
	}

	/**
	 * @return Returns the experimentalValues.
	 */
	public final Map<String, String> getExperimentalValues() {
		return _mExperimentalValues ;
	}
	public void addExperimentalValue(final String sKey, final String sValue) {
		_mExperimentalValues.put(sKey, sValue) ;
	}
	public void setExperimentalValues(Map<String, String> aExExperimentalValues)
	{
		_mExperimentalValues.clear() ;
		
		if ((null == aExExperimentalValues) || aExExperimentalValues.isEmpty())
			return ;
		
		for (Map.Entry<String, String> entry : aExExperimentalValues.entrySet())
			_mExperimentalValues.put(entry.getKey(), entry.getValue()) ;
	}

	public final String getFrequency() {
		return _sFrequency ;
	}
	public final void setFrequency(final String sFrequency)
	{
		if (isValidFrequency(sFrequency))
			_sFrequency = sFrequency ;
	}

	/**
	 * @return Returns the interval or -1 if the rule does not have an interval defined.
	 */
	public final int getInterval() {
		return _iInterval ;
	}
	public final void setInterval(final int interval) {
    _iInterval = interval ;
	}
	
	/**
	 * @return Returns the count of a recurrent event or -1 if the rule does not have a count defined.
	 */
	public final int getCount() {
		return _iCount ;
	}
	public final void setCount(final int iCount)
	{
    _iCount = iCount ;
    
    if (_iCount > 0)
    	_dUntil.init() ;
	}

	public final LdvTime getUntil() {
		return _dUntil ;
	}
	public final void setUntil(final LdvTime until)
	{
		if ((null == until) || until.isEmpty())
		{
			_dUntil.init() ;
			return ;
		}
		
		_dUntil.initFromLdvTime(until) ;
		_iCount = -1 ;
	}
	public final void setUntil(final Date date)
	{
		if (null == date)
		{
			_dUntil.init() ;
			return ;
		}
		
		_dUntil.initFromJavaDate(date) ;
		_iCount = -1 ;
	}
	
	/**
	 * Check if a string contains a valid frequency
	 */
	protected boolean isValidFrequency(final String sFrequency) 
	{
    if (null == sFrequency)
    	return false ;
    	
    if ("".equals(sFrequency))
    	return true ;
    	
    if (SECONDLY.equals(sFrequency) || MINUTELY.equals(sFrequency) || HOURLY.equals(sFrequency)  || 
    	  DAILY.equals(sFrequency)    || WEEKLY.equals(sFrequency)   || MONTHLY.equals(sFrequency) ||
    	  YEARLY.equals(sFrequency))
    	return true ;
    	  
    return false ;
	}
}
