package com.ldv.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.database.Person;
import com.ldv.shared.graph.LdvGraphTools;

public class LdvBackOfficePerson 
{
	private Person _person ;
	
  public static String FIRST_USER  = "0000000" ;
	
	public LdvBackOfficePerson()
	{
		_person = new Person() ;
	}
		
	/**
	 * Initialize this object from the "persons" table record corresponding to an ID    
	 * 
	 * @param iId : ID of the row to be retrieved
	 * @return true if successful
	 * 
	 **/
	public boolean initFromId(int iId)
	{
		DBConnector dbConnector = new DBConnector(true, iId) ;

		String sqlText = "SELECT * FROM persons WHERE id = ?" ;
		
		dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtInt(1, iId) ;
		
		// Execute prepared statement 
		//
		boolean bSuccess = dbConnector.executePreparedStatement() ;
		if (false == bSuccess)
		{
			dbConnector.closeAll() ;
			return false ;
		}
		
		ResultSet rs = dbConnector.getResultSet() ;
		if (null == rs)
		{
			dbConnector.closeAll() ;
			return false ;
		}
		
		boolean bResult = false ;
		try
		{
	    if (rs.next())
	    {
	    	_person.setPersonId(rs.getInt("personId")) ;
	    	_person.setPseudo(rs.getString("pseudo")) ;
	    	_person.setLanguage(rs.getString("language")) ;
	    	_person.setPassword(rs.getString("password")) ;
	    	_person.setLdvId(rs.getString("ldvid"));
	    	_person.setContractType(rs.getString("contractType"));
	    	_person.setContractEndDate(rs.getString("contractEndDate"));
	  	  
	  	  bResult = true ;
	    }
		} 
		catch (SQLException e) {
			Logger.trace("LdvBackOfficePerson.initFromId: exception " + e.getMessage(), -1, Logger.TraceLevel.ERROR) ;
		}
		
		dbConnector.closeAll() ;
   
		return bResult ;
	}
	
	/**
	 * Initialize this object from the "persons" table record corresponding to an ID    
	 * 
	 * @param iId : ID of the row to be retrieved
	 * @return true if successful
	 * 
	 **/
	public boolean initFromPseudoAndPassword(String sLogin, String sPassword)
	{
		if ((null == sLogin) || sLogin.equals("") || (null == sPassword) || sPassword.equals(""))
			return false ;
		
		String sqlText = "SELECT * FROM persons " +
                                    "WHERE pseudo = ? " +
                                      "AND password = ?" ;

		DBConnector dbConnector = new DBConnector(false, -1) ;

		dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtString(1, sLogin) ;
		dbConnector.setStatememtString(2, sPassword) ;
				
		// Execute prepared statement 
		//
		boolean bSuccess = dbConnector.executePreparedStatement() ;
		if (false == bSuccess)
		{
			dbConnector.closeAll() ;
			Logger.trace("LdvBackOfficePerson.initFromPseudoAndPassword: prepared statement execution failed", -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		ResultSet rs = dbConnector.getResultSet() ;
		if (null == rs)
		{	
			dbConnector.closeAll() ;
			return false ;
		}
		
		boolean bResult = false ;
		try
		{
	    if (rs.next())
	    {
	    	_person.setPersonId(rs.getInt("personId")) ;
	    	_person.setPseudo(rs.getString("pseudo")) ;
	    	_person.setLanguage(rs.getString("language")) ;
	    	_person.setPassword(rs.getString("password")) ;
	    	_person.setLdvId(rs.getString("ldvid"));
	    	_person.setContractType(rs.getString("contractType"));
	    	_person.setContractEndDate(rs.getString("contractEndDate"));
	  	  
	  	  bResult = true ;
	    }
		} 
		catch (SQLException e) {
			Logger.trace("LdvBackOfficePerson.initFromPseudoAndPassword: exception " + e.getMessage(), -1, Logger.TraceLevel.ERROR) ;
		}
		
		dbConnector.closeAll() ;
   
		return bResult ;
	}
	
	/**
	 * Create a new person, populated with a LdV Id    
	 * 
	 * @param iId : ID of the row to be retrieved
	 * @return person's record id if successful, -1 if failed
	 * 
	 **/
	public int createNewPerson(DBConnector dbConnector)
	{
		boolean bLocallyCreatedConnector = false ;
		
		if (null == dbConnector)
		{
			dbConnector = new DBConnector(true, -1) ;
			bLocallyCreatedConnector = true ;
		}

		// Lock table
		//
		if (false == lockTableWrite(dbConnector))
		{
			Logger.trace("LdvBackOfficePerson.createNewPerson: cannot lock table Persons; creation failed", -1, Logger.TraceLevel.ERROR) ;
			if (bLocallyCreatedConnector)
				dbConnector.closeAll() ;
			return -1 ;
		}
		
		String sLdvId = getNextPersonCode(dbConnector) ;
		
		if (sLdvId.equals(""))
		{
			unlockTable(dbConnector) ;
			if (bLocallyCreatedConnector)
				dbConnector.closeAll() ;
			return -1 ;
		}
		
		_person.setLdvId(sLdvId) ;
		
		// Date dateNow = new Date() ;
		// SimpleDateFormat ldvFormat = new SimpleDateFormat("yyyyMMddHHmmss") ;
		// _sDatetimeValidate = ldvFormat.format(dateNow) ;
			
		String sQuery = "INSERT INTO persons (ldvid, pseudo, language, password) VALUES (?, ?, ?, ?)" ;
		dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == dbConnector.getPreparedStatement())
		{
			Logger.trace("LdvBackOfficePerson.createNewPerson: cannot get Statement", -1, Logger.TraceLevel.ERROR) ;
			unlockTable(dbConnector) ;
			if (bLocallyCreatedConnector)
				dbConnector.closeAll() ;
			return -1 ;
		}
		
		dbConnector.setStatememtString(1, _person.getLdvId()) ;
		dbConnector.setStatememtString(2, _person.getPseudo()) ;
		dbConnector.setStatememtString(3, _person.getLanguage()) ;
		dbConnector.setStatememtString(4, _person.getPassword()) ;
		
		// Execute query 
		//
		int iNbAffectedRows = dbConnector.executeUpdatePreparedStatement(true) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace("LdvBackOfficePerson.createNewPerson: failed query " + sQuery, -1, Logger.TraceLevel.ERROR) ;
			unlockTable(dbConnector) ;
			if (bLocallyCreatedConnector)
				dbConnector.closeAll() ;
			else
				dbConnector.closePreparedStatement() ;
			return -1 ;
		}
		
		ResultSet rs = dbConnector.getResultSet() ;
		int iNewPersonId = -1 ;
		try
    {
	    if (rs.next())
	    	iNewPersonId = rs.getInt(1) ;
	    else
	    	Logger.trace("LdvBackOfficePerson.createNewPerson: cannot get Id after query " + sQuery, -1, Logger.TraceLevel.ERROR) ;
    } 
		catch (SQLException e)
    {
			Logger.trace("LdvBackOfficePerson.createNewPerson: exception when iterating results " + e.getMessage(), -1, Logger.TraceLevel.ERROR) ;
    }
		dbConnector.closeResultSet() ;
		dbConnector.closePreparedStatement() ;

		unlockTable(dbConnector) ;
		
		if (bLocallyCreatedConnector)
			dbConnector.closeAll() ;
  	
		Logger.trace("LdvBackOfficePerson.createNewPerson: new person (" + iNewPersonId + ") created with LdV Id " + _person.getLdvId(), -1, Logger.TraceLevel.DETAIL) ;
		
   	return iNewPersonId ;
	}

	/**
	 * Lock Persons table for write    
	 * 
	 * @param dbConnector : database connector
	 * @return true if successful
	 * 
	 **/
	private boolean lockTableWrite(DBConnector dbConnector)
	{
		if (null == dbConnector)
			return false ;
		
		try
		{
			dbConnector.getStatement().execute("LOCK TABLE persons WRITE") ;
		}
		catch (SQLException ex)
		{				 	
			Logger.trace("LdvBackOfficePerson.lockTableWrite: cannot lock table Persons " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Lock Persons table for write    
	 * 
	 * @param dbConnector : database connector
	 * @return true if successful
	 * 
	 **/
	private boolean unlockTable(DBConnector dbConnector)
	{
		if (null == dbConnector)
			return false ;
		
		try
		{
			dbConnector.getStatement().execute("UNLOCK TABLES") ;
		}
		catch (SQLException ex)
		{		
			Logger.trace("LdvBackOfficePerson.unlockTable: exception " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		return true ;
	}
	
	/**
	 * Get the next LdV ID (to be attributed when creating a new person)    
	 * 
	 * @param dbConnector : database connector
	 * @return a String containing the code if successful or "" if failure
	 * 
	 **/
	private String getNextPersonCode(DBConnector dbConnector)
	{
		if (null == dbConnector)
			return "" ;
		
		if (false == dbConnector.executeQuery("SELECT MAX(ldvid) AS MAX_PERSON FROM persons"))
			return "" ;
		
		ResultSet rs = dbConnector.getResultSet() ;
		
		String sMax = "" ;
		
		try
    {
	    if (rs.next())
	    {
	    	sMax = rs.getString("MAX_PERSON") ;
	    	if (null != sMax)	//when no tuple in the table, it however returns a null value
	    		sMax = LdvGraphTools.getNextId(sMax) ;
	    	else
	    		sMax = FIRST_USER ;
	    }
    } 
		catch (SQLException e) {
			Logger.trace("LdvBackOfficePerson.getNextUserCode: exception when iterating results " + e.getMessage(), -1, Logger.TraceLevel.ERROR) ;
    }
	
		dbConnector.closeResultSet() ;
		
		return sMax ;
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
	
	public void setId(int iId)
  {
		_person.setPersonId(iId) ;
  }
	public int getId()
  {
	  return _person.getPersonId() ;
  }

	public void setPseudo(String sPseudo)
  {
		_person.setPseudo(sPseudo) ;
  }
	public String getPseudo()
  {
	  return _person.getPseudo() ;
  }

	public void setLanguage(String sLanguage)
  {
		_person.setLanguage(sLanguage) ;
  }
	public String getLanguage()
  {
	  return _person.getLanguage() ;
  }

	public void setPassword(String sPassword)
  {
		_person.setPassword(sPassword) ;
  }
	public String getPassword()
  {
	  return _person.getPassword() ;
  }

	public void setLdvId(String sLdvId)
  {
		_person.setLdvId(sLdvId) ;
  }
	public String getLdvId()
  {
	  return _person.getLdvId() ;
  }

	public String getContractType()
	{
		return _person.getContractType() ;
	}
	public void setContractType(String sContractType)
	{
		_person.setContractType(sContractType) ;
	}

	public String getContractEndDate()
	{
		return _person.getContractEndDate() ;
	}
	public void setContractEndDate(String sContractEndDate)
	{
		_person.setContractEndDate(sContractEndDate) ;
	}
	
	public Person getPerson() { 
		return _person ;
	}
}
