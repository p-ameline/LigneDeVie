package com.ldv.shared.database ;

import com.google.gwt.user.client.rpc.IsSerializable ;

import com.ldv.shared.util.MiscellanousFcts;

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
public class Agenda implements IsSerializable 
{
	private int    _iAgendaId ;
	private String _sLdvId ;
	private int    _iIsPro ;
	private int    _iUEP ;
	private String _sAgendaType ;
	private String _sAgendaUrl ;
	private String _sAgendaIdentifier ;
	private String _sAgendaPassword ;
	
	//
	//
	public Agenda() 
	{
		reset() ;
	}
		
	/**
	 * Copy constructor 
	 */
	public Agenda(final Agenda model) {
		initFromAgenda(model) ;
	}
	
	/**
	 * Initialize this object from another object of the same kind 
	 */
	public void initFromAgenda(final Agenda model)
	{
		reset() ;
		
		if (null == model)
			return ;
		
		_iAgendaId         = model._iAgendaId ;
		_sLdvId            = model._sLdvId ;
		_iIsPro            = model._iIsPro ;
		_iUEP              = model._iUEP ;
		_sAgendaType       = model._sAgendaType ;
		_sAgendaUrl        = model._sAgendaUrl ;
		_sAgendaIdentifier = model._sAgendaIdentifier ;
		_sAgendaPassword   = model._sAgendaPassword ;
	}
	
	/**
	 * Reset all information 
	 */
	public void reset() 
	{
		_iAgendaId         = 0 ;
		_sLdvId            = "";
		_iIsPro            = 0 ;
		_iUEP              = 0 ;
		_sAgendaType       = "" ;
		_sAgendaUrl        = "" ;
		_sAgendaIdentifier = "" ;
		_sAgendaPassword   = "" ;
	}
			
	// getter and setter
	//
	public int getAgendaId() {
		return _iAgendaId ;
	}
	public void setAgendaId(final int id) {
		_iAgendaId = id ;
	}

	public String getLdvId() {
		return _sLdvId ;
	}
	public void setLdvId(final String sLdvId) {
		_sLdvId = sLdvId ;
	} 

	public int isPro() {
		return _iIsPro ;
	}
	public void setIsPro(final int ip) {
		_iIsPro = ip ;
	}
	
	public int getUEP() {
		return _iUEP ;
	}
	public void setUEP(final int iUEP) {
		_iUEP = iUEP ;
	}
	
	public String getAgendaType() {
		return _sAgendaType ;
	}
	public void setAgendaType(final String sAgendaType) {
		_sAgendaType = sAgendaType ;
	}

	public String getAgendaUrl() {
		return _sAgendaUrl ;
	}
	public void setAgendaUrl(final String sAgendaUrl) {
		_sAgendaUrl = sAgendaUrl ;
	}

	public String getAgendaIdentifier() {
		return _sAgendaIdentifier ;
	}
	public void setAgendaIdentifier(final String sAgendaIdentifier) {
		_sAgendaIdentifier = sAgendaIdentifier ;
	}

	public String getAgendaPassword() {
		return _sAgendaPassword ;
	}
	public void setAgendaPassword(final String sAgendaPassword) {
		_sAgendaPassword = sAgendaPassword ;
	}
	
	/**
	  * Determine whether two Agenda are exactly similar
	  * 
	  * @param  other Agenda to compare with
	  * @return true if all data are the same, false if not
	  */
	public boolean equals(final Agenda other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (_iAgendaId == other._iAgendaId)  &&
				   (_iIsPro    == other._iIsPro)     &&
				   (_iUEP      == other._iUEP)       &&
				   MiscellanousFcts.areStringsEqual(_sLdvId,            other._sLdvId)            && 
				   MiscellanousFcts.areStringsEqual(_sAgendaType,       other._sAgendaType)       &&
				   MiscellanousFcts.areStringsEqual(_sAgendaUrl,        other._sAgendaUrl)        &&
				   MiscellanousFcts.areStringsEqual(_sAgendaIdentifier, other._sAgendaIdentifier) &&
				   MiscellanousFcts.areStringsEqual(_sAgendaPassword,   other._sAgendaPassword) ;
	}

	/**
	  * Determine whether this Agenda is exactly similar to another object
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare with
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if ((null == o) || (getClass() != o.getClass())) {
			return false ;
		}

		final Agenda agenda = (Agenda) o ;

		return equals(agenda) ;
	}
}
