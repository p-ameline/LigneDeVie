package com.ldv.shared.graph;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

import java.util.Vector;

/**
 * 
 * @author Dominique Sauquet
*/
public class NautilusNode 
{
	//XML tag
	public static String NODE       = "node";
	//XML attributes
	public static String NODE_ID    = "id" ;
	public static String TYPE       = "type" ;
	public static String LEXICON    = "lexique" ;
	public static String COMPLEMENT = "complement" ;
	public static String CERTITUDE  = "certitude" ;
	public static String PLURAL     = "pluriel" ;
	public static String UNIT       = "unite" ;  
  public static String FREE_TEXT  = "freetext" ;
  public static String LOC        = "loc" ; 
  public static String VISIBLE    = "visible" ;	  
  public static String INTEREST   = "interet" ;
	
  LdvModelNode node ;
	Element      elem ;
	
  public NautilusNode(LdvModelNode aNode)
  {
  	node = aNode ;
  }
    
  static public Vector<LdvModelNode> getDescendantNodes(String aTreeId, Element treeElem, String idCol)
  //========================================================================================
  {
    Vector<LdvModelNode> v = new Vector<LdvModelNode>() ;
    
    LdvModelNode aNode = (new NautilusNode(aTreeId, treeElem, idCol)).node ;
    v.addElement(aNode) ;
    	
    //=== tags node
    
    NodeList<Node> elems = treeElem.getChildNodes() ;
    
    int iListLen = elems.getLength() ;
	  if (iListLen > 0)
	  {
	  	idCol = LdvGraphTools.getNextId(idCol) ;
	  	
	  	for (int i = 0 ; i < iListLen ; i++)
	  	{
	  		Element nextElement = (Element) elems.getItem(i) ;	
				v.addAll( getDescendantNodes(aTreeId, nextElement, idCol) ) ;
			}
	  }
    
/*
    List elems = treeElem.getChildren(NautilusNode.NODE) ;
	  if (null != elems)
	  {
	  	idCol = LdvGraphTools.getNextId( idCol ) ;
	  	
	  	Iterator itr = elems.iterator() ; 
			while (itr.hasNext()) 
			{
				Element nextElement = (Element) itr.next() ;
				
				v.addAll( getDescendantNodes(aTreeId, nextElement, idCol) ) ;
			}
	  }
*/
	  return v ;
	}
	
  public NautilusNode(String aTreeId, Element treeElem)
  //======================================================
  {
  	// TODO fix this
/*
    node = new LdvModelNode( aTreeId,
    		treeElem.getAttribute(NODE_ID).getValue(),
    		treeElem.getAttribute(TYPE).getValue(),
    		treeElem.getAttribute(LEXICON).getValue(),
    		treeElem.getAttribute(COMPLEMENT).getValue(),
    		treeElem.getAttribute(CERTITUDE).getValue(),
    		treeElem.getAttribute(PLURAL).getValue(),
    		treeElem.getAttribute(UNIT).getValue(),
    		treeElem.getAttribute(FREE_TEXT).getValue(),
    		treeElem.getAttribute(LOC).getValue(),
    		treeElem.getAttribute(VISIBLE).getValue(),
    		treeElem.getAttribute(INTEREST).getValue()
    		);
*/
    elem = treeElem ;
    	
// System.err.println("######## node.LEXICON :" + node.LEXICON + " FREE_TEXT :" + node.FREE_TEXT);
	}
  
	public NautilusNode(String aTreeId, Element treeElem, String idCol)
	//====================================================================
  {
    // String loc = idCol + "0000" ;
    	
    // TODO fix this
/*
    node = new LdvModelNode( aTreeId,
    		treeElem.getAttribute(NODE_ID).getValue(),
    		treeElem.getAttribute(TYPE).getValue(),
    		treeElem.getAttribute(LEXICON).getValue(),
    		treeElem.getAttribute(COMPLEMENT).getValue(),
    		treeElem.getAttribute(CERTITUDE).getValue(),
    		treeElem.getAttribute(PLURAL).getValue(),
    		treeElem.getAttribute(UNIT).getValue(),
    		treeElem.getAttribute(FREE_TEXT).getValue(),
    		loc,
    		treeElem.getAttribute(VISIBLE).getValue(),
    		treeElem.getAttribute(INTEREST).getValue()
    		);
*/
    elem = treeElem ;
    	
// System.err.println("######## node.LEXICON :" + node.LEXICON + " LOC :" + node.LOCALISATION);
	}
	
  /*
    public String getFreeText()
    {
    	return getFreeText(node);
    }

 	static public String getFreeText(Node aNode)
    {
    	if ( aNode.LEXICON.equals("£?????") || aNode.LEXICON.startsWith("£CL") )
    	  return aNode.FREE_TEXT;
    }
   */
  
	public Element getElement()
  {
		if (null != elem)
			return elem ;
    // elem = getElement(node) ;
    // return elem ;
		return null ;
	}
	
	/**
   * Creates a DOM Element with the node information :
   *  TREE_ID, NODE, TYPE, LEXICON, COMPLEMENT, CERTITUDE, PLURIEL	
	 *  UNIT, FREE_TEXT, LOCALISATION, VISIBLE, INTEREST
	 */
/*
	static public Element getElement(LdvModelNode aNode)
  {
		Element el = new Element(NODE) ;
    	
    el.setAttribute(NODE_ID, aNode.getNodeID()) ;
    	
    if (false == aNode.getType().equals(""))
    	el.setAttribute(TYPE, aNode.getType()) ;
    	
    if (false == aNode.getLexicon().equals(""))
    	el.setAttribute(LEXICON, aNode.getLexicon()) ;
    	
    if (false == aNode.getComplement().equals(""))
    	el.setAttribute(COMPLEMENT, aNode.getComplement()) ;
    	  
    if (false == aNode.getCertitude().equals(""))
    	el.setAttribute(CERTITUDE, aNode.getCertitude()) ;
    	
    if (false == aNode.getPlural().equals(""))
    	el.setAttribute(PLURAL, aNode.getPlural()) ;
    	  
    if (false == aNode.getUnit().equals(""))
    	el.setAttribute(UNIT, aNode.getUnit()) ;
    	  
    if (false == aNode.getFreeText().equals(""))
    	el.setAttribute(FREE_TEXT, aNode.getFreeText()) ;
    	  
    if (false == aNode.getLocalisation().equals(""))
    	el.setAttribute(LOC, aNode.getLocalisation()) ;
    	  
    if (false == aNode.getVisible().equals(""))
    	el.setAttribute(VISIBLE, aNode.getVisible()) ;
    	  
    if (false == aNode.getInterest().equals(""))
    	el.setAttribute(INTEREST, aNode.getInterest()) ;
    	  
    return el ;
	}
*/
	
	public String toString()
	{
		try {
			if (null != elem)
				return elem.toString() ;
			
			return null ;
			// return getElement().toString() ;
		}
		catch(Exception e) {
			return null ;
		}
	}
}
