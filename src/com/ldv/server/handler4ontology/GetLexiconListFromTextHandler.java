package com.ldv.server.handler4ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.model.LdvModelLexicon;
import com.ldv.shared.rpc4ontology.GetLexiconListInfo;
import com.ldv.shared.rpc4ontology.GetLexiconListResult;
import com.ldv.shared.util.MiscellanousFcts;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class GetLexiconListFromTextHandler implements ActionHandler<GetLexiconListInfo, GetLexiconListResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public GetLexiconListFromTextHandler(final Log logger,
                                       final Provider<ServletContext> servletContext,       
                                       final Provider<HttpServletRequest> servletRequest)
	{
		super() ;
		
		_logger         = logger ;
		_servletContext = servletContext ;
		_servletRequest = servletRequest ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public GetLexiconListFromTextHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public GetLexiconListResult execute(final GetLexiconListInfo action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sText = action.getStartingText() ;
   		String sLang = action.getLanguage() ;
   		
   		// Creates a connector to Ontology database
   		//
   		DBConnector dbconnector = new DBConnector(true, -1, DBConnector.databaseType.databaseOntology) ;
   		
   		GetLexiconListResult LexiconListResult = new GetLexiconListResult("", action.getBoxIndex()) ;
   		
   		if (true == getLexiconListFromText(dbconnector, sLang, sText, LexiconListResult))
   			return LexiconListResult ;
   		
			return new GetLexiconListResult("Error", action.getBoxIndex()) ;
		}
		catch (Exception cause) 
		{
			_logger.error("Unable to know if pseudo exists", cause) ;
   
			throw new ActionException(cause);
		}
  }

	/**
	 * Look for an entry with a given code in the "lexique" table 
	 * 
	 * @param dbconnector Database connector
	 * @param sCode       Code to be looked for
	 * @param lexicon     Record content
	 * 
	 **/	
	private boolean getLexiconListFromText(DBConnector dbConnector, String sLanguage, String sText, GetLexiconListResult LexiconListResult)
	{
		String sFctName = "GetLexiconListFromTextHandler.getLexiconListFromText" ;
		
		if ((null == dbConnector) || (null == sText) || sText.equals(""))
		{
			Logger.trace(sFctName + ": bad parameter", -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sFlexTableName = "lexiq" ; // "flechies" ;
		if ((null != sLanguage) && (false == sLanguage.equals("")) /* && (false == sLanguage.equals("fr")) */ && MiscellanousFcts.isValidLanguage(sLanguage))
			sFlexTableName += "_" + sLanguage ;
			
		String sqlText = "SELECT * FROM " + sFlexTableName + " WHERE libelle LIKE ?" ;
		
		dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtString(1, sText + "%") ;
				
		if (false == dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sqlText + " and text = " + sText, -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		int iNbRecords = 0 ;
		
		ResultSet rs = dbConnector.getResultSet() ;
		try
		{        
			while (rs.next())
			{
				String sLabel     = rs.getString("LIBELLE") ;
				String sCode      = rs.getString("CODE") ;
				String sGrammar   = rs.getString("GRAMMAIRE") ;
				String sFrequency = rs.getString("FREQ") ;
								
				LdvModelLexicon modelLexicon = new LdvModelLexicon(sCode, sLabel, sGrammar, sFrequency) ;

				LexiconListResult.addLexiconModel(modelLexicon) ;
				
				iNbRecords++ ;
			}
			
			if (0 == iNbRecords)
			{
				Logger.trace(sFctName + ": nothing found for \"" + sText + "\" in " + sFlexTableName, -1, Logger.TraceLevel.SUBSTEP) ;
				dbConnector.closePreparedStatement() ;
				return true ;
			}
		}
		catch(SQLException ex)
		{
			Logger.trace(sFctName + ": DBConnector.dbSelectPreparedStatement: executeQuery failed for preparedStatement " + sqlText, -1, Logger.TraceLevel.ERROR) ;
			Logger.trace(sFctName + ": SQLException: " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace(sFctName + ": SQLState: " + ex.getSQLState(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace(sFctName + ": VendorError: " +ex.getErrorCode(), -1, Logger.TraceLevel.ERROR) ;        
		}
		
		Logger.trace(sFctName + ": found " + iNbRecords + " entries for text " + sText, -1, Logger.TraceLevel.SUBDETAIL) ;
		
		dbConnector.closePreparedStatement() ;
		
		return true ;
	}
		
	@Override
	public void rollback(final GetLexiconListInfo action,
        							 final GetLexiconListResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<GetLexiconListInfo> getActionType()
	{
		return GetLexiconListInfo.class ;
	}
}
