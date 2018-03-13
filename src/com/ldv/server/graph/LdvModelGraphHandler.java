package com.ldv.server.graph;

import java.util.Vector;

import com.ldv.server.model.LdvXmlGraph;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelTree;

/**
 * An object, derived from LdvModelGraph (the graph as it travels from server to client),
 * used to operate on "itself" (load, update, etc) 
 * 
 **/
public class LdvModelGraphHandler extends LdvModelGraph
{
	public LdvModelGraphHandler(NSGRAPHTYPE graphType)
	{
		_graphType = graphType ;
		init() ; 
	}
	
	public LdvModelGraphHandler()
	{
		_graphType = NSGRAPHTYPE.personGraph ;
		init() ; 
	}
			
	/**
	 * Open the graph for a given graph Id, as a defined user
	 * 
	 * @param sGraphId        Person or object identifier whose graph is to be opened
	 * @param sUserIdentifier Person identifier of the requester
	 * @param sTraceFile      Trace file full name
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean openGraph(final String sGraphId, String sUserIdentifier, String sFilesDir, String sDirSeparator)
	{
		if ((null == sGraphId) || "".equals(sGraphId))
			return false ;
		
		setBasicRootID(sGraphId) ;
		
		LdvXmlGraph xmlGraph = new LdvXmlGraph(LdvGraphConfig.COLLECTIVE_SERVER, sGraphId, sUserIdentifier) ;
 		
		if (NSGRAPHTYPE.personGraph == _graphType)
			return xmlGraph.openGraph((LdvModelGraph) this, sFilesDir, sDirSeparator) ;
		else
			return xmlGraph.openObjectGraph((LdvModelGraph) this, sFilesDir, sDirSeparator) ;
	}
	
	/**
   * This methods sets a graph for a given person. It updates the database. successfully  
   * 
   * @param person. The person
   */
	public boolean writeGraph(final String sGraphId, final String sUserIdentifier, final String sFilesDir, 
			                      final String sDirSeparator, LdvModelGraph newGraph, Vector<LdvGraphMapping> aMappings)
	{
		if ((null == sGraphId) || "".equals(sGraphId))
			return false ;
		
		if ((null == newGraph))
			return false ;
		
		// First, open the graph
		//
		setBasicRootID(sGraphId) ;
		
		LdvXmlGraph xmlGraph = new LdvXmlGraph(LdvGraphConfig.COLLECTIVE_SERVER, sGraphId, sUserIdentifier) ;
 		
/*
		boolean bSuccessfullyOpened = false ;
		if (NSGRAPHTYPE.personGraph == _graphType)
			bSuccessfullyOpened = xmlGraph.openGraph((LdvModelGraph) this, sFilesDir, sDirSeparator) ;
		else
			bSuccessfullyOpened = xmlGraph.openObjectGraph((LdvModelGraph) this, sFilesDir, sDirSeparator) ;
		
		if (false == bSuccessfullyOpened)
			return false ;
*/
		
		// Then update it
		//
		return xmlGraph.writeGraph(newGraph, (LdvModelGraph) this, sFilesDir, sDirSeparator, aMappings) ;
	}
	
	/**
	 * Open the graph for a given graph Id, as a defined user
	 * 
	 * @param sGraphId        Person or object identifier whose graph is to be opened
	 * @param sUserIdentifier Person identifier of the requester
	 * @param sTraceFile      Trace file full name
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean saveObjectGraph(final String sGraphId, final String sUserIdentifier, final String sFileName)
	{
		if ((null == sGraphId) || "".equals(sGraphId))
			return false ;
		
		if ((NSGRAPHTYPE.objectGraph != _graphType) || _aTrees.isEmpty())
			return false ;
		
		// An object graph is mono-document
		//
		LdvModelTree tree = _aTrees.elementAt(0) ;	
	
		// @ToDo finish this
/*
		LdvFilesManager filesManager = new LdvFilesManager(sGraphId, sFilesDir, sDirSeparator) ;
		filesManager.writeDocumentToDisk(final String sContent, final String sCompleteFileName)
		
		setBasicRootID(sGraphId) ;
		
		LdvXmlGraph xmlGraph = new LdvXmlGraph(LdvGraphConfig.COLLECTIVE_SERVER, sGraphId, sUserIdentifier) ;
 		
		if (NSGRAPHTYPE.personGraph == _graphType)
			return xmlGraph.openGraph((LdvModelGraph) this, sFilesDir, sDirSeparator) ;
		else
			return xmlGraph.openObjectGraph((LdvModelGraph) this, sFilesDir, sDirSeparator) ;
*/
		return true ;
	}
	
	

	/**
	 * Initialize _sROOT_ID from a user Id, guessing that root tree's Id is "000000"
	 * 
	 * @param sUserId User Id (must be exactly of <code>PERSON_ID_LEN</code> length)
	 * 
	 **/
	public void setBasicRootID(String sUserId)
  {
		if ((null == sUserId) || sUserId.equals(""))
			return ;
		
		if (NSGRAPHTYPE.objectGraph == _graphType)
		{
			if (sUserId.length() != LdvGraphConfig.OBJECT_ID_LEN)
				return ;
			
			_sROOT_ID = sUserId ;
			
			return ;
		}
		
		if (NSGRAPHTYPE.personGraph != _graphType)
			return ;
		
		if (sUserId.length() != LdvGraphConfig.PERSON_ID_LEN)
			return ;
		
		_sROOT_ID = sUserId ;
		
		for (int i = 0 ; i < LdvGraphConfig.DOCUMENT_ID_LEN ; i++)
			_sROOT_ID += "0" ;
  }
}
