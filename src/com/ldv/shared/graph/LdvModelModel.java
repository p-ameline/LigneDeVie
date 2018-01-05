package com.ldv.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LdvModelModel implements IsSerializable
{
	//
	// Models types
	//
	public enum MODEL_TYPE { MODEL_UNDEFINED, MODEL_ARCHETYPE } ;
	
	protected String     _sOBJECT ;
	protected String     _sNODE ;
	protected MODEL_TYPE _iTYPE ;
	protected String     _sMODEL ;
	
	/**
	 * Default constructor
	 */
	@SuppressWarnings("unused")
	private LdvModelModel() {
		init() ; 
	}
	
	/**
	 * Plain vanilla constructor 
	 */
	public LdvModelModel(final String sTree, final String sNode, final MODEL_TYPE iType, final String sModel)
	{
		_sOBJECT = sTree ;
		_sNODE   = sNode ;
		_iTYPE   = iType ;
		_sMODEL  = sModel ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model right
	 * 
	 */
	public LdvModelModel(final LdvModelModel source) {
		initFromModelSource(source) ;
	}
	
	/**
	 * Copy all information from the model
	 * 
	 * @param source LdvModelModel object to initialize from
	 */
	public void initFromModelSource(final LdvModelModel source) 
	{
		init() ;
		
		if (null == source)
			return ;

		_sOBJECT = source._sOBJECT ;
		_sNODE   = source._sNODE ;
		_iTYPE   = source._iTYPE ;
		_sMODEL  = source._sMODEL ;
	}
	
	/**
	 * Reset all information
	 */
	void init()
	{
		_sOBJECT = "" ;
		_sNODE   = "" ;
		_iTYPE   = MODEL_TYPE.MODEL_UNDEFINED ;
		_sMODEL  = "" ;
	}
	
	public String getObject() {
  	return _sOBJECT ;
  }
	public void setObject(final String sTree) {
		_sOBJECT = sTree ;
  }
	
	public String getNode() {
  	return _sNODE ;
  }
	public void setNode(final String sNode) {
		_sNODE = sNode ;
  }

	public MODEL_TYPE getType() {
  	return _iTYPE ;
  }
	public String getTypeAsString()
	{
		switch(_iTYPE)
		{
			case MODEL_ARCHETYPE: return "AR" ;
			default             : return "" ;
		}
	}
	public void setType(final MODEL_TYPE iType) {
		_iTYPE = iType ;
  }
	public void setTypeAsString(final String sTypeAsString)
	{
		if ("AR".equals(sTypeAsString))
			_iTYPE = MODEL_TYPE.MODEL_ARCHETYPE ;
		else
			_iTYPE = MODEL_TYPE.MODEL_UNDEFINED ;
  }
	
	public String getModel() {
  	return _sMODEL ;
  }
	public void setModel(final String sModel) {
		_sMODEL = sModel ;
  }
	
	/**
	  * Determine whether two models contain the same information
	  * 
	  * @param other LdvModelModel to compare to
	  * 
	  * @return true if all contained data are the same, false if not
	  */
	public boolean hasSameContent(final LdvModelModel other)
	{
		if (this == other)
			return true ;

		if (null == other) 
			return false ;
		
		return (_sOBJECT.equals(other._sOBJECT) &&
				    _sNODE.equals(other._sNODE) &&
				    (_iTYPE == other._iTYPE) &&
				    _sMODEL.equals(other._sMODEL)) ;
	}
	
	/**
	  * Determine whether two models have the same content
	  * 
	  * @param other LdvModelModel to compare to
	  * 
	  * @return <code>true</code> if Ok, <code>false</code> if not
	  */
	public boolean equals(final LdvModelModel other)
	{
		if (this == other) 
			return true ;
	
		if (null == other) 
			return false ;
		
		return hasSameContent(other) ;
	}
	
	/**
	  * Determine whether two objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param node LdvModelNode to compare to
	  * 
	  * @return true if all data are the same, false if not
	  */
	public boolean equals(final Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final LdvModelModel other = (LdvModelModel) o ;

		return (this.equals(other)) ;
	}
}
