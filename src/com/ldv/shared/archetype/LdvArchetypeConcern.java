package com.ldv.shared.archetype;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeConcern implements IsSerializable
{
	protected String  _sLexicon ;
	protected String  _sProject ;
	protected String  _sCategory ;
	protected boolean _bAutoCreate ;
	protected int     _iSeverity ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeConcern() {
		init() ; 
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeConcern(final LdvArchetypeConcern rv) {
		initFromArchetypeConcern(rv) ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sLexicon    = "" ;
		_sProject    = "" ;
		_sCategory   = "" ;
		_bAutoCreate = false ;
		_iSeverity   = 0 ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromArchetypeConcern(final LdvArchetypeConcern other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sLexicon    = other._sLexicon ;
		_sProject    = other._sProject ;
		_sCategory   = other._sCategory ;
		_bAutoCreate = other._bAutoCreate ;
		_iSeverity   = other._iSeverity ;
	}

	public String getLexicon() {
		return _sLexicon ;
	}
	public void setLexicon(String sLexicon) {
		_sLexicon = sLexicon ;
	}
	
	public String getProject() {
		return _sProject ;
	}
	public void setProject(String sProject) {
		_sProject = sProject ;
	}

	public String getCategory() {
		return _sCategory ;
	}
	public void setCategory(String sCategory) {
		_sCategory = sCategory ;
	}

	public boolean mustAutoCreate() {
		return _bAutoCreate ;
	}
	public void setAutoCreate(boolean bAutoCreate) {
		_bAutoCreate = bAutoCreate ;
	}

	public int getSeverity() {
		return _iSeverity ;
	}
	public void setSeverity(int iSeverity) {
		_iSeverity = iSeverity ;
	}

	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelLexique to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeConcern other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sLexicon,  other._sLexicon)  &&
						MiscellanousFcts.areIdenticalStrings(_sProject,  other._sProject)  &&
						MiscellanousFcts.areIdenticalStrings(_sCategory, other._sCategory) &&
						(_bAutoCreate == other._bAutoCreate) &&
						(_iSeverity   == other._iSeverity)) ;
	}

	/**
	  * Determine whether two LdvArchetypeConcern objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @return true if all data are the same, false if not
	  * @param o LdvArchetypeConcern to compare to
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

		final LdvArchetypeConcern node = (LdvArchetypeConcern) o ;

		return (this.equals(node)) ;
	}
}
