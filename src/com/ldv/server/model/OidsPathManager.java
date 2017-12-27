package com.ldv.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.database.OidsPath;

/** 
 * Object in charge of Read/Write operations in the <code>o_traits_paths</code> table 
 *   
 */
public class OidsPathManager  
{	
	protected final DBConnector _dbConnector ;
	protected final String      _sUserId ;
	
	/**
	 * Constructor 
	 */
	public OidsPathManager(String sUserId, final DBConnector dbConnector)
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
	public boolean insertData(final OidsPath dataToInsert)
	{
		if ((null == _dbConnector) || (null == dataToInsert))
			return false ;
		
		String sFctName = "OidsPathManager.insertData" ;
		
		String sQuery = "INSERT INTO o_traits_paths (PATH, TRAIT_ID) VALUES (?, ?)" ;
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToInsert.getPath()) ;
		_dbConnector.setStatememtInt(2, dataToInsert.getTraitId()) ;
		
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
		
		Logger.trace(sFctName + ": user " + _sUserId + " successfuly recorded OidsPath " + iDataId + " for path " + dataToInsert.getPath() + " for trait id " + dataToInsert.getTraitId(), _sUserId, Logger.TraceLevel.STEP) ;
		
		return true ;
	}
	
	/**
	  * Update a Oids in database
	  * 
	  * @return true if successful, false if not
	  * @param dataToUpdate Oids to be updated
	  * 
	  */
	public boolean updateData(OidsPath dataToUpdate)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("OidsPathManager.updateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		OidsPath foundData = new OidsPath() ;
		if (false == existData(dataToUpdate.getId(), foundData))
			return false ;
		
		if (foundData.equals(dataToUpdate))
		{
			Logger.trace("OidsPathManager.updateData: OidsPath to update (" + dataToUpdate.getPath() + "/" + dataToUpdate.getTraitId() + ") unchanged; nothing to do", _sUserId, Logger.TraceLevel.SUBSTEP) ;
			return true ;
		}
		
		return forceUpdateData(dataToUpdate) ;
	}
	
	/**
	  * Check if there is any OidsPath with a given path in database and, if true get its content
	  * 
	  * @return True if found, else false
	  * 
	  * @param sPath     Path to find
	  * @param foundData OidsPath to get existing information
	  * 
	  */
	public boolean findRecordForPath(final String sPath, OidsPath foundData)
	{
		String sFctName = "OidsPathManager.findRecordForPath" ;
		
		if ((null == _dbConnector) || (null == sPath) || "".equals(sPath))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM o_traits_paths WHERE PATH = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sPath) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no o_traits_paths found for path = " + sPath, _sUserId, Logger.TraceLevel.WARNING) ;
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
	private boolean existData(int iDataId, OidsPath foundData)
	{
		String sFctName = "OidsPathManager.existData" ;
		
		if ((null == _dbConnector) || (-1 == iDataId) || (null == foundData))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM o_traits_paths WHERE id = ?" ;
		
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
			Logger.trace(sFctName + ": no o_traits_paths found for id = " + iDataId, _sUserId, Logger.TraceLevel.WARNING) ;
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
	  * Update a OidsPath in database
	  * 
	  * @return <code>true</code> if creation succeeded, <code>false</code> if not
	  * @param  dataToUpdate OidsPath to update
	  * 
	  */
	private boolean forceUpdateData(OidsPath dataToUpdate)
	{
		String sFctName = "OidsPathManager.forceUpdateData" ;
		
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Prepare SQL query
		//
		String sQuery = "UPDATE o_traits_paths SET PATH = ? AND TRAIT_ID = ?" +
				                                       " WHERE id = ?" ; 
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToUpdate.getPath()) ;
		_dbConnector.setStatememtInt(2, dataToUpdate.getTraitId()) ;
		
		_dbConnector.setStatememtInt(3, dataToUpdate.getId()) ;
				
		// Execute query  
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}

		Logger.trace(sFctName + ": updated data for OidsPath " + dataToUpdate.getId(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		_dbConnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	  * Fill a structure with all the different traits for a given path 
	  * 
	  * @param iUserId   ID of user
	  * @param aTraits   Array of OidsPath to be filled
	  * @param sObjectId Path root to get traits for 
	  * 
	  */
	public void fillTraitsForPath(Vector<OidsPath> aTraits, String sPathRoot)
	{
		String sFctName = "OidsPathManager.fillTraitsForPath" ;
		
		if ((null == _dbConnector) || (null == sPathRoot) || (null == aTraits))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return ;
		}
		
		String sQuery = "SELECT * FROM o_traits_paths WHERE PATH LIKE ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sPathRoot + "%") ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no trait found for root " + sPathRoot, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return ;
		}
		
		int iNbFound = 0 ;
		
		try
		{
	    while (rs.next())
	    {
	    	OidsPath foundData = new OidsPath() ;
	    	fillDataFromResultSet(rs, foundData) ;
	    	aTraits.add(foundData) ;
	    	
	    	iNbFound++ ;
	    }
		} catch (SQLException e)
		{
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
				
		Logger.trace(sFctName + ": found " + iNbFound + " traits for root " + sPathRoot, _sUserId, Logger.TraceLevel.STEP) ;
	}
	
	/**
	  * Initialize a OidsPath from a query ResultSet 
	  * 
	  * @param rs        ResultSet of a query
	  * @param foundData OidsPath to fill
	  * 
	  */
	protected void fillDataFromResultSet(ResultSet rs, OidsPath foundData)
	{
		if ((null == rs) || (null == foundData))
			return ;
		
		try
		{
    	foundData.setId(rs.getInt("id")) ;
    	foundData.setPath(rs.getString("PATH")) ;
    	foundData.setTraitId(rs.getInt("TRAIT_ID")) ;
		} 
		catch (SQLException e) {
			Logger.trace("OidsPathManager.fillDataFromResultSet: exception when processing results set: " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
	}
}
