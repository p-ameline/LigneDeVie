package com.ldv.client.util;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvModelGraph;
import com.ldv.shared.graph.LdvModelProjectGraph;
import com.ldv.shared.graph.LdvModelTree;

public class LdvTreesManager 
{	
	private Vector<LdvModelTree> _aTrees ;
	
	public LdvTreesManager(LdvModelGraph graph)
	{
		if (null != graph)
			_aTrees = graph.getTrees() ;
		else
			_aTrees = null ;
	}
	
	public LdvTreesManager(LdvModelProjectGraph projectGraph)
	{
		if (null != projectGraph)
			_aTrees = projectGraph.getTrees() ;
		else
			_aTrees = null ;
	}
	
	/**
	 *  Get a tree from its ID 
	 *  
	 * @param  sTreeId ID of tree to be found
	 * @return The tree if found, or null if not
	 *
	 */
	public LdvModelTree getTree(String sTreeId) 
	{
		if ((null == sTreeId) || "".equals(sTreeId))
			return null ;
		
		if ((null == _aTrees) || _aTrees.isEmpty())
			return null ;
				
		// Browsing through trees
		//
		for (Iterator<LdvModelTree> itr = _aTrees.iterator() ; itr.hasNext() ; )
		{
			LdvModelTree tree = itr.next() ;
			if (tree.getTreeID().equals(sTreeId))
				return tree ;
	  }
		
		return (LdvModelTree) null ;
	}
}
