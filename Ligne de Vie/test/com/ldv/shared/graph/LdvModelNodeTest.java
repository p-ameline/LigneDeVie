package com.ldv.shared.graph ;

import junit.framework.TestCase ;

public class LdvModelNodeTest extends TestCase
{

  public void testConstructor() 
  {
  	// Test default constructor
  	//
  	LdvModelNode ldvNode = new LdvModelNode() ; 
  	assertNotNull(ldvNode) ;
  	  	
  	assertEquals("", ldvNode.getPersonId()) ;
  	assertEquals("", ldvNode.getTreeID()) ;
  	assertEquals("", ldvNode.getNodeID()) ;
  	assertEquals("", ldvNode.getType()) ;
  	assertEquals("", ldvNode.getLexicon()) ;
  	assertEquals("", ldvNode.getComplement()) ;
  	assertEquals("", ldvNode.getCertitude()) ;
  	assertEquals("", ldvNode.getPlural()) ;
  	assertEquals("", ldvNode.getUnit()) ;
  	assertEquals("", ldvNode.getFreeText()) ;
  	assertEquals("", ldvNode.getCoordinates()) ;
  	assertEquals("", ldvNode.getVisible()) ;
  	assertEquals("", ldvNode.getInterest()) ;
  	
  	// Test comprehensive constructor
  	//
  	LdvModelNode ldvModelNode2 = new LdvModelNode("0123456", "ABCDEF", "NodId", "Ty", "Lex", "Cplt", "Cert", 
  			                                          "Plur", "Unit", "FT", "Loc", "Vis", "Int") ; 
  	assertNotNull(ldvModelNode2) ;
  	  	
  	assertEquals("0123456",       ldvModelNode2.getPersonId()) ;
  	assertEquals("ABCDEF",        ldvModelNode2.getDocumentId()) ;
  	assertEquals("0123456ABCDEF", ldvModelNode2.getTreeID()) ;
  	assertEquals("NodId",         ldvModelNode2.getNodeID()) ;
  	assertEquals("Ty",   ldvModelNode2.getType()) ;
  	assertEquals("Lex",  ldvModelNode2.getLexicon()) ;
  	assertEquals("Cplt", ldvModelNode2.getComplement()) ;
  	assertEquals("Cert", ldvModelNode2.getCertitude()) ;
  	assertEquals("Plur", ldvModelNode2.getPlural()) ;
  	assertEquals("Unit", ldvModelNode2.getUnit()) ;
  	assertEquals("FT",   ldvModelNode2.getFreeText()) ;
  	assertEquals("Loc",  ldvModelNode2.getCoordinates()) ;
  	assertEquals("Vis",  ldvModelNode2.getVisible()) ;
  	assertEquals("Int",  ldvModelNode2.getInterest()) ;
  	
  	// Test simple node constructor
  	//
  	LdvModelNode ldvModelNode3 = new LdvModelNode("Lex") ; 
  	assertNotNull(ldvModelNode3) ;
  	  	
  	assertEquals("", ldvModelNode3.getPersonId()) ;
  	assertEquals("", ldvModelNode3.getTreeID()) ;
  	assertEquals("", ldvModelNode3.getNodeID()) ;
  	assertEquals("", ldvModelNode3.getType()) ;
  	assertEquals("Lex", ldvModelNode3.getLexicon()) ;
  	assertEquals("", ldvModelNode3.getComplement()) ;
  	assertEquals("", ldvModelNode3.getCertitude()) ;
  	assertEquals("", ldvModelNode3.getPlural()) ;
  	assertEquals("", ldvModelNode3.getUnit()) ;
  	assertEquals("", ldvModelNode3.getFreeText()) ;
  	assertEquals("", ldvModelNode3.getCoordinates()) ;
  	assertEquals("", ldvModelNode3.getVisible()) ;
  	assertEquals("", ldvModelNode3.getInterest()) ;
  	
  	// Test Lexicon and complement constructor
  	//
  	LdvModelNode ldvModelNode23 = new LdvModelNode("Lex", "Cplt") ; 
  	assertNotNull(ldvModelNode23) ;
  	  	
  	assertEquals("", ldvModelNode23.getPersonId()) ;
  	assertEquals("", ldvModelNode23.getTreeID()) ;
  	assertEquals("", ldvModelNode23.getNodeID()) ;
  	assertEquals("", ldvModelNode23.getType()) ;
  	assertEquals("Lex",  ldvModelNode23.getLexicon()) ;
  	assertEquals("Cplt", ldvModelNode23.getComplement()) ;
  	assertEquals("", ldvModelNode23.getCertitude()) ;
  	assertEquals("", ldvModelNode23.getPlural()) ;
  	assertEquals("", ldvModelNode23.getUnit()) ;
  	assertEquals("", ldvModelNode23.getFreeText()) ;
  	assertEquals("", ldvModelNode23.getCoordinates()) ;
  	assertEquals("", ldvModelNode23.getVisible()) ;
  	assertEquals("", ldvModelNode23.getInterest()) ;
  	
  	// Test Lexicon, value and unit constructor
  	//
  	LdvModelNode ldvModelNode24 = new LdvModelNode("Lex", "Val", "Unit") ; 
  	assertNotNull(ldvModelNode24) ;
  	  	
  	assertEquals("", ldvModelNode24.getPersonId()) ;
  	assertEquals("", ldvModelNode24.getTreeID()) ;
  	assertEquals("", ldvModelNode24.getNodeID()) ;
  	assertEquals("", ldvModelNode24.getType()) ;
  	assertEquals("Lex", ldvModelNode24.getLexicon()) ;
  	assertEquals("Val", ldvModelNode24.getComplement()) ;
  	assertEquals("", ldvModelNode24.getCertitude()) ;
  	assertEquals("", ldvModelNode24.getPlural()) ;
  	assertEquals("Unit", ldvModelNode24.getUnit()) ;
  	assertEquals("", ldvModelNode24.getFreeText()) ;
  	assertEquals("", ldvModelNode24.getCoordinates()) ;
  	assertEquals("", ldvModelNode24.getVisible()) ;
  	assertEquals("", ldvModelNode24.getInterest()) ;
  	
  	// Test copy constructor
  	//
  	LdvModelNode ldvModelNode4 = new LdvModelNode(ldvModelNode2) ; 
  	assertNotNull(ldvModelNode4) ;
  	
  	assertEquals("0123456",       ldvModelNode2.getPersonId()) ;
  	assertEquals("ABCDEF",        ldvModelNode4.getDocumentId()) ;
  	assertEquals("0123456ABCDEF", ldvModelNode4.getTreeID()) ;
  	assertEquals("NodId",         ldvModelNode4.getNodeID()) ;
  	assertEquals("Ty",   ldvModelNode4.getType()) ;
  	assertEquals("Lex",  ldvModelNode4.getLexicon()) ;
  	assertEquals("Cplt", ldvModelNode4.getComplement()) ;
  	assertEquals("Cert", ldvModelNode4.getCertitude()) ;
  	assertEquals("Plur", ldvModelNode4.getPlural()) ;
  	assertEquals("Unit", ldvModelNode4.getUnit()) ;
  	assertEquals("FT",   ldvModelNode4.getFreeText()) ;
  	assertEquals("Loc",  ldvModelNode4.getCoordinates()) ;
  	assertEquals("Vis",  ldvModelNode4.getVisible()) ;
  	assertEquals("Int",  ldvModelNode4.getInterest()) ;
  	
  }
  
  public void testGetIndice() 
  {
  	// Test getLine and getCol
  	//
  	LdvModelNode ldvNode = new LdvModelNode("PId", "TId", "NId", "Ty", "Lex", "Cplt", "Cert", 
  			                                          "Plur", "Unit", "FT", "+0002001", "Vis", "Int") ; 
  	assertNotNull(ldvNode) ;
  	  	
  	assertTrue(2 == ldvNode.getLine()) ;
  	assertTrue(1 == ldvNode.getCol()) ;
  	
  	ldvNode.setCoordinates("+00A2111") ;
  	assertTrue(622 == ldvNode.getLine()) ;
  	assertTrue(3907 == ldvNode.getCol()) ;
  	
  	// Test deprecated mode 
  	//
  	ldvNode.setCoordinates("A2110000") ;
  	assertTrue(622 == ldvNode.getLine()) ;
  	assertTrue(63 == ldvNode.getCol()) ;
  	
  	// test setIndice
  	//
  	ldvNode.setLine(3907) ;
  	ldvNode.setCol(622) ;
  	assertEquals("+01110A2", ldvNode.getCoordinates()) ;
  }
  
  public void testStaticSemanticsFunction() 
  {
  	String sTest1 = LdvModelNode.getSemanticLabel("ZPOMR1/0PRO11/PHTA01/KOUVR1/£T0;19.2DA021.$19980615130952|ZPOMR1/0PRO11/PHTA01/VQUAN1/VIGRA1/£N2;04.200001.$010") ; 
  	String sResu1 = "ZPOMR/0PRO1/PHTA0/KOUVR/£T0.2DA02.$19980615130952|ZPOMR/0PRO1/PHTA0/VQUAN/VIGRA/£N2.20000.$010" ;
  	assertTrue(sResu1.equals(sTest1)) ;
  }
  
  public void testSemantics() 
  {
  	// Test getLine and getCol
  	//
  	LdvModelNode ldvNode = new LdvModelNode("PId", "TId", "NId", "Ty", "LIDET1", "Cplt", "Cert", 
  			                                          "Plur", "Unit", "FT", "+0002001", "Vis", "Int") ; 
  	assertNotNull(ldvNode) ;
  	  	
  	assertTrue("LIDET".equals(ldvNode.getSemanticLexicon())) ;
  	
  	ldvNode.setLexicon("£LC231") ;
  	assertTrue("£LC".equals(ldvNode.getSemanticLexicon())) ;
  }
  
  public void testPoundsFunctions() 
  {
  	// Test getLine and getCol
  	//
  	LdvModelNode ldvNode = new LdvModelNode("PId", "TId", "NId", "Ty", "LIDET1", "Cplt", "Cert", 
  			                                          "Plur", "Unit", "FT", "+0002001", "Vis", "Int") ; 
  	assertNotNull(ldvNode) ;
  	  	
  	assertFalse(ldvNode.startsWithPound()) ;
  	assertEquals("", ldvNode.followsPound()) ;
  	
  	ldvNode.setLexicon("£LC231") ;
  	
  	assertTrue(ldvNode.startsWithPound()) ;
  	assertEquals("LC", ldvNode.followsPound()) ;
  }
  
  public void testMessageFunctions() 
  {
  	// Test getLine and getCol
  	//
  	LdvModelNode ldvNode = new LdvModelNode("PId", "TId", "NId", "Ty", "LIDET1", "Cplt", "Cert", 
  			                                          "Plur", "Unit", "FT", "+0002001", "Vis", "Int") ; 
  	assertNotNull(ldvNode) ;
  	  	
  	BBMessage message = new BBMessage() ;
  	message.initFromNode(ldvNode) ;
  	
  	LdvModelNode ldvNode2 = new LdvModelNode() ;
  	ldvNode2.initFromMessage(message) ;
  	
  	assertTrue(ldvNode.hasSameContent(ldvNode2)) ;
  }
  
  public void testIDFunctions() 
  {
  	// Test getLine and getCol
  	//
  	LdvModelNode ldvNode = new LdvModelNode("PId", "TId", "NId", "Ty", "LIDET1", "Cplt", "Cert", 
  			                                          "Plur", "Unit", "FT", "+0002001", "Vis", "Int") ; 
  	assertNotNull(ldvNode) ;
  	  	
  	ldvNode.setTreeID("0123456ABCDEF") ;
  	
  	assertTrue("0123456".equals(ldvNode.getPersonId())) ;
  	assertTrue("ABCDEF".equals(ldvNode.getDocumentId())) ;
  	
  	ldvNode.setPersonID("1234567") ;
  	assertTrue("1234567ABCDEF".equals(ldvNode.getTreeID())) ;
  	
  	ldvNode.setDocumentID("BCDEFG") ;
  	assertTrue("1234567BCDEFG".equals(ldvNode.getTreeID())) ;
  }
}
