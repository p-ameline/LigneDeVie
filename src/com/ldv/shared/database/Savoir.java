package com.ldv.shared.database ;

import com.google.gwt.user.client.rpc.IsSerializable ;

/**
 * Lexicon.java
 *
 * The Lexicon class represents object type information in database
 * 
 * Created: 25 Dec 2011
 *
 * Author: PA
 * 
 */
public class Savoir implements IsSerializable 
{
	private String _sCode ;
	private String _sQualified ;
	private String _sLink ;
	private String _sQualifier ;
	
	//
	//
	public Savoir() {
		reset() ;
	}
		
	/**
	 * Standard constructor
	 */
	public Savoir(final String sCode, final String sQualified, final String sLink, final String sQualifier) 
	{
		_sCode      = sCode ;
		_sQualified = sQualified ;
		_sLink      = sLink ;
		_sQualifier = sQualifier ;
	}
	
	/**
	 * Copy constructor
	 */
	public Savoir(final Savoir model) 
	{
		_sCode      = model._sCode ;
		_sQualified = model._sQualified ;
		_sLink      = model._sLink ;
		_sQualifier = model._sQualifier ;
	}
			
	public void reset() 
	{
		_sCode      = "" ;
		_sQualified = "" ;
		_sLink      = "" ;
		_sQualifier = "" ;
	}
	
	public String getCode() {
  	return _sCode ;
  }
	public void setCode(final String sCode) {
  	_sCode = sCode ;
  }
	
	public String getQualified() {
  	return _sQualified ;
  }
	public void setQualified(final String sQualified) {
		_sQualified = sQualified ;
  }

	public String getLink() {
  	return _sLink ;
  }
	public void setLink(final String sLink) {
		_sLink = sLink ;
  }

	public String getQualifier() {
  	return _sQualifier ;
  }
	public void setQualifier(final String sQualifier) {
		_sQualifier = sQualifier ;
  }
		
	/**
	  * Determine whether two Savoir objects are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other Other Svoir to compare to
	  * 
	  */
	public boolean equals(Savoir other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (_sCode.equals(other._sCode) && 
				    _sQualified.equals(other._sQualified) &&
				    _sLink.equals(other._sLink) &&
				    _sQualifier.equals(other._sQualifier)) ;
	}
  
	/**
	  * Determine whether an object is exactly similar to this Lexicon object
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

		final Savoir other = (Savoir) o ;

		return (this.equals(other)) ;
	}
}
