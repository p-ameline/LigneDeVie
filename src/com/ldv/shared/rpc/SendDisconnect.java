package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class SendDisconnect implements Action<SendDisconnectResult> 
{
	private String _SessionId ;

	public SendDisconnect(final String sessionId) 
	{
		_SessionId = sessionId ;
	}

  public SendDisconnect() 
	{
  	_SessionId = "" ;
	}

	public String getSessionId() 
	{
		return _SessionId ;
	}
}
