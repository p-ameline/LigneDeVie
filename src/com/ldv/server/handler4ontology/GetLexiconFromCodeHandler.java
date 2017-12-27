package com.ldv.server.handler4ontology;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.rpc4ontology.GetLexiconAction;
import com.ldv.shared.rpc4ontology.GetLexiconResult;
import com.ldv.shared.util.MiscellanousFcts;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Get a Lexicon record from its code 
 * 
 **/	
public class GetLexiconFromCodeHandler implements ActionHandler<GetLexiconAction, GetLexiconResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;
	
	@Inject
	public GetLexiconFromCodeHandler(final Log logger,
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
	public GetLexiconFromCodeHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
	}

	@Override
	public GetLexiconResult execute(final GetLexiconAction action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
   		String sCode = action.getCode() ;
   		String sLang = action.getLang() ;
   		
   		// Creates a connector to Ontology database
   		//
   		DBConnector dbconnector = new DBConnector(true, -1, DBConnector.databaseType.databaseOntology) ;
   		
   		Lexicon lexicon = new Lexicon() ;
   		
   		if (true == getLexiconFromCode(dbconnector, sCode, sLang, lexicon))
   			return new GetLexiconResult(true, lexicon, "", action.getNodeID()) ;
   		
			return new GetLexiconResult(false, (Lexicon) null, "", action.getNodeID()) ;
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
	private boolean getLexiconFromCode(DBConnector dbconnector, final String sCode, final String sLang, Lexicon lexicon)
	{
		if ((null == dbconnector) || (null == sCode) || sCode.equals(""))
		{
			Logger.trace("GetLexiconFromCodeHandler.getLexiconFromCode: bad parameter", -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sFlexTableName = "lexiq" ;
		if ((null != sLang) && (false == sLang.equals("")) /* && (false == sLanguage.equals("fr")) */ && MiscellanousFcts.isValidLanguage(sLang))
			sFlexTableName += "_" + sLang ;
		
		String sqlText = "SELECT * FROM " + sFlexTableName + " WHERE code = ?" ;
		
		dbconnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbconnector.setStatememtString(1, sCode) ;
				
		try
		{
			Map<String, String> userInfo = dbconnector.dbSelectPreparedStatement() ;
		
			if ((null == userInfo) || userInfo.isEmpty())
			{
				Logger.trace("GetLexiconFromCodeHandler.getLexiconFromCode: code not found " + sCode, -1, Logger.TraceLevel.SUBSTEP) ;
				dbconnector.closePreparedStatement() ;
				return false ;
			}
			
			lexicon.setLabel(userInfo.get("LIBELLE")) ;
			lexicon.setCode(userInfo.get("CODE")) ;
			lexicon.setGrammar(userInfo.get("GRAMMAIRE")) ;
			lexicon.setFrequency(userInfo.get("FREQ")) ;
		}
		catch(SQLException ex)
		{
			Logger.trace("GetLexiconFromCodeHandler.getLexiconFromCode: executeQuery failed for preparedStatement " + sqlText + " for code " + sCode, -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLException: " + ex.getMessage(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("SQLState: " + ex.getSQLState(), -1, Logger.TraceLevel.ERROR) ;
			Logger.trace("VendorError: " +ex.getErrorCode(), -1, Logger.TraceLevel.ERROR) ;        
		}
		
		Logger.trace("GetLexiconFromCodeHandler.getLexiconFromCode: found pseudo " + sCode, -1, Logger.TraceLevel.SUBDETAIL) ;
		
		dbconnector.closePreparedStatement() ;
		
		return true ;
	}
		
	@Override
	public void rollback(final GetLexiconAction action,
        							 final GetLexiconResult result,
                       final ExecutionContext context) throws ActionException
  {
		// Nothing to do here
  }
 
	@Override
	public Class<GetLexiconAction> getActionType()
	{
		return GetLexiconAction.class ;
	}
}
