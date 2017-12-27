package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class GetArchetypeAction implements Action<GetArchetypeResult> 
{
	private String _sUserId ;
	private String _sArchetypeReference ;

	public GetArchetypeAction(final String sArchetypeReference, final String sUserId) 
	{
		super() ;

		_sUserId             = sUserId ;
		_sArchetypeReference = sArchetypeReference ;
	}

  public GetArchetypeAction() 
	{
  	super() ;

  	_sUserId             = "" ;
		_sArchetypeReference = "" ;
	}

  public String getUserId() {
		return _sUserId ;
	}
  
  public String getArchetypeReference() {
		return _sArchetypeReference ;
	}
}
