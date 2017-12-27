package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class OpenPerson implements Action<SendLoginResult> 
{
	private String _sessionId ;
	private String _encryptedPassword ;

	public OpenPerson(final String id, final String pass) 
	{
		_sessionId         = id ;
		_encryptedPassword = pass ;		
	}

  public OpenPerson() 
	{
  	_sessionId         = "" ;
		_encryptedPassword = "" ;
	}

	public String getSessionId() 
	{
		return _sessionId ;
	}

	public String getEncryptedPassword() 
	{
		return _encryptedPassword ;
	}
}
