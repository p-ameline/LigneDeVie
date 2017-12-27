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
	
	public LdvModelLink()
	{
		init() ;
	}
	
	public LdvModelLink(String sQualified, String sLink, String sQualifier)
	{
		init() ;
		
		_sQUALIFIED = sQualified ;
		_sLINK      = sLink ;
		_sQUALIFIER = sQualifier ;
	}
	
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
	
	protected void init()
	{
		_sQUALIFIED = "" ;
		_sLINK      = "" ;
		_sQUALIFIER = "" ;
		
		_sStorageFileName = "" ;
	}
	
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
}
