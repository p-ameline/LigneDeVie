package com.ldv.server.database ;

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
public class Session implements IsSerializable 
{
	private int    _iSessionId ;
	private String _sLdvId ;
	private String _sUserLdvId ;
	private String _sToken ;
	private String _sDateTimeOpen ;
	private String _sDateTimeClose ;
	private String _sDateTimeLastBeat ;
	
	//
	//
	public Session() 
	{
		reset() ;
	}
		
	public Session(int iSessionId, String sLdvId, String sUserLdvId, String sToken, String sDateTimeOpen, String sDateTimeClose, String sDateTimeLastBeat) 
	{
		_iSessionId        = iSessionId ;
		_sLdvId            = sLdvId ;
		_sUserLdvId        = sUserLdvId ;
		_sToken            = sToken ;
		_sDateTimeOpen     = sDateTimeOpen ;
		_sDateTimeClose    = sDateTimeClose ;
		_sDateTimeLastBeat = sDateTimeLastBeat ;
	}
	
	public void initFromPerson(Session session)
	{
		reset() ;
		
		if (null == session)
			return ;
		
		_iSessionId        = session._iSessionId ;
		_sLdvId            = session._sLdvId ;
		_sUserLdvId        = session._sUserLdvId ;
		_sToken            = session._sToken ;
		_sDateTimeOpen     = session._sDateTimeOpen ;
		_sDateTimeClose    = session._sDateTimeClose ;
		_sDateTimeLastBeat = session._sDateTimeLastBeat ;
	}
			
	public void reset() 
	{
		_iSessionId        = -1 ;
		_sLdvId            = "" ;
		_sUserLdvId        = "" ;
		_sToken            = "" ;
		_sDateTimeOpen     = "" ;
		_sDateTimeClose    = "" ;
		_sDateTimeLastBeat = "" ;
	}
			
	// getter and setter
	//
	public int getSessionId() {
		return _iSessionId ;
	}
	public void setSessionId(int id) {
		_iSessionId = id ;
	}

	public String getUserLdvId()
	{
		return _sUserLdvId ;
	}
	public void setUserLdvId(String sUserLdvId)
	{
		_sUserLdvId = sUserLdvId ;
	}
	
	public String getLdvId()
	{
		return _sLdvId ;
	}
	public void setLdvId(String sLdvId)
	{
		_sLdvId = sLdvId ;
	}

	public String getToken()
	{
		return _sToken ;
	}
	public void setToken(String sToken)
	{
		_sToken = sToken ;
	}

	public String getDateTimeOpen()
	{
		return _sDateTimeOpen ;
	}
	public void setDateTimeOpen(String sDateTimeOpen)
	{
		_sDateTimeOpen = sDateTimeOpen ;
	}
	
	public String getDateTimeClose()
	{
		return _sDateTimeClose ;
	}
	public void setDateTimeClose(String sDateTimeClose)
	{
		_sDateTimeClose = sDateTimeClose ;
	}
	
	public String getDateTimeLastBeat()
	{
		return _sDateTimeLastBeat ;
	}
	public void setDateTimeLastBeat(String sDateTimeLastBeat)
	{
		_sDateTimeLastBeat = sDateTimeLastBeat ;
	}
}
