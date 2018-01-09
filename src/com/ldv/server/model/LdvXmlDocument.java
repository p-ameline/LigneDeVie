package com.ldv.server.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ldv.shared.graph.LdvGraphConfig;
import com.ldv.shared.graph.LdvGraphMapping;
import com.ldv.shared.graph.LdvGraphTools;
import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelNodeArray;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.graph.LdvModelGraph.NSGRAPHTYPE;
import com.ldv.shared.model.DocumentLabel;
import com.ldv.shared.model.LdvTime;

/**
 * A LdvXmlDocument is an XML document that contains 3 DOM documents: a tree, its links and its access rights
 * 
 * This is the object that embodies a tree on the server side
 * 
 **/
public class LdvXmlDocument
{	
	protected LdvXmlGraph _ContainerGraph ;
	
	protected int      _iServerType ;
	
	protected String   _sObjectId ;  // If it is an object
	
	protected String   _sPersonId ;  // If it is a person, person Id
	protected String   _sTreeId ;    // If it is a person, tree Id
		
	protected Document _tree ;
	protected Document _rights ;
	protected Document _links ;

	protected String   _sMaxCollectiveNodeId ;
	protected String   _sMaxGroupNodeId ;
	protected String   _sMaxLocalNodeId ;
	
	protected DocumentBuilderFactory _factory ;
	
	public static String ROOT_LABEL           = "GraphElement" ;
	public static String TREE_LABEL           = "Tree" ;
	public static String NODE_LABEL           = "Node" ;
	public static String RIGHTS_LABEL         = "Rights" ;
	public static String LINKS_LABEL          = "Links" ;
	public static String LINK_LABEL           = "Link" ;
	
	public static String PERSON_ID_ATTRIBUTE  = "personId" ;
	public static String TREE_ID_ATTRIBUTE    = "treeId" ;
	public static String NODE_ID_ATTRIBUTE    = "nodeId" ;
	public static String OBJECT_ID_ATTRIBUTE  = "objectId" ;
	public static String LEXIQUE_ATTRIBUTE    = "lexique" ;
	public static String FREETEXT_ATTRIBUTE   = "freeText" ;
	public static String COMPLEMENT_ATTRIBUTE = "complement" ;
	public static String UNIT_ATTRIBUTE       = "unite" ;
	public static String CERTAINTY_ATTRIBUTE  = "certitude" ;
	public static String INTEREST_ATTRIBUTE   = "interet" ;
	public static String PLURAL_ATTRIBUTE     = "pluriel" ;
	public static String VISIBLE_ATTRIBUTE    = "visible" ;
	
	/**
	 * Constructor for a new Document Label 
	 * 
	 * @param containerGraph graph this document belongs to
	 * @param sDocumentId    document Id
	 * @param sLexiqueRoot   root concept for document
	 */
	public LdvXmlDocument(LdvXmlGraph containerGraph, final String sDocumentId, final String sLexiqueRoot)
	{
		init() ;
		
		_ContainerGraph = containerGraph ;
		
		if (null != _ContainerGraph)
		{
			_iServerType = _ContainerGraph.getServerType() ;
			_sPersonId   = _ContainerGraph.getPersonId() ;
		}
		_sTreeId     = sDocumentId ;
	
		initForRoot(sLexiqueRoot) ;
	}
	
	/**
	 * Constructor for a new document label 
	 * 
	 * @param containerGraph graph this document belongs to
	 * @param sDocumentId    document Id
	 * @param tree           LdvModelTree to initialize from
	 * @param aMappings      the mapping for new nodes (can be <code>null</code>)
	 */
	public LdvXmlDocument(LdvXmlGraph containerGraph, String sDocumentId, LdvModelTree tree, Vector<LdvGraphMapping> aMappings)
	{
		init() ;
		
		_ContainerGraph = containerGraph ;
		_sTreeId        = sDocumentId ;
		
		if (null != _ContainerGraph)
		{
			_iServerType = _ContainerGraph.getServerType() ;
			_sPersonId   = _ContainerGraph.getPersonId() ;
		}
		
		initFromTree(tree, aMappings) ;
	}
	
	/**
	 * Constructor for a new document label 
	 * 
	 **/
	public LdvXmlDocument(LdvXmlGraph containerGraph, DocumentLabel docLabel)
	{
		init() ;
		
		_ContainerGraph = containerGraph ;
		
		if (null != _ContainerGraph)
		{
			_iServerType = _ContainerGraph.getServerType() ;
			_sPersonId   = _ContainerGraph.getPersonId() ;
		}
		
		_sTreeId     = docLabel.getDocumentId() ;
	
		initAsDocumentLabelTree(docLabel) ;
	}
	
	/**
	 * Constructor for initialization from a xml file
	 * 
	 **/
	public LdvXmlDocument(LdvXmlGraph containerGraph, String sFileName)
	{
		init() ;
		
		_ContainerGraph = containerGraph ;
		
		if (null != _ContainerGraph)
		{
			_iServerType = _ContainerGraph.getServerType() ;
			_sPersonId   = _ContainerGraph.getPersonId() ;
		}
	
		initFromXmlFileAsString(sFileName, false) ;
	}
	
	/**
	 * Constructor for initialization from a xml file
	 * 
	 */
	public LdvXmlDocument(NSGRAPHTYPE graphType, String sFileName, LdvFilesManager filesManager, LdvXmlGraph containerGraph)
	{
		init() ;
		
		_ContainerGraph = containerGraph ;
	
		if (null != _ContainerGraph)
		{
			_iServerType = _ContainerGraph.getServerType() ;
		
			if (NSGRAPHTYPE.objectGraph == graphType)
				_sObjectId = _ContainerGraph.getPersonId() ;
			else
				_sPersonId = _ContainerGraph.getPersonId() ;
		}
		
		if ((null == sFileName) || "".equals(sFileName))
			return ;
		
		String sCompleteFileName = "" ;
		
		if (NSGRAPHTYPE.personGraph == graphType)
			sCompleteFileName = filesManager.getWorkingFileCompleteName(sFileName) ;
		else
			sCompleteFileName = filesManager.getFileCompleteName(sFileName) ;
		
		initFromXmlFile(sCompleteFileName, false) ;
	}
	
	/**
	 * Initialize all information and components
	 */
	public void init()
	{
		_iServerType = LdvGraphConfig.COLLECTIVE_SERVER ;
		_sObjectId   = "" ;
		_sPersonId   = "" ;
		_sTreeId     = "" ;
		
		_sMaxCollectiveNodeId = "" ;
		_sMaxGroupNodeId      = "" ;
		_sMaxLocalNodeId      = "" ;
		
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
	    // TODO Auto-generated catch block
	    e.printStackTrace() ;
	    return ;
    }
    
    // Create documents
    //
    DOMImplementation impl = builder.getDOMImplementation() ;
		
    _tree   = impl.createDocument(null, null, null) ;
    _rights = impl.createDocument(null, null, null) ;
    _links  = impl.createDocument(null, null, null) ;
	}
	
	/**
	 * Initialize from a String with xml content
	 * 
	 * @param sXmlFileAsString xml content
	 * @param mustValidate true if the xml content must be validated
	 * @return true if everything went well
	 * 
	 **/
	public boolean initFromXmlFileAsString(String sXmlFileAsString, boolean mustValidate)
	{
		if ((null == sXmlFileAsString) || "".equals(sXmlFileAsString))
			return false ;
		
		InputSource is = new InputSource(new StringReader(sXmlFileAsString)) ;
		
		return initFromInputSource(is, mustValidate, "") ;
	}
	
	/**
	 * Initialize from a file name and a directory
	 * 
	 * @param sXmlFileName file name
	 * @param mustValidate true if the xml content must be validated
	 * 
	 * @return true if everything went well
	 */
	public boolean initFromXmlFile(String sXmlFileName, boolean mustValidate)
	{
		if ((null == sXmlFileName) || "".equals(sXmlFileName))
			return false ;
		
		FileInputStream fi;
		try
		{
			fi = new FileInputStream(sXmlFileName) ;
			
			return initFromInputSource(new InputSource(fi), mustValidate, sXmlFileName) ;
		} 
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		}
	}
		
	/**
		* Initialize from a Reader
		* 
		* @param sXmlFileAsString xml content
		* @param mustValidate true if the xml content must be validated
		* @return true if everything went well
		* 
		**/
	public boolean initFromInputSource(InputSource inputSource, boolean mustValidate, String sFileName)
	{
		if (null == inputSource) 
			return false ;
		
		_iServerType = LdvGraphConfig.COLLECTIVE_SERVER ;
		_sObjectId   = "" ;
		_sPersonId   = "" ;
		_sTreeId     = "" ;
		
		_sMaxCollectiveNodeId = "" ;
		_sMaxGroupNodeId      = "" ;
		_sMaxLocalNodeId      = "" ;
		
		Document document = LdvXmlGraph.getDocumentFromInputSource(inputSource, mustValidate, sFileName) ;
		if (null == document)
			return false ;
		        
    return initDocumentsFromMasterDocument(document) ;
	}
	
	/**
	 * Initialize from a "master document" that contains the tree, rights and links sub-blocks
	 * 
	 * @param masterDocument document to initialize from
	 * 
	 * @return <code>true</code> if all went well
	 */
	public boolean initDocumentsFromMasterDocument(Document masterDocument)
	{
		if (null == masterDocument)
			return false ;
		
		initDocumentFromMasterDocument(masterDocument, _tree,   TREE_LABEL) ;
		initDocumentFromMasterDocument(masterDocument, _rights, RIGHTS_LABEL) ;
		initDocumentFromMasterDocument(masterDocument, _links,  LINKS_LABEL) ;
		
		Element tree = getRootElementForTree() ;
		if (null != tree)
		{
			_sPersonId = tree.getAttribute(PERSON_ID_ATTRIBUTE) ;
			_sTreeId   = tree.getAttribute(TREE_ID_ATTRIBUTE) ;
			_sObjectId = tree.getAttribute(OBJECT_ID_ATTRIBUTE) ;
		}
		
		return true ;
	}
	
	/**
	 * Initialize one of the tree, rights or links document from a sub_block of a master document
	 * 
	 * @param masterDocument Document to initialize from
	 * @param subDocument    Document to initialize
	 * @param sTag           Tag that defines the sub_block
	 * 
	 * @return <code>true</code> if all went well
	 */
	public boolean initDocumentFromMasterDocument(Document masterDocument, Document subDocument, String sTag)
	{
		if (null == masterDocument)
			return false ;
		
		NodeList listOfTrees = masterDocument.getElementsByTagName(sTag) ;
		if (null == listOfTrees)
			return false ;
		
    int iTotalElements = listOfTrees.getLength() ;
    if (iTotalElements <= 0)
    	return false ;
    
    Node firstTree = listOfTrees.item(0) ;

    // Node importedNode = masterDocument.importNode(firstTree, true);
    Node importedNode = subDocument.importNode(firstTree, true);
    subDocument.appendChild(importedNode) ;
    
		return true ;
	}
	
	/**
	 * Add a new link to the links document
	 * 
	 **/
	public void addNewLink(String sQualified, String sLink, String sQualifier, String sTransaction)
	{
		Element linksRootElement = _links.getDocumentElement() ;
		
		// If there is not already a root element for links, it is mandatory to create one
		//
		if (null == linksRootElement)
		{
			createRootNodeForLinks() ;
			linksRootElement = _links.getDocumentElement() ;
		}
		
		if (null == linksRootElement)
			return ;
		
		Element eNewLink = _links.createElement(LINK_LABEL) ;
		eNewLink.setAttribute(LdvXmlGraph.LINK_QUALIFIED_ATTR,   sQualified) ;
		eNewLink.setAttribute(LdvXmlGraph.LINK_LINK_ATTR,        sLink) ;
		eNewLink.setAttribute(LdvXmlGraph.LINK_QUALIFIER_ATTR,   sQualifier) ;
		eNewLink.setAttribute(LdvXmlGraph.LINK_TRANSACTION_ATTR, sTransaction) ;
		
		linksRootElement.appendChild(eNewLink) ;
	}
	
	/**
	 * Build the tree corresponding to a DocumentLabel and convert it into a string 
	 * 
	 * @param bReturnGeneratedKeys : if true, then execute getGeneratedKeys
	 * @return Number of affected rows
	 * 
	 **/
	public Document getFinalDocument()
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
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null ;
    }
    
    // Create a document
    //
    DOMImplementation impl = builder.getDOMImplementation();
		
		Document finalDoc = impl.createDocument(null, null, null) ;
		
		// Create Root
    //
    Element eRoot = finalDoc.createElement(ROOT_LABEL) ;
    finalDoc.appendChild(eRoot) ;
    
    // Create Tree
    //
    Element eTree = finalDoc.createElement(TREE_LABEL) ;
    
    if (false == "".equals(_sPersonId))
    {
    	eTree.setAttribute(PERSON_ID_ATTRIBUTE, _sPersonId) ;
    	eTree.setAttribute(TREE_ID_ATTRIBUTE,   _sTreeId) ;
    }
    if (false == "".equals(_sObjectId))
    	eTree.setAttribute(OBJECT_ID_ATTRIBUTE, _sObjectId) ;
    
    eRoot.appendChild(eTree) ;
    
    Element treeRootElement = getRootElementForTree() ;
    if (null != treeRootElement)
    {
    	Node dup = finalDoc.importNode(treeRootElement, true) ;
    	if (null != dup)
    		eTree.appendChild(dup) ;
    }
    
    // Create Rights
    //
    // Element eRights = finalDoc.createElement(RIGHTS_LABEL) ;
    // eRoot.appendChild(eRights) ;
    
    Element rightsRootElement = _rights.getDocumentElement() ;
    if (null != rightsRootElement)
    {
    	Node dup = finalDoc.importNode(rightsRootElement, true) ;
    	if (null != dup)
    		// eRights.appendChild(dup) ;
    		eRoot.appendChild(dup) ;
    }
    
    // Create Links
    //
    // Element eLinks = finalDoc.createElement(LINKS_LABEL) ;
    // eRoot.appendChild(eLinks) ;
    
    Element linksRootElement = _links.getDocumentElement() ;
    if (null != linksRootElement)
    {
    	Node dup = finalDoc.importNode(linksRootElement, true) ;
    	if (null != dup)
    		// eLinks.appendChild(dup) ;
    		eRoot.appendChild(dup) ;
    }
    
    return finalDoc ;
	}
	
	/**
	 * Fills internal tree document with the content of a LdvModelTree 
	 * 
	 * @param modelTree the tree to initialize this object from
	 * @param aMappings the mapping for new nodes (can be <code>null</code>)
	 * 
	 * @return <code>true</code> if successful
	 **/
	public boolean initFromTree(LdvModelTree modelTree, Vector<LdvGraphMapping> aMappings)
	{
		if ((null == modelTree) || (modelTree.isEmpty()))
			return false ;
		
		// modelTree.sortByLocalisation() ;
		
		LdvModelNode modelNode = modelTree.getNodeAtIndex(0) ;
		if (null == modelNode)
			return false ;
				
		Element eNode = _tree.createElement(NODE_LABEL) ;
		initializeElementFromNode(eNode, modelNode, aMappings) ;
		_tree.appendChild(eNode) ;
		
		createChildrenNodes(eNode, modelTree, 0, aMappings) ;
		
		return true ;
	}
	
	/**
	 * Recursively add sons nodes from corresponding nodes in LdvModelTree 
	 * 
	 * @param eFatherNode  Element to add sons to
	 * @param modelTree    LdvModelTree to add nodes from
	 * @param iFatherIndex index of father node in modelTree
	 * @param aMappings    the mapping for new nodes (can be <code>null</code>)
	 */
	public void createChildrenNodes(Element eFatherNode, LdvModelTree modelTree, int iFatherIndex, Vector<LdvGraphMapping> aMappings)
	{
		int iSonIndex = modelTree.findFirstSonIndex(iFatherIndex) ;
		
		while (-1 != iSonIndex)
		{
			Element eNode = _tree.createElement(NODE_LABEL) ;
			initializeElementFromNode(eNode, modelTree.getNodeAtIndex(iSonIndex), aMappings) ;
			eFatherNode.appendChild(eNode) ;
			
			createChildrenNodes(eNode, modelTree, iSonIndex, aMappings) ;
			
			iSonIndex = modelTree.findFirstBrotherIndex(iSonIndex) ;
		}
	}
	
	/**
	 * Build a document from a LdvModelTree 
	 * 
	 * @param sLexiqueRoot the Lexicon code of the root node
	 * 
	 * @return true if successful
	 */
	public boolean initForRoot(final String sLexiqueRoot)
	{
		if ((null == sLexiqueRoot) || ("".equals(sLexiqueRoot)))
			return false ;
		
		// Document concept
    //
    Element eDocum = addRootNode(_tree, sLexiqueRoot) ;
    if (null == eDocum)
    	return false ;
		
		return true ;
	}
	
	/**
	 * Build the tree corresponding to a DocumentLabel  
	 * 
	 * @param docLabel label description object to be represented
	 * 
	 * @return true if successful
	 */
	public boolean initAsDocumentLabelTree(DocumentLabel docLabel)
	{
		if (null == docLabel)
			return false ;
		    	
    // Document concept
    //
    Element eDocum = addRootNode(_tree, "ZDOCU1") ;
    
    // Document's title
    //
    String sDocTitle = docLabel.getDocumentTitle() ;
    if (false == "".equals(sDocTitle))
    {
    	Element eTitle = addNode(eDocum, "0INTI1") ;
    	addFreeTextNode(eTitle, sDocTitle, "") ;
    }
    
    // Creator's Id
    //
    String sCreatorId = docLabel.getCreatorId() ;
    if (false == "".equals(sCreatorId))
    {
    	Element eCreator = addNode(eDocum, "DOPER1") ;
    	addPersonIdNode(eCreator, sCreatorId) ;
    }
    
    // Author's Id
    //
    String sAuthorId = docLabel.getAuthorId() ;
    if (false == "".equals(sAuthorId))
    {
    	Element eAuthor = addNode(eDocum, "DAUTE1") ;
    	addPersonIdNode(eAuthor, sAuthorId) ;
    }
    
    // Recipient's Id
    //
    String sRecipientId = docLabel.getRecipientId() ;
    if (false == "".equals(sRecipientId))
    {
    	Element eRecipient = addNode(eDocum, "DDEST1") ;
    	addPersonIdNode(eRecipient, sRecipientId) ;
    }
    
    // Document's type
    //
    String sDocType = docLabel.getFullDocumentType() ;
    if (false == "".equals(sDocType))
    {
    	Element eType = addNode(eDocum, "0TYPE1") ;
    	addNode(eType, sDocType) ;
    }
    
    // Document's URI
    //
    String sDocURI = docLabel.getDocumentURI() ;
    if (false == "".equals(sDocURI))
    {
    	Element eURI = addNode(eDocum, "0URI01") ;
    	addFreeTextNode(eURI, sDocURI, "") ;
    }
    
    // Document's creation date
    //
    LdvTime timeCreationDate = docLabel.getCreationDate() ;
    if (false == timeCreationDate.isEmpty())
    {
    	Element eCreationDate = addNode(eDocum, "KREDA1") ;
    	addDateTimeNode(eCreationDate, timeCreationDate) ;
    }
    
    // Document's content date
    //
    LdvTime timeContentDate = docLabel.getContentDate() ;
    if (false == timeContentDate.isEmpty())
    {
    	Element eDocumentDate = addNode(eDocum, "KCHIR2") ;
    	addDateTimeNode(eDocumentDate, timeContentDate) ;
    }
    
    // Document's content
    //
    String sDocContent = docLabel.getFullDocumentContent() ;
    if (false == "".equals(sDocContent))
    {
    	Element eType = addNode(eDocum, "0TYPC1") ;
    	addNode(eType, sDocContent) ;
    }
    
		return true ;
	}

	/**
	 * Set document's root as a Lexique term 
	 * 
	 * @param document document that will receive the new node
	 * @param sLexiqTerm Lexique term
	 * 
	 * @return The new node as an Element
	 */
	public Element addRootNode(Document document, String sLexiqTerm)
	{
		Element eNode = document.createElement(NODE_LABEL) ;
		eNode.setAttribute(LEXIQUE_ATTRIBUTE, sLexiqTerm) ;
		
		setIdsForNewNode(eNode) ;
		
		document.appendChild(eNode) ;
		
		return eNode ;
	}
	
	/**
	 * Add a node that simply represents a Lexique term 
	 * 
	 * @param document document that will receive the new node
	 * @param eFatherElement node that will receive the new node as a son
	 * @param sLexiqTerm Lexique term
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addNode(Document document, Element eFatherElement, final String sLexiqTerm)
	{
		if ((null == document) || (null == eFatherElement) || (null == sLexiqTerm) || "".equals(sLexiqTerm))
			return null ;
		
		Element eNode = document.createElement(NODE_LABEL) ;
		if (null == eNode)
			return null ;
		
		eNode.setAttribute(LEXIQUE_ATTRIBUTE, sLexiqTerm) ;
		
		setIdsForNewNode(eNode) ;
		
		eFatherElement.appendChild(eNode) ;
		
		return eNode ;
	}
	
	/**
	 * Add, to the tree, a node that simply represents a Lexique term 
	 * 
	 * @param eFatherElement node that will receive the new node as a son
	 * @param sLexiqTerm Lexique term
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addNode(Element eFatherElement, final String sLexiqTerm) {
		return addNode(_tree, eFatherElement, sLexiqTerm) ;
	}
	
	/**
	 * Add a node that represents a free text 
	 * 
	 * @param document       Document that will receive the new node
	 * @param eFatherElement Node that will receive the new node as a son
	 * @param sFreeText      Free text to be stored in the node
	 * @param sLexiqTerm     Lexique term for this special flavor of free text (or <code>""</code> for regular free text)
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addFreeTextNode(Document document, Element eFatherElement, final String sFreeText, final String sLexiqTerm)
	{
		if ((null == document) || (null == eFatherElement) || (null == sFreeText) || "".equals(sFreeText))
			return null ;
		
		String sLexique = "�?????" ;
		if ((null != sLexiqTerm) && (false == "".equals(sLexiqTerm)))
			sLexique = sLexiqTerm ;
		
		Element eNode = document.createElement(NODE_LABEL) ;
		if (null == eNode)
			return null ;
		
		eNode.setAttribute(LEXIQUE_ATTRIBUTE,  sLexique) ;
		eNode.setAttribute(FREETEXT_ATTRIBUTE, sFreeText) ;
		
		setIdsForNewNode(eNode) ;
		
		eFatherElement.appendChild(eNode) ;
		
		return eNode ;
	}
	
	/**
	 * Add, to the tree, a node that represents a free text 
	 * 
	 * @param eFatherElement Node that will receive the new node as a son
	 * @param sFreeText      Free text to be stored in the node
	 * @param sLexiqTerm     Lexique term for this special flavor of free text (or <code>""</code> for regular free text)
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addFreeTextNode(Element eFatherElement, final String sFreeText, final String sLexiqTerm) {
		return addFreeTextNode(_tree, eFatherElement, sFreeText, sLexiqTerm) ;
	}
	
	/**
	 * Add a node that represents a person through her Id 
	 * 
	 * @param document document that will receive the new node
	 * @param eFatherElement node that will receive the new node as a son
	 * @param sPersonId Person's Id
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addPersonIdNode(Document document, Element eFatherElement, String sPersonId)
	{
		if ((null == document) || (null == eFatherElement) || (null == sPersonId) || "".equals(sPersonId))
			return null ;
		
		Element eNode = document.createElement(NODE_LABEL) ;
		if (null == eNode)
			return null ;
		
		eNode.setAttribute(LEXIQUE_ATTRIBUTE,  "�SPID1") ;
		eNode.setAttribute(COMPLEMENT_ATTRIBUTE, sPersonId) ;
		
		setIdsForNewNode(eNode) ;
		
		eFatherElement.appendChild(eNode) ;
		
		return eNode ;
	}

	/**
	 * Add, to the tree, a node that represents a person through her Id 
	 * 
	 * @param eFatherElement node that will receive the new node as a son
	 * @param sPersonId Person's Id
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addPersonIdNode(Element eFatherElement, final String sPersonId) {
		return addPersonIdNode(_tree, eFatherElement, sPersonId) ;
	}
	
	/**
	 * Add a node that represents a date and time information 
	 * 
	 * @param document document that will receive the new node
	 * @param eFatherElement node that will receive the new node as a son
	 * @param timeDate LdvTime object to be stored in the node
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addDateTimeNode(Document document, Element eFatherElement, LdvTime timeDate)
	{
		if ((null == document) || (null == eFatherElement) || (null == timeDate) || timeDate.isEmpty())
			return null ;
		
		Element eNode = document.createElement(NODE_LABEL) ;
		if (null == eNode)
			return null ;
		
		eNode.setAttribute(LEXIQUE_ATTRIBUTE,  "�T0;19") ;
		eNode.setAttribute(COMPLEMENT_ATTRIBUTE, timeDate.getLocalDateTime()) ;
		eNode.setAttribute(UNIT_ATTRIBUTE,  "2DA021") ;
		
		setIdsForNewNode(eNode) ;
		
		eFatherElement.appendChild(eNode) ;
		
		return eNode ;
	}
	
	/**
	 * Add, to the tree, a node that represents a date and time information 
	 * 
	 * @param document document that will receive the new node
	 * @param eFatherElement node that will receive the new node as a son
	 * @param timeDate LdvTime object to be stored in the node
	 * 
	 * @return The new node as an Element or <code>null</code> if something went wrong
	 */
	public Element addDateTimeNode(Element eFatherElement, LdvTime timeDate) {
		return addDateTimeNode(_tree, eFatherElement, timeDate) ;
	}
	
	/**
	 * Set person, tree and node Ids (node Id being calculated) 
	 * 
	 * @param eNode Node to be initialized
	 * 
	 * @return true if all went well, false if new node Id could not be processed
	 */
	public boolean setIdsForNewNode(Element eNode)
	{
		// eNode.setAttribute(PERSON_ID_ATTRIBUTE, _sPersonId) ;
		// eNode.setAttribute(TREE_ID_ATTRIBUTE,   _sTreeId) ;
		
		String sNodeId = "" ;
		if      (isLocalServer())
			sNodeId = getNextNodeId(_sMaxLocalNodeId) ;
		else if (isGroupServer())
			sNodeId = getNextNodeId(_sMaxGroupNodeId) ;
		else if (isCollectiveServer())
			sNodeId = getNextNodeId(_sMaxCollectiveNodeId) ;
		else
			return false ;
		
		if ("".equals(sNodeId))
			return false ;
		
		if      (isLocalServer())
			_sMaxLocalNodeId = sNodeId ;
		else if (isGroupServer())
			_sMaxGroupNodeId = sNodeId ;
		else if (isCollectiveServer())
			_sMaxCollectiveNodeId = sNodeId ;
		
		eNode.setAttribute(NODE_ID_ATTRIBUTE, sNodeId) ;
		
		return true ;
	}
	
	/**
	 * Get the next node Id to be attributed
	 * 
	 * @param sNodeId Previous highest ranked node Id (or <code>""</code> if first tree node)
	 * 
	 * @return The new highest node Id
	 */
	public String getNextNodeId(String sNodeId)
	{
		if ("".equals(sNodeId))
		{
			String sResult = "" ;
			if      (isLocalServer())
				sResult += LdvGraphConfig.LOCAL_CHAR ;
			else if (isGroupServer())
				sResult += LdvGraphConfig.GROUP_CHAR ;
			else if (isCollectiveServer())
				sResult += '0' ;
			
			for (int i = 1 ; i < LdvGraphConfig.NODE_ID_LEN ; i++)
				sResult += '0' ;
			
			return sResult ;
		}
		
		return LdvGraphTools.getNextId(sNodeId) ;
	}
	
	/**
	 * Create the root element (&lt;links&gt;) for the links document 
	 */
	public void createRootNodeForLinks()
	{
		Element eNode = _links.createElement(LINKS_LABEL) ;
		_links.appendChild(eNode) ;
	}
	
	/**
	 * Convert a Document into a String 
	 * 
	 * @param doc : document to be serialized
	 * 
	 * @return An XML representation as a String
	 */
	public static String documentToString(Document doc)
	{
		// transform the Document into a String
    DOMSource domSource = new DOMSource(doc) ;
    TransformerFactory tf = TransformerFactory.newInstance() ;
    Transformer transformer = null ;
    try
    {
	    transformer = tf.newTransformer();
    } catch (TransformerConfigurationException e1)
    {
	    // TODO Auto-generated catch block
	    e1.printStackTrace() ;
	    return "" ;
    }
    //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    java.io.StringWriter sw = new java.io.StringWriter();
    StreamResult sr = new StreamResult(sw);
    try
    {
	    transformer.transform(domSource, sr) ;
    } catch (TransformerException e)
    {
	    // TODO Auto-generated catch block
	    e.printStackTrace() ;
	    return "" ;
    }
    String sXml = sw.toString() ;
    return sXml ;
	}
	
	public static boolean isNodeType(final String sNode, final String sType)
	{
		String sNodeUpper = sNode.toUpperCase() ;
		String sTypeUpper = sType.toUpperCase() ;
		
		if (sTypeUpper.equals(sNodeUpper))
			return true ;
		
		return false ;
	}
	
	/**
	 * Initializes a LdvModelTree from an XML file 
	 * 
	 * @param modelTree : LdvModelTree to be initialized
	 * @param sFileName : Complete file name
	 * 
	 * @return true if everything went well
	 */
	public boolean initializeLdvModelFromFile(LdvModelTree modelTree)
	{
		if (null == modelTree)
			return false ;
		
		modelTree.resetNodes() ;
		
		// Set document Id
		//
		modelTree.setTreeID(getDocumentId()) ;
		
		if (false == _tree.hasChildNodes())
			return false ;
		
		// This is the Tree node, use it to fill all the nodes
		//
		Node rootNode = _tree.getFirstChild() ;
		if (rootNode.hasChildNodes())
			initializeLdvModelColumn(modelTree, rootNode, 0) ;
						
		return true ;
	}
		
	/**
	 * Recursively initializes a LdvModelTree column and its son nodes 
	 * 
	 * @param modelTree :  LdvModelTree to be initialized
	 * @param fatherNode : Father node of column to be initialized
	 * @param iColumn :    Column number to be set for this column
	 * 
	 * @return <code>true</code> if everything went well
	 */
	public boolean initializeLdvModelColumn(LdvModelTree modelTree, Node fatherNode, int iColumn)
	{
		if ((null == modelTree) || (null == fatherNode))
			return false ;
						
		if (false == fatherNode.hasChildNodes())
			return true ;
			
		Node columnNode = fatherNode.getFirstChild() ;
		while ((Node) null != columnNode)
		{
			String sNodeName = columnNode.getNodeName() ; 
			if (isNodeType(sNodeName, NODE_LABEL))
			{
				LdvModelNode ldvNode = new LdvModelNode() ;
				initializeNodeFromElement((Element) columnNode, ldvNode) ;
				
				modelTree.addNode(ldvNode, iColumn) ;
			
				if (columnNode.hasChildNodes())
					initializeLdvModelColumn(modelTree, columnNode, iColumn + 1) ;
			}
			columnNode = columnNode.getNextSibling() ;
		}
						
		return true ;
	}
		
	/**
	 * Convert a Document into a String 
	 * 
	 * @param doc : document to be serialized
	 * 
	 * @return An XML representation as a String
	 */
	public static boolean writeDocumentToDisk(Document doc, String sFileName)
	{
		if ((null == doc) || (null == sFileName) || "".equals(sFileName))
			return false ;
		
		// transform the Document into a String
		//
		String sDocAsString = documentToString(doc) ;
		if ("".equals(sDocAsString))
			return false ;
		
		// Open output file
		//
		FileOutputStream out = null ;
		try
    {
			out = new FileOutputStream(sFileName, false) ;
    } 
		catch (FileNotFoundException e1)
    {
	    e1.printStackTrace();
	    return false ;
    }
		
		// Write string to disk
		//
		boolean bSuccess = true ;
		
		byte data[] = sDocAsString.getBytes() ;
		try
    {
	    out.write(data, 0, data.length) ;
    } 
		catch (IOException x)
    {
			System.err.println(x);
			bSuccess = false ;
    }
		finally
		{		
			try
      {
	      out.flush() ;
      } 
			catch (IOException e)
      {
	      e.printStackTrace() ;
	      bSuccess = false ;
      }
			try
      {
	      out.close() ;
      } 
			catch (IOException e)
      {
	      e.printStackTrace() ;
	      bSuccess = false ;
      }
		}
		
		return bSuccess ;
	}
	
	/**
	 * Initialize a "node" XML Element from a LdvModelNode object  
	 * 
	 * @param eNode : XML Element to be initialized
	 * @param modelNode : Source LdvModelNode object
	 * @param aMappings the mapping for new nodes (can be <code>null</code>)
	 * 
	 * @return true if all went well, false if not
	 */
	public boolean initializeElementFromNode(Element eNode, LdvModelNode modelNode, Vector<LdvGraphMapping> aMappings)
	{
		if ((null == eNode) || (null == modelNode))
			return false ;
		if ((null == modelNode.getLexicon()) || "".equals(modelNode.getLexicon()))
			return false ;
		
		// Identifiers
		//
		// if (false == _sPersonId.equals(""))
		// 	eNode.setAttribute(PERSON_ID_ATTRIBUTE, _sPersonId) ;
		// if (false == _sTreeId.equals(""))
		// 	eNode.setAttribute(TREE_ID_ATTRIBUTE, _sTreeId) ;
		
		String sNodeID = modelNode.getNodeID() ; 
		if ((null != sNodeID) && (false == "".equals(modelNode.getNodeID())))
		{
			if (LdvGraphTools.isInMemoryNode(sNodeID, _iServerType))
			{
				setIdsForNewNode(eNode) ;
				
				if (null != aMappings)
				{
					LdvGraphMapping	aMapping = new LdvGraphMapping() ;
					
					aMapping.setTemporaryObject_ID(modelNode.getDocumentId()) ;
					aMapping.setTemporaryNode_ID(sNodeID) ;
					
					aMapping.setStoredObject_ID(getDocumentId()) ; 
					aMapping.setStoredNode_ID(eNode.getAttribute(NODE_ID_ATTRIBUTE)) ;
					
					aMappings.add(aMapping) ;
				}
			}
			else
				eNode.setAttribute(NODE_ID_ATTRIBUTE, sNodeID) ;
		}
		
		// Data
		//
		eNode.setAttribute(LEXIQUE_ATTRIBUTE, modelNode.getLexicon()) ;
		
		if ((null != modelNode.getUnit()) && (false == "".equals(modelNode.getUnit())))
			eNode.setAttribute(UNIT_ATTRIBUTE, modelNode.getUnit()) ;
		if ((null != modelNode.getComplement()) && (false == "".equals(modelNode.getComplement())))
			eNode.setAttribute(COMPLEMENT_ATTRIBUTE, modelNode.getComplement()) ;
		if ((null != modelNode.getFreeText()) && (false == "".equals(modelNode.getFreeText())))
			eNode.setAttribute(FREETEXT_ATTRIBUTE, modelNode.getFreeText()) ;
		
		// Modifiers
		//
		if ((null != modelNode.getCertitude()) && (false == "".equals(modelNode.getCertitude())))
			eNode.setAttribute(CERTAINTY_ATTRIBUTE, modelNode.getCertitude()) ;
		if ((null != modelNode.getInterest()) && (false == "".equals(modelNode.getInterest())))
			eNode.setAttribute(INTEREST_ATTRIBUTE, modelNode.getInterest()) ;
		if ((null != modelNode.getPlural()) && (false == "".equals(modelNode.getPlural())))
			eNode.setAttribute(PLURAL_ATTRIBUTE, modelNode.getPlural()) ;
		if ((null != modelNode.getVisible()) && (false == "".equals(modelNode.getVisible())))
			eNode.setAttribute(VISIBLE_ATTRIBUTE, modelNode.getVisible()) ;
		
		return true ;
	}
	
	/**
	 * Initialize a LdvModelNode object from a "node" XML Element 
	 * 
	 * @param eNode : source XML Element
	 * @param modelNode : LdvModelNode to be initialized
	 * 
	 * @return An XML representation as a String
	 */
	public boolean initializeNodeFromElement(Element eNode, LdvModelNode modelNode)
	{
		if ((null == eNode) || (null == modelNode))
			return false ;

		modelNode.init() ;
		
		// Set document ID
		//
		if (false == "".equals(_sObjectId))
			modelNode.setObjectID(_sObjectId) ;
		else
			modelNode.setTreeID(getDocumentId()) ;
		
		// Set nodes specific information
		//
		modelNode.setNodeID(eNode.getAttribute(NODE_ID_ATTRIBUTE)) ;
		
		modelNode.setLexicon(eNode.getAttribute(LEXIQUE_ATTRIBUTE)) ;
		
		modelNode.setUnit(eNode.getAttribute(UNIT_ATTRIBUTE)) ;
		modelNode.setComplement(eNode.getAttribute(COMPLEMENT_ATTRIBUTE)) ;
		modelNode.setFreeText(eNode.getAttribute(FREETEXT_ATTRIBUTE)) ;
		
		modelNode.setCertitude(eNode.getAttribute(CERTAINTY_ATTRIBUTE)) ;
		modelNode.setInterest(eNode.getAttribute(INTEREST_ATTRIBUTE)) ;
		modelNode.setPlural(eNode.getAttribute(PLURAL_ATTRIBUTE)) ;
		modelNode.setVisible(eNode.getAttribute(VISIBLE_ATTRIBUTE)) ;
		
		return true ;
	}
	
	/**
	 * Update this document from a LdvModelTree object
	 * 
	 * @param tree      LdvModelTree object to update from
	 * @param aMappings Mapping for new nodes (can be null)
	 * 
	 * @return <code>true</code> is all went well
	 */
	public boolean updateFromModelTree(final LdvModelTree newTree, Vector<LdvGraphMapping> aMappings)
	{
		if (null == newTree)
			return false ;
		
		// First, we get the LdvModelTree for previous information, because it is easier and faster to process than DOM 
		//
		LdvModelTree previousTree = new LdvModelTree() ;
		if (false == initializeLdvModelFromFile(previousTree))
			return false ;
		
		
		// Unmask tree, that's to say put back information where it was removed due to insufficient access rights 
		//
		unmaskHiddenNodes(newTree, previousTree) ;
		
		// First check modified nodes
		//
		Element root = getRootElementForTree() ;
		
		
		return true ;
	}
	
	/**
	 * Unmask tree, that's to say put back information where it was removed due to insufficient access rights
	 * 
	 * @param tree Tree to rebuild
	 */
	public void unmaskHiddenNodes(LdvModelTree newTree, final LdvModelTree previousTree)
	{
		if ((null == newTree) || newTree.isEmpty() || (null == previousTree) || previousTree.isEmpty())
			return ;
		
		LdvModelNodeArray aNewNodes      = newTree.getNodes() ;
		LdvModelNodeArray aPreviousNodes = previousTree.getNodes() ;
		
		// Is there any "cut node" (the branch being replaced by a "900001" Lexicon code) 
		//
		boolean bCutFound = false ;
		for (Iterator<LdvModelNode> nodeIter = aNewNodes.iterator() ; (false == bCutFound) && nodeIter.hasNext() ; )
			if ("900001".equals(nodeIter.next().getLexicon()))
				bCutFound = true ;
		
		if (false == bCutFound)
			return ;
		
		// The Col of the node whose sons must be discarded, because they were just added 
		//
		int iNewRefCol = -1 ;
		
		// Scan the new tree for "cut nodes"
		// Since we will add new nodes, it is better not to use an iterator
		//
		for (int iNewNodeIndex = 0 ; iNewNodeIndex < aNewNodes.size() ; iNewNodeIndex++)
		{
			LdvModelNode newNode = aNewNodes.get(iNewNodeIndex) ;
			
			// If a node was cut
			//
			if ("900001".equals(newNode.getLexicon()))
			{
				// The usual methods of LdvModelNodeArray are not used here (getNodeForId, extractPatPatho, etc)
				// for several reasons: it would be slower and it usually doesn't respect nodes ID
				// This algorithm is "single pass"
				
				//
				// Find the real node (that was obfuscated) inside the previous tree
				//
				boolean bFound = false ;
				
				LdvModelNode previousNode = null ;
				
				int iPreviousNodeIndex = 0 ;
				for ( ; iPreviousNodeIndex < aPreviousNodes.size() ; iPreviousNodeIndex++)
				{
					previousNode = aPreviousNodes.get(iPreviousNodeIndex) ;
					if (newNode.getNodeID().equals(previousNode.getNodeID()))
					{
						bFound = true ;
						
						// Give back its content to the obfuscated node
						//
						int iCol  = newNode.getCol() ;
						int iLine = newNode.getLine() ;
						
						newNode.initFromNode(previousNode) ;
						
						newNode.setCol(iCol) ;
						newNode.setLine(iLine) ;
						
						break ;
					}
				}
				
				// Add obfuscated sons to the new array
				//
				if (bFound && (iPreviousNodeIndex < aPreviousNodes.size()))
				{
					// Get obfuscated nodes
					//
					LdvModelNodeArray aObfuscatedNodes = new LdvModelNodeArray() ;
					aPreviousNodes.extractPatPatho(previousNode, aObfuscatedNodes) ;
					
					// Add them to the new tree
					//
					int iNextNewNodeIndex = iNewNodeIndex + 1 ;
					if (iNextNewNodeIndex < aNewNodes.size())
					{
						LdvModelNode insertBefore = aNewNodes.get(iNextNewNodeIndex) ; 
						aNewNodes.insertVector(insertBefore, aObfuscatedNodes, newNode.getCol() + 1, true, true) ;
					}
					else
						aNewNodes.addVector(aObfuscatedNodes, newNode.getCol() + 1) ;					
				}
			}
		}					
	}
		
	public int getServerType() {
  	return _iServerType ;
  }
	public void setServerType(int iServerType) {
  	_iServerType = iServerType ;
  }

	public String getPersonId() {
  	return _sPersonId ;
  }
	public void setPersonId(String sPersonId) {
  	_sPersonId = sPersonId ;
  }
	
	public String getTreeId() {
  	return _sTreeId ;
  }
	public void setTreeId(String sTreeId) {
		_sTreeId = sTreeId ;
  }
	
	public String getObjectId() {
		return _sObjectId ;
	}
	public void setObjectId(String sObjectId) {
		_sObjectId = sObjectId ;
	}

	/**
	 * Return document's Id as being person Id + tree Id for a person or object Id for an object 
	 * 
	 * @return person Id + tree Id or object Id as a String 
	 */
	public String getDocumentId() 
	{
		if (false == "".equals(_sPersonId))
			return _sPersonId + _sTreeId ;
		if (false == "".equals(_sObjectId))
			return _sObjectId ;
		
		return "" ;
  }
	
	public String getMaxCollectiveNodeId()
  {
  	return _sMaxCollectiveNodeId ;
  }
	public void setMaxCollectiveNodeId(String sMaxGlobalNodeId)
  {
		_sMaxCollectiveNodeId = sMaxGlobalNodeId ;
  }
	
	public boolean isCollectiveServer()
	{
		if ((LdvGraphConfig.COLLECTIVE_SERVER == _iServerType) || (LdvGraphConfig.DIRECT_COLLECTIVE_SERVER == _iServerType))
			return true ;
		return false ;
	}
	
	public boolean isGroupServer()
	{
		if ((LdvGraphConfig.GROUP_SERVER == _iServerType) || (LdvGraphConfig.DIRECT_GROUP_SERVER == _iServerType))
			return true ;
		return false ;
	}
	
	public boolean isLocalServer() 
	{
		if (LdvGraphConfig.LOCAL_SERVER == _iServerType)
			return true ;
		return false ;
	}
	
	// Example
/*
	public void example() throws ParserConfigurationException, SAXException, IOException
	{
*/
		/*
		 * <book>
<person>
  <first>Kiran</first>
  <last>Pai</last>
  <age>22</age>
</person>
<person>
  <first>Bill</first>
  <last>Gates</last>
  <age>46</age>
</person>
<person>
  <first>Steve</first>
  <last>Jobs</last>
  <age>40</age>
</person>
</book>
		 */
/*		
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
    Document doc = docBuilder.parse (new File("book.xml"));

    // normalize text representation
    doc.getDocumentElement ().normalize ();
    System.out.println ("Root element of the doc is " + 
         doc.getDocumentElement().getNodeName());


    NodeList listOfPersons = doc.getElementsByTagName("person");
    int totalPersons = listOfPersons.getLength();
    System.out.println("Total no of people : " + totalPersons);

    for(int s=0; s<listOfPersons.getLength() ; s++){


        Node firstPersonNode = listOfPersons.item(s);
        if(firstPersonNode.getNodeType() == Node.ELEMENT_NODE){


            Element firstPersonElement = (Element)firstPersonNode;

            //-------
            NodeList firstNameList = firstPersonElement.getElementsByTagName("first");
            Element firstNameElement = (Element)firstNameList.item(0);

            NodeList textFNList = firstNameElement.getChildNodes();
            System.out.println("First Name : " + 
                   ((Node)textFNList.item(0)).getNodeValue().trim());

            //-------
            NodeList lastNameList = firstPersonElement.getElementsByTagName("last");
            Element lastNameElement = (Element)lastNameList.item(0);

            NodeList textLNList = lastNameElement.getChildNodes();
            System.out.println("Last Name : " + 
                   ((Node)textLNList.item(0)).getNodeValue().trim());

            //----
            NodeList ageList = firstPersonElement.getElementsByTagName("age");
            Element ageElement = (Element)ageList.item(0);

            NodeList textAgeList = ageElement.getChildNodes();
            System.out.println("Age : " + 
                   ((Node)textAgeList.item(0)).getNodeValue().trim());

            //------


        }//end of if clause


    }//end of for loop with s var
	}
*/
	
	/**
	 * Get the tree as a Document  
	 */
	public Document getTree() {
		return _tree ; 
	}

	/**
	 * Get the Element that is root for the tree 
	 */
	public Element getRootElementForTree() {
		return _tree.getDocumentElement() ;
	}
}
