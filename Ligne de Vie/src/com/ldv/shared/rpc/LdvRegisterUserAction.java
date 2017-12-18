package com.ldv.shared.rpc;

import net.customware.gwt.dispatch.shared.Action;

public class LdvRegisterUserAction implements Action<LdvRegisterUserResult> 
{	
	private String _sPseudo ;
	private String _sPassword ;
	private String _sEmail ;
	private String _sLanguage ;
	
	public LdvRegisterUserAction() {
		super() ;
		
		_sPseudo    = "" ;
		_sPassword  = "" ;
		_sEmail     = "" ;
		_sLanguage  = "" ;
	}
	
	public LdvRegisterUserAction(String sPseudo, String sPassword, String sEmail, String sLanguage) 
	{
		_sPseudo    = sPseudo ;
		_sPassword  = sPassword ;
		_sEmail     = sEmail ;
		_sLanguage  = sLanguage ;
	}
	
	public String getPseudo() {
		return _sPseudo ;
	}
	public void setPseudo(String sPseudo) {
		_sPseudo = sPseudo ;
	}

	public String getPassword() {
		return _sPassword ;
	}
	public void setPassword(String sPassword) {
		_sPassword = sPassword ;
	}
	
	public String getEmail() {
		return _sEmail ;
	}
	public void setEmail(String sEmail) {
		_sEmail = sEmail ;
	}

	public String getLanguage() {
		return _sLanguage ;
	}
	public void setLanguage(String sLanguage) {
		_sLanguage = sLanguage ;
	}
}
