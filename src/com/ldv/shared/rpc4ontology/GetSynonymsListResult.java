package com.ldv.shared.rpc4ontology;

import java.util.ArrayList;

import com.ldv.shared.model.LdvModelLexicon;

import net.customware.gwt.dispatch.shared.Result;

public class GetSynonymsListResult implements Result
{
  private ArrayList<LdvModelLexicon> _aLexiconList = new ArrayList<LdvModelLexicon>() ;
	private String                     _sMessage ;
	
	/**
	 * */
	public GetSynonymsListResult()
	{
		super() ;
		
		_sMessage = "" ;
	}

	/**
	 * @param sMessage
	 * */
	public GetSynonymsListResult(final String sMessage)
	{
		super() ;
		
		_sMessage = sMessage ;
	}
	
	public String getMessage() {
		return _sMessage ;
	}
	public void setMessage(String sMessage) {
		_sMessage = sMessage ;
	}
	
	public ArrayList<LdvModelLexicon> getLexiconArray() {
		return _aLexiconList ;
	}
	
	public void addLexiconModel(LdvModelLexicon lexicon) {
		_aLexiconList.add(new LdvModelLexicon(lexicon)) ;
	}
}
