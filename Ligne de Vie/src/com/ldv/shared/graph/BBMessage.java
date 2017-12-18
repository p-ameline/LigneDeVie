package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvModelLinkedNode;
import com.ldv.shared.graph.LdvModelNode;

public class BBMessage {

	protected String _sTreeID ;
	protected String _sNodeID ;
	protected String _sLexique ;
	protected String _sComplement ;	 
	protected String _sCertitude ;
	protected String _sUnit ;
	protected String _sInterest ;
	protected String _sPlural ;
	protected String _sVisible ;
	protected String _sType ;        
	protected String _sFreeText ;  
	protected String _sArchetype ; 
	protected String _sRights ;
	
	protected Vector<LdvModelLinkedNode> _aTemporaryLinks = new Vector<LdvModelLinkedNode>() ;

	public BBMessage() {
		init() ;
	}

	public BBMessage(final BBMessage src) {
		initFromModel(src) ;
	}

  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBMessage src)
	{
		init() ;
		
		if (null == src)
			return ;

		_sTreeID     = src._sTreeID ;
		_sNodeID     = src._sNodeID ;
		_sLexique    = src._sLexique ;
		_sComplement = src._sComplement ;
		_sCertitude  = src._sCertitude ;
		_sUnit       = src._sUnit ;
		_sInterest   = src._sInterest ;
		_sPlural     = src._sPlural ;
		_sVisible    = src._sVisible ;
		_sType       = src._sType ;
		_sFreeText   = src._sFreeText ;
		_sArchetype  = src._sArchetype ;
		_sRights     = src._sRights ;
		
		SetTemporaryLinks(src._aTemporaryLinks) ;
	}
	
	/**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromNode(final LdvModelNode node)
	{
		init() ;
		
		if (null == node)
			return ;

		_sLexique    = node.getLexicon() ;
		_sComplement = node.getComplement() ;
		_sCertitude  = node.getCertitude() ;
		_sUnit       = node.getUnit() ;
		_sInterest   = node.getInterest() ;
		_sPlural     = node.getPlural() ;
		_sVisible    = node.getVisible() ;
		_sType       = node.getType() ;
		_sFreeText   = node.getFreeText() ;
		_sArchetype  = node.getArchetype() ;
	}
	
	/**
	*  Initialize node ID and tree ID from a LdvModelNode information
	*  
	*  @param node Object to initialize IDs from 
	**/
	public void initIdsFromNode(final LdvModelNode node)
	{
		if (null == node)
		{
			_sTreeID = "" ;
			_sNodeID = "" ;
			return ;
		}

		_sTreeID = node.getTreeID() ;
		_sNodeID = node.getNodeID() ;
	}
  
  public void init() 
  {
  	_sTreeID     = "" ;
		_sNodeID     = "" ;
		_sLexique    = "" ;
		_sComplement = "" ;	 
		_sCertitude  = "" ;
		_sUnit       = "" ;
		_sInterest   = "A" ;
		_sPlural     = "" ;
		_sVisible    = "1" ;
		_sType       = "" ;        
		_sFreeText   = "" ;  
		_sArchetype  = "" ; 
		_sRights     = "" ;
		
		_aTemporaryLinks.clear() ;
  }
  
  /**
	*  Get the label as a string that sums up this message content
	*  
	*  @return a String in the form Unit/Lexicon/Plural/Certitude 
	**/
  public String getLabel()
  {
  	String sLabel = _sLexique ;
  	
  	if (false == "".equals(_sUnit))
  		sLabel = _sUnit + LdvGraphConfig.pathSeparationMARK + _sLexique ;
  	
  	if (false == "".equals(_sPlural))
  		sLabel += LdvGraphConfig.pathSeparationMARK + _sPlural ;
  	
  	if (false == "".equals(_sCertitude))
  		sLabel += LdvGraphConfig.pathSeparationMARK + _sCertitude ;
  	
  	return sLabel ;
  }
  
  /**
	*  Init from a label 
	*  
	*  @param sLabel a label in the form Unit/Lexicon/Plural/Certitude or Unit.Lexicon.Plural.Certitude  
	**/
  public void initFromLabel(String sLabel)
  {
  	init() ;
  	
  	if ((null == sLabel) || "".equals(sLabel))
  		return ;
  	
  	// Find separator (either intranodeSeparationMARK or cheminSeparationMARK) 
  	//
  	String sSeparator = LdvGraphConfig.intranodeSeparationMARK ;
  	int iPosit = sLabel.indexOf(sSeparator) ;
    if (iPosit < 0)
    {
    	sSeparator = LdvGraphConfig.pathSeparationMARK ;
    	iPosit = sLabel.indexOf(sSeparator) ;
    }
    
    // No separator, then label is simply a Lexicon
    //
    if (iPosit < 0)
    {
    	_sLexique = sLabel ;
    	return ;
    }
    
    // First guess, Lexicon + modifiers
    //
    _sLexique = sLabel.substring(0, iPosit) ;
    sLabel    = sLabel.substring(iPosit + 1, sLabel.length()) ;

    // If Lexicon is a unit, then the label is probably of the kind Unit + Lexicon + modifiers 
    //
    if ('2' == _sLexique.charAt(0))
    {
    	_sUnit = _sLexique ;

    	iPosit = sLabel.indexOf(sSeparator) ;
      if (iPosit < 0)
      {
      	_sLexique = sLabel ;
        return ;
      }

      _sLexique = sLabel.substring(0, iPosit) ;
      sLabel    = sLabel.substring(iPosit + 1, sLabel.length()) ;
    }
    
    UpdateCertitudeAndPlural(sLabel) ;
  }
  
  public void UpdateCertitudeAndPlural(String sLabel)
  {
    int posWPL   = sLabel.indexOf("WPL") ;
    int posWCE   = sLabel.indexOf("WCE") ;
    int posWCEA0 = sLabel.indexOf("WCEA0") ;
    int posCompl = sLabel.indexOf("$") ;

    // if plural
    if (posWPL >= 0)
    	_sPlural = sLabel.substring(posWPL, posWPL + LdvGraphConfig.LEXI_LEN) ;

    // if certitude is 100%, there is no need to put anything because it is the default value 
    if (posWCEA0 >= 0)
    	_sCertitude = "" ;
    else
      if (posWCE >= 0)
        _sCertitude = sLabel.substring(posWCE, posWCE + LdvGraphConfig.LEXI_LEN) ;

    // by convention, the complement is always in the end
    if (posCompl >= 0)
    {
      String sCompl = sLabel.substring(posCompl+1, sLabel.length()) ;
      // if (sCompl.length() > BASE_COMPLEMENT_LEN)
      //   sCompl = string(sCompl, 0, BASE_COMPLEMENT_LEN) ;
      _sComplement = sCompl ;
    }
  }
  
	public String getTreeID() {
		return _sTreeID ;
	}
	public void setTreeID(String sTreeID)
	{
		_sTreeID = sTreeID ;
	}

	public String getNodeID() {
		return _sNodeID ;
	}
	public void setNodeID(String sNodeID) {
		_sNodeID = sNodeID ;
	}

	public String getLexique() {
		return _sLexique ;
	}
	public void setLexique(String sLexique) {
		_sLexique = sLexique ;
	}

	public String getComplement() {
		return _sComplement ;
	}
	public void setComplement(String sComplement)
	{
		_sComplement = sComplement ;
	}

	public String getCertitude() {
		return _sCertitude ;
	}
	public void setCertitude(String sCertitude) {
		_sCertitude = sCertitude ;
	}

	public String getUnit() {
		return _sUnit ;
	}
	public void setUnit(String sUnit) {
		_sUnit = sUnit ;
	}

	public String getInterest() {
		return _sInterest ;
	}
	public void setInterest(String sInterest) {
		_sInterest = sInterest ;
	}

	public String getPlural() {
		return _sPlural ;
	}
	public void setPlural(String sPlural) {
		_sPlural = sPlural ;
	}

	public String getVisible() {
		return _sVisible ;
	}
	public void setVisible(String sVisible) {
		_sVisible = sVisible ;
	}

	public String getType() {
		return _sType ;
	}
	public void setType(String sType) {
		_sType = sType ;
	}

	public String getFreeText() {
		return _sFreeText ;
	}
	public void setFreeText(String sFreeText) {
		_sFreeText = sFreeText ;
	}

	public String getArchetype() {
		return _sArchetype ;
	}
	public void setArchetype(String sArchetype) {
		_sArchetype = sArchetype ;
	}

	public String getRights() {
		return _sRights ;
	}
	public void setRights(String sRights) {
		_sRights = sRights ;
	}

	public Vector<LdvModelLinkedNode> GetTemporaryLinks() {
		return _aTemporaryLinks ;
	}
	public void SetTemporaryLinks(Vector<LdvModelLinkedNode> aTemporaryLinks) 
	{
		_aTemporaryLinks.clear() ;
		
		if (aTemporaryLinks.isEmpty())
			return ;
		
		for (Iterator<LdvModelLinkedNode> itr = aTemporaryLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLinkedNode LinkedNode = itr.next() ;
			_aTemporaryLinks.addElement(new LdvModelLinkedNode(LinkedNode)) ;
		}
	}
	
	/**
	  * Determine whether two BBMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  otherMessage BBMessage to compare with
	  * 
	  */
	public boolean equals(BBMessage otherMessage)
	{
		if (this == otherMessage) {
			return true ;
		}
		if (null == otherMessage) {
			return false ;
		}
		
		return (_sTreeID.equals(otherMessage._sTreeID)         &&
						_sNodeID.equals(otherMessage._sNodeID)         &&
						_sLexique.equals(otherMessage._sLexique)       &&
						_sComplement.equals(otherMessage._sComplement) &&	 
						_sCertitude.equals(otherMessage._sCertitude)   &&
						_sUnit.equals(otherMessage._sUnit)             &&
						_sInterest.equals(otherMessage._sInterest)     &&
						_sPlural.equals(otherMessage._sPlural)         &&
						_sVisible.equals(otherMessage._sVisible)       &&
						_sType.equals(otherMessage._sType)             &&        
						_sFreeText.equals(otherMessage._sFreeText)     &&  
						_sArchetype.equals(otherMessage._sArchetype)   && 
						_sRights.equals(otherMessage._sRights)) ;
	}

	/**
	  * Determine whether an object is exactly similar to this
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare
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

		final BBMessage itemData = (BBMessage) o ;

		return equals(itemData) ;
	}
}
