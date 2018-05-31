package com.ldv.shared.rpc;

import com.ldv.shared.graph.LdvModelGraph;

import net.customware.gwt.dispatch.shared.Action;

/**
 * This object is sent from the client to the server in order to save/update a (sub)graph (linked to a given project)
 */
public class UpdateGraphAction implements Action<UpdateGraphResult> 
{
	private SessionActionModel _sessionElements ;
	private LdvModelGraph      _modifiedGraph ;
	private String             _sProjectURI ;

	public UpdateGraphAction(final String sLdvId, final String sUserId, final String sToken, final LdvModelGraph modifiedGraph, final String sProjectURI) 
	{
		super() ;

		_sessionElements = new SessionActionModel(sLdvId, sUserId, sToken) ;
		_modifiedGraph   = new LdvModelGraph(modifiedGraph) ;
		
		_sProjectURI     = sProjectURI ;
	}

  public UpdateGraphAction() 
	{
  	super() ;

		_sessionElements = new SessionActionModel() ;
		_modifiedGraph   = null ;
		_sProjectURI     = "" ;
	}

  public SessionActionModel getSessionElements() {
		return _sessionElements ;
	}
  
  public LdvModelGraph getModifiedGraph() {
  	return _modifiedGraph ;
  }
  
  public String getProjectURI() {
  	return _sProjectURI ;
  }
}
