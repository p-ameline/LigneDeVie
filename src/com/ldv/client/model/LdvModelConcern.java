package com.ldv.client.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Project management issue
 * 
 * A line that can host severity levels and "worst goals" indicator
 */
public class LdvModelConcern extends LdvModelLine
{
	protected String _sID ;
	protected String _sContinuityCode ;
	
	private List<LdvModelConcernSeverityLevel> _severitiesArray = new ArrayList<LdvModelConcernSeverityLevel>() ;
	private List<LdvModelGoalSegment>          _worstJalons     = new ArrayList<LdvModelGoalSegment>() ;
	
	public LdvModelConcern() {
		init() ; 
	}
	
	void init()
	{
		initBox() ;
		
		_sID             = "" ;
		_sContinuityCode = "" ;
	}
	
	public void addSeverity(LdvModelConcernSeverityLevel severity) {
		_severitiesArray.add(severity) ;
	}
	
	public void addWorstJalon(LdvModelGoalSegment jalon) {
		_worstJalons.add(jalon) ;
	}

	public String getID() {
  	return _sID ;
  }
	public void setID(String sID) {
  	_sID = sID ;
  }
	
	public List<LdvModelConcernSeverityLevel> getSeverityArray() {
		return  this._severitiesArray ;
	}
	
	public String getContinuityCode() {
  	return _sContinuityCode;
  }
	public void setContinuityCode(String sContinuityCode) {
  	_sContinuityCode = sContinuityCode;
  }
}
