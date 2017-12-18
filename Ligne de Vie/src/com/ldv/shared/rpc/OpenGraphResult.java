package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class OpenGraphResult implements Result 
{
	private boolean _bSuccess ;
	private String  _sMessage ;

	public OpenGraphResult(final boolean bSuccess, final String message) 
	{
		_bSuccess = bSuccess ;
		_sMessage = message ;
	}

	@SuppressWarnings("unused")
	private OpenGraphResult() 
	{
		_bSuccess = false ;
		_sMessage = "" ;
	}

	public boolean getSuccess() {
		return _bSuccess ;
	}

	public String getMessage() {
		return _sMessage ;
	}
}
