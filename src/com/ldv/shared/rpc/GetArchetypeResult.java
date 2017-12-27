package com.ldv.shared.rpc;

import com.ldv.shared.archetype.LdvArchetype;

import net.customware.gwt.dispatch.shared.Result;

public class GetArchetypeResult implements Result 
{
	private boolean      _bSuccess ;
	private LdvArchetype _archetype ;
	private String       _message ;

	public GetArchetypeResult(final boolean bSuccess, final LdvArchetype archetype, final String message) 
	{
		_bSuccess = bSuccess ;
		_message  = message ;
		
		if (null == archetype)
			_archetype = null ;
		else
			_archetype = new LdvArchetype(archetype) ;
	}

	@SuppressWarnings("unused")
	private GetArchetypeResult() 
	{
		_bSuccess  = false ;
		_archetype = null ;
		_message   = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public LdvArchetype getArchetype() {
		return _archetype ;
	}

	public String getMessage() {
		return _message ;
	}
}
