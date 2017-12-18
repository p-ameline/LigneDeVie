package com.ldv.shared.archetype;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeDialogBox implements IsSerializable
{
	protected String _sLang ;
	protected String _sCaption ;
	
	protected String _sType ;
	protected String _sStyle ;
	protected int    _iStyle ;
	
	protected LdvArchetypeCoordinates _coordinates = new LdvArchetypeCoordinates() ; 

	protected String _sClass ;
  protected String _sFontSize ;
  protected String _sFontType ;
  
  protected Vector<LdvArchetypeControl> _aControls = new Vector<LdvArchetypeControl>() ;
  
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeDialogBox() {
		init() ; 
	}
	
	/**
	 * Usual constructor 
	 * 
	 **/
	public LdvArchetypeDialogBox(String sLang, String sCaption, String sType, String sStyle, String sCoords, String sClass, String sFontSize, String sFontType) 
	{
		init() ;
		
		_sCaption  = sCaption ;
		_sLang     = sLang ;
		_sType     = sType ;
		_sStyle    = sStyle ;

		_coordinates.setCoords(sCoords, " ") ;
		
		_sClass    = sClass ;
	  _sFontSize = sFontSize ;
	  _sFontType = sFontType ;
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeDialogBox(final LdvArchetypeDialogBox rv) {
		initFromArchetypeDialog(rv) ; 
	}
				
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sCaption  = "" ;
		_sLang     = "" ;
		_sType     = "" ;
		_sStyle    = "" ;
		_iStyle    = 0  ;
		
		_coordinates.init() ;
	  
	  _sClass    = "" ;
	  _sFontSize = "" ;
	  _sFontType = "" ;
	  
	  _aControls.clear() ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param other LdvArchetypeDialog to initialize from
	 * @return void
	 * 
	 **/
	public void initFromArchetypeDialog(final LdvArchetypeDialogBox other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sCaption  = other._sCaption ;
		_sLang     = other._sLang ;
		
		_sType     = other._sType ;
		_sStyle    = other._sStyle ;
		_iStyle    = other._iStyle  ;
		
		_coordinates.initFromArchetypeCoordinates(other.getCoordinates()) ;
		
	  _sClass    = other._sClass ;
	  _sFontSize = other._sFontSize ;
	  _sFontType = other._sFontType ;
	  
	  if ((null == other._aControls) || other._aControls.isEmpty())
	  	return ;
	  
	  for (Iterator<LdvArchetypeControl> itr = other._aControls.iterator() ; itr.hasNext() ; )
	  	_aControls.addElement(new LdvArchetypeControl(itr.next())) ;
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

	public LdvArchetypeCoordinates getCoordinates() {
		return _coordinates ;
	}
	
	public String getCoords() {
		return _coordinates.getCoords() ;
	}
	public void setCoords(String sCoords) {
		_coordinates.setCoords(sCoords, " ") ;
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

	public String getDialogClass() {
		return _sClass ;
	}
	public void setDialogClass(String sClass) {
		_sClass = sClass ;
	}

	public String getFontSize() {
		return _sFontSize ;
	}
	public void setFontSize(String sFontSize) {
		_sFontSize = sFontSize ;
	}

	public String getFontType() {
		return _sFontType ;
	}
	public void setFontType(String sFontType) {
		_sFontType = sFontType ;
	}

	/**
	  * Is there no control in this Archetype?
	  * 
	  * @return <code>true</code> if the array of controls is <code>null</code> or empty, <code>false</code> if not
	  * 
	  */
	public boolean isEmpty() 
	{
		if ((null == _aControls) || _aControls.isEmpty())
			return true ;
		
		return false ;
	}
	
	/**
	  * Get the first LdvArchetypeControl from the list
	  * 
	  * @return a LdvArchetypeControl is the list is not empty, <code>null</code> if it is
	  * 
	  */
	public LdvArchetypeControl getFirstControl() 
	{
		if ((null == _aControls) || _aControls.isEmpty())
			return (LdvArchetypeControl) null ;
		
		Iterator<LdvArchetypeControl> itr = _aControls.iterator() ;
		
  	return itr.next() ;
	}
	
	/**
	  * Get the LdvArchetypeControl that is next to otherControl in the list
	  * 
	  * @return a LdvArchetypeControl is it exists, <code>null</code> if not
	  */
	public LdvArchetypeControl getNextControl(LdvArchetypeControl otherControl) 
	{
		if ((null == _aControls) || _aControls.isEmpty())
			return (LdvArchetypeControl) null ;
		
		Iterator<LdvArchetypeControl> itr = _aControls.iterator() ;
		for ( ; itr.hasNext() ; )
			if (otherControl.equals(itr.next()))
				break ;
		
		if (itr.hasNext())
			return itr.next() ;
		
		return (LdvArchetypeControl) null ;
	}
	
	/**
	  * Get the count of otherControls in the list
	  * 
	  * @return vector size
	  */
  public int getNbControl() {
  	return _aControls.size() ;
  }
	
	/**
	  * Determine whether two LdvArchetypeDialogBox objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeDialogBox to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeDialogBox other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sCaption,  other._sCaption)  &&
				    MiscellanousFcts.areIdenticalStrings(_sLang,     other._sLang)     &&
				    MiscellanousFcts.areIdenticalStrings(_sType,     other._sType)     &&
				    MiscellanousFcts.areIdenticalStrings(_sStyle,    other._sStyle)    &&
				    MiscellanousFcts.areIdenticalStrings(_sClass,    other._sClass)    &&
				    MiscellanousFcts.areIdenticalStrings(_sFontSize, other._sFontSize) &&
				    MiscellanousFcts.areIdenticalStrings(_sFontType, other._sFontType) &&
				    (_iStyle == other._iStyle) &&
				    _coordinates.equals(other._coordinates) &&
				    containsSameControls(other._aControls)) ;
	}

	/**
	  * Determine whether another vector of LdvArchetypeControl contains the same controls<br>
	  * <br>
	  * Both vectors can be null or empty. If not, they must have the same size and
	  * every control in _aControls must be contained in the other vector 
	  * 
	  * @return true if all controls are the same, false if not
	  * @param otherVector Vector<LdvArchetypeControl> to compare to
	  * 
	  */
	protected boolean containsSameControls(Vector<LdvArchetypeControl> otherVector)
	{
		if ((null == _aControls) || _aControls.isEmpty())
		{
			if ((null == otherVector) || otherVector.isEmpty())
				return true ;
			return false ;
		}
		
		if ((null == otherVector) || otherVector.isEmpty())
			return false ;
	  	
		if (otherVector.size() != _aControls.size())
			return false ;
		
		for (Iterator<LdvArchetypeControl> itr = _aControls.iterator() ; itr.hasNext() ; )
	  	if (false == otherVector.contains(itr.next()))
	  		return false ;
		
		return true ;
	}

	/**
	  * Determine whether two LdvArchetypeDialogBox objects are exactly similar
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

		final LdvArchetypeDialogBox dlg = (LdvArchetypeDialogBox) o ;

		return (this.equals(dlg)) ;
	}
}
