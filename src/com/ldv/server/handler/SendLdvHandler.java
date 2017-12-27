package com.ldv.server.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.shared.database.Person;
import com.ldv.shared.rpc.SendLoginAction;
import com.ldv.shared.rpc.SendLoginResult;
import com.ldv.server.Logger;
import com.ldv.server.model.LdvBackOfficePerson;
import com.ldv.server.model.LdvSessionsManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SendLdvHandler extends LdvActionHandler<SendLoginAction, SendLoginResult>
{
	@Inject
	public SendLdvHandler(final Log logger,
       final Provider<ServletContext> servletContext,       
       final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}

	/**
	  * Constructor dedicated to unit tests 
	  */
	public SendLdvHandler()
	{
		super() ;
	}
	
	@Override
	public SendLoginResult execute(final SendLoginAction action,
       					                 final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sIdentifier = action.getIdentifier() ;
   		String sPassword   = action.getEncryptedPassword() ;
   		
   		// Get identifier into "persons" database
   		//
   		Person user = getLdvId(sIdentifier, sPassword) ;
   		if (null == user)
   			return new SendLoginResult("", getInfoString("Wrong identifiers"), null) ;
   		
   		// Open a session into "sessions" database
   		//
   		LdvSessionsManager sessionManager = new LdvSessionsManager() ;
   		boolean bSessionCreated = sessionManager.createNewSession(user.getLdvId(), user.getLdvId()) ;
   		if (false == bSessionCreated)
   			return new SendLoginResult("", getInfoString("Cannot create session"), null) ;
   				
   		// Return Token
   		//
   		return new SendLoginResult(sessionManager.getToken(), "", user) ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to send message", cause);
   
			throw new ActionException(cause);
		}
  }
	
	private Person getLdvId(String sLogin, String sPassword)
	{
		if (sLogin.equals("") && sPassword.equals(""))
		{
			Logger.trace("SendLdvHandler.getLdvId: empty parameters", -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}		
		if (sLogin.equals("") || sPassword.equals(""))
		{
			if      (sLogin.equals(""))
				Logger.trace("SendLdvHandler.getLdvId: empty login for password=" + sPassword, -1, Logger.TraceLevel.ERROR) ;
			else if (sPassword.equals(""))
				Logger.trace("SendLdvHandler.getLdvId: empty password for login=" + sLogin, -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
		
		LdvBackOfficePerson backOfficePerson = new LdvBackOfficePerson() ;
		backOfficePerson.initFromPseudoAndPassword(sLogin, sPassword) ;
		
		if ((false == sLogin.equals(backOfficePerson.getPseudo())) || 
				(false == sPassword.equals(backOfficePerson.getPassword())))
		{
			Logger.trace("SendLdvHandler.getLdvId: bad user found for pseudo " + sLogin + " and pass " + sPassword, -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
				
		return new Person(backOfficePerson.getPerson()) ;
	}
	
	@Override
	public void rollback(final SendLoginAction action,
        							 final SendLoginResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<SendLoginAction> getActionType()
	{
		return SendLoginAction.class;
	}
}
