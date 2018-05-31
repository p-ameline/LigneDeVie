package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

/**
 * A graph as it travels from server to client
 * 
 **/
public class LdvModelGraph implements IsSerializable
{
	public enum NSGRAPHTYPE { personGraph, objectGraph } ;
	
	protected NSGRAPHTYPE             _graphType ;
	
	protected String                  _sROOT_ID ;
	protected String                  _sLastTreeID ;
	
	/**
	 * Informations that are not project specific
	 */
	protected Vector<LdvModelTree>    _aTrees ;
	protected Vector<LdvModelLink>    _aLinks ;
	protected LdvModelRightArray      _aRights ;
	
	/**
	 * Global information
	 */
	protected LdvModelModelArray      _aModels ;
	
	/**
	 * Projects specific information
	 */
	protected Vector<LdvModelProjectGraph> _aProjects ;
	
	public LdvModelGraph(NSGRAPHTYPE graphType)
	{
		_graphType = graphType ;
		init() ; 
	}
	
	public LdvModelGraph()
	{
		_graphType = NSGRAPHTYPE.personGraph ;
		init() ; 
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model graph
	 * 
	 **/
	public LdvModelGraph(final LdvModelGraph source)
	{
		_graphType = NSGRAPHTYPE.personGraph ;
		init() ;
		
		initFromModelGraph(source) ;
	}
			
	/**
	 * Initialize all information
	 */
	protected void init()
	{
		_sROOT_ID = getInitialRootId() ;
		
		setLastTree("") ;
		
		_aTrees    = new Vector<LdvModelTree>() ;
		_aLinks    = new Vector<LdvModelLink>() ;
		_aRights   = new LdvModelRightArray() ;
		_aModels   = new LdvModelModelArray() ;
		
		_aProjects = new Vector<LdvModelProjectGraph>() ; 
	}
	
	/**
	 * Reset all information
	 */
	public void reset()
	{
		_sROOT_ID = getInitialRootId() ;
		
		if (null != _aTrees)
			_aTrees.clear() ;
		if (null != _aLinks)
			_aLinks.clear() ;
		if (null != _aRights)
			_aRights.clear() ;
		if (null != _aModels)
			_aModels.clear() ;
		
		if (null != _aProjects)
			_aProjects.clear() ;
	}
	
	public String getInitialRootId()
	{
		if (NSGRAPHTYPE.objectGraph == _graphType)
			return LdvGraphTools.getUnknownObjectId() ;
		
		return LdvGraphTools.getUnknownPersonId() + LdvGraphTools.getUnknownDocumentId() ;
	}
	
	/**
	 * Is the root ID in a non initialized state? (i.e. equal to the unknown state)
	 */
	public boolean hasUninitializedRootId() {
		return _sROOT_ID.equals(getInitialRootId()) ;
	}
	
	/**
	 * Initialize this object from another LdvModelGraph 
	 * 
	 * @param other Object to initialize from
	 */
	protected void initFromModelGraph(LdvModelGraph other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_graphType = other._graphType ;
		_sROOT_ID  = other._sROOT_ID ;
		
		if (false == other._aTrees.isEmpty())
			for (Iterator<LdvModelTree> itr = other._aTrees.iterator() ; itr.hasNext() ; )
				_aTrees.add(new LdvModelTree(itr.next())) ;
						
		addToLinks(other._aLinks) ;
		
		_aRights.initFromModelRightArray(other._aRights) ;
		_aModels.initFromModelModelArray(other._aModels) ;
		
		if (false == other._aProjects.isEmpty())
			for (Iterator<LdvModelProjectGraph> itr = other._aProjects.iterator() ; itr.hasNext() ; )
				_aProjects.add(new LdvModelProjectGraph(itr.next())) ;
	}
	
	public String getRootID() {
  	return _sROOT_ID ;
  }
	public void setRootID(String sID) {
		_sROOT_ID = sID ;
  }
	
	/**
	 * Get the rights for a given node in an array
	 * 
	 * @param sNodeId Id of node to get rights for
	 * @param aRights Array to look into
	 * 
	 * @return A vector of LdvModelRight (that may be empty)
	 */
	public static Vector<LdvModelRight> getRightForNode(final String sNodeId, final LdvModelRightArray aRights) {
		return aRights.getRightForNode(sNodeId) ;
	}
	
	/**
	 * Get the rights for a given node
	 * 
	 * @param sNodeId Id of node to get rights for
	 * 
	 * @return A vector of LdvModelRight (that may be empty)
	 */
	public Vector<LdvModelRight> getRightForNode(String sNodeId)
	{
		// First get rights for global trees
		//
		Vector<LdvModelRight> aRights = getRightForNode(sNodeId, _aRights) ;
		
		// Then browse projects
		//
		if (_aProjects.isEmpty())
			return aRights ;
			
		for (Iterator<LdvModelProjectGraph> itr = _aProjects.iterator() ; itr.hasNext() ; )
		{
			Vector<LdvModelRight> aProjectRights = itr.next().getRightForNode(sNodeId) ;
			if (false == aProjectRights.isEmpty())
				addRightsVectors(aRights, aProjectRights) ;
		}
					
		return aRights ;
	}
	
	public Vector<LdvModelModel> getModelsForNode(String sFullNodeId)
	{
		String sTreeId = LdvGraphTools.getNodeTreeId(sFullNodeId) ;
		String sNodeId = LdvGraphTools.getNodeNodeId(sFullNodeId) ;
		return _aModels.getModelForNode(sTreeId, sNodeId) ;
	}
	
	public NSGRAPHTYPE getGraphType() {
  	return _graphType ;
  }
	public void setGraphType(NSGRAPHTYPE graphType) {
  	_graphType = graphType ;
  }

	public Vector<LdvModelTree> getTrees() {
  	return _aTrees ;
  }

	public Vector<LdvModelLink> getLinks() {
  	return _aLinks ;
  }
	
	/**
	 * Add a vector of links inside another vector of links
	 * 
	 * @param aLinks            Vector of links to add
	 * @param aDestinationLinks Vector to add links into
	 */
	public static void addToLinks(final Vector<LdvModelLink> aLinks, Vector<LdvModelLink> aDestinationLinks)
	{
		if ((null == aDestinationLinks) || (null == aLinks) || aLinks.isEmpty())
			return ;
		
		for (Iterator<LdvModelLink> itr = aLinks.iterator() ; itr.hasNext() ; )
			aDestinationLinks.add(new LdvModelLink(itr.next())) ;
	}
	
	/**
	 * Add a vector of links to current vector
	 * 
	 * @param aLinks Vector of links to add
	 */
	public void addToLinks(final Vector<LdvModelLink> aLinks) {
		addToLinks(aLinks, _aLinks) ;
	}

	public LdvModelRightArray getRights() {
  	return _aRights ;
  }

	public LdvModelModelArray getModels() {
  	return _aModels ;
  }
	
	public Vector<LdvModelProjectGraph> getProjects() {
  	return _aProjects ;
  }
	
	/**
	 * Get the project graph from its ID
	 * 
	 * @return <code>null</code> if not found
	 */
	public LdvModelProjectGraph getProjectGraphFromID(final String sProjectId) 
	{
		if ((null == _aProjects) || _aProjects.isEmpty() || (null == sProjectId) || "".equals(sProjectId))
			return null ;
		
		for (Iterator<LdvModelProjectGraph> itr = _aProjects.iterator() ; itr.hasNext() ; )
		{
			LdvModelProjectGraph project = itr.next() ;
			
			if (sProjectId.equals(project.getProjectID()))
				return project ;
		}
			
  	return null ;
  }
	
	/**
	 * Does the graph contain any tree?
	 * 
	 * @return <code>true</code> if there is no tree in graph, <code>false</code> if not
	 * 
	 **/
	public boolean isEmpty() {
		return _aTrees.isEmpty() && _aProjects.isEmpty() ; 
	}
	
	/**
	 * Does a given tree exist in graph?
	 * 
	 * @param sTreeId tree Id to look for in graph
	 * 
	 * @return <code>true</code> if this tree exists in graph, <code>false</code> if not
	 **/
	public boolean existTreeForId(final String sDocumentId) 
	{
		// First look into the global trees
		//
		if (existTreeForId(sDocumentId, _aTrees))
			return true ;
		
		// Then browse projects
		//
		if (_aProjects.isEmpty())
			return false ;
		
		for (Iterator<LdvModelProjectGraph> itr = _aProjects.iterator() ; itr.hasNext() ; )
			if (itr.next().existTreeForId(sDocumentId))
				return true ;
					
		return false ;
	}
	
	/**
	 * Does a given tree exist in a vector
	 * 
	 * @param sDocumentId ID to look for
	 * @param aTrees      Vector of trees to look into
	 * 
	 * @return <code>true</code> if this tree exists in graph, <code>false</code> if not
	 */
	public static boolean existTreeForId(final String sDocumentId, final Vector<LdvModelTree> aTrees)
	{
		if ((null == sDocumentId) || sDocumentId.equals(""))
			return false ;
		
		if (aTrees.isEmpty())
			return false ;
		
		for (Iterator<LdvModelTree> itr = aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvModelTree doc = itr.next() ;
			if (doc.getTreeID().equals(sDocumentId))
				return true ;
		}
		
		return false ;
	}
	
	static public boolean isObjectId(final String sID) {
		return LdvGraphTools.isObjectId(sID) ;
	}
	
	/**
	* Get a LdvModelNode from its node ID
	* 
	* @param sNodeId ID of the node we are looking for
	* @return the node if found, <code>null</code> if not
	* 
	**/
	public LdvModelNode getNodeFromId(final String sNodeId)
	{
		if ((null == sNodeId) || "".equals(sNodeId))
			return null ;
		
		String sTreeId = getURITreeId(sNodeId) ;
		if ("".equals(sTreeId))
			return null ;
		
		LdvModelTree tree = getTreeFromId(sTreeId) ;
		if (null == tree)
			return null ;
		
		return tree.getNodeFromId(sNodeId) ;
	}
	
	/**
	 * Get a LdvModelTree from its tree ID
	 * 
	 * @param sTreeId ID of the tree we are looking for
	 * 
	 * @return the tree if found, <code>null</code> if not
	 */
	public LdvModelTree getTreeFromId(final String sTreeId)
	{
		if (NSGRAPHTYPE.personGraph != _graphType)
			return null ;
		
		// First look into the global trees
		//
		LdvModelTree tree = getTreeFromId(sTreeId, _aTrees) ;
		if (null != tree)
			return tree ;
		
		// Then browse projects
		//
		if (_aProjects.isEmpty())
			return null ;
			
		for (Iterator<LdvModelProjectGraph> itr = _aProjects.iterator() ; itr.hasNext() ; )
		{
			tree = itr.next().getTreeFromId(sTreeId) ;
			if (null != tree)
				return tree ;
		}
						
		return null ;
	}
	
	/**
	 * Get a LdvModelTree from its tree ID inside a vector of trees
	 * 
	 * @param sTreeId Tree ID to look for
	 * @param aTrees  Vector of trees to look into
	 * 
	 * @return The tree if found, <code>null</code> if not
	 */
	public static LdvModelTree getTreeFromId(final String sTreeId, final Vector<LdvModelTree> aTrees)
	{
		if ((null == sTreeId) || "".equals(sTreeId))
			return null ;
		
		if (aTrees.isEmpty())
			return null ;
		
		for (Iterator<LdvModelTree> itr = aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvModelTree tree = itr.next() ;
			if (tree.getTreeID().equals(sTreeId))
				return tree ;
		}	
		
		return null ;
	}
	
	/**
	 * Get a LdvModelLink from its model
	 * 
	 * @param modelLink Model of the link we are looking for
	 * 
	 * @return The link if found, <code>null</code> if not
	 */
	public LdvModelLink getLinkFromModel(final LdvModelLink modelLink) {
		return getLinkFromModel(modelLink, _aLinks) ;
	}
	
	/**
	 * Get a LdvModelLink from its model inside a vector of links
	 * 
	 * @param modelLink Link to look for
	 * @param aLinks    Vector of links to look into
	 * 
	 * @return The link if found, <code>null</code> if not
	 */
	public static LdvModelLink getLinkFromModel(final LdvModelLink modelLink, final Vector<LdvModelLink> aLinks)
	{
		if (null == modelLink)
			return null ;
		
		if (aLinks.isEmpty())
			return null ;
		
		for (Iterator<LdvModelLink> itr = aLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			if (modelLink.equals(link))
				return link ;
		}	
		
		return null ;
	}
	
	/**
	 * Add a copy of a tree to the non project specific array of trees
	 * 
	 * @param tree            Tree to add a copy of in the array of trees
	 * @param sDocumentRosace Access rights rosace for this tree 
	 * @param sForcedID       <code>""</code> if a new tree, its ID if an already existing one
	 */
	public void addTree(final LdvModelTree tree, final String sDocumentRosace, final String sForcedID) {
		addTree(tree, sDocumentRosace, sForcedID, _aTrees, _aRights) ;
	}
	
	/**
	 * Add a copy of a tree to a project specific array of trees
	 * 
	 * @param tree            Tree to add a copy of in the array of trees
	 * @param sDocumentRosace Access rights rosace for this tree 
	 * @param sForcedID       <code>""</code> if a new tree, its ID if an already existing one
	 * @param sProjectId      ID of project to add this tree to
	 */
	public void addTreeToProject(final LdvModelTree tree, final String sDocumentRosace, final String sForcedID, final String sProjectId)
	{
		// Get project's graph and create it if it doesn't exist
		//
		LdvModelProjectGraph projectGraph = getProjectGraphFromID(sProjectId) ;
		if (null == projectGraph)
		{
			projectGraph = new LdvModelProjectGraph(sProjectId) ;
			_aProjects.add(projectGraph) ;
		}
		
		addTree(tree, sDocumentRosace, sForcedID, projectGraph.getTrees(), projectGraph.getRights()) ;
	}
	
	/**
	 * Add a copy of a tree to the array of trees
	 * 
	 * @param tree            Tree to add a copy of in the array of trees
	 * @param sDocumentRosace Access rights rosace for this tree
	 * @param sForcedID       <code>""</code> if a new tree, its ID if an already existing one
	 * @param aTrees          Array of trees to add this tree to
	 * @param aRights         Array of rights to add this tree's rights to
	 */
	public void addTree(final LdvModelTree tree, final String sDocumentRosace, final String sForcedID, Vector<LdvModelTree> aTrees, LdvModelRightArray aRights)
	{
		if (null == tree)
			return ;
		
		LdvModelTree newTree = new LdvModelTree(tree) ;
		
		// Set the document ID for the tree
		//
		String sTreeId = "" ;
		
		if ("".equals(sForcedID))
			sTreeId = newTree.getTreeID() ;
		else
			sTreeId = sForcedID ;
		
		// If set, check validity and if tree already exist in the graph
		//
		boolean bNewTree = true ;
		
		if (false == "".equals(sTreeId))
		{
			if (sTreeId.length() != LdvGraphConfig.OBJECT_ID_LEN)
				return ;
			
			bNewTree = (false == existTreeForId(sTreeId, aTrees)) ;
		}
		
		// If the tree didn't already exist in the graph
		//
		if (bNewTree)
		{
			// If it is the first tree in the graph and it has no specified Id, give it the initial "in memory Id"
			//
			if (hasUninitializedRootId() && "".equals(sForcedID))
			{
				sTreeId = _sLastTreeID ;
				setRootID(sTreeId) ;
			}
			// else, if the tree has no Id yet, give it the next "in memory Id" to come... or the specified id if not empty
			//
			else if ("".equals(sTreeId))
			{
				if ("".equals(sForcedID))
					sTreeId = getNextTreeID() ;
				else
					sTreeId = sForcedID ;
			}	
		}
		
		// Set identifiers to the tree and its nodes
		//
		newTree.setTreeID(sTreeId) ;	
		setNodesIdentification(newTree, sTreeId) ;
		
		// Rights rosaces and models management
		//
		
		// First, remove all already existing rights information for this document (and its nodes)
		//
		if (false == bNewTree)
			aRights.RemoveDocument(sTreeId) ;

		if (false == "".equals(sDocumentRosace))
			aRights.set(sTreeId, sDocumentRosace) ;

		// Then, remove all already existing models information for this document (and its nodes)
		//
		if (false == bNewTree)
			_aModels.RemoveDocument(sTreeId) ;
		
		// Finally, reference all rights and models present in tree's nodes
		//
		for (Iterator<LdvModelNode> itr = newTree.getNodes().iterator() ; itr.hasNext() ; )
		{
			LdvModelNode node = itr.next() ;
			
			if (false == "".equals(node.getNodeRight()))
				aRights.set(node.getNodeID(), node.getNodeRight()) ;
			
			if (false == "".equals(node.getArchetype()))
				_aModels.set(node.getTreeID(), node.getNodeID(), LdvModelModel.MODEL_TYPE.MODEL_ARCHETYPE, node.getArchetype()) ;
		}
		
		// If this tree is already in the graph, update it
		//
		if ((false == bNewTree) && existTreeForId(sTreeId, aTrees))
		{
			LdvModelTree existingTree = getTreeFromId(sTreeId, aTrees) ;
			existingTree.setNodes(existingTree.getNodes()) ;
			return ;
		}
		
		// If not, add it
		//
		aTrees.add(newTree) ;
	}
	
	/**
	 * Set in memory identifiers to new nodes 
	 * 
	 * @param newTree Tree to process
	 * @param sTreeId Tree identifier
	 */
	public void setNodesIdentification(LdvModelTree newTree, final String sTreeId)
	{
		if ((null == newTree) || (null == sTreeId))
			return ;
		
		if (newTree.getNodes().isEmpty())
			return ;
		
		String sLastNode = LdvGraphTools.getFirstInMemoryNodeId() ;
		
		// Set proper identification information to all nodes
		//
		for (Iterator<LdvModelNode> itr = newTree.getNodes().iterator() ; itr.hasNext() ; )
		{
			LdvModelNode node = itr.next() ;
				
			String sCurrentNodeId = node.getNodeID() ;
				
			// If it is a new node (at large or relative to this tree), set its tree Id and reset its node Id 
			//
			if ("".equals(node.getDocumentId()) || (false == sTreeId.equals(node.getTreeID())))
			{
				sCurrentNodeId = "" ;
				node.setTreeID(sTreeId) ;
			}
				
			// If a new node, set its node Id
			//
			if ("".equals(sCurrentNodeId))
			{
				sLastNode = getIncrementedTreeId(sLastNode) ;
				if ("".equals(sLastNode))
					return ;
					
				sCurrentNodeId = sLastNode ;
					
				node.setNodeID(sCurrentNodeId) ;
				node.numberTemporaryNodes() ;
			}
		}
	}
	
	/**
	* Get the object tree (since an object graph contains a single tree, it is the first (and only) in the vector
	* 
	* @return the tree if found, <code>null</code> if not
	* 
	**/
	public LdvModelTree getObjectTree()
	{
		if (_aTrees.isEmpty() || (NSGRAPHTYPE.objectGraph != _graphType))
			return null ;
		
		Iterator<LdvModelTree> itr = _aTrees.iterator() ;
		
		return itr.next() ;
	}
	
	/**
	* Get the person Id sub-part of a qualified or qualifier string.
	* Such string is either an Object string or a tree Id in the form of just personId + treeId or personId + treeId + nodeId
	* 
	* @param sGlobalId qualified or qualifier string
	* @return tree Id sub-part if possible, <code>""</code> elsewhere
	* 
	**/
	static public String getURIPersonId(final String sGlobalId)
  {
		if ((null == sGlobalId) || (sGlobalId.length() < LdvGraphConfig.PERSON_ID_LEN))
			return "" ;

		// Check if it is not an Object qualifier
		//
		if (isObjectId(sGlobalId))
			return "" ;
		
		return sGlobalId.substring(0, LdvGraphConfig.PERSON_ID_LEN) ;
  }
	
	/**
	* Get the tree Id sub-part (ie personId + documentId) of a qualified or qualifier string.
	* Such string is either an Object string or a tree Id in the form personId + treeId
	* 
	* @param sGlobalId qualified or qualifier string
	* @return tree Id sub-part if possible, "" elsewhere
	* 
	**/
	static public String getURITreeId(final String sGlobalId)
  {
		if ((null == sGlobalId) || (sGlobalId.length() < LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN))
			return "" ;

		// Check if it is not an Object qualifier
		//
		if (LdvModelGraph.isObjectId(sGlobalId))
			return "" ;
		
		return sGlobalId.substring(0, LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN) ;
  }
	
	/**
	* Get the document Id sub-part of a qualified or qualifier string (tree Id being in the form personId + documentId).
	* 
	* @param sGlobalId qualified or qualifier string
	* @return document Id sub-part if possible, "" if not
	* 
	**/
	static public String getURIDocumentId(final String sGlobalId)
  {
		if ((null == sGlobalId) || (sGlobalId.length() < LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN))
			return "" ;

		// Check if it is not an Object qualifier
		//
		if (LdvModelGraph.isObjectId(sGlobalId))
			return "" ;
		
		return sGlobalId.substring(LdvGraphConfig.PERSON_ID_LEN, LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN) ;
  }
	
	/**
	* Get the node Id sub-part of a qualified or qualifier string.
	* Such string is either an Object string or a tree Id in the form of just personId + treeId or personId + treeId + nodeId
	* 
	* @param sGlobalId qualified or qualifier string
	* @return node Id sub-part if possible, "" if not
	* 
	**/
	static public String getURINodeId(final String sGlobalId)
  {
		if ((null == sGlobalId) || (sGlobalId.length() < LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN + LdvGraphConfig.NODE_ID_LEN))
			return "" ;

		// Check if it is not an Object qualifier
		//
		if (LdvModelGraph.isObjectId(sGlobalId))
			return "" ;
		
		return sGlobalId.substring(LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN, LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN + LdvGraphConfig.NODE_ID_LEN) ;
  }
	
	/**
	* Get the node Id sub-part of a qualified or qualifier string.
	* Such string is either an Object string or a tree Id in the form of just personId + treeId or personId + treeId + nodeId
	* 
	* @param sGlobalId qualified or qualifier string
	* @return node Id sub-part if possible, "" elsewhere
	* 
	**/
	static public String getNodeURI(final String sPersonId, final String sDocumentId, final String sNodeId)
  {
		if ((null == sPersonId)   || (sPersonId.length()   != LdvGraphConfig.PERSON_ID_LEN)   ||
				(null == sDocumentId) || (sDocumentId.length() != LdvGraphConfig.DOCUMENT_ID_LEN) ||
				(null == sNodeId)     || (sNodeId.length()     != LdvGraphConfig.NODE_ID_LEN))
			return "" ;
		
		return sPersonId + sDocumentId + sNodeId ;
  }
	
	/**
	 * Set the _sLastTreeID parameter 
	 * 
	 * @param sLast The previous "last tree" identifier
	 */
	protected void setLastTree(final String sLast)
	{
		// If no previous object, initialize ID to "zero"
		//
		if ("".equals(sLast))
		{
			if (NSGRAPHTYPE.personGraph == _graphType)
			{
				String sPerson ;
	      if ("".equals(_sROOT_ID) || (_sROOT_ID.length() < LdvGraphConfig.PERSON_ID_LEN))
	      	sPerson = MiscellanousFcts.getNChars(LdvGraphConfig.PERSON_ID_LEN, LdvGraphConfig.MEMORY_CHAR) ;
	      else
	      	sPerson = _sROOT_ID.substring(0, LdvGraphConfig.PERSON_ID_LEN) ;
	      	
	      String sDocum = MiscellanousFcts.getNChars(LdvGraphConfig.DOCUMENT_ID_LEN, '0') ;
	      sDocum = MiscellanousFcts.replace(sDocum, 0, LdvGraphConfig.MEMORY_CHAR) ;
	 
	      _sLastTreeID = sPerson + sDocum ;
			}
			else
	    {
	    	String sObject = MiscellanousFcts.getNChars(LdvGraphConfig.OBJECT_ID_LEN, '0') ;
	      sObject = MiscellanousFcts.replace(sObject, 0, LdvGraphConfig.OBJECT_CHAR) ;
	      sObject = MiscellanousFcts.replace(sObject, 1, LdvGraphConfig.MEMORY_CHAR) ;
	      
	      _sLastTreeID = sObject ;
	    }
			
			return ;
		}
		
		// If the ID is specified, set it after checking its length
		//
		if (sLast.length() == LdvGraphConfig.OBJECT_ID_LEN)
		{
			_sLastTreeID = sLast ;
			return ;
		}
		
		// TODO consider throwing an exception
		//
		_sLastTreeID = "" ;
	}
	
	/**
	* Get the next tree Id as an increment of previous one (which becomes the newly calculated Id)
	* 
	* @return the newly calculated Id if all went well, <code>""</code> if not
	* 
	**/
	public String getNextTreeID()
	{
		String sLast = _sLastTreeID ;

		if (NSGRAPHTYPE.personGraph == _graphType)
		{
	  	String sPerson = _sLastTreeID.substring(0, LdvGraphConfig.PERSON_ID_LEN) ;
	    String sDocum  = _sLastTreeID.substring(LdvGraphConfig.PERSON_ID_LEN, LdvGraphConfig.OBJECT_ID_LEN) ;
	    
	    sDocum = getIncrementedTreeId(sDocum) ;
	    if ("".equals(sDocum))
	    	return "" ;

	    sLast = sPerson + sDocum ;
		}
		else
		{
	  	sLast = getIncrementedTreeId(sLast) ;
	    if ("".equals(sLast))
	    	return "" ;
		}

		setLastTree(sLast) ;
		return sLast ;
	}
	
	/**
	 * Get all links from a given vector that relate to a given document (they are added to an array that may not be initially empty)
	 * 
	 * @param sDocId Document ID
	 * @param links  Vector to fill 
	 * @param aLinks Vector to look into
	 */
	public static void getLinksForDocument(final String sDocId, Vector<LdvModelLink> links, final Vector<LdvModelLink> aLinks)
	{
		if ((null == links) || (null == sDocId) || "".equals(sDocId) || (null == aLinks) || aLinks.isEmpty())
			return ;
		
		for (Iterator<LdvModelLink> itr = aLinks.iterator() ; itr.hasNext() ; )
		{
			LdvModelLink link = itr.next() ;
			
			if (sDocId.equals(link.getQualifiedDocumentId()) || sDocId.equals(link.getQualifierDocumentId()))
				if (false == links.contains(link))
					links.add(new LdvModelLink(link)) ;
		}
	}
	
	/**
	 * Get all links related to a given document (they are added to an array that may not be initially empty)
	 * 
	 * @param sDocId Id of document to get related links of
	 * @param links  The array to fill
	 */
	public void getLinksForDocument(final String sDocId, Vector<LdvModelLink> links)
	{
		// First, have a look in global links
		//
		getLinksForDocument(sDocId, links, _aLinks) ;
		
		// Then browse projects
		//
		if (false == _aProjects.isEmpty())
			for (Iterator<LdvModelProjectGraph> itr = _aProjects.iterator() ; itr.hasNext() ; )
				itr.next().getLinksForDocument(sDocId, links) ;
	}
	
	/**
	 * Get all models related to a given document (they are added to an array that may not be empty at start)
	 * 
	 * @param sDocId Id of document to get related models of
	 * @param models The array to fill
	 */
	public void getModelsForDocument(final String sDocId, Vector<LdvModelModel> models)
	{
		if ((null == models) || (null == sDocId) || "".equals(sDocId) || (null == _aModels) || _aModels.isEmpty())
			return ;
		
		for (Iterator<LdvModelModel> itr = _aModels.iterator() ; itr.hasNext() ; )
		{
			LdvModelModel model = itr.next() ;
			
			if (sDocId.equals(model.getObject()))
				if (false == models.contains(model))
					models.add(new LdvModelModel(model)) ;
		}
	}
	
	/**
	 * Get a model from a tree Id and a node Id (can be null) 
	 * 
	 * @param sTreeId The tree Id
	 * @param sNodeId The node Id (may be null)
	 * 
	 * @return The model if found or <code>null</code> if not
	 */
	public LdvModelModel getModelForNode(final String sTreeId, final String sNodeId)
	{
		if ((null == sTreeId) || "".equals(sTreeId) || (null == _aModels) || _aModels.isEmpty())
			return null ;
		
		boolean bJustTree = (null == sNodeId) || "".equals(sNodeId) ;
		
		for (Iterator<LdvModelModel> itr = _aModels.iterator() ; itr.hasNext() ; )
		{
			LdvModelModel model = itr.next() ;
			
			if ((sTreeId.equals(model.getObject())) && (bJustTree || sNodeId.equals(model.getNode())))
				return model ;
		}
		
		return null ;
	}
	
	/**
	 * Get all rights from a given vector related to a given document (they are added to an array that may not be empty at start)
	 * 
	 * @param sDocId  Id of document to get related rights of
	 * @param rights  The array to fill
	 * @param aRights The array to look into
	 */
	public static void getRightsForDocument(final String sDocId, Vector<LdvModelRight> rights, final Vector<LdvModelRight> aRights)
	{
		if ((null == rights) || (null == sDocId) || "".equals(sDocId) || (null == aRights) || aRights.isEmpty())
			return ;
		
		for (Iterator<LdvModelRight> itr = aRights.iterator() ; itr.hasNext() ; )
		{
			LdvModelRight right = itr.next() ;
			
			if (sDocId.equals(LdvGraphTools.getNodeTreeId(right.getNode())))
				if (false == rights.contains(right))
					rights.add(new LdvModelRight(right)) ;
		}
	}
	
	/**
	 * Get all rights related to a given document (they are added to an array that may not be empty at start)
	 * 
	 * @param sDocId Id of document to get related rights of
	 * @param models The array to fill
	 */
	public void getRightsForDocument(final String sDocId, Vector<LdvModelRight> rights)
	{
		// First, have a look in global links
		//
		getRightsForDocument(sDocId, rights, _aRights) ;
			
		// Then browse projects
		//
		if (_aProjects.isEmpty())
			return ;
			
		for (Iterator<LdvModelProjectGraph> itr = _aProjects.iterator() ; itr.hasNext() ; )
			itr.next().getRightsForDocument(sDocId, rights) ;
	}
	
	/**
	 * Add all rights from a vector to another one (which may not be empty)
	 * 
	 * @param aRightsDestination Vector to be filled
	 * @param aRightsSource      Vector of rights to be copied
	 */
	public static void addRightsVectors(Vector<LdvModelRight> aRightsDestination, Vector<LdvModelRight> aRightsSource)
	{
		if ((null == aRightsDestination) || (null == aRightsSource) || aRightsSource.isEmpty())
			return ;
		
		for (Iterator<LdvModelRight> itr = aRightsSource.iterator() ; itr.hasNext() ; )
		{
			LdvModelRight right = itr.next() ;
			
			if (false == aRightsDestination.contains(right))
				aRightsDestination.add(new LdvModelRight(right)) ;
		}
	}
	
	/**
	* Get the Id that immediately follows a provided Id (in base 36 (0-9)(A-Z))
	* 
	* @param sPreviousId The Id we want an incremented value of
	* 
	* @return the newly calculated Id if all went well, <code>""</code> if not
	* 
	**/
	public String getIncrementedTreeId(String sPreviousId)
	{
		if ((null == sPreviousId) || "".equals(sPreviousId))
	  	return "" ;

		//
		// Incrementing the previous Id
		//
		int  i = sPreviousId.length() - 1 ;
	  char j ;

	  boolean bKeepGoing = true ;
		while (bKeepGoing)
	  {
			// Get the increment of the i-nth character
			//
			j = sPreviousId.charAt(i) ;
	    j++ ;

	    // New character is valid ((0-9)(A-Z)) -> we are done
	    //
	    if (((j >= '0') && (j <= '9')) || ((j >= 'A') && (j <= 'Z')))
	    {
	    	sPreviousId = MiscellanousFcts.replace(sPreviousId, i, j) ;
	    	bKeepGoing = false ;
	    }
	    // New character is between 9 and A -> set to A and we are done
	    //
	    else if ((j > '9') && (j < 'A'))
	    {
	    	sPreviousId = MiscellanousFcts.replace(sPreviousId, i, 'A') ;
	      bKeepGoing = false ;
	    }
	    // New character is after Z -> set to 0 and keep going
	    //
	    else
	    {
	    	sPreviousId = MiscellanousFcts.replace(sPreviousId, i, '0') ;

	      // We have to stop going if we reach 1 because of the "in memory" char located at 0
	      // For objects, stop at 2 because of the "object char" ($)
	      if (((NSGRAPHTYPE.personGraph == _graphType) && (i == 1)) ||
	          ((NSGRAPHTYPE.objectGraph == _graphType) && (i == 2)))
	        return "" ;

	      i--;
	    }
	  }

		return sPreviousId ;
	}
}
