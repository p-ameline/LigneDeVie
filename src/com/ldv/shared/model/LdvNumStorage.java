package com.ldv.shared.model;

import com.ldv.shared.graph.LdvModelNode;

/**
 * Information from a node filled with numeric data    
 * 
 **/
public class LdvNumStorage
{
	protected String _sNum ;
	protected String _sNumInf ; 
	protected String _sNumSup ;
	
	protected double _dValue ; 
	protected double _dInfValue ; 
	protected double _dSupValue ;
	
	protected String _sFormat ;
	protected String _sUnit ;
	
	protected boolean _bExact ; 
	protected boolean _bInf ; 
	protected boolean _bSup ;
	protected boolean _bInfEgal ; 
	protected boolean _bSupEgal ;
	
	protected LdvNum  _NormalValue      = new LdvNum() ;
	protected LdvNum  _UpperNormalValue = new LdvNum() ;
	protected LdvNum  _LowerNormalValue = new LdvNum() ;
	
	public LdvNumStorage()
	{
		init() ;
	}
	
	public LdvNumStorage(String sNum, String sFormat, String sUnit)
	{
		init() ;
		
		_sFormat = sFormat ;
		_sUnit   = sUnit ;
		
		setNumInformation(sNum) ;
	}
	
	public LdvNumStorage(LdvModelNode modelNode)
	{
		init() ;
		
		if (null == modelNode)
			return ;
		
		_sNum    = modelNode.getComplement() ;
		_sFormat = modelNode.getLexicon() ;
		_sUnit   = modelNode.getUnit() ; 
	}
	
	public LdvNumStorage(LdvNumStorage model) 
	{
		initFromModel(model) ; 
	}
	
	/**
	*  Equivalent of = operator
	*  
	*  @param model Object to initialize from 
	**/
	public void initFromModel(LdvNumStorage model)
	{
		init() ;
		
		if (null == model)
			return ;
		
		_sNum      = model._sNum ;
		_sNumInf   = model._sNumInf ; 
		_sNumSup   = model._sNumSup ;
		
		_dValue    = model._dValue ; 
		_dInfValue = model._dInfValue ; 
		_dSupValue = model._dSupValue ;
		
		_sFormat   = model._sFormat ;
		_sUnit     = model._sUnit ;
		
		_bExact    = model._bExact ; 
		_bInf      = model._bInf ; 
		_bSup      = model._bSup ;
		_bInfEgal  = model._bInfEgal ; 
		_bSupEgal  = model._bSupEgal ;
		
		_NormalValue.initFromModel(model._NormalValue) ;
		_UpperNormalValue.initFromModel(model._UpperNormalValue) ;
		_LowerNormalValue.initFromModel(model._LowerNormalValue) ; 
	}
	
	/**
	*  Reset all object information to default values
	*  
	**/
	public void init()
	{
		initNum() ;
		
 		_sFormat = "" ;
		_sUnit   = "" ;
	}
	
	/**
	*  Reset object information dedicated to value (or interval) description to default values
	*  
	**/
	public void initNum()
	{
		_sNum      = "" ;
		_sNumInf   = "" ;
		_sNumSup   = "" ;
		_dValue    = 0.0d ; 
		_dInfValue = 0.0d ; 
		_dSupValue = 0.0d ;
		_bExact    = false ; 
		_bInf      = false ; 
		_bSup      = false ;
		_bInfEgal  = false ; 
		_bSupEgal  = false ;
		
		_NormalValue.reset() ; 
		_UpperNormalValue.reset() ;
		_LowerNormalValue.reset() ;
	}
	
	private void initValue(String sRawData)
	{
		if ((null == sRawData) || "".equals(sRawData))
			return ;
		
		_sNum   = sRawData ;
		_dValue = Double.parseDouble(sRawData) ;
		_bExact = true ;
	}
	
  private void initInfValue(String sRawData)
  {
		if ((null == sRawData) || "".equals(sRawData))
			return ;
		
		_sNumInf   = sRawData ;
		_dInfValue = Double.parseDouble(sRawData) ;
		_bInfEgal  = true ;
	}
  
  private void initSupValue(String sRawData)
  {
		if ((null == sRawData) || "".equals(sRawData))
			return ;
		
		_sNumSup   = sRawData ;
		_dSupValue = Double.parseDouble(sRawData) ;
		_bSupEgal  = true ;
	}
	
	/**
	*  Parse a String in order to set object information dedicated to value (or interval) description
	*  
	*  @param  sNum String that contains information to be parsed, can be in the forms (where '[' stands for "<code>&lt;=</code>" and ']' stands for "<code>&gt;=</code>"): "<code>N</code>", "<code>&lt;N</code>", "<code>[N</code>", "<code>&gt;N</code>", "<code>]N</code>", "<code>N1&lt;&lt;N2</code>", "<code>N1[&lt;N2</code>", "<code>N1&lt;[N2</code>", "<code>N1[[N2</code>"
	**/
	public void setNumInformation(String sNum)
	{
		initNum() ;
		
		if ((null == sNum) || "".equals(sNum))
			return ;
		
		int iNumLen = sNum.length() ;

		// Check separators that involve both a min and a max
		//
		int iPos = sNum.indexOf("<<") ;
	  if (-1 == iPos)
	  {
	  	iPos = sNum.indexOf("[<") ;
	    if (-1 == iPos)
	    {
	    	iPos = sNum.indexOf("<[") ;
	      if (-1 == iPos)
	      {
	      	iPos = sNum.indexOf("[[") ;
	        if (-1 != iPos)
	        {
	        	_bInfEgal = true ;
	        	_bSupEgal = true ;
	        }
	      }
	      else
	      	_bSupEgal = true ;
	    }
	    else
	    	_bInfEgal = true ;
	  }

	  if (-1 != iPos)
		{
	    if (iPos > 0)
	    	initInfValue(sNum.substring(0, iPos)) ;
	    if (iPos < iNumLen - 2)
	    	initSupValue(sNum.substring(iPos + 2, iNumLen)) ;
	    
	    return ;
	  }
	  //
	  // Value of the "<1" kind
	  //
	  if ('<' == sNum.charAt(0))
	  {
	  	initSupValue(sNum.substring(1, iNumLen)) ;
	  	return ;
	  }
	  //
	  // Value of the "<=1" kind
	  //
	  if ('[' == sNum.charAt(0))
	  {
	  	initSupValue(sNum.substring(1, iNumLen)) ;
	  	_bSupEgal = true ;
	  	return ;
	  }
	  //
	  // Value of the ">1" kind
	  //
	  if ('>' == sNum.charAt(0))
	  {
	  	initInfValue(sNum.substring(1, iNumLen)) ;
	  	return ;
	  }
	  //
	  // Value of the ">=1" kind
	  //
	  if (']' == sNum.charAt(0))
	  {
	  	initInfValue(sNum.substring(1, iNumLen)) ;
	  	_bInfEgal = true ;
	  	return ;
	  }
	  
	  //
	  // Exact value (not an interval)
	  //
	  initValue(sNum) ;
	}
	
	/**
	 * Return <code>true</code> if there is no exact information, no min information and no max information 
	 * 
	 * @return <code>true</code> if empty, <code>false</code> if not
	 * 
	 **/
	public boolean isEmpty() {
		return (false == _bExact) && (false == _bInf) && (false ==_bSup) ;
	}

	public String getNum() {
		return _sNum ;
	}
	public void set_sNum(String sNum) {
		_sNum = sNum ;
	}

	public String getNumInf() {
		return _sNumInf ;
	}
	public void setNumInf(String sNumInf) {
		_sNumInf = sNumInf ;
	}

	public String getNumSup() {
		return _sNumSup ;
	}
	public void setNumSup(String sNumSup) {
		_sNumSup = sNumSup ;
	}

	public double getValue() {
		return _dValue ;
	}
	public void setValue(double dValue) {
		_dValue = dValue ;
	}

	public double getInfValue() {
		return _dInfValue ;
	}
	public void setInfValue(double dInfValue) {
		_dInfValue = dInfValue ;
	}

	public double getSupValue() {
		return _dSupValue ;
	}
	public void set_dSupValue(double dSupValue) {
		_dSupValue = dSupValue ;
	}

	public String getFormat() {
		return _sFormat ;
	}
	public void setFormat(String sFormat) {
		_sFormat = sFormat ;
	}

	public String getUnit() {
		return _sUnit ;
	}
	public void setUnit(String sUnit) {
		_sUnit = sUnit ;
	}

	public boolean isExact() {
		return _bExact ;
	}
	public void set_bExact(boolean bExact) {
		_bExact = bExact ;
	}

	public boolean isInf() {
		return _bInf ;
	}
	public void set_bInf(boolean bInf) {
		_bInf = bInf ;
	}

	public boolean isSup() {
		return _bSup ;
	}
	public void setSup(boolean bSup) {
		_bSup = bSup ;
	}

	public boolean isInfEgal() {
		return _bInfEgal ;
	}
	public void setInfEgal(boolean bInfEgal) {
		_bInfEgal = bInfEgal ;
	}

	public boolean isSupEgal() {
		return _bSupEgal ;
	}
	public void setSupEgal(boolean bSupEgal) {
		_bSupEgal = bSupEgal ;
	}
		
	public void setNormalValue(LdvNum normal) {
		_NormalValue.initFromModel(normal) ;
	}

	public void setLowerNormalValue(LdvNum lowerNormal) {
		_LowerNormalValue.initFromModel(lowerNormal) ;
	}

	public void setUpperNormalValue(LdvNum upperNormal) {
		_UpperNormalValue.initFromModel(upperNormal) ;
	}
}
