package com.ldv.client.bigbro;

import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelNodeArray;

public class BBFatheredPatPathoArray 
{
	protected LdvModelNodeArray _PatPatho ;
	protected LdvModelNode      _FatherNode ;

	public BBFatheredPatPathoArray() {
		init() ;
	}

	public BBFatheredPatPathoArray(LdvModelNode fatherNode, LdvModelNodeArray patPatho) 
	{
		_PatPatho   = patPatho ;
  	_FatherNode = fatherNode ;
	}
	
	public BBFatheredPatPathoArray(final BBFatheredPatPathoArray src) {
		initFromModel(src) ;
	}

  /**
	*  Equivalent of = operator
	*  
	*  @param src Object to initialize from 
	**/
	public void initFromModel(final BBFatheredPatPathoArray src)
	{
		init() ;
		
		if (null == src)
			return ;

		_PatPatho   = new LdvModelNodeArray(src._PatPatho) ;
		_FatherNode = new LdvModelNode(src._FatherNode) ;
	}
	
  public void init() 
  {
  	_PatPatho   = null ;
  	_FatherNode = null ;
  }
  
	public LdvModelNodeArray getPatPatho() {
		return _PatPatho ;
	}
	public void setPatPatho(LdvModelNodeArray PatPatho)
	{
		_PatPatho = new LdvModelNodeArray(PatPatho) ;
	}

	public LdvModelNode getFatherNode() {
		return _FatherNode ;
	}
	public void setFatherNode(LdvModelNode FatherNode) {
		_FatherNode = new LdvModelNode(FatherNode) ;
	}
	
	/**
	  * Determine whether two BBMessage are exactly similar
	  * 
	  * @return true if all data are the same, false if not
	  * @param  other BBFatheredPatPathoArray to compare with
	  * 
	  */
	public boolean equals(BBFatheredPatPathoArray other)
	{
		if (this == other) {
			return true ;
		}
		if (null == other) {
			return false ;
		}
		
		return (_PatPatho.equals(other._PatPatho)         && 
						_FatherNode.equals(other._FatherNode)) ;
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

		final BBFatheredPatPathoArray itemData = (BBFatheredPatPathoArray) o ;

		return equals(itemData) ;
	}
}
