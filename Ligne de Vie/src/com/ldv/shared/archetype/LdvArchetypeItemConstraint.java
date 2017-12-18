package com.ldv.shared.archetype;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

public class LdvArchetypeItemConstraint implements IsSerializable
{
	public static String VAL_ATTR_CONTR_TYPE_EXCLUS = "exclusion" ;
	public static String VAL_ATTR_CONTR_TYPE_EXP    = "expression" ;
	public static String VAL_ATTR_CONTR_TYPE_COND   = "condition" ;
	public static String VAL_ATTR_CONTR_TYPE_EXIST  = "exist" ;
	public static String VAL_ATTR_CONTR_TYPE_SONSEX = "sonsexist" ;
	public static String VAL_ATTR_CONTR_TYPE_NEEDED = "needed" ;
	
	protected String _sType ;
	protected String _sList ;
	
	/**
	 * Default constructor 
	 * 
	 **/
	public LdvArchetypeItemConstraint() {
		init() ; 
	}
	
	/**
	 * Standard constructor 
	 * 
	 **/
	public LdvArchetypeItemConstraint(String sType, String sList)
	{
		init() ; 
		
		_sType = sType ;
		_sList = sList ;
	}
	
	/**
	 * Copy constructor 
	 * 
	 **/
	public LdvArchetypeItemConstraint(final LdvArchetypeItemConstraint rv) {
		initFromItemConstraint(rv) ;
	}

	/**
	 * Reset all information
	 * 
	 **/
	public void init()
	{
		_sType = "" ;
		_sList = "" ;
	}

	/**
	 * Sets all information by copying other node content   
	 * 
	 * @param otherNode Model node
	 * @return void
	 * 
	 **/
	public void initFromItemConstraint(final LdvArchetypeItemConstraint other)
	{
		init() ;
		
		if (null == other)
			return ;
		
		_sType = other._sType ;
		_sList = other._sList ;
	}

	public String getType() {
		return _sType ;
	}
	public void setType(String sType) {
		_sType = sType ;
	}

	public String getList() {
		return _sList ;
	}
	public void setList(String sList) {
		_sList = sList ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItemConstraint objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param other LdvArchetypeItemConstraint to compare to
	  * 
	  */
	public boolean equals(LdvArchetypeItemConstraint other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sType, other._sType) &&
						MiscellanousFcts.areIdenticalStrings(_sList, other._sList)) ;
	}
	
	/**
	  * Determine whether two LdvArchetypeItemConstraint objects are exactly similar
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

		final LdvArchetypeItemConstraint node = (LdvArchetypeItemConstraint) o ;

		return (this.equals(node)) ;
	}
}
