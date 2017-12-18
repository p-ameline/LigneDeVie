package com.ldv.shared.model;

public class LdvDate
{
	protected String _sDate ;
	protected String _sFormat ;
	
	protected String _sUnit ;
	protected String _sSemanticUnit ;

	public LdvDate()
	{
		reset() ;
	}
	
	public LdvDate(String sDate, String sFormat, String sUnit, String sSemanticUnit)
	{
		_sDate         = sDate ;
		_sFormat       = sFormat ;
		_sUnit         = sUnit ;
		_sSemanticUnit = sSemanticUnit ;
	}
	
	/**
	 * Empty this object 
	 * 
	 **/
	public void reset() 
	{
		_sDate         = "" ;
		_sFormat       = "" ;
		_sUnit         = "" ;
		_sSemanticUnit = "" ;
	}
	
	/**
	 * Return <code>true</code> if there is no date information 
	 * 
	 * @return <code>true</code> if empty, <code>false</code> if not
	 * 
	 **/
	public boolean isEmpty() {
		return "".equals(_sDate) ;
	}
	
	/**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(LdvDate src)
	{
		reset() ;
		
		if (null == src)
			return ;
		
		_sDate         = src._sDate ;
		_sFormat       = src._sFormat ;
		_sUnit         = src._sUnit ;
		_sSemanticUnit = src._sSemanticUnit ;
	}

	public String getDate() {
		return _sDate ;
	}
	public void setDate(String sDate) {
		_sDate = sDate ;
	}

	public String getFormat() {
		return _sFormat ;
	}
	public void setFormat(String sFormat) {
		_sFormat = sFormat ;
	}

	public String getUnit() {
		return _sUnit ;
	}
	public void setUnit(String sUnit) {
		_sUnit = sUnit ;
	}
	
	public String getSemanticUnit() {
		return _sSemanticUnit ;
	}
	public void setSemanticUnit(String sSemanticUnit) {
		_sSemanticUnit = sSemanticUnit ;
	}
}
