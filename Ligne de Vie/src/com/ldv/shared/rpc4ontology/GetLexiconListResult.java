package com.ldv.shared.rpc4ontology;

import java.util.ArrayList;

import com.ldv.shared.model.LdvModelLexicon;

import net.customware.gwt.dispatch.shared.Result;

public class GetLexiconListResult implements Result
{
  private ArrayList<LdvModelLexicon> _aLexiconList = new ArrayList<LdvModelLexicon>() ;
	private String                     _sMessage ;
	private int                        _iLexiconTextBoxIndex ;
	
	/**
	 * */
	public GetLexiconListResult()
	{
		super() ;
		
		_sMessage = "" ;
		_iLexiconTextBoxIndex = -1 ;
	}

	/**
	 * @param sMessage
	 * */
	public GetLexiconListResult(String sMessage, int iLexiconTextBoxIndex)
	{
		super() ;
		
		_sMessage             = sMessage ;
		_iLexiconTextBoxIndex = iLexiconTextBoxIndex ;
	}
	
	public String getMessage() {
		return _sMessage ;
	}
	public void setMessage(String sMessage) {
		_sMessage = sMessage ;
	}
	
	public int getLexiconTextBoxIndex() {
		return _iLexiconTextBoxIndex ;
	}
	public void setLexiconTextBoxIndex(int iLexiconTextBoxIndex) {
		_iLexiconTextBoxIndex = iLexiconTextBoxIndex ;
	}

	public ArrayList<LdvModelLexicon> getLexiconArray() {
		return _aLexiconList ;
	}
	
	public void addLexiconModel(LdvModelLexicon lexicon) {
		_aLexiconList.add(new LdvModelLexicon(lexicon)) ;
	}
}
