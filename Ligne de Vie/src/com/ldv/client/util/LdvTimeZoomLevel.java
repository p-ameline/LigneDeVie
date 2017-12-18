package com.ldv.client.util;

public class LdvTimeZoomLevel {

	public enum pixUnit { pixYear, pixMonth, pixWeek, pixDay, pixHour, pixMinute, pixSecond, pixUndefined } ;
	
	private pixUnit _iPixUnit ;
	private int     _iNumberOfUnitPerPixel ;
	private int     _iUpperLimitForUnitPerPixel ;
	private int     _iLowerLimitForUnitPerPixel ;
	
	/**
	 * Default constructor
	 */
	public LdvTimeZoomLevel() {
		reset() ;
	}
	
	/**
	 * Plain vanilla constructor
	 */
	public LdvTimeZoomLevel(pixUnit iPixUnit, int iNbUPP, int iLowerUPP, int iUpperUPP) 
	{
		_iPixUnit                   = iPixUnit ;
		_iNumberOfUnitPerPixel      = iNbUPP ;
		_iUpperLimitForUnitPerPixel = iLowerUPP ;
		_iLowerLimitForUnitPerPixel = iUpperUPP ;
	}

	/**
	 * Copy constructor
	 */
	public LdvTimeZoomLevel(final LdvTimeZoomLevel other) {
		initFromLdvTimeZoomLevel(other) ;
	}

	/**
	 * Initialize from a model
	 */
	public void initFromLdvTimeZoomLevel(final LdvTimeZoomLevel other) 
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_iPixUnit                   = other._iPixUnit ;
		_iNumberOfUnitPerPixel      = other._iNumberOfUnitPerPixel ;
		_iUpperLimitForUnitPerPixel = other._iUpperLimitForUnitPerPixel ;
		_iLowerLimitForUnitPerPixel = other._iLowerLimitForUnitPerPixel ;
	}

	/**
	 * Set all variables to default values
	 */
	public void reset()
	{
		_iPixUnit                   = pixUnit.pixUndefined ;
		_iNumberOfUnitPerPixel      = 0 ;
		_iUpperLimitForUnitPerPixel = 0 ;
		_iLowerLimitForUnitPerPixel = 0 ;
	}
	
	public pixUnit getPixUnit()    { return _iPixUnit ; }
	public int     getUppRate()    { return _iNumberOfUnitPerPixel ; }
	public int     getUpperLimit() { return _iUpperLimitForUnitPerPixel ; }
	public int     getLowerLimit() { return _iLowerLimitForUnitPerPixel ; }

	public void setPixUnit(pixUnit iPU) { _iPixUnit = iPU ; }
	public void setUppRate(int iUR)     { _iNumberOfUnitPerPixel = iUR ; }
	public void setUpperLimit(int iUL)  { _iUpperLimitForUnitPerPixel = iUL ; }
	public void setLowerLimit(int iLL)  { _iLowerLimitForUnitPerPixel = iLL ; }
}
