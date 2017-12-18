package com.ldv.client;

import com.ldv.shared.model.LdvTime;

public class LdvTimeManager
{
	// 0 = pix/year
	// 1 = pix/month
	// 2 = pix/week
	// 3 = pix/day
	// 4 = pix/hour
	// 5 = pix/min
	// 6 = pix/sec
	 
	protected int _timeMetaZoomLevel ;
	protected int _timeLocalZoomLevel ;

	protected int     _iTimeZone ;   // seconds
	
	protected LdvTime _rightDate = new LdvTime(0) ;
	protected int     _rightPixel ;
	protected LdvTime _leftDate  = new LdvTime(0) ;
	
	public LdvTimeManager()
	{
		init() ; 
	}
	
	void init()
	{
		_timeMetaZoomLevel  = 2 ;
		_timeLocalZoomLevel = 1 ;
		_rightPixel = 0 ;
	}
	
	public int getWidthInPixel() { 
		return getPixelFromDate(_rightDate) ;
	}
	
	public int getPixelFromDate(LdvTime ldvDate)
	{
		LdvTime theDate = new LdvTime(0) ;
		if (ldvDate.isNoLimit())
			theDate.initFromLdvTime(_rightDate) ;
		else
			theDate.initFromLdvTime(ldvDate) ;

		//
		// A pixel is supposed to be a year/month/day... wide : it means that, in pixel/year mode, if rightDate 
		// is any date in 2007 then any other date in 2007 belongs to the same pixel and any date in 2008 returns 1
		//
		int iYearsDiff = theDate.getUTCFullYear() - _leftDate.getUTCFullYear() ;

		// pixel/year
		//
		if (0 == _timeMetaZoomLevel) 
			return iYearsDiff ;

		int iMonthsDiff = 12 * iYearsDiff + theDate.getUTCMonth() - _leftDate.getUTCMonth() ;

		// pixel/month
		//
		if (1 == _timeMetaZoomLevel) 
			return iMonthsDiff ;

		long lDateJD = theDate.getUTCJulianDay() ;
		long lLeftJD = _leftDate.getUTCJulianDay() ;
		
		long lDaysBetween = lDateJD - lLeftJD ;

		// pix/week
		//
		if (2 == _timeMetaZoomLevel)
		{
			// return (int) (java.lang.Math.floor(lDaysBetween / 7)) ;
			
			int iDateWeekIndex = (int) (java.lang.Math.floor(lDateJD / 7)) ; 
			int iLeftWeekIndex = (int) (java.lang.Math.floor(lLeftJD / 7)) ;
			
			return iDateWeekIndex - iLeftWeekIndex ;
		}
			
		// pix/day
		//
		if (3 == _timeMetaZoomLevel)
			return (int) lDaysBetween ;

		int iHoursBetween = (int) (24 * lDaysBetween + theDate.getUTCHours() - _leftDate.getUTCHours()) ;
 
		// pix/hour
		//
		if (4 == _timeMetaZoomLevel)
			return iHoursBetween;

		int iMinutesBetween = 60 * iHoursBetween + theDate.getUTCMinutes() - _leftDate.getUTCMinutes() ;

		// pix/minute
		//
		if (5 == _timeMetaZoomLevel)
			return iMinutesBetween;

		int iSecondsBetween = 60 * iMinutesBetween + theDate.getUTCSeconds() - _leftDate.getUTCSeconds() ;

		// pix/second
		//
		if (6 == _timeMetaZoomLevel)
			return iSecondsBetween ;

		return -1 ;
	}
	
	public int getTimeMetaZoomLevel() {
  	return _timeMetaZoomLevel ;
  }
	public void setTimeMetaZoomLevel(int timeMetaZoomLevel) {
  	this._timeMetaZoomLevel = timeMetaZoomLevel ;
  }

	public int getTimeLocalZoomLevel() {
  	return _timeLocalZoomLevel ;
  }
	public void setTimeLocalZoomLevel(int timeLocalZoomLevel) {
  	this._timeLocalZoomLevel = timeLocalZoomLevel ;
  }

	public LdvTime getRightDate() {
  	return _rightDate ;
  }
	public void setRightDate(LdvTime rightDate) {
  	_rightDate.initFromLdvTime(rightDate) ;
  }

	public int getRightPixel() {
  	return _rightPixel ;
  }
	public void setRightPixel(int rightPixel) {
  	_rightPixel = rightPixel ;
  }

	public LdvTime getLeftDate() {
  	return _leftDate ;
  }
	public void setLeftDate(LdvTime leftDate) {
  	_leftDate.initFromLdvTime(leftDate) ;
  }
}
