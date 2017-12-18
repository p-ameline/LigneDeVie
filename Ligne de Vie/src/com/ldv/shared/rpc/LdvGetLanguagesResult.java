package com.ldv.shared.rpc;

import java.util.ArrayList;

import net.customware.gwt.dispatch.shared.Result;

public class LdvGetLanguagesResult implements Result 
{	
	private ArrayList<Language> _aLanguages = new ArrayList<Language>() ;
	
	public LdvGetLanguagesResult()
	{
		super() ;
	}
		
	public void addLanguage(Language newLanguage)
	{
		if (null == newLanguage)
			return ;
		
		_aLanguages.add(newLanguage) ;
	}
	
	public ArrayList<Language> getLanguages() {
		return _aLanguages ;
	}
	
	public boolean isEmpty() {
		return _aLanguages.isEmpty() ;
	}
}
