package com.ldv.shared.rpc;

import com.ldv.shared.graph.LdvModelGraph;

import net.customware.gwt.dispatch.shared.Result;

public class UpdateGraphResult implements Result 
{
	private boolean       _bSuccess ;
	private LdvModelGraph _savedGraph ;
	private String        _message ;

	public UpdateGraphResult(final boolean bSuccess, final LdvModelGraph savedGraph, final String message) 
	{
		_bSuccess   = bSuccess ;
		_savedGraph = new LdvModelGraph(savedGraph) ;
		_message    = message ;
	}

	@SuppressWarnings("unused")
	private UpdateGraphResult() 
	{
		_bSuccess   = false ;
		_savedGraph = null ;
		_message    = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public LdvModelGraph getGraph() {
		return _savedGraph ;
	}

	public String getMessage() {
		return _message ;
	}
}
