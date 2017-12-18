package com.ldv.shared.archetype;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeCoordinates implements IsSerializable
{
  protected String _sCoords ;

  protected int    _iX ;
  protected int    _iY ;
  protected int    _iW ;
  protected int    _iH ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeCoordinates() {
		init() ; 
	}
	
	/**
	 * Usual constructor 
	 * 
	 **/
	public LdvArchetypeCoordinates(String sCoords, String sSeparator) 
	{
		init() ;
		
		_sCoords = sCoords ;
		initCoordinatesFromString(sSeparator) ;
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeCoordinates(final LdvArchetypeCoordinates rv) {
		initFromArchetypeCoordinates(rv) ; 
	}
				
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
	  _sCoords = "" ;
	  initCoordinates() ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void initCoordinates()
	{
	  _iX = 0 ;
	  _iY = 0 ;
	  _iW = 0 ;
	  _iH = 0 ;
	}
	
	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param other LdvArchetypeDialog to initialize from
	 * @return void
	 * 
	 **/
	public void initFromArchetypeCoordinates(final LdvArchetypeCoordinates other)
	{
		init() ;
		
		if (null == other)
			return ;
		
	  _sCoords   = other._sCoords ;
	  
	  _iX        = other._iX ;
	  _iY        = other._iY ;
	  _iW        = other._iW ;
	  _iH        = other._iH ;	  
	}

	public void initCoordinatesFromString(String sSeparator)
	{
		initCoordinates() ;
		
		if ("".equals(_sCoords))
			return ;
		
		int iBlanc = _sCoords.indexOf(sSeparator) ;
		if (-1 == iBlanc)
			return ;
		String sX = _sCoords.substring(0, iBlanc) ;
		
		_iX = Integer.parseInt(sX.trim()) ;
		
		int iDeb = iBlanc ;
		iBlanc = _sCoords.indexOf(sSeparator, iDeb + 1) ;
		if (-1 == iBlanc)
			return ;
		String sY = _sCoords.substring(iDeb + 1, iBlanc) ;
		_iY = Integer.parseInt(sY.trim()) ;
		
		iDeb = iBlanc ;
		iBlanc = _sCoords.indexOf(sSeparator, iDeb + 1) ;
		if (-1 == iBlanc)
			return ;
		String sW = _sCoords.substring(iDeb + 1, iBlanc) ;
		_iW = Integer.parseInt(sW.trim()) ;
		
		iDeb = iBlanc ;
		int iRemainingLen = _sCoords.length() - iDeb ;
		
		if (iRemainingLen > 0)
		{
			String sH = _sCoords.substring(iDeb + 1, _sCoords.length()) ;
			_iH = Integer.parseInt(sH.trim()) ;
		}
	}
	
	public String getCoords() {
		return _sCoords ;
	}
	public void setCoords(String sCoords, String sSeparator) 
	{
		_sCoords = sCoords ;
		initCoordinatesFromString(sSeparator) ;
	}

	public int getX() {
		return _iX ;
	}
	public void setX(int iX) {
		_iX = iX ;
	}

	public int getY() {
		return _iY ;
	}
	public void setY(int iY) {
		_iY = iY ;
	}

	public int getW() {
		return _iW ;
	}
	public void setW(int iW) {
		_iW = iW ;
	}

	public int getH() {
		return _iH ;
	}
	public void setH(int iH) {
		_iH = iH ;
	}
	
	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelLexique to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeCoordinates other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sCoords, other._sCoords)   &&
				    (_iX == other._iX)         &&
				    (_iY == other._iY)         &&
				    (_iW == other._iW)         &&
				    (_iH == other._iH)) ;
	}

	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelNode to compare to
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final LdvArchetypeCoordinates dlg = (LdvArchetypeCoordinates) o ;

		return (this.equals(dlg)) ;
	}
}
