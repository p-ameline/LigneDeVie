package com.ldv.client.model;

public class LdvModelEvent extends LdvModelGenericBox
{
	protected String  _sID ;
	
	public LdvModelEvent()
	{
		super() ;
		init() ; 
	}
	
	void init()
	{
		initBox() ;
		
		_sID = "" ;
	}

	public String getID()
  {
  	return _sID ;
  }
	public void setID(String sID)
  {
  	_sID = sID ;
  }
}
