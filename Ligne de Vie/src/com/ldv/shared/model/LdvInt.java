package com.ldv.shared.model;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvInt
{
	protected int _iValue ;
	
	public LdvInt(int iValue)
	{
		_iValue = iValue ; 
	}
	
	/**
	 * Build a base 10 string of fixed size   
	 * 
	 * @param iSize size of resulting string
	 * @return A string that represents the value, or "" if any problem 
	 * 
	 **/
	public String intToString(int iSize)
	{
		if (0 == iSize)
			return "" ;
		
		Integer intValue = new Integer(_iValue) ;
		String sValue = intValue.toString() ;
		
		return setStringToSize(sValue, iSize) ;
	}
	
	/**
	 * Build a base X string of fixed size (X <= 62)   
	 * 
	 * @param iSize size of resulting string
	 * @return A string that represents the value, or "" if any problem 
	 * 
	 **/
	public String intToBaseString(int iSize, int iBase)
	{
	  if ((0 == iSize) || (0 == iBase) || (iBase > 62))
	    return "" ;

	  if (0 == _iValue)
	  {
	  	if (-1 == iSize)
	  		return "0" ;
	  	return MiscellanousFcts.getNChars(iSize, '0') ;
	  }
	    
	  // Check the maximal degree of _iValue 
	  //
	  int i = 1 ;
	  for ( ; (int) Math.pow(iBase, i) <= _iValue ; i++) ;
	  
	  // If converted value will exceed iSize, we quit 
	  //
	  if ((iSize > 0) && (i >= iSize))
	  	return "" ;
	  
	  i-- ;
	  
	  int iRemainingValue = _iValue ;
	  
	  String sResult  = "" ;
	  String sRefList = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" ;
	  
	  while (i >= 0)
	  {
	    int iLocalDivide = (int) Math.pow(iBase, i) ;
	    int iLocalValue  = iRemainingValue / iLocalDivide ;
	    
	    sResult += sRefList.substring(iLocalValue, iLocalValue+1) ;

	    iRemainingValue -= iLocalDivide * iLocalValue ;
	    
	    i-- ;
	  }
	  
	  return setStringToSize(sResult, iSize) ;
	}
	
	/**
	 * Adapt a string for a fixed size, for example "23" for size 5 -> "00023"   
	 * 
	 * @param sEntry string to be adapted
	 * @param iSize size of resulting string
	 * @return A string that represents the adapted string, or "" if any problem 
	 * 
	 **/
	public static String setStringToSize(final String sEntry, final int iSize)
	{
		if (0 == iSize)
			return "" ;
		
		int iStrSize = sEntry.length() ;
		
		if ((iSize == iStrSize) || (-1 == iSize)) 
			return sEntry ;
		
		if (iSize < iStrSize)
			return "" ;
		
		String sResult = sEntry ;
		
		int iNbZero = iSize - iStrSize ; 
		for (int i = 0 ; i < iNbZero; i++)
			sResult = "0" + sResult ;
		
		return sResult ;
	}

	public int getValue() {
		return _iValue ;
	}
	public void setValue(int iValue) {
		_iValue = iValue ;
	}
}
