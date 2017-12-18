package com.ldv.client;

import java.util.Vector;

import com.ldv.shared.model.LdvTime;

public class LdvConcern
{
	protected String  _sTitle ;
	protected String  _sID ;
	
	protected LdvTime _tBeginDate ;
	protected LdvTime _tEndDate ;
		
	protected Vector<LdvConcernDocument> _aDocuments ;
	protected Vector<LdvConcernSeverity> _aSeverities ;
	
	public LdvConcern()
	{
		init() ; 
	}
	
	void init()
	{
		_sTitle = "" ;
		_sID    = "" ;
		_tBeginDate.init() ;
		_tEndDate.init() ;
	}

	public String get_sTitle()
  {
  	return _sTitle;
  }

	public void set_sTitle(String sTitle)
  {
  	_sTitle = sTitle;
  }

	public String get_sID()
  {
  	return _sID;
  }

	public void set_sID(String sID)
  {
  	_sID = sID;
  }

	public LdvTime get_tBeginDate()
  {
  	return _tBeginDate;
  }

	public void set_tBeginDate(LdvTime tBeginDate)
  {
  	_tBeginDate = tBeginDate;
  }

	public LdvTime get_tEndDate()
  {
  	return _tEndDate;
  }

	public void set_tEndDate(LdvTime tEndDate)
  {
  	_tEndDate = tEndDate;
  }
	
	public void add_Severity(LdvConcernSeverity severity)
  {
		_aSeverities.addElement(severity) ;
  }
	
	public void add_Document(LdvConcernDocument document)
  {
		_aDocuments.addElement(document) ;
  }
}