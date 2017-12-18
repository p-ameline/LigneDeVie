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
	
	protected NSGRAPHTYPE           _graphType ;
	protected String                _sROOT_ID ;
	protected String                _sLastTree ;
	protected Vector<LdvModelTree>  _aTrees ;
	protected Vector<LdvModelLink>  _aLinks ;
	protected LdvModelRightArray    _aRights ;
	
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
			
	protected void init()
	{
		_sROOT_ID = getInitialRootId() ;
		
		setLastTree("") ;
		
		_aTrees   = new Vector<LdvModelTree>() ;
		_aLinks   = new Vector<LdvModelLink>() ;
		_aRights  = new LdvModelRightArray() ;
	}
	
	public void reset()
	{
		_sROOT_ID = getInitialRootId() ;
		
		if (null != _aTrees)
			_aTrees.clear() ;
		if (null != _aLinks)
			_aLinks.clear() ;
		if (null != _aLinks)
			_aLinks.clear() ;
	}
	
	public String getInitialRootId()
	{
		if (NSGRAPHTYPE.objectGraph == _graphType)
			return LdvGraphConfig.UNKNOWN_OBJECT[1] ;
		
		return LdvGraphConfig.UNKNOWN_USER[1] + LdvGraphConfig.UNKNOWN_ROOTDOC[1] ;
	}
	
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
						
		if (false == other._aLinks.isEmpty())
			for (Iterator<LdvModelLink> itr = other._aLinks.iterator() ; itr.hasNext() ; )
				_aLinks.add(new LdvModelLink(itr.next())) ;
		
		_aRights.initFromModelRightArray(other._aRights) ;		
	}
	
	public String getRootID() {
  	return _sROOT_ID ;
  }
	public void setRootID(String sID) {
		_sROOT_ID = sID ;
  }
	
	public Vector<LdvModelRight> getRightForNode(String sNodeId) {
		return _aRights.getRightForNode(sNodeId) ;
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

	public Vector<LdvModelRight> getRights() {
  	return _aRights ;
  }

	/**
	 * Does the graph contain any tree?
	 * 
	 * @return <code>true</code> if there is no tree in graph, <code>false</code> if not
	 * 
	 **/
	public boolean isEmpty() {
		return _aTrees.isEmpty() ; 
	}
	
	/**
	 * Does a given tree exist in graph?
	 * 
	 * @param sTreeId tree Id to look for in graph
	 * @return true if this tree exists in graph, false if not
	 * 
	 **/
	public boolean existTreeForId(final String sDocumentId)
	{
		if ((null == sDocumentId) || sDocumentId.equals(""))
			return false ;
		
		if (_aTrees.isEmpty())
			return false ;
		
		for (Iterator<LdvModelTree> itr = _aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvModelTree doc = itr.next() ;
			if (doc.getTreeID().equals(sDocumentId))
				return true ;
		}
		
		return false ;
	}
	
	static public boolean isObjectId(final String sID)
	{
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
	* Get a LdvModelNode from its node ID
	* 
	* @param sNodeId ID of the node we are looking for
	* @return the node if found, <code>null</code> if not
	* 
	**/
	public LdvModelTree getTreeFromId(final String sTreeId)
	{
		if ((null == sTreeId) || "".equals(sTreeId))
			return null ;
		
		if (_aTrees.isEmpty() || (NSGRAPHTYPE.personGraph != _graphType))
			return null ;
		
		for (Iterator<LdvModelTree> itr = _aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvModelTree doc = itr.next() ;
			if (doc.getTreeID().equals(sTreeId))
				return doc ;
		}	
		
		return null ;
	}
	
	/**
	* Add a copy of a tree to the array of trees
	* 
	* @param tree      Tree to add a copy of in the array of trees 
	* @param sForcedID <code>""</code> if a new tree, its ID if an already existing one
	* 
	**/
	public void addTree(final LdvModelTree tree, final String sDocumentRosace, final String sForcedID)
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
		
		// If set, check validity and novelty status
		//
		boolean bNewTree = true ;
		
		if (false == "".equals(sTreeId))
		{
			if (sTreeId.length() != LdvGraphConfig.OBJECT_ID_LEN)
				return ;
			
			bNewTree = (false == existTreeForId(sTreeId)) ;
		}
		
		if (bNewTree)
		{
			// If it is the first tree in the graph and it has no specified Id, give it the initial "in memory Id"
			//
			if (_sROOT_ID.equals(getInitialRootId()) && "".equals(sForcedID))
			{
				sTreeId = _sLastTree ;
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
		
		newTree.setTreeID(sTreeId) ;
		
		String sLastNode = MiscellanousFcts.getNChars(LdvGraphConfig.NODE_ID_LEN, '0') ;
		sLastNode = MiscellanousFcts.replace(sLastNode, 0, LdvGraphConfig.MEMORY_CHAR) ;
		
		// Set proper identification information to all nodes
		//
		for (Iterator<LdvModelNode> itr = newTree.getNodes().iterator() ; itr.hasNext() ; )
		{
			LdvModelNode node = itr.next() ;
			
			String sCurrentNodeId = node.getNodeID() ;
			
			// If it is a new node (at large or relative to this tree), set its tree Id and reset its node Id 
			//
			if ("".equals(node.getDocumentId()) || (false == sTreeId.equals(node.getDocumentId())))
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
		
		// Rights rosaces management
		//
		// First, remove all already existing rights information for this document (and its nodes)
		//
		if (false == bNewTree)
			_aRights.RemoveDocument(sTreeId) ;

		if (false == "".equals(sDocumentRosace))
			_aRights.set(sTreeId, sDocumentRosace) ;

		for (Iterator<LdvModelNode> itr = newTree.getNodes().iterator() ; itr.hasNext() ; )
		{
			LdvModelNode node = itr.next() ;
			
			if (false == "".equals(node.getNodeRight()))
				_aRights.set(node.getNodeID(), node.getNodeRight()) ;
		}
		
		// If this tree is already in the graph, update it
		//
		if ((false == bNewTree) && existTreeForId(sTreeId))
		{
			LdvModelTree existingTree = getTreeFromId(sTreeId) ;
			existingTree.setNodes(existingTree.getNodes()) ;
			return ;
		}
			
		_aTrees.add(newTree) ;
	}
	
	/**
	* Get a LdvModelNode from its node ID
	* 
	* @return the node if found, <code>null</code> if not
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
		if (LdvModelGraph.isObjectId(sGlobalId))
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
	* Get the document Id sub-part of a qualified or qualifier string (tree Id being in the form personId + treeId).
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
	 
	      _sLastTree = sPerson + sDocum ;
			}
			else
	    {
	    	String sObject = MiscellanousFcts.getNChars(LdvGraphConfig.OBJECT_ID_LEN, '0') ;
	      sObject = MiscellanousFcts.replace(sObject, 0, LdvGraphConfig.OBJECT_CHAR) ;
	      sObject = MiscellanousFcts.replace(sObject, 1, LdvGraphConfig.MEMORY_CHAR) ;
	      
	      _sLastTree = sObject ;
	    }
			
			return ;
		}
		
		// If the ID is specified, set it after checking its length
		//
		if (sLast.length() == LdvGraphConfig.OBJECT_ID_LEN)
		{
			_sLastTree = sLast ;
			return ;
		}
		
		// TODO consider throwing an exception
		//
		_sLastTree = "" ;
	}
	
	/**
	* Get the next tree Id as an increment of previous one (which becomes the newly calculated Id)
	* 
	* @return the newly calculated Id if all went well, <code>""</code> if not
	* 
	**/
	protected String getNextTreeID()
	{
		String sLast = _sLastTree ;

		if (NSGRAPHTYPE.personGraph == _graphType)
		{
	  	String sPerson = _sLastTree.substring(0, LdvGraphConfig.PERSON_ID_LEN) ;
	    String sDocum  = _sLastTree.substring(LdvGraphConfig.PERSON_ID_LEN, LdvGraphConfig.OBJECT_ID_LEN) ;
	    
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
