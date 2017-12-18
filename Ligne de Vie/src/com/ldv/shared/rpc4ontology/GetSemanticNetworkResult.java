package com.ldv.shared.rpc4ontology;

import java.util.ArrayList;

import com.ldv.shared.database.Savoir;
import com.ldv.shared.model.LdvModelLexicon;

import net.customware.gwt.dispatch.shared.Result;

public class GetSemanticNetworkResult implements Result
{
	private ArrayList<LdvModelLexicon> _aLexiconList = new ArrayList<LdvModelLexicon>() ;
  private ArrayList<Savoir>          _aNetwork     = new ArrayList<Savoir>() ;
	private String                     _sMessage ;
	
	/**
	 * */
	public GetSemanticNetworkResult()
	{
		super() ;
		
		_sMessage = "" ;
	}

	/**
	 * @param sMessage
	 * */
	public GetSemanticNetworkResult(final String sMessage)
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
	
	public void addLexiconModel(final LdvModelLexicon lexicon) {
		_aLexiconList.add(new LdvModelLexicon(lexicon)) ;
	}
	
	public ArrayList<Savoir> getNetwork() {
		return _aNetwork ;
	}
	
	public void addTrait(final Savoir trait) {
		_aNetwork.add(new Savoir(trait)) ;
	}
}
