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
import com.ldv.server.DbParameters;
import com.ldv.server.Logger;
import com.ldv.server.model.LdvBackOfficePerson;
import com.ldv.server.model.LdvFilesManager;
import com.ldv.server.model.LdvXmlGraph;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.rpc.LdvValidatorUserAction;
import com.ldv.shared.rpc.LdvValidatorUserResult;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ValidateUserHandler extends LdvActionHandler<LdvValidatorUserAction, LdvValidatorUserResult>
{
	/**
	  * Standard constructor
	  */
	@Inject
	public ValidateUserHandler(final Log logger,
                             final Provider<ServletContext> servletContext,       
                             final Provider<HttpServletRequest> servletRequest)
	{
		super(logger, servletContext, servletRequest) ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public ValidateUserHandler()
	{
		super() ;
	}

	@Override
	public LdvValidatorUserResult execute(final LdvValidatorUserAction action,
       					                        final ExecutionContext context) throws ActionException 
  {
		if (null == action)
		{
			Logger.trace("ValidateUserHandler.execute: error, null action", -1, Logger.TraceLevel.ERROR) ;
			return new LdvValidatorUserResult(false, "Technical problem, null action parameter.") ;
		}
			
		if ((null == action.getId()) || action.getId().equals(""))
		{
			Logger.trace("ValidateUserHandler.execute: error, empty Id", -1, Logger.TraceLevel.ERROR) ;
			return new LdvValidatorUserResult(false, "Technical problem, bad action parameter.") ;
		}
		
		Logger.trace("Entering ValidateUserHandler.execute for Id= " + action.getId(), -1, Logger.TraceLevel.STEP) ;
		
		LdvBackOfficePerson BOclient = new LdvBackOfficePerson() ;
		
		// First, manage the back-office stuffs
		//
		int iClientId = validateNewUser(action, BOclient) ;
		if (-1 == iClientId)
			return new LdvValidatorUserResult(false, "Cannot find registration information") ;
		
		boolean bFilesCreated = createFiles(BOclient, action) ;
		if (false == bFilesCreated)
		{
			Logger.trace("ValidateUserHandler.execute: error, cannot create files", -1, Logger.TraceLevel.ERROR) ;
			return new LdvValidatorUserResult(false, "Technical problem, cannot create files.") ;
		}
		
		String sPassword   = BOclient.getPassword() ;
		String sIdentifier = BOclient.getPseudo() ;
		
		// Ask for the front-office creation
		//
		_sServerRequest = "GET askfor=createNewPerson&Pass=" + sPassword + "&login=" + sIdentifier + " HTTP/1.1" ;
		
		// CallServer() ;
		
		if (false == isSuccess())
			return new LdvValidatorUserResult(false, getInfoString("message")) ;
		
		// String sToken = getToken() ;
		// if (sToken.equals(""))
		//	return new SendLoginResult("", getInfoString("message")) ;
		
		Logger.trace("Leaving ValidateUserHandler.execute for Id= " + action.getId(), -1, Logger.TraceLevel.STEP) ;
		
		return new LdvValidatorUserResult(true, "") ;
  }

	/**
	  * Fully create new person information in "persons" table and erase its record in "futures" table
	  * 
	  * @param    action Parameters sent by client
	  * @param    BOclient User to be instantiated
	  * @return   Newly created User Id if everything Ok; -1 if some technical problem occurred, -2 if password don't match between client entry and information in futures
	  */
	private int validateNewUser(LdvValidatorUserAction action, LdvBackOfficePerson BOclient) throws ActionException
	{
		if ((null == BOclient) || (null == action))
			return -1 ;
		
		try 
		{			
			DBConnector dbConnector = new DBConnector(true, -1) ;
			
			// Record information in the clients table
			//
			int iFutureId = getFuturesInformation(action.getId(), dbConnector, BOclient) ;
			if (-1 == iFutureId)
				return -1 ;
			
			// We have to check if password match, because, if not, Secret Key might be wrong
			//
			if (false == BOclient.getPassword().equals(action.getPassword()))
				return -2 ;
		
			int iNewPersonId = BOclient.createNewPerson(dbConnector) ;
			if (-1 == iNewPersonId)
				return -1 ;
			
			boolean bSuccess = deleteRegistrationInformation(iFutureId, dbConnector) ;
			if (false == bSuccess)
			{
				// TODO remove client
				return -1 ;
			}
			
			return iNewPersonId ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to know if pseudo exists", cause) ;
   
			throw new ActionException(cause);
		}
	}
	
	/**
	  * Get id from the validation string and initialize the new person from its futures information
	  */
	private int getFuturesInformation(String sId, DBConnector dbConnector, LdvBackOfficePerson BOclient)
	{
		if ((null == dbConnector) || (null == sId) || sId.equals(""))
			return -1 ;
		
		String sQuery = "SELECT * FROM futures WHERE registerId = ?" ;
		
		dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtString(1, sId) ;
   
		if (false == dbConnector.executePreparedStatement())
		{
			Logger.trace("ValidateUserHandler.getFuturesId: failed query when looking for futures' registerId for key " + sId, -1, Logger.TraceLevel.ERROR) ;
			dbConnector.closePreparedStatement() ;
			return -1 ;
		}
		
		ResultSet rs = dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace("ValidateUserHandler.getFuturesId: information not found when looking for futures' registerId for key " + sId, -1, Logger.TraceLevel.ERROR) ;
			dbConnector.closePreparedStatement() ;
			return -1 ;
		}
		
		int iResult = -1 ;
		try
		{
	    if (rs.next())
	    {
	    	iResult = rs.getInt("futureId") ;
	    	
	    	BOclient.setPseudo(rs.getString("pseudo")) ;
	    	BOclient.setPassword(rs.getString("password")) ;
	    	BOclient.setLanguage(rs.getString("language")) ;
	    }
		} catch (SQLException e) {
			Logger.trace("ValidateUserHandler.getFuturesId: error parsing results for registerId=" + sId + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
		}
		
   	dbConnector.closeResultSet() ;
   	dbConnector.closePreparedStatement() ;
   
   	Logger.trace("ValidateUserHandler.getFuturesId: successfuly initialized new person from future number " + iResult, -1, Logger.TraceLevel.SUBSTEP) ;
   	
		return iResult ;
	}
		
	/**
	  * Delete information in 'futures' for this user
	  */
	private boolean deleteRegistrationInformation(int iClientId, DBConnector dbConnector)
	{
		if ((-1 == iClientId) || (null == dbConnector))
			return false ;
		
		// Prepare sql query
		//
		String sQuery = "DELETE FROM futures WHERE futureId = ?" ;
		                
		dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		dbConnector.setStatememtInt(1, iClientId) ;
		
		// Execute query 
		//
		int iNbAffectedRows = dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("ValidateUserHandler.deleteRegistrationInformation: failed deleting record from futures.", -1, Logger.TraceLevel.ERROR) ;
			dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		dbConnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	 * Create all the files that create the initial graph of trees
	 * 
	 * @param BOclient Person to create the graph for
	 * @param action
	 * 
	 * @return <code>true</code> if all went well
	 */
	boolean createFiles(LdvBackOfficePerson BOclient, LdvValidatorUserAction action)
	{
		// First create a LdvFilesManager
		//
		LdvFilesManager filesManager = new LdvFilesManager(BOclient.getLdvId(), DbParameters._sFilesDir, DbParameters._sDirSeparator) ;
		
		// Then ask it to create Main Directory for the new LdV
		//
		if (false == filesManager.createMainDirectory())
			return false ;
		
		// Finally, create the initial graph
		//
		String sRealPath = _servletContext.get().getRealPath("") ;
		
		LdvXmlGraph xmlGraph = new LdvXmlGraph(LdvGraphConfig.COLLECTIVE_SERVER, BOclient.getLdvId(), BOclient.getLdvId()) ;
		xmlGraph.initNewGraph(BOclient.getLdvId(), "", sRealPath) ;
		
		xmlGraph.exportFiles(filesManager, action.getKey()) ;
		
		return true ;
	}
			
	@Override
	public void rollback(final LdvValidatorUserAction action,
        							 final LdvValidatorUserResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<LdvValidatorUserAction> getActionType()
	{
		return LdvValidatorUserAction.class ;
	}
}
