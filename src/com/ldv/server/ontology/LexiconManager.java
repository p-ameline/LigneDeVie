package com.ldv.server.ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.util.MiscellanousFcts;

/** 
 * Object in charge of Read/Write operations in the <code>lexiq_X</code> tables, where X is a language code 
 *   
 */
public class LexiconManager  
{	
	protected final DBConnector _dbConnector ;
	protected final String      _sUserId ;
	
	/**
	 * Constructor 
	 */
	public LexiconManager(String sUserId, final DBConnector dbConnector)
	{
		_dbConnector = dbConnector ;
		_sUserId     = sUserId ;
	}

	/**
	  * Insert a Lexicon object in database
	  * 
	  * @param dataToInsert Lexicon to be inserted
	  * @param sLanguage    Language (as an ISO 639-1 code)
	  *
	  * @return <code>true</code> if successful, <code>false</code> if not
	  */
	public boolean insertData(final Lexicon dataToInsert, final String sLanguage)
	{
		String sFctName = "LexiconManager.insertData" ;
		
		if ((null == _dbConnector) || (null == dataToInsert) || (null == sLanguage) || "".equals(sLanguage))
		{
			Logger.trace(sFctName + ": invalid parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sTableName = getTableForLanguage(sLanguage) ;
		if ("".equals(sTableName))
			return false ;
		
		String sQuery = "INSERT INTO " + sTableName + " (LIBELLE, CODE, GRAMMAIRE, FREQ) VALUES (?, ?, ?, ?)" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToInsert.getLabel()) ;
		_dbConnector.setStatememtString(2, dataToInsert.getCode()) ;
		_dbConnector.setStatememtString(3, dataToInsert.getGrammar()) ;
		_dbConnector.setStatememtString(4, dataToInsert.getFrequency()) ;
		
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		ResultSet rs = _dbConnector.getResultSet() ;
		try
    {
			if (false == rs.next())
				Logger.trace(sFctName + ": cannot get row after query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
    } 
		catch (SQLException e)
    {
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
    }
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
		
		Logger.trace(sFctName +  ": user " + _sUserId + " successfuly recorded lexicon " + dataToInsert.getCode() + ":" + dataToInsert.getLabel() + " in table " + sTableName, _sUserId, Logger.TraceLevel.STEP) ;
		
		return true ;
	}
	
	/**
	  * Update a Lexicon in database
	  * 
	  * @return true if successful, false if not
	  * 
	  * @param dataToUpdate Lexicon to be updated
	  * @param sLanguage    Language (as an ISO 639-1 code)
	  * 
	  */
	public boolean updateData(Lexicon dataToUpdate, final String sLanguage)
	{
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace("LexiconManager.updateData: bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		Lexicon foundData = new Lexicon() ;
		if (false == existData(dataToUpdate.getCode(), foundData, sLanguage))
			return false ;
		
		if (foundData.equals(dataToUpdate))
		{
			Logger.trace("LexiconManager.updateData: Trait to update (" + dataToUpdate.getCode() + " / " + dataToUpdate.getLabel() + ") unchanged; nothing to do", _sUserId, Logger.TraceLevel.SUBSTEP) ;
			return true ;
		}
		
		return forceUpdateData(dataToUpdate, sLanguage) ;
	}
		
	/**
	  * Check if there is any Lexicon with this code in database and, if true get its content
	  * 
	  * @return True if found, else false
	  * 
	  * @param sCode     Code of Lexicon to check
	  * @param foundData Lexicon to get existing information
	  * @param sLanguage Language (as an ISO 639-1 code)
	  * 
	  */
	public boolean existData(final String sCode, Lexicon foundData, final String sLanguage)
	{
		String sFctName = "LexiconManager.existData" ;
		
		if ((null == _dbConnector) || (null == sCode) || (null == foundData))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sTableName = getTableForLanguage(sLanguage) ;
		if ("".equals(sTableName))
			return false ;
		
		String sQuery = "SELECT * FROM " + sTableName + " WHERE CODE = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sCode) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
	   		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no Lexicon found for object = " + sCode, _sUserId, Logger.TraceLevel.WARNING) ;
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
	  * Update a Lexicon in database
	  * 
	  * @return <code>true</code> if creation succeeded, <code>false</code> if not
	  * 
	  * @param  dataToUpdate Lexicon to update
	  * @param sLanguage Language (as an ISO 639-1 code)
	  * 
	  */
	private boolean forceUpdateData(final Lexicon dataToUpdate, final String sLanguage)
	{
		String sFctName = "LexiconManager.forceUpdateData" ;
		
		if ((null == _dbConnector) || (null == dataToUpdate))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sTableName = getTableForLanguage(sLanguage) ;
		if ("".equals(sTableName))
			return false ;
			
		// Prepare SQL query
		//
		String sQuery = "UPDATE " + sTableName + " SET LIBELLE = ?, GRAMMAIRE = ?, FREQ = ?" +
				                          " WHERE CODE = ?" ; 
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, dataToUpdate.getLabel()) ;
		_dbConnector.setStatememtString(2, dataToUpdate.getGrammar()) ;
		_dbConnector.setStatememtString(3, dataToUpdate.getFrequency()) ;
		
		_dbConnector.setStatememtString(4, dataToUpdate.getCode()) ;
				
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}

		Logger.trace(sFctName + ": updated data for Lexicon " + dataToUpdate.getCode(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		_dbConnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	  * Initialize a Lexicon from a query ResultSet 
	  * 
	  * @param rs        ResultSet of a query
	  * @param foundData Lexicon to fill
	  * 
	  */
	protected void fillDataFromResultSet(ResultSet rs, Lexicon foundData)
	{
		if ((null == rs) || (null == foundData))
			return ;
		
		try
		{
    	foundData.setCode(rs.getString("CODE")) ;
    	foundData.setLabel(rs.getString("LIBELLE")) ;
    	foundData.setGrammar(rs.getString("GRAMMAIRE")) ;
    	foundData.setFrequency(rs.getString("FREQ")) ;
		} 
		catch (SQLException e) {
			Logger.trace("LexiconManager.fillDataFromResultSet: exception when processing results set: " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
	}
		
	/**
	 * Get the name of the Lexicon table for a given language    
	 * 
	 * @return a String containing the table name if successful or <code>""</code> if the language is not valid
	 * 
	 **/
	protected String getTableForLanguage(final String sLanguage)
	{
		if ((null == sLanguage) || "".equals(sLanguage) || (false == MiscellanousFcts.isValidLanguage(sLanguage)))
			return "" ;
		
		return "lexiq" + "_" + sLanguage ;
	}
}
