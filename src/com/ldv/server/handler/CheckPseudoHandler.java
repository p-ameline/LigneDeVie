package com.ldv.server.handler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.rpc.LdvCheckPseudoAction;
import com.ldv.shared.rpc.LdvCheckPseudoResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class CheckPseudoHandler extends LdvActionHandler<LdvCheckPseudoAction, LdvCheckPseudoResult>
{
	@Inject
	public CheckPseudoHandler(final Log logger,
                            final Provider<ServletContext> servletContext,       
                            final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public CheckPseudoHandler()
	{
		super() ;
	}

	@Override
	public LdvCheckPseudoResult execute(final LdvCheckPseudoAction action,
       					final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sPseudo = action.getPseudo() ;
   		
   		DBConnector dbconnector = new DBConnector(true, -1) ;
   		
   		if (true == pseudoExistsInPersons(dbconnector, sPseudo))
   			return new LdvCheckPseudoResult(true, true) ;
   		
   		if (true == pseudoExistsInFutures(dbconnector, sPseudo))
   			return new LdvCheckPseudoResult(true, true) ;
			
			return new LdvCheckPseudoResult(false, true) ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to know if pseudo exists", cause) ;
   
			throw new ActionException(cause);
		}
  }

	/**
	 * Check if a pseudo already exists in the "persons" table 
	 * 
	 * @param dbconnector : database connector
	 * @param sPseudo     : pseudo to be checked
	 * 
	 **/	
	private boolean pseudoExistsInPersons(DBConnector dbconnector, String sPseudo)
	{
		if ((null == dbconnector) || (null == sPseudo))
		{
			Logger.trace("CheckPseudoHandler.pseudoExistsInUsers: bad parameter", -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sqlText = "SELECT personId FROM persons WHERE pseudo = ?" ;
		
		dbconnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbconnector.setStatememtString(1, sPseudo) ;
				
		try
		{
			Map<String, String> userInfo = dbconnector.dbSelectPreparedStatement() ;
		
			if ((null == userInfo) || userInfo.isEmpty())
			{
				Logger.trace("CheckPseudoHandler.pseudoExistsInUsers: pseudo not found " + sPseudo, -1, Logger.TraceLevel.SUBSTEP) ;
				dbconnector.closePreparedStatement() ;
				return false ;
			}
		}
		catch(SQLException ex)
		{
			Logger.trace("CheckPseudoHandler.pseudoExistsInUsers: executeQuery failed for preparedStatement " + sqlText + " for pseudo " + sPseudo, -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLException: " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLState: " + ex.getSQLState(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("VendorError: " +ex.getErrorCode(), -1, Logger.TraceLevel.ERROR) ;        
		}
		
		Logger.trace("CheckPseudoHandler.pseudoExistsInUsers: found pseudo " + sPseudo, -1, Logger.TraceLevel.SUBSTEP) ;
		
		dbconnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	 * Check if a pseudo already exists in the "futures" table 
	 * 
	 * @param dbconnector : database connector
	 * @param sPseudo     : pseudo to be checked
	 * 
	 **/
	private boolean pseudoExistsInFutures(DBConnector dbconnector, String sPseudo)
	{
		if ((null == dbconnector) || (null == sPseudo))
		{
			Logger.trace("CheckPseudoHandler.pseudoExistsInFutures: bad parameter", -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sqlText = "SELECT futureId FROM futures WHERE pseudo = ?" ;
		
		dbconnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbconnector.setStatememtString(1, sPseudo) ;
			
		try
		{
			Map<String, String> userInfo = dbconnector.dbSelectPreparedStatement() ;
		
			if ((null == userInfo) || userInfo.isEmpty())
			{
				Logger.trace("CheckPseudoHandler.pseudoExistsInFutures: pseudo not found " + sPseudo, -1, Logger.TraceLevel.SUBSTEP) ;
				dbconnector.closePreparedStatement() ;
				return false ;
			}
		}
		catch(SQLException ex)
		{
			Logger.trace("CheckPseudoHandler.pseudoExistsInFutures: executeQuery failed for preparedStatement " + sqlText + " for pseudo " + sPseudo, -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLException: " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLState: " + ex.getSQLState(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("VendorError: " +ex.getErrorCode(), -1, Logger.TraceLevel.ERROR) ;        
		}
		
		Logger.trace("CheckPseudoHandler.pseudoExistsInFutures: found pseudo " + sPseudo, -1, Logger.TraceLevel.SUBSTEP) ;
		
		dbconnector.closePreparedStatement() ;
		
		return true ;
	}
	
	@Override
	public void rollback(final LdvCheckPseudoAction action,
        							 final LdvCheckPseudoResult result,
        final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<LdvCheckPseudoAction> getActionType()
	{
		return LdvCheckPseudoAction.class ;
	}
}
