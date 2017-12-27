package com.ldv.server.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.shared.rpc.OpenGraphAction;
import com.ldv.shared.rpc.OpenGraphResult;
import com.ldv.server.DbParameters;
import com.ldv.server.model.LdvFilesManager;
import com.ldv.server.model.LdvSessionsManager;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class OpenLdvGraphHandler extends LdvActionHandler<OpenGraphAction, OpenGraphResult>
{
	@Inject
	public OpenLdvGraphHandler(final Log logger,
       final Provider<ServletContext> servletContext,       
       final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}

	/**
	  * Constructor dedicated to unit tests 
	  */
	public OpenLdvGraphHandler()
	{
		super() ;
	}
	
	@Override
	public OpenGraphResult execute(final OpenGraphAction action,
       					                 final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sLdvIdentifier  = action.getSessionElements().getLdvIdentifier() ;
   		String sUserIdentifier = action.getSessionElements().getUserIdentifier() ;
   		String sToken          = action.getSessionElements().getToken() ;
   		
   		// Check token validity
   		//
   		LdvSessionsManager sessionManager = new LdvSessionsManager() ;
   		boolean bValidSession = sessionManager.isValidToken(sLdvIdentifier, sUserIdentifier, sToken) ;
   		if (false == bValidSession)
   			return new OpenGraphResult(false, "Invalid session") ;
   			
   		String sKey = action.getKey() ;
   		
   		// Get LdV objects
   		//
   		LdvFilesManager filesManager = new LdvFilesManager(sLdvIdentifier, DbParameters._sFilesDir, DbParameters._sDirSeparator) ;
   		if (false == filesManager.openWorkingEnvironment(sKey))
   			return new OpenGraphResult(false, "Can't open graph") ;
   			
   		return new OpenGraphResult(true, "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to send message", cause);
   
			throw new ActionException(cause);
		}
  }

	@Override
	public void rollback(final OpenGraphAction action,
        							 final OpenGraphResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<OpenGraphAction> getActionType()
	{
		return OpenGraphAction.class;
	}
}
