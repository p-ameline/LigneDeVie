package com.ldv.server.ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.ontodatabase.Classif;

/** 
 * Object in charge of Read/Write operations in the <code>classif</code> table (repository of classifications)
 *   
 */
public class ClassifManager  
{	
	protected final DBConnector _dbConnector ;
	protected final String      _sUserId ;
	
	/**
	 * Constructor 
	 */
	public ClassifManager(String sUserId, final DBConnector dbConnector)
	{
		_dbConnector = dbConnector ;
		_sUserId     = sUserId ;
	}
	
	/**
	  * Check if there is any classification element for a given classification and code in database and, if true get its content
	  * 
	  * @return <code>true</code> if found, <code>false</code> if not
	  * 
	  * @param sClassification Lexique semantic code for the classification
	  * @param sCode           Code in the classification
	  * @param foundData       Classif to get existing information
	  * 
	  */
	public boolean findCode(final String sClassification, final String sCode, Classif foundData)
	{
		String sFctName = "ClassifManager.findTrait" ;
		
		if ((null == _dbConnector) || (null == sClassification) || ("".equals(sClassification)) || (null == sCode) || ("".equals(sCode)))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM classif WHERE CLASSIF = ? AND CODE = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		
		_dbConnector.setStatememtString(1, sClassification) ;
		_dbConnector.setStatememtString(2, sCode) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no Classif found for classification = " + sClassification + " and code " + sCode, _sUserId, Logger.TraceLevel.WARNING) ;
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
	  * Insert a Classif object in database
	  * 
	  * @return <code>true</code> if successful, <code>false</code> if not
	  * 
	  * @param dataToInsert Classif to be inserted
	  * 
	  */
	public boolean insertData(final Classif dataToInsert)
	{
		String sFctName = "ClassifManager.insertData" ;
		
		if ((null == _dbConnector) || (null == dataToInsert))
			return false ;
		
		String sQuery = "INSERT INTO classif (CLASSIF, CODE, LIBELLE, CHAP) VALUES (?, ?, ?, ?)" ;
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToInsert.getClassification()) ;
		_dbConnector.setStatememtString(2, dataToInsert.getCode()) ;
		_dbConnector.setStatememtString(3, dataToInsert.getLabel()) ;
		_dbConnector.setStatememtString(4, dataToInsert.getChapter()) ;
		
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
		
		Logger.trace(sFctName + ": user " + _sUserId + " successfuly recorded classif for classification " + dataToInsert.getClassification() + " and code " + dataToInsert.getCode(), _sUserId, Logger.TraceLevel.STEP) ;
		
		return true ;
	}
	
	/**
	  * Update a Classif in database
	  * 
	  * @return true if successful, false if not
	  * 
	  * @param dataToUpdate Classif to be updated
	  * 
	  */
	public boolean updateData(Classif dataToUpdate)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("ClassifManager.updateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		Classif foundData = new Classif() ;
		if (false == findCode(dataToUpdate.getClassification(), dataToUpdate.getCode(), foundData))
			return false ;
		
		if (foundData.equals(dataToUpdate))
		{
			Logger.trace("ClassifManager.updateData: Code to update (" + dataToUpdate.getClassification() + " / " + dataToUpdate.getCode() + ") unchanged; nothing to do", _sUserId, Logger.TraceLevel.SUBSTEP) ;
			return true ;
		}
		
		return forceUpdateData(dataToUpdate) ;
	}
		
	/**
	  * Update a Classif in database
	  * 
	  * @return <code>true</code> if creation succeeded, <code>false</code> if not
	  * 
	  * @param  dataToUpdate Classif to update
	  * 
	  */
	private boolean forceUpdateData(final Classif dataToUpdate)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("OidsManager.forceUpdateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Prepare SQL query
		//
		String sQuery = "UPDATE oids SET LIBELLE = ?, CHAP = ?" +
				                          " WHERE CLASSIF = ? AND CODE = ?" ; 
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace("OidsManager.forceUpdateData: cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToUpdate.getLabel()) ;
		_dbConnector.setStatememtString(2, dataToUpdate.getChapter()) ;
		_dbConnector.setStatememtString(3, dataToUpdate.getClassification()) ;
		_dbConnector.setStatememtString(4, dataToUpdate.getCode()) ;
				
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("OidsManager.forceUpdateData: failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}

		Logger.trace("OidsManager.forceUpdateData: updated data for Classif " + dataToUpdate.getClassification() + " / " + dataToUpdate.getCode(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		_dbConnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	  * Fill a structure with all the different codes for a given classification 
	  * 
	  * @param sClassificatin Lexique semantic code of the classification
	  * @param aElements      Array of Classif to be filled
	  * 
	  */
	public void getClassificationElements(final String sClassification, Vector<Classif> aElements)
	{
		String sFctName = "ClassifManager.getClassificationElements" ;
		
		if ((null == _dbConnector) || (null == sClassification) || "".equals(sClassification) || (null == aElements))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return ;
		}
		
		String sQuery = "SELECT * FROM classif WHERE CLASSIF = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sClassification) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no element found for classification " + sClassification, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return ;
		}
		
		try
		{
	    while (rs.next())
	    {
	    	Classif foundData = new Classif() ;
	    	fillDataFromResultSet(rs, foundData) ;
	    	aElements.add(foundData) ;
	    }
		} catch (SQLException e)
		{
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
	}
	
	/**
	  * Initialize a Classif from a query ResultSet 
	  * 
	  * @param rs        ResultSet of a query
	  * @param foundData Classif to fill
	  * 
	  */
	protected void fillDataFromResultSet(ResultSet rs, Classif foundData)
	{
		if ((null == rs) || (null == foundData))
			return ;
		
		try
		{
    	foundData.setClassification(rs.getString("CLASSIF")) ;
    	foundData.setCode(rs.getString("CODE")) ;
    	foundData.setLabel(rs.getString("LIBELLE")) ;
    	foundData.setChapter(rs.getString("CHAP")) ;
		} 
		catch (SQLException e) {
			Logger.trace("OidsManager.fillDataFromResultSet: exception when processing results set: " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
	}	
}
