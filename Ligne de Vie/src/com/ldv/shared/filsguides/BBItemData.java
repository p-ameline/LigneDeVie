package com.ldv.shared.filsguides;

import com.ldv.shared.util.MiscellanousFcts;

public class BBItemData {

	protected String _sPath ;
	
	protected String _sComment ;
	protected String _sDialogFile ;
	protected String _sDialogName ;
	protected String _sOpenDialog ;
	protected String _sFunctionFile ;
	protected String _sFunctionName ;
	protected String _sLevelShift ;
	protected String _sSons ;
	protected String _sExclusions ;
	protected String _sAutomatic ;
	protected String _sActiveWhenEmpty ;
	protected String _sUnicity ;
	protected String _sMandatory ;
	protected String _sSort ;
	
	public BBItemData() {
		init() ;
	}
	
	public BBItemData(final BBItemData src) {
		initFromModel(src) ;
	}
	
	public boolean IsAutomatic() {
		return ((false == "".equals(_sAutomatic)) && 
				              ((_sAutomatic.charAt(0) == 'Y') || (_sAutomatic.charAt(0) == 'y') ||
				               (_sAutomatic.charAt(0) == 'O') || (_sAutomatic.charAt(0) == 'o'))) ; 
	}
	
	public boolean IsActiveWhenEmpty() {
		return ("".equals(_sActiveWhenEmpty) || 
				              ((_sActiveWhenEmpty.charAt(0) != 'N') && (_sActiveWhenEmpty.charAt(0) != 'n'))) ; 
	}
	
	public boolean IsUnique() {
		return ("".equals(_sUnicity) || 
				              ((_sUnicity.charAt(0) != 'N') && (_sUnicity.charAt(0) != 'n'))) ; 
	}
	
	public boolean OpensDialog() {
		return ((false == "".equals(_sOpenDialog)) && 
				              ((_sOpenDialog.charAt(0) == 'Y') || (_sOpenDialog.charAt(0) == 'y') ||
				               (_sOpenDialog.charAt(0) == 'O') || (_sOpenDialog.charAt(0) == 'o'))) ; 
	}
	
	public boolean OpensArchetype() {
		return ((false == "".equals(_sOpenDialog)) && 
				              ((_sOpenDialog.charAt(0) == 'A') || (_sOpenDialog.charAt(0) == 'a'))) ; 
	}
	
	public boolean IsMandatory() {
		return ((false == "".equals(_sMandatory)) && 
				              ((_sMandatory.charAt(0) == 'Y') || (_sMandatory.charAt(0) == 'y') ||
				               (_sMandatory.charAt(0) == 'O') || (_sMandatory.charAt(0) == 'o'))) ; 
	}
	
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBItemData src)
	{
		init() ;
		
		if (null == src)
			return ;
		
		_sPath            = src._sPath ;
		_sComment         = src._sComment ;
  	_sDialogFile      = src._sDialogFile ;
  	_sDialogName      = src._sDialogName ;
  	_sOpenDialog      = src._sOpenDialog ;
  	_sFunctionFile    = src._sFunctionFile ;
  	_sFunctionName    = src._sFunctionName ;
  	_sLevelShift      = src._sLevelShift ;
  	_sSons            = src._sSons ;
  	_sExclusions      = src._sExclusions ;
  	_sAutomatic       = src._sAutomatic ;
  	_sActiveWhenEmpty = src._sActiveWhenEmpty ;
  	_sUnicity         = src._sUnicity ;
  	_sMandatory       = src._sMandatory ;
  	_sSort            = src._sSort ;
	}
  
  public void init() 
  {
  	_sPath            = "" ;
  	_sComment         = "" ;
  	_sDialogFile      = "" ;
  	_sDialogName      = "" ;
  	_sOpenDialog      = "" ;
  	_sFunctionFile    = "" ;
  	_sFunctionName    = "" ;
  	_sLevelShift      = "" ;
  	_sSons            = "" ;
  	_sExclusions      = "" ;
  	_sAutomatic       = "" ;
  	_sActiveWhenEmpty = "" ;
  	_sUnicity         = "" ;
  	_sMandatory       = "" ;
  	_sSort            = "" ;
  }
  
  public String getPath() {
		return _sPath ;
	}
	public void setPath(final String sPath) {
		_sPath = sPath ;
	}
  
	public String getComment() {
		return _sComment ;
	}
	public void setComment(String sComment) {
		_sComment = sComment;
	}
	
	public String getDialogFile() {
		return _sDialogFile ;
	}
	public void setDialogFile(String sDialogFile) {
		_sDialogFile = sDialogFile ;
	}
	
	public String getDialogName() {
		return _sDialogName ;
	}
	public void setDialogName(String sDialogName) {
		_sDialogName = sDialogName;
	}
	public boolean isDialogNameIdem() {
		return "__IDEM".equals(_sDialogName) ;
	}
	public boolean isDialogNameAuto() {
		return "__AUTO".equals(_sDialogName) ;
	}
	
	public String getOpenDialog() {
		return _sOpenDialog ;
	}
	public void setOpenDialog(String sOpenDialog) {
		_sOpenDialog = sOpenDialog ;
	}
	public void setOpenDialog(boolean bOpenDialog) {
		if (bOpenDialog)
			_sOpenDialog = "O" ;
		else
			_sOpenDialog = "N" ;
	}
	public void setOpenArchetype(boolean bOpenArchetype) {
		if (bOpenArchetype)
			_sOpenDialog = "A" ;
		else
			_sOpenDialog = "N" ;
	}
	
	public String getFunctionFile() {
		return _sFunctionFile ;
	}
	public void setFunctionFile(String sFunctionFile) {
		_sFunctionFile = sFunctionFile ;
	}
	
	public String getFunctionName() {
		return _sFunctionName ;
	}
	public void setFunctionName(String sFunctionName) {
		_sFunctionName = sFunctionName ;
	}

	public String getLevelShift() {
		return _sLevelShift ;
	}
	public void setLevelShift(String sLevelShift) {
		_sLevelShift = sLevelShift ;
	}
	
	public String getSons() {
		return _sSons ;
	}
	public void setSons(String sSons) {
		_sSons = sSons ;
	}
	public boolean hasSons() {
		return (false == "".equals(_sSons.trim())) ;
	}
	
	public String getExclusions() {
		return _sExclusions ;
	}
	public void setExclusions(String sExclusions) {
		_sExclusions = sExclusions ;
	}
	
	public String getAutomatic() {
		return _sAutomatic ;
	}
	public void setAutomatic(String sAutomatic) {
		_sAutomatic = sAutomatic ;
	}

	public String getActiveWhenEmpty() {
		return _sActiveWhenEmpty ;
	}
	public void setActiveWhenEmpty(String sActiveWhenEmpty) {
		_sActiveWhenEmpty = sActiveWhenEmpty ;
	}
	public void setActiveWhenEmpty(boolean bActiveWhenEmpty) {
		if (bActiveWhenEmpty)
			_sActiveWhenEmpty = "O" ;
		else
			_sActiveWhenEmpty = "N" ;
	}

	public String getUnicity() {
		return _sUnicity ;
	}
	public void setUnicity(String sUnicity) {
		_sUnicity = sUnicity ;
	}

	public String getMandatory() {
		return _sMandatory ;
	}
	public void setMandatory(String sMandatory) {
		_sMandatory = sMandatory ;
	}

	public String getSort() {
		return _sSort ;
	}
	public void setSort(String sSort) {
		_sSort = sSort ;
	}
	
	/**
	  * Determine whether two BBItemData are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  itemData BBItemData to compare
	  * 
	  */
	public boolean equals(BBItemData itemData)
	{
		if (this == itemData) {
			return true ;
		}
		if (null == itemData) {
			return false ;
		}
		
		return (MiscellanousFcts.areIdenticalStrings(_sPath,            itemData._sPath)            &&
					  MiscellanousFcts.areIdenticalStrings(_sComment,         itemData._sComment)         &&
					  MiscellanousFcts.areIdenticalStrings(_sDialogFile,      itemData._sDialogFile)      &&
					  MiscellanousFcts.areIdenticalStrings(_sDialogName,      itemData._sDialogName)      &&
					  MiscellanousFcts.areIdenticalStrings(_sOpenDialog,      itemData._sOpenDialog)      &&
					  MiscellanousFcts.areIdenticalStrings(_sFunctionFile,    itemData._sFunctionFile)    &&
					  MiscellanousFcts.areIdenticalStrings(_sFunctionName,    itemData._sFunctionName)    &&
					  MiscellanousFcts.areIdenticalStrings(_sLevelShift,      itemData._sLevelShift)      &&
					  MiscellanousFcts.areIdenticalStrings(_sSons,            itemData._sSons)            &&
					  MiscellanousFcts.areIdenticalStrings(_sExclusions,      itemData._sExclusions)      &&
					  MiscellanousFcts.areIdenticalStrings(_sAutomatic,       itemData._sAutomatic)       &&
					  MiscellanousFcts.areIdenticalStrings(_sActiveWhenEmpty, itemData._sActiveWhenEmpty) &&
					  MiscellanousFcts.areIdenticalStrings(_sUnicity,         itemData._sUnicity)         &&
					  MiscellanousFcts.areIdenticalStrings(_sMandatory,       itemData._sMandatory)       &&
					  MiscellanousFcts.areIdenticalStrings(_sSort,            itemData._sSort)) ;
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
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final BBItemData itemData = (BBItemData) o ;

		return equals(itemData) ;
	}
}
