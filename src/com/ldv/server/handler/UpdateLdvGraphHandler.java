package com.ldv.server.handler;

import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvModelGraph;
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
   			
   		// Write / Update LdV objects
   		//
   		LdvModelGraphHandler modelGraph = new LdvModelGraphHandler(LdvModelGraph.NSGRAPHTYPE.personGraph) ;
   		
   		Vector<LdvGraphMapping> aMappings = new Vector<LdvGraphMapping>() ;
   		
   		if (false == modelGraph.writeGraph(sLdvIdentifier, sUserIdentifier, DbParameters._sFilesDir, DbParameters._sDirSeparator, action.getModifiedGraph(), aMappings))
   			return new UpdateGraphResult(false, null, "Cannot save/update the graph") ;
   		
   		return new UpdateGraphResult(true, aMappings, "") ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to save/update the graph", cause) ;
   
			return new UpdateGraphResult(false, null, "Server error") ;
			
			// throw new ActionException(cause);
		}
  }
	
	@Override
	public void rollback(final UpdateGraphAction action,
        							 final UpdateGraphResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<UpdateGraphAction> getActionType() {
		return UpdateGraphAction.class;
	}
}
