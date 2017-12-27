package com.ldv.server.database ;

/**
 * The Oids class represents a trait for an object
 * 
 */
public class Oids
{
	private String _sObjectId ;
	private int    _iTraitId ;
	private String _sValue ;
	
	//
	//
	public Oids() 
	{
		reset() ;
	}
		
	public Oids(final String sObjectId, int iTraitId, final String sValue) 
	{
		_sObjectId = sObjectId ;
		_iTraitId  = iTraitId ;
		_sValue    = sValue ;
	}
	
	public void initFromOids(final Oids model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_sObjectId = model._sObjectId ;
		_iTraitId  = model._iTraitId ;
		_sValue    = model._sValue ;
	}
			
	public void reset() 
	{
		_sObjectId = "" ;
		_iTraitId  = 0 ;
		_sValue    = "" ;
	}
			
	// getter and setter
	//
	public String getObjectId() {
		return _sObjectId ;
	}
	public void setObjectId(final String sObjectId) {
		_sObjectId = sObjectId ;
	}
	
	public int getTraitId() {
		return _iTraitId ;
	}
	public void setTraitId(int iTraitId) {
		_iTraitId = iTraitId ;
	}

	public String getValue() {
		return _sValue ;
	}
	public void setValue(final String sValue) {
		_sValue = sValue ;
	}
}
