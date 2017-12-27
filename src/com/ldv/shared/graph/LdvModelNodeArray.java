package com.ldv.shared.graph;

import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.util.MiscellanousFcts;

/**
*  Vector of LdvModelNodes<br><br>
*   
*  This vector is sorted on nodes lines since LdvModelNode implements Comparable<br>
*  It could have been a Tree, but when changing line of a node, it would have been necessary to remove then add it
*  
**/
public class LdvModelNodeArray extends Vector<LdvModelNode> implements IsSerializable
{
	private static final long serialVersionUID = -815005981860109867L;
	public  static final int  ORIGINE_PATH_PATHO = 0 ;

	public LdvModelNodeArray()
	{ 
		super() ;
	}
	
	public LdvModelNodeArray(final LdvModelNodeArray src) {
		initFromModel(src) ;
	}
	
	public void init() {
		clear() ;
  }
	
	public void addNode(LdvModelNode node)
	{
		if (null == node)
			return ;
		
		add(node) ;
	}
	
	/**
	*  Adds a new LdvModelNode at the end. This node will contain sLabel as Lexicon information,
	*  be located at column iCol and, if message is not <code>null</code> will be initialized
	*  with other information from message 
	*  
	*  @param sLabel  The Lexicon information
	*  @param message Complementary information or <code>null</code>
	*  @param iCol    The column information
	*   
	**/
	public void addNode(final String sLabel, final BBMessage message, int iCol)
	{
		if ("".equals(sLabel) || (iCol < 0))
			return ;
		
		LdvModelNode node = new LdvModelNode() ;
		
		if (null != message)
			node.initFromMessage(message) ;
		
		node.setLexicon(sLabel) ;
		node.setCol(iCol) ;		
		node.setLine(getNextAvailableLine()) ;
		
		addNode(node) ;
	}
	
	/**
	*  Adds another LdvModelNodeArray at the end.
	*  
	*  @param other     The LdvModelNodeArray to paste at the end of current one 
	*  @param iColShift The level of right shift
	*   
	**/
	public void addVector(final LdvModelNodeArray other, int iColShift)
	{
	  if ((null == other) || (other.isEmpty()))
	    return ;

	  int iLine = getNextAvailableLine() ;

	  for (Iterator<LdvModelNode> itr = other.iterator() ; itr.hasNext() ; )
	  {
	  	LdvModelNode otherNode = itr.next() ;
	  
	  	LdvModelNode node = new LdvModelNode(otherNode) ;
	  	node.setLine(iLine) ;
	  	node.setCol(node.getCol() + iColShift) ;
	  	
	  	addNode(node) ;
	  	
	  	iLine++ ;
	  }
	  
	  // No need to sort since elements are added to the end with a proper line number
	}
	
	/**
	*  Insert another LdvModelNodeArray before a given LdvModelNode 
	*  
	*  @param insertBefore The LdvModelNode to insert before 
	*  @param other        The LdvModelNodeArray to paste at the end of current one
	*  @param iColShift    The level of right shift
	*  @param bAddTreeId   If <code>yes</code>, treeId are to be set for new nodes.
	*   
	**/
	public void insertVector(final LdvModelNode insertBefore, final LdvModelNodeArray other, int iColShift, boolean bAddTreeId)
	{
	  if ((null == other) || (other.isEmpty()))
	    return ;

	  Iterator<LdvModelNode> iterBefore = getIteratorAfterNode(insertBefore) ;
		if (null == iterBefore)
			return ;
		
		int iRefLine = insertBefore.getLine() ;
		
		// First add inserted vector size to line numbers of nodes from the block located behind 
		//
		int iInsertedVectSize = other.size() ;
		
		for (Iterator<LdvModelNode> iterDecal = iterBefore ; iterDecal.hasNext() ; )
		{
			LdvModelNode shiftedNode = iterDecal.next() ;
			shiftedNode.setLine(shiftedNode.getLine() + iInsertedVectSize) ;
		}
		
		// Now add all nodes from the other vector (note: it always keeps being sorted)
		//
		int iLine = 0 ;
		
		for (Iterator<LdvModelNode> iterAdd = other.iterator() ; iterAdd.hasNext() ; )
		{
			LdvModelNode newNode = new LdvModelNode(iterAdd.next()) ;
			
			newNode.setObjectID("") ;
			if (false == bAddTreeId)
				newNode.setTreeID("") ;
			
			newNode.setLine(iRefLine + iLine) ;
			iLine++ ;
	  	newNode.setCol(newNode.getCol() + iColShift) ;
			
			addNode(newNode) ;
		}
		
		// Collections.sort(null) ;
	}
	
	/**
	  * Fills a LdvModelNodeArray with the sons of a LdvModelNode 
	  * 
	  * @param node           LdvModelNode whose sons must be copied
	  * @param patpathoToFill LdvModelNodeArray to fill
	  * 
	  */
	public void extractPatPatho(final LdvModelNode node, LdvModelNodeArray patpathoToFill)
	{
		if (null == patpathoToFill)
			return ;

		if (false == patpathoToFill.isEmpty())
			patpathoToFill.clear() ;

		if (isEmpty() || (null == node))
			return ;
		
		int elementCol = node.getCol() ;
		
		Iterator<LdvModelNode> iter = getIteratorAfterNode(node) ;
		if (null == iter)
			return ;
		
		int     iNewLine   = ORIGINE_PATH_PATHO ;
		boolean bAmongSons = true ;
		
		while (bAmongSons && iter.hasNext())
		{
			LdvModelNode currentNode = iter.next() ;
			
			int currentCol = currentNode.getCol() ;
			if (currentCol > elementCol)
			{
				LdvModelNode newNode = new LdvModelNode(currentNode) ;
				
				newNode.setCol(currentCol - elementCol - 1) ;
				newNode.setLine(iNewLine) ;
				iNewLine++ ;
				
				patpathoToFill.addNode(newNode) ;
			}
			else
				bAmongSons = false ;
		}	
	}
	
	/**
	  * Fills a LdvModelNodeArrayArray (a vector of LdvModelNodeArray) from all brothers of
	  * a given node (including this node) 
	  * 
	  * @param node LdvModelNode whose sons must be copied
	  * @param vect LdvModelNodeArrayArray to fill
	  * 
	  */
	public void extractVectorOfBrothersPatPatho(final LdvModelNode node, LdvModelNodeArrayArray vect)
	{
		if (null == vect)
			return ;

		vect.clear() ;
		
		if ((isEmpty()) || (null == node))
			return ;

		int elementCol = node.getCol() ;
		
		Iterator<LdvModelNode> iter = getIteratorAfterNode(node) ;
		if (null == iter)
			return ;
		
		// Create a buffer PatPatho
		//
		LdvModelNodeArray BrotherPatPatho = new LdvModelNodeArray() ;
	
		int     iLine  = 0 ;
		boolean bStart = true ;

		LdvModelNode currentNode = node ;
		
		while (true)
		{
			int sonCol = currentNode.getCol() ;
			//
			// Too far
			//
			if (sonCol < elementCol)
				break ;
			
			//
			// Same level as reference node, means we are entering a new brother
			//
			if (sonCol == elementCol)
			{
				if (false == bStart)
					vect.add(new LdvModelNodeArray(BrotherPatPatho)) ;

				BrotherPatPatho.clear() ;
				iLine  = 0 ;
				bStart = false ;
			}
			//
			// Adding this node
			//
			if (false == bStart)
			{
				int iCol = sonCol - elementCol ;

				LdvModelNode newNode = new LdvModelNode(currentNode) ;

				newNode.setCol(iCol) ;
				newNode.setLine(iLine) ;
				iLine++ ;

				BrotherPatPatho.add(newNode) ;
			}
			
			if (false == iter.hasNext())
				break ;
				
			currentNode = iter.next() ;
		}

		// adding current buffer
		//
		if (false == bStart)
			vect.add(new LdvModelNodeArray(BrotherPatPatho)) ;
	}
	
	/**
	  * Find a node from its Lexicon (either semantic or not) starting from root
	  * 
	  * @param sItem        Lexicon to look for
	  * 
	  * @return the node if found, or <code>null</code>
	  * 
	  */
	public LdvModelNode findItem(String sItem)
	{
		return findItem(sItem, false, null) ;
	}
	
	/**
	  * Find a node from its Lexicon (either semantic or not)
	  * 
	  * @param sItem        Lexicon to look for
	  * @param bPrepareNext if yes, don't start from the node after root or nodeFrom
	  * @param nodeFrom     if not null, the node the search must start from
	  * 
	  * @return the node if found, or <code>null</code>
	  * 
	  */
	public LdvModelNode findItem(String sItem, boolean bPrepareNext, LdvModelNode nodeFrom)
	{
		if (isEmpty() || "".equals(sItem))
			return null ;

		// Are we comparing on semantic or exact chain
		//
		boolean bOnSemantic = false ;
		if (sItem.length() < LdvGraphConfig.LEXI_LEN)
			bOnSemantic = true ;
		
		Iterator<LdvModelNode> iter ; 
		if (null != nodeFrom)
			iter = getIteratorAfterNode(nodeFrom) ;
		else
			iter = iterator() ;

		if ((bPrepareNext) && (iter.hasNext()))
			iter.next() ;

		while (iter.hasNext())
		{
			LdvModelNode currentNode = iter.next() ; 
			
			String sLexicon = currentNode.getLexicon() ;
			if (bOnSemantic)
				sLexicon = currentNode.getSemanticLexicon() ;

			if (sLexicon.equals(sItem))
				return currentNode ;
		}

		return null ;
	}

	/**
	  * Find a node from its line number
	  * 
	  * @param iLine line number to find node
	  * 
	  * @return the node if found, or <code>null</code>
	  * 
	  */
	public LdvModelNode findNodeForLine(int iLine)
	{
		if (isEmpty())
			return null ;
		
		for (Iterator<LdvModelNode> iter = iterator() ; iter.hasNext() ; )
		{
			LdvModelNode node = iter.next() ;
			if (node.getLine() == iLine)
				return node ;
		}
		
		return null ;
	}
	
	/**
	  * Delete a node (and its sons)
	  * 
	  * @param node LdvModelNode to be deleted
	  * 
	  */
	public void deleteNode(LdvModelNode node)
	{
		if (isEmpty() || (null == node))
			return ;
		
		int iDeletedLine  = node.getLine() ;
		int iDeletedCol   = node.getCol() ;
		
		// Find iterator for node (a pointer for this node in the patpatho)
		//
		Iterator<LdvModelNode> iter = getIteratorAfterNode(node) ;
		
		// Delete sons
		//
		while ((null != iter) && iter.hasNext())
		{
			// Get next node
			//
			LdvModelNode sonNode = iter.next() ;
			int iLine = sonNode.getLine() ;
			int iCol  = sonNode.getCol() ;
					
			// If node is not a son, leave
			//
			if ((iCol <= iDeletedCol) || (iLine < iDeletedLine))
				break ;
			
			iter.remove() ;
				
			// The behavior of an iterator is unspecified if the underlying collection 
			// is modified while the iteration is in progress...
			// ... means that, even if expensive, we have to find back father node
			//
			iter = getIteratorAfterNode(node) ;
		}
		
		// Now, remove node
		//
		iter = getIteratorAfterNode(node) ;
		iter.remove() ;
		
		if (isEmpty())
			return ;
		
		// And refresh line numbers
		//
		LdvModelNode firstUntouchednode = findNodeForLine(iDeletedLine - 1) ;
		refreshLines(firstUntouchednode) ;
	}
	
	/**
	  * Delete all the sons of a node, then sort
	  * 
	  * @param node LdvModelNode which sons are to be deleted
	  * 
	  */
	public void deleteSons(LdvModelNode node)
	{
		deleteSons(node, true) ;
	}
	
	/**
	  * Delete all the sons of a node
	  * 
	  * @param node LdvModelNode which sons are to be deleted
	  * 
	  */
	protected void deleteSons(LdvModelNode node, boolean bThenSort)
	{
		if (isEmpty() || (null == node))
			return ;
		
		int iColPere = node.getCol() ;
		
		// Find iterator for node (a pointer for this node in the patpatho)
		//
		Iterator<LdvModelNode> iter = getIteratorAfterNode(node) ;
		  
		// While node found and not at the end of the patpatho
		//
		while ((null != iter) && iter.hasNext())
		{
			// Get next node
			//
			LdvModelNode sonNode = iter.next() ;
			int iCol = sonNode.getCol() ;
			
			// If node is not a son, leave
			//
			if (iCol <= iColPere)
				return ;
		
			deleteNode(sonNode) ;

			// Go back to the father to iterate
			// (since a node - and its sons - was deleted, we have to reinitialize the iterator) 
			//
			iter = getIteratorAfterNode(node) ;
		}
		
		if (bThenSort)
			Collections.sort(null) ;
	}
	
	/**
	  * Determine the line of a node that would be appended at the end of the tree
	  * 
	  * @return line number
	  * @param node LdvModelNode to compare to
	  * 
	  */
	protected int getNextAvailableLine()
	{
		int iLine = ORIGINE_PATH_PATHO ;
		
		if (isEmpty())
			return iLine ;
		
		LdvModelNode lastNode = getLastNode() ;
		if (null != lastNode)
			iLine = lastNode.getLine() + 1 ;
		
		return iLine ;
	}
	
	/**
	*  Returns the first node 
	*  
	*  @return <code>null</code> if empty, the first node if not
	*   
	**/
	public LdvModelNode getFirstRootNode()
	{
		if (isEmpty())
			return null ;
		
		return get(0) ;
	}
	
	/**
	*  Returns the last node
	*  
	*  @return <code>null</code> if empty, the first node if not
	*   
	**/
	public LdvModelNode getLastNode()
	{
		if (isEmpty())
			return null ;
		
		return get(size() -1) ;
	}
	
	/**
	*  Returns the first "brother node", i.e. the next node with same col and the same father node 
	*  
	*  @param  bro     An iterator 
	*  @param  iBroCol The brother node's column 
	*  
	*  @return <code>null</code> if not found, the node if found 
	**/
	public LdvModelNode getNextBrother(LdvModelNode bro)
	{
		if (isEmpty() || (null == bro))
			return null ;
		
		// First, find bro in current PatPatho
		//
		Iterator<LdvModelNode> itr = getIteratorAfterNode(bro) ;
		if (null == itr)
			return null ;
		
		// Then find a node with the same column in the same branch
		//
		int iCol = bro.getCol() ;
		
		for ( ; itr.hasNext() ; )
		{
			LdvModelNode ModelNode = itr.next() ;
			if (iCol == ModelNode.getCol())
				return ModelNode ;
			
			// If ModelNode's col is lower, it means we are entering another branch
			//
			if (iCol > ModelNode.getCol())
				return null ;
		}
		
		return null ;
	}
	
	/**
	*  Returns the first "son node", i.e. the next node with col + 1 
	*  
	*  @param  bro The Node to find the first brother of
	*  @return <code>null</code> if not found, the node if found 
	**/
	public LdvModelNode getFirstSon(LdvModelNode father)
	{
		if (isEmpty() || (null == father))
			return null ;
		
		// First, find bro in current PatPatho
		//
		Iterator<LdvModelNode> itr = getIteratorAfterNode(father) ;
		if (null == itr)
			return null ;
		
		// Then find a node at column + 1 in the same branch
		//
		int iTargetCol = father.getCol() + 1 ;
		
		for ( ; itr.hasNext() ; )
		{
			LdvModelNode ModelNode = itr.next() ;
			if (ModelNode.getCol() == iTargetCol)
				return ModelNode ;
			
			// If ModelNode's col is lower, it means we are entering another branch
			if (ModelNode.getCol() < iTargetCol)
				return null ;
		}
		
		return null ;
	}
	
	/**
	*  Find the node located at a given exact path (which path is the given path and not simply contains the givent path) 
	*  
	*  @param sPath      Path to look for in the tree
	*  @param sSeparator Path separator
	*  
	*  @return The first node with proper path, or <code>null</code> if no node found
	**/
	public LdvModelNode getNodeForExactPath(final String sPath, final String sSeparator)
	{
		if (isEmpty() || "".equals(sPath))
			return null ;
		
		// We maintain 2 arrays: the first with the path elements, 
		//                       the second with the corresponding nodes in the tree 
		//
		Vector<String>       aConcepts = MiscellanousFcts.ParseString(sPath, sSeparator) ;
		Vector<LdvModelNode> aNodes    = new Vector<LdvModelNode>() ; 
		
		int iCurrentPosInPath = 0 ;
		
		LdvModelNode currentNode = getFirstRootNode() ;
		
		while (null != currentNode)
		{
			// If currentNode fits current concept, we can keep on searching using its sons
			//
			if (currentNode.getSemanticLexicon().equals(aConcepts.get(iCurrentPosInPath)))
			{
				// Set the node at the same stack level as its path element 
				//
				if (aNodes.size() > iCurrentPosInPath)
					aNodes.set(iCurrentPosInPath, currentNode) ;
				else
					aNodes.add(currentNode) ;
				
				iCurrentPosInPath++ ;
				
				// FOUND!
				//
				if (aConcepts.size() == iCurrentPosInPath)
					return currentNode ;
				
				currentNode = getFirstSon(currentNode) ;
			}
			// If currentNode doesn't fit current concept, we switch to its next brother
			//
			else
			{
				currentNode = getNextBrother(currentNode) ;
				
				while (null == currentNode)
				{
					if (0 == iCurrentPosInPath)
						return null ;
					
					iCurrentPosInPath-- ;
					
					currentNode = getNextBrother(aNodes.get(iCurrentPosInPath)) ;
				}
			}
		}
		
		return null ;
	}
	
	/**
	*  Returns an iterator that is located after a node (means next() will return the following node) 
	*  
	*  @param  bro The Node to find the first brother of
	*  @return <code>null</code> if not found, the node if found
	**/
	public Iterator<LdvModelNode> getIteratorAfterNode(LdvModelNode node)
	{
		int iBrotherLine = node.getLine() ;
		
		Iterator<LdvModelNode> itr = iterator() ;
		for ( ; itr.hasNext() && (iBrotherLine != itr.next().getLine()) ; ) ;
		
		if (false == itr.hasNext())
			return null ;
		
		return itr ;
	}

	/**
	*  Refresh line numbers from a given node (means after it), or for the whole collection  
	*  
	*  @param  node The Node to start refreshing from (refresh all if <code>null</code>)
	**/
	protected void refreshLines(LdvModelNode node)
	{
		Iterator<LdvModelNode> itr = iterator() ;
		int iLineNumber = ORIGINE_PATH_PATHO ;
		
		if (null != node)
		{
			Iterator<LdvModelNode> itrNode = getIteratorAfterNode(node) ;
			if (null != itrNode)
			{
				iLineNumber = node.getLine() + 1 ;
				itr = itrNode ;
			}
		}
				
		// And refresh line numbers
		//
		while (itr.hasNext())
		{
			LdvModelNode refreshNode = itr.next() ;
			refreshNode.setLine(iLineNumber++) ;
		}
	}
	
	/**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final LdvModelNodeArray src)
	{
		init() ;
		
		if ((null == src) || src.isEmpty())
			return ;

		for (Iterator<LdvModelNode> itr = src.iterator() ; itr.hasNext() ; )
		{
			LdvModelNode ModelNode = itr.next() ;
			add(new LdvModelNode(ModelNode)) ;
		}
	}
	
	/**
	  * Determine whether two patpatho are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * 
	  * @param other LdvModelNodeArray to compare to
	  * 
	  */
	public boolean isSamePpt(final LdvModelNodeArray other)
	{
		if (null == other)
			return false ;
		
		if (other.size() != this.size())
			return false ;
		
		if (isEmpty())
			return true ;
		
		Iterator<LdvModelNode> itrOther = other.iterator() ;
		Iterator<LdvModelNode> itr      = this.iterator() ;
		
		for ( ; itr.hasNext() ; )
		{
			LdvModelNode ModelNode      = itr.next() ;
			LdvModelNode OtherModelNode = itrOther.next() ;
			if (false == ModelNode.isSameNode(OtherModelNode))
				return false ;
		}
		
		return true ;
	}
	
	/**
	  * Determine whether two patpatho have the same content (without regard to identifiers)
	  * 
	  * @return true if all nodes have the same content, false if not
	  * 
	  * @param other LdvModelNodeArray to compare to
	  * 
	  */
	public boolean hasSameContent(final LdvModelNodeArray other)
	{
		if (null == other)
			return false ;
		
		if (other.size() != this.size())
			return false ;
		
		if (isEmpty())
			return true ;
		
		Iterator<LdvModelNode> itrOther = other.iterator() ;
		Iterator<LdvModelNode> itr      = this.iterator() ;
		
		for ( ; itr.hasNext() && itrOther.hasNext() ; )
		{
			LdvModelNode ModelNode      = itr.next() ;
			LdvModelNode OtherModelNode = itrOther.next() ;
			if (false == ModelNode.hasSameContent(OtherModelNode))
				return false ;
		}
		
		return true ;
	}
}
