package com.ldv.shared.rpc4caldav;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.calendar.Availability;

import net.customware.gwt.dispatch.shared.Result;

public class GetAvailabilityResult implements Result 
{
	private boolean              _bSuccess ;
	private Vector<Availability> _aAvailabilities = new Vector<Availability>() ;
	private String               _sMessage ;
	
	public GetAvailabilityResult(final boolean bSuccess, final Vector<Availability> aAvailabilities, final String message) 
	{
		_bSuccess = bSuccess ;
		_sMessage = message ;
		
		if ((null != aAvailabilities) && (false == aAvailabilities.isEmpty()))
			initFromAvailabilities(aAvailabilities) ;
	}

	@SuppressWarnings("unused")
	private GetAvailabilityResult() 
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
	
	public Vector<Availability> getAvailabilities() {
		return _aAvailabilities ;
	}
	public void addAvailability(final Availability availability)
	{
		if (null == availability)
			return ;
		_aAvailabilities.add(new Availability(availability)) ;
	}
	public void initFromAvailabilities(Vector<Availability> aAvailabilities)
	{
		_aAvailabilities.clear() ;
		
		if ((null == aAvailabilities) || aAvailabilities.isEmpty())
			return ;
		
		for (Iterator<Availability> it = aAvailabilities.iterator() ; it.hasNext() ; )
			_aAvailabilities.add(new Availability(it.next())) ;
	}
}
