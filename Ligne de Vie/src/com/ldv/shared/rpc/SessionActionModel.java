package com.ldv.shared.rpc;

import java.io.Serializable;

public class SessionActionModel implements Serializable
{
	private static final long serialVersionUID = -2400141373312561921L;
	
	private String _sLdvId ;
	private String _sUserId ;
	private String _sToken ;

	public SessionActionModel(final String sLdvId, final String sUserId, final String sToken) 
	{
		_sLdvId  = sLdvId ;
		_sUserId = sUserId ; 
		_sToken  = sToken ;
	}

  public SessionActionModel() 
	{
  	_sLdvId  = "" ;
  	_sUserId = "" ;
  	_sToken  = "" ;
	}

	public String getLdvIdentifier() {
		return _sLdvId ;
	}
	
	public String getUserIdentifier() {
		return _sUserId ;
	}
	
	public String getToken() {
		return _sToken ;
	}	
}
