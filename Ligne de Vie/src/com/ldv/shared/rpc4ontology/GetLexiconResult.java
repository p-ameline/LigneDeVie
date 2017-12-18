package com.ldv.shared.rpc4ontology;

import com.ldv.shared.database.Lexicon;

import net.customware.gwt.dispatch.shared.Result;

public class GetLexiconResult implements Result 
{
	private boolean _bSuccess ;
	private Lexicon _lexicon ;
	private String  _sMessage ;
	
	private String  _sNodeID ;

	public GetLexiconResult(final boolean bSuccess, final Lexicon lexicon, final String message, final String sNodeId) 
	{
		_bSuccess = bSuccess ;
		_lexicon  = lexicon ;
		_sMessage = message ;
		_sNodeID  = sNodeId ;
	}

	@SuppressWarnings("unused")
	private GetLexiconResult() 
	{
		_bSuccess = false ;
		_lexicon  = null ;
		_sMessage = "" ;
		_sNodeID  = "" ;
	}

	public boolean wasSuccessful() {
		return _bSuccess ;
	}
	
	public Lexicon getLexicon() {
		return _lexicon ;
	}

	public String getMessage() {
		return _sMessage ;
	}
	
	public String getNodeId() {
		return _sNodeID ;
	}
}
