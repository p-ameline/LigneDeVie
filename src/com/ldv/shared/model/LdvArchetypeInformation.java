package com.ldv.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeInformation implements IsSerializable
{
	enum ARCTYPE { undefined, archetype, referentiel, decisionTree } ;
	
	protected ARCTYPE _iType ;
	
	protected String _sIdentifier ;
	protected String _sLabel ;
	protected String _sFileName ;
	protected String _sObjectId ;

	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeInformation() {
		init() ;
	}

	/**
	 * Copy constructor  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public LdvArchetypeInformation(LdvArchetypeInformation sourceNode) {
		initFromArchetypeInformation(sourceNode) ;
	}
	
	/**
	 * Constructor from a tree  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public LdvArchetypeInformation(LdvModelTree tree) {
		initFromTree(tree) ;
	}
	
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_iType       = ARCTYPE.undefined ;
		_sIdentifier = "" ;
		_sLabel      = "" ;
		_sFileName   = "" ;
		_sObjectId   = "" ;
	}

	public ARCTYPE getType() {
		return _iType ;
	}
	public void setType(ARCTYPE iType) {
		_iType = iType ;
	}
	
	public ARCTYPE getTypeFromRootConcept(String sRootConcept) 
	{
		if (null == sRootConcept)
			return ARCTYPE.undefined ;
		
		if ("0ARCH".equals(sRootConcept))
			return ARCTYPE.archetype ;
		if ("0REFE".equals(sRootConcept))
			return ARCTYPE.referentiel ;
		if ("0ARDE".equals(sRootConcept))
			return ARCTYPE.decisionTree ;
		
		return ARCTYPE.undefined ;
	}
	
	public String getIdentifier() {
		return _sIdentifier ;
	}
	public void setIdentifier(String sIdentifier) {
		_sIdentifier = sIdentifier ;
	}
	
	public String getLabel() {
		return _sLabel ;
	}
	public void setLabel(String sLabel) {
		_sLabel = sLabel ;
	}
	
	public String getFileName() {
		return _sFileName ;
	}
	public void setFileName(String sFileName) {
		_sFileName = sFileName ;
	}
	
	public String getObjectId() {
		return _sObjectId ;
	}
	public void setObjectId(String sObjectId) {
		_sObjectId = sObjectId ;
	}
	
	/**
	 * Sets all information by copying other initFromArchetypeInformation content   
	 * 
	 * @param other Model LdvArchetypeControl
	 * @return void
	 * 
	 **/
	public void initFromArchetypeInformation(final LdvArchetypeInformation other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_iType       = other._iType ;
		_sIdentifier = other._sIdentifier ;
		_sLabel      = other._sFileName ;
		_sFileName   = other._sFileName ;
		_sObjectId   = other._sObjectId ;
	}
		
	public void initFromTree(LdvModelTree tree)
	{
		init() ;
		
		_sObjectId = tree.getTreeID() ;
		
		if ((null == tree) || tree.isEmpty())
			return ;
		
		LdvModelNode rootNode = tree.getRootNode() ;
		if (null == rootNode)
			return ;
		
		_iType = getTypeFromRootConcept(rootNode.getSemanticLexicon()) ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			String sConcept = sonNode.getSemanticLexicon() ;
			
			if      ("0ARID".equals(sConcept))
				_sIdentifier = tree.getFreeText(sonNode) ;
			else if ("LNOMA".equals(sConcept))
				_sLabel = tree.getFreeText(sonNode) ;
			else if ("0NFIC".equals(sConcept))
				_sFileName = tree.getFreeText(sonNode) ;
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
	}
		
	public String getConceptForType(ARCTYPE iType)
	{
		switch(iType)
		{
			case archetype    : return "0ARCH1" ;
			case referentiel  : return "0REFE1" ;
			case decisionTree : return "0ARDE1" ;
			default           : return "" ;
		}
	}
	
	public String getRootConceptForType(ARCTYPE iType)
	{
		switch(iType)
		{
			case archetype    : return "0ARCH" ;
			case referentiel  : return "0REFE" ;
			case decisionTree : return "0ARDE" ;
			default           : return "" ;
		}
	}
	
	/**
	  * Determine whether two LdvArchetypeInformation objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeInformation to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeInformation other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return ((_iType == other._iType) &&
				    MiscellanousFcts.areIdenticalStrings(_sIdentifier, other._sIdentifier) &&
				    MiscellanousFcts.areIdenticalStrings(_sLabel,      other._sLabel)      &&
						MiscellanousFcts.areIdenticalStrings(_sFileName,   other._sFileName)   &&
						MiscellanousFcts.areIdenticalStrings(_sObjectId,   other._sObjectId)) ;
	}
   
	/**
	  * Determine whether two LdvArchetypeInformation objects are exactly similar
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

		final LdvArchetypeInformation archetype = (LdvArchetypeInformation) o ;

		return (this.equals(archetype)) ;
	}
}
