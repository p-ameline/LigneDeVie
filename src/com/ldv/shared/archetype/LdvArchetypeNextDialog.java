package com.ldv.shared.archetype;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeNextDialog implements IsSerializable
{
	protected String _sReference ;

	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeNextDialog() {
		init() ; 
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeNextDialog(final LdvArchetypeNextDialog rv) {
		initFromArchetypeNextDialog(rv) ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sReference = "" ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromArchetypeNextDialog(final LdvArchetypeNextDialog other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sReference = other._sReference ;
	}

	public String getReference() {
		return _sReference ;
	}
	public void setReference(String sReference) {
		_sReference = sReference ;
	}

	/**
	  * Determine whether two lexicon objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param node LdvModelLexique to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeNextDialog other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sReference, other._sReference)) ;
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

		final LdvArchetypeNextDialog node = (LdvArchetypeNextDialog) o ;

		return (this.equals(node)) ;
	}
}
