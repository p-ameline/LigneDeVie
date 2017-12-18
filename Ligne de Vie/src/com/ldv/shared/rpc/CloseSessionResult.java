package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Result;

public class CloseSessionResult implements Result 
{
	private boolean _bSuccess ;
	private String  _sMessage ;

	public CloseSessionResult(final boolean bSuccess, final String sMessage) 
	{
		_bSuccess = bSuccess ;
		_sMessage = sMessage ;
	}

	@SuppressWarnings("unused")
	private CloseSessionResult() 
	{
	}

	public boolean getSuccess() {
		return _bSuccess ;
	}

	public String getMessage() {
		return _sMessage ;
	}
}
