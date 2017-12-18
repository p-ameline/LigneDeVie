package com.ldv.server.database ;

/**
 * The OidsPath class represents the set of paths that justifies the creation of a trait
 * 
 */
public class OidsPath
{
	private int    _iId ;
	private String _sPath ;
	private int    _iTraitId ;
	
	private static String _sPathSeparator = "/" ;
	
	//
	//
	public OidsPath() 
	{
		reset() ;
	}
		
	public OidsPath(int iId, final String sPath, int iTraitId) 
	{
		_iId      = iId ;
		_sPath    = sPath ;
		_iTraitId = iTraitId ;
	}
	
	public void initFromOids(final OidsPath model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_iId      = model._iId ;
		_sPath    = model._sPath ;
		_iTraitId = model._iTraitId ;
	}
			
	public void reset() 
	{
		_iId      = 0 ;
		_sPath    = "" ;
		_iTraitId = 0 ;
	}
			
	// getter and setter
	//
	public int getId() {
		return _iId ;
	}
	public void setId(int iId) {
		_iId = iId ;
	}
	
	public String getPath() {
		return _sPath ;
	}
	public void setPath(final String sPath) {
		_sPath = sPath ;
	}

	public int getTraitId() {
		return _iTraitId ;
	}
	public void setTraitId(int iTraitId) {
		_iTraitId = iTraitId ;
	}
	
	public static String getPathSeparator() {
		return _sPathSeparator ;
	}
}
