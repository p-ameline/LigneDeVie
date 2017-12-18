package com.ldv.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.database.OidsIdentifier;

/** 
 * Object in charge of Read/Write operations in the <code>o_traits_id</code> table 
 *   
 */
public class OidsIdentifierManager  
{	
	protected final DBConnector _dbConnector ;
	protected final String      _sUserId ;
	
	/**
	 * Constructor 
	 */
	public OidsIdentifierManager(String sUserId, final DBConnector dbConnector)
	{
		_dbConnector = dbConnector ;
		_sUserId     = sUserId ;
	}
	
	/**
	  * Insert a OidsPath object in database
	  * 
	  * @return true if successful, false if not
	  * @param dataToInsert OidsPath to be inserted
	  * 
	  */
	public boolean insertData(final OidsIdentifier dataToInsert)
	{
		if ((null == _dbConnector) || (null == dataToInsert))
			return false ;
		
		String sFctName = "OidsIdentifierManager.insertData" ;
		
		String sQuery = "INSERT INTO o_traits_id (TRAIT_NAME) VALUES (?)" ;
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToInsert.getName()) ;
		
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		int iDataId = 0 ;
		
		ResultSet rs = _dbConnector.getResultSet() ;
		try
    {
			if (rs.next())
			{
				iDataId = rs.getInt(1) ;
				dataToInsert.setId(iDataId) ;
			}
			else
				Logger.trace(sFctName + ": cannot get Id after query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
    } 
		catch (SQLException e)
    {
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
    }
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
		
		Logger.trace(sFctName + ": user " + _sUserId + " successfuly recorded OidsIdentifier " + iDataId + " for name " + dataToInsert.getName(), _sUserId, Logger.TraceLevel.STEP) ;
		
		return true ;
	}
	
	/**
	  * Update a OidsIdentifier in database
	  * 
	  * @return true if successful, false if not
	  * @param dataToUpdate OidsIdentifier to be updated
	  * 
	  */
	public boolean updateData(OidsIdentifier dataToUpdate)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("OidsIdentifierManager.updateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		OidsIdentifier foundData = new OidsIdentifier() ;
		if (false == existData(dataToUpdate.getId(), foundData))
			return false ;
		
		if (foundData.equals(dataToUpdate))
		{
			Logger.trace("OidsIdentifierManager.updateData: OidsPath to update (" + dataToUpdate.getName() + "/" + dataToUpdate.getId() + ") unchanged; nothing to do", _sUserId, Logger.TraceLevel.SUBSTEP) ;
			return true ;
		}
		
		return forceUpdateData(dataToUpdate) ;
	}
	
	/**
	  * Check if there is any OidsIdentifierManager with a given name in database and, if true get its content
	  * 
	  * @return True if found, else false
	  * 
	  * @param sName     Name to find
	  * @param foundData OidsIdentifier to set existing information
	  * 
	  */
	public boolean findRecordForName(final String sName, OidsIdentifier foundData)
	{
		String sFctName = "OidsIdentifierManager.findRecordForPath" ;
		
		if ((null == _dbConnector) || (null == sName) || "".equals(sName))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM o_traits_id WHERE TRAIT_NAME = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sName) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no o_traits_id found for name = " + sName, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		try
		{
	    if (rs.next())
	    {
	    	fillDataFromResultSet(rs, foundData) ;
	    	
	    	_dbConnector.closeResultSet() ;
	    	_dbConnector.closePreparedStatement() ;
	    	
	    	return true ;	    	
	    }
		} catch (SQLException e)
		{
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
				
		return false ;
	}
		
	/**
	  * Check if there is any OidsPath with this Id in database and, if true get its content
	  * 
	  * @return True if found, else false
	  * @param iDataId ID of OidsPath to check
	  * @param foundData OidsPath to get existing information
	  * 
	  */
	private boolean existData(int iDataId, OidsIdentifier foundData)
	{
		String sFctName = "OidsIdentifierManager.existData" ;
		
		if ((null == _dbConnector) || (-1 == iDataId) || (null == foundData))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM o_traits_id WHERE TRAIT_ID = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtInt(1, iDataId) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no o_traits_id found for TRAIT_ID = " + iDataId, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		try
		{
	    if (rs.next())
	    {
	    	fillDataFromResultSet(rs, foundData) ;
	    	
	    	_dbConnector.closeResultSet() ;
	    	_dbConnector.closePreparedStatement() ;
	    	
	    	return true ;	    	
	    }
		} catch (SQLException e)
		{
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
				
		return false ;
	}
		
	/**
	  * Update a OidsIdentifier in database
	  * 
	  * @return <code>true</code> if creation succeeded, <code>false</code> if not
	  * @param  dataToUpdate OidsIdentifier to update
	  * 
	  */
	private boolean forceUpdateData(OidsIdentifier dataToUpdate)
	{
		String sFctName = "OidsIdentifierManager.forceUpdateData" ;
		
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Prepare SQL query
		//
		String sQuery = "UPDATE o_traits_id SET TRAIT_NAME = ?" +
				                                       " WHERE TRAIT_ID = ?" ; 
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToUpdate.getName()) ;
		
		_dbConnector.setStatememtInt(2, dataToUpdate.getId()) ;
				
		// Execute query  
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}

		Logger.trace(sFctName + ": updated data for OidsIdentifier " + dataToUpdate.getId(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		_dbConnector.closePreparedStatement() ;
		
		return true ;
	}
		
	/**
	  * Initialize a OidsIdentifier from a query ResultSet 
	  * 
	  * @param rs        ResultSet of a query
	  * @param foundData OidsIdentifier to fill
	  * 
	  */
	protected void fillDataFromResultSet(ResultSet rs, OidsIdentifier foundData)
	{
		if ((null == rs) || (null == foundData))
			return ;
		
		try
		{
    	foundData.setId(rs.getInt("TRAIT_ID")) ;
    	foundData.setName(rs.getString("TRAIT_NAME")) ;
		} 
		catch (SQLException e) {
			Logger.trace("OidsIdentifierManager.fillDataFromResultSet: exception when processing results set: " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
	}
}
