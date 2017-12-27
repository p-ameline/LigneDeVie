package com.ldv.server.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.database.Agenda;

/** 
 * Object in charge of Read/Write operations in the <code>user</code> table 
 *   
 */
public class AgendaManager  
{	
	protected final DBConnector _dbConnector ;
	protected final String      _sUserId ;
	
	public AgendaManager(final String sUserId, final DBConnector dbConnector) 
	{
		_dbConnector = dbConnector ;
		_sUserId     = sUserId ;
	}
	
	/**
	  * Insert a UserData in database
	  * 
	  * @return true if successful, false if not
	  * @param user UserData to be inserted
	  * 
	  */
	public boolean insert(Agenda agenda)
	{
		if ((null == _dbConnector) || (null == agenda))
			return false ;
		
		String sFctName = "AgendaManager.insert" ;
		
		String sQuery = "INSERT INTO agenda (ldvid, pro, uep, type, url, identifier, password) VALUES (?, ?, ?, ?, ?, ?, ?)" ;
		_dbConnector.prepareStatememt(sQuery, Statement.RETURN_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closeAll() ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, agenda.getLdvId()) ;
		_dbConnector.setStatememtInt(2,    agenda.isPro()) ;
		_dbConnector.setStatememtInt(3,    agenda.getUEP()) ;
		_dbConnector.setStatememtString(4, agenda.getAgendaType()) ;
		_dbConnector.setStatememtString(5, agenda.getAgendaUrl()) ;
		_dbConnector.setStatememtString(6, agenda.getAgendaIdentifier()) ;
		_dbConnector.setStatememtString(7, agenda.getAgendaPassword()) ;
		
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
			if (rs.next())
				agenda.setAgendaId(rs.getInt(1)) ;
			else
				Logger.trace(sFctName + ": cannot get Id after query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
    } 
		catch (SQLException e)
    {
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
    }
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
		
		Logger.trace(sFctName + ": agenda " + agenda.getAgendaId() + " successfuly recorded for ldv " + agenda.getLdvId(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		return true ;
	}
		
	/**
	  * Update an agenda record in database
	  * 
	  * @param agendaToUpdate Agenda to be updated
	  * @return true if successful, false if not
	  */
	public boolean update(Agenda agendaToUpdate)
	{
		String sFctName = "AgendaManager.update" ;
		
		if ((null == _dbConnector) || (null == agendaToUpdate))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		// Find this user in database
		//
		Agenda foundAgenda = new Agenda() ;
		if (false == exist(agendaToUpdate.getAgendaId(), foundAgenda))
			return false ;
		
		// If nothing to change, just leave
		//
		if (foundAgenda.equals(agendaToUpdate))
		{
			Logger.trace(sFctName + ": agenda to update (id = " + agendaToUpdate.getAgendaId() + ") unchanged; nothing to do", _sUserId, Logger.TraceLevel.SUBSTEP) ;
			return true ;
		}
		
		return forceUpdate(agendaToUpdate) ;
	}
		
	/**
	  * Check if there is any agenda with this Id in database and, if true get its content
	  * 
	  * @param iAgendaId   ID of agenda to check
	  * @param foundAgenda Agenda to fill
	  *
	  * @return <code>true</code> if found, <code>false</code> if not
	  */
	public boolean exist(int iAgendaId, Agenda foundAgenda)
	{
		String sFctName = "AgendaManager.exist" ;
		
		if ((null == _dbConnector) || (-1 == iAgendaId) || (null == foundAgenda))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM agendas WHERE agendaId = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtInt(1, iAgendaId) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no agenda found for id = " + iAgendaId, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		try
		{
	    if (rs.next())
	    {
	    	fillDataFromResultSet(rs, foundAgenda) ;
	    	
	    	_dbConnector.closeResultSet() ;
	    	_dbConnector.closePreparedStatement() ;
	    	return true ;	    	
	    }
	    else
			{
	    	Logger.trace(sFctName + ": nothing found in table agenda for agenda " + iAgendaId, _sUserId, Logger.TraceLevel.WARNING) ;
	    	_dbConnector.closePreparedStatement() ;
				return false ;
			}
		} 
		catch (SQLException e)
		{
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
				
		return false ;
	}
	
	/**
	  * Check if there is any agenda with this Id in database and, if true get its content
	  * 
	  * @param iAgendaId   ID of agenda to check
	  * @param foundAgenda Agenda to fill
	  *
	  * @return <code>true</code> if found, <code>false</code> if not
	  */
	public boolean findFromLdvId(final String sLdvId, int isPro, Agenda foundAgenda)
	{
		String sFctName = "AgendaManager.findFromLdvId" ;
		
		if ((null == _dbConnector) || (null == sLdvId) || "".equals(sLdvId) || (null == foundAgenda))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sQuery = "SELECT * FROM agendas WHERE ldvid = ? AND pro = ?" ;
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		_dbConnector.setStatememtString(1, sLdvId) ;
		_dbConnector.setStatememtInt(2, isPro) ;
	   		
		if (false == _dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		ResultSet rs = _dbConnector.getResultSet() ;
		if (null == rs)
		{
			Logger.trace(sFctName + ": no agenda found for LdvId = " + sLdvId + " and pro = " + isPro, _sUserId, Logger.TraceLevel.WARNING) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		try
		{
	    if (rs.next())
	    {
	    	fillDataFromResultSet(rs, foundAgenda) ;
	    	
	    	_dbConnector.closeResultSet() ;
	    	_dbConnector.closePreparedStatement() ;
	    	return true ;	    	
	    }
	    else
			{
	    	Logger.trace(sFctName + ": nothing found in table agenda for LdvId = " + sLdvId + " and pro = " + isPro, _sUserId, Logger.TraceLevel.WARNING) ;
	    	_dbConnector.closePreparedStatement() ;
				return false ;
			}
		} 
		catch (SQLException e)
		{
			Logger.trace(sFctName + ": exception when iterating results " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
		
		_dbConnector.closeResultSet() ;
		_dbConnector.closePreparedStatement() ;
				
		return false ;
	}
		
	/**
	  * Update an Agenda
	  * 
	  * @param agendaToUpdate Agenda to update
	  * 
	  * @return <code>true</code> if update succeeded, <code>false</code> if not
	  */
	private boolean forceUpdate(Agenda agendaToUpdate)
	{
		String sFctName = "AgendaManager.forceUpdate" ;
		
		if ((null == _dbConnector) || (null == agendaToUpdate))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
			
		// Prepare sql query
		//
		String sQuery = "UPDATE agendas SET ldvid = ?, pro = ?, uep = ?, type = ?, url = ?, identifier = ?, password = ?" +
				                          " WHERE " +
				                               "id = '" + agendaToUpdate.getAgendaId() + "'" ; 
		
		_dbConnector.prepareStatememt(sQuery, Statement.NO_GENERATED_KEYS) ;
		if (null == _dbConnector.getPreparedStatement())
		{
			Logger.trace(sFctName + ": cannot get Statement", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		_dbConnector.setStatememtString(1, agendaToUpdate.getLdvId()) ;
		_dbConnector.setStatememtInt(2,    agendaToUpdate.isPro()) ;
		_dbConnector.setStatememtInt(3,    agendaToUpdate.getUEP()) ;
		_dbConnector.setStatememtString(4, agendaToUpdate.getAgendaType()) ;
		_dbConnector.setStatememtString(5, agendaToUpdate.getAgendaUrl()) ;
		_dbConnector.setStatememtString(6, agendaToUpdate.getAgendaIdentifier()) ;
		_dbConnector.setStatememtString(7, agendaToUpdate.getAgendaPassword()) ;
				
		// Execute query 
		//
		int iNbAffectedRows = _dbConnector.executeUpdatePreparedStatement(false) ;
		if (-1 == iNbAffectedRows)
		{
			Logger.trace(sFctName + ": failed query " + sQuery, _sUserId, Logger.TraceLevel.ERROR) ;
			_dbConnector.closePreparedStatement() ;
			return false ;
		}

		Logger.trace(sFctName + ": updated agenda for id " + agendaToUpdate.getAgendaId(), _sUserId, Logger.TraceLevel.SUBSTEP) ;
		
		_dbConnector.closePreparedStatement() ;
		
		return true ;
	}
	
	/**
	  * Initialize a UserData from a query ResultSet 
	  * 
	  * @param rs        ResultSet of a query
	  * @param foundData UserData to fill
	  * 
	  */
	public void fillDataFromResultSet(ResultSet rs, Agenda foundData)
	{
		if ((null == rs) || (null == foundData))
			return ;
		
		try
		{
			foundData.setAgendaId(rs.getInt("agendaId")) ;
			foundData.setLdvId(rs.getString("ldvid")) ;
			foundData.setIsPro(rs.getInt("pro")) ;
			foundData.setUEP(rs.getInt("uep")) ;
			foundData.setAgendaType(rs.getString("type")) ;
			foundData.setAgendaUrl(rs.getString("url")) ;
			foundData.setAgendaIdentifier(rs.getString("identifier")) ;
			foundData.setAgendaPassword(rs.getString("password")) ;
		} 
		catch (SQLException e) {
			Logger.trace("AgendaManager.fillDataFromResultSet: exception when processing results set: " + e.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
		}
	}
}
