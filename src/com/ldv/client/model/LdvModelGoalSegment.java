package com.ldv.client.model;

public class LdvModelGoalSegment extends LdvModelGenericBox
{
	public enum jalonType      { jalonNotype, jalonOpen, jalonClose } ;
	public enum jalonEventType { jalonNoEvent, jalonOpen, jalonOpenCycle, jalonCycle, jalonInBetween, jalonClose, jalonNow } ;
	public enum jalonLevel     { NoColor, RedBefore, YellowBefore, GreenBefore, Blue, GreenAfter, YellowAfter, RedAfter } ;
	
	protected String         _sRefGoal ;
	
	protected jalonType      _iJalonType ;
	protected jalonEventType _iJalonEventType ;
	
	protected jalonLevel     _iTimeLevel ;    // level for time dimension
	protected jalonLevel     _iValueLevel ;   // level for value dimension
	protected jalonLevel     _iLevel ;        // Synthesis
	
	protected String	       _sCode ;
	protected double         _dValue ;
	protected String	       _sValue ;
	protected String	       _sUnit ;
	protected String	       _sFormat ;
	
	public LdvModelGoalSegment()
	{
		init() ; 
	}
	
	void init()
	{
		initBox() ;
		
		_sRefGoal    = "" ;  
		
		_iJalonType      = jalonType.jalonNotype ;
		_iJalonEventType = jalonEventType.jalonNoEvent ; 
		
		_iTimeLevel  = jalonLevel.NoColor ; 
		_iValueLevel = jalonLevel.NoColor ; 
		_iLevel      = jalonLevel.NoColor ;
		
		_sCode       = "" ;
		_dValue      = 0 ;
		_sValue      = "" ;
		_sUnit       = "" ;
		_sFormat     = "" ;
	}
	
	public jalonLevel getPreviousColor(jalonLevel iColorLevel)
	{
		switch(iColorLevel)
		{
			case RedAfter     : return jalonLevel.YellowAfter ;
			case YellowAfter  : return jalonLevel.GreenAfter ;
			case GreenAfter   : return jalonLevel.Blue ;
			case Blue         : return jalonLevel.GreenBefore ;
			case GreenBefore  : return jalonLevel.YellowBefore ;
			case YellowBefore : return jalonLevel.RedBefore ;
			case RedBefore    : return jalonLevel.NoColor ;
			case NoColor      : return jalonLevel.NoColor ;
			default           : return jalonLevel.NoColor ;
		}
	}
	
	public jalonLevel getNextColor(jalonLevel iColorLevel)
	{
		switch(iColorLevel)
		{
			case RedAfter     : return jalonLevel.NoColor ;
			case YellowAfter  : return jalonLevel.RedAfter ;
			case GreenAfter   : return jalonLevel.YellowAfter ;
			case Blue         : return jalonLevel.GreenAfter ;
			case GreenBefore  : return jalonLevel.Blue ;
			case YellowBefore : return jalonLevel.GreenBefore ;
			case RedBefore    : return jalonLevel.YellowBefore ;
			case NoColor      : return jalonLevel.NoColor ;
			default           : return jalonLevel.NoColor ;
		}
	}

	public String getRefGoal()
  {
  	return _sRefGoal;
  }
	public void setRefGoal(String sRefGoal)
  {
  	_sRefGoal = sRefGoal;
  }

	public jalonType getJalonType()
  {
  	return _iJalonType;
  }
	public void setJalonType(jalonType iJalonType)
  {
  	_iJalonType = iJalonType;
  }

	public jalonEventType getJalonEventType()
  {
  	return _iJalonEventType;
  }
	public void setJalonEventType(jalonEventType iJalonEventType)
  {
  	_iJalonEventType = iJalonEventType;
  }

	public jalonLevel getTimeLevel()
  {
  	return _iTimeLevel;
  }
	public void setTimeLevel(jalonLevel iTimeLevel)
  {
  	_iTimeLevel = iTimeLevel;
  }

	public jalonLevel getValueLevel()
  {
  	return _iValueLevel;
  }
	public void setValueLevel(jalonLevel iValueLevel)
  {
  	_iValueLevel = iValueLevel;
  }

	public jalonLevel getLevel()
  {
  	return _iLevel;
  }
	public void setLevel(jalonLevel iLevel)
  {
  	_iLevel = iLevel;
  }

	public String getCode()
  {
  	return _sCode;
  }
	public void setCode(String sCode)
  {
  	_sCode = sCode;
  }

	public double getdValue()
  {
  	return _dValue;
  }
	public void setdValue(double dValue)
  {
  	_dValue = dValue;
  }

	public String getsValue()
  {
  	return _sValue;
  }
	public void setsValue(String sValue)
  {
  	_sValue = sValue;
  }

	public String getUnit()
  {
  	return _sUnit;
  }
	public void setUnit(String sUnit)
  {
  	_sUnit = sUnit;
  }

	public String getFormat()
  {
  	return _sFormat;
  }
	public void setFormat(String sFormat)
  {
  	_sFormat = sFormat;
  }	
}
