package com.ldv.client.model;

public class LdvModelGoalValues
{
	protected boolean _bMinAllowedValue ;
	protected double  _dMinAllowedValue ; // minimal allowed value: from red to yellow
	protected String  _sMinAllowedValueUnit ;
	
	protected boolean _bMinGoodValue ;
	protected double  _dMinGoodValue ;    // minimal good value: from yellow to green
	protected String  _sMinGoodValueUnit ;
  
	protected boolean _bMinIdealValue ;
	protected double  _dMinIdealValue ;   // minimal ideal value: from green to blue
	protected String  _sMinIdealValueUnit ;
	
	protected boolean _bMaxIdealValue ;
	protected double  _dMaxIdealValue ;   // maximal ideal value: from blue to green
	protected String  _sMaxIdealValueUnit ;
	
	protected boolean _bMaxGoodValue ;
	protected double  _dMaxGoodValue ;    // maximal good value: from green to yellow
	protected String  _sMaxGoodValueUnit ;
	
	protected boolean _bMaxAllowedValue ;
	protected double  _dMaxAllowedValue ; // maximal allowed value: from yellow to red
	protected String  _sMaxAllowedValueUnit ;

	public LdvModelGoalValues()
	{
		init() ; 
	}
	
	void init()
	{
		_bMinAllowedValue = false ;
		_bMinGoodValue    = false ;
		_bMinIdealValue   = false ;
		_bMaxIdealValue   = false ;
		_bMaxGoodValue    = false ;
		_bMaxAllowedValue = false ;
		
		_dMinAllowedValue = 0 ;
		_dMinGoodValue    = 0 ;
		_dMinIdealValue   = 0 ;
		_dMaxIdealValue   = 0 ;
		_dMaxGoodValue    = 0 ;
		_dMaxAllowedValue = 0 ;
		
		_sMinAllowedValueUnit = "" ;
	  _sMinGoodValueUnit    = "" ;
	  _sMinIdealValueUnit   = "" ;
		_sMaxIdealValueUnit   = "" ;
		_sMaxGoodValueUnit    = "" ;
		_sMaxAllowedValueUnit = "" ;
	}
}
