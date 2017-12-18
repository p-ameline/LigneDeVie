package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

public class LdvModelGenericBox
{
	protected String  _sTitle ;
	protected LdvTime _dBeginDate ;
	protected LdvTime _dEndDate ;
	
	public LdvModelGenericBox()
	{
		initBox() ; 
	}
	
	void initBox()
	{
		_sTitle = "" ;
		_dBeginDate = new LdvTime(0) ;
		_dEndDate   = new LdvTime(0) ;
	}

	public String getTitle() {
  	return _sTitle ;
  }
	public void setTitle(String sTitle) {
  	_sTitle = sTitle ;
  }

	public LdvTime getBeginDate() {
  	return _dBeginDate ;
  }
	public void setBeginDate(LdvTime dDate) {
  	_dBeginDate.initFromLdvTime(dDate) ;
  }

	public LdvTime getEndDate() {
  	return _dEndDate ;
  }
	public void setEndDate(LdvTime dDate) {
  	_dEndDate.initFromLdvTime(dDate) ;
  }
}
