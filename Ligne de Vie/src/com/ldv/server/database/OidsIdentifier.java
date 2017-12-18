package com.ldv.server.database ;

/**
 * The OidsIdentifier class represents a trait ID and its "short path" label
 * 
 */
public class OidsIdentifier
{
	private int    _iId ;
	private String _sName ;
	
	//
	//
	public OidsIdentifier() 
	{
		reset() ;
	}
		
	public OidsIdentifier(int iId, final String sName) 
	{
		_iId   = iId ;
		_sName = sName ;
	}
	
	public void initFromOidsIdentifier(OidsIdentifier model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_iId   = model._iId ;
		_sName = model._sName ;
	}
			
	public void reset() 
	{
		_iId   = 0 ;
		_sName = "" ;
	}
			
	// getter and setter
	//
	public int getId() {
		return _iId ;
	}
	public void setId(int iId) {
		_iId = iId ;
	}
	
	public String getName() {
		return _sName ;
	}
	public void setName(String sName) {
		_sName = sName ;
	}
}
