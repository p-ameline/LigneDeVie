package com.ldv.server.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import org.apache.commons.logging.Log;

import com.ldv.shared.rpc.CloseSessionAction;
import com.ldv.shared.rpc.CloseSessionResult;
import com.ldv.server.model.LdvSessionsManager;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class CloseSessionHandler extends LdvActionHandler<CloseSessionAction, CloseSessionResult>
{
	@Inject
	public CloseSessionHandler(final Log logger,
                             final Provider<ServletContext> servletContext,       
                             final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}

	/**
	  * Constructor dedicated to unit tests 
	  */
	public CloseSessionHandler()
	{
		super() ;
	}
	
	@Override
	public CloseSessionResult execute(final CloseSessionAction action,
       			                        final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sLdvIdentifier  = action.getSessionElements().getLdvIdentifier() ;
   		String sUserIdentifier = action.getSessionElements().getUserIdentifier() ;
   		String sToken          = action.getSessionElements().getToken() ;
   		
   		// We need a Session Manager
   		//
   		LdvSessionsManager sessionManager = new LdvSessionsManager() ;
   		
   		// First, we have to check if this token is valid
   		//
   		if (false == sessionManager.isValidToken(sLdvIdentifier, sUserIdentifier, sToken))
   			return new CloseSessionResult(false, getInfoString("Invalid token")) ;
   		
   		// Now, we can close current session
   		//
   		sessionManager.closeSession(sLdvIdentifier, sUserIdentifier, sToken) ;
   		   		
   		return new CloseSessionResult(true, "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("CloseSessionResult exception", cause) ;
   
			throw new ActionException(cause);
		}
  }
	
	@Override
	public void rollback(final CloseSessionAction action,
        							 final CloseSessionResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<CloseSessionAction> getActionType()
	{
		return CloseSessionAction.class;
	}
}
