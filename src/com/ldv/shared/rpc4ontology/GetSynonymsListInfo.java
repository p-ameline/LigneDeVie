package com.ldv.shared.rpc4ontology;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a list of Lexicon records from a text 
 */
public class GetSynonymsListInfo implements Action<GetSynonymsListResult> 
{
  private String _sUserId ;
	private String _sLanguage ;
	
	private String _sSemanticCode ;

	public GetSynonymsListInfo(final String sUserId, final String sLanguage, final String sSemanticCode) 
	{
		super() ;
		
		_sUserId       = sUserId ;
		_sLanguage     = sLanguage ;
		
		_sSemanticCode = sSemanticCode ;
	}

	@SuppressWarnings("unused")
	private GetSynonymsListInfo() 
	{
		super() ;
		
		_sUserId       = "" ;
		_sLanguage     = "" ;
		_sSemanticCode = "" ;
	}

	public String getLanguage() {
		return _sLanguage ;
	}

	public String getSemanticCode() {
		return _sSemanticCode ;
	}
	
	public String getUserId() {
		return _sUserId ;
	}
}
