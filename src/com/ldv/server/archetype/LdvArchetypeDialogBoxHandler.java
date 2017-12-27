package com.ldv.server.archetype;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ldv.shared.archetype.LdvArchetypeControl;
import com.ldv.shared.archetype.LdvArchetypeDialogBox;

public class LdvArchetypeDialogBoxHandler extends LdvArchetypeDialogBox
{
	public static String LABEL_DIALOGBOX             = "dialogbox" ;
	
	public static String ATTRIBUT_DIALOGBOX_LANG     = "lang" ;
	public static String ATTRIBUT_DIALOGBOX_TYPE     = "type" ;
	public static String ATTRIBUT_DIALOGBOX_COORDS   = "coords" ;
	public static String ATTRIBUT_DIALOGBOX_STYLE    = "style" ;
	public static String ATTRIBUT_DIALOGBOX_CLASS    = "class" ;
	public static String ATTRIBUT_DIALOGBOX_CAPTION  = "caption" ;
	public static String ATTRIBUT_DIALOGBOX_FONTSIZE = "fontsize" ;
	public static String ATTRIBUT_DIALOGBOX_FONTTYPE = "fonttype" ;
	public static String ATTRIBUT_DIALOGBOX_NSTITRE  = "nstitre" ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeDialogBoxHandler() {
		init() ; 
	}
		
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeDialogBoxHandler(final LdvArchetypeDialogBoxHandler rv) {
		initFromArchetypeDialog(rv) ; 
	}
				
	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param other LdvArchetypeDialog to initialize from
	 * @return void
	 * 
	 **/
	public void initFromArchetypeDialogHandler(final LdvArchetypeDialogBoxHandler other) {
		super.initFromArchetypeDialog((LdvArchetypeDialogBox) other) ;
	}
	
	/**
	  * Initialize from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "dialogbox" tag
	  * 
	  */
	public boolean initFromElement(Element rootElement)
	{
		init() ;
		
		if (null == rootElement)
			return false ;
		
		// Check if we are parsing the proper element
		//
		String sTagName = rootElement.getTagName() ;		
		if (false == sTagName.equals(LABEL_DIALOGBOX))
			return false ;
		
		// Initialize internal data from node attributes
		//
		initDataFromElement(rootElement) ;
		
		if (false == rootElement.hasChildNodes())
			return true ;
		
		// Create controls from node's children
		//
		return getControlsFromElement(rootElement) ;
	}
	
	/**
	  * Initialize _aControls, the array of controls from a DOM element's sons
	  * 
	  * @param rootElement a DOM element representing the "dialogbox" tag
	  * 
	  */
	protected boolean getControlsFromElement(Element element)
	{
		NodeList listOfControls = element.getElementsByTagName(LdvArchetypeControlHandler.LABEL_DIALOGBOX_CONTROL) ;
		if (null == listOfControls)
			return true ;
		
    int iTotalElements = listOfControls.getLength() ;
    if (iTotalElements <= 0)
    	return false ;
    
    for (int i = 0 ; i < iTotalElements ; i++)
    {
    	Node controlNode = listOfControls.item(i) ;
    	if (null != controlNode)
    	{
    		LdvArchetypeControlHandler control = new LdvArchetypeControlHandler() ;
    		if (control.initFromElement((Element) controlNode))
    			_aControls.addElement((LdvArchetypeControl) control) ;
    	}
    }
    
    return true ;
	}
	
	/**
	  * Initialize archetype's information from a DOM element attributes
	  * 
	  * @param rootElement a DOM element representing the "dialogbox" tag
	  * 
	  */
	public void initDataFromElement(Element rootElement)
	{
		if (null == rootElement)
			return ;

		_sCaption  = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_CAPTION) ;
		_sLang     = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_LANG) ;
		_sType     = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_TYPE) ;
		_sStyle    = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_STYLE) ;
		
		_coordinates.setCoords(rootElement.getAttribute(ATTRIBUT_DIALOGBOX_COORDS), " ") ;
	  
	  _sClass    = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_CLASS) ;
	  _sFontSize = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_FONTSIZE) ;
	  _sFontType = rootElement.getAttribute(ATTRIBUT_DIALOGBOX_FONTTYPE) ;
	}
	
	/**
	  * Determine whether two LdvArchetypeDialogBoxHandler objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeDialogBoxHandler to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeDialogBoxHandler other) {
		return super.equals((LdvArchetypeDialogBox) other) ;
	}

	/**
	  * Determine whether two lexicon objects are exactly similar
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

		final LdvArchetypeDialogBoxHandler dlg = (LdvArchetypeDialogBoxHandler) o ;

		return (this.equals(dlg)) ;
	}
}
