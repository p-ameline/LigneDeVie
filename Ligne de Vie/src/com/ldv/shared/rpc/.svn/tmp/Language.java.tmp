package com.ldv.shared.rpc ;

import com.google.gwt.user.client.rpc.IsSerializable ;

/**
 * Language.java
 *
 * The Language class represents object type Traject in database
 * 
 * Created: 10 Oct 2010
 *
 * Author: PA
 * 
 */
public class Language implements java.lang.Comparable<Language>, IsSerializable 
{
	private String _sIsoCode ;
	private String _sLabel ;
	
	public Language() {
		reset() ;
	}
	
	public Language(String sIsoCode, String sLabel) 
	{
		_sIsoCode = sIsoCode ;
		_sLabel   = sLabel ;
	}
	
	public Language(Language lang) 
	{
		_sIsoCode = lang.getIsoCode() ;
		_sLabel   = lang.getLabel() ;
	}
		
	public void reset() 
	{
		_sIsoCode = "" ;
		_sLabel   = "" ;
	}
	
	public String getIsoCode() {
		return _sIsoCode ;
	}
	public void setIsoCode(String sIsoCode) {
		_sIsoCode = sIsoCode ;
	}

	public String getLabel() {
		return _sLabel ;
	}
	public void setLabel(String sLabel) {
		_sLabel = sLabel ;
	}
	
	@Override
	public int compareTo(Language arg0) {
		if (_sIsoCode.equals(arg0._sIsoCode))
			return 1 ;
		return 0;
	}
}
