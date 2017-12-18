package com.ldv.shared.rpc4ontology;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a Lexicon record from its code 
 */
public class GetLexiconAction implements Action<GetLexiconResult> 
{
	private String _sCode ;			// Lexicon record's code
	private String _sNodeID ;		// Node to be updated when information found

	public GetLexiconAction(final String sCode, final String sNodeID) 
	{
		super() ;

		_sCode   = sCode ;
		_sNodeID = sNodeID ;
	}

  public GetLexiconAction() 
	{
  	super() ;

  	_sCode   = "" ;
  	_sNodeID = "" ;
	}

  public String getCode() {
		return _sCode ;
	}
  
  public String getNodeID() {
		return _sNodeID ;
	}
}
