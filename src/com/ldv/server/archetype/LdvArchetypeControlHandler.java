package com.ldv.server.archetype;

import org.w3c.dom.Element;

import com.ldv.shared.archetype.LdvArchetypeControl;

public class LdvArchetypeControlHandler extends LdvArchetypeControl
{
	public static String LABEL_DIALOGBOX_CONTROL     = "control" ;
	
	public static String ATTRIBUT_CONTROL_VALUE      = "value" ;
	public static String ATTRIBUT_CONTROL_CAPTION    = "caption" ;
	public static String ATTRIBUT_CONTROL_REFID      = "refid" ;
	public static String ATTRIBUT_CONTROL_TYPE       = "type" ;
	public static String ATTRIBUT_CONTROL_STYLE      = "style" ;
	public static String ATTRIBUT_CONTROL_COORDS     = "coords" ;
	public static String ATTRIBUT_CONTROL_ONGLET     = "tab" ;
	public static String ATTRIBUT_CONTROL_DATA       = "data" ;
	public static String ATTRIBUT_CONTROL_FILLING    = "filling" ;
	public static String ATTRIBUT_CONTROL_HELP_TEXT  = "helptext" ;
	
  /**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeControlHandler() {
		init() ; 
	}
  
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeControlHandler(final LdvArchetypeControlHandler rv) {
		initFromArchetypeControl(rv) ;
	}

	/**
	 * Sets all information by copying other LdvArchetypeControl content   
	 * 
	 * @param other Model LdvArchetypeControl
	 * @return void
	 * 
	 **/
	public void initFromArchetypeControlHandler(final LdvArchetypeControlHandler other) {
		super.initFromArchetypeControl((LdvArchetypeControl) other) ;
	}
	
	public boolean initFromElement(Element rootElement)
	{
		init() ;
		
		if (null == rootElement)
			return false ;
		
		// Check if we are parsing the proper element
		//
		String sTagName = rootElement.getTagName() ;		
		if (false == sTagName.equals(LABEL_DIALOGBOX_CONTROL))
			return false ;
		
		// Initialize internal data from node attributes
		//
		initDataFromElement(rootElement) ;
				
		return true ;
	}
	
	public void initDataFromElement(Element rootElement)
	{
		if (null == rootElement)
			return ;
		
		_sCaption = rootElement.getAttribute(ATTRIBUT_CONTROL_CAPTION) ;
		_sLang    = "" ;
		_sType    = rootElement.getAttribute(ATTRIBUT_CONTROL_TYPE) ;
		
		setStyleAsString(rootElement.getAttribute(ATTRIBUT_CONTROL_STYLE)) ;

		_coordinates.setCoords(rootElement.getAttribute(ATTRIBUT_CONTROL_COORDS), " ") ;
	  
		_sDataFunction  = "" ;
	  _sDataExtension = "" ;
	  
	  _sDataIdentity  = rootElement.getAttribute(ATTRIBUT_CONTROL_DATA) ;
	  if (false == "".equals(_sDataIdentity))
	  	initFunctionFromData() ;
	  
	  String sTab     = rootElement.getAttribute(ATTRIBUT_CONTROL_ONGLET) ;
	  if (false == "".equals(sTab))
	  	_iTab = Integer.parseInt(sTab) ;
	  
	  _sFilling       = rootElement.getAttribute(ATTRIBUT_CONTROL_FILLING) ;
	  _sHelpText      = rootElement.getAttribute(ATTRIBUT_CONTROL_HELP_TEXT) ;
	  
	  String sValue = rootElement.getAttribute(ATTRIBUT_CONTROL_VALUE) ;
		if (false == "".equals(sValue))
			initDataFromValue(sValue) ;
	}
	
	protected void initFunctionFromData()
	{
		if ((null == _sDataIdentity) || "".equals(_sDataIdentity))
			return ;
		
	  // Functions are separated from node's path with a ":"   
		//
	  int deb = _sDataIdentity.indexOf(":") ;
	  if (-1 == deb)
	    return ;

	  _sDataFunction = _sDataIdentity.substring(deb + 1, _sDataIdentity.length()).trim() ;
	  _sDataIdentity = _sDataIdentity.substring(0, deb).trim() ;
	}

	protected boolean initDataFromValue(String sValue)
	{
		if ((null == sValue) || "".equals(sValue))
			return false ;
		
	  // Parsing caption: caption is the first information, and is delimited by quotes 
		//
	  int deb = sValue.indexOf("\"") ;
	  if (-1 == deb)
	    return false ;
	  int fin = sValue.indexOf("\"", deb + 1) ;
	  if (-1 == fin)
	    return false ;

	  _sCaption = sValue.substring(deb + 1, fin).trim() ;

	  // Parsing RefId
	  //
	  deb = sValue.indexOf(",", fin + 1) ;
	  if (-1 == deb)
	    return false ;
	  fin = sValue.indexOf(",", deb + 1) ;
	  if (-1 == fin)
	    return false ;
	  
	  _sCtrlId = sValue.substring(deb + 1, fin).trim() ;
	  
	  // Parsing type
	  //
	  deb = sValue.indexOf("\"", fin + 1) ;
	  if (-1 == deb)
	    return false ;
	  fin = sValue.indexOf("\"", deb + 1) ;
	  if (-1 == fin)
	    return false ;
	  
	  _sType = sValue.substring(deb + 1, fin).trim() ;

	  // Parsing style
	  //
	  deb = sValue.indexOf(",", fin + 1) ;
	  if (-1 == deb)
	    return false ;
	  fin = sValue.indexOf(",", deb + 1) ;
	  if (-1 == fin)
	    return false ;
	  
	  setStyleAsString(sValue.substring(deb + 1, fin).trim()) ;

	  // Parsing coordinates
	  //
	  deb = fin ;
	  fin = sValue.length() ;
	  
	  setCoords(sValue.substring(deb + 1, fin).trim()) ;
	  
	  return true ;
	}
	
	/**
	  * Determine whether two LdvArchetypeControlHandler objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeControlHandler to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeControlHandler other) {
		return super.equals((LdvArchetypeControl) other) ;
	}

	/**
	  * Determine whether two LdvArchetypeControlHandler objects are exactly similar
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

		final LdvArchetypeControlHandler node = (LdvArchetypeControlHandler) o ;

		return (this.equals(node)) ;
	}
}
