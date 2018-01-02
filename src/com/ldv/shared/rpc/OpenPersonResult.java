package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class OpenPersonResult implements Result 
{
	private String _sessionID ;
	private String _message ;

	public OpenPersonResult(final String sessionId, final String message) 
	{
		_sessionID = sessionId ;
		_message   = message ;
	}

	@SuppressWarnings("unused")
	private OpenPersonResult() 
	{
		_sessionID = "" ;
		_message   = "" ;
	}

	public String getSessionId() {
		return _sessionID ;
	}

	public String getMessage() {
		return _message ;
	}
}