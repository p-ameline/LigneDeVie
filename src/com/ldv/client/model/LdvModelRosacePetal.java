package com.ldv.client.model;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;
import com.ldv.shared.model.LdvNum;

/** 
 * Rosace petal: angle (plus angle left and right for drawing) and vector of petal segments
 * 
 */
public class LdvModelRosacePetal implements Cloneable
{
	protected String _sMembersCategory ;
	protected String _sLabel ;
	protected int    _iAngle ;  
	protected int    _iLeftRosaceAngleLdvD ;   // Left and right regarding text writing
	protected int    _iRightRosaceAngleLdvD ;
	
	protected Vector<LdvModelRosacePetalSegment> _aSegments = new Vector<LdvModelRosacePetalSegment>() ;
		
	public LdvModelRosacePetal()
	{
		reset() ; 
	}
	
	/** 
	 * Copy constructor 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public LdvModelRosacePetal(final LdvModelRosacePetal src)
	{
		deepCopy(src) ;
	}
	
	void reset()
	{	
		_sMembersCategory      = "" ;
		_sLabel                = "" ;
		_iAngle                = -1 ;
		_iLeftRosaceAngleLdvD  = -1 ;
		_iRightRosaceAngleLdvD = -1 ;
		
		_aSegments.clear() ;
	}
	
	/** 
	 * Copy local informations and duplicate petal segments 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public void deepCopy(final LdvModelRosacePetal src)
	{
		if (null == src)
		{
			reset() ;
			return ;
		}
		
		_sMembersCategory      = src._sMembersCategory ;
		_sLabel                = src._sLabel ;
		_iAngle                = src._iAngle ;
		_iLeftRosaceAngleLdvD  = src._iLeftRosaceAngleLdvD ;
		_iRightRosaceAngleLdvD = src._iRightRosaceAngleLdvD ;
	
		setSegments(src.getSegments()) ;
	}
	
	public boolean isEmpty()
	{
		if (_iAngle >= 0)
			return false ;
		return true ;
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
		if (false == sSemanticConcept.equals("0PETA"))
			return false ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			sSemanticConcept = sonNode.getSemanticLexicon() ;

			if     (sSemanticConcept.equals("0TYPM"))
			{
				LdvModelNode catNode = tree.findFirstSon(sonNode) ;
				if (null != catNode)
					_sMembersCategory = catNode.getLexicon() ;
			}
				
			else if (sSemanticConcept.equals("LNOMA"))
				_sLabel = tree.getFreeText(sonNode) ;
			
			else if (sSemanticConcept.equals("VANPA"))
			{
				LdvNum distance = new LdvNum() ;
				tree.getNum(sonNode, distance) ;
				if (distance.existValue())
					_iAngle = (int) distance.getValue() ;
			}
			else if (sSemanticConcept.equals("VANLE"))
			{
				LdvNum distance = new LdvNum() ;
				tree.getNum(sonNode, distance) ;
				if (distance.existValue())
					_iLeftRosaceAngleLdvD = (int) distance.getValue() ;
			}
			else if (sSemanticConcept.equals("VANRI"))
			{
				LdvNum distance = new LdvNum() ;
				tree.getNum(sonNode, distance) ;
				if (distance.existValue())
					_iRightRosaceAngleLdvD = (int) distance.getValue() ;
			}
			else if (sSemanticConcept.equals("0ROSE"))
			{
				LdvModelRosacePetalSegment petalSegment = new LdvModelRosacePetalSegment() ;
				if (petalSegment.initFromTree(tree, sonNode))
					addSegment(petalSegment) ;
			}
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
		return (false == isEmpty()) ;
	}
	
	public String getMembersCategory() {
  	return _sMembersCategory ;
  }
	public void setMembersCategory(final String sMembersCategory) {
		_sMembersCategory = sMembersCategory ;
  }
	
	public String getLabel() {
  	return _sLabel ;
  }
	public void setLabel(final String sLabel) {
		_sLabel = sLabel ;
  }

	public int getAngle() {
  	return _iAngle ;
  }
	public void setAngle(final int iAngle) {
		_iAngle = iAngle ;
  }

	public Vector<LdvModelRosacePetalSegment> getSegments() {
  	return _aSegments ;
  }
	
	/** 
	 * Add a segment to the segments vector (note that it is not duplicated but added) 
	 * 
	 * @param segment segment to be added
	 * 
	 */
	public void addSegment(LdvModelRosacePetalSegment segment)
  {
		if (null == segment)
			return ;
		
		_aSegments.add(segment) ;
  }
	
	/** 
	 * Initialize segments by duplicating the content of the source vector 
	 * 
	 * @param Segments vector of segments to initialize local ones
	 * 
	 */
  public void setSegments(final Vector<LdvModelRosacePetalSegment> Segments)
  {
  	_aSegments.clear() ;
		
		Iterator<LdvModelRosacePetalSegment> itr = Segments.iterator() ; 
		while (itr.hasNext()) 
			_aSegments.add(new LdvModelRosacePetalSegment(itr.next())) ;
  }

  public int getLeftRosaceAngleLdvD() {
  	return _iLeftRosaceAngleLdvD ;
  }
	public void setLeftRosaceAngleLdvD(final int iRosaceAngle) {
		_iLeftRosaceAngleLdvD = iRosaceAngle ;
  }
	
	public int getRightRosaceAngleLdvD() {
  	return _iRightRosaceAngleLdvD ;
  }
	public void setRightRosaceAngleLdvD(final int iRosaceAngle) {
		_iRightRosaceAngleLdvD = iRosaceAngle ;
  }	
}
