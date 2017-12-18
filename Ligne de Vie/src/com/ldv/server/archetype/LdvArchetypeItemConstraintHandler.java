package com.ldv.server.archetype;

import org.w3c.dom.Element;

import com.ldv.shared.archetype.LdvArchetypeItemConstraint;

public class LdvArchetypeItemConstraintHandler extends LdvArchetypeItemConstraint
{
	public static String LABEL_CONTRAINTE     = "constraint" ;
	
	public static String ATTRIBUT_CONTR_TYPE  = "type" ;
	public static String ATTRIBUT_CONTR_LISTE = "list" ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeItemConstraintHandler() {
		init() ; 
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeItemConstraintHandler(final LdvArchetypeItemConstraintHandler rv) {
		initFromItemConstraint(rv) ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromItemConstraint(final LdvArchetypeItemConstraintHandler other) {
		super.initFromItemConstraint((LdvArchetypeItemConstraint) other) ;
	}

	/**
	  * Initialize from a DOM element  
	  * 
	  * @param rootElement a DOM element representing the "constraint" tag
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
		if (false == sTagName.equals(LABEL_CONTRAINTE))
			return false ;
		
		// Initialize internal data from node attributes
		//
		initDataFromElement(rootElement) ;
		
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

		_sType = rootElement.getAttribute(ATTRIBUT_CONTR_TYPE) ;
		_sList = rootElement.getAttribute(ATTRIBUT_CONTR_LISTE) ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItemConstraintHandler objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeItemConstraintHandler to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeItemConstraintHandler other) {
		return super.equals((LdvArchetypeItemConstraint) other) ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItemConstraintHandler objects are exactly similar
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

		final LdvArchetypeItemConstraintHandler node = (LdvArchetypeItemConstraintHandler) o ;

		return (this.equals(node)) ;
	}
}
