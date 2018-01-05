package com.ldv.shared.rpc;

import com.ldv.shared.graph.LdvModelGraph;

import net.customware.gwt.dispatch.shared.Action;

public class UpdateGraphAction implements Action<UpdateGraphResult> 
{
	private SessionActionModel _sessionElements ;
	private LdvModelGraph      _modifiedGraph ;

	public UpdateGraphAction(final String sLdvId, final String sUserId, final String sToken, final LdvModelGraph modifiedGraph) 
	{
		super() ;

		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
		_modifiedGraph   = new LdvModelGraph(modifiedGraph) ;
	}

  public UpdateGraphAction() 
	{
  	super() ;

		_sessionElements = new SessionActionModel() ;
		_modifiedGraph   = null ;
	}

  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
  
  public LdvModelGraph getModifiedGraph() {
  	return _modifiedGraph ;
  }
}
