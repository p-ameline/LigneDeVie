package com.ldv.client.bigbro;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvModelNodeArray;

public class BBVectFatheredPatPathoArray extends Vector<BBFatheredPatPathoArray> 
{
	private static final long serialVersionUID = -9085375698306760848L;

	public BBVectFatheredPatPathoArray() {
	}

	public BBVectFatheredPatPathoArray(final BBVectFatheredPatPathoArray src) {
		initFromModel(src) ;
	}

	public void addFatheredPatho(BBFatheredPatPathoArray patho)
	{
		if (null == patho)
			return ;
		
		addElement(patho) ;
	}
	
	/**
	*  Are all contained PatPatho empty? 
	*  
	*  @return true if empty or if all contained PatPatho are empty
	**/
	public boolean hasEmptyContent()
	{
		if (isEmpty())
			return true ;

		for (Iterator<BBFatheredPatPathoArray> itr = iterator() ; itr.hasNext() ; )
		{
			BBFatheredPatPathoArray FPP = itr.next() ;
			LdvModelNodeArray PatPatho = FPP.getPatPatho() ;
			if ((null != PatPatho) && (false == PatPatho.isEmpty()))
				return false ;
		}
		
		return true ;
	}
		
  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBVectFatheredPatPathoArray src)
	{
		clear() ;
		
		if ((null == src) || src.isEmpty())
			return ;

		for (Iterator<BBFatheredPatPathoArray> itr = src.iterator() ; itr.hasNext() ; )
		{
			BBFatheredPatPathoArray FPP = itr.next() ;
			addElement(new BBFatheredPatPathoArray(FPP)) ;
		}
	}

	/**
	  * Determine whether two BBMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other BBFatheredPatPathoArray to compare with
	  * 
	  */
	public boolean equals(BBVectFatheredPatPathoArray other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		// return (_PatPatho.equals(other._PatPatho)         && 
		//				_FatherNode.equals(other._FatherNode)) ;
		return true ;
	}

	/**
	  * Determine whether an object is exactly similar to this
	  * 
	  * @return true if all data are the same, false if not
	  * @param o Object to compare
	  * 
	  */
	public boolean equals(Object o) 
	{
		if (this == o) {
			return true ;
		}
		if (null == o || getClass() != o.getClass()) {
			return false;
		}

		final BBVectFatheredPatPathoArray itemData = (BBVectFatheredPatPathoArray) o ;

		return equals(itemData) ;
	}
}
