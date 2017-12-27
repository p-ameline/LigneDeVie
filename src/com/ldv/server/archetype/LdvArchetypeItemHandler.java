package com.ldv.server.archetype;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ldv.shared.archetype.LdvArchetypeItem;
import com.ldv.shared.archetype.LdvArchetypeItemConstraint;

public class LdvArchetypeItemHandler extends LdvArchetypeItem
{
	public static String LABEL_ITEM               = "item" ;
	
	public static String ATTRIBUT_ITEM_CODE       = "code" ;
	public static String ATTRIBUT_ITEM_DECAL      = "decal" ;
	public static String ATTRIBUT_ITEM_CONTROL    = "control" ;
	public static String ATTRIBUT_ITEM_TEXT       = "text" ;
	public static String ATTRIBUT_ITEM_ARCHETYPE  = "archetype" ;
	public static String ATTRIBUT_ITEM_COMPLEMENT = "complement" ;
		
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeItemHandler() {
		init() ; 
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeItemHandler(final LdvArchetypeItemHandler rv) {
		initFromArchetypeItem(rv) ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromArchetypeItem(final LdvArchetypeItemHandler other) {
		super.initFromArchetypeItem((LdvArchetypeItem) other) ;
	}

	/**
	  * Initialize from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "item" tag
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
		if (false == sTagName.equals(LABEL_ITEM))
			return false ;
		
		// Initialize internal data from node attributes
		//
		initDataFromElement(rootElement) ;
		
		if (false == rootElement.hasChildNodes())
			return true ;
		
		// Create controls from node's children
		//
		boolean bItems = getSonItemsFromElement(rootElement) ;
		
		return bItems ; 
	}
	
	/**
	  * Initialize _aControls, the array of controls from a DOM element's sons
	  * 
	  * @param rootElement a DOM element representing the "item" tag
	  * 
	  */
	protected boolean getSonItemsFromElement(Element element)
	{
		NodeList listOfItems = element.getChildNodes() ;
		if (null == listOfItems)
			return true ;
		
		int iTotalElements = listOfItems.getLength() ;
		if (iTotalElements <= 0)
			return true ;
   
		for (int i = 0 ; i < iTotalElements ; i++)
		{
			Node itemNode = listOfItems.item(i) ;
			if (null != itemNode)
			{
				String sTagName = itemNode.getNodeName() ;
				
				if (sTagName.equals(LABEL_ITEM))
				{
					LdvArchetypeItemHandler item = new LdvArchetypeItemHandler() ;
					if (item.initFromElement((Element) itemNode))
						_aSubItems.addElement(item) ;
				}
				else if (sTagName.equals(LdvArchetypeItemConstraintHandler.LABEL_CONTRAINTE))
				{
					LdvArchetypeItemConstraintHandler constraint = new LdvArchetypeItemConstraintHandler() ;
					if (constraint.initFromElement((Element) itemNode))
						_aConstraints.addElement((LdvArchetypeItemConstraint) constraint) ;
				}
			}
		}	
   
   	return true ;
	}
			
	/**
	  * Initialize items's information from a DOM element attributes
	  * 
	  * @param rootElement a DOM element representing the "item" tag
	  * 
	  */
	public void initDataFromElement(Element rootElement)
	{
		if (null == rootElement)
			return ;

		_sCode       = rootElement.getAttribute(ATTRIBUT_ITEM_CODE) ;
		_sShiftLevel = rootElement.getAttribute(ATTRIBUT_ITEM_DECAL) ;
		_sText       = rootElement.getAttribute(ATTRIBUT_ITEM_TEXT) ;
		_sArchetype  = rootElement.getAttribute(ATTRIBUT_ITEM_ARCHETYPE) ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItemHandler objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeItemHandler to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeItemHandler other) {
		return super.equals((LdvArchetypeItem) other) ;
	}

	/**
	  * Determine whether two LdvArchetypeItemHandler objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param o LdvArchetypeHead to compare to
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

		final LdvArchetypeItemHandler node = (LdvArchetypeItemHandler) o ;

		return (this.equals(node)) ;
	}
}
