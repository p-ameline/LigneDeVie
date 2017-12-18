package com.ldv.shared.graph ;

import junit.framework.TestCase ;

public class LdvGraphToolsGwtTest extends TestCase
{

  public void testIdMethods() 
  {
  	// Test getTreeId
  	//
  	assertEquals("-000057-0000G", LdvGraphTools.getDocumentId("-000057", "-0000G")) ;
  	assertEquals("",              LdvGraphTools.getDocumentId(null, "-0000G")) ;
  	assertEquals("",              LdvGraphTools.getDocumentId("-000057", null)) ;
  	assertEquals("",              LdvGraphTools.getDocumentId(null, null)) ;
  	
  	// Test getDocumentPersonId
  	//
  	assertEquals("-000057", LdvGraphTools.getDocumentPersonId("-000057-0000G")) ;
  	assertEquals("",        LdvGraphTools.getDocumentPersonId("-00005")) ;
  	assertEquals("",        LdvGraphTools.getDocumentPersonId(null)) ;
  	
  	// Test getPersonTreeId
  	//
  	assertEquals("-0000G", LdvGraphTools.getDocumentTreeId("-000057-0000G")) ;
  	assertEquals("",       LdvGraphTools.getDocumentTreeId("-000057-0000G-0006")) ;
  	assertEquals("",       LdvGraphTools.getDocumentTreeId("-000")) ;
  	assertEquals("",       LdvGraphTools.getDocumentTreeId(null)) ;
  }
  
  public void testLexiconMethods() 
  {
  	// Test getSenseCode
  	//
  	assertEquals("GCONS", LdvGraphTools.getSenseCode("GCONS1")) ;
  	assertEquals("",      LdvGraphTools.getSenseCode(null)) ;
  	assertEquals("GCONS", LdvGraphTools.getSenseCode("GCONS")) ;
  }
  
  public void testNextIdMethods() 
  {
  	// Test getSenseCode
  	//
  	assertNull(LdvGraphTools.getNextId(null)) ;
  	assertNull(LdvGraphTools.getNextId("")) ;
  	assertEquals("001", LdvGraphTools.getNextId("000")) ;
  	assertEquals("00A", LdvGraphTools.getNextId("009")) ;
  	assertEquals("010", LdvGraphTools.getNextId("00Z")) ;
  	assertEquals("100", LdvGraphTools.getNextId("0ZZ")) ;
  	
  	try {
  		assertNull(LdvGraphTools.getNextId("ZZZ")) ;
  		fail("Should raise an NumberFormatException"); 
  	}
  	catch (NumberFormatException e) {
    } 
  }
}

