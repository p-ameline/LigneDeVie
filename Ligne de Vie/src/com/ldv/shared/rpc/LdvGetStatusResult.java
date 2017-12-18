package com.ldv.shared.rpc;

import com.ldv.shared.model.LdvStatus;

import net.customware.gwt.dispatch.shared.Result;

public class LdvGetStatusResult implements Result 
{		
	private LdvStatus _status ;
	private boolean   _bSuccess ;
	
	public LdvGetStatusResult(boolean bSuccess, LdvStatus.AvailabilityLevel availability)
	{
		super() ;
		
		_bSuccess = bSuccess ;
		
		_status = new LdvStatus() ;
		_status.setAvailabilityLevel(availability) ;
	}
	
	public LdvGetStatusResult()
	{
		super() ;
		
		_bSuccess = false ;
		_status = new LdvStatus() ;
	}
		
	public LdvStatus getStatus() {
		return _status ;
	}
	
	public boolean isSuccessful() {
		return _bSuccess ;
	}
}
