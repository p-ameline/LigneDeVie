package com.ldv.shared.rpc;

import com.ldv.shared.database.Person;
import net.customware.gwt.dispatch.shared.Result;

public class SendLoginResult implements Result 
{
	private String _sessionToken ;
	private Person _user ;
	private String _message ;

	public SendLoginResult(final String sessionToken, final String message, final Person user) 
	{
		super() ;
		
		_sessionToken = sessionToken ;
		_user         = new Person(user) ;
		_message      = message ;
	}

	@SuppressWarnings("unused")
	private SendLoginResult() 
	{
		super() ;
		
		_sessionToken = "" ;
		_user         = new Person() ;
		_message      = "" ;
	}

	public String getSessionToken() {
		return _sessionToken ;
	}

	public Person getUser() {
		return _user ;
	}
	
	public String getMessage() {
		return _message;
	}
}
