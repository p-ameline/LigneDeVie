package com.ldv.shared.archetype;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeControl implements IsSerializable
{
	protected String _sCaption ;
	protected String _sLang ;
	protected String _sType ;
	protected String _sCtrlId ;
	protected String _sStyle ;
	protected int    _iStyle ;
	protected int    _iTab ;
	
	protected LdvArchetypeCoordinates _coordinates = new LdvArchetypeCoordinates() ;
  
  protected String _sDataIdentity ;
  protected String _sDataFunction ;
  protected String _sDataExtension ;
  
  protected String _sFilling ;
  protected String _sHelpText ;

  /**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeControl() {
		init() ; 
	}
  
	/**
	 * Usual constructor 
	 * 
	 **/
	public LdvArchetypeControl(String sCaption, String sLang, String sType, String sStyle, String sCoords, String sDataIdentity, String sDataFunction, String sDataExtension, String sFilling, String sHelpText, String sTab) 
	{
		init() ;
		
		_sCaption       = sCaption ;
		_sLang          = sLang ;
		_sType          = sType ;
		_sStyle         = sStyle ;
		_iTab           = Integer.parseInt(sTab) ;
		
		_coordinates.setCoords(sCoords, ",") ;
	  
	  _sDataIdentity  = sDataIdentity ;
	  _sDataFunction  = sDataFunction ;
	  _sDataExtension = sDataExtension ;
	  
	  _sFilling       = sFilling ;
	  _sHelpText      = sHelpText ;
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeControl(final LdvArchetypeControl rv) {
		initFromArchetypeControl(rv) ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sCaption = "" ;
		_sLang    = "" ;
		_sType    = "" ;
		_sCtrlId  = "" ;
		_sStyle   = "" ;
		_iStyle   = 0  ;
		_iTab     = 0  ;

		_coordinates.init() ;
	  
	  _sDataIdentity  = "" ;
	  _sDataFunction  = "" ;
	  _sDataExtension = "" ;
	  
	  _sFilling       = "" ;
	  _sHelpText      = "" ;
	}

	/**
	 * Sets all information by copying other LdvArchetypeControl content   
	 * 
	 * @param other Model LdvArchetypeControl
	 * @return void
	 * 
	 **/
	public void initFromArchetypeControl(final LdvArchetypeControl other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sCaption       = other._sCaption ;
		_sLang          = other._sLang ;
		_sType          = other._sType ;
		_sCtrlId        = other._sCtrlId ;
		_sStyle         = other._sStyle ;
		_iStyle         = other._iStyle  ;
		_iTab           = other._iTab ;

		_coordinates.initFromArchetypeCoordinates(other._coordinates) ;
		
	  _sDataIdentity  = other._sDataIdentity ;
	  _sDataFunction  = other._sDataFunction ;
	  _sDataExtension = other._sDataExtension ;
	  
	  _sFilling       = other._sFilling ;
	  _sHelpText      = other._sHelpText ;
	}
		
	public String getCaption() {
		return _sCaption ;
	}
	public void setCaption(String sCaption) {
		_sCaption = sCaption ;
	}

	public String getLang() {
		return _sLang ;
	}
	public void setLang(String sLang) {
		_sLang = sLang ;
	}

	public String getType() {
		return _sType ;
	}
	public void setType(String sType) {
		_sType = sType ;
	}
	
	public String getCtrlId() {
		return _sCtrlId ;
	}
	public void setCtrlId(String sCtrlId) {
		_sCtrlId = sCtrlId ;
	}

	public String getStyleAsString() {
		return _sStyle ;
	}
	public void setStyleAsString(String sStyle) {
		_sStyle = sStyle ;
	}

	public int getStyle() {
		return _iStyle ;
	}
	public void setStyle(int iStyle) {
		_iStyle = iStyle ;
	}
	
	public int getTab() {
		return _iTab ;
	}
	public void setTab(int iTab) {
		_iTab = iTab ;
	}

	public LdvArchetypeCoordinates getCoordinates() {
		return _coordinates ;
	}
	
	public String getCoords() {
		return _coordinates.getCoords() ;
	}
	public void setCoords(String sCoords) {
		_coordinates.setCoords(sCoords, ",") ;
	}

	public int getX() {
		return _coordinates.getX() ;
	}
	public void setX(int iX) {
		_coordinates.setX(iX) ;
	}

	public int getY() {
		return _coordinates.getY() ;
	}
	public void setY(int iY) {
		_coordinates.setY(iY) ;
	}

	public int getW() {
		return _coordinates.getW() ;
	}
	public void setW(int iW) {
		_coordinates.setW(iW) ;
	}

	public int getH() {
		return _coordinates.getH() ;
	}
	public void setH(int iH) {
		_coordinates.setH(iH) ;
	}

	public String getDataIdentity() {
		return _sDataIdentity ;
	}
	public void setDataIdentity(String sDataIdentity) {
		_sDataIdentity = sDataIdentity ;
	}

	public String getDataFunction() {
		return _sDataFunction ;
	}
	public void setDataFunction(String sDataFunction) {
		_sDataFunction = sDataFunction ;
	}

	public String getDataExtension() {
		return _sDataExtension ;
	}
	public void setDataExtension(String sDataExtension) {
		_sDataExtension = sDataExtension ;
	}

	public String getFilling() {
		return _sFilling ;
	}
	public void setFilling(String sFilling) {
		_sFilling = sFilling ;
	}

	public String getHelpText() {
		return _sHelpText ;
	}
	public void setHelpText(String sHelpText) {
		_sHelpText = sHelpText ;
	}

	/**
	  * Determine whether two LdvArchetypeControl objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeControl to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeControl other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sCaption, other._sCaption)             &&
				    MiscellanousFcts.areIdenticalStrings(_sLang,    other._sLang)                &&
				    MiscellanousFcts.areIdenticalStrings(_sType,    other._sType)                &&
				    MiscellanousFcts.areIdenticalStrings(_sStyle,   other._sStyle)               &&
				    MiscellanousFcts.areIdenticalStrings(_sDataIdentity,  other._sDataIdentity)  &&
				    MiscellanousFcts.areIdenticalStrings(_sDataFunction,  other._sDataFunction)  &&
				    MiscellanousFcts.areIdenticalStrings(_sDataExtension, other._sDataExtension) &&
				    MiscellanousFcts.areIdenticalStrings(_sFilling,       other._sFilling)       &&
				    MiscellanousFcts.areIdenticalStrings(_sHelpText,      other._sHelpText)      &&
				    (_iStyle == other._iStyle) &&
				    (_iTab   == other._iTab)   &&
				    _coordinates.equals(other._coordinates)) ;
	}

	/**
	  * Determine whether two LdvArchetypeControl objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare to
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

		final LdvArchetypeControl node = (LdvArchetypeControl) o ;

		return (this.equals(node)) ;
	}
}
