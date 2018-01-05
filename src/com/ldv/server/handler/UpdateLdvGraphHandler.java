package com.ldv.server.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.rpc.GetGraphAction;
import com.ldv.shared.rpc.GetGraphResult;
import com.ldv.shared.rpc.UpdateGraphAction;
import com.ldv.shared.rpc.UpdateGraphResult;
import com.ldv.server.DbParameters;
import com.ldv.server.graph.LdvModelGraphHandler;
import com.ldv.server.model.LdvSessionsManager;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class UpdateLdvGraphHandler extends LdvActionHandler<UpdateGraphAction, UpdateGraphResult>
{
	@Inject
	public UpdateLdvGraphHandler(final Log logger,
                            final Provider<ServletContext> servletContext,       
                            final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}

	/**
	  * Constructor dedicated to unit tests 
	  */
	public UpdateLdvGraphHandler()
	{
		super() ;
	}
	
	@Override
	public UpdateGraphResult execute(final UpdateGraphAction action,
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
   			return new UpdateGraphResult(false, null, "Cannot create session") ;
   			
   		// Get LdV objects
   		//
   		LdvModelGraphHandler modelGraph = new LdvModelGraphHandler(LdvModelGraph.NSGRAPHTYPE.personGraph) ;
   		if (false == modelGraph.openGraph(sLdvIdentifier, sUserIdentifier, DbParameters._sFilesDir, DbParameters._sDirSeparator))
   			return new GetGraphResult(false, null, "Cannot get graph from files") ;
   		
   		return new GetGraphResult(true, modelGraph, "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to open graph", cause) ;
   
			return new GetGraphResult(false, null, "Server error") ;
			
			// throw new ActionException(cause);
		}
  }
		
	@Override
	public void rollback(final GetGraphAction action,
        							 final GetGraphResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<GetGraphAction> getActionType()
	{
		return GetGraphAction.class;
	}
}
