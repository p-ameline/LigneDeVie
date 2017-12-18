package com.ldv.shared.rpc4ontology;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a list of Lexicon records from a text 
 */
public class GetLexiconListInfo implements Action<GetLexiconListResult> 
{
  private String _sUserId ;
	private String _sLanguage ;
	private String _sStartingText ;
	private int    _iBoxIndex ;

	public GetLexiconListInfo(final String sUserId, final String sLanguage, final String sStartingText, final int iBoxIndex) 
	{
		super() ;
		
		_sUserId       = sUserId ;
		_sLanguage     = sLanguage ;
		_sStartingText = sStartingText ;
		_iBoxIndex     = iBoxIndex ;
	}

	@SuppressWarnings("unused")
	private GetLexiconListInfo() 
	{
		super() ;
		
		_sUserId       = "" ;
		_sLanguage     = "" ;
		_sStartingText = "" ;
		_iBoxIndex     = -1 ;
	}

	public String getLanguage() {
		return _sLanguage ;
	}

	public String getStartingText() {
		return _sStartingText ;
	}
	
	public String getUserId() {
		return _sUserId ;
	}
	
	public int getBoxIndex() {
		return _iBoxIndex ;
	}
}
