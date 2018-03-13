package com.ldv.client.model;

import java.util.Iterator;
import java.util.Vector;

import com.ldv.shared.graph.LdvModelNode;
import com.ldv.shared.graph.LdvModelTree;

/** 
 * Rosace: vector of petals
 * 
 */
public class LdvModelRosace implements Cloneable
{
	protected String _sTeamCategory ;
	protected String _sID ;
	protected String _sLabel ;
	
	protected Vector<LdvModelRosacePetal> _aPetals = new Vector<LdvModelRosacePetal>() ;
		
	/** 
	 * Default constructor 
	 */
	public LdvModelRosace()
	{
		reset() ; 
	}
	
	/** 
	 * Copy constructor 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public LdvModelRosace(LdvModelRosace src)
	{
		deepCopy(src) ; 
	}
	
	void reset()
	{	
		_sTeamCategory = "" ;
		_sID           = "" ;
		_sLabel        = "" ;
		
		_aPetals.clear() ;
	}
	
	/** 
	 * Copy local informations and duplicate mandates 
	 * 
	 * @param src model to initialize this object from
	 * 
	 */
	public void deepCopy(LdvModelRosace src)
	{
		if (null == src)
		{
			reset() ;
			return ;
		}
		
		_sTeamCategory = src._sTeamCategory ;
		_sID           = src._sID ;
		_sLabel        = src._sLabel ;
	
		setPetals(src.getPetals()) ;
	}
	
	public boolean isEmpty()
	{
		if (false == "".equals(_sID))
			return false ;
		return true ;
	}

	/**
	*  Reset this object, then parse branches of a rosace inside a tree in order to initialize it
	*  
	*  @param tree     Rosace models library tree
	*  @param rootNode Rosace root node
	*  
	*  @return <code>true</code> if parsing succeeded and result in a non empty object, else <code>false</code> 
	**/
	public boolean initFromTree(final LdvModelTree tree, final LdvModelNode rootNode)
	{
		reset() ;
		
		if ((null == tree) || (null == rootNode))
			return false ;
		
		String sSemanticConcept = rootNode.getSemanticLexicon() ;
		if (false == sSemanticConcept.equals("0ROSA"))
			return false ;
		
		_sID = rootNode.getNodeURI() ;
		
		LdvModelNode sonNode = tree.findFirstSon(rootNode) ;
		while (null != sonNode)
		{
			sSemanticConcept = sonNode.getSemanticLexicon() ;

			if     (sSemanticConcept.equals("0TYPT"))
			{
				LdvModelNode catNode = tree.findFirstSon(sonNode) ;
				if (null != catNode)
					_sTeamCategory = catNode.getLexicon() ;
			}
			
			else if (sSemanticConcept.equals("LNOMA"))
				_sLabel = tree.getFreeText(sonNode) ;
			
			else if (sSemanticConcept.equals("0PETA"))
			{
				LdvModelRosacePetal petal = new LdvModelRosacePetal() ;
				if (petal.initFromTree(tree, sonNode))
					addPetal(petal) ;
			}
			
			sonNode = tree.findFirstBrother(sonNode) ;
		}
		
		return (false == isEmpty()) ;
	}
	
	public String getID() {
  	return _sID ;
  }
	public void setID(String sID) {
		_sID = sID ;
  }
	
	public String getTeamCategory() {
  	return _sTeamCategory ;
  }
	public void setTeamCategory(String sTeamCategory) {
		_sTeamCategory = sTeamCategory ;
  }
	
	public String getLabel() {
  	return _sLabel ;
  }
	public void setLabel(String sLabel) {
		_sLabel = sLabel ;
  }

	public Vector<LdvModelRosacePetal> getPetals() {
  	return _aPetals ;
  }
	
	/** 
	 * Add a petal to the petals vector (note that it is not duplicated but added) 
	 * 
	 * @param petal petal to be added
	 * 
	 */
	public void addPetal(LdvModelRosacePetal petal)
  {
		if (null == petal)
			return ;
		
		_aPetals.add(petal) ;
  }
	
	/** 
	 * Initialize petals by duplicating the content of the source vector 
	 * 
	 * @param Petals vector of petals to initialize local ones
	 * 
	 */
  public void setPetals(Vector<LdvModelRosacePetal> Petals)
  {
  	_aPetals.clear() ;
		
		Iterator<LdvModelRosacePetal> itr = _aPetals.iterator() ; 
		while (itr.hasNext()) 
		{
			LdvModelRosacePetal copyPetal = new LdvModelRosacePetal() ; 
			copyPetal.deepCopy(itr.next()) ; 
			_aPetals.add(copyPetal) ;
		}
  }
}
