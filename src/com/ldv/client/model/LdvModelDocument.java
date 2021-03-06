package com.ldv.client.model;

public class LdvModelDocument extends LdvModelGenericBox
{
	protected String  _sID ;
	private String _sType ;
	private String _sLineID ;
	
	public LdvModelDocument()
	{
		super() ;
		init() ; 
	}
	
	void init()
	{
		initBox() ;	
		_sID    = "" ;
	}

	public String getID()
  {
  	return _sID ;
  }
	
	public void setID(String sID)
  {
  	_sID = sID ;
  }
	
	public String getType()
  {
  	return _sType ;
  }
	
	public void setType(String sType)
  {
  	_sType = sType ;
  }
	
	public String getLineID()
  {
  	return _sLineID ;
  }
	
	public void setLineID(String sLindID)
  {
		_sLineID = sLindID ;
  }

}
