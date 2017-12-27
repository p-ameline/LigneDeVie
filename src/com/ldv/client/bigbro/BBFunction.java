package com.ldv.client.bigbro;

import com.ldv.shared.util.MiscellanousFcts;

public class BBFunction 
{
	public static enum ACTIONTYPE { fctRef, fctInit, fctActivate, fctSwitch, fctSentence } ;
	
	protected String _sFunctionName ; 
  protected BBItem _bbItem ;	
	
	public BBFunction(BBItem item, String sFunctionName) 
	{
		_sFunctionName = sFunctionName ;
		_bbItem        = item ;
	}
	
	public BBFunction(final BBFunction src) {
		initFromModel(src) ;
	}
	
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBFunction src)
	{
		init() ;
		
		if (null == src)
			return ;
		
		_sFunctionName = src._sFunctionName ;
		_bbItem        = src._bbItem ;
	}
  
  public void init() 
  {
  	_sFunctionName = "" ;
  	_bbItem        = null ;
  }
  	
  public boolean execute(ACTIONTYPE iParam, int iValue)
  {
  	if (null == _bbItem)
  		return false ;
  	
  	BBSmallBrother bigBrother = _bbItem.getBigBoss() ;

  	/*
  	if (pAdresseFct)
  		return ((*pAdresseFct)(this, pBigBrother, iParam, piValeur)) ;
  	*/

  	return false ;
  }
  
  public BBItem getBBItem() {         
  	return _bbItem ;
  }
  
  public String getFunctionName() {       
  	return _sFunctionName ;
  }
  
	/**
	  * Determine whether two BBItemData are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  itemData BBItemData to compare
	  * 
	  */
	public boolean equals(BBFunction other)
	{
		if (this == other)
			return true ;
		
		if (null == other) 
			return false ;
		
		return (MiscellanousFcts.areIdenticalStrings(_sFunctionName, other._sFunctionName) && 
				    (_bbItem == other._bbItem)) ;
	}

	/**
	  * Determine whether an object is exactly similar to this
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) 
			return true ;

		if (null == o || getClass() != o.getClass()) 
			return false ;

		final BBFunction itemData = (BBFunction) o ;

		return equals(itemData) ;
	}
}
