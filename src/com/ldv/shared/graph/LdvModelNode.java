package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.graph.LdvGraphEnum.NODELINKDIRECTION;
import com.ldv.shared.graph.LdvGraphEnum.NODELINKTYPES;
import com.ldv.shared.model.LdvInt;

public class LdvModelNode implements IsSerializable, Comparable<LdvModelNode>
{	
	protected String _sTREE_ID ;   // Concatenates Person ID and Document ID
	protected String _sNODE_ID ;

	protected String _sPERSON_ID ;    // Only there in order to parse Tree ID just once
	protected String _sDOCUMENT_ID ;  // Idem
	
	protected String _sTYPE ;
	protected String _sLEXICON ;
	protected String _sCOMPLEMENT ;
	protected String _sCERTITUDE ;
	protected String _sPLURAL ;
	protected String _sUNIT ;
	protected String _sFREE_TEXT ;
	
	protected String _sARCHETYPE ;
	protected String _sNodeRight ; // Right for Node and document
	
	protected String _sCOORDINATES ;
	protected String _sVISIBLE ;
	protected String _sINTEREST ;
	
	protected int    _iID ;
	
	protected Vector<LdvModelLinkedNode> _aTemporaryLinks = new Vector<LdvModelLinkedNode>() ;

	static protected String BASE_LOCALISATION = "+0000000" ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvModelNode()
	{
		init() ; 
	}
	
	/**
	 * Basic lexicon constructor  
	 * 
	 * @param sLexicon Lexicon concept this Node will represent
	 * 
	 **/
	public LdvModelNode(final String sLexicon)
	{
		init() ;
		
		_sLEXICON = sLexicon ;
	}
	
	/**
	 * Constructor for a concept and its complement  
	 * 
	 * @param sLexicon Lexicon concept this Node will represent
	 * @param sComplement Complement
	 * 
	 **/
	public LdvModelNode(final String sLexicon, final String sComplement)
	{
		init() ;
		
		_sLEXICON    = sLexicon ;
		_sCOMPLEMENT = sComplement ;
	}
	
	/**
	 * Constructor for a concept with value and unit  
	 * 
	 * @param sLexicon Lexicon concept this Node will represent
	 * @param sComplement Complement
	 * 
	 **/
	public LdvModelNode(final String sLexicon, final String sValue, final String sUnit)
	{
		init() ;
		
		_sLEXICON    = sLexicon ;
		_sCOMPLEMENT = sValue ;
		_sUNIT       = sUnit ;
	}
	
	/**
	 * Constructor for a free text  
	 * 
	 * @param sLexicon Lexicon concept this Node will represent
	 * @param sText    Text
	 * 
	 **/
	public LdvModelNode(final String sLexicon, final String sText, final boolean bIsFreeTxt)
	{
		init() ;
		
		_sLEXICON   = sLexicon ;
		_sFREE_TEXT = sText ;
	}
	
	/**
	 * Full constructor  
	 * 
	 **/
	public LdvModelNode(final String sPersonId, final String sDocumentId, final String sNodeId, final String sType, 
			                final String sLexicon, final String sComplement, final String sCertitude, final String sPlural, 
			                final String sUnit, final String sFreeText, final String sLoc, final String sVisible, final String sInterest)
	{
		init() ;
		
		_sPERSON_ID    = sPersonId ;
		_sDOCUMENT_ID  = sDocumentId ;
		buildTreeID() ;
		
		_sNODE_ID      = sNodeId ;

		_sTYPE         = sType ;
		_sLEXICON      = sLexicon ;
		_sCOMPLEMENT   = sComplement ;
		_sCERTITUDE    = sCertitude ;
		_sPLURAL       = sPlural ;
		_sUNIT         = sUnit ;
		_sFREE_TEXT    = sFreeText ;
		
		_sCOORDINATES = sLoc ;
		_sVISIBLE      = sVisible ;
		_sINTEREST     = sInterest ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public LdvModelNode(final LdvModelNode sourceNode)
	{
		initFromNode(sourceNode) ;
	}
	
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sPERSON_ID    = "" ;
		_sDOCUMENT_ID  = "" ;
		_sTREE_ID      = "" ;
		_sNODE_ID      = "" ;

		_sTYPE         = "" ;
		_sLEXICON      = "" ;
		_sCOMPLEMENT   = "" ;
		_sCERTITUDE    = "" ;
		_sPLURAL       = "" ;
		_sUNIT         = "" ;
		_sFREE_TEXT    = "" ;
		
		_sARCHETYPE    = "" ;
		_sNodeRight    = "" ;
		
		_sCOORDINATES  = "" ;
		_sVISIBLE      = "" ;
		_sINTEREST     = "" ;
		
		_iID           = 0 ;
		
		_aTemporaryLinks.clear() ;
	}
	
	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 **/
	public void initFromNode(final LdvModelNode otherNode)
	{
		init() ;
		
		if (null == otherNode)
			return ;
		
		_sPERSON_ID    = otherNode._sPERSON_ID ;
		_sDOCUMENT_ID  = otherNode._sDOCUMENT_ID ;
		_sTREE_ID      = otherNode._sTREE_ID ;
		_sNODE_ID      = otherNode._sNODE_ID ;

		_sTYPE         = otherNode._sTYPE ;
		_sLEXICON      = otherNode._sLEXICON ;
		_sCOMPLEMENT   = otherNode._sCOMPLEMENT ;
		_sCERTITUDE    = otherNode._sCERTITUDE ;
		_sPLURAL       = otherNode._sPLURAL ;
		_sUNIT         = otherNode._sUNIT ;
		_sFREE_TEXT    = otherNode._sFREE_TEXT ;
		
		_sARCHETYPE    = otherNode._sARCHETYPE ;
		_sNodeRight    = otherNode._sNodeRight ;
		
		_sCOORDINATES = otherNode._sCOORDINATES ;
		_sVISIBLE      = otherNode._sVISIBLE ;
		_sINTEREST     = otherNode._sINTEREST ;
		
		_iID           = otherNode._iID ;
		
		initLinkArrayFromLinkArray(otherNode._aTemporaryLinks) ;
	}

	/**
	 * Sets all information from a BBMessage
	 * 
	 * @param message BBMessage to initialize from
	 * @return <code>void</code>
	 * 
	 **/
	public void initFromMessage(final BBMessage message)
	{
		init() ;
		
		if (null == message)
			return ;
		
		_sTREE_ID      = message.getTreeID() ;
		parseTreeID() ;
		
		_sNODE_ID      = message.getNodeID() ;

		_sTYPE         = message.getType() ;
		_sLEXICON      = message.getLexique() ;
		_sCOMPLEMENT   = message.getComplement() ;
		_sCERTITUDE    = message.getCertitude() ;
		_sPLURAL       = message.getPlural() ;
		_sUNIT         = message.getUnit() ;
		_sFREE_TEXT    = message.getFreeText() ;
		
		_sARCHETYPE    = message.getArchetype() ;
		_sNodeRight    = message.getRights() ;
		
		_sVISIBLE      = message.getVisible() ;
		_sINTEREST     = message.getInterest() ;
		
		initLinkArrayFromLinkArray(message._aTemporaryLinks) ;
	}
	
	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	protected void initLinkArrayFromLinkArray(final Vector<LdvModelLinkedNode> otherTemporaryLinks)
	{
		_aTemporaryLinks.clear() ;
		
		if ((null == otherTemporaryLinks) || otherTemporaryLinks.isEmpty()) 
			return ;
		
		for (Iterator<LdvModelLinkedNode> itr = otherTemporaryLinks.iterator() ; itr.hasNext() ; )
			_aTemporaryLinks.add(new LdvModelLinkedNode(itr.next())) ;
	}
	
	/**
	 * Get node's line number   
	 * 
	 **/
	public int getLine() { 
		return getIndice(0) ; 
	}
	
	/**
	 * Get node's column number   
	 * 
	 **/
  public int getCol()  { 
  	return getIndice(1) ; 
  }
  
  /**
	 * Get node's coordinate   
	 * 
	 * @param indice 0 for line, 1 for column
	 * 
	 **/
  private int getIndice(final int indice)
  {
  	if (_sCOORDINATES.equals(""))
      return 0 ;

    String sStringToConvert = "" ;

    // Starting by '+': format is "+LLLLCCC"
    //                             01234567
    if ('+' == _sCOORDINATES.charAt(0))
    {
      if      (0 == indice)
        sStringToConvert = _sCOORDINATES.substring(1, 5) ;
      else if (1 == indice)
        sStringToConvert = _sCOORDINATES.substring(5, 8) ;
    }
    // Not starting by '+': format is "LLCCzztt"
    //                                 01234567
    else
    {
      if ((indice >= 0) && (indice < 4))
        sStringToConvert = _sCOORDINATES.substring(2 * indice, 2 * indice + 2) ;
    }

    if (sStringToConvert.equals(""))
      return 0 ;

    int iValeur = 0 ;
    
    // base 62 = 26 + 26 + 10
    //
    for (int i = 0 ; i < sStringToConvert.length() ; i++)
    {
      iValeur = 62 * iValeur ;
      char caract = sStringToConvert.charAt(i) ;

      if      ((caract >= '0') && (caract <= '9'))
        iValeur += caract - '0' ;
      else if ((caract >= 'A') && (caract <= 'Z'))
        iValeur += caract + 10 - 'A' ;
      else if ((caract >= 'a') && (caract <= 'z'))
        iValeur += caract + 36 - 'a' ;
    }
    
    return iValeur ;
  }
  
  /**
	 * Set node's line number   
	 * 
	 **/
	public void setLine(final int iLine) { 
		setIndice(0, iLine) ; 
	}
	
	/**
	 * Set node's column number   
	 * 
	 **/
  public void setCol(final int iCol)  { 
  	setIndice(1, iCol) ; 
  }
  
  /**
   * Set node's line and column as (0, 0)
   */
  public void setAsRoot() {
  	_sCOORDINATES = BASE_LOCALISATION ;
  }
  
  /**
	 * Set node's coordinate   
	 * 
	 * @param indice 0 for line, 1 for column
	 * @param value Value to be set for this coordinate
	 * 
	 **/
  private void setIndice(final int iIndice, final int iValue)
  {
  	if ((iIndice < 0) || (iIndice > 3))
      return ;

    // reference format is "+LLLLCCC"
    //
    if (_sCOORDINATES.equals(""))
    	_sCOORDINATES = BASE_LOCALISATION ;
    //
    // Old format - need to adapt
    //
    else if ('+' != _sCOORDINATES.charAt(0))
    {
    	int iOtherValue = 0 ;
      if (0 == iIndice)
      	iOtherValue = getCol() ;
      else
      	iOtherValue = getLine() ;

      _sCOORDINATES = BASE_LOCALISATION ;

      if (0 == iIndice)
      	setCol(iOtherValue) ;
      else
      	setLine(iOtherValue) ;
    }

    int iSize = 4 ;
    if (1 == iIndice)
    	iSize = 3 ;

    LdvInt ldvInt = new LdvInt(iValue) ;
    String sValue = ldvInt.intToBaseString(iSize, 62) ;

    if (sValue.equals(""))
    	return ;

    if (0 == iIndice)
    	_sCOORDINATES = "+" + sValue + _sCOORDINATES.substring(5, 8) ;
    else if (1 == iIndice)
    	_sCOORDINATES = _sCOORDINATES.substring(0, 5) + sValue ;
  }
	
  public String getPersonId() {
  	return _sPERSON_ID ;
  }
  public void setPersonID(final String sID) 
  {
  	if ((null == sID) || "".equals(sID))
  	{
  		_sPERSON_ID = "" ;
  		buildTreeID() ;
  		return ;
  	}
  	
  	if (sID.length() != LdvGraphConfig.PERSON_ID_LEN)
  		return ;
  	
  	_sPERSON_ID = sID ;
  	
  	buildTreeID() ;
  }
  
  /**
   * Get the document Id (tree Id = person Id + document Id inside a person graph)
   */
  public String getDocumentId() {
  	return _sDOCUMENT_ID ;
  }
  /**
   * Set the document Id (tree Id = person Id + document Id inside a person graph)
   */
  public void setDocumentID(final String sID) 
  {
  	if ((null == sID) || "".equals(sID))
  	{
  		_sDOCUMENT_ID = "" ;
  		buildTreeID() ;
  		return ;
  	}
  	
  	if (sID.length() != LdvGraphConfig.DOCUMENT_ID_LEN)
  		return ;
  	
  	_sDOCUMENT_ID = sID ;
  	
  	buildTreeID() ;
  }
  
  /**
   * Get the tree Id (tree Id = person Id + document Id inside a person graph)
   */
	public String getTreeID() {
  	return _sTREE_ID ;
  }
	/**
   * Set the tree Id (tree Id = person Id + document Id inside a person graph)
   */
	public void setTreeID(final String sID) 
	{
		if ((null == sID) || "".equals(sID))
  	{
			_sTREE_ID = "" ;
			parseTreeID() ;
			return ;
  	}
		
		_sTREE_ID = sID ;
		
		parseTreeID() ;
  }
	
	/**
	 * Build the tree Id as Person Id + Document Id (no necessary to say that it only applies to trees inside a person graph)
	 */
	protected void buildTreeID() 
	{
		if (("".equals(_sPERSON_ID)) || ("".equals(_sDOCUMENT_ID)))
		{
			_sTREE_ID = "" ;
			return ;
		}
		
		_sTREE_ID = _sPERSON_ID + _sDOCUMENT_ID ;
  }
	
	/**
	 * Parse the Person Id and the Document Id from the tree Id (no necessary to say that it only applies to trees inside a person graph)
	 */
	protected void parseTreeID() 
	{
		_sPERSON_ID   = "" ;
		_sDOCUMENT_ID = "" ;
		
		if ("".equals(_sTREE_ID))
			return ;
		
		// If an object Id, it makes no sense to instantiate a person Id and a document Id
		//
		if (LdvGraphTools.isObjectId(_sTREE_ID))
			return ;
		
		if (_sTREE_ID.length() < LdvGraphConfig.PERSON_ID_LEN)
			return ;
		
		_sPERSON_ID = _sTREE_ID.substring(0, LdvGraphConfig.PERSON_ID_LEN) ;
		
		if (_sTREE_ID.length() < LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN)
			return ;
		
		_sDOCUMENT_ID = _sTREE_ID.substring(LdvGraphConfig.PERSON_ID_LEN, LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN) ;
	}
	public void setObjectID(final String sID) 
	{
		_sTREE_ID = "" ;
		parseTreeID() ;
		
		_sTREE_ID = sID ;
  }
	
	public String getNodeID() {
  	return _sNODE_ID ;
  }
	public void setNodeID(final String sID) {
		_sNODE_ID = sID ;
  }

	public String getNodeURI() {
  	return LdvModelGraph.getNodeURI(_sPERSON_ID, _sDOCUMENT_ID, _sNODE_ID) ;
  }
	
	public String getType() {
  	return _sTYPE ;
  }
	public void setType(final String sTYPE) {
  	_sTYPE = sTYPE ;
  }

	public String getLexicon() {
  	return _sLEXICON ;
  }
	public String getSemanticLexicon() {
  	return getSemanticCode(_sLEXICON) ;
  }
	public void setLexicon(final String sLEXICON) {
  	_sLEXICON = sLEXICON ;
  }

	public String getComplement() {
  	return _sCOMPLEMENT ;
  }
	public void setComplement(final String sCOMPLEMENT) {
  	_sCOMPLEMENT = sCOMPLEMENT ;
  }

	public String getCertitude() {
  	return _sCERTITUDE ;
  }
	public void setCertitude(final String sCERTITUDE) {
  	_sCERTITUDE = sCERTITUDE ;
  }

	public String getPlural() {
  	return _sPLURAL ;
  }
	public void setPlural(final String sPLURAL) {
  	_sPLURAL = sPLURAL ;
  }

	public String getUnit() {
  	return _sUNIT ;
  }
	public String getSemanticUnit() {
  	return getSemanticCode(_sUNIT) ;
  }
	public void setUnit(final String sUNIT) {
  	_sUNIT = sUNIT ;
  }

	public String getFreeText() {
  	return _sFREE_TEXT ;
  }
	public void setFreeText(final String sFREETEXT) {
  	_sFREE_TEXT = sFREETEXT ;
  }
	
	public String getArchetype() {
		return _sARCHETYPE ;
	}
	public void setArchetype(final String sARCHETYPE) {
		_sARCHETYPE = sARCHETYPE ;
	}
	
	public String getNodeRight() {
		return _sNodeRight ;
	}
	public void setNodeRight(final String sNodeRight) {
		_sNodeRight = sNodeRight ;
	}
	
	public String getCoordinates() {
  	return _sCOORDINATES ;
  }
	public void setCoordinates(final String sCOORDINATES) {
  	_sCOORDINATES = sCOORDINATES ;
  }

	public String getVisible() {
  	return _sVISIBLE ;
  }
	public void setVisible(final String sVISIBLE) {
  	_sVISIBLE = sVISIBLE ;
  }

	public String getInterest() {
  	return _sINTEREST ;
  }
	public void setInterest(final String sINTEREST) {
  	_sINTEREST = sINTEREST ;
  }
	
	public Integer getLocalID() {
		return _iID ;
	}
	public void setLocalID(Integer iID) {
		_iID = iID ;
	}
	
	public Vector<LdvModelLinkedNode> getTemporaryLinks() {
		return _aTemporaryLinks ;
	}
	public void setTemporaryLinks(Vector<LdvModelLinkedNode> aOther) 
	{
		_aTemporaryLinks.clear() ;
		
		if ((null == aOther) || aOther.isEmpty())
			return ;
		
		initLinkArrayFromLinkArray(aOther) ;
	}
	
	
	/**
	 * For all temporary nodes, replace the link to a "waiting node" by a link to its Id   
	 * 
	 * @return <code>true</code> if all went well, <code>false</code> if not  
	 * 
	 **/
	public boolean numberTemporaryNodes()
	{
		if (_aTemporaryLinks.isEmpty())
			return true ;
		
		if ("".equals(_sNODE_ID))
			return false ;
		
		for (Iterator<LdvModelLinkedNode> itr = _aTemporaryLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLinkedNode link = itr.next() ;
			
			// Is the link connected to the node or to the whole tree
			//
			String sNodeSource ;
	  	if (link.isLinkedWithDoc())
	    	sNodeSource = _sTREE_ID ;
	    else
	    	sNodeSource = getNodeURI() ;
	  	
	  	// If the other node has no Id yet
	  	//
	  	if ("".equals(link.getOtherNodeID()))
	    {
	  		LdvModelNode otherNode = link.getOtherTemporaryNode() ;
	    	if (null != otherNode)
	      {
	        boolean bFound = otherNode.updateLinksForNode(this, sNodeSource) ;

	        // If the link to this node was not found in other node, create it 
	        //
	        if (false == bFound)
	        {
	        	if (link.getLinkDirection() == NODELINKDIRECTION.dirFleche)
	        		otherNode.addTemporaryLink(sNodeSource, link.getLinkType(), NODELINKDIRECTION.dirEnvers, link.isLinkedWithDoc()) ;
	          else
	          	otherNode.addTemporaryLink(sNodeSource, link.getLinkType(), NODELINKDIRECTION.dirFleche, link.isLinkedWithDoc()) ;

	          bFound = true ;
	        }
	      }
	    }
		}
		
		return true ;
	}
	
	protected void addTemporaryLink(String sNode, NODELINKTYPES iLkType, NODELINKDIRECTION iLkDirection, boolean bLkWithDoc)
	{
		if (null == sNode)
			return ;
		
		_aTemporaryLinks.add(new LdvModelLinkedNode(sNode, iLkType, iLkDirection, bLkWithDoc)) ;
	}
	
	/**
	 * Replace a link to a "waiting node" by a link to its Id (when this Id is set)   
	 * 
	 * @param otherNode Node that must be replaced by its Id
	 * @param sId       Id to set instead of the "waiting node"
	 * 
	 * @return <code>true</code> if a link to this "waiting node" was found, <code>false</code> if not  
	 * 
	 **/
	protected boolean updateLinksForNode(final LdvModelNode otherNode, final String sId)
	{
		if (_aTemporaryLinks.isEmpty())
			return false ;
		
		boolean bNodeFound = false ;
		
		for (Iterator<LdvModelLinkedNode> itr = _aTemporaryLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLinkedNode link = itr.next() ;
			
			if (link.getOtherTemporaryNode() == otherNode)
      {
      	link.setOtherNodeID(sId) ;
        link.setOtherTemporaryNode(null) ;
        
        bNodeFound = true ;
      }
		}
		
		return bNodeFound ;
	}
	
	/**
	  * Get "category code" of a Lexique code (usually its first char)
	  * 
	  * @param sCode Lexique code to find category of
	  * @return Its category string
	  */
	public static String getLexiqueCodeCategory(final String sCode) 
	{
		if ((null == sCode) || "".equals(sCode))
			return "" ;
		return sCode.substring(0, 1) ;
	}
	
	/**
	  * Get "semantic code" of a Lexique code (usually its five first char)
	  * 
	  * @param sCode Lexique code to find semantic code of
	  * @return Its semantic code
	  */
	public static String getSemanticCode(final String sCode) 
	{
		if ((null == sCode) || sCode.equals(""))
			return "" ;
		
		int iCodeLength = sCode.length() ;
		
		if (startsWithPound(sCode) && (iCodeLength > LdvGraphConfig.FORMAT_SENS_LEN))
		{
			String sTwoAndThree = followsPound(sCode) ;
			if ((false == "SG".equals(sTwoAndThree)) && (false == "SP".equals(sTwoAndThree)))
				return sCode.substring(0, LdvGraphConfig.FORMAT_SENS_LEN) ;
		}
	 
		String sCategory = getLexiqueCodeCategory(sCode) ;
		
		if ((false == "$".equals(sCategory)) && (iCodeLength >= LdvGraphConfig.LEXI_LEN))
			return sCode.substring(0, LdvGraphConfig.LEXI_SENS_LEN) ;
		
		return sCode ;
	}
	
	/**
	  * Get the first term code from a "semantic code"
	  * 
	  * @param sSemanticCode Semantic code to find the first Lexicon code of
	  * @return The Lexicon code
	  */
	public static String getFirstTermCode(final String sSemanticCode) 
	{
		if ((null == sSemanticCode) || sSemanticCode.equals(""))
			return "" ;
		
		return sSemanticCode + "1" ;
	}
	
	/**
	  * Get "semantic label" of a Label
	  * 
	  * @param sLabel Label to find semantic code of
	  * @return Its semantic code
	  */
	public static String getSemanticLabel(final String sLabel) 
	{
		if ((null == sLabel) || "".equals(sLabel))
			return "" ;
		
		// Look for concepts separators
		//
		int positNode = sLabel.indexOf(LdvGraphConfig.nodeSeparationMARK) ;
	  if (-1 != positNode)
			return getSemanticLabelForSeparator(sLabel, LdvGraphConfig.nodeSeparationMARK) ;
		
		int positPath = sLabel.indexOf(LdvGraphConfig.pathSeparationMARK) ;
	  if (-1 != positPath)
			return getSemanticLabelForSeparator(sLabel, LdvGraphConfig.pathSeparationMARK) ;
	  
	  int positINod = sLabel.indexOf(LdvGraphConfig.intranodeSeparationMARK) ;
	  if (-1 != positINod)
			return getSemanticLabelForSeparator(sLabel, LdvGraphConfig.intranodeSeparationMARK) ;

		return getSemanticCode(sLabel) ;
	}		
	
	protected static String getSemanticLabelForSeparator(final String sLabel, final String sSeparator)
	{
		if ((null == sLabel) || "".equals(sLabel))
			return "" ;
		
		int iPreviousPosit = 0 ;
		int iPosit         = sLabel.indexOf(sSeparator) ;
		
		if (-1 == iPosit)
			return sLabel ;
		
		String sReturnedLabel = "" ;
		
		while (-1 != iPosit)
		{
			String sProcessPart = sLabel.substring(iPreviousPosit, iPosit) ;
			
			if (false == "".equals(sReturnedLabel))
				sReturnedLabel += sSeparator ;
			sReturnedLabel += getSemanticLabel(sProcessPart) ; 
			
			iPreviousPosit = iPosit + sSeparator.length() ;
			
			iPosit = sLabel.indexOf(sSeparator, iPreviousPosit) ;
		}
		
		String sProcessPart = sLabel.substring(iPreviousPosit, sLabel.length()) ;
		if (false == "".equals(sReturnedLabel))
			sReturnedLabel += sSeparator ;
		sReturnedLabel += getSemanticLabel(sProcessPart) ;
		
		return sReturnedLabel ;
	}
	
	/**
	  * Determine whether two nodes contain the same information, regardless of IDs, coordinates and visibility status
	  * 
	  * @return true if all contained data are the same, false if not
	  * @param node LdvModelNode to compare to
	  * 
	  */
	public boolean hasSameContent(final LdvModelNode node)
	{
		if (this == node)
			return true ;

		if (null == node) 
			return false ;
		
		return (_sTYPE.equals(node._sTYPE) &&
		        _sLEXICON.equals(node._sLEXICON) &&
		        _sCOMPLEMENT.equals(node._sCOMPLEMENT) &&
		        _sCERTITUDE.equals(node._sCERTITUDE) &&
		        _sPLURAL.equals(node._sPLURAL) &&
		        _sUNIT.equals(node._sUNIT) &&
		        _sFREE_TEXT.equals(node._sFREE_TEXT) &&
		        _sARCHETYPE.equals(node._sARCHETYPE) &&
		        _sINTEREST.equals(node._sINTEREST)) ;
	}
	
	/**
	  * Determine whether two nodes have the same content and the same visibility status
	  * 
	  * @return <code>true</code> if Ok, <code>false</code> if not
	  * @param node LdvModelNode to compare to
	  * 
	  */
	public boolean equals(final LdvModelNode node)
	{
		if (this == node) 
			return true ;
	
		if (null == node) 
			return false ;
		
		if (false == hasSameContent(node))
			return false ;
		
		return (_sVISIBLE.equals(node._sVISIBLE)) ;
	}
	
	/**
	  * Determine whether two nodes are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelNode to compare to
	  * 
	  */
	public boolean isSameNode(final LdvModelNode node)
	{
		if (this == node)
			return true ;
		
		if (null == node)
			return false ;
		
		if (false == equals(node))
			return false ;
		
		return (_sTREE_ID.equals(node._sTREE_ID)) ;
	}
   
	/**
	  * Determine whether two nodes are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelNode to compare to
	  * 
	  */
	public boolean equals(final Object o) 
	{
		if (this == o)
			return true ;
	
		if (null == o || getClass() != o.getClass()) 
			return false ;

		final LdvModelNode node = (LdvModelNode) o ;

		return (this.equals(node)) ;
	}
	
	/**
	  * Returns <code>true</code> if the node's Lexicon is '�?????'
	  * 
	  * @return <code>true</code> if node's Lexicon is '�?????' and <code>false</code> if not 
	  * 
	  */
	public boolean isFreeText() {
		return isFreeText(_sLEXICON) ;
	}
	
	/**
	  * Returns <code>true</code> if the node's Lexicon starts with a '�'
	  * 
	  * @return <code>true</code> if node's Lexicon starts with a '�' and <code>false</code> if not 
	  * 
	  */
	public boolean startsWithPound() {
		return startsWithPound(_sLEXICON) ;
	}
	
	/**
	  * Returns the 2 chars that follow the '�' char in the node's Lexicon (if it starts with a '�')
	  * 
	  * @return A String with 2 chars if Ok, <code>""</code> if not
	  * 
	  */
	public String followsPound() {
		return followsPound(_sLEXICON) ;
	}
	
	/**
	  * Returns the 2 chars that follow the '�' char for strings that start with a '�'
	  * 
	  * @return A String with 2 chars if Ok, <code>""</code> if not
	  * @param sLexicon String to get chars from
	  * 
	  */
	public static String followsPound(final String sLexicon) 
	{
		if ((null == sLexicon) || (sLexicon.length() < 3))
			return "" ;
		
		if (false == startsWithPound(sLexicon))
			return "" ;
		
		return sLexicon.substring(1, 3) ;
	}

	/**
	  * Returns <code>true</code> if the string starts with a '�'
	  * 
	  * @return <code>true</code> if string starts with a '�' and <code>false</code> if not 
	  * @param  sLexicon String to check
	  * 
	  */
	public static boolean startsWithPound(final String sLexicon)
	{
		if ((null == sLexicon) || "".equals(sLexicon))
			return false ;
		
		char cCategory = sLexicon.charAt(0) ;
		if (LdvGraphConfig.POUND_CHAR == cCategory)
			return true ;
		
		return false ;
	}
	
	/**
	  * Returns <code>true</code> if the string is '�?????' (pound only followed by ?)
	  * 
	  * @return <code>true</code> if string is '�?????' and <code>false</code> if not 
	  * 
	  */
	public static boolean isFreeText(final String sLexicon) 
	{
		if ((null == sLexicon) || "".equals(sLexicon))
			return false ;
		
		if (false == startsWithPound(sLexicon))
			return false ;
	
		for (int i = 1 ; i < sLexicon.length() ; i++)
			if ('?' != sLexicon.charAt(i))
				return false ;
				
		return true ;
	}

	@Override
	public int compareTo(LdvModelNode otherNode)
	{
		if (null == otherNode)
			return 1 ;
		
		return (this.getLine() - otherNode.getLine()) ;
	}
}
