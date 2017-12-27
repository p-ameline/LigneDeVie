package com.ldv.server.archetype;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ldv.shared.archetype.LdvArchetype;
import com.ldv.shared.archetype.LdvArchetypeDialogBox;
import com.ldv.shared.archetype.LdvArchetypeItem;

public class LdvArchetypeHandler extends LdvArchetype
{
	public static String LABEL_ARCHETYPE          = "archetype" ;
	public static String LABEL_ITEMS              = "items" ;
	public static String LABEL_PRESENTATION       = "presentation" ;
	
	public static String ATTRIBUT_ARCHETYPE_NAME  = "name" ;
	public static String ATTRIBUT_ARCHETYPE_FUNCT = "function" ;
	public static String ATTRIBUT_PRESENT_VALUE   = "value" ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeHandler() {
		init() ;
	}

	/**
	 * Copy constructor  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public LdvArchetypeHandler(LdvArchetypeHandler sourceNode) {
		initFromArchetype(sourceNode) ;
	}
		
	/**
	 * Sets all information by copying other LdvArchetypeControl content   
	 * 
	 * @param other Model LdvArchetypeControl
	 * @return void
	 * 
	 **/
	public void initFromArchetype(final LdvArchetypeHandler other)
	{
		super.initFromArchetype((LdvArchetype) other) ;		
	}
	
	/**
	  * Initialize this archetype from a DOM document  
	  * 
	  * @param document a DOM document representing the archetype XML content
	  * 
	  * @return <code>true</code> if everything went well, <code>false</code> if not
	  * 
	  */
	public boolean initFromDocument(final Document document)
	{
		init() ;
		
		if (null == document)
			return false ;
		
		NodeList listOfArchetypes = document.getElementsByTagName(LABEL_ARCHETYPE) ;
		if (null == listOfArchetypes)
			return false ;
		
		int iTotalElements = listOfArchetypes.getLength() ;
    if (iTotalElements <= 0)
    	return false ;
    
    Node firstArchetype = listOfArchetypes.item(0) ;
    Element element = (Element) firstArchetype ;
    
/*
		Element element = (Element) document ;
		String sTagName = element.getTagName() ;
		
		if (false == sTagName.equals(LABEL_ARCHETYPE))
			return false ;
*/
		
		initDataFromElement(element) ;
		
		// Get dialog boxes and items
		//
		boolean bDialogs = getDialogBoxesFromElement(element) ;
		boolean bItems   = getItemsFromElement(element) ;
		
		// Get the presentation String
		//
		boolean bPres    = getPresentationFromElement(element) ;
		
		return bDialogs && bItems && bPres ;
	}

	/**
	  * Initialize _sPresentation and _sFunction from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "archetype" tag
	  * 
	  */
	public void initDataFromElement(Element rootElement)
	{
		if (null == rootElement)
			return ;

		_sIdentifier = rootElement.getAttribute(ATTRIBUT_ARCHETYPE_NAME) ;
		_sFunction   = rootElement.getAttribute(ATTRIBUT_ARCHETYPE_FUNCT) ; 
	}
	
	/**
	  * Initialize _aDialogs, the array of DialogBoxes from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "archetype" tag
	  * 
	  */
	protected boolean getDialogBoxesFromElement(Element element)
	{
		NodeList listOfDialogs = element.getElementsByTagName(LdvArchetypeDialogBoxHandler.LABEL_DIALOGBOX) ;
		if (null == listOfDialogs)
			return false ;
		
    int iTotalElements = listOfDialogs.getLength() ;
    if (iTotalElements <= 0)
    	return false ;
    
    for (int i = 0 ; i < iTotalElements ; i++)
    {
    	Node dialogNode = listOfDialogs.item(i) ;
    	if (null != dialogNode)
    	{
    		LdvArchetypeDialogBoxHandler dialogBox = new LdvArchetypeDialogBoxHandler() ;
    		if (dialogBox.initFromElement((Element) dialogNode))
    			_aDialogs.addElement((LdvArchetypeDialogBox) dialogBox) ;
    	}
    }
    return true ;
	}
	
	/**
	  * Initialize _aItems, the array of items from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "archetype" tag
	  * 
	  */
	protected boolean getItemsFromElement(Element element)
	{
		// Get the "items" node
		//
		NodeList listOfItems = element.getElementsByTagName(LABEL_ITEMS) ;
		if (null == listOfItems)
			return false ;
		
		if (listOfItems.getLength() <= 0)
			return false ;
   
		Element itemsElement = (Element) listOfItems.item(0) ;
		
		// Get the list of "item" root tags
		//
		// NodeList listOfTrees = itemsElement.getElementsByTagName(LdvArchetypeItemHandler.LABEL_ITEM) ;
		NodeList listOfTrees = itemsElement.getChildNodes() ;
		if (null == listOfTrees)
			return false ;
		
    int iTotalElements = listOfTrees.getLength() ;
    if (iTotalElements <= 0)
    	return false ;
    
		for (int i = 0 ; i < iTotalElements ; i++)
		{
			Node itemNode = listOfTrees.item(i) ;
			if (null != itemNode)
			{
				String sTagName = itemNode.getNodeName() ;		
				if (sTagName.equals(LdvArchetypeItemHandler.LABEL_ITEM))
				{
					LdvArchetypeItemHandler item = new LdvArchetypeItemHandler() ;
					if (item.initFromElement((Element) itemNode))
						_aItems.addElement((LdvArchetypeItem) item) ;
				}
			}
		}
		return true ;
	}
	
	/**
	  * Initialize _aItems, the array of items from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "archetype" tag
	  * 
	  */
	protected boolean getPresentationFromElement(Element element)
	{
		// Get the "items" node
		//
		NodeList listOfItems = element.getElementsByTagName(LABEL_PRESENTATION) ;
		if (null == listOfItems)
			return true ;
		
		if (listOfItems.getLength() <= 0)
			return false ;
  
		Element presentationElement = (Element) listOfItems.item(0) ;
		
		_sPresentation = presentationElement.getAttribute(ATTRIBUT_PRESENT_VALUE) ;
		
		return true ;
	}
	
	/**
	  * Determine whether two LdvArchetypeHandler objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param archetype LdvArchetypeHandler to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeHandler archetype) {
		return super.equals((LdvArchetype) archetype) ;
	}
   
	/**
	  * Determine whether two LdvArchetypeHandler objects are exactly similar
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

		final LdvArchetypeHandler archetype = (LdvArchetypeHandler) o ;

		return (this.equals(archetype)) ;
	}
}
