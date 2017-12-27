package com.ldv.shared.rpc;

import com.ldv.shared.graph.LdvModelGraph;

import net.customware.gwt.dispatch.shared.Result;

public class GetGraphResult implements Result 
{
	private boolean       _bSuccess ;
	private LdvModelGraph _modelGraph ;
	private String        _message ;

	public GetGraphResult(final boolean bSuccess, final LdvModelGraph modelGraph, final String message) 
	{
		_bSuccess   = bSuccess ;
		_modelGraph = new LdvModelGraph(modelGraph) ;
		_message    = message ;
	}

	@SuppressWarnings("unused")
	private GetGraphResult() 
	{
		_bSuccess   = false ;
		_modelGraph = null ;
		_message    = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public LdvModelGraph getGraph() {
		return _modelGraph ;
	}

	public String getMessage() {
		return _message ;
	}
}
