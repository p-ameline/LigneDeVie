package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A project, as a sub-graph as it travels from server to client
 * 
 **/
public class LdvModelProjectGraph implements IsSerializable
{
	protected String                _sProjectID ;
	
	protected Vector<LdvModelTree>  _aTrees ;
	protected Vector<LdvModelLink>  _aLinks ;
	protected LdvModelRightArray    _aRights ;
	
	public LdvModelProjectGraph(final String sProjectID)
	{
		init() ;
		
		_sProjectID = sProjectID ;
	}
	
	public LdvModelProjectGraph() {
		init() ; 
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model graph
	 * 
	 **/
	public LdvModelProjectGraph(final LdvModelProjectGraph source)
	{
		init() ;
		
		initFromModelProjectGraph(source) ;
	}
			
	/**
	 * Initialize all information
	 */
	protected void init()
	{
		_sProjectID = "" ;
		_aTrees     = new Vector<LdvModelTree>() ;
		_aLinks     = new Vector<LdvModelLink>() ;
		_aRights    = new LdvModelRightArray() ;
	}
	
	/**
	 * Reset all information
	 */
	public void reset()
	{
		_sProjectID = "" ;
		
		if (null != _aTrees)
			_aTrees.clear() ;
		if (null != _aLinks)
			_aLinks.clear() ;
		if (null != _aRights)
			_aRights.clear() ;
	}
	
	/**
	 * Affectation operator
	 * 
	 * @param other Other object to initialize from
	 */
	protected void initFromModelProjectGraph(final LdvModelProjectGraph other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_sProjectID = other._sProjectID ;
		
		if (false == other._aTrees.isEmpty())
			for (Iterator<LdvModelTree> itr = other._aTrees.iterator() ; itr.hasNext() ; )
				_aTrees.add(new LdvModelTree(itr.next())) ;
						
		addToLinks(other._aLinks) ;
		
		_aRights.initFromModelRightArray(other._aRights) ;
	}
	
	public String getProjectID() {
  	return _sProjectID ;
  }
	public void setProjectID(String sID) {
		_sProjectID = sID ;
  }
	
	/**
	 * Get the rights for a given node
	 * 
	 * @param sNodeId Id of node to get rights for
	 * 
	 * @return A vector of LdvModelRight (that may be empty)
	 */
	public Vector<LdvModelRight> getRightForNode(final String sNodeId) {
		return LdvModelGraph.getRightForNode(sNodeId, _aRights) ;
	}
		
	public Vector<LdvModelTree> getTrees() {
  	return _aTrees ;
  }

	public Vector<LdvModelLink> getLinks() {
  	return _aLinks ;
  }
	public void addToLinks(final Vector<LdvModelLink> aLinks) {
		LdvModelGraph.addToLinks(aLinks, _aLinks) ;
	}

	public LdvModelRightArray getRights() {
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
	public boolean existTreeForId(final String sDocumentId) {
		return LdvModelGraph.existTreeForId(sDocumentId, _aTrees) ;
	}
	
	/**
	* Get a LdvModelNode from its node ID inside project's trees
	* 
	* @param sNodeId ID of the node we are looking for
	* @return the node if found, <code>null</code> if not
	* 
	**/
	public LdvModelNode getNodeFromId(final String sNodeId)
	{
		if ((null == sNodeId) || "".equals(sNodeId))
			return null ;
		
		// Get the tree ID for this node
		//
		String sTreeId = LdvModelGraph.getURITreeId(sNodeId) ;
		if ("".equals(sTreeId))
			return null ;
		
		// Look for the corresponding tree
		//
		LdvModelTree tree = getTreeFromId(sTreeId) ;
		if (null == tree)
			return null ;
		
		// Find the node inside the tree
		//
		return tree.getNodeFromId(sNodeId) ;
	}
		
	/**
	 * Get a LdvModelTree from its tree ID
	 * 
	 * @param sTreeId ID of the tree we are looking for
	 * 
	 * @return the tree if found, <code>null</code> if not
	 */
	public LdvModelTree getTreeFromId(final String sTreeId) {
		return LdvModelGraph.getTreeFromId(sTreeId, _aTrees) ;
	}
	
	/**
	 * Get a LdvModelLink from its model
	 * 
	 * @param modelLink Model of the link we are looking for
	 * 
	 * @return the link if found, <code>null</code> if not
	 */
	public LdvModelLink getLinkFromModel(final LdvModelLink modelLink) {
		return LdvModelGraph.getLinkFromModel(modelLink, _aLinks) ;
	}
	
	/**
	 * Add a copy of a tree to the array of trees
	 * 
	 * @param tree      Tree to add a copy of in the array of trees 
	 * @param sForcedID <code>""</code> if a new tree, its ID if an already existing one
	 */
/*
	public void addTree(LdvModelGraph graph, final LdvModelTree tree, final String sDocumentRosace, final String sForcedID) {
		graph.addTree(tree, sDocumentRosace, sForcedID, _aTrees, _aRights) ;
	}
*/

	/**
	 * Get all links related to a given document (they are added to an array that may not be initially empty)
	 * 
	 * @param sDocId Id of document to get related links of
	 * @param links  The array to fill
	 */
	public void getLinksForDocument(final String sDocId, Vector<LdvModelLink> links) {
		LdvModelGraph.getLinksForDocument(sDocId, links, _aLinks) ;
	}

	/**
	 * Get all rights related to a given document (they are added to an array that may not be empty at start)
	 * 
	 * @param sDocId Id of document to get related rights of
	 * @param models The array to fill
	 */
	public void getRightsForDocument(final String sDocId, Vector<LdvModelRight> rights) {
		LdvModelGraph.getRightsForDocument(sDocId, rights, _aRights) ;
	}
}
