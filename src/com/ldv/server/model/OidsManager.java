package com.ldv.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.database.Oids;
import com.ldv.shared.graph.LdvGraphTools;

/** 
 * Object in charge of Read/Write operations in the <code>city</code> table 
 *   
 */
public class OidsManager  
{	
	protected final DBConnector _dbConnector ;
	protected final String      _sUserId ;
	
	public static String FIRST_OBJECT  = "$000000000000" ;
	
	/**
	 * Constructor 
	 */
	public OidsManager(final String sUserId, final DBConnector dbConnector)
	{
		_dbConnector = dbConnector ;
		_sUserId     = sUserId ;
	}
	
	/**
	  * Check if there is any Oids with this trait Id and value in database and, if true get its content
	  * 
	  * @return True if found, else false
	  * 
	  * @param iDataId     ID of trait to search for
	  * @param sTraitValue Value of trait to search for
	  * @param foundData   Oids to get existing information
	  * 
	  */
	public boolean findTrait(final int iTraitId, final String sTraitValue, Oids foundData)
	{
		String sFctName = "OidsManager.findTrait" ;
		
		if ((null == _dbConnector) || (null == sTraitValue) || ("".equals(sTraitValue)) || (iTraitId <= iTraitId))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM oids WHERE ID_TRAIT = ? AND VALUE_TRAIT = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		
		_dbConnector.setStatememtInt(1, iTraitId) ;
		_dbConnector.setStatememtString(2, sTraitValue) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no Oids found for value = " + sTraitValue + " and trait " + iTraitId, _sUserId, Logger.TraceLevel.WARNING) ;
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
	  * Insert a Oids object in database
	  * 
	  * @return true if successful, false if not
	  * @param dataToInsert Oids to be inserted
	  * 
	  */
	public boolean insertData(Oids dataToInsert)
	{
		if ((null == _dbConnector) || (null == dataToInsert))
			return false ;
		
		String sQuery = "INSERT INTO oids (OBJECT, ID_TRAIT, VALUE_TRAIT) VALUES (?, ?, ?)" ;
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace("OidsManager.insertData: cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToInsert.getObjectId()) ;
		_dbConnector.setStatememtInt(2, dataToInsert.getTraitId()) ;
		_dbConnector.setStatememtString(3, dataToInsert.getValue()) ;
		
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("OidsManager.insertData: failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
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
			}
			else
				Logger.trace("OidsManager.insertData: cannot get Id after query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
    } 
		catch (SQLException e)
    {
			Logger.trace("OidsManager.insertData: exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
    }
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
		
		Logger.trace("OidsManager.insertData: user " + _sUserId + " successfuly recorded oids " + iDataId + " for object " + dataToInsert.getObjectId() + " : trait value " + dataToInsert.getValue() + " for trait id " + dataToInsert.getTraitId(), _sUserId, Logger.TraceLevel.STEP) ;
		
		return true ;
	}
	
	/**
	  * Update a Oids in database
	  * 
	  * @return true if successful, false if not
	  * @param dataToUpdate Oids to be updated
	  * 
	  */
	public boolean updateData(Oids dataToUpdate)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("OidsManager.updateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		Oids foundData = new Oids() ;
		if (false == existData(dataToUpdate.getObjectId(), dataToUpdate.getTraitId(), foundData))
			return false ;
		
		if (foundData.equals(dataToUpdate))
		{
			Logger.trace("OidsManager.updateData: Trait to update (" + dataToUpdate.getObjectId() + " / " + dataToUpdate.getTraitId() + ") unchanged; nothing to do", _sUserId, Logger.TraceLevel.SUBSTEP) ;
			return true ;
		}
		
		return forceUpdateData(dataToUpdate) ;
	}
		
	/**
	  * Check if there is any Oids with this Id in database and, if true get its content
	  * 
	  * @return True if found, else false
	  * 
	  * @param iDataId ID of Oids to check
	  * @param foundData Oids to get existing information
	  * 
	  */
	private boolean existData(String sObjectId, int iTraitId, Oids foundData)
	{
		String sFctName = "OidsManager.existData" ;
		
		if ((null == _dbConnector) || (null == sObjectId) || (null == foundData))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM oids WHERE OBJECT = ? AND ID_TRAIT = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sObjectId) ;
		_dbConnector.setStatememtInt(2, iTraitId) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no Oids found for object = " + sObjectId + " and trait " + iTraitId, _sUserId, Logger.TraceLevel.WARNING) ;
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
	  * Update a Oids in database
	  * 
	  * @return <code>true</code> if creation succeeded, <code>false</code> if not
	  * @param  dataToUpdate Oids to update
	  * 
	  */
	private boolean forceUpdateData(Oids dataToUpdate)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("OidsManager.forceUpdateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Prepare SQL query
		//
		String sQuery = "UPDATE oids SET VALUE_TRAIT = ?" +
				                          " WHERE OBJECT = ? AND ID_TRAIT = ?" ; 
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace("OidsManager.forceUpdateData: cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToUpdate.getValue()) ;
		_dbConnector.setStatememtString(2, dataToUpdate.getObjectId()) ;
		_dbConnector.setStatememtInt(3, dataToUpdate.getTraitId()) ;
				
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("OidsManager.forceUpdateData: failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}

		Logger.trace("OidsManager.forceUpdateData: updated data for Oids " + dataToUpdate.getObjectId() + " / " + dataToUpdate.getTraitId(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		_dbConnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	  * Fill a structure with all the different traits for a given object 
	  * 
	  * @param iUserId   ID of user
	  * @param aTraits   Array of Oids to be filled
	  * @param sObjectId ID of object to get traits for 
	  * 
	  */
	public void fillTraitsForObject(int iUserID, Vector<Oids> aTraits, String sObjectId)
	{
		if ((null == _dbConnector) || (null == sObjectId) || (null == aTraits))
		{
			Logger.trace("OidsManager.fillTraitsForObject: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return ;
		}
		
		String sQuery = "SELECT * FROM oids WHERE OBJECT = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sObjectId) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace("OidsManager.fillTraitsForObject: failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace("OidsManager.fillTraitsForObject: no trait found for object " + sObjectId, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return ;
		}
		
		try
		{
	    while (rs.next())
	    {
	    	Oids foundData = new Oids() ;
	    	fillDataFromResultSet(rs, foundData) ;
	    	aTraits.add(foundData) ;
	    }
		} catch (SQLException e)
		{
			Logger.trace("OidsManager.fillTraitsForObject: exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
				
		return ;
	}
	
	/**
	 * Create a new object, populated with a first trait    
	 * 
	 * @param iTraitID    ID of the trait to inserted
	 * @param sTraitLabel Value of the trait to inserted
	 * 
	 * @return object's identifier if successful, "" if failed
	 * 
	 **/
	public String createNewObjectFromTrait(final int iTraitID, final String sTraitLabel)
	{
		String sFctName = "OidsManager.createNewObjectFromTrait" ;
		
		if (null == _dbConnector)
		{
			Logger.trace(sFctName + ": uninitialized connector", _sUserId, Logger.TraceLevel.ERROR) ;
			return "" ;
		}
		
		// Lock table
		//
		if (false == lockTableWrite())
		{
			Logger.trace(sFctName + ": cannot lock table Persons; creation failed", _sUserId, Logger.TraceLevel.ERROR) ;
			return "" ;
		}
		
		// Get the next object Id
		//
		String sObjectId = getNextPersonCode() ;
		
		if (sObjectId.equals(""))
		{
			unlockTable() ;
			return "" ;
		}

		// Insert a first trait in order to "secure this Id"
		//
		Oids dataToInsert = new Oids(sObjectId, iTraitID, sTraitLabel) ;
		boolean bInserted = insertData(dataToInsert) ;
		
		unlockTable() ;
		
		if (false == bInserted)
			return "" ;
		
		return sObjectId ;
	}
	
	/**
	  * Initialize a Oids from a query ResultSet 
	  * 
	  * @param rs        ResultSet of a query
	  * @param foundData Oids to fill
	  * 
	  */
	protected void fillDataFromResultSet(ResultSet rs, Oids foundData)
	{
		if ((null == rs) || (null == foundData))
			return ;
		
		try
		{
    	foundData.setObjectId(rs.getString("OBJECT")) ;
    	foundData.setTraitId(rs.getInt("ID_TRAIT")) ;
    	foundData.setValue(rs.getString("VALUE_TRAIT")) ;
		} 
		catch (SQLException e) {
			Logger.trace("OidsManager.fillDataFromResultSet: exception when processing results set: " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
	}
	
	/**
	 * Lock Oids table for write    
	 * 
	 * @return true if successful
	 * 
	 **/
	private boolean lockTableWrite()
	{
		if (null == _dbConnector)
		{
			Logger.trace("OidsManager.lockTableWrite: uninitialized connector", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		try
		{
			_dbConnector.getStatement().execute("LOCK TABLE oids WRITE") ;
		}
		catch (SQLException ex)
		{				 	
			Logger.trace("OidsManager.lockTableWrite: cannot lock table Oids " + ex.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Unlock Oids table for write    
	 * 
	 * @return true if successful
	 * 
	 **/
	private boolean unlockTable()
	{
		if (null == _dbConnector)
			return false ;
		
		try
		{
			_dbConnector.getStatement().execute("UNLOCK TABLES") ;
		}
		catch (SQLException ex)
		{		
			Logger.trace("OidsManager.unlockTable: exception " + ex.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Get the next Object ID (to be attributed when creating a new object)    
	 * 
	 * @return a String containing the code if successful or "" if not
	 * 
	 **/
	private String getNextPersonCode()
	{
		if (null == _dbConnector)
			return "" ;
		
		if (false == _dbConnector.executeQuery("SELECT MAX(OBJECT) AS MAX_OBJECT FROM oids"))
			return "" ;
		
		ResultSet rs = _dbConnector.getResultSet() ;
		
		String sMax = "" ;
		
		try
    {
	    if (rs.next())
	    {
	    	sMax = rs.getString("MAX_OBJECT") ;
	    	if (null != sMax)	//when no tuple in the table, it however returns a null value
	    		sMax = LdvGraphTools.getNextId(sMax) ;
	    	else
	    		sMax = FIRST_OBJECT ;
	    }
    } 
		catch (SQLException e) {
			Logger.trace("OidsManager.getNextPersonCode: exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
    }
	
		_dbConnector.closeResultSet() ;
		
		return sMax ;
	}
}
