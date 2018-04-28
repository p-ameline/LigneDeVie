package com.ldv.shared.graph;

import com.google.gwt.user.client.rpc.IsSerializable;

/** 
 * link: Qualified -- link --> Qualifier 
 * 
 */
public class LdvModelLink implements IsSerializable
{
	protected String _sQUALIFIED ;
	protected String _sLINK ;
	protected String _sQUALIFIER ;
	
	protected String _sStorageFileName ;
	
	/**
	 * Default constructor
	 */
	public LdvModelLink()
	{
		init() ;
	}
	
	/**
	 * Usual constructor, initializes everything but the storage file name
	 * 
	 * @param sQualified Trait's origin
	 * @param sLink      Trait's concept
	 * @param sQualifier Trait's destination
	 */
	public LdvModelLink(String sQualified, String sLink, String sQualifier)
	{
		init() ;
		
		_sQUALIFIED = sQualified ;
		_sLINK      = sLink ;
		_sQUALIFIER = sQualifier ;
	}
	
	/**
	 * Plain vanilla constructor
	 * 
	 * @param sQualified       Trait's origin
	 * @param sLink            Trait's concept
	 * @param sQualifier       Trait's destination
	 * @param sStorageFileName Name of the file where the link is stored
	 */
	public LdvModelLink(String sQualified, String sLink, String sQualifier, String sStorageFileName)
	{
		init() ;
		
		_sQUALIFIED = sQualified ;
		_sLINK      = sLink ;
		_sQUALIFIER = sQualifier ;
		
		_sStorageFileName = sStorageFileName ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model link
	 * 
	 **/
	public LdvModelLink(final LdvModelLink source) {
		initFromModelLink(source) ;
	}
	
	/**
	 * Reset all link information
	 */
	protected void init()
	{
		_sQUALIFIED = "" ;
		_sLINK      = "" ;
		_sQUALIFIER = "" ;
		
		_sStorageFileName = "" ;
	}
	
	/**
	 * Copy operator
	 * 
	 * @param source Link to initialize from
	 */
	protected void initFromModelLink(final LdvModelLink source)
	{
		init() ;
		
		if (null == source)
			return ;
		
		_sQUALIFIED = source._sQUALIFIED ;
		_sLINK      = source._sLINK ;
		_sQUALIFIER = source._sQUALIFIER ;
		
		_sStorageFileName = source._sStorageFileName ;
	}
	
	public String getQualified() {
  	return _sQUALIFIED ;
  }
	public String getQualifiedDocumentId() {
		return LdvModelGraph.getURIDocumentId(_sQUALIFIED) ;
  }
	public String getQualifiedPersonId() {
		return LdvModelGraph.getURIPersonId(_sQUALIFIED) ;
  }
	public String getQualifiedTreeId(String s) {
		return LdvModelGraph.getURITreeId(_sQUALIFIED) ;
  }
	public String getQualifiedNodeId() {
		return LdvModelGraph.getURINodeId(_sQUALIFIED) ;
  }
	public void setQualified(String sQualified) {
		_sQUALIFIED = sQualified ;
  }

	public String getQualifier() {
  	return _sQUALIFIER ;
  }
	public String getQualifierDocumentId() {
		return LdvModelGraph.getURIDocumentId(_sQUALIFIER) ;
  }
	public String getQualifierPersonId() {
		return LdvModelGraph.getURIPersonId(_sQUALIFIER) ;
  }
	public String getQualifierTreeId() {
		return LdvModelGraph.getURITreeId(_sQUALIFIER) ;
  }
	public String getQualifierNodeId() {
		return LdvModelGraph.getURINodeId(_sQUALIFIER) ;
  }
	public void setQualifier(String sQualifier) {
		_sQUALIFIER = sQualifier ;
  }

	public String getLink() {
  	return _sLINK ;
  }
	public void setLink(String sLink) {
		_sLINK = sLink ;
  }
	
	public String getStorageFileName() {
  	return _sStorageFileName ;
  }
	public void setStorageFileName(String sStorageFileName) {
		_sStorageFileName = sStorageFileName ;
  }
	
	/**
	 * Determine whether two models have the same content
	 * 
	 * @param other LdvModelLink to compare to
	 * 
	 * @return <code>true</code> if Ok, <code>false</code> if not
	 */
	public boolean equals(final LdvModelLink other)
	{
		if (this == other) 
			return true ;
	
		if (null == other) 
			return false ;
		
		return _sQUALIFIED.equals(other._sQUALIFIED) &&
				   _sQUALIFIER.equals(other._sQUALIFIER) &&
		       _sLINK.equals(other._sLINK) ;
	}
	
	/**
	 * Determine whether two objects are exactly similar
	 * 
	 * designed for ArrayList.contains(Obj) method
	 * because by default, contains() uses equals(Obj) method of Obj class for comparison
	 * 
	 * @param o Object to compare to
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

		final LdvModelLink other = (LdvModelLink) o ;

		return (this.equals(other)) ;
	}
}
