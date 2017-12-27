package com.ldv.shared.database ;

import com.google.gwt.user.client.rpc.IsSerializable ;

/**
 * Person.java
 *
 * The Person class represents object type user in database
 * 
 * Created: 19 Jul 2011
 *
 * Author: PA
 * 
 */
public class Person implements IsSerializable 
{
	private int    _iPersonId ;
	private String _sPseudo ;
	private String _sPassword ;
	private String _sLanguage ;
	private String _sLdvId ;
	private String _sContractType ;
	private String _sContractEndDate ;
	
	//
	//
	public Person() 
	{
		reset() ;
	}
		
	public Person(int iPersonId, final String sPseudo, final String sPassword, final String sLanguage, final String sLdvId) 
	{
		_iPersonId = iPersonId ;
		_sPseudo   = sPseudo ;
		_sPassword = sPassword ;
		_sLanguage = sLanguage ;
		_sLdvId    = sLdvId ;
	}
	
	public Person(Person modelPerson) {
		initFromPerson(modelPerson) ;
	}
	
	public void initFromPerson(Person person)
	{
		reset() ;
		
		if (null == person)
			return ;
		
		_iPersonId        = person._iPersonId ;
		_sPseudo          = person._sPseudo ;
		_sPassword        = person._sPassword ;
		_sLanguage        = person._sLanguage ;
		_sLdvId           = person._sLdvId ;
		_sContractType    = person._sContractType ;
		_sContractEndDate = person._sContractEndDate ;
	}
			
	public void reset() 
	{
		_iPersonId        = -1 ;
		_sPseudo          = "" ;
		_sPassword        = "" ;
		_sLanguage        = "" ;
		_sLdvId           = "" ;
		_sContractType    = "" ;
		_sContractEndDate = "" ;
	}
			
	// getter and setter
	//
	public int getPersonId() {
		return _iPersonId ;
	}
	public void setPersonId(final int id) {
		_iPersonId = id ;
	}

	public String getPseudo() {
		return _sPseudo ;
	}
	public void setPseudo(final String sPseudo) {
		_sPseudo = sPseudo ;
	} 

	public String getPassword() {
		return _sPassword ;
	}
	public void setPassword(final String sPassword) {
		_sPassword = sPassword ;
	}

	public String getLanguage() {
		return _sLanguage ;
	}
	public void setLanguage(final String sLanguage) {
		_sLanguage = sLanguage ;
	}

	public String getLdvId() {
		return _sLdvId ;
	}
	public void setLdvId(final String sLdvId) {
		_sLdvId = sLdvId ;
	}

	public String getContractType() {
		return _sContractType ;
	}
	public void setContractType(final String sContractType) {
		_sContractType = sContractType ;
	}

	public String getContractEndDate() {
		return _sContractEndDate ;
	}
	public void setContractEndDate(final String sContractEndDate) {
		_sContractEndDate = sContractEndDate ;
	}
}
