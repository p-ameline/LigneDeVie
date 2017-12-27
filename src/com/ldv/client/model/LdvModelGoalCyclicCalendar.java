package com.ldv.client.model;

public class LdvModelGoalCyclicCalendar
{
	protected double _dMinAllowedStartInterval ; // minimal allowed start interval: from red to yellow
	protected String _sMinAllowedStartUnit ;
	
	protected double _dMinGoodStartInterval ;    // minimal good start interval: from yellow to green
	protected String _sMinGoodStartUnit ;
  
	protected double _dMinIdealStartInterval ;   // minimal ideal start interval: from green to blue
	protected String _sMinIdealStartUnit ;
	
	protected double _dMaxIdealStartInterval ;   // maximal ideal start interval: from blue to green
	protected String _sMaxIdealStartUnit ;
	
	protected double _dMaxGoodStartInterval ;    // maximal good start interval: from green to yellow
	protected String _sMaxGoodStartUnit ;
	
	protected double _dMaxAllowedStartInterval ; // maximal allowed start interval: from yellow to red
	protected String _sMaxAllowedStartUnit ;
	
	public LdvModelGoalCyclicCalendar()
	{
		init() ; 
	}
	
	void init()
	{
		_dMinAllowedStartInterval = -1 ;
		_dMinGoodStartInterval    = -1 ;
		_dMinIdealStartInterval   = -1 ;
		_dMaxIdealStartInterval   = -1 ;
		_dMaxGoodStartInterval    = -1 ;
		_dMaxAllowedStartInterval = -1 ;
		
		_sMinAllowedStartUnit = "" ;
		_sMinGoodStartUnit    = "" ;
	  _sMinIdealStartUnit   = "" ;
		_sMaxIdealStartUnit   = "" ;
		_sMaxGoodStartUnit    = "" ;
		_sMaxAllowedStartUnit = "" ;
	}
}
