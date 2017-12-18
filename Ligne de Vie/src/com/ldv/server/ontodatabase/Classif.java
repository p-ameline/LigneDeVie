package com.ldv.server.ontodatabase ;

/**
 * The Classif class represents a classification element
 * 
 */
public class Classif
{
	private String _sClassification ;
	private String _sCode ;
	private String _sLabel ;
	private String _sChapter ;
	
	//
	//
	public Classif() {
		reset() ;
	}
		
	public Classif(final String sClassif, final String sCode, final String sLabel, final String sChapter) 
	{
		_sClassification = sClassif ;
		_sCode           = sCode ;
		_sLabel          = sLabel ;
		_sChapter        = sChapter ;
	}
			
	public void reset() 
	{
		_sClassification = "" ;
		_sCode           = "" ;
		_sLabel          = "" ;
		_sChapter        = "" ;
	}

	public String getClassification() {
  	return _sClassification ;
  }
	public void setClassification(final String sClassification) {
  	_sClassification = sClassification ;
  }

	public String getCode() {
  	return _sCode ;
  }
	public void setCode(final String sCode) {
  	_sCode = sCode ;
  }

	public String getLabel() {
  	return _sLabel ;
  }
	public void setLabel(final String sLabel) {
  	_sLabel = sLabel ;
  }

	public String getChapter() {
  	return _sChapter ;
  }
	public void setChapter(final String sChapter) {
  	_sChapter = sChapter ;
  }
			
	public String getCispChapter()
	{
		if (_sChapter.equals(""))
			return "" ;
		
		return _sChapter.substring(0, 1) ;
	}
	
	public String getCispCategory()
	{
		if (_sChapter.length() < 2)
			return "" ;
		
		return _sChapter.substring(1, 2) ;
	}
}
