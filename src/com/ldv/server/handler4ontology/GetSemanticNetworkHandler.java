package com.ldv.server.handler4ontology;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.customware.gwt.dispatch.server.ActionHandler;
import net.customware.gwt.dispatch.server.ExecutionContext;
import net.customware.gwt.dispatch.shared.ActionException;
import net.customware.gwt.dispatch.shared.DispatchException;

import org.apache.commons.logging.Log;

import com.ldv.server.DBConnector;
import com.ldv.server.Logger;
import com.ldv.server.ontology.LexiconManager;
import com.ldv.shared.database.Lexicon;
import com.ldv.shared.database.Savoir;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.model.LdvModelLexicon;
import com.ldv.shared.rpc4ontology.GetSemanticNetworkInfo;
import com.ldv.shared.rpc4ontology.GetSemanticNetworkResult;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GetSemanticNetworkHandler implements ActionHandler<GetSemanticNetworkInfo, GetSemanticNetworkResult>
{
	protected final Log                          _logger ;
	protected final Provider<ServletContext>     _servletContext ;
	protected final Provider<HttpServletRequest> _servletRequest ;

	protected       int                          _maximalDepth ;
	
	protected       String                       _sUserId ;
	protected       String                       _sLanguage ;
	
	@Inject
	public GetSemanticNetworkHandler(final Log logger,
                                   final Provider<ServletContext> servletContext,       
                                   final Provider<HttpServletRequest> servletRequest)
	{
		super() ;
		
		_logger         = logger ;
		_servletContext = servletContext ;
		_servletRequest = servletRequest ;
		
		_maximalDepth   = 0 ;
		
		_sUserId        = "" ;
		_sLanguage      = "" ;
	}
	
	/**
	  * Constructor dedicated to unit tests 
	  */
	public GetSemanticNetworkHandler()
	{
		super() ;
		
		_logger         = null ;
		_servletContext = null ;
		_servletRequest = null ;
		
		_maximalDepth   = 0 ;
		
		_sUserId        = "" ;
		_sLanguage      = "" ;
	}

	@Override
	public GetSemanticNetworkResult execute(final GetSemanticNetworkInfo action, final ExecutionContext context) throws ActionException 
  {	
		try 
		{			
			_sUserId   = action.getUserId() ;
			_sLanguage = action.getLanguage() ;
			
   		String sSemanticCode = action.getSemanticCode() ;
   		String sLang         = action.getLanguage() ;
   		
   		_maximalDepth = action.getNetworkDepth() ;
   		
   		// Creates a connector to Ontology database
   		//
   		DBConnector dbconnector = new DBConnector(true, -1, DBConnector.databaseType.databaseOntology) ;
   		
   		GetSemanticNetworkResult semanticNetworkResult = new GetSemanticNetworkResult("") ;
   		
   		// List of all concepts that were encountered (and, for example, which label must found in order to display the results)
   		//
   		ArrayList<String> aProcessedItems = new ArrayList<String>() ;
   		aProcessedItems.add(sSemanticCode) ;
   		
   		// List of items to be processed at a given "depth" (at first, it only contains a single element)
   		//
   		ArrayList<String> aItemsToLookFor = new ArrayList<String>() ;
   		aItemsToLookFor.add(sSemanticCode) ;
   		
   		// Process "depth by depth" from the list of items to be processed for that depth
   		//
   		int iCurrentDepth = 0 ;
   		while (iCurrentDepth < _maximalDepth)
   		{
   			// List of concepts that will be discovered at this depth (and will be processed during next step)
   			//
   			ArrayList<String> aDiscoveredItems = new ArrayList<String>() ;
   			
   			if (false == getSemanticNetworkResult(dbconnector, sLang, iCurrentDepth, aItemsToLookFor, aDiscoveredItems, aProcessedItems, action.isReverse(), semanticNetworkResult))
   				return semanticNetworkResult ;
   			
   			iCurrentDepth++ ;
   			
   			if (iCurrentDepth < _maximalDepth)
   			{
   				aItemsToLookFor.clear() ;
   				aItemsToLookFor.addAll(aDiscoveredItems) ;
   			}
   		}
   		
   		// Get the label of all encountered elements
   		//
   		if (false == aProcessedItems.isEmpty())
   			getLabels(dbconnector, sLang, aProcessedItems, semanticNetworkResult) ;
   		
   		return semanticNetworkResult ;
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
	private boolean getSemanticNetworkResult(DBConnector dbConnector, String sLanguage, int iCurrentDepth, ArrayList<String> aItemsToLookFor, ArrayList<String> aDiscoveredItems, ArrayList<String> aProcessedItems, boolean bReverse, GetSemanticNetworkResult semanticNetworkResult)
	{
		if ((null == aItemsToLookFor) || aItemsToLookFor.isEmpty())
			return false ;
		
		// Iterate through all items to look for
		//
		for (Iterator<String> itr = aItemsToLookFor.iterator() ; itr.hasNext() ; )
		{
			String sItem = itr.next() ;
			
			// Get all traits from/to this item in the semantic network
			//
			ArrayList<Savoir> aNetwork = new ArrayList<Savoir>() ;
			if (false == getSemanticNetwork(dbConnector, sItem, aNetwork, bReverse))
				return false ;
			
			if (false == aNetwork.isEmpty())
			{
				// Stores into the result all traits that are not already inside it
				//
				for (Iterator<Savoir> traitItr = aNetwork.iterator() ; traitItr.hasNext() ; )
				{
					Savoir trait = traitItr.next() ;
					if (isNewInResults(semanticNetworkResult, trait))
					{
						semanticNetworkResult.addTrait(trait) ;
						addTraitInProcessedItems(aProcessedItems, aDiscoveredItems, trait, bReverse) ;
					}
				}
			}
		}
		
		return true ;
	}
	
	/**
	 * Get all the traits from/to this node in the semantic network 
	 * 
	 * @param dbConnector Database connector
	 * @param sItem       Node to get traits from/to
	 * @param aNetwork    Repository of found traits
	 * @param bReverse    If <code>true</code>, then get traits that point to the node instead of those that start from it 
	 * 
	 * @return <code>true</code> if everything went well, <code>false</code> if not
	 * 
	 **/	
	private boolean getSemanticNetwork(DBConnector dbConnector, String sItem, ArrayList<Savoir> aNetwork, boolean bReverse)
	{
		String sFctName = "GetSemanticNetworkHandler.getSemanticNetwork" ;
		
		if ((null == dbConnector) || (null == aNetwork) || (null == sItem) || "".equals(sItem))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		String sqlText    = "SELECT * FROM savoir WHERE QUALIFIE = ?" ;
		String sDirection = " in regular direction" ;
		if (bReverse)
		{
			sqlText    = "SELECT * FROM savoir WHERE QUALIFIANT = ?" ;
			sDirection = " in reverse direction" ;
		}
		
		dbConnector.prepareStatememt(sqlText, Statement.NO_GENERATED_KEYS) ;
		dbConnector.setStatememtString(1, sItem) ;
				
		if (false == dbConnector.executePreparedStatement())
		{
			Logger.trace(sFctName + ": failed query " + sqlText + " and item = " + sItem, _sUserId, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		int iNbRecords = 0 ;
		
		ResultSet rs = dbConnector.getResultSet() ;
		try
		{        
			while (rs.next())
			{
				String sCode      = rs.getString("CODE") ;
				String sQualified = rs.getString("QUALIFIE") ;
				String sLink      = rs.getString("LIEN") ;
				String sQualifier = rs.getString("QUALIFIANT") ;
								
				Savoir trait = new Savoir(sCode, sQualified, sLink, sQualifier) ;

				aNetwork.add(trait) ;
				
				iNbRecords++ ;
			}
			
			if (0 == iNbRecords)
			{
				Logger.trace(sFctName + ": no trait found for item \"" + sItem + "\"" + sDirection, _sUserId, Logger.TraceLevel.SUBSTEP) ;
				dbConnector.closePreparedStatement() ;
				return true ;
			}
		}
		catch(SQLException ex)
		{
			Logger.trace(sFctName + ": DBConnector.dbSelectPreparedStatement: executeQuery failed for preparedStatement " + sqlText, _sUserId, Logger.TraceLevel.ERROR) ;
			Logger.trace(sFctName + ": SQLException: " + ex.getMessage(), _sUserId, Logger.TraceLevel.ERROR) ;
			Logger.trace(sFctName + ": SQLState: " + ex.getSQLState(), _sUserId, Logger.TraceLevel.ERROR) ;
			Logger.trace(sFctName + ": VendorError: " +ex.getErrorCode(), _sUserId, Logger.TraceLevel.ERROR) ;
			dbConnector.closePreparedStatement() ;
			return false ;
		}
		
		Logger.trace(sFctName + ": found " + iNbRecords + " traits for code " + sItem + sDirection, _sUserId, Logger.TraceLevel.SUBDETAIL) ;
		
		dbConnector.closePreparedStatement() ;
		
		return true ;
	}
		
	/**
	 * Get the labels for all concepts that have been encountered during the process 
	 * 
	 * @param dbConnector           Database connector
	 * @param sLang                 Language the labels are to be found for
	 * @param aProcessedItems       List of semantic concepts that we have to find a label for
	 * @param semanticNetworkResult Result object to be completed with all the labels 
	 * 
	 **/
	private void getLabels(DBConnector dbConnector, final String sLang, ArrayList<String> aProcessedItems, GetSemanticNetworkResult semanticNetworkResult)
	{
		String sFctName = "GetSemanticNetworkHandler.getLabels" ;
		
		if ((null == dbConnector) || (null == aProcessedItems) || (null == semanticNetworkResult))
		{
			Logger.trace(sFctName + ": bad parameter", _sUserId, Logger.TraceLevel.ERROR) ;
			return ;
		}
		
		if (aProcessedItems.isEmpty())
			return ;
		
		LexiconManager lexiconManager = new LexiconManager(_sUserId, dbConnector) ;
		
		for (Iterator<String> it = aProcessedItems.iterator() ; it.hasNext() ; )
		{
			Lexicon lexicon = new Lexicon() ;
			
			if (lexiconManager.existData(LdvModelNode.getFirstTermCode(it.next()), lexicon, _sLanguage))
				semanticNetworkResult.addLexiconModel(new LdvModelLexicon(lexicon.getCode(), lexicon.getLabel(), lexicon.getGrammar(), lexicon.getFrequency()));
		}
	}
	
	/**
	 * Check if a trait already exists in the results 
	 * 
	 * @param semanticNetworkResult Result object to process
	 * @param trait                 Trait to look for inside the results
	 * 
	 * @return <code>true</code> if the trait was found, <code>false</code> if not
	 * 
	 **/
	protected boolean isNewInResults(final GetSemanticNetworkResult semanticNetworkResult, final Savoir trait)
	{
		if (null == semanticNetworkResult)
			return true ;
		
		ArrayList<Savoir> results = semanticNetworkResult.getNetwork() ;
		if ((null == results) || results.isEmpty())
			return true ;
		
		if ((null == trait) || results.contains(trait))
			return false ;
		
		return true ;
	}
	
	/**
	 * Add the qualifier or the qualified (depending if we process in regular or reverse direction) to the list of
	 * already visited concepts, if it is not already there.
	 * 
	 * @param aProcessedItems List of concepts that have already been visited
	 * @param trait           Trait that is currently being processed
	 * @param bReverse        If <code>true</code>, then process trait's qualified, else (regular direction) process qualifier
	 * 
	 **/
	private void addTraitInProcessedItems(ArrayList<String> aProcessedItems, ArrayList<String> aNewItemsToLookFor, final Savoir trait, boolean bReverse)
	{
		if ((null == aProcessedItems) || (null == trait))
			return ;
		
		// Get the concept that was just "encountered".
		//
		// In the regular case, we start from a concept X and look for other concepts Y that are of the form X -isA-> Y
		// Hence we "discover" Y i.e. the qualifier
		//
		String sConcept = trait.getQualifier() ;
		if (bReverse)
			sConcept = trait.getQualified() ;
		
		if ("".equals(sConcept))
			return ;
		
		// Add this concept to the lists if it is not already there 
		//
		if (false == aProcessedItems.contains(sConcept))
			aProcessedItems.add(sConcept) ;
		
		if (false == aNewItemsToLookFor.contains(sConcept))
			aNewItemsToLookFor.add(sConcept) ;
	}
	
	@Override
	public Class<GetSemanticNetworkInfo> getActionType() {
		return GetSemanticNetworkInfo.class ;
	}

	@Override
	public void rollback(GetSemanticNetworkInfo arg0, GetSemanticNetworkResult arg1, ExecutionContext arg2)
			throws DispatchException {
		// TODO Auto-generated method stub
		
	}
}
