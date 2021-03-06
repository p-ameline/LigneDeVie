package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

public class LdvModelConcernSeverityLevel
{
	protected int     _iSeverityLevel ;
	protected LdvTime _Date ;
	
	public LdvModelConcernSeverityLevel()
	{
		init() ; 
	}
	
	void init()
	{
		_iSeverityLevel = 0 ;
		_Date = new LdvTime(0) ;
	}
	
	public int getSeverityLevel()
  {
  	return _iSeverityLevel ;
  }
	public void setSeverityLevel(int iSeverityLevel)
  {
  	_iSeverityLevel = iSeverityLevel ;
  }
	public LdvTime getDate()
  {
  	return _Date ;
  }
	public void setDate(LdvTime date)
  {
  	_Date.initFromLdvTime(date) ;
  }
}
