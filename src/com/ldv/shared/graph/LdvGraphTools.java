package com.ldv.shared.graph ;

import java.util.Date;
// import java.sql.*;
// import java.io.*;

import com.ldv.shared.util.MiscellanousFcts;

public class LdvGraphTools implements LdvGraphConfig 
{
	// private static String           defaultDateStr    = "yyyyMMddHHmmss" ;
  // private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat(defaultDateStr) ;
    
  // private static String           shortDateStr    = "yyyyMMdd" ;
  // private static SimpleDateFormat shortDateFormat = new SimpleDateFormat(shortDateStr) ;
    
	/**
	 * Return a tree ID from a person ID and a document ID 
	 * 
	 **/
	static public String getTreeId(final String sPersonId, final String sDocumentId)
	{
		if ((null == sPersonId) || (null == sDocumentId))
			return "" ;
		
		return sPersonId + sDocumentId ;
	}
	
	/**
	 * Return the person ID from a tree ID 
	 * 
	 **/
	static public String getTreePersonId(final String sTreeId)
	{
		if ((null == sTreeId) || (sTreeId.length() < LdvGraphConfig.PERSON_ID_LEN))
			return "" ;
		
		return sTreeId.substring(0, LdvGraphConfig.PERSON_ID_LEN) ;
	}
	
	/**
	 * Return the tree ID from a tree ID 
	 * 
	 **/
	static public String getTreeDocumentId(final String sTreeId)
	{
    if ((null == sTreeId) || (sTreeId.length() != LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN))
			return "" ;

		return sTreeId.substring(LdvGraphConfig.PERSON_ID_LEN, LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN) ;
	}
	
	/**
	 * Return the tree ID from a full node ID 
	 */
	static public String getNodeTreeId(final String fullNodeId)
	{
    if ((null == fullNodeId) || (fullNodeId.length() < LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN))
			return "" ;

    return fullNodeId.substring(0, LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN) ;
	}
	
	/**
	 * Return the node ID from a full node ID 
	 */
	static public String getNodeNodeId(final String fullNodeId)
	{
    if ((null == fullNodeId) || (fullNodeId.length() != LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN + LdvGraphConfig.NODE_ID_LEN))
			return "" ;
    
    int iDocumentLen = LdvGraphConfig.PERSON_ID_LEN + LdvGraphConfig.DOCUMENT_ID_LEN ;

    return fullNodeId.substring(iDocumentLen, iDocumentLen + LdvGraphConfig.NODE_ID_LEN) ;
	}
	
	/**
	 * Return the sub part of a term's code that represents the code of the related concept 
	 * 
	 **/
	static public String getSenseCode(final String lexiconCode)
	{
		if (null == lexiconCode)
			return "" ;
		if (lexiconCode.length() <= LdvGraphConfig.LEXI_SENS_LEN)
			return lexiconCode ;
		
		return lexiconCode.substring(0, LdvGraphConfig.LEXI_SENS_LEN) ;
	}
	
/*
	static public boolean isSavingTree(Tree tree, String[] treesArray)
	//================================================================
	{
		String codeSens = getSenseCode(tree.nodes[0].LEXICON) ;
		return isElementInStringArray(codeSens, treesArray) ;		
	}
*/
	
/*
	static public boolean isPerson(String id)
	//========================================
	{
// System.err.println("id in isPers == " + id + " len == " + id.length() + " char == " + id.substring(0,1));
		if (null == id)
			return false ;
		
		if (id.length() < LdvGraphConfig.PERSON_ID_LEN)
			return false ;
		
		if ((id.length() == LdvGraphConfig.OBJECT_ID_LEN) && ((id.substring(0,1)).equals(LdvGraphConfig.OBJECT_CHAR)))
			return false ;
			
		return true ;
	}
*/
	
/*
	public static String verifyInputData(String tagName, DOMElement inputTree, DOMElement outTree, String errCode)
	{
		if (null == tagName)
			return null ;
		
		String res = null ;
		DOMElement resErr = Pilot.newDOMTree("error") ;
System.err.println("tagName = " + tagName);		
		if (inputTree.getFirstElement(tagName) != null)
			res = inputTree.getFirstElement(tagName).getFirstText() ;
		else
		{
			resErr.addText(errCode);
			outTree.appendChild(resErr);
			return null;
		}
		return res;
	}
*/

/*	// -------------------------------------------------------------------------
	// sort a tree by node's localisation
	// ------------------------------------------------------------------------- */
/*
	public static void sortByLocalisation(Tree theTree)
	//=================================================
	{
		Node[] nodes = theTree.nodes ;
		
		int len	= nodes.length ;
		for (int i = len - 1 ; i >= 0 ; i--)
		{
			for (int j = 0 ; j < i ; j++)
			{
				if ((nodes[j].LOCALISATION).compareTo(nodes[j+1].LOCALISATION) > 0)
				{
					Node temp	= nodes[j] ;
					nodes[j]	= nodes[j+1] ;
					nodes[j+1]	= temp ;
				}
			}
		}
		return ;
	}
*/

	/**
	 * Is the tree an "in memory" tree, that's to say a tree that has never been saved
	 * 
	 * @param tree        LdvModelTree object to be evaluated
	 * @param isPerson    <code>true</code> if the tree belongs to a person's graph, <code>false</code> if it belongs to an object's graph
	 * 
	 * @return <code>true</code> if it is "in memory", <code>false</code> if not, or the status is undecidable 
	 */
	public static boolean isInMemory(final LdvModelTree tree, boolean isPerson)
	{
		if ((null == tree) || (null == tree.getTreeID()))
			return false ;
		
		String sRealTreeId = "" ;
		if (isPerson)
			sRealTreeId = getTreeDocumentId(tree.getTreeID()) ;
		else
			sRealTreeId = tree.getTreeID() ;
		
		if ((null == sRealTreeId) || "".equals(sRealTreeId))
			return false ;
		
		return (sRealTreeId.charAt(0) == MEMORY_CHAR) ;
	}
	
	/**
	 * Is the node Id an "in memory" node Id, that's to say a node that has never been saved
	 * 
	 * @param sNodeId     Id of node to be evaluated
	 * 
	 * @return <code>true</code> if it is "in memory", <code>false</code> if not, or the status is undecidable 
	 */
	public static boolean isInMemoryNode(final String sNodeId)
	{
		if ((null == sNodeId) || "".equals(sNodeId))
			return false ;
		
		return (sNodeId.charAt(0) == MEMORY_CHAR) ;
	}
	
	/**
	 * Return true if id(0) and id(7) are global type identifiers.
	 *
	 */
	public static boolean isGlobalTypeIdentifier(String id, char[] tempChars)
	//=======================================================================
	{
		boolean isGlobal = false;
		int i=0;
		while((i<tempChars[i]) && (!isGlobal))
		{
			if( id.charAt(0) == tempChars[i] ) 
			{
				int len = id.length();
				if(len == 7)
					isGlobal = true;
				if((len == 13) && (( id.charAt(7) == tempChars[i] )))
					isGlobal = true;
				if((len == 18) && (( id.charAt(7) == tempChars[i] ))&&
						(( id.charAt(13) == tempChars[i] )))
					isGlobal = true;		
			}
			
		}
		return isGlobal;
	}
	
	
	public static boolean isCollectivePerson(String personId)
	//=======================================================
	{
		if ((null == personId) || (personId.equals("")))
			return false ;
		
		if ((personId.charAt(0) == LdvGraphConfig.LOCAL_CHAR)  ||
			  (personId.charAt(0) == LdvGraphConfig.MEMORY_CHAR) ||
			  (personId.charAt(0) == LdvGraphConfig.GROUP_CHAR))
			return false ;
		return true ;
	}
	
	public static boolean isGlobalTypePerson(String personId)
	//=======================================================
	{
		if ((null == personId) || (personId.equals("")))
			return false ;
		
		if ((personId.charAt(0) == LdvGraphConfig.LOCAL_CHAR) ||
			  (personId.charAt(0) == LdvGraphConfig.MEMORY_CHAR))		
			return false ;
		
		return true ;
	}

	/**
	 * A tree can represent a document in a graph or the description of an object.
	 * Objects IDs start with a '$'
	 *
	 * @return <code>true</code> if the ID has the format (size and beginning char) of an object, <code>false</code> if not
	 *
	 */
	public static boolean isObjectId(String sID)
	{
		if ((null == sID) || "".equals(sID))
			return false ;
		
		if (sID.length() != OBJECT_ID_LEN)
			return false ;
		
		if (sID.charAt(0) != OBJECT_CHAR)
			return false ;
		
		return true ;
	}
	
	public static boolean isLocalId(String id)
	//========================================
	{
		if ((null == id) || (id.equals("")))
			return false ;
		
		if ((id.charAt(0) == LdvGraphConfig.LOCAL_CHAR ) ||
			  (id.charAt(LdvGraphConfig.PERSON_ID_LEN) == LdvGraphConfig.LOCAL_CHAR ))
			return true ;
		
		return false ;
	}
	
	public static boolean isLocalNodeId(String id)
	//============================================
	{
		if( id.charAt(0) == LdvGraphConfig.LOCAL_CHAR ) 		
			return true;
		return false;
	}
	
	public static boolean isGroupId(String id)
	//========================================
	{
		if(( id.charAt(0) == LdvGraphConfig.GROUP_CHAR ) ||
			( id.charAt(7) == LdvGraphConfig.GROUP_CHAR ))
			return true;
		return false;
	}
	
	public static boolean isGroupNodeId(String id)
	//============================================
	{
		if( id.charAt(0) == LdvGraphConfig.GROUP_CHAR ) 		
			return true;
		return false;
	}

	/**
	 * Computes an increment in base 36.
	 * @param id the String id in base 36.
	 * @return the following id.
	 */
	static public String getNextId(final String id) throws NumberFormatException
	//====================================================================
	{
		if (null == id)
		  return null ;
		
		int len = id.length() ;
		if (0 == len)
		  return null ;
  
		StringBuffer nextId = new StringBuffer(id) ;	//make a copy of the id
		int i = len - 1 ;
		while (true)
		{
			char j = id.charAt(i) ;
			j++ ;
			if ((j >= '0' && j <= '9') || (j >= 'A' && j <= 'Z'))
			{
				nextId.setCharAt(i, j) ;
				break ;
			}
			else if (j > '9' && j < 'A')
			{
				nextId.setCharAt(i, 'A') ;
				break ;
			}
			else
			{
				nextId.setCharAt(i, '0') ;
				if (0 == i)
				  throw new NumberFormatException() ;
				i-- ;
			}
		}
		return nextId.toString() ;    
	}
	
	/**
	 * Get the initial node ID for an in-memory node (à la "#0000")
	 */
	public static String getFirstInMemoryNodeId()
	{
		String sFirstNodeId = MiscellanousFcts.getNChars(LdvGraphConfig.NODE_ID_LEN, '0') ;
		sFirstNodeId = MiscellanousFcts.replace(sFirstNodeId, 0, LdvGraphConfig.MEMORY_CHAR) ;
		return sFirstNodeId ;
	}
	
	/**
	 * Get the initial object ID for an unknown object ID (à la "#____________")
	 */
	public static String getUnknownObjectId()
	{
		String sUnknowObjectId = MiscellanousFcts.getNChars(LdvGraphConfig.OBJECT_ID_LEN, '_') ;
		sUnknowObjectId = MiscellanousFcts.replace(sUnknowObjectId, 0, LdvGraphConfig.MEMORY_CHAR) ;
		return sUnknowObjectId ;
	}
	
	/**
	 * Get the initial person ID for an unknown person ID (à la "#______")
	 */
	public static String getUnknownPersonId()
	{
		String sUnknowPersonId = MiscellanousFcts.getNChars(LdvGraphConfig.PERSON_ID_LEN, '_') ;
		sUnknowPersonId = MiscellanousFcts.replace(sUnknowPersonId, 0, LdvGraphConfig.MEMORY_CHAR) ;
		return sUnknowPersonId ;
	}
	
	/**
	 * Get the initial document ID for an unknown document ID (à la "#_____")
	 */
	public static String getUnknownDocumentId()
	{
		String sUnknowDocumentId = MiscellanousFcts.getNChars(LdvGraphConfig.DOCUMENT_ID_LEN, '_') ;
		sUnknowDocumentId = MiscellanousFcts.replace(sUnknowDocumentId, 0, LdvGraphConfig.MEMORY_CHAR) ;
		return sUnknowDocumentId ;
	}
	
	/**
	 * Get the initial document ID depending on server type (à la "000000", "~00000", "-00000")
	 */
	public static String getFirstDocumentId(int iServerType)
	{
		String sFirstDocumentId = MiscellanousFcts.getNChars(LdvGraphConfig.DOCUMENT_ID_LEN, '0') ;
		
		if ((LdvGraphConfig.COLLECTIVE_SERVER == iServerType) || (LdvGraphConfig.DIRECT_COLLECTIVE_SERVER == iServerType))
			return sFirstDocumentId ;
		
		if      (LdvGraphConfig.LOCAL_SERVER == iServerType)
			sFirstDocumentId = MiscellanousFcts.replace(sFirstDocumentId, 0, LdvGraphConfig.LOCAL_CHAR) ;
		else if ((LdvGraphConfig.GROUP_SERVER == iServerType) || (LdvGraphConfig.DIRECT_GROUP_SERVER == iServerType))
			sFirstDocumentId = MiscellanousFcts.replace(sFirstDocumentId, 0, LdvGraphConfig.GROUP_CHAR) ;
		
		return sFirstDocumentId ;
	}
	
	/**
	 * Is the ID to compare greater than the previous max?
	 */
	static boolean isIdGreaterThan(final String sToCompare, final String sPreviousMax)
	{
		// Longer means greater
		//
		if (sToCompare.length() > sPreviousMax.length())
			return true ;
		
		return (sToCompare.compareTo(sPreviousMax) > 0) ;
	}

	/**
   * This method gets the current date and returns it in minutes.
   */	
	static public long getDateInMinutes() 
	//=================================== 
	{
		Date theDate = new Date() ;
		return theDate.getTime() / 60000 ;	
	}	
	/**
    * This method gets the current date and returns it in the format YYYYMMDDHHMMSS.
    */
/*
	static public String getNautilusDate()
	//====================================  
	{
        return defaultDateFormat.format( new Date() );	
	}
*/
	/**
    * This method gets the current date and returns it in the format YYYYMMDD.
    */
/*
	static public String getNautilusShortDate() 
	//========================================= 
	{
        return shortDateFormat.format( new Date() );	
	}
*/
	
	/**
    * This method tranforms a DOMElement tree into a traits array
    */	  
/*
	static public Trait[] treeToArrayTrait(DOMElement tree) 
	//==================================================== 
	{
		int count = tree.getNbChildren("*");
		if (count == 0)
			return null;
		DOMElement[] nodeElem = tree.getChildrenElements("*");
		Trait[] info = new Trait[count];
		for (int i=0; i < count; i++)
		{
			DOMElement currentNode = nodeElem[i];
			String val = currentNode.getFirstText();
			int pos = val.indexOf("'");
						
			if(pos != -1)
			{						
				val = val.replaceAll("'", "\\\\\'");				
			}
			info[i] = new Trait(currentNode.getTagName(), val);
System.err.println("val in treeToArrayTrait =" + val);
		
		}
		return info;
        	
	}
	
	public static boolean isEmptyTrait(Trait[] traits)
	//================================================
	{
		if((traits.length==1) && (traits[0].name.equals("")) && 
			(traits[0].value.equals("")))
			return true;
		return false;
		
	}
*/
	
	/**
    * This method verify if the nodes valeurs contient "'"
    */
/*
	public static void verifyTree(DOMElement tree) 
	//============================================ 
	{
			
		int count = tree.getNbChildren("*");
		if (count == 0)
			return ;
		DOMElement[] nodeElem = tree.getChildrenElements("*");
		for(int i=0; i<nodeElem.length; i++)
		{
			String val = nodeElem[i].getFirstText();
			int pos = val.indexOf("'");
			if(pos != -1)
			{						
System.err.println("pos =" + pos);				
				val = val.replaceAll("'", "\\\\\'");
System.err.println("val apres =" + val);
			}
			nodeElem[i].addText(val);
		}
	}
	
	public static String getValidString(String val)
	//=============================================
	{	
		if(val.indexOf("'") != -1)
			return val.replaceAll("'", "\\\\\'");	
		return val;
	}	
	
	public static String getStrangeValidString(String val)
	//====================================================
	{	
		if(val.indexOf("'") != -1)
			return val.replaceAll("'", "\\\'");	
		return val;
	}
*/	
	
	/**For a vector with elements of string type
	 *
	 */
/*
	static public boolean isElementInVector(String elem, Vector<String> vect)
	//===============================================================
	{
		int len = vect.size() ;
		int i = 0 ;
		while ((i < len) && (false == elem.equals((String)vect.elementAt(i))))
			i++ ;
			
		if (i < len)
			return true ;
		return false ;
	}
	
	static public boolean isElementInStringArray(String elem, String[] array)
	//=======================================================================
	{
		int len = array.length ;
		int i = 0 ;
		while((i < len) && (false == elem.equals(array[i])))
		{
//System.err.println("lex = " + array[i]);			
			i++ ;
		}
		if (i < len)
			return true ;
		return false ;
	}
*/
				
	/**For a vector with elements of link type
	 *
	 */
/*
	static public boolean isLinkInVector(Link elem, Vector<Link> vect)
	//==========================================================
	{
		int len = vect.size() ;
		int i=0 ;
		while((i < len) && (false == isEqualLinks(elem, (Link)vect.elementAt(i))))
			i++ ;
			
		if (i < len)
			return true ;
		return false ;
	}
*/

	/**For a array with elements of rigth type
	 *
	 */
/*
	static public String getNodeRight(String nodeId, Right[] rights)
	//==============================================================
	{
		int len = rights.length;

		int i=0;
		while((i<len)&&(!nodeId.equals(rights[i].RIGHT)))
			i++;		
			
		if(i<len)
			return rights[i].NODE;
			
		return ""; 
	}
	
		
	static boolean isEqualLinks(Link link1, Link link2 )
  	//==================================================
  	{    	
  		if(((link1.QUALIFIER).equals(link2.QUALIFIER)) &&
  		   ((link1.QUALIFIED).equals(link2.QUALIFIED)) &&
  		   ((link1.LINK).equals(link2.LINK)) )
  		{
  			return true;
  		}

  		return false;  		
  	}  	
  		

	
	
	static public boolean isEquals(String[] array1, String[] array2)
	//=============================================================
	{
		int len1 = array1.length;
		int len2 = array2.length;
		boolean found = false;
		int j = 0;
		if( len1 != len2)
			return false;
		else
		{
			for(int i=0; i<len1;i++)
			{
				while((j<len1 ) && (!found))
					if(array1[i].equals(array2[j]))
						found = true;
					else
						j++;
				if(found == false)
					return false;
			}
									
		}
		return true;
	}
	static public boolean isEquals(Trait trait1, Trait trait2)
	{
		if(((trait1.name).equals(trait1.name)) && ((trait1.value).equals(trait1.value)))
			return true;
		
		return false;
	}
*/
	
	/**
	* This method returns a String representation in base 36
	* of a long in base 10
    */
	static public String convert36(long time) 
	{
		if ((time >= 0) && (time <= 9))
			return (Long.toString(time)) ;
				
		if (time == 10) return ("A") ;	
		if (time == 11) return ("B") ;
		if (time == 12) return ("C") ;
		if (time == 13) return ("D") ;
		if (time == 14) return ("E") ;
		if (time == 15) return ("F") ;
		if (time == 16) return ("G") ;
		if (time == 17) return ("H") ;
		if (time == 18) return ("I") ;
		if (time == 19) return ("J") ;
		if (time == 20) return ("K") ;
		if (time == 21) return ("L") ;
		if (time == 22) return ("M") ;
		if (time == 23) return ("N") ;
		if (time == 24) return ("O") ;
		if (time == 25) return ("P") ;
		if (time == 26) return ("Q") ;
		if (time == 27) return ("R") ;
		if (time == 28) return ("S") ;
		if (time == 29) return ("T") ;
		if (time == 30) return ("U") ;
		if (time == 31) return ("V") ;
		if (time == 32) return ("W") ;
		if (time == 33) return ("X") ;
		if (time == 34) return ("Y") ;
		if (time == 35) return ("Z") ;
			
		return (convert36((long)(time/36)) + convert36(time%36)) ;
	}
	
    /**
	* This method returns a long from a String representation in base 36.
	* The String is assumed to be on 5 characters.
    */
	static public long convert10( String time1 )
	//=========================================
	{
			
			if(time1 != null)
			{
				if (time1.length() == 5)
				{
					char[] timechar = time1.toCharArray() ;
					long tmp=0, j=0;	
							
					for (int i=0; i<5; i++)
					{	
						if ((timechar[i] >= '0') && (timechar[i] <= '9'))
						{
							j = timechar[i] - '0' ;
						}
						else if ((timechar[i] >= 'A') && (timechar[i] <= 'Z'))
						{
							j = timechar[i] - 'A' + 10 ;
						}
						tmp = tmp * 36 + j ;
					}
					return tmp;
				}
				else
				{ 
					System.err.println(" votre String d�passe 5 caract�res !!");
					return (0);
				}	
			}
			else
			{	
				return (0);
			}			
	}
	
	public static short max (short a , short b)
	//===================================
	{
		if (a <= b)
			return b ;
		else
			return a ;
	}
	
	public static String max (String a , String b)
	//============================================
	{
		if (a.compareTo(b) <= 0) 
			return b ;
		else
			return a ;
	}
	
	public static String min (String a , String b)
	//============================================
	{
		if (a.compareTo(b) <= 0) 
			return a;
		else
			return b;
	}
	
	public static void main(String args[]) 
	{
		System.out.println( getDateInMinutes() );
		// System.out.println( getNautilusDate() );
		System.out.println("  ---------------  Test getNextId ");
	}	
}
