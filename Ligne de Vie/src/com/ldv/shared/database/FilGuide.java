package com.ldv.shared.database ;

import com.google.gwt.user.client.rpc.IsSerializable ;

/**
 * Information from a FilGuide record
 * 
 */
public class FilGuide implements IsSerializable 
{
	private int    _iID ;
	private String _sGroup ;
	private String _sCode ;
	private String _sPath ;
	private String _sComment ;
	private String _sInterfaceLocation ;
	private String _sInterfaceID ;
	private String _sOpenInterface ;
	private String _sFunctionLocation ;
	private String _sFunctionID ;
	private String _sLevelShift ;
	private String _sSons ;
	private String _sExclusion ;
	private String _sAutomatic ;
	private String _sActivatedWhenEmpty ;
	private String _sMandatory ;
	
	//
	//
	public FilGuide() 
	{
		reset() ;
	}
		
	public FilGuide(int iID, String sGroup, String sCode, String sPath, String sComment, String sInterfaceLocation, String sInterfaceID, String sOpenInterface, String sFunctionLocation, String sFunctionID, String sLevelShift, String sSons, String sExclusion, String sAutomatic, String sActivatedWhenEmpty, String sMandatory) 
	{
		_iID                 = iID ;
		_sGroup              = sGroup ;
		_sCode               = sCode ;
		_sPath               = sPath ;
		_sComment            = sComment ;
		_sInterfaceLocation  = sInterfaceLocation ;
		_sInterfaceID        = sInterfaceID ;
		_sOpenInterface      = sOpenInterface ;
		_sFunctionLocation   = sFunctionLocation ;
		_sFunctionID         = sFunctionID ;
		_sLevelShift         = sLevelShift ;
		_sSons               = sSons ;
		_sExclusion          = sExclusion ;
		_sAutomatic          = sAutomatic ;
		_sActivatedWhenEmpty = sActivatedWhenEmpty ;
		_sMandatory          = sMandatory ;
	}
	
	public FilGuide(FilGuide modelPerson) 
	{
		initFromPerson(modelPerson) ;
	}
	
	public void initFromPerson(FilGuide filguide)
	{
		reset() ;
		
		if (null == filguide)
			return ;
		
		_iID                 = filguide._iID ;
		_sGroup              = filguide._sGroup ;
		_sCode               = filguide._sCode ;
		_sPath               = filguide._sPath ;
		_sComment            = filguide._sComment ;
		_sInterfaceLocation  = filguide._sInterfaceLocation ;
		_sInterfaceID        = filguide._sInterfaceID ;
		_sOpenInterface      = filguide._sOpenInterface ;
		_sFunctionLocation   = filguide._sFunctionLocation ;
		_sFunctionID         = filguide._sFunctionID ;
		_sLevelShift         = filguide._sLevelShift ;
		_sSons               = filguide._sSons ;
		_sExclusion          = filguide._sExclusion ;
		_sAutomatic          = filguide._sAutomatic ;
		_sActivatedWhenEmpty = filguide._sActivatedWhenEmpty ;
		_sMandatory          = filguide._sMandatory ;
	}
			
	public void reset() 
	{
		_iID                 = -1 ;
		_sGroup              = "" ;
		_sCode               = "" ;
		_sPath               = "" ;
		_sComment            = "" ;
		_sInterfaceLocation  = "" ;
		_sInterfaceID        = "" ;
		_sOpenInterface      = "" ;
		_sFunctionLocation   = "" ;
		_sFunctionID         = "" ;
		_sLevelShift         = "" ;
		_sSons               = "" ;
		_sExclusion          = "" ;
		_sAutomatic          = "" ;
		_sActivatedWhenEmpty = "" ;
		_sMandatory          = "" ;
	}
			
	// getter and setter
	//
	public int getId() {
		return _iID ;
	}
	public void setId(final int id) {
		_iID = id ;
	}

	public String getGroup() {
		return _sGroup ;
	}
	public void setGroup(final String sGroup) {
		_sGroup = sGroup ;
	} 

	public String getCode() {
		return _sCode ;
	}
	public void setCode(final String sCode) {
		_sCode = sCode ;
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
	public void setComment(final String sComment) {
		_sComment = sComment ;
	}

	public String getInterfaceLocation() {
		return _sInterfaceLocation ;
	}
	public void setInterfaceLocation(final String sInterfaceLocation) {
		_sInterfaceLocation = sInterfaceLocation ;
	}

	public String getInterfaceID() {
		return _sInterfaceID ;
	}
	public void setInterfaceID(final String sInterfaceID) {
		_sInterfaceID = sInterfaceID ;
	}

	public String getOpenInterface() {
		return _sOpenInterface ;
	}
	public void set_sOpenInterface(String sOpenInterface) {
		_sOpenInterface = sOpenInterface ;
	}

	public String getFunctionLocation() {
		return _sFunctionLocation ;
	}
	public void setFunctionLocation(String sFunctionLocation) {
		_sFunctionLocation = sFunctionLocation ;
	}

	public String getFunctionID() {
		return _sFunctionID ;
	}
	public void setFunctionID(String sFunctionID) {
		_sFunctionID = sFunctionID ;
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

	public String getExclusion() {
		return _sExclusion ;
	}
	public void setExclusion(String sExclusion) {
		_sExclusion = sExclusion ;
	}

	public String getAutomatic() {
		return _sAutomatic ;
	}
	public void setAutomatic(String sAutomatic) {
		_sAutomatic = sAutomatic ;
	}

	public String getActivatedWhenEmpty() {
		return _sActivatedWhenEmpty ;
	}
	public void setActivatedWhenEmpty(String sActivatedWhenEmpty) {
		_sActivatedWhenEmpty = sActivatedWhenEmpty ;
	}

	public String getMandatory() {
		return _sMandatory ;
	}
	public void setMandatory(String sMandatory) {
		_sMandatory = sMandatory ;
	}
}
