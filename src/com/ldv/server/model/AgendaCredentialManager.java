package com.ldv.server.model;

import org.osaf.caldav4j.credential.CaldavCredential;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.handler4caldav.CyrusCaldavCredential;
import com.ldv.shared.database.Agenda;
import com.ldv.shared.rpc.SessionActionModel;

/** 
 * Object in charge of finding the proper CaldavCredential from information stored in the agenda database 
 *   
 */
public class AgendaCredentialManager  
{	
	public AgendaCredentialManager() {
	}
	
	/**
	  * Insert a UserData in database
	  * 
	  * @return true if successful, false if not
	  * @param user UserData to be inserted
	  * 
	  */
	public static CaldavCredential getCredential(final SessionActionModel session)
	{
		if (null == session)
			return null ;
		
		// String sFctName = "AgendaCredentialManager.getCredential" ;
		
		// Get the Ldv Id of agenda to connect to
		//
		String sUserLdvId   = session.getUserIdentifier() ;
		String sTargetLdvId = session.getLdvIdentifier() ;
		if ("".equals(sTargetLdvId))
			sTargetLdvId = sUserLdvId ;
		
		if ("".equals(sTargetLdvId))
			return null ;
		
		Agenda agenda = getAgenda(sTargetLdvId, session) ;
		if (null == agenda)
			return null ;
		
		return getCredentialForAgenda(agenda, sUserLdvId) ;
	}
		
	/**
	  * Get an agenda record that fits with sTargetLdvId in database
	  * 
	  * @param sTargetLdvId Ldv Id of the person whose agenda is to be found
	  * 
	  * @return the agenda if successful, <code>null</code> if not
	  */
	protected static Agenda getAgenda(final String sTargetLdvId, final SessionActionModel session)
	{
		DBConnector dbConnector = new DBConnector(true, -1) ;
		AgendaManager manager = new AgendaManager(session.getUserIdentifier(), dbConnector) ; 
		
		Agenda agenda = new Agenda() ;
		if (false == manager.findFromLdvId(sTargetLdvId, 0, agenda))
			return null ;
		
		return agenda ;
	}
		
	/**
	  * Return a credential for a given agenda
	  * 
	  * @param agenda Agenda to get gredential for
	  *
	  * @return An object that inherits from CaldavCredential if found, <code>null</code> if not
	  */
	protected static CaldavCredential getCredentialForAgenda(final Agenda agenda, final String sUserIdentifier)
	{
		String sFctName = "AgendaCredentialManager.getCredentialForAgenda" ;
		
		if (null == agenda)
		{
			Logger.trace(sFctName + ": bad parameter", sUserIdentifier, Logger.TraceLevel.ERROR) ;
			return null ;
		}
		
		String sWebDavType = agenda.getAgendaType() ;
		
		if ("cyrus".equals(sWebDavType))
			return new CyrusCaldavCredential(agenda.getAgendaUrl(), agenda.getAgendaIdentifier(), agenda.getAgendaPassword()) ;
		
		Logger.trace(sFctName + ": unknown webdav type " + sWebDavType, sUserIdentifier, Logger.TraceLevel.ERROR) ;
		
		return null ;
	}		
}
