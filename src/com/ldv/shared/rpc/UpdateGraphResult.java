package com.ldv.shared.rpc;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvGraphMapping;

import net.customware.gwt.dispatch.shared.Result;

public class UpdateGraphResult implements Result 
{
	private boolean                 _bSuccess ;
	private Vector<LdvGraphMapping> _aMappings ;
	private String                  _message ;

	public UpdateGraphResult(final boolean bSuccess, final Vector<LdvGraphMapping> aMappings, final String message) 
	{
		_bSuccess  = bSuccess ;
		_aMappings = new Vector<LdvGraphMapping>() ;
		initFromMappings(aMappings) ;
		_message   = message ;
	}

	@SuppressWarnings("unused")
	private UpdateGraphResult() 
	{
		_bSuccess  = false ;
		_aMappings = null ;
		_message   = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public Vector<LdvGraphMapping> getMappings() {
		return _aMappings ;
	}

	public String getMessage() {
		return _message ;
	}
	
	protected void initFromMappings(Vector<LdvGraphMapping> aMappings)
	{
		if ((null == aMappings) || aMappings.isEmpty())
			return ;
		
		_aMappings.clear() ;
		
		for (Iterator<LdvGraphMapping> itr = aMappings.iterator() ; itr.hasNext() ; )
			_aMappings.add(new LdvGraphMapping(itr.next())) ;
	}
}
