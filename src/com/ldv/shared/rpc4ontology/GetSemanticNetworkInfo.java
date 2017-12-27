package com.ldv.shared.rpc4ontology;

import net.customware.gwt.dispatch.shared.Action;

/**
 * Object used to get a list of Lexicon records from a text 
 */
public class GetSemanticNetworkInfo implements Action<GetSemanticNetworkResult> 
{
  private String _sUserId ;
	private String _sLanguage ;
	
	private String  _sSemanticCode ;
	private boolean _bReverse ;
	private int     _iNetworkDepth ;

	public GetSemanticNetworkInfo(final String sUserId, final String sLanguage, final String sSemanticCode, boolean bReverse, int iNetworkDepth) 
	{
		super() ;
		
		_sUserId       = sUserId ;
		_sLanguage     = sLanguage ;
		
		_sSemanticCode = sSemanticCode ;
		_bReverse      = bReverse ;
		_iNetworkDepth = iNetworkDepth ;
	}

	@SuppressWarnings("unused")
	private GetSemanticNetworkInfo() 
	{
		super() ;
		
		_sUserId       = "" ;
		_sLanguage     = "" ;
		_sSemanticCode = "" ;
		_bReverse      = false ;
		_iNetworkDepth = 1 ;
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
	
	public boolean isReverse() {
		return _bReverse ;
	}
	
	public int getNetworkDepth() {
		return _iNetworkDepth ;
	}
}
