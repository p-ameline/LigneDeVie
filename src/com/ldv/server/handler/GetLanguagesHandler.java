package com.ldv.server.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.rpc.Language;
import com.ldv.shared.rpc.LdvGetLanguagesAction;
import com.ldv.shared.rpc.LdvGetLanguagesResult;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GetLanguagesHandler extends LdvActionHandler<LdvGetLanguagesAction, LdvGetLanguagesResult>
{
	@Inject
	public GetLanguagesHandler(final Log logger,
                             final Provider<ServletContext> servletContext,       
                             final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public GetLanguagesHandler()
	{
		super() ;
	}

	@Override
	public LdvGetLanguagesResult execute(final LdvGetLanguagesAction action,
       					                       final ExecutionContext context) throws ActionException 
  {
		try 
		{			
			Logger.trace("GetLanguagesHandler.execute: entering.", -1, Logger.TraceLevel.DETAIL) ;
			
			DBConnector dbConnector = new DBConnector(true, -1) ;
			
			LdvGetLanguagesResult languageResults = new LdvGetLanguagesResult() ;
		
			// Prepare sql query
			//
			String sQuery = "SELECT * FROM languages" ;
			
			Statement dbStatement = dbConnector.getStatement() ;
			if (null == dbStatement)
			{
				Logger.trace("GetLanguagesHandler.execute: no Statement, leaving.", -1, Logger.TraceLevel.ERROR) ;
				return languageResults ;
			}
			
			// Execute query 
			//
			boolean bSuccess = dbConnector.executeQuery(sQuery) ;
			if (false == bSuccess)
			{
				Logger.trace("GetLanguagesHandler.execute: query failed, leaving.", -1, Logger.TraceLevel.ERROR) ;
				return languageResults ;
			}
			
			ResultSet rs = dbConnector.getResultSet() ;
			if (null == rs)
			{
				Logger.trace("GetLanguagesHandler.execute: no result found, leaving.", -1, Logger.TraceLevel.ERROR) ;
				return languageResults ;
			}
			
			try
	    {
		    while (rs.next())
		    {
		    	Language newLanguage = new Language(rs.getString("Id"), rs.getString("label")) ;
		    	languageResults.addLanguage(newLanguage) ;
		    }
	    } catch (SQLException e)
	    {
	    	Logger.trace("GetLanguagesHandler.execute: error parsing results ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	    }
			
	    dbConnector.closeResultSet() ;
			
	    Logger.trace("GetLanguagesHandler.execute: leaving. " + languageResults.getLanguages().size() + " items found.", -1, Logger.TraceLevel.DETAIL) ;
	    
			return languageResults ;
		}
		catch (Exception cause) 
		{
			Logger.trace("GetLanguagesHandler.execute: exception ; cause: " + cause, -1, Logger.TraceLevel.ERROR) ;
			throw new ActionException(cause) ;
		}
  }
	
	@Override
	public void rollback(final LdvGetLanguagesAction action,
        							 final LdvGetLanguagesResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<LdvGetLanguagesAction> getActionType()
	{
		return LdvGetLanguagesAction.class ;
	}
}
