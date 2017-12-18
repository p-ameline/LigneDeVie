package com.ldv.shared.graph ;

import junit.framework.TestCase ;

public class LdvModelTreeGwtTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvModelTree ldvTree = new LdvModelTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertEquals("", ldvTree.getDocumentID()) ;
  	
  	// Test comprehensive constructor
  	//
  	LdvModelTree ldvTree2 = new LdvModelTree("TId") ; 
  	assertNotNull(ldvTree2) ;
  	assertNotNull(ldvTree2.getNodes()) ;
  	  	
  	assertEquals("TId",  ldvTree2.getDocumentID()) ;
  } ;
  
  public void testSortingFunctions() 
  {
  	// Test default constructor
  	//
  	LdvModelTree ldvTree = new LdvModelTree() ; 
  	assertNotNull(ldvTree) ;
  	assertNotNull(ldvTree.getNodes()) ;
  	  	
  	assertEquals("", ldvTree.getDocumentID()) ;
  	
  	ldvTree.getNodes().addElement(new LdvModelNode("PId", "TId", "NId", "Ty", "Lex", "Cplt", "Cert", "Plur", "Unit", "FT", "+0003002", "Vis", "Int")) ;
  	ldvTree.getNodes().addElement(new LdvModelNode("PId", "TId", "NId", "Ty", "Lex", "Cplt", "Cert", "Plur", "Unit", "FT", "+0001001", "Vis", "Int")) ;
  	ldvTree.getNodes().addElement(new LdvModelNode("PId", "TId", "NId", "Ty", "Lex", "Cplt", "Cert", "Plur", "Unit", "FT", "+0002002", "Vis", "Int")) ;
  	ldvTree.getNodes().addElement(new LdvModelNode("PId", "TId", "NId", "Ty", "Lex", "Cplt", "Cert", "Plur", "Unit", "FT", "+0000000", "Vis", "Int")) ;
  	
  	ldvTree.sortByLocalisation() ;
  	
  	assertEquals("+0000000", ldvTree.getNodeAtIndex(0).getLocalisation()) ;
  	assertEquals("+0001001", ldvTree.getNodeAtIndex(1).getLocalisation()) ;
  	assertEquals("+0002002", ldvTree.getNodeAtIndex(2).getLocalisation()) ;
  	assertEquals("+0003002", ldvTree.getNodeAtIndex(3).getLocalisation()) ;
  	
  	// Test access functions
  	//
  	assertEquals(1, ldvTree.findFirstSonIndex(0)) ;
  	assertEquals(2, ldvTree.findFirstSonIndex(1)) ;
  	assertEquals(-1, ldvTree.findFirstSonIndex(2)) ;
  	
  	assertEquals(3, ldvTree.findFirstBrotherIndex(2)) ;
  	assertEquals(-1, ldvTree.findFirstBrotherIndex(3)) ;
  	assertEquals(-1, ldvTree.findFirstBrotherIndex(1)) ;
  	
  	assertEquals(2, ldvTree.findNodeIndex(2, 2)) ;
  	assertEquals(-1, ldvTree.findNodeIndex(0, 1)) ;
  }
}
