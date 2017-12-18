package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

public class LdvModelGoalEventInfo
{
	protected LdvTime _tDate ;   // Event date
	
	protected boolean _bValue ;  // Event value	
	protected double  _dValue ;       
  protected String  _sValue ;
  protected String  _sUnit ;
  
  public LdvModelGoalEventInfo()
	{
		init() ; 
	}
	
	void init()
	{
		_tDate = new LdvTime(0) ;
		
		_bValue = false ; 
		_dValue = 0 ;       
	  _sValue = "" ;
	  _sUnit  = "" ;
	}
	
	public LdvTime getDate()
  {
  	return _tDate ;
  }
	public void setDate(LdvTime tDate)
  {
  	_tDate.initFromLdvTime(tDate) ;
  }

	public boolean isValue()
  {
  	return _bValue ;
  }
	public void setbValue(boolean bValue)
  {
  	_bValue = bValue ;
  }

	public double getdValue()
  {
  	return _dValue ;
  }
	public void setdValue(double dValue)
  {
  	_dValue = dValue ;
  }

	public String getsValue()
  {
  	return _sValue ;
  }
	public void setsValue(String sValue)
  {
  	_sValue = sValue ;
  }

	public String getUnit()
  {
  	return _sUnit ;
  }
	public void setUnit(String sUnit)
  {
  	_sUnit = sUnit ;
  }
}
