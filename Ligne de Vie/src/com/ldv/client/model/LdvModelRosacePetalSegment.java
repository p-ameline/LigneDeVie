package com.ldv.client.model;

import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.LdvNum;

/** 
 * Petal segment: radius, color, left and
 * 
 */
public class LdvModelRosacePetalSegment implements Cloneable
{
	enum MemberType { person, team } ;
	
	protected int    _iRadius ;
	protected String _sColor ;
			
	/** 
	 * Default constructor 
	 */
	public LdvModelRosacePetalSegment()
	{
		reset() ; 
	}
	
	/** 
	 * Copy constructor 
	 * 
	 * @param src model to initialize this object from
	 */
	public LdvModelRosacePetalSegment(final LdvModelRosacePetalSegment src)
	{
		deepCopy(src) ;
	}
	
	void reset()
	{	
		_iRadius = -1 ;
		_sColor  = "" ;
	}
	
	/** 
	 * Copy local informations and duplicate mandates 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public void deepCopy(final LdvModelRosacePetalSegment src)
	{
		if (null == src)
		{
			reset() ;
			return ;
		}
		
		_iRadius = src._iRadius ;
		_sColor  = src._sColor ;
	}
		
	public boolean isEmpty()
	{
		if (-1 == _iRadius)
			return true ;
		return false ;
	}

	/**
	*  Reset this object, then parse branches of a rosace petal inside a tree in order to initialize it
	*  
	*  @param tree     Rosace models library tree
	*  @param rootNode Petal segment root node
	*  
	*  @return <code>true</code> if parsing succeeded and result in a non empty object, else <code>false</code> 
	**/
	public boolean initFromTree(final LdvModelTree tree, final LdvModelNode rootNode)
	{
		reset() ;
		
		if ((null == tree) || (null == rootNode))
			return false ;
		
		String sSemanticConcept = rootNode.getSemanticLexicon() ;
		if (false == sSemanticConcept.equals("0ROSE"))
			return false ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			sSemanticConcept = sonNode.getSemanticLexicon() ;

			if     (sSemanticConcept.equals("VDIPA"))
			{
				LdvNum distance = new LdvNum() ;
				tree.getNum(sonNode, distance) ;
				if (distance.existValue())
					_iRadius = (int) distance.getValue() ;
			}
			
			else if (sSemanticConcept.equals("0COUL"))
				_sColor = tree.getFreeText(sonNode) ;
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
		return (false == isEmpty()) ;
	}
	
	public int getRadius() {
  	return _iRadius ;
  }
	public void setRadius(final int iRadius) {
		_iRadius = iRadius ;
  }
	
	public String getColor() {
  	return _sColor ;
  }
	public void setColor(final String sColor) {
		_sColor = sColor ;
  }
}
