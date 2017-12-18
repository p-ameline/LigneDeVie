package com.ldv.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvModelLexicon implements IsSerializable
{
	protected String _sCode ;
	protected String _sLabel ;
	protected String _sGrammar ;
	protected String _sFrequency ;

	/**
	 * Default constructor 
	 * 
	 **/
	public LdvModelLexicon()
	{
		init() ; 
	}
				
	/**
	 * Full constructor  
	 * 
	 **/
	public LdvModelLexicon(String sCode, String sLabel, String sGrammar, String sFrequency)
	{
		init() ;
		
		_sCode      = sCode ;
		_sLabel     = sLabel ;
		_sGrammar   = sGrammar ;
		_sFrequency = sFrequency ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param sourceNode Model node
	 * 
	 **/
	public LdvModelLexicon(LdvModelLexicon sourceNode)
	{
		initFromLexique(sourceNode) ;
	}
	
	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sCode      = "" ;
		_sLabel     = "" ;
		_sGrammar   = "" ;
		_sFrequency = "" ; 
	}
	
	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromLexique(LdvModelLexicon otherNode)
	{
		init() ;
		
		if (null == otherNode)
			return ;
		
		_sCode      = otherNode._sCode ;
		_sLabel     = otherNode._sLabel ;
		_sGrammar   = otherNode._sGrammar ;
		_sFrequency = otherNode._sFrequency ; 
	}
		
	
	
	public String getCode() {
		return _sCode ;
	}

	public void setCode(String sCode) {
		_sCode = sCode ;
	}

	public String getLabel() {
		return _sLabel ;
	}

	public void setLabel(String sLabel) {
		_sLabel = sLabel ;
	}

	public String getGrammar() {
		return _sGrammar ;
	}

	public void setGrammar(String sGrammar) {
		_sGrammar = sGrammar ;
	}

	public String getFrequency() {
		return _sFrequency ;
	}

	public void setFrequency(String sFrequency) {
		_sFrequency = sFrequency ;
	}

	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelLexique to compare to
	  * 
	  */
	public boolean equals(LdvModelLexicon lexicon)
	{
		if (this == lexicon) {
			return true ;
		}
		if (null == lexicon) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sCode, lexicon._sCode) && 
				    MiscellanousFcts.areIdenticalStrings(_sLabel, lexicon._sLabel) &&
				    MiscellanousFcts.areIdenticalStrings(_sGrammar, lexicon._sGrammar) &&
				    MiscellanousFcts.areIdenticalStrings(_sFrequency, lexicon._sFrequency)) ;
	}
   
	/**
	  * Determine whether two lexicon objects are exactly similar
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

		final LdvModelLexicon node = (LdvModelLexicon) o ;

		return (this.equals(node)) ;
	}
}
