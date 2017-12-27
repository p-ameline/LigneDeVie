package com.ldv.shared.graph;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
*  Tree of LdvModelNodes<br><br> 
*  This tree is sorted on nodes lines since LdvModelNode implements Comparable
*  
**/
public class LdvModelNodeArrayArray extends Vector<LdvModelNodeArray> implements IsSerializable
{
	private static final long serialVersionUID = -6269567398185725083L;

	public LdvModelNodeArrayArray()
	{ 
		super() ;
	}
	
	public LdvModelNodeArrayArray(final LdvModelNodeArrayArray src) {
		initFromModel(src) ;
	}
	
	public void init() {
		clear() ;
  }
		
	/**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final LdvModelNodeArrayArray src)
	{
		init() ;
		
		if ((null == src) || src.isEmpty())
			return ;

		for (Iterator<LdvModelNodeArray> itr = src.iterator() ; itr.hasNext() ; )
		{
			LdvModelNodeArray ModelNode = itr.next() ;
			add(new LdvModelNodeArray(ModelNode)) ;
		}
	}
	
	/**
	*  Are all contained LdvModelNodeArray empty?
	*  
	*  @return <code>true</code> if empty or only contains empty LdvModelNodeArray, <code>false</code> if not 
	**/
	public boolean hasEmptyContent()
	{
		if (isEmpty())
			return true ;
		
		for (Iterator<LdvModelNodeArray> itr = iterator() ; itr.hasNext() ; )
			if (itr.next().isEmpty())
				return false ;
		
		return false ;
	}
}
