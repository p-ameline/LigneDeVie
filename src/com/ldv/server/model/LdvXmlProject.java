package com.ldv.server.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.ldv.server.DbParameters;
import com.ldv.server.Logger;
// import com.ldv.server.DbParameters;
import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphTools;
import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelLink;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.graph.LdvModelProjectGraph;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.DocumentLabel;
import com.ldv.shared.model.LdvTime;

/**
 * A LdvXmlProject is a set of LdvXmlDocument that are all part of the same project
 * 
 * This is the object that embodies a project on the server side
 * 
 **/
public class LdvXmlProject
{	
	protected LdvXmlGraph            _graph ;
	
	protected Document               _aTechnicalData ;
	protected Vector<LdvXmlDocument> _aTrees ;
	protected Document               _aLinks ;
	protected Document               _aRights ;
	
	protected String                 _sProjectRootId ;   // simply the document ID (not person + document)
		
	/**
	 * Standard constructor for a Project graph
	 * 
	 * @param graph          Graph this project is part of
	 * @param sProjectRootId Project Identifier (just the document ID, not person + document)
	 * 
	 **/
	public LdvXmlProject(LdvXmlGraph graph, final String sProjectRootId)
	{
		_graph          = graph ;
		_sProjectRootId = sProjectRootId ;
		
		_aTrees = new Vector<LdvXmlDocument>() ; 
	}

	/**
	 * Open the project by parsing all XML files at a given location
	 * 
	 * @param filesManager Object to access files on disk
	 * @param modelGraph   Object to initialize
	 * 
	 * @return <code>true</code> of all went well, <code>false</code> if not 
	 * 
	 **/
	public boolean openProject(LdvFilesManager filesManager, LdvModelGraph modelGraph)
	{
		if ((null == filesManager) || (null == modelGraph))
			return false ;
		
		LdvModelProjectGraph modelProject = new LdvModelProjectGraph(_sProjectRootId) ;
		
		// Get links
		//
		if (false == LdvXmlGraph.openLinksDocument(filesManager, modelProject.getLinks(), getLinksFileName()))
			return false ;

		if (modelProject.getLinks().isEmpty())
			return false ;

		// Get trees that appear in links (graph trees)
		//
		String sPersonId = _graph.getPersonId() ;
		
		for (Iterator<LdvModelLink> itr = modelProject.getLinks().iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
			if (sPersonId.equals(link.getQualifiedPersonId()))
			{
				String sQualifiedDocument = link.getQualifiedDocumentId() ;
				if ((false == "".equals(sQualifiedDocument)) && (false == existTreeForId(link.getQualified())))
					_graph.addTreeToModel(sQualifiedDocument, filesManager, modelProject.getTrees()) ;
			}
			
			if (sPersonId.equals(link.getQualifierPersonId()))
			{
				String sQualifierDocument = link.getQualifierDocumentId() ;
				if ((false == sQualifierDocument.equals("")) && (false == existTreeForId(link.getQualifier())))
					_graph.addTreeToModel(sQualifierDocument, filesManager, modelProject.getTrees()) ;
			}
		}
		
		// Add the links to the graph
		//
		modelGraph.getProjects().add(modelProject) ;
		
		return true ;
	}

	/**
	 * Create and initialize the document that hosts links 
	 * 
	 **/
	public void createNewLinksDocument()
	{
		// Get a document builder
		//
		DOMImplementation impl = LdvXmlGraph.GetDOMImplementation() ;
		if (null == impl)
			return ;
			
		// Create the document
	  //
    _aLinks = impl.createDocument(null, null, null) ;
    
    // Create Root
    //
    Element eRoot = _aLinks.createElement(LdvXmlGraph.LINKS_ROOT_LABEL) ;
    _aLinks.appendChild(eRoot) ;
	}
	
	/**
	 * Add a new link 
	 * 
	 **/
	public void addNewLink(final String sQualified, final String sLink, final String sQualifier, final String sTransaction) {
		LdvXmlGraph.addNewLink(_aLinks, sQualified, sLink, sQualifier, sTransaction) ;
	}
	
	/**
	 * Create and initialize the document that hosts rights 
	 * 
	 **/
	public void createNewRightsDocument()
	{
		// Get a document builder
		//
		DOMImplementation impl = LdvXmlGraph.GetDOMImplementation() ;
		if (null == impl)
			return ;
				
		// Create the document
		//
    _aRights = impl.createDocument(null, null, null) ;
    
    // Create Root
    //
    Element eRoot = _aRights.createElement(LdvXmlGraph.RIGHTS_ROOT_LABEL) ;
    _aRights.appendChild(eRoot) ;
	}
	
	/**
	 * Add a new right 
	 * 
	 **/
	public void addNewRight(final String sDocument, final String sNode, final String sRose, final String sRights) {
		LdvXmlGraph.addNewRight(_aRights, sDocument, sNode, sRose, sRights) ;
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
		
		LdvTime timeNow = new LdvTime(0) ;
		timeNow.takeTime() ;
		
		//
		// Create project's root document
		//
		
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		_sProjectRootId = _graph.getMaxTreeId() ;
		
		createNewLinksDocument() ;
		createNewRightsDocument() ;
		
		// Create root document's label 
		//
		DocumentLabel rootLabelDoc = new DocumentLabel(_sProjectRootId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "root", sProjectLexique, timeNow) ;
		LdvXmlDocument rootLabel = new LdvXmlDocument(_graph, rootLabelDoc) ;
		_aTrees.add(rootLabel) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		String sRootDocTreeId = _graph.getMaxTreeId() ;
		
		// --------------- Create root document's data --------------- 
		//
		LdvXmlDocument rootDocument = new LdvXmlDocument(_graph, sRootDocTreeId, sProjectLexique) ;
		_aTrees.add(rootDocument) ;
		
		// Link label and document
		//
		addNewLink(_graph.getTreeIdFromDocumentId(_sProjectRootId), "ZDATA", _graph.getTreeIdFromDocumentId(sRootDocTreeId), "") ;
		
		// Link label and root label
		//
		_graph.addNewLink(_graph.getTreeIdFromDocumentId(sRootDocId), "0PROJ", _graph.getTreeIdFromDocumentId(_sProjectRootId), "") ;

		// --------------- Create project's team document ---------------
		//
		String sTeamDocumentId = createProjectTeam(_sProjectRootId, timeNow, sProjectLexique, sRealPath) ;
		
		// Link team label and root label
		//
		addNewLink(_graph.getTreeIdFromDocumentId(_sProjectRootId), "LEQUI", _graph.getTreeIdFromDocumentId(sTeamDocumentId), "") ;
		
		// --------------- Create project's folders library document ---------------
		//
		String sFoldersDocumentId = createProjectFoldersLibrary(_sProjectRootId, timeNow) ;
		
		// Link folders label and root label
		//
		addNewLink(_graph.getTreeIdFromDocumentId(_sProjectRootId), "0LIBC", _graph.getTreeIdFromDocumentId(sFoldersDocumentId), "") ;

		// --------------- Create project's concerns library document ---------------
		//
/*
		String sConcernsDocumentId = createProjectConcerns(_sProjectRootId, timeNow) ;
		
		// Link concerns label and root label
		//
		addNewLink(_graph.getDocumentIdFromTreeId(_sProjectRootId), "ZPOMR", _graph.getDocumentIdFromTreeId(sConcernsDocumentId), "") ;
*/	
		createProjectConcerns(timeNow) ;
		
		return _sProjectRootId ;
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
		LdvXmlGraph.writeDocumentFiles(filesManager, _aTrees, _graph.getPersonId()) ;
		
		return true ;
	}

	/**
	 * Create a team document 
	 * 
	 * @param sProjectDocumentId Project's root document Id
	 * @param timeNow            Creation time
	 * @param sProjectLexique    Lexicon code for this project (ex code for "education project", "health project", etc)
	 * @param sRealPath          Root path for model documents
	 * 
	 * @return Team document's Id if everything went well, <code>""</code> if not
	 * 
	 **/
	public String createProjectTeam(String sProjectDocumentId, LdvTime timeNow, final String sProjectLexique, final String sRealPath)
	{
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		String sLabelTreeId = _graph.getMaxTreeId() ;
	
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "team", "LEQUI1", timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(_graph, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		String sDocTreeId = _graph.getMaxTreeId() ;
	
		// Create team document's data 
		//
		LdvModelTree tree = new LdvModelTree() ;
		tree.addNode(new LdvModelNode("LEQUI1"), 0) ;
		tree.addNode(new LdvModelNode("HMEMB1"), 1) ;
		tree.addNode(new LdvModelNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "SPID1", _graph.getPersonId()), 2) ;
		
  	LdvTime dNoLimit = new LdvTime(0) ;
  	dNoLimit.setNoLimit() ;
  	
  	// Add an administration mandate for angle 0, radius 0, starting now with no limit
		//
		addAdministrationMandate(tree, 0, 0, timeNow, dNoLimit) ;
		
  	// Add an access mandate for the same conditions
		//
		addAccessMandate(tree, 0, 0, timeNow, dNoLimit) ;
		
		// Add the rosace model
		//
		addRoseModel(tree, sProjectLexique, sRealPath) ;
		
		LdvXmlDocument Document = new LdvXmlDocument(_graph, sDocTreeId, tree) ;
		_aTrees.add(Document) ;
	
		// Link label and document
		//
		addNewLink(_graph.getTreeIdFromDocumentId(sLabelTreeId), "ZDATA", _graph.getTreeIdFromDocumentId(sDocTreeId), "") ;

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
	protected void createProjectConcerns(LdvTime timeNow)
	{
		createBasicDocument("0PRO11", timeNow) ;
		createBasicDocument("0OBJE1", timeNow) ;
		createBasicDocument("N00001", timeNow) ;
	}
	
/*
	public String createProjectConcerns(String sProjectDocumentId, LdvTime timeNow)
	{
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		String sLabelTreeId = _graph.getMaxTreeId() ;
	
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "team", "ZPOMR1", timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(_graph, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		String sDocTreeId = _graph.getMaxTreeId() ;
	
		// Create team document's data 
		//
		LdvModelTree tree = new LdvModelTree() ;
		tree.addNode(new LdvModelNode("ZPOMR1"), 0) ;
		tree.addNode(new LdvModelNode("0PRO11"), 1) ;
		tree.addNode(new LdvModelNode("0OBJE1"), 1) ;
		tree.addNode(new LdvModelNode("N00001"), 1) ;
		
		Vector<LdvGraphMapping> aMappings = new Vector<LdvGraphMapping>() ;
		LdvXmlDocument Document = new LdvXmlDocument(this, sDocTreeId, tree) ;
		_aTrees.add(Document) ;
	
		// Link label and document
		//
		Label.addNewLink(getDocumentIdFromTreeId(sLabelTreeId), "ZDATA", getDocumentIdFromTreeId(sDocTreeId), "") ;
		
		return sLabelTreeId ;
	}
*/
	
	/**
	 * Create a document limited to a root node and connected to the project with a trait by the same concept
	 * 
	 * @param sLexique Root concept for the new tree
	 * @param timeNow  Creation date
	 */
	protected void createBasicDocument(final String sLexique, final LdvTime timeNow)
	{
		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return ;
		String sLabelTreeId = _graph.getMaxTreeId() ;
		
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "team", sLexique, timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(_graph, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return ;
		String sDocTreeId = _graph.getMaxTreeId() ;
		
		// Create team document's data 
		//
		LdvModelTree tree = new LdvModelTree() ;
		tree.addNode(new LdvModelNode(sLexique), 0) ;
			
		LdvXmlDocument Document = new LdvXmlDocument(_graph, sDocTreeId, tree) ;
		_aTrees.add(Document) ;
		
		// Link label and document
		//
		addNewLink(_graph.getTreeIdFromDocumentId(sLabelTreeId), "ZDATA", _graph.getTreeIdFromDocumentId(sDocTreeId), "") ;

		// Link concerns label and root label
		//
		addNewLink(_graph.getTreeIdFromDocumentId(_sProjectRootId), LdvGraphTools.getSenseCode(sLexique), _graph.getTreeIdFromDocumentId(sLabelTreeId), "") ;
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
		if (iRadius > 0)
		{
			tree.addNode(new LdvModelNode("VANPA1"), 4) ;
			tree.addNode(new LdvModelNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "N0;02", intAngle.toString(), "2RODE1"), 5) ;
		}
		tree.addNode(new LdvModelNode("VDIPA1"), 4) ;
		tree.addNode(new LdvModelNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "N0;02", intRadius.toString(), "200001"), 5) ;
		
		// Dates
		//
		tree.addNode(new LdvModelNode("KOUVR1"), 3) ;
		tree.addNode(new LdvModelNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "T0;19", dStartDate.getLocalDateTime(), "2DA021"), 4) ;
		tree.addNode(new LdvModelNode("KFERM1"), 3) ;
		tree.addNode(new LdvModelNode(String.valueOf(LdvGraphConfig.POUND_CHAR) + "T0;19", dEndDate.getLocalDateTime(), "2DA021"), 4) ;
	}
	
	/**
	 * Add the proper rose model to the project team tree 
	 * 
	 * @param tree            Tree to add rose model to
	 * @param sProjectLexique Lexicon code for this project (ex code for "education project", "health project", etc)
	 * @param sRealPath       Root path for model documents
	 * 
	 **/
	protected void addRoseModel(LdvModelTree tree, final String sProjectLexique, final String sRealPath)
	{
		if ((null == tree))
			return ;
		
		// Get the model as an array of nodes
		//
		LdvModelNodeArray roseArray = getRosaceModel(LdvGraphTools.getSenseCode(sProjectLexique), sRealPath) ;
		if ((null == roseArray) || roseArray.isEmpty())
			return ;
		
		// Happens the model to the tree
		//
		LdvModelNodeArray treeNodes = tree.getNodes() ;
		treeNodes.addVector(roseArray, 1, false, false, "") ;
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
		if (false == _graph.getNexTreeId())
			return "" ;
		String sLabelTreeId = _graph.getMaxTreeId() ;
	
		// Create team document's label 
		//
		DocumentLabel LabelDoc = new DocumentLabel(sLabelTreeId, LdvGraphConfig.SYSTEM_USER, "ZCS001", "folders library", "0LIBC1", timeNow) ;
		LdvXmlDocument Label = new LdvXmlDocument(_graph, LabelDoc) ;
		_aTrees.add(Label) ;

		// Get a new tree Id, and store it (before it changes)
		//
		if (false == _graph.getNexTreeId())
			return "" ;
		String sDocTreeId = _graph.getMaxTreeId() ;
	
		// Create team document's data 
		//
		LdvXmlDocument Document = new LdvXmlDocument(_graph, sDocTreeId, "0LIBC1") ;
		_aTrees.add(Document) ;
	
		// Link label and document
		//
		Label.addNewLink(_graph.getTreeIdFromDocumentId(sLabelTreeId), "ZDATA", _graph.getTreeIdFromDocumentId(sDocTreeId), "") ;

		return sLabelTreeId ;
	}
	
	public Document getLinks() {
  	return _aLinks ;
  } 
	
	/**
	 * Get project links' file name 
	 * 
	 * @return file name
	 * 
	 **/
	public String getLinksFileName() {
		return _graph.getPersonId() + "_" + _sProjectRootId + "_links.xml" ;
	}
		
	/**
	 * Does a given tree exist in project
	 * 
	 * @param sTreeId tree Id to look for in project
	 * 
	 * @return <code>true</code> if this tree is in graph, <code>false</code> if not
	 **/
	public boolean existTreeForId(String sTreeId) {
		return LdvXmlGraph.existTreeForIdInVector(sTreeId, _aTrees) ;
	}
	
	/**
	 * Get the rose model for a given project type as a LdvModelNodeArray
	 * 
	 * @param sProjectLexique Project type
	 * @param sRealPath       Path the WEB-INF directory is son of 
	 * 
	 * @return The model as a LdvModelNodeArray if all went well, <code>null</code> if not
	 */
	protected LdvModelNodeArray getRosaceModel(final String sProjectLexique, final String sRealPath)
	{
		if ((null == sRealPath) || (null == sProjectLexique) || "".equals(sProjectLexique))
			return null ;
		
		// Build file name for the specific rose model
		//
		String sFullFileName = sRealPath + DbParameters._sDirSeparator + "WEB-INF" + 
		                                   DbParameters._sDirSeparator + "models"  + 
		                                   DbParameters._sDirSeparator + "roses"   +
		                                   DbParameters._sDirSeparator + "RoseModel_" + sProjectLexique + ".xml" ;
		
		// Parse the file as a XML Document
		//
		Document roseModelDocument = null ;
		
		FileInputStream fi ;
		try
		{
			fi = new FileInputStream(sFullFileName) ;
			
			roseModelDocument = LdvXmlGraph.getDocumentFromInputSource(new InputSource(fi), false, sFullFileName) ;
			if (null == roseModelDocument)
				return null ;
		} 
		catch (FileNotFoundException e)
		{
			Logger.trace("LdvXmlProject.getRosaceModel: input stream exception for file " + sFullFileName + " ; stackTrace:" + e.getStackTrace(), -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
		
		if (false == roseModelDocument.hasChildNodes())
		{
			Logger.trace("LdvXmlProject.getRosaceModel: file " + sFullFileName + " contains no nodes", -1, Logger.TraceLevel.ERROR) ;
			return null ;
		}
		
		// Fill the array of nodes 
		//
		LdvModelNodeArray nodeArray = new LdvModelNodeArray() ;
		
		Node rootNode = roseModelDocument.getFirstChild() ;
		
		LdvModelNode rootModelNode = new LdvModelNode() ;
		LdvXmlDocument.initializeNodeContentFromElement((Element) rootNode, rootModelNode) ;
		
		rootModelNode.setCol(0) ;
		rootModelNode.setLine(0) ;
		nodeArray.add(rootModelNode) ;
		
		if (rootNode.hasChildNodes())
			addSonsToNodeArray(nodeArray, rootNode, 1) ;
		
		return nodeArray ;
	}
	
	/**
	 * Recursively add sons of a Node to the end of a LdvModelNodeArray  
	 * 
	 * @param nodeArray :  LdvModelNodeArray to add nodes to
	 * @param fatherNode : Father node of column to be initialized
	 * @param iColumn :    Column number to be set for this column
	 * 
	 * @return <code>true</code> if everything went well
	 */
	public boolean addSonsToNodeArray(LdvModelNodeArray nodeArray, Node fatherNode, int iColumn)
	{
		if ((null == nodeArray) || (null == fatherNode))
			return false ;
						
		if (false == fatherNode.hasChildNodes())
			return true ;
			
		Node columnNode = fatherNode.getFirstChild() ;
		while ((Node) null != columnNode)
		{
			String sNodeName = columnNode.getNodeName() ; 
			if (LdvXmlDocument.isNodeType(sNodeName, LdvXmlDocument.NODE_LABEL))
			{
				// Create a new node and initialize it
				//
				LdvModelNode ldvNode = new LdvModelNode() ;
				LdvXmlDocument.initializeNodeContentFromElement((Element) columnNode, ldvNode) ;
				
				// Set line and column
				//
				ldvNode.setCol(iColumn) ;
				
				if (nodeArray.isEmpty())
					ldvNode.setLine(0) ;
				else
				{
					int iLastLine = nodeArray.lastElement().getLine() ;
					ldvNode.setLine(iLastLine + 1) ;
				}
				
				// Add it to the array
				//
				nodeArray.add(ldvNode) ;
			
				// Recursively add sons
				//
				if (columnNode.hasChildNodes())
					addSonsToNodeArray(nodeArray, columnNode, iColumn + 1) ;
			}
			columnNode = columnNode.getNextSibling() ;
		}
		
		return true ;
	}
	
	/**
	 * Get project's document ID (not person + document)
	 */
	public String getProjectRootId() {
		return _sProjectRootId ;
	}
	
	/**
	 * Add a document to the tree array
	 */
	public void addTree(LdvXmlDocument xmlDoc) {
		_aTrees.add(xmlDoc) ;
	}
}
