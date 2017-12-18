package com.ldv.server.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.model.LdvStatus;
import com.ldv.shared.rpc.LdvGetStatusAction;
import com.ldv.shared.rpc.LdvGetStatusResult;
import com.ldv.server.DbParameters;
import com.ldv.server.model.LdvFilesManager;
import com.ldv.server.model.LdvSessionsManager;
import com.ldv.server.model.LdvXmlGraph;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GetStatusHandler extends LdvActionHandler<LdvGetStatusAction, LdvGetStatusResult>
{
	@Inject
	public GetStatusHandler(final Log logger,
                          final Provider<ServletContext> servletContext,       
                          final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}

	/**
	  * Constructor dedicated to unit tests 
	  */
	public GetStatusHandler()
	{
		super() ;
	}
	
	@Override
	public LdvGetStatusResult execute(final LdvGetStatusAction action,
       			                        final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sLdvIdentifier  = action.getSessionElements().getLdvIdentifier() ;
   		String sUserIdentifier = action.getSessionElements().getUserIdentifier() ;
   		String sToken          = action.getSessionElements().getToken() ;
   		
   		// First, we have to check if this token is valid
   		//
   		LdvSessionsManager sessionManager = new LdvSessionsManager() ;
   		if (false == sessionManager.isValidToken(sLdvIdentifier, sUserIdentifier, sToken))
   			return new LdvGetStatusResult(false, LdvStatus.AvailabilityLevel.UNKNOWN) ;
   		
   		// We need to know the name of links file in order to check if it is present in working directory
   		//
   		LdvXmlGraph xmlGraph = new LdvXmlGraph(LdvGraphConfig.COLLECTIVE_SERVER, sLdvIdentifier, sUserIdentifier) ;
   		String sLinksFileName = xmlGraph.getLinksFileName() ;
   		
   		// If links file name exists, it means that graph is in working position
   		//
   		LdvFilesManager filesManager = new LdvFilesManager(sLdvIdentifier, DbParameters._sFilesDir, DbParameters._sDirSeparator) ;
   		if (filesManager.existWorkingDirectoryFile(sLinksFileName))
   			return new LdvGetStatusResult(true, LdvStatus.AvailabilityLevel.OPEN) ;
   		
   		// If not in a working position, we have to check if ciphered file is there
   		//
   		if (filesManager.existCipheredFile())
   			return new LdvGetStatusResult(true, LdvStatus.AvailabilityLevel.HERE) ;
   		
   		// No material there!
   		//
   		return new LdvGetStatusResult(true, LdvStatus.AvailabilityLevel.OUT) ;
		}
		catch (Exception cause) 
		{
			_logger.error("GetStatusHandler exception", cause) ;
			throw new ActionException(cause);
		}
  }
	
	@Override
	public void rollback(final LdvGetStatusAction action,
        							 final LdvGetStatusResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<LdvGetStatusAction> getActionType()
	{
		return LdvGetStatusAction.class;
	}
}
