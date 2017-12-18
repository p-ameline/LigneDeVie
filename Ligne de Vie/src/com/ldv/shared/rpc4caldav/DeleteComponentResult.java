package com.ldv.shared.rpc4caldav;

import net.customware.gwt.dispatch.shared.Result;

public class DeleteComponentResult implements Result 
{
	private boolean _bSuccess ;
	private String  _sMessage ;
	
	public DeleteComponentResult(final boolean bSuccess, final String message) 
	{
		_bSuccess = bSuccess ;
		_sMessage = message ;
	}

	@SuppressWarnings("unused")
	private DeleteComponentResult() 
	{
		_bSuccess = false ;
		_sMessage = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public String getMessage() {
		return _sMessage ;
	}
}
