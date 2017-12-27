package com.ldv.server.handler4ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;

import com.ldv.shared.model.LdvModelLexicon;
import com.ldv.shared.rpc4ontology.GetSynonymsListInfo;
import com.ldv.shared.rpc4ontology.GetSynonymsListResult;
import com.ldv.shared.util.MiscellanousFcts;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GetSynonymsHandler implements ActionHandler<GetSynonymsListInfo, GetSynonymsListResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public GetSynonymsHandler(final Log logger,
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
	public GetSynonymsHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public GetSynonymsListResult execute(final GetSynonymsListInfo action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sSemanticCode = action.getSemanticCode() ;
   		String sLang         = action.getLanguage() ;
   		
   		// Creates a connector to Ontology database
   		//
   		DBConnector dbconnector = new DBConnector(true, -1, DBConnector.databaseType.databaseOntology) ;
   		
   		GetSynonymsListResult SynonymsListResult = new GetSynonymsListResult("") ;
   		
   		if (true == getSynonyms(dbconnector, sLang, sSemanticCode, SynonymsListResult))
   			return SynonymsListResult ;
   		
			return new GetSynonymsListResult("Error") ;
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
	private boolean getSynonyms(DBConnector dbConnector, String sLanguage, String sSemanticCode, GetSynonymsListResult synonymsListResult)
	{
		String sFctName = "GetSynonymsHandler.getSynonyms" ;
		
		if ((null == dbConnector) || (null == sSemanticCode) || "".equals(sSemanticCode))
		{
			Logger.trace(sFctName + ": bad parameter", -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sFlexTableName = "lexiq" ; // lexicon tables root ;
		if ((null != sLanguage) && (false == sLanguage.equals("")) /* && (false == sLanguage.equals("fr")) */ && MiscellanousFcts.isValidLanguage(sLanguage))
			sFlexTableName += "_" + sLanguage ;
			
		String sqlText = "SELECT * FROM " + sFlexTableName + " WHERE CODE LIKE ?" ;
		
		dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtString(1, sSemanticCode + "%") ;
				
		if (false == dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sqlText + " and code = " + sSemanticCode, -1, Logger.TraceLevel.ERROR) ;
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

				synonymsListResult.addLexiconModel(modelLexicon) ;
				
				iNbRecords++ ;
			}
			
			if (0 == iNbRecords)
			{
				Logger.trace(sFctName + ": no synonym found for code \"" + sSemanticCode + "\" in " + sFlexTableName, -1, Logger.TraceLevel.SUBSTEP) ;
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
		
		Logger.trace(sFctName + ": found " + iNbRecords + " synonyms for code " + sSemanticCode, -1, Logger.TraceLevel.SUBDETAIL) ;
		
		dbConnector.closePreparedStatement() ;
		
		return true ;
	}
		
	@Override
	public Class<GetSynonymsListInfo> getActionType() {
		return GetSynonymsListInfo.class ;
	}

	@Override
	public void rollback(GetSynonymsListInfo arg0, GetSynonymsListResult arg1, ExecutionContext arg2)
			throws DispatchException {
		// TODO Auto-generated method stub
		
	}
}
