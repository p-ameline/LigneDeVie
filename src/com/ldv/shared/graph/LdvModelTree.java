package com.ldv.shared.graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.ldv.shared.model.LdvDate;
import com.ldv.shared.model.LdvNum;
import com.ldv.shared.model.LdvTime;

/**
 * An information tree as it travels into a LdvModelGraph  
 * 
 **/
public class LdvModelTree implements IsSerializable
{
	protected String            _sTreeId ;  // Either an object Id or a Person + tree Id
	protected LdvModelNodeArray _aNodes ;
	
	public LdvModelTree()
	{
		init() ;
	}
	
	public LdvModelTree(String sDocumentId)
	{
		init() ;
		_sTreeId = sDocumentId ;
	}
	
	/**
	 * Copy constructor  
	 * 
	 * @param source Model node
	 * 
	 **/
	public LdvModelTree(final LdvModelTree source) 
	{
		init() ;
		initFromModelTree(source) ;
	}
	
	/**
	 * Sets all information by copying other tree content   
	 * 
	 * @param other Model tree
	 * @return void
	 * 
	 **/
	public void initFromModelTree(final LdvModelTree other)
	{
		reset() ;
		
		if (null == other)
			return ;
		
		_sTreeId = other._sTreeId ;
		
		if ((null == _aNodes) || (null == other._aNodes) || other._aNodes.isEmpty())
			return ;
		
		for (Iterator<LdvModelNode> itr = other._aNodes.iterator() ; itr.hasNext() ; )
			_aNodes.add(new LdvModelNode(itr.next())) ;
	}
	
	public void sortByLocalisation()
	{
		Comparator<LdvModelNode> orderLocalisation = new Comparator<LdvModelNode>()
		{
			public int compare(LdvModelNode node1, LdvModelNode node2)
			{ return node1.getLine() - node2.getLine() ; }
		};
		
		Collections.sort(_aNodes, orderLocalisation) ;
	}
	
	void init()
	{
		_sTreeId = "" ;
		_aNodes  = new LdvModelNodeArray() ;
	}
	
	public void reset()
	{
		_sTreeId = "" ;
		resetNodes() ;
	}
	
	public void resetNodes()
	{
		if (false == _aNodes.isEmpty())
			_aNodes.clear() ;
	}
	
	/**
	 * Add a copy of provided node as a new node (at the end of the tree) at a given column 
	 * 
	 * @param node node to be added
	 * @param iColNumber column the new node has to be added to
	 * 
	 **/
	public void addNode(final LdvModelNode node, final int iColNumber)
	{
		if ((null == node) || (iColNumber < 0))
			return ;
		
		LdvModelNode newNode = new LdvModelNode(node) ;
		newNode.setCol(iColNumber) ;
		
		if (_aNodes.isEmpty())
			newNode.setLine(0) ;
		else
		{
			int iLastLine = _aNodes.lastElement().getLine() ;
			newNode.setLine(iLastLine + 1) ;
		}
		
		// If tree Id has not been set, it is high time we do it
		//
		if (("".equals(newNode.getTreeID())) && (false == "".equals(_sTreeId)))
		{
			if (LdvGraphTools.isObjectId(_sTreeId))
				newNode.setObjectID(_sTreeId) ;
			else
			{
				newNode.setPersonID(LdvGraphTools.getDocumentPersonId(_sTreeId)) ;
				newNode.setTreeID(LdvGraphTools.getDocumentTreeId(_sTreeId)) ;
			}
		}
		
		_aNodes.add(newNode) ;
	}

	/**
	 * Find the node from its ID 
	 * 
	 * @param sNodeId ID of the node we are looking for
	 * @return the node if found, <code>null</code> if not
	 * 
	 **/
	public LdvModelNode getNodeFromId(final String sNodeId)
	{
		if (_aNodes.isEmpty())
			return null ;
		
		// ToDo: Could be faster using a true sorting algorithm (if we know that nodes are sorted)
		//
		Iterator<LdvModelNode> nodeIter = _aNodes.iterator() ;
		while (nodeIter.hasNext())
		{
			LdvModelNode node = nodeIter.next() ;
			if (sNodeId.equals(node.getNodeURI()))
				return node ;
		}
		
		return null ;
	}
	
	/**
	 * Returns tree's root node 
	 * 
	 * @return the corresponding LdvModelNode if found, <code>null</code> if not
	 * 
	 **/
	public LdvModelNode getRootNode() {
		return getNodeAtIndex(0) ;
	}
	
	/**
	 * Find the first son node of a node
	 * 
	 * @param node Reference node
	 * @return LdvModelNode of first son if found, <code>null</code> if not
	 * 
	 **/
	public LdvModelNode findFirstSon(final LdvModelNode node)
	{
		int iNodeIndex = findNodeIndex(node) ;
		if (-1 == iNodeIndex)
			return null ;
		
		int iFirstSonNodeIndex = findFirstSonIndex(iNodeIndex) ;
		if (-1 == iFirstSonNodeIndex)
			return null ;
		
		return getNodeAtIndex(iFirstSonNodeIndex) ;
	}
	
	/**
	 * Find the first brother node of a node
	 * 
	 * @param node Reference node
	 * @return LdvModelNode of first brother if found, <code>null</code> if not
	 * 
	 **/
	public LdvModelNode findFirstBrother(final LdvModelNode node)
	{
		int iNodeIndex = findNodeIndex(node) ;
		if (-1 == iNodeIndex)
			return null ;
		
		int iFirstBrotherNodeIndex = findFirstBrotherIndex(iNodeIndex) ;
		if (-1 == iFirstBrotherNodeIndex)
			return null ;
		
		return getNodeAtIndex(iFirstBrotherNodeIndex) ;
	}
	
	/**
	 * Find the node located at a given line and column 
	 * 
	 * @param iLine line
	 * @param iCol column
	 * @return the node if found, <code>null</code> if not
	 * 
	 **/
	public LdvModelNode findNode(final int iLine, final int iCol)
	{
		if (_aNodes.isEmpty())
			return null ;
		
		Iterator<LdvModelNode> nodeIter = _aNodes.iterator() ;
		while (nodeIter.hasNext())
		{
			LdvModelNode node = nodeIter.next() ;
			if ((node.getLine() == iLine) && (node.getCol() == iCol))
				return node ;
		}
		
		return null ;
	}
	
	/**
	 * Find the node located at a given line and column and returns its index 
	 * 
	 * @param iLine node's line
	 * @param iCol node's column
	 * @return index of the node if found, <code>-1</code> if not
	 * 
	 **/
	public int findNodeIndex(final int iLine, final int iCol)
	{
		if (_aNodes.isEmpty())
			return -1 ;
		
		ListIterator<LdvModelNode> nodeIter = _aNodes.listIterator() ;
		while (nodeIter.hasNext())
		{
			LdvModelNode node = nodeIter.next() ;
			if ((node.getLine() == iLine) && (node.getCol() == iCol))
				return nodeIter.previousIndex() ;
		}
		
		return -1 ;
	}
		
	/**
	 * Find node in tree and returns its index 
	 * 
	 * @param  node LdvModelNode to get index of
	 * @return index of the node if found, <code>-1</code> if not
	 * 
	 **/
	public int findNodeIndex(final LdvModelNode node)
	{
		if (_aNodes.isEmpty())
			return -1 ;
		
		String sNodeId = node.getNodeID() ;
		
		ListIterator<LdvModelNode> nodeIter = _aNodes.listIterator() ;
		while (nodeIter.hasNext())
		{
			LdvModelNode nextNode = nodeIter.next() ;
			if (sNodeId.equals(nextNode.getNodeID()))
				return nodeIter.previousIndex() ;
		}
		
		return -1 ;
	}
	
	/**
	 * Find the first son node of the node located at a given index and returns its index 
	 * 
	 * @param iFatherIndex father node's index
	 * @return index of the node if found, <code>-1</code> if not
	 * 
	 **/
	public int findFirstSonIndex(final int iFatherIndex) throws IndexOutOfBoundsException
	{
		if (_aNodes.isEmpty())
			return -1 ;
		
		if ((iFatherIndex < 0) || (iFatherIndex >= _aNodes.size()))
			throw new IndexOutOfBoundsException() ;
		
		ListIterator<LdvModelNode> nodeIter = _aNodes.listIterator(iFatherIndex) ;
		LdvModelNode fatherNode = nodeIter.next() ;
		int iFatherCol = fatherNode.getCol() ;
		
		if (nodeIter.hasNext())
		{
			LdvModelNode node = nodeIter.next() ;
			if (node.getCol() > iFatherCol)
				return nodeIter.previousIndex() ;
		}
		
		return -1 ;
	}
	
	/**
	 * Find the first brother node of the node located at a given index and returns its index 
	 * 
	 * @param iReferenceBrotherIndex reference node's index
	 * @return index of the node if found, <code>-1</code> if not
	 * 
	 **/
	public int findFirstBrotherIndex(final int iReferenceBrotherIndex) throws IndexOutOfBoundsException
	{
		if (_aNodes.isEmpty())
			return -1 ;
		
		if ((iReferenceBrotherIndex < 0) || (iReferenceBrotherIndex >= _aNodes.size()))
			throw new IndexOutOfBoundsException() ;
		
		ListIterator<LdvModelNode> nodeIter = _aNodes.listIterator(iReferenceBrotherIndex) ;
		LdvModelNode refBrotherNode = nodeIter.next() ;
		int iRefBrotherCol = refBrotherNode.getCol() ;
		
		while (nodeIter.hasNext())
		{
			LdvModelNode node = nodeIter.next() ;
			if (node.getCol() == iRefBrotherCol)
				return nodeIter.previousIndex() ;
			
			// This test means that we met a father or grandfather
			//
			if (node.getCol() < iRefBrotherCol)
				return -1 ;
		}
		
		return -1 ;
	}
	
	/**
	*  Parse sub-nodes of a date node (KOUVR, KFERM...) in order to find its date
	*  
	*  @param fatherNode Node of the date concept
	*  @param date       LdvDate to instantiate
	**/
	public void getDate(LdvModelNode fatherNode, LdvDate date)
	{
		if ((null == fatherNode) || (null == date))
			return ;
		
		String sUnit   = "" ;
		String sFormat = "" ;
		String sValue  = "" ;
		
		LdvModelNode sonNode = findFirstSon(fatherNode) ;
		while (null != sonNode)
		{
			if (sonNode.startsWithPound())
			{
				sFormat = sonNode.getSemanticLexicon() ;
				sValue  = sonNode.getComplement() ;
				sUnit   = sonNode.getSemanticUnit() ;
				
				if ((sFormat.length() > 2) && (false == "".equals(sValue)) && (false == "".equals(sUnit)))
				{
					String sFormatType = sFormat.substring(1, 2) ;
					if ("D".equals(sFormatType) || "T".equals(sFormatType))
					{
						date.setDate(sValue) ;
						date.setFormat(sFormat) ;
						date.setUnit(sonNode.getUnit()) ;
						date.setSemanticUnit(sUnit) ;
					}
				}
			}	
			
			sonNode = findFirstBrother(sonNode) ;
		}		
	}
	
	/**
	*  Parse sub-nodes of a date node (KOUVR, KFERM...) in order to find its date
	*  
	*  @param  fatherNode Node of the date concept
	*  @return A date in the YYYYMMDDHHmmss form or an empty string if not found
	**/
	public LdvTime getDate(LdvModelNode fatherNode)
	{
		if (null == fatherNode)
			return null ;
		
		LdvDate date = new LdvDate() ;
		getDate(fatherNode, date) ;
		
		if (date.isEmpty())
			return null ;
		
		// Not a proper unit
		//
		String sUnit = date.getSemanticUnit() ;
		if ((false == sUnit.equals("2DA01")) && (false == sUnit.equals("2DA02")))
			return null ;
		
		LdvTime ldvTime = new LdvTime(0) ;
		ldvTime.initFromLocalDate(date.getDate()) ;
		
		return ldvTime ;
	}
	
	/**
	*  Parse sub-nodes of a date node (KOUVR, KFERM...) in order to find its date
	*  
	*  @param  fatherNode Node of the date concept
	*  @return A date in the YYYYMMDDHHmmss form or an empty string if not found
	**/
	public void getNum(LdvModelNode fatherNode, LdvNum num)
	{
		if ((null == fatherNode) || (null == num)) 
			return ;
		
		String sUnit   = "" ;
		String sFormat = "" ;
		String sValue  = "" ;
		
		int iIndice = -1 ;
		
		LdvModelNode sonNode = findFirstSon(fatherNode) ;
		while (null != sonNode)
		{
			String sSemanticConcept = sonNode.getSemanticLexicon() ;
			
			if (sonNode.startsWithPound())
			{
				sFormat = sonNode.getSemanticLexicon() ;
				sValue  = sonNode.getComplement() ;
				sUnit   = sonNode.getSemanticUnit() ;
				
				iIndice++ ;
				
				num.instantiate(sValue, sUnit, sFormat, iIndice) ;
			}	
			else if ("VNOMA".equals(sSemanticConcept))
			{
				LdvNum numNormal = new LdvNum() ;
				getNum(sonNode, numNormal) ;
				num.setNormal(numNormal, iIndice) ;
			}
			else if ("VNOMI".equals(sSemanticConcept))
			{
				LdvNum numNormal = new LdvNum() ;
				getNum(sonNode, numNormal) ;
				num.setLowerNormal(numNormal, iIndice) ;
			}
			else if ("VNOMS".equals(sSemanticConcept))
			{
				LdvNum numNormal = new LdvNum() ;
				getNum(sonNode, numNormal) ;
				num.setUpperNormal(numNormal, iIndice) ;
			}
			else if ("KDARE".equals(sSemanticConcept))
			{
				LdvDate date = new LdvDate() ;
				getDate(sonNode, date) ;
				num.setDate(date) ;
			}
			
			sonNode = findFirstBrother(sonNode) ;
		}
	}
	
	/**
	*  Parse sub-nodes of a node that is supposed to contain a free text in order to get it
	*  
	*  @param  fatherNode Node of the date concept
	*  @return The (last found) free text if found, <code>""</code> if not
	**/
	public String getFreeText(LdvModelNode fatherNode)
	{
		if (null == fatherNode)
			return "" ;
		
		String sText = "" ;
		
		LdvModelNode sonNode = findFirstSon(fatherNode) ;
		while (null != sonNode)
		{
			if (sonNode.startsWithPound())
			{
				String sFollows = sonNode.followsPound() ;
				if (("??".equals(sFollows)) || ("CL".equals(sFollows)) || ("C;".equals(sFollows))) 
					sText = sonNode.getFreeText() ;
			}

			sonNode = findFirstBrother(sonNode) ;
		}
		
		return sText ;
	}
	
	/**
	 * Returns the "value" of a node: either a num, or a date, or a free text, or a semantic concept
	 * 
	 * @param iIndex index (from <code>0</code> to <code>size-1</code>)
	 * @return the corresponding LdvModelNode if found, <code>null</code> if not
	 * 
	 **/
	public String getNodeValue(LdvModelNode fatherNode)
	{
		if (null == fatherNode)
			return "" ;
		
		// Is there a free text?
		//
		String sFreeText = getFreeText(fatherNode) ;
		if (false == "".equals(sFreeText))
			return sFreeText ;
		
		// Is there a numerical value?
		//
		LdvNum num = new LdvNum() ;
		getNum(fatherNode, num) ;
		if (false == num.isEmpty(0))
			return num.toString() ;
		
		// Is there a date?
		//
		LdvTime ldvTime = getDate(fatherNode) ;
		if (null != ldvTime)
			return ldvTime.getLocalFullDateTime() ;
		
		// Is there a concept?
		//
		LdvModelNode sonNode = findFirstSon(fatherNode) ;
		if (null != sonNode)
			return sonNode.getSemanticLexicon() ;
		
		return "" ;
	}
	
	/**
	 * Provide all new nodes (those with no ID) with an in-memory ID
	 */
	public void provideNewNodesWithInMemoryId()
	{
		if ((null == _aNodes) || _aNodes.isEmpty())
			return ;
		
		boolean bExistNoIdNodes = false ;
		
		// First, find max ID for existing in-memory nodes 
		//
		String sMaxMemoryID = "" ;
		
		for (Iterator<LdvModelNode> itr = _aNodes.iterator() ; itr.hasNext() ; )
		{
			String sNodeID = itr.next().getNodeID() ;
			if ("".equals(sNodeID))
				bExistNoIdNodes = true ;
			else if (LdvGraphTools.isInMemoryNode(sNodeID) && LdvGraphTools.isIdGreaterThan(sNodeID, sMaxMemoryID))
				sMaxMemoryID = sNodeID ;
		}
		
		if (false == bExistNoIdNodes)
			return ;
		
		// If there is no previous in-memory node, get the first ID
		//
		if ("".equals(sMaxMemoryID))
			sMaxMemoryID = LdvGraphTools.getFirstInMemoryNodeId() ;
		else
			sMaxMemoryID = LdvGraphTools.getNextId(sMaxMemoryID) ;
		
		// Provide in-memory nodes IDs for nodes with no node ID
		//
		for (Iterator<LdvModelNode> itr = _aNodes.iterator() ; itr.hasNext() ; )
		{
			LdvModelNode node = itr.next() ;
			if ("".equals(node.getNodeID()))
			{
				node.setNodeID(sMaxMemoryID) ;
				sMaxMemoryID = LdvGraphTools.getNextId(sMaxMemoryID) ;
			}
		}
	}
	
	/**
	 * Is tree empty ? 
	 * 
	 * @return <code>true</code> if empty, <code>false</code> if not
	 * 
	 */
	public boolean isEmpty() {
		return _aNodes.isEmpty() ;
	}

	public String getTreeID() {
  	return _sTreeId ;
  }
	public void setTreeID(String sID) {
		_sTreeId = sID ;
  }
	
	/**
	 * Set the document Id at large and for all nodes 
	 * 
	 * @param sID document Id
	 * 
	 **/
	public void setDocumentIDExtended(String sID)
	{
		_sTreeId = sID ;
		
		if (_aNodes.isEmpty())
			return ;
		
		Iterator<LdvModelNode> nodeIter = _aNodes.iterator() ;
		while (nodeIter.hasNext())
		{
			LdvModelNode node = nodeIter.next() ;
			node.setTreeID(sID) ;
		}
	}

	public LdvModelNodeArray getNodes() {
  	return _aNodes ;
  }
	public void setNodes(LdvModelNodeArray aNodes) {
  	_aNodes = aNodes ;
  }

	/**
	 * Returns the node located at a given index 
	 * 
	 * @param iIndex index (from <code>0</code> to <code>size-1</code>)
	 * @return the corresponding LdvModelNode if found, <code>null</code> if not
	 * 
	 **/
	public LdvModelNode getNodeAtIndex(int iIndex) throws IndexOutOfBoundsException
	{
		if ((iIndex < 0) || (iIndex >= _aNodes.size()))
			throw new IndexOutOfBoundsException() ;
		
		return _aNodes.get(iIndex) ;
	}
}
