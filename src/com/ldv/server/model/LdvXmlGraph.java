package com.ldv.server.model;

import java.io.*;
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
	
	/**
	 * In order to be fully efficient and to sandbox projects, they are treated as isolated sub-graphs
	 */
	protected Vector<LdvXmlProject> _aProjects ;
	
	/**
	 * Informations that are not project specific
	 */
	protected Document               _aTechnicalData ;
	protected Vector<LdvXmlDocument> _aTrees ;
	protected Document               _aLinks ;
	protected Document               _aRights ;
	
	/**
	 * Global identifiers
	 */
	protected String                 _sGraphId ;  // either person Id or object Id
	protected String                 _sUserId ;
	protected String                 _sMaxTreeId ;
	
	/**
	 * XML tag names
	 */
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
		_aProjects   = new Vector<LdvXmlProject>() ; 
		
		_sGraphId    = sPersonId ;
		_sUserId     = sUserId ;
		_sMaxTreeId  = LdvGraphTools.getUnknownDocumentId() ;
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
		_aProjects   = new Vector<LdvXmlProject>() ;
		
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
		if (false == openLinksDocument(filesManager, modelGraph.getLinks(), getLinksFileName()))
			return false ;

		if (modelGraph.getLinks().isEmpty())
			return false ;

		// Initialize projects
		//
		for (Iterator<LdvModelLink> itr = modelGraph.getLinks().iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
			if (_sGraphId.equals(link.getQualifiedPersonId()) && "0PROJ".equals(link.getLink()))
			{
				String sProjectID = link.getQualifierDocumentId() ;
				
				LdvXmlProject project = new LdvXmlProject(this, sProjectID) ;
				project.openProject(filesManager, modelGraph) ;
			}
		}
		
		// Get trees that appear in links (graph trees) and have not already been included in any project
		//
		for (Iterator<LdvModelLink> itr = modelGraph.getLinks().iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
			if (_sGraphId.equals(link.getQualifiedPersonId()))
			{
				String sQualifiedDocument = link.getQualifiedDocumentId() ;
				if ((false == "".equals(sQualifiedDocument)) && (false == modelGraph.existTreeForId(link.getQualified())))
					addTreeToModel(sQualifiedDocument, filesManager, modelGraph.getTrees()) ;
			}
			
			if (_sGraphId.equals(link.getQualifierPersonId()))
			{
				String sQualifierDocument = link.getQualifierDocumentId() ;
				if ((false == sQualifierDocument.equals("")) && (false == modelGraph.existTreeForId(link.getQualifier())))
					addTreeToModel(sQualifierDocument, filesManager, modelGraph.getTrees()) ;
			}
		}
		
		return true ;
	}
	
	/**
	 * Open the Object Graph by parsing the XML file (since an object graph contains a single tree)
	 * 
	 * @param modelGraph       Object to initialize
	 * @param sObjectsFilesDir Location of objects files
	 * @param sDirSeparator    Directories separator
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean openObjectGraph(LdvModelGraph modelGraph, final String sObjectsFilesDir, final String sDirSeparator)
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
	 * @param filesManager Object that manages files 
	 * @param aLinks       Array of LdvModelLink to fill
	 * 
	 * @return <code>true</code> if everything went well
	 * 
	 **/
	public static boolean openLinksDocument(LdvFilesManager filesManager, Vector<LdvModelLink> aLinks, final String sLinksFileName)
	{
		if ((null == filesManager) || (null == aLinks))
			return false ;
		
		String sLinksFileTitle = filesManager.getWorkingFileCompleteName(sLinksFileName) ; 
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
		
		return fillLinksDocument(linksDocument, aLinks) ;
	}
	
	/**
	 * Fill a vector of LdvModelLink by parsing an XML Document
	 * 
	 * @param linksDocument XML Document to parse
	 * @param aLinks        Vector of LdvModelLink to fill
	 *             
	 * @return <code>true</code> if everything went well
	 */
	public static boolean fillLinksDocument(final Document linksDocument, Vector<LdvModelLink> aLinks)
	{
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
	public boolean addTreeToModel(String sDocumentId, LdvFilesManager filesManager, Vector<LdvModelTree> aModelTrees)
	{
		if ((null == aModelTrees) || (null == filesManager) || (null == sDocumentId) || "".equals(sDocumentId))
			return false ;

		String sFileName = getDocumentFileName(sDocumentId) ;
		LdvXmlDocument ldvXmlDoc = new LdvXmlDocument(NSGRAPHTYPE.personGraph, sFileName, filesManager, this) ;
		
		LdvModelTree modelTree = new LdvModelTree(LdvGraphTools.getTreeId(_sGraphId, sDocumentId)) ;
		if (false == ldvXmlDoc.initializeLdvModelFromFile(modelTree))
			return false ;
			
		aModelTrees.add(modelTree) ;
		
		return true ;
	}
	
	/**
	 * If an Object Graph, the graph is limited to a single tree. This function initializes the graph from it. 
	 * 
	 * @param sFileName    Name of the XML file 
	 * @param filesManager Object that manage files
	 * @param modelGraph   LdvModelGraph to add the LdvModelTree (as parsing result of the XML file) into
	 * 
	 * @return true if everything went well
	 * 
	 **/
	public boolean addTreeToObjectGraph(final String sFileName, LdvFilesManager filesManager, LdvModelGraph modelGraph)
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
	 * Get a DOM document builder 
	 * 
	 **/
	public static DOMImplementation GetDOMImplementation()
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
    	Logger.trace("LdvXmlGraph.GetDOMImplementation: cannot create a Document Builder ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
	    return null ;
    }
    
    // Return the document builder
    //
    return builder.getDOMImplementation() ;
	}
	
	/**
	 * Create and initialize the document that hosts links 
	 * 
	 **/
	public void createNewLinksDocument()
	{
		// Get a document builder
		//
    DOMImplementation impl = GetDOMImplementation() ;
    if (null == impl)
    	return ;
    
    // Create the document
    //
    _aLinks = impl.createDocument(null, null, null) ;
    
    // Create the Root node
    //
    Element eRoot = _aLinks.createElement(LINKS_ROOT_LABEL) ;
    _aLinks.appendChild(eRoot) ;
	}
	
	/**
	 * Add a new link
	 * 
	 **/
	public void addNewLink(final String sQualified, final String sLink, final String sQualifier, final String sTransaction) {
		addNewLink(_aLinks, sQualified, sLink, sQualifier, sTransaction) ;
	}
	
	/**
	 * Add a new link to a Document
	 * 
	 **/
	public static void addNewLink(Document dLinks, final String sQualified, final String sLink, final String sQualifier, final String sTransaction)
	{
		Element linksRootElement = dLinks.getDocumentElement() ;
		
		Element eNewLink = dLinks.createElement(LINK_LABEL) ;
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
		// Get a document builder
		//
	  DOMImplementation impl = GetDOMImplementation() ;
	  if (null == impl)
	  	return ;
		
	  // Create the document
    //
    _aRights = impl.createDocument(null, null, null) ;
    
    // Create Root
    //
    Element eRoot = _aRights.createElement(RIGHTS_ROOT_LABEL) ;
    _aRights.appendChild(eRoot) ;
	}
	
	/**
	 * Add a new right to a Document 
	 * 
	 **/
	public static void addNewRight(Document dRights, final String sDocument, final String sNode, final String sRose, final String sRights)
	{
		Element rightsRootElement = dRights.getDocumentElement() ;
		
		Element eNewRight = dRights.createElement(RIGHT_LABEL) ;
		
		eNewRight.setAttribute(RIGHT_DOCUMENT_ATTR, sDocument) ;
		eNewRight.setAttribute(RIGHT_NODE_ATTR,     sNode) ;
		eNewRight.setAttribute(RIGHT_ROSE_ATTR,     sRose) ;
		eNewRight.setAttribute(RIGHT_VALUE_ATTR,    sRights) ;
		
		rightsRootElement.appendChild(eNewRight) ;
	}
	
	/**
	 * Add a new right 
	 * 
	 **/
	public void addNewRight(final String sDocument, final String sNode, final String sRose, final String sRights) {
		addNewRight(_aRights, sDocument, sNode, sRose, sRights) ;
	}
	
	/**
	 * Create and initialize the set of documents that represents a project
	 * 
	 * @param sProjectLexique Lexique code for this project (ex code for "education project", "health project", etc)
	 * @param sRootDocId      Identifier of graph's root identifier
	 * @param sRealPath       Root path for model documents
	 * 
	 * @return Returns project's root identifier (identifier of the label document for project's root information)
	 * 
	 */
	public String addNewProject(final String sProjectLexique, final String sRootDocId, final String sRealPath)
	{
		if ((null == sProjectLexique) || sProjectLexique.equals(""))
			return "" ;
		
		LdvXmlProject newProject = new LdvXmlProject(this, "") ;
		String sProjectRootId = newProject.addNewProject(sProjectLexique, sRootDocId, sRealPath) ;
		_aProjects.add(newProject) ;
		
		return sProjectRootId ;
	}

	/**
	 * Create a new graph when a Ligne de vie is created 
	 * 
	 * @param sPersonId         Identifier of the person
	 * @param sExceptForProject Lexicon code for a project that is not to be created
	 * @param sRealPath         Root path for model documents
	 * 
	 * @return The root label tree ID if all went well, <code>""</code> if not
	 **/
	public String initNewGraph(final String sPersonId, final String sExceptForProject, final String sRealPath)
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
		rootLabel.addNewLink(getTreeIdFromDocumentId(sRootLabelTreeId), "ZDATA", getTreeIdFromDocumentId(sRootDocTreeId), "") ;
		
		// Create Life Project
		//
		if (false == "0PRVI1".equals(sExceptForProject))
			addNewProject("0PRVI1", sRootLabelTreeId, sRealPath) ;
		
		// Create Health Project
		//
		if (false == "0PRSA1".equals(sExceptForProject))
			addNewProject("0PRSA1", sRootLabelTreeId, sRealPath) ;
		
		// Create Social Project
		//
		if (false == "0PRSO1".equals(sExceptForProject))
			addNewProject("0PRSO1", sRootLabelTreeId, sRealPath) ;
		
		// Create Education Project
		//
		if (false == "0PEDU1".equals(sExceptForProject))
			addNewProject("0PEDU1", sRootLabelTreeId, sRealPath) ;
		
		// Create Professional Project
		//
		if (false == "0PRPR1".equals(sExceptForProject))
			addNewProject("0PRPR1", sRootLabelTreeId, sRealPath) ;
		
		// Create Financial Project
		//
		if (false == "0PRFI1".equals(sExceptForProject))
			addNewProject("0PRFI1", sRootLabelTreeId, sRealPath) ;
		
		// Create Asset management Project
		//
		if (false == "0PRAC1".equals(sExceptForProject))
			addNewProject("0PRAC1", sRootLabelTreeId, sRealPath) ;
		
    return sRootLabelTreeId ;
	}
	
	/**
   * This methods sets a graph for a given person. It updates the database.
   * 
   * @param newGraph        Information to be saved or updated
   * @param currentGraph    Existing graph
   * @param sFilesDir       Directory where graph files are located
   * @param sDirSeparator   Directory separator for current operating system
   * @param aMappings       Mapping of nodes IDs, from client temporary ID to server attributed ID
   * @param sProjectURI     ID of project to link information to 
   */
	public boolean writeGraph(LdvModelGraph newGraph, LdvModelGraph currentGraph, String sFilesDir, String sDirSeparator, Vector<LdvGraphMapping> aMappings, final String sProjectURI)
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
      		sMaxDoc = LdvGraphTools.getTreeDocumentId(tree.getTreeID()) ;
      	else
      		sMaxDoc = "" ;

      	isNew = false ;
      }

			boolean isSave = treeProcessing(tree, sMaxDoc, isNew, aMappings, filesManager, sProjectURI) ;
		} // to remove
		
		// Write graph to disk
		//
		writeFiles(filesManager) ;
		
		
/*
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
/*
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
*/
		
		return true ;
	}

	/**
	 * Insert a new tree or update an existing tree
	 * 
	 * @param tree         Tree to be created or updated
	 * @param sDocId       Id of this document
	 * @param aMappings    Mappings for new nodes in this document
	 * @param filesManager File manager to load previous version of tree from disk in update mode
	 * @param sProjectURI  ID of project to link information to
	 */
	protected boolean treeProcessing(final LdvModelTree tree, final String sDocId, boolean isNew, Vector<LdvGraphMapping> aMappings, LdvFilesManager filesManager, final String sProjectURI)
	{
		if ((null == tree) || tree.isEmpty())
			return true ;
		
		if (isNew)
			insertTree(tree, sDocId, sProjectURI) ;
		else
			updateTree(tree, sDocId, aMappings, filesManager) ;
		
		return true ;
	}
	
	/**
	 * Insert a new tree
	 * 
	 * @param tree        New tree to be added
	 * @param sDocId      Id of this new document
	 * @param aMappings   Mappings for this new document
	 * @param sProjectURI ID of project to link information to
	 */
	protected void insertTree(final LdvModelTree tree, final String sDocId, final String sProjectURI)
	{
		if ((null == tree) || tree.isEmpty())
			return ;
		
		// Create the document to be saved
		//
		LdvXmlDocument xmlDoc = new LdvXmlDocument(this, sDocId, tree) ;
		
		// Get the project to save the document into
		//
		LdvXmlProject targetProject = getProjectFromId(sProjectURI) ;
		
		if (null == targetProject)
			_aTrees.add(xmlDoc) ;
		else
			targetProject.addTree(xmlDoc) ;
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
		
		if (xmlDoc.updateFromModelTree(tree, aMappings))
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
	 * @param filesManager Files manager
	 * 
	 * @return true of all went well
	 * 
	 **/
	public boolean writeFiles(LdvFilesManager filesManager)
	{
		if (null == filesManager)
			return false ;
		
		// Update links file
		//
		if ((null != _aLinks) && (_aLinks.hasChildNodes()))
		{
			String sLinksFileTitle = filesManager.getWorkingFileCompleteName(getLinksFileName()) ; 
			if (sLinksFileTitle.equals(""))
				return false ;
		
			LdvXmlDocument.writeDocumentToDisk(_aLinks, sLinksFileTitle) ;
		}
		
		// Update documents files
		//
		if (false == writeDocumentFiles(filesManager, _aTrees, _sGraphId))
			return false ;
				
		// Write information from projects
		//
		if (false == _aProjects.isEmpty())
			for (Iterator<LdvXmlProject> itr = _aProjects.iterator() ; itr.hasNext() ; )
				if (false == itr.next().writeFiles(filesManager))
					return false ;
		
		return true ;
	}
	
	/**
	 * Write all tree files to disk 
	 * 
	 * @param filesManager Files manager
	 * @param aTrees       Trees to write to disk
	 * @param sGraphId     Graph ID
	 * 
	 * @return true of all went well
	 * 
	 **/
	public static boolean writeDocumentFiles(LdvFilesManager filesManager, final Vector<LdvXmlDocument> aTrees, final String sGraphId)
	{
		if (null == filesManager)
			return false ;
				
		// Update documents files
		//
		if ((null == aTrees) || aTrees.isEmpty())
			return true ;

		for (Iterator<LdvXmlDocument> itr = aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvXmlDocument doc = itr.next() ;				
			String sDocFileTitle = filesManager.getWorkingFileCompleteName(getDocumentFileName(doc.getTreeId(), sGraphId)) ;
			LdvXmlDocument.writeDocumentToDisk(doc.getFinalDocument(), sDocFileTitle) ;
		}
		
		return true ;
	}
	
	/**
	 * Write all files to disk and close  
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
		if (false == writeDocumentFiles(filesManager, _aTrees, _sGraphId))
			return false ;
		
		// Write information from projects
		//
		if (false == _aProjects.isEmpty())
			for (Iterator<LdvXmlProject> itr = _aProjects.iterator() ; itr.hasNext() ; )
				if (false == itr.next().writeFiles(filesManager))
					return false ;
		
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
		// First tree
		//
		if (_sMaxTreeId.equals(LdvGraphTools.getUnknownDocumentId()))
		{
			_sMaxTreeId = LdvGraphTools.getFirstDocumentId(_iServerType) ;			
			return true ;
		}
		
		try
		{
			String sNewMaxTreeId = LdvGraphTools.getNextId(_sMaxTreeId) ;
			
			if (sNewMaxTreeId.equals(""))
				return false ;
			
			_sMaxTreeId = sNewMaxTreeId ;
			
			return true ;
			
		} catch (NumberFormatException e) {
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
	 * @param ldvLink    LdvModelLink object to initialize
	 * @param xmlElement Source XML Element 
	 * 
	 * @return <code>true</code> if everything went well - or null
	 * 
	 **/
	public static boolean getLinkModelFromXml(LdvModelLink ldvLink, Element xmlElement)
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
	public String getDocumentFileName(final String sDocumentId) {
		return getDocumentFileName(sDocumentId, _sGraphId) ;
	}
	
	/**
	 * Get tree's file name from its tree ID and graph ID
	 * 
	 * @param sDocumentId Document's unique identifier in person's graph
	 * @param sGraphId    Graph's identifier
	 * 
	 * @return file name as PersonId + "_" + Tree_Id + ".xml" or ""
	 * 
	 **/
	public static String getDocumentFileName(final String sDocumentId, final String sGraphId)
	{
		if ((null == sDocumentId) || "".equals(sDocumentId) || (null == sGraphId) || "".equals(sGraphId))
			return "" ;
		
		return sGraphId + "_" + sDocumentId + ".xml" ;
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
	 * @param sTreeId ID of tree to look for
	 * @param aTrees  Vector of trees to look into
	 * 
	 * @return <code>true</code> if the tree exist in the vector, <code>false</code> if not
	 */
	public static boolean existTreeForIdInVector(final String sTreeId, final Vector<LdvXmlDocument> aTrees)
	{
		if ((null == sTreeId) || sTreeId.equals(""))
			return false ;
		
		if ((null == aTrees) || aTrees.isEmpty())
			return false ;
		
		for (Iterator<LdvXmlDocument> itr = aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvXmlDocument doc = itr.next() ;
			if (sTreeId.equals(doc.getTreeId()))
				return true ;
		}
		
		return false ;
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
		
		if (existTreeForIdInVector(sTreeId, _aTrees))
			return true ;
		
		// Search inside projects
		//
		if (_aProjects.isEmpty())
			return false ;
		
		for (Iterator<LdvXmlProject> itr = _aProjects.iterator() ; itr.hasNext() ; )
			if (itr.next().existTreeForId(sTreeId))
				return true ;
		
		return false ;
	}
	
	/**
	 * Return tree Id as being person Id + document Id 
	 * 
	 * @param  sTreeId Tree id of the tree which document Id is required
	 * @return person Id + tree Id as a String 
	 * 
	 **/
	public String getTreeIdFromDocumentId(String sTreeId) {
  	return LdvGraphTools.getTreeId(_sGraphId, sTreeId) ;
  }
	
	/**
	 * Get the project for a given project URI
	 * 
	 * @param sProjectURI The URI of project to find
	 * 
	 * @return A project if found, <code>null</code> if not
	 */
	protected LdvXmlProject getProjectFromId(final String sProjectURI)
	{
		if ((null == sProjectURI) || "".equals(sProjectURI))
			return null ;
		
		String sDocumentId = LdvGraphTools.getTreeDocumentId(sProjectURI) ;
		
		for (Iterator<LdvXmlProject> itr = _aProjects.iterator() ; itr.hasNext() ; )
		{
			LdvXmlProject project = itr.next() ;
			
			if (sDocumentId.equals(project.getProjectRootId()))
				return project ;
		}
		
		return null ;
	}
}
