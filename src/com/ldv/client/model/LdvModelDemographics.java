package com.ldv.client.model;

import com.ldv.shared.model.LdvTime;

public class LdvModelDemographics implements Cloneable
{
	enum GenderType { undefined, male, female } ;
	
	protected String     _sFirstName ;
	protected String     _sMiddleName ;
	protected String     _sName ;
	protected String     _sMaidenName ;
	protected LdvTime    _tBirthDate ;
	protected GenderType _eGender ;
		
	public LdvModelDemographics()
	{
		_tBirthDate = new LdvTime(0) ;
		reset() ;
	}
	
	public void reset()
	{	
		_sFirstName  = "" ;
		_sMiddleName = "" ;
		_sName       = "" ;
		_sMaidenName = "" ;
		_eGender     = GenderType.undefined ;
		_tBirthDate.init() ;
	}
	
	public void deepCopy(LdvModelDemographics src)
	{
		_sFirstName  = src._sFirstName ;
		_sMiddleName = src._sMiddleName ;
		_sName       = src._sName ;
		_sMaidenName = src._sMaidenName ;
		_eGender     = src._eGender ;
		_tBirthDate  = src._tBirthDate ;
	}

	public String getFirstName() {
		return _sFirstName ;
	}
	public void setFirstName(String sFirstName) {
		_sFirstName = sFirstName ;
	}

	public String getMiddleName() {
		return _sMiddleName ;
	}
	public void setMiddleName(String sMiddleName) {
		_sMiddleName = sMiddleName ;
	}

	public String getName() {
		return _sName ;
	}
	public void setName(String sName) {
		_sName = sName ;
	}

	public String getMaidenName() {
		return _sMaidenName ;
	}
	public void setMaidenName(String sMaidenName) {
		_sMaidenName = sMaidenName ;
	}

	public LdvTime getBirthDate() {
		return _tBirthDate ;
	}
	public void setBirthDate(LdvTime tBirthDate) {
		_tBirthDate = tBirthDate ;
	}

	public GenderType getGender() {
		return _eGender ;
	}
	public void setGender(GenderType eGender) {
		_eGender = eGender;
	}
}
