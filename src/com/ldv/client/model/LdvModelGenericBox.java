package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

public class LdvModelGenericBox
{
	protected String  _sTitle ;
	protected String  _sLexicon ;
	protected LdvTime _dBeginDate ;
	protected LdvTime _dEndDate ;
	
	public LdvModelGenericBox()
	{
		initBox() ; 
	}
	
	void initBox()
	{
		_sTitle     = "" ;
		_sLexicon   = "" ;
		_dBeginDate = new LdvTime(0) ;
		_dEndDate   = new LdvTime(0) ;
	}

	public String getTitle() {
  	return _sTitle ;
  }
	public void setTitle(final String sTitle) {
  	_sTitle = sTitle ;
  }

	public String getLexicon() {
  	return _sLexicon ;
  }
	public void setLexicon(final String sLexicon) {
		_sLexicon = sLexicon ;
  }
	
	public LdvTime getBeginDate() {
  	return _dBeginDate ;
  }
	public void setBeginDate(final LdvTime dDate) {
  	_dBeginDate.initFromLdvTime(dDate) ;
  }

	public LdvTime getEndDate() {
  	return _dEndDate ;
  }
	public void setEndDate(final LdvTime dDate) {
  	_dEndDate.initFromLdvTime(dDate) ;
  }
}
