package com.ldv.server.model;

import java.io.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

// import com.ldv.server.DbParameters;
import com.ldv.server.Logger;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvGraphTools;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelLink;
import com.ldv.shared.graph.LdvModelModelArray;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelRightArray;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.graph.LdvModelGraph.NSGRAPHTYPE;
import com.ldv.shared.model.DocumentLabel;
import com.ldv.shared.model.LdvTime;

import graphServer.Mapping;

/**
 * A LdvXmlGraph is a set of LdvXmlDocument
 * 
 * This is the object that embodies a graph on the server side
 * 
 **/
public class LdvXmlGraph
{	
	protected int                    _iServerType ;
	protected NSGRAPHTYPE            _graphType ;
	
	protected Document               _aTechnicalData ;
	protected Vector<LdvXmlDocument> _aTrees ;
	protected Document               _aLinks ;
	protected Document               _aRights ;
	
	protected String                 _sGraphId ;  // either person Id or object Id
	protected String                 _sUserId ;
	protected String                 _sMaxTreeId ;
	
	public static String LINKS_ROOT_LABEL      = "links" ;
	public static String LINK_LABEL            = "link" ;
	
	public static String LINK_QUALIFIED_ATTR   = "qualified" ;
	public static String LINK_LINK_ATTR        = "semantic" ;
	public static String LINK_QUALIFIER_ATTR   = "qualifier" ;
	public static String LINK_TRANSACTION_ATTR = "transaction" ;
	
	public static String RIGHTS_ROOT_LABEL     = "rights" ;
	public static String RIGHT_LABEL           = "right" ;
	
	public static String RIGHT_DOCUMENT_ATTR   = "document" ;
	public static String RIGHT_NODE_ATTR       = "node" ;
	public static String RIGHT_ROSE_ATTR       = "rose" ;
	public static String RIGHT_VALUE_ATTR      = "value" ;
	
	/**
	 * Standard constructor for a Person graph
	 * 
	 * @param iServerType Is it a global, a group or a local server?
	 * @param sPersonId   LdV identifier of the graph owner
	 * @param sUserId     LdV identifier of the user that asks access to the graph
	 * 
	 **/
	public LdvXmlGraph(int iServerType, final String sPersonId, final String sUserId)
	{
		_iServerType = iServerType ;
		_graphType   = NSGRAPHTYPE.personGraph ; 
		
		_aTrees      = new Vector<LdvXmlDocument>() ;
		
		_sGraphId    = sPersonId ;
		_sUserId     = sUserId ;
		_sMaxTreeId  = LdvGraphConfig.UNKNOWN_ROOTDOC[_iServerType] ;
	}
	
	/**
	 * Standard constructor for an Object graph
	 * 
	 * @param iServerType Is it a global, a group or a local server?
	 * @param sObjectId   LdV identifier of the object
	 * @param sUserId     LdV identifier of the user that asks access to the graph
	 * 
	 **/
	public LdvXmlGraph(final String sObjectId, final String sUserId, int iServerType)
	{
		_iServerType = iServerType ;
		_graphType   = NSGRAPHTYPE.objectGraph ; 
		
		_aTrees      = new Vector<LdvXmlDocument>() ;
		
		_sGraphId    = sObjectId ;
		_sUserId     = sUserId ;
		_sMaxTreeId  = "" ;
	}
	
	/**
	 * Open the graph by parsing all XML files at a given location
	 * 
	 * @param modelGraph   object to initialize
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean openGraph(LdvModelGraph modelGraph, String sFilesDir, String sDirSeparator)
	{
		if (null == modelGraph)
			return false ;
		
		if ((null == _sGraphId) || _sGraphId.equals(""))
			return false ;
		
		// LdvFilesManager filesManager = new LdvFilesManager(_sGraphId, DbParameters._sFilesDir, DbParameters._sDirSeparator, _sTraceFile) ;
		LdvFilesManager filesManager = new LdvFilesManager(_sGraphId, sFilesDir, sDirSeparator) ;
		
		return openGraph(filesManager, modelGraph) ;
	}
	
	/**
	 * Open the graph by parsing all XML files at a given location
	 * 
	 * @param filesManager object to access files on disk
	 * @param modelGraph   object to initialize
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean openGraph(LdvFilesManager filesManager, LdvModelGraph modelGraph)
	{
		if ((null == filesManager) || (null == modelGraph))
			return false ;
		
		// Get links
		//
		if (false == openLinksDocument(filesManager, modelGraph.getLinks()))
			return false ;

		if (modelGraph.getLinks().isEmpty())
			return false ;

		// Get trees that appear in links (graph trees)
		//
		for (Iterator<LdvModelLink> itr = modelGraph.getLinks().iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
			if (_sGraphId.equals(link.getQualifiedPersonId()))
			{
				String sQualifiedDocument = link.getQualifiedDocumentId() ;
				if ((false == "".equals(sQualifiedDocument)) && (false == modelGraph.existTreeForId(link.getQualifiedDocumentId())))
					addTreeToLdvGraph(sQualifiedDocument, filesManager, modelGraph) ;
			}
			
			if (_sGraphId.equals(link.getQualifierPersonId()))
			{
				String sQualifierDocument = link.getQualifierDocumentId() ;
				if ((false == sQualifierDocument.equals("")) && (false == modelGraph.existTreeForId(link.getQualifierDocumentId())))
					addTreeToLdvGraph(sQualifierDocument, filesManager, modelGraph) ;
			}
		}
		
		return true ;
	}
	
	/**
	 * Open the object graph by parsing the XML file
	 * 
	 * @param modelGraph   object to initialize
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean openObjectGraph(LdvModelGraph modelGraph, String sObjectsFilesDir, String sDirSeparator)
	{
		if (null == modelGraph) 
			return false ;
		
		if ((null == _sGraphId) || _sGraphId.equals(""))
			return false ;
		
		// LdvFilesManager filesManager = new LdvFilesManager(_sGraphId, DbParameters._sObjectsFilesDir, DbParameters._sDirSeparator, _sTraceFile) ;
		LdvFilesManager filesManager = new LdvFilesManager(_sGraphId, sObjectsFilesDir, sDirSeparator) ;
		
		String sFileName = getObjectFileName(_sGraphId) ;
		
		return addTreeToObjectGraph(sFileName, filesManager, modelGraph) ;
	}
	
	/**
	 * Fills an array of links from file
	 * 
	 * @param sDirectory directory where to find file 
	 * @param aLinks array of LdvModelLink to fill
	 * @return true if everything went well
	 * 
	 **/
	public boolean openLinksDocument(LdvFilesManager filesManager, Vector<LdvModelLink> aLinks)
	{
		if ((null == filesManager) || (null == aLinks))
			return false ;
		
		String sLinksFileTitle = filesManager.getWorkingFileCompleteName(getLinksFileName()) ; 
		if (sLinksFileTitle.equals(""))
			return false ;
		
		Document linksDocument = null ;
		
		FileInputStream fi;
		try
		{
			fi = new FileInputStream(sLinksFileTitle) ;
			
			linksDocument = getDocumentFromInputSource(new InputSource(fi), false, sLinksFileTitle) ;
			if (null == linksDocument)
				return false ;
		} 
		catch (FileNotFoundException e)
		{
			Logger.trace("LdvXmlGraph.openLinksDocument: input stream exception for file " + sLinksFileTitle + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return false ;
		}
		
		NodeList listOfLinks = linksDocument.getElementsByTagName(LINK_LABEL) ;
		if (null == listOfLinks)
			return false ;
		
    int iLinksCount = listOfLinks.getLength() ;
    if (-1 == iLinksCount)
    	return false ;
    
    for (int iLink = 0 ; iLink < iLinksCount ; iLink++)
    {
    	Node linkNode = listOfLinks.item(iLink) ;
    	LdvModelLink link = new LdvModelLink() ;
    	if (getLinkModelFromXml(link, (Element) linkNode))
    		aLinks.add(link) ;
    }
    
    return true ;
	}
	
	/**
	 * Adds a LdvModelTree, from xml file, into a LdvModelGraph 
	 * 
	 * @param sTreeId tree Id 
	 * @param sDirectory file directory
	 * @param modelGraph LdvModelGraph to add a new LdvModelTree into
	 * @return true if everything went well
	 * 
	 **/
	public boolean addTreeToLdvGraph(String sDocumentId, LdvFilesManager filesManager, LdvModelGraph modelGraph)
	{
		if ((null == modelGraph) || (null == filesManager) || (null == sDocumentId) || "".equals(sDocumentId))
			return false ;

		String sFileName = getDocumentFileName(sDocumentId) ;
		LdvXmlDocument ldvXmlDoc = new LdvXmlDocument(NSGRAPHTYPE.personGraph, sFileName, filesManager, this) ;
		
		LdvModelTree modelTree = new LdvModelTree(LdvGraphTools.getDocumentId(_sGraphId, sDocumentId)) ;
		if (false == ldvXmlDoc.initializeLdvModelFromFile(modelTree))
			return false ;
			
		modelGraph.getTrees().add(modelTree) ;
		
		return true ;
	}
	
	/**
	 * Adds a LdvModelTree, from xml file, into a LdvModelGraph 
	 * 
	 * @param sTreeId tree Id 
	 * @param sDirectory file directory
	 * @param modelGraph LdvModelGraph to add a new LdvModelTree into
	 * @return true if everything went well
	 * 
	 **/
	public boolean addTreeToObjectGraph(String sFileName, LdvFilesManager filesManager, LdvModelGraph modelGraph)
	{
		if ((null == modelGraph) || (null == filesManager) || (null == sFileName) || sFileName.equals(""))
			return false ;

		// First, get file content as a Xml document
		//
		LdvXmlDocument ldvXmlDoc = new LdvXmlDocument(NSGRAPHTYPE.objectGraph, sFileName, filesManager, this) ;
		
		// Then ask this Xml document to parse itself into a Ldv tree
		//
		LdvModelTree modelTree = new LdvModelTree() ;
		if (false == ldvXmlDoc.initializeLdvModelFromFile(modelTree))
			return false ;
			
		if (modelTree.isEmpty())
			return false ;
		
		LdvModelNode rootNode = modelTree.getRootNode() ;
		
		modelGraph.setRootID(rootNode.getTreeID()) ;
		modelGraph.getTrees().add(modelTree) ;
		
		return true ;
	}
	
	/**
	 * Create and initialize the document that hosts links 
	 * 
	 **/
	public void createNewLinksDocument()
	{
		// Get factory instance
		//
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
		DocumentBuilder builder ;
    try
    {
	    builder = factory.newDocumentBuilder() ;
    } 
    catch (ParserConfigurationException e)
    {
    	Logger.trace("LdvXmlGraph.createNewLinksDocument: parser configuration exception for ldvId = " + _sGraphId + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	    return ;
    }
    
    // Create a document
    //
    DOMImplementation impl = builder.getDOMImplementation();
		
    _aLinks = impl.createDocument(null, null, null) ;
    
    // Create Root
    //
    Element eRoot = _aLinks.createElement(LINKS_ROOT_LABEL) ;
    _aLinks.appendChild(eRoot) ;
	}
	
	/**
	 * Add a new link 
	 * 
	 **/
	public void addNewLink(String sQualified, String sLink, String sQualifier, String sTransaction)
	{
		Element linksRootElement = _aLinks.getDocumentElement() ;
		
		Element eNewLink = _aLinks.createElement(LINK_LABEL) ;
		eNewLink.setAttribute(LINK_QUALIFIED_ATTR,   sQualified) ;
		eNewLink.setAttribute(LINK_LINK_ATTR,        sLink) ;
		eNewLink.setAttribute(LINK_QUALIFIER_ATTR,   sQualifier) ;
		eNewLink.setAttribute(LINK_TRANSACTION_ATTR, sTransaction) ;
		
		linksRootElement.appendChild(eNewLink) ;
	}
	
	/**
	 * Create and initialize the document that hosts rights 
	 * 
	 **/
	public void createNewRightsDocument()
	{
		// Get factory instance
		//
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
		DocumentBuilder builder ;
    try
    {
	    builder = factory.newDocumentBuilder() ;
    } 
    catch (ParserConfigurationException e)
    {
    	Logger.trace("LdvXmlGraph.createNewRightsDocument: parser configuration exception for ldvId = " + _sGraphId + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	    return ;
    }
    
    // Create a document
    //
    DOMImplementation impl = builder.getDOMImplementation();
		
    _aRights = impl.createDocument(null, null, null) ;
    
    // Create Root
    //
    Element eRoot = _aRights.createElement(RIGHTS_ROOT_LABEL) ;
    _aRights.appendChild(eRoot) ;
	}
	
	/**
	 * Add a new right 
	 * 
	 **/
	public void addNewRight(String sDocument, String sNode, String sRose, String sRights)
	{
		Element rightsRootElement = _aRights.getDocumentElement() ;
		
		Element eNewRight = _aRights.createElement(RIGHT_LABEL) ;
		
		eNewRight.setAttribute(RIGHT_DOCUMENT_ATTR, sDocument) ;
		eNewRight.setAttribute(RIGHT_NODE_ATTR,     sNode) ;
		eNewRight.setAttribute(RIGHT_ROSE_ATTR,     sRose) ;
		eNewRight.setAttribute(RIGHT_VALUE_ATTR,    sRights) ;
		
		rightsRootElement.appendChild(eNewRight) ;
	}
	
	/**
	 * Create and initialize the set of documents that represents a project
	 * 
	 */
	public String addNewProject(String sProjectLexique, String sRootDocId)
	{
		if ((null == sProjectLexique) || sProjectLexique.equals(""))
			return "" ;
		
		LdvTime timeNow = new LdvTime(0) ;
		timeNow.takeTime() ;
		
		//
		// Create project's root document
		//
		
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sRootLabelTreeId = _sMaxTreeId ;
		
		// Create root document's label 
		//
		DocumentLabel rootLabelDoc = new DocumentLabel(sRootLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "root", sProjectLexique, timeNow) ;
		LdvXmlDocument rootLabel = new LdvXmlDocument(this, rootLabelDoc) ;
		_aTrees.add(rootLabel) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sRootDocTreeId = _sMaxTreeId ;
		
		// Create root document's data 
		//
		LdvXmlDocument rootDocument = new LdvXmlDocument(this, sRootDocTreeId, sProjectLexique) ;
		_aTrees.add(rootDocument) ;
		
		// Link label and document
		//
		rootLabel.addNewLink(getDocumentIdFromTreeId(sRootLabelTreeId), "ZDATA", getDocumentIdFromTreeId(sRootDocTreeId), "") ;
		
		// Link label and root label
		//
		addNewLink(getDocumentIdFromTreeId(sRootDocId), "0PROJ", getDocumentIdFromTreeId(sRootLabelTreeId), "") ;

		// Create project's team document
		//
		String sTeamDocumentId = createProjectTeam(sRootLabelTreeId, timeNow) ;
		
		// Link team label and root label
		//
		rootLabel.addNewLink(getDocumentIdFromTreeId(sRootLabelTreeId), "LEQUI", getDocumentIdFromTreeId(sTeamDocumentId), "") ;
		
		// Create project's folders library document
		//
		String sFoldersDocumentId = createProjectFoldersLibrary(sRootLabelTreeId, timeNow) ;
		
		// Link team label and root label
		//
		rootLabel.addNewLink(getDocumentIdFromTreeId(sRootLabelTreeId), "0LIBC", getDocumentIdFromTreeId(sFoldersDocumentId), "") ;

		// Create project's concerns library document
		//
		String sConcernsDocumentId = createProjectConcerns(sRootLabelTreeId, timeNow) ;
		
		// Link team label and root label
		//
		rootLabel.addNewLink(getDocumentIdFromTreeId(sRootLabelTreeId), "ZPOMR", getDocumentIdFromTreeId(sConcernsDocumentId), "") ;
		
		return sRootLabelTreeId ;
	}
	
	/**
	 * Create a new graph 
	 * 
	 * @param sPersonId Identifier of the person
	 * @return true of all went well
	 * 
	 **/
	public String initNewGraph(String sPersonId, String sExceptForProject)
	{
		if ((null == sPersonId) || sPersonId.equals(""))
			return "" ;
		
		createNewLinksDocument() ;
		createNewRightsDocument() ;
		
		LdvTime timeNow = new LdvTime(0) ;
		timeNow.takeTime() ;
		
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sRootLabelTreeId = _sMaxTreeId ;
		
		// Create root document's label 
		//
		DocumentLabel rootLabelDoc = new DocumentLabel(sRootLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "root", "HHUMA3", timeNow) ;
		LdvXmlDocument rootLabel = new LdvXmlDocument(this, rootLabelDoc) ;
		_aTrees.add(rootLabel) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sRootDocTreeId = _sMaxTreeId ;
		
		// Create root document's data 
		//
		LdvXmlDocument rootDocument = new LdvXmlDocument(this, sRootDocTreeId, "HHUMA3") ;
		_aTrees.add(rootDocument) ;
		
		// Link label and document
		//
		rootLabel.addNewLink(getDocumentIdFromTreeId(sRootLabelTreeId), "ZDATA", getDocumentIdFromTreeId(sRootDocTreeId), "") ;
		
		// Create Life Project
		//
		if (false == "0PRVI1".equals(sExceptForProject))
			addNewProject("0PRVI1", sRootLabelTreeId) ;
		
		// Create Health Project
		//
		if (false == "0PRSA1".equals(sExceptForProject))
			addNewProject("0PRSA1", sRootLabelTreeId) ;
		
		// Create Social Project
		//
		if (false == "0PRSO1".equals(sExceptForProject))
			addNewProject("0PRSO1", sRootLabelTreeId) ;
		
		// Create Education Project
		//
		if (false == "0PEDU1".equals(sExceptForProject))
			addNewProject("0PEDU1", sRootLabelTreeId) ;
		
		// Create Professional Project
		//
		if (false == "0PRPR1".equals(sExceptForProject))
			addNewProject("0PRPR1", sRootLabelTreeId) ;
		
		// Create Financial Project
		//
		if (false == "0PRFI1".equals(sExceptForProject))
			addNewProject("0PRFI1", sRootLabelTreeId) ;
		
		// Create Asset management Project
		//
		if (false == "0PRAC1".equals(sExceptForProject))
			addNewProject("0PRAC1", sRootLabelTreeId) ;
		
    return sRootLabelTreeId ;
	}
	
	/**
   * This methods sets a graph for a given person. It updates the database.
   * @param person. The person
   */
	public boolean writeGraph(LdvModelGraph newGraph, LdvModelGraph currentGraph, String sFilesDir, String sDirSeparator, Vector<LdvGraphMapping> aMappings)
	{
		if ((null == newGraph) || (null == aMappings))
			return false ;
		
		/*
		* writeGraph algorithm :
		* ----------------------
		*   - We write the graph but we never change local person into collective,
		*	  nor "in memory" into local.
		*
		*   - loop on the trees, for each one :
		*     ---------------------------------
		*		- if the tree has a inMemory id (with "#") create a new tree id and
		*		  insert the tree with all it nodes
		*		- if the tree has already a global id :
		*			- create an array of objects from nsmdat with their last localisation
		*             from nsmloc (SQL query)
		*			- loop on all the nodes sent by the client
		*
		*			  - if the node has a local id, create a global node id and insert it
		*			  - if the node has a global id, mark it in the array :
		*				- if it is in the array with the same localisation, nothing to do
		*				- if it is in the array with another  localisation, update nsmloc
		*			- delete all the not-marked nodes in the array
		*/

		// We always have a person already created, either in local or collective DB
		// We never create the person in this method
		//
		if (newGraph.isEmpty())
			return true ;
		
		// Create the file manager
		//
		LdvFilesManager filesManager = new LdvFilesManager(_sGraphId, sFilesDir, sDirSeparator) ;
		
		// (Re)init the trees that will contain the new / updated trees
		//
		_aTrees.clear() ;
		
		int iNbTrees  = 0 ;
		int iNbLinks  = 0 ;
		int	iNbModels = 0 ;
		int iNbRights = 0 ;

		if (null != newGraph.getTrees())
			iNbTrees  = newGraph.getTrees().size() ;
		if (null != newGraph.getLinks())
			iNbLinks  = newGraph.getLinks().size() ;
		if (null != newGraph.getModels())
			iNbModels = newGraph.getModels().size() ;
		if (null != newGraph.getRights())
			iNbRights = newGraph.getRights().size() ;

		String sMaxDoc = null ;

		aMappings.clear() ;

		// We keep track of the previous lists of links, models and rights
		//
		Vector<LdvModelLink> vecOldLinks  = new Vector<LdvModelLink>() ;
		LdvModelModelArray   vecOldModels = new LdvModelModelArray() ;
		LdvModelRightArray   vecOldRights = new LdvModelRightArray() ;

		Vector<String> vecSavedTrees = new Vector<String>();		//String vector

		boolean isPerson = false ;

		// Loop on trees
		//
		for (Iterator<LdvModelTree> itTree = newGraph.getTrees().iterator() ; itTree.hasNext() ; )
		{
			LdvModelTree tree = itTree.next() ;
			
			if (_sGraphId.length() != LdvGraphConfig.OBJECT_ID_LEN)
				isPerson = true ;
			else					// it is an object
			{
				isPerson = false ;
				sMaxDoc = "" ;
			}
			
			boolean isNew = false ;

			// If it is a new document, get the next tree Id to be allocated
			//
			if (LdvGraphTools.isInMemory(tree, isPerson))
			{
				// Get a new tree Id, and store it (before it changes)
				//
				if (getNexTreeId())
					sMaxDoc = _sMaxTreeId ;

				isNew = true ;
      }
			// If it is not a new document, get its tree Id
			//
      else
      {
      	if (isPerson)
      		sMaxDoc = LdvGraphTools.getDocumentTreeId(tree.getTreeID()) ;
      	else
      		sMaxDoc = "" ;

      	isNew = false ;
      }

			boolean isSave = treeProcessing(tree, sMaxDoc, isNew, aMappings, filesManager) ;
			
			if (false == isNew)		//old tree
			{
				 newGraph.getLinksForDocument(tree.getTreeID(), vecOldLinks) ;
				 newGraph.getModelsForDocument(tree.getTreeID(), vecOldModels) ;

         if (isPerson)
        	 newGraph.getRightsForDocument(tree.getTreeID(), vecOldRights) ;

         vecSavedTrees.addElement(tree.getTreeID()) ;
			}
			else		//new tree
      {
         if (isPerson)
         {
           //research the new treeId
           if (isSave)
             vecSavedTrees.addElement(MappingManager.getNewObjectID(vecMappings, tree.TREE_ID)) ;
	 		  	 	  	//saveLastDocID(connect, id, maxDoc) ;
         }
         else
           if (isSave)
             vecSavedTrees.addElement(MappingManager.getNewObjectID(vecMappings, tree.TREE_ID)) ;
      }
		}
			
		Tools.trace("before execute", Tools.TraceLevel.DETAIL) ;
		nodeManager.execute(connect, jdbcObject, curTrans) ;
		Tools.trace("after execute", Tools.TraceLevel.DETAIL) ;

			if (isPerson)
			{
			/*	try
				{
					vecSavedTrees = JDBCTreeManagerTools.getAllTrees(connect,getDataTable(), id) ;

	  			}
	  			catch(SQLException e)
	  			{
	  				try
	  				{
	  					Thread.sleep(100);
	  				}
	  				catch(InterruptedException ex )
	  				{
	  					Tools.trace("sleep problem", Tools.TraceLevel.DETAIL) ;
	  				}

				}*/

				vecSavedTrees = JDBCTreeManagerTools.getAllTrees(connect,getDataTable(), id) ;
			}
			Tools.trace("after getTrees", Tools.TraceLevel.DETAIL) ;

		//	Mapping[] mappings = MappingManager.getMappings(vecMappings);

			mappings = MappingManager.getMappings(vecMappings) ;
			Tools.trace("mapping len = " + mappings.length, Tools.TraceLevel.DETAIL) ;

	 		//=== Loop on the Links from input graph
	 		//======================================

	 		if ((nbLinks > 0) && (false == Tools.isEmptyLink(graph.vectorLink)))
	 		{
	 			boolean isOk = JDBCLinkManagerTools.saveLinks(connect, jdbcObject, graph.vectorLink, vecOldLinks,
	 						TEMPORARY_CHAR[type], mappings, vecSavedTrees, curTrans) ;
	 			if (false == isOk)
	 			{
	 				warningMessage = WRONG_GRAPH_STRUCTURE ;
	 				Tools.trace("===========================WRONG_GRAPH_STRUCTURE===================================", Tools.TraceLevel.ERROR) ;
	 			}
			}
	 		Tools.trace("after link", Tools.TraceLevel.DETAIL) ;
	 		
	 		//=== Loop on the Models
	 		//======================
	 		// Tools.trace("=================  models  ========================", Tools.TraceLevel.DETAIL) ;
	 		if ((nbModels > 0) && (false == Tools.isEmptyModel(graph.vectorModel)))
	 			JDBCModelManagerTools.saveModels(connect, jdbcObject, graph.vectorModel, vecOldModels, TEMPORARY_CHAR[type], mappings,  vecSavedTrees, curTrans);
	 		
	 		Tools.trace("after model", Tools.TraceLevel.DETAIL) ;
	 		
			//=== Loop on the Rights
			//=======================
			if ((isPerson) && (nbRights > 0) && (false == Tools.isEmptyRight(graph.vectorRight)))
				JDBCRightManagerTools.saveRights(connect, jdbcObject, graph.vectorRight, vecOldRights, TEMPORARY_CHAR[type], mappings,  vecSavedTrees, curTrans);
	 		
			Tools.trace("after right", Tools.TraceLevel.DETAIL) ;

			endTransaction(connect, id, curTrans, operator);
			lastTrans = curTrans;

			connect.commit() ;
			connect.setAutoCommit(true) ;
		}		//try autocommit
		catch (SQLException ex)
		{
			connect.rollback() ;
			statement.close() ;
			close(connect) ;
			return null ;
		}

		statement.close() ;
		close(connect) ;

		vecMappings   = null ;
		vecOldLinks   = null ;
		vecOldModels  = null ;
		vecOldRights  = null ;
		vecSavedTrees = null ;
		graph         = null ;

		return mappings ;
	}

	/**
	 * Insert a new tree or update an existing tree
	 * 
	 * @param tree         Tree to be created or updated
	 * @param sDocId       Id of this document
	 * @param aMappings    Mappings for new nodes in this document
	 * @param filesManager File manager to load previous version of tree from disk in update mode
	 */
	protected boolean treeProcessing(final LdvModelTree tree, final String sDocId, boolean isNew, Vector<LdvGraphMapping> aMappings, LdvFilesManager filesManager)
	{
		if ((null == tree) || tree.isEmpty())
			return true ;
		
		if (isNew)
			insertTree(tree, sDocId, aMappings) ;
		else
			updateTree(tree, sDocId, aMappings, filesManager) ;
	}
	
	/**
	 * Insert a new tree
	 * 
	 * @param tree      New tree to be added
	 * @param sDocId    Id of this new document
	 * @param aMappings Mappings for this new document
	 */
	protected void insertTree(final LdvModelTree tree, final String sDocId, Vector<LdvGraphMapping> aMappings)
	{
		if ((null == tree) || tree.isEmpty())
			return ;
		
		LdvXmlDocument xmlDoc = new LdvXmlDocument(this, sDocId, tree, aMappings) ;
		
		_aTrees.add(xmlDoc) ;
	}
	
	/**
	 * Update an existing tree
	 * 
	 * @param tree         Tree to be updated
	 * @param sDocId       Id of this document
	 * @param aMappings    Mappings for new nodes in this document
	 * @param filesManager File manager to load previous version of tree from disk
	 */
	protected void updateTree(final LdvModelTree tree, final String sDocId, Vector<LdvGraphMapping> aMappings, LdvFilesManager filesManager)
	{
		if ((null == tree) || tree.isEmpty())
			return ;
		
		String sFileName = getDocumentFileName(sDocId) ;
		LdvXmlDocument xmlDoc = new LdvXmlDocument(NSGRAPHTYPE.personGraph, sFileName, filesManager, this) ;
		
		_aTrees.add(xmlDoc) ;
	}
	
	/**
	* Creates a Document from an InputSource
	* 
	* @param inputSource input source content
	* @param mustValidate true if the xml content must be validated
	* @return true if everything went well
	* 
	**/
	public static Document getDocumentFromInputSource(InputSource inputSource, boolean mustValidate, String sFileName)
	{
		if (null == inputSource) 
			return null ;
	
		// Get factory instance
		//
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance() ;
	
		if (false == mustValidate)
		{
			factory.setNamespaceAware(false) ;
			factory.setValidating(false) ;
			try
			{
				factory.setFeature("http://xml.org/sax/features/namespaces", false) ;
				factory.setFeature("http://xml.org/sax/features/validation", false) ;
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false) ;
				factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false) ;
			} 
			catch (ParserConfigurationException e)
			{
				Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser config exception for file \"" + sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
				e.printStackTrace();
			}
		}
	
		DocumentBuilder builder ;
		try
		{
			builder = factory.newDocumentBuilder() ;
		} 
		catch (ParserConfigurationException e)
		{
			Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser config exception when getting a document builder for file \"" + sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
  
		// Create documents
		//
		Document document = null ;
  
		try
		{
			document = builder.parse(inputSource) ;
		} 
		catch (SAXException e)
		{
			Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser exception for file \"" + sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		} 
		catch (IOException e)
		{
			Logger.trace("LdvXmlGraph.getDocumentFromInputSource: parser IO exception for file \"" + sFileName + "\" ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
      
		return document ;
	}
	
	/**
	 * Write all files to disk 
	 * 
	 * @param sDirectory Where to write files
	 * @return true of all went well
	 * 
	 **/
	public boolean exportFiles(LdvFilesManager filesManager, String sSecretKey)
	{
		if (null == filesManager)
			return false ;
		
		// Update links file
		//
		String sLinksFileTitle = filesManager.getWorkingFileCompleteName(getLinksFileName()) ; 
		if (sLinksFileTitle.equals(""))
			return false ;
		
		LdvXmlDocument.writeDocumentToDisk(_aLinks, sLinksFileTitle) ;
		
		// Update documents files
		//
		if (false == _aTrees.isEmpty())
		{
			for (Iterator<LdvXmlDocument> itr = _aTrees.iterator() ; itr.hasNext() ; )
			{
				LdvXmlDocument doc = itr.next() ;				
				String sDocFileTitle = filesManager.getWorkingFileCompleteName(getDocumentFileName(doc.getTreeId())) ;
				LdvXmlDocument.writeDocumentToDisk(doc.getFinalDocument(), sDocFileTitle) ;
			}
		}
		
		// Close working environment (zip, delete, cipher)
		//
		filesManager.closeWorkingEnvironment(sSecretKey) ;
				
		return true ;
	}
				
	/**
	 * Get tree Id for a new tree 
	 * 
	 * @return <code>true</code> if everything went well
	 * 
	 **/
	public boolean getNexTreeId()
	{
		StringBuffer nextId = new StringBuffer(_sMaxTreeId) ;
		
		// First tree
		//
		if (_sMaxTreeId.equals(LdvGraphConfig.UNKNOWN_ROOTDOC[_iServerType]))
		{
			if      (LdvGraphConfig.LOCAL_SERVER == _iServerType) 
				nextId.setCharAt(0, LdvGraphConfig.LOCAL_CHAR) ;
			else if ((LdvGraphConfig.GROUP_SERVER == _iServerType) || (LdvGraphConfig.DIRECT_GROUP_SERVER == _iServerType))
				nextId.setCharAt(0, LdvGraphConfig.GROUP_CHAR) ;
			else if ((LdvGraphConfig.COLLECTIVE_SERVER == _iServerType) || (LdvGraphConfig.DIRECT_COLLECTIVE_SERVER == _iServerType))
				nextId.setCharAt(0, '0') ;
			else
				return false ;
			
			for (int i = 1 ; i < nextId.length() ; i++)
				nextId.setCharAt(i, '0') ;
			
			_sMaxTreeId = nextId.toString() ;
			
			return true ;
		}
		
		try
		{
			String sNewMaxTreeId = LdvGraphTools.getNextId(_sMaxTreeId) ;
			
			if (sNewMaxTreeId.equals(""))
				return false ;
			
			_sMaxTreeId = sNewMaxTreeId ;
			
			return true ;
			
		} catch (NumberFormatException e)
		{
			return false ;
		}
	}
	
	/**
	 * Create a new graphElement 
	 * 
	 * @return <code>true</code> if everything went well
	 * 
	 **/
	public boolean buildRootElements(DOMImplementation impl)
	{
		if ((null == impl) || (null == _sGraphId) || _sGraphId.equals(""))
			return false ;
		
		Document rootDoc = impl.createDocument(null, null, null) ;
		
    // Create Root
    //
    Element e1 = rootDoc.createElement("graphElement");
    rootDoc.appendChild(e1);
		
		return true ;
	}

	/**
	 * Create a team document 
	 * 
	 * @param sProjectDocumentId project's root document Id
	 * @param timeNow creation time
	 * @return team document's Id
	 * 
	 **/
	public String createProjectTeam(String sProjectDocumentId, LdvTime timeNow)
	{
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sLabelTreeId = _sMaxTreeId ;
	
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "team", "LEQUI1", timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(this, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sDocTreeId = _sMaxTreeId ;
	
		// Create team document's data 
		//
		LdvModelTree tree = new LdvModelTree() ;
		tree.addNode(new LdvModelNode("LEQUI1"), 0) ;
		tree.addNode(new LdvModelNode("HMEMB1"), 1) ;
		tree.addNode(new LdvModelNode("�SPID1", _sGraphId), 2) ;
		
  	LdvTime dNoLimit = new LdvTime(0) ;
  	dNoLimit.setNoLimit() ;
  	//
  	// Add an administration mandate for angle 0, radius 0, starting now with no limit
		//
		addAdministrationMandate(tree, 0, 0, timeNow, dNoLimit) ;
		
		//
  	// Add an access mandate for the same conditions
		//
		addAdministrationMandate(tree, 0, 0, timeNow, dNoLimit) ;
		
		LdvXmlDocument Document = new LdvXmlDocument(this, sDocTreeId, tree) ;
		_aTrees.add(Document) ;
	
		// Link label and document
		//
		Label.addNewLink(getDocumentIdFromTreeId(sLabelTreeId), "ZDATA", getDocumentIdFromTreeId(sDocTreeId), "") ;

		return sLabelTreeId ;
	} 
	
	/**
	 * Create a concerns document 
	 * 
	 * @param sProjectDocumentId project's root document Id
	 * @param timeNow creation time
	 * @return concern document's Id
	 * 
	 **/
	public String createProjectConcerns(String sProjectDocumentId, LdvTime timeNow)
	{
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sLabelTreeId = _sMaxTreeId ;
	
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "team", "ZPOMR1", timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(this, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sDocTreeId = _sMaxTreeId ;
	
		// Create team document's data 
		//
		LdvModelTree tree = new LdvModelTree() ;
		tree.addNode(new LdvModelNode("ZPOMR1"), 0) ;
		tree.addNode(new LdvModelNode("0PRO11"), 1) ;
		tree.addNode(new LdvModelNode("0OBJE1"), 1) ;
		tree.addNode(new LdvModelNode("N00001"), 1) ;
		
		LdvXmlDocument Document = new LdvXmlDocument(this, sDocTreeId, tree) ;
		_aTrees.add(Document) ;
	
		// Link label and document
		//
		Label.addNewLink(getDocumentIdFromTreeId(sLabelTreeId), "ZDATA", getDocumentIdFromTreeId(sDocTreeId), "") ;
		
		return sLabelTreeId ;
	} 
	
	/**
	 * Adding an administration mandate in a team document tree
	 * 
	 * @param tree <code>LdvModelTree</code> to add information to
	 * @param iAngle angle in the rose 
	 * @param iRadius radius in the rose
	 * @param dStartDate mandate starting date
	 * @param dEndDate mandate ending date
	 * @return void
	 * 
	 **/
	public void addAdministrationMandate(LdvModelTree tree, int iAngle, int iRadius, LdvTime dStartDate, LdvTime dEndDate)
	{
		addMandate(tree, "LROOT1", iAngle, iRadius, dStartDate, dEndDate) ;
	}
	
	/**
	 * Adding an administration mandate in a team document tree
	 * 
	 * @param tree <code>LdvModelTree</code> to add information to
	 * @param iAngle angle in the rose 
	 * @param iRadius radius in the rose
	 * @param dStartDate mandate starting date
	 * @param dEndDate mandate ending date
	 * @return void
	 * 
	 **/
	public void addAccessMandate(LdvModelTree tree, int iAngle, int iRadius, LdvTime dStartDate, LdvTime dEndDate)
	{
		addMandate(tree, "LMAND1", iAngle, iRadius, dStartDate, dEndDate) ;
	}
	
	/**
	 * Add a mandate in a team document tree 
	 * 
	 * @param tree <code>LdvModelTree</code> to add information to
	 * @param sType root node of the mandate sub-tree; <code>LROOT1</code> for administration mandate, <code>LMAND1</code> for access mandate
	 * @param iAngle angle in the rose 
	 * @param iRadius radius in the rose
	 * @param dStartDate mandate starting date
	 * @param dEndDate mandate ending date
	 * @return void
	 * 
	 **/
	public void addMandate(LdvModelTree tree, String sType, int iAngle, int iRadius, LdvTime dStartDate, LdvTime dEndDate)
	{
		if ((null == tree) || (null == sType) || sType.equals("") || (null == dStartDate) || dStartDate.isEmpty() || (null == dEndDate) || dEndDate.isEmpty())
			return ;
		
		// Type
		//
		tree.addNode(new LdvModelNode(sType), 2) ;
		
		// Position inside the rose
		//
		Integer intRadius = iRadius ;
		Integer intAngle  = iAngle ;
		tree.addNode(new LdvModelNode("LPOSI1"), 3) ;
		tree.addNode(new LdvModelNode("VANPA1"), 4) ;
		tree.addNode(new LdvModelNode("�N0;02", intAngle.toString(), "2RODE1"), 5) ;
		tree.addNode(new LdvModelNode("VDIPA1"), 4) ;
		tree.addNode(new LdvModelNode("�N0;02", intRadius.toString(), "200001"), 5) ;
		
		// Dates
		//
		tree.addNode(new LdvModelNode("KOUVR1"), 3) ;
		tree.addNode(new LdvModelNode("�T0;19", dStartDate.getLocalDateTime(), "2DA021"), 4) ;
		tree.addNode(new LdvModelNode("KFERM1"), 3) ;
		tree.addNode(new LdvModelNode("�T0;19", dEndDate.getLocalDateTime(), "2DA021"), 4) ;
	}
	
	/**
	 * Create a folders library document 
	 * 
	 * @param sProjectDocumentId project's root document Id
	 * @param timeNow creation time
	 * @return folders library document's Id
	 * 
	 **/
	public String createProjectFoldersLibrary(String sProjectDocumentId, LdvTime timeNow)
	{
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sLabelTreeId = _sMaxTreeId ;
	
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "folders library", "0LIBC1", timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(this, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == getNexTreeId())
			return "" ;
		String sDocTreeId = _sMaxTreeId ;
	
		// Create team document's data 
		//
		LdvXmlDocument Document = new LdvXmlDocument(this, sDocTreeId, "0LIBC1") ;
		_aTrees.add(Document) ;
	
		// Link label and document
		//
		Label.addNewLink(getDocumentIdFromTreeId(sLabelTreeId), "ZDATA", getDocumentIdFromTreeId(sDocTreeId), "") ;

		return sLabelTreeId ;
	}
	
	public int getServerType() {
  	return _iServerType ;
  }
	public void setServerType(int iServerType) {
  	_iServerType = iServerType ;
  }

	public String getPersonId() {
  	return _sGraphId ;
  }
	public void setPersonId(String sPersonId) {
		_sGraphId = sPersonId ;
  }

	public String getMaxTreeId() {
  	return _sMaxTreeId ;
  }
	public void setMaxTreeId(String sMaxTreeId) {
  	_sMaxTreeId = sMaxTreeId ;
  }
	
	public Document getLinks() {
  	return _aLinks ;
  }

	/**
	 * Fills a LdvModelLink from a 'link' XML Element 
	 * 
	 * @param linkNode source XML Element
	 * @return a LdvModelLink if everything went well - or null
	 * 
	 **/
	public boolean getLinkModelFromXml(LdvModelLink ldvLink, Element xmlElement)
	{
		if ((null == xmlElement) || (null == ldvLink))
			return false ;
		
		ldvLink.setQualified(xmlElement.getAttribute(LINK_QUALIFIED_ATTR)) ;
		ldvLink.setLink(xmlElement.getAttribute(LINK_LINK_ATTR)) ;
		ldvLink.setQualifier(xmlElement.getAttribute(LINK_QUALIFIER_ATTR)) ;
		
		return true ;
	}
	
	/**
	 * Get links' file name 
	 * 
	 * @return file name or ""
	 * 
	 **/
	public String getLinksFileName()
	{
		return _sGraphId + "_links.xml" ;
	}
		
	/**
	 * Get tree's file name from its tree ID
	 * 
	 * @param  sDocumentId Document's unique identifier in person's graph
	 * @return file name as PersonId + "_" + Tree_Id + ".xml" or ""
	 * 
	 **/
	public String getDocumentFileName(final String sDocumentId)
	{
		if ((null == sDocumentId) || "".equals(sDocumentId))
			return "" ;
		
		return _sGraphId + "_" + sDocumentId + ".xml" ;
	}
	
	/**
	 * Get object's file name from its unique identifier
	 * 
	 * @param  sObjectId Object's unique identifier
	 * @return a file name in the form sObjectId + ".xml" or "" if sObjectId is empty or null  
	 * 
	 **/
	public String getObjectFileName(String sObjectId)
	{
		if ((null == sObjectId) || "".equals(sObjectId))
			return "" ;
		
		return sObjectId + ".xml" ;
	}
		
	/**
	 * Does a given tree exist in graph
	 * 
	 * @param sTreeId tree Id to look for in graph
	 * @return true if this tree is in graph, false if not
	 * 
	 **/
	public boolean existTreeForId(String sTreeId)
	{
		if ((null == sTreeId) || sTreeId.equals(""))
			return false ;
		
		if (_aTrees.isEmpty())
			return false ;
		
		for (Iterator<LdvXmlDocument> itr = _aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvXmlDocument doc = itr.next() ;
			if (doc.getTreeId().equals(sTreeId))
				return true ;
		}
		
		return false ;
	}
	
	/**
	 * Return document's Id as being person Id + tree Id 
	 * 
	 * @param  sTreeId Tree id of the tree which document Id is required
	 * @return person Id + tree Id as a String 
	 * 
	 **/
	public String getDocumentIdFromTreeId(String sTreeId) {
  	return _sGraphId + sTreeId ;
  }
}
