package com.ldv.shared.archetype;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeHead implements IsSerializable
{
	protected String _sTitle ;
	protected String _sHelpUrl ;
	protected String _sLang ;
	protected String _sGroup ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeHead() {
		init() ; 
	}
	
	/**
	 * Standard constructor 
	 * 
	 **/
	public LdvArchetypeHead(String sTitle, String sHelpUrl, String sLang, String sGroup)
	{
		init() ; 
		
		_sTitle   = sTitle ;
		_sHelpUrl = sHelpUrl ;
		_sLang    = sLang ;
		_sGroup   = sGroup ;
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeHead(final LdvArchetypeHead rv) {
		initFromArchetypeConcern(rv) ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sTitle   = "" ;
		_sHelpUrl = "" ;
		_sLang    = "" ;
		_sGroup   = "" ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromArchetypeConcern(final LdvArchetypeHead other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sTitle   = other._sTitle ;
		_sHelpUrl = other._sHelpUrl ;
		_sLang    = other._sLang ;
		_sGroup   = other._sGroup ;
	}

	public String getTitle() {
		return _sTitle ;
	}
	public void setTitle(String sTitle) {
		_sTitle = sTitle ;
	}
	
	public String getHelpUrl() {
		return _sHelpUrl ;
	}
	public void setHelpUrl(String sHelpUrl) {
		_sHelpUrl = sHelpUrl ;
	}

	public String getLang() {
		return _sLang ;
	}
	public void setLang(String sLang) {
		_sLang = sLang ;
	}

	public String getGroup() {
		return _sGroup ;
	}
	public void setGroup(String sGroup) {
		_sGroup = sGroup ;
	}

	/**
	  * Determine whether two LdvArchetypeHead objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeHead to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeHead other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sTitle,   other._sTitle)   &&
						MiscellanousFcts.areIdenticalStrings(_sHelpUrl, other._sHelpUrl) &&
						MiscellanousFcts.areIdenticalStrings(_sLang,    other._sLang)    &&
						MiscellanousFcts.areIdenticalStrings(_sGroup,   other._sGroup)) ;
	}

	/**
	  * Determine whether two LdvArchetypeHead objects are exactly similar
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

		final LdvArchetypeHead node = (LdvArchetypeHead) o ;

		return (this.equals(node)) ;
	}
}
