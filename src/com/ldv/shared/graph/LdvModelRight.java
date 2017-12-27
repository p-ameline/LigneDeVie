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
}
