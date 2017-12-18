package com.ldv.server.model;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.database.Session;

public class LdvSessionsManager 
{
	private Session     _session ;
	private DBConnector _dbConnector ;
	
	public LdvSessionsManager()
	{
		_session     = new Session() ;
		_dbConnector = new DBConnector(true, -1) ;
	}
		
	public LdvSessionsManager(DBConnector dbConnector)
	{
		_session     = new Session() ;
		_dbConnector = dbConnector ;
	}
	
	/**
	 * Create a new session for a given LdvId and a given user    
	 * 
	 * @param sLdvId  : Person's Ldv identifier
	 * @param sUserId : user's identifier
	 * @return Calculated token if everything went well or "" elsewhere
	 * 
	 **/
	public boolean createNewSession(String sLdvId, String sUserId)
	{
		if ((null == sLdvId) || sLdvId.equals(""))
			return false ;
		
		if (null == _dbConnector)
			return false ;

		// First, close existing sessions (if any)
		//
		closePreviousSessions(sLdvId, sUserId) ;
		
		_session.reset() ;
		
		_session.setLdvId(sLdvId) ;
		_session.setUserLdvId(sUserId) ;
		
		initSessionTokenAndDate() ;
		
		// Finally, store session in database
		//
		int iSessionId = createNewSession() ;
		if (-1 == iSessionId)
			return false ;
		
		return true ;
	}
	
	/**
	 * Delete a session records for a given LdvId, userId and token    
	 * 
	 * @param sLdvId  Person's Ldv identifier
	 * @param sUserId User's identifier
	 * @param sToken  Token
	 * 
	 **/
	public void closeSession(String sLdvId, String sUserId, String sToken)
	{
		if ((null == sLdvId) || sLdvId.equals("") || (null == sToken) || sToken.equals(""))
		
		if (false == checkConnectorAndLdvId(sLdvId, "closeSession"))
			return ;
		
		String sqlText = "DELETE FROM sessions WHERE ldvid = ? AND token = ?" ;
		if ((null != sUserId) && (false == sUserId.equals("")))
			sqlText += " AND ldvidUser = ?" ;
		
		_dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sLdvId) ;
		_dbConnector.setStatememtString(2, sToken) ;
		if ((null != sUserId) && (false == sUserId.equals("")))
			_dbConnector.setStatememtString(3, sUserId) ;
		
		// Execute prepared statement 
		//
		_dbConnector.executeUpdatePreparedStatement(false) ;
		
		_dbConnector.closePreparedStatement() ;
	}
	
	/**
	 * Delete all sessions records for a given LdvId    
	 * 
	 * @param sLdvId      Person's Ldv identifier
	 * @param sUserLdvId  User's Ldv identifier, if empty, means close all sessions for this Person
	 * 
	 **/
	private void closePreviousSessions(String sLdvId, String sUserLdvId)
	{
		if (false == checkConnectorAndLdvId(sLdvId, "closePreviousSessions"))
			return ;
		
		String sqlText = "DELETE FROM sessions WHERE ldvid = ?" ;
		if ((null != sUserLdvId) && (false == sUserLdvId.equals("")))
			sqlText += " AND ldvidUser = ?" ;
		
		_dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sLdvId) ;
		if ((null != sUserLdvId) && (false == sUserLdvId.equals("")))
			_dbConnector.setStatememtString(2, sUserLdvId) ;
		
		// Execute prepared statement 
		//
		_dbConnector.executeUpdatePreparedStatement(false) ;
		
		_dbConnector.closePreparedStatement() ;
	}
		
	/**
	 * Check if database connector and LdvId are valid    
	 * 
	 * @param sLdvId Person's Ldv identifier
	 * @param sFonctionName Name of calling function - for logging purposes
	 * @return true if all is fine, false elsewhere
	 * 
	 **/
	private boolean checkConnectorAndLdvId(String sLdvId, String sFonctionName)
	{
		if ((null == _dbConnector) || (null == sLdvId) || sLdvId.equals(""))
		{
			if ((null == _dbConnector) && ((null == sLdvId) || sLdvId.equals("")))
			{
				Logger.trace("LdvSessionsManager." + sFonctionName + ": null connector and empty LdvId.", -1, Logger.TraceLevel.ERROR) ;
				return false ;
			}
			
			if (null == _dbConnector)
				Logger.trace("LdvSessionsManager." + sFonctionName + ": null connector for LdvId=" + sLdvId, -1, Logger.TraceLevel.ERROR) ;
			if ((null == sLdvId) || sLdvId.equals(""))
				Logger.trace("LdvSessionsManager." + sFonctionName + ": empty LdvId.", -1, Logger.TraceLevel.ERROR) ;
			
			return false ;
		}
		return true ;
	}
	
	/**
	 * Create a new session, populated with a LdV Id    
	 * 
	 * @return person's record id if successful, -1 if failed
	 * 
	 **/
	public int createNewSession()
	{
		if (null == _dbConnector)
		{
			Logger.trace("LdvSessionsManager.createNewSession: null connector; creation failed", -1, Logger.TraceLevel.ERROR) ;
			return -1 ;
		}
		
		String sQuery = "INSERT INTO sessions (ldvid, ldvidUser, token, datetimeOpen) VALUES (?, ?, ?, ?)" ;
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace("LdvSessionsManager.createNewSession: cannot get Statement", -1, Logger.TraceLevel.ERROR) ;
			return -1 ;
		}
		
		_dbConnector.setStatememtString(1, _session.getLdvId()) ;
		_dbConnector.setStatememtString(2, _session.getUserLdvId()) ;
		_dbConnector.setStatememtString(3, _session.getToken()) ;
		_dbConnector.setStatememtString(4, _session.getDateTimeOpen()) ;
		
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("LdvSessionsManager.createNewSession: failed query " + sQuery, -1, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return -1 ;
		}
		
		ResultSet rs = _dbConnector.getResultSet() ;
		int iNewSessionId = -1 ;
		try
    {
	    if (rs.next())
	    	iNewSessionId = rs.getInt(1) ;
	    else
	    	Logger.trace("LdvSessionsManager.createNewSession: cannot get Id after query " + sQuery, -1, Logger.TraceLevel.ERROR) ;
    } 
		catch (SQLException e)
    {
			Logger.trace("LdvSessionsManager.createNewSession: exception when iterating results " + e.getMessage(), -1, Logger.TraceLevel.ERROR) ;
    }
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;

		Logger.trace("LdvSessionsManager.createNewSession: new session (" + iNewSessionId + ") created for person " + _session.getLdvId(), -1, Logger.TraceLevel.DETAIL) ;
		
   	return iNewSessionId ;
	}

	/**
	 * Sets the token and the date and time of opening    
	 * 
	 **/
	private void initSessionTokenAndDate()
	{
		// Get formated current date
		//
		Date dateNow = new Date() ;
		SimpleDateFormat ldvFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		_session.setDateTimeOpen(ldvFormat.format(dateNow)) ;
		
		// Create token
		//
		String sTokenSeed = _session.getLdvId() + _session.getDateTimeOpen() ;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5") ;
			final byte[] md5Digest = md.digest(sTokenSeed.getBytes()) ;
	    final BigInteger md5Number = new BigInteger(1, md5Digest) ;
	    final String md5String = md5Number.toString(16) ;

	    _session.setToken(md5String)  ;
		} 
		catch (NoSuchAlgorithmException e) {
			Logger.trace("LdvSessionsManager.initSessionTokenAndDate: no MD5 algorithm found. " + e.getMessage(), -1, Logger.TraceLevel.ERROR) ;
		}    
	}
	
	/**
	 * Check if a session exists for this token, this Ldv Id and this user Id    
	 * 
	 * @param sLdvId  : Person's Ldv identifier
	 * @param sUserId : User's Ldv identifier
	 * @param sToken  : Token
	 * @return Calculated token if everything went well or "" elsewhere
	 * 
	 **/
	public boolean isValidToken(String sLdvId, String sUserId, String sToken)
	{
		if ((null == sLdvId) || sLdvId.equals(""))
			return false ;
		
		if (null == _dbConnector)
			return false ;
		
		String sQuery = "SELECT * FROM sessions WHERE ldvid = ? AND ldvidUser = ? AND token = ?" ;
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sLdvId) ;
		_dbConnector.setStatememtString(2, sUserId) ;
		_dbConnector.setStatememtString(3, sToken) ;
		
		// Execute prepared statement 
		//
		boolean bSuccess = _dbConnector.executePreparedStatement() ;
		if (false == bSuccess)
		{
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		boolean bResult = false ;
		try
		{
	    if (rs.next())
	    {
	    	// String sDateTimeOpen = rs.getString("datetimeOpen") ;
	  	  bResult = true ;
	    }
		} 
		catch(SQLException ex)
		{
			Logger.trace("LdvSessionsManager.isValidToken: exception enumerating result ", -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLException: " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLState: " + ex.getSQLState(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("VendorError: " +ex.getErrorCode(), -1, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;

		return bResult ;
	}
			
	/**
	  * Set the date to "now" for a date whose field name is in sDateFieldName 
	  */
/*
	private boolean setDateToNow(String sDateFieldName)
	{
		if ((null == sDateFieldName) || (sDateFieldName.equals("")) || (-1 == _iId))
			return false ;
		
		// Get formated current date
		//
		Date dateNow = new Date() ;
		SimpleDateFormat ldvFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		String sFormatedNow = ldvFormat.format(dateNow) ;
				
		DBConnector dbConnector = new DBConnector(true, _iId) ;
		if (null == dbConnector)
			return false ;
		
		// Prepare sql query
		//
		String sqlText = "UPDATE clients SET " + sDateFieldName + " = ? WHERE id = ?" ;
		
		dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtString(1, sFormatedNow) ;
		dbConnector.setStatememtInt(1, _iId) ;
				
		// Execute query and get the Id that was automatically set 
		//
		int iNbAffectedRows = dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
			return false ;

		dbConnector.closeAll() ;
		
		return true ;
	}
*/

	public String getToken()
  {
	  return _session.getToken() ;
  }
	
	public String getLdvId()
  {
	  return _session.getLdvId() ;
  }
}
