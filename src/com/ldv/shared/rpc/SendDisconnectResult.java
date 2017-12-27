package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class SendDisconnectResult implements Result 
{
	private boolean _ok ;
	private String  _message ;

	public SendDisconnectResult(final boolean isOk, final String message) 
	{
		_ok      = isOk ;
		_message = message ;
	}

	@SuppressWarnings("unused")
	private SendDisconnectResult() 
	{
		_ok      = false ;
		_message = "" ;
	}

	public boolean isOk() 
	{
		return _ok ;
	}

	public String getMessage() 
	{
		return _message ;
	}
}
