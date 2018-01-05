package com.ldv.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LdvModelRight implements IsSerializable
{
	protected String _sNODE ;
	protected String _sRIGHT ;
	
	public LdvModelRight()
	{
		init() ; 
	}
	
	public LdvModelRight(final String sNode, final String sRight)
	{
		_sNODE  = sNode ;
		_sRIGHT = sRight ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model right
	 * 
	 **/
	public LdvModelRight(final LdvModelRight source) {
		initFromModelRight(source) ;
	}
	
	public void initFromModelRight(final LdvModelRight source) 
	{
		init() ;
		
		if (null == source)
			return ;
		
		_sNODE  = source._sNODE ;
		_sRIGHT = source._sRIGHT ;
	}
	
	void init()
	{
		_sNODE  = "" ;
		_sRIGHT = "" ;
	}
	
	public String getNode() {
  	return _sNODE ;
  }
	public void setNode(final String sNode) {
		_sNODE = sNode ;
  }

	public String getRight() {
  	return _sRIGHT ;
  }
	public void setRight(final String sRight) {
		_sRIGHT = sRight ;
  }
	
	/**
	  * Determine whether two models have the same content
	  * 
	  * @param other LdvModelLink to compare to
	  * 
	  * @return <code>true</code> if Ok, <code>false</code> if not
	  */
	public boolean equals(final LdvModelRight other)
	{
		if (this == other) 
			return true ;
	
		if (null == other) 
			return false ;
		
		return _sNODE.equals(other._sNODE) &&
				   _sRIGHT.equals(other._sRIGHT) ;
	}
	
	/**
	  * Determine whether two objects are exactly similar
	  * 
	  * designed for ArrayList.contains(Obj) method
		* because by default, contains() uses equals(Obj) method of Obj class for comparison
	  * 
	  * @param node LdvModelRight to compare to
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

		final LdvModelRight other = (LdvModelRight) o ;

		return (this.equals(other)) ;
	}
}
